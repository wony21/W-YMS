package com.compact.base.scheduler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.compact.base.domain.CTQ.CTQService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quickchart.QuickChart;

@Component
public class CTQScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CTQScheduler.class);

	@Autowired
	CTQService ctqService;

	@Value("${ctq.daily.mail.template}")
	String mailTmpl;

	@Value("${ctq.daily.mail.template.style}")
	String mailTmplStyle;
	@Value("${ctq.daily.mail.template.top.msg}")
	String mailTmplTopMsg;
	@Value("${ctq.daily.mail.template.data.table}")
	String mailTmplDataTable;
	@Value("${ctq.daily.mail.template.data.chart}")
	String mailTmplDataChart;

	@Value("${ctq.daily.mail.template.css}")
	String mailTmplCSS;
	@Value("${ctq.daily.mail.template.body.summary}")
	String mailSummaryTmplHtml;
	@Value("${ctq.daily.mail.template.body}")
	String mailTmplHtml;

	@Value("${chart.api.server}")
	String chartAPIServer;

	Boolean scheduleRunned = false;

	public String getMapVal(Map data, String key) {
		Object objVal = data.get(key);
		if (objVal == null) {
			return "";
		} else {
			return objVal.toString();
		}
	}

	/**
	 * DB 설정된 스케쥴 타임 값 불러오기
	 * 
	 * @return HHmm형식의 기준시간
	 */
	private Boolean isScheduleIsRunnableTime() {

		Date today = new Date();
		String hhmm = DateFormatUtils.format(today, "HHmm");
		String codeHour = "";
		String codeMinuate = "";
		String schedulerTime = "";
		List<Map<String, String>> scheduleTimes = ctqService.getCommonCode("SCHEDULE-TIME");
		for (Map<String, String> sch : scheduleTimes) {
			String timeName = getMapVal(sch, "code");
			String timeValue = getMapVal(sch, "value1");

			logger.info("timeName : {}", timeName);
			logger.info("timeValue : {}", timeValue);

			if (timeName.equals("HOUR")) {
				codeHour = timeValue;
			} else if (timeName.equals("MINUATE")) {
				codeMinuate = timeValue;
			}
		}

		logger.info("Scheduler time is [{}{}] => Current time is [{}]", new Object[] { codeHour, codeMinuate, hhmm });

		if (codeHour.isEmpty() || codeMinuate.isEmpty()) {
			logger.error("Invalid Schedule Time Configuration");
			return false;
		}

		schedulerTime = String.format("%s%s", codeHour, codeMinuate);

		// 설정한 시간에서만 동작 하도록 설정
		if (!schedulerTime.equals(hhmm)) {
			logger.info("Not runnable scheduler time. [SCHEDULE END] : {}{}", schedulerTime, hhmm);
			return false;
		}

		return true;
	}

	public int getTableRowCountLimit() {

		int defaultRowCount = 15;
		int rowCount = 0;
		List<Map<String, String>> scheduleTimes = ctqService.getCommonCode("ROWCOUNT");

		for (Map<String, String> sch : scheduleTimes) {
			String value1 = getMapVal(sch, "value1");
			try {
				rowCount = Integer.parseInt(value1);
			} catch (Exception e) {
				rowCount = defaultRowCount;
			}
		}

		if (rowCount <= 0) {
			rowCount = defaultRowCount;
		}

		return rowCount;
	}

	/**
	 * 설정된 집계 방식 가져오기
	 * 
	 * @return 1 : 월 1일부터 현재까지 2 : 최근 시간 30일 기준
	 */
	private String getSummaryType() {
		String optionSummary = "";
		List<Map<String, String>> summaryOpts = ctqService.getCommonCode("SUMMARY-TYPE");
		for (Map<String, String> opt : summaryOpts) {
			String enable = getMapVal(opt, "value1");
			String code = getMapVal(opt, "code");
			if (enable.equals("Y")) {
				optionSummary = code;
				break;
			}
		}
		logger.info("OPTION SUMMARY-TYPE ::: " + optionSummary);
		// default 1
		if (optionSummary.isEmpty())
			optionSummary = "1";

		return optionSummary;
	}

	/**
	 * NG만 집계 Flag
	 * 
	 * @return Y : NG만 집계 N : 모두 집계
	 */
	private String getNGFlag() {
		String optionNG = "";
		List<Map<String, String>> ngFlag = ctqService.getCommonCode("ONLY-NG");
		optionNG = getMapVal(ngFlag.get(0), "value1");

		// default N
		if (optionNG.isEmpty())
			optionNG = "N";

		return optionNG;
	}

	/**
	 * 자동 메일 발송 스케쥴러 실행 부
	 * 
	 * @throws Exception
	 */
	//@Scheduled(cron = "${sch.ctq.daily.cron}")
	public void CTQDailyMail() throws Exception {

		// DB에 설정된 시간에 동작 하도록
		if (!isScheduleIsRunnableTime())
			return;

		if (scheduleRunned)
			return;

		scheduleRunned = true;

		logger.info("[CTQDailyMail] Scheduling START");

		// 스케쥴 기준 정보 생성
		Date today = new Date();
		String mailDate = DateFormatUtils.format(today, "yyyyMMdd");
		String todayFormatStr = DateFormatUtils.format(today, "yyyy년 MM월 dd일");

		// Mail template
		StringBuffer htmlTemplete = new StringBuffer();
		String mail_CSS = readFileFromResourceFile(mailTmplCSS);
		String mail_Summary = readFileFromResourceFile(mailSummaryTmplHtml);
		String mailTmplHtmlStr = readFileFromResourceFile(mailTmplHtml);

		// Config Options
		String optionSummary = getSummaryType();
		String optionNG = getNGFlag();

		logger.info("Summary[{}] NG[{}]", optionSummary, optionNG);

		List<Map<String, String>> mailProductGroups = ctqService.getCommonCode("PRODUCTGROUP-MAIL");
		List<Map<String, String>> testProvs = ctqService.getCommonCode("TEST-PROV-VINA");
		List<Map<String, String>> mailProducts = ctqService.getCommonCode("PRODUCT-VINA");

		int rowCountLimit = getTableRowCountLimit();

		if (mailProductGroups.size() < 0 || testProvs.size() < 0) {
			logger.error("No set productGroupMail or TestProv..");
			return;
		}

		List<String> products = mailProducts.stream().map(m -> m.get("code")).collect(Collectors.toList());

		List<String> testProvCodeStrings = testProvs.stream().map(m -> m.get("code")).collect(Collectors.toList());

		// th
		String tagSummaryHtml = "";
		String smy_trth = "";
		int testProvCount = testProvs.size();
		for (Map<String, String> testProv : testProvs) {
			String testProvCode = testProv.get("code");
			String testProvName = testProv.get("value");
			smy_trth += "<th>" + testProvName + "</th>";
		}
		mail_Summary = mail_Summary.replace("<!--@@{TEST-PROV}-->", smy_trth);

		// td
		String smy_trtd = "";
		for (String product : products) {

			List<Map<String, String>> info = ctqService.getProductInfo(product);

			smy_trtd += "<tr>";
			smy_trtd += "<td>" + product + "</td>";

			if (info.size() > 0) {

				Map<String, String> infoObj = info.get(0);
				String itemGroup = getMapVal(infoObj, "itemGroup");
				String itemCode = getMapVal(infoObj, "itemCode");
				String itemName = getMapVal(infoObj, "itemName");

				smy_trtd += "<td>" + itemGroup + "</td>";
				smy_trtd += "<td>" + itemName + "</td>";

				for (Map<String, String> testProv : testProvs) {

					String testProvCode = testProv.get("code");
					String testProvName = testProv.get("value");

					List<Map<String, String>> ctqProductCountList = ctqService.getCTQProductDataCount(product,
							testProvCode, optionSummary, optionNG);
					int rowCount = ctqProductCountList.size();
					if (rowCount > 0) {
						for (Map<String, String> productCountObj : ctqProductCountList) {
							String dataCount = getMapVal(productCountObj, "cnt");
							smy_trtd += "<td>" + dataCount + "</td>";
							break;
						}
					} else {
						// smy_trtd += "<td colspan=\"" + (testProvCount) + "\">기간 내 데이터 미존재</td>";
						smy_trtd += "<td>기간 내 데이터 미존재</td>";
					}
				}

			} else {
				// smy_trtd += "<td colspan=\"" + (testProvCount + 2) + "\">존재하지 않는 제품</td>";
				smy_trtd += "<td>존재하지 않는 제품</td>";
				continue;
			}
			smy_trtd += "</tr>";
		}
		mail_Summary = mail_Summary.replace("<!--@@{TABLE}@@-->", smy_trtd);

		for (Map<String, String> productGroup : mailProductGroups) {

			for (Map<String, String> testProv : testProvs) {

				String productGroupName = productGroup.get("code");
				String testProvCode = testProv.get("code");

				// DB에서 데이터 읽어오기
				List<Map<String, Object>> ctqDataList = ctqService.getCTQMailData(productGroupName, testProvCode,
						optionSummary, optionNG, products);

				// Grouping
				Map<Object, List<Map<String, Object>>> groupData = ctqDataList.stream()
						.collect(Collectors.groupingBy((map) -> map.get("itemCode")));

				// 제품 코드별 표/차트 생성
				groupData.forEach((key, value) -> {

					StringBuffer htmlTable = new StringBuffer();
					StringBuffer htmlChartImage = new StringBuffer();
					String product = key.toString();
					List<Map<String, Object>> mapList = (List<Map<String, Object>>) value;
					int mapCount = mapList.size();
					int no = 1;

					for (Map<String, Object> ctqData : mapList) {

						String site = getMapVal(ctqData, "site");
						String ctqDate = getMapVal(ctqData, "ctqDate");
						String itemGroup = getMapVal(ctqData, "itemGroup");
						String itemCode = getMapVal(ctqData, "itemCode");
						String itemName = getMapVal(ctqData, "itemName");
						String item1 = getMapVal(ctqData, "item1");
						String item2 = getMapVal(ctqData, "item2");
						String chipName = getMapVal(ctqData, "chipName");
						String equip = getMapVal(ctqData, "equip");
						String minValue = getMapVal(ctqData, "minValue");
						String maxValue = getMapVal(ctqData, "maxValue");
						String cMinValue = getMapVal(ctqData, "cMixValue");
						String cMaxValue = getMapVal(ctqData, "cMaxValue");
						String avgValue = getMapVal(ctqData, "avgValue");
						String value1 = getMapVal(ctqData, "value1");
						String value2 = getMapVal(ctqData, "value2");
						String value3 = getMapVal(ctqData, "value3");
						String value4 = getMapVal(ctqData, "value4");
						String value5 = getMapVal(ctqData, "value5");
						String value6 = getMapVal(ctqData, "value6");
						String value7 = getMapVal(ctqData, "value7");
						String value8 = getMapVal(ctqData, "value8");
						String value9 = getMapVal(ctqData, "value9");
						String value10 = getMapVal(ctqData, "value10");
						String value11 = getMapVal(ctqData, "value11");
						String value12 = getMapVal(ctqData, "value12");
						String value13 = getMapVal(ctqData, "value13");
						String value14 = getMapVal(ctqData, "value14");
						String value15 = getMapVal(ctqData, "value15");
						String value16 = getMapVal(ctqData, "value16");
						String value17 = getMapVal(ctqData, "value17");
						String value18 = getMapVal(ctqData, "value18");
						String value19 = getMapVal(ctqData, "value19");
						String value20 = getMapVal(ctqData, "value20");
						String ng = getMapVal(ctqData, "qcokngReason");
						String trtd = "<tr>";
						trtd += String.format("<td>%s</td>", no);
						trtd += String.format("<td>%s</td>", site);
						trtd += String.format("<td>%s</td>", ctqDate);
						trtd += String.format("<td>%s</td>", itemGroup);
						trtd += String.format("<td>%s</td>", itemCode);
						trtd += String.format("<td>%s</td>", itemName);
						trtd += String.format("<td>%s</td>", item1);
						trtd += String.format("<td>%s</td>", item2);
						trtd += String.format("<td>%s</td>", chipName);
						trtd += String.format("<td>%s</td>", equip);
						trtd += String.format("<td>%s</td>", minValue);
						trtd += String.format("<td>%s</td>", maxValue);
						trtd += String.format("<td>%s</td>", cMinValue);
						trtd += String.format("<td>%s</td>", cMaxValue);
						trtd += String.format("<td>%s</td>", avgValue);
						trtd += String.format("<td>%s</td>", value1);
						trtd += String.format("<td>%s</td>", value2);
						trtd += String.format("<td>%s</td>", value3);
						trtd += String.format("<td>%s</td>", value4);
						trtd += String.format("<td>%s</td>", value5);
						trtd += String.format("<td>%s</td>", value6);
						trtd += String.format("<td>%s</td>", value7);
						trtd += String.format("<td>%s</td>", value8);
						trtd += String.format("<td>%s</td>", value9);
						trtd += String.format("<td>%s</td>", value10);
						trtd += String.format("<td>%s</td>", value11);
						trtd += String.format("<td>%s</td>", value12);
						trtd += String.format("<td>%s</td>", value13);
						trtd += String.format("<td>%s</td>", value14);
						trtd += String.format("<td>%s</td>", value15);
						trtd += String.format("<td>%s</td>", value16);
						trtd += String.format("<td>%s</td>", value17);
						trtd += String.format("<td>%s</td>", value18);
						trtd += String.format("<td>%s</td>", value19);
						trtd += String.format("<td>%s</td>", value20);
						trtd += String.format("<td>%s</td>", ng);
						trtd += "</tr>";

						// 설정된 행수보다 작으면 추가
						// 최근 날짜로 변경 20.11.17 박지훈 상무  
						if ((mapCount - rowCountLimit + 1) <= no) {
							htmlTable.insert(0, trtd);
						}

						no = no + 1;
					}

					if (mapCount > 0) {

						try {

							// Chart Image 생성
							ObjectMapper mapper = new ObjectMapper();
							String json = mapper.writeValueAsString(mapList);

							logger.debug("Json Data [" + json + "]");

							String fileName = "";

							URL url = new URL("http://" + chartAPIServer + "/makeChartImage");
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();

							connection.setRequestMethod("POST");
							connection.setRequestProperty("User-Agent", "Mozilla/5.0");
							connection.setRequestProperty("Content-Type", "application/json");
							connection.setDoOutput(true);

							DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
							outputStream.writeBytes(json);
							outputStream.flush();
							outputStream.close();

							int responseCode = connection.getResponseCode();

							if (responseCode == 200) {

								BufferedReader reader = new BufferedReader(
										new InputStreamReader(connection.getInputStream()));
								StringBuffer stringBuffer = new StringBuffer();
								String inputLine = "";

								while ((inputLine = reader.readLine()) != null) {
									stringBuffer.append(inputLine);
								}

								String response = stringBuffer.toString();

								logger.debug("POST : MakeChartImage => " + response);

								Map<String, String> responseMap = mapper.readValue(response, Map.class);

								fileName = responseMap.get("filename");
							} else {
								logger.error("POST : Response Code [%d]. Error", responseCode);
							}

							if (!fileName.isEmpty()) {
								String chartHtml = String.format(
										"<div><img src=\"http://" + chartAPIServer + "/image/%s\" /></div>", fileName);
								htmlChartImage.append(chartHtml);
							}

							String strBuf = mailTmplHtmlStr.replace("<!--@@{TABLE}@@-->", htmlTable.toString());
							strBuf = strBuf.replace("<!--@@{CHART}@@-->", htmlChartImage.toString());

							htmlTemplete.append(strBuf);

						} catch (Exception e) {
							// TODO: handle exception
							e.getStackTrace();
						}
					}
				});
			}
		}

		// Send Mail
		if (htmlTemplete.length() > 0) {

			String topMessage = todayFormatStr + "자 자동 알람 메일 입니다.<br>설정된 표 데이터 행수 제한은 (" + rowCountLimit
					+ ") 입니다.<br>(최대 측정 개수가 250개 초과인 경우, 최근 기준 250개까지 표시 됩니다.)";
			htmlTemplete.insert(0, mail_Summary);
			htmlTemplete.insert(0, "<div><p>" + topMessage + "</p></div>");
			htmlTemplete.insert(0, mail_CSS);
			// Insert mail result
			ctqService.reserveSendMail(mailDate, htmlTemplete.toString());
			// Send E-mail from DB
			ctqService.requestMailSend();
			logger.info("[CTQDailyMail] Send reservce mail.");
		} else {
			logger.warn("[CTQDailyMail] No has data.");
		}
		logger.info("[CTQDailyMail] Scheduling END");

		scheduleRunned = false;
	}

	public String readFileFromResourceFile(String resourceName) throws Exception {
		ClassPathResource resource = new ClassPathResource(resourceName);
		File resourceFile = resource.getFile();
		String fileBody = FileUtils.readFileToString(resourceFile);
		return fileBody;
	}

}
