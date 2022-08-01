package com.compact.base.scheduler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.compact.base.domain.CTQ.CTQService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CTQSchedulerVer211223 {

	private static final Logger logger = LoggerFactory.getLogger(CTQSchedulerVer211223.class);

	@Autowired
	CTQService ctqService;
	
	@Value("${sch.ctq.daily.cron.enable:true}")
	public Boolean isEnabled;

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
	 * 
	 * @return
	 */
	private Boolean isScheduleIsRunnableTime(String hour, String min) {
		Date today = new Date();
		String hhmm = DateFormatUtils.format(today, "HHmm");
		return hhmm.equals(hour + min);
	}

	public String readFileFromResourceFile(String resourceName) throws Exception {
		ClassPathResource resource = new ClassPathResource(resourceName);
		File resourceFile = resource.getFile();
		String fileBody = FileUtils.readFileToString(resourceFile);
		return fileBody;
	}
	
	public String createTableHtml(int no, Map<String, Object> ctqData) {
		
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
		
		return trtd;
	}
	
	public String createChartLinkFilename(List<Map<String, Object>> ctqDataList) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(ctqDataList);

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

		return fileName;
		
	}

	@Scheduled(cron = "${sch.ctq.daily.cron}")
	public void SendMail() throws Exception {
		
		if( !isEnabled ) return;
		
		// 중복 실행 방지
		scheduleRunned = true;
		
		try {
			
			// CTQ Mail Group
			logger.info("--- MAIL GROUP LIST ---");
			
			// 스케쥴 기준 정보 생성
			Date today = new Date();
			String mailDate = DateFormatUtils.format(today, "yyyyMMdd");
			String todayFormatStr = DateFormatUtils.format(today, "yyyy년 MM월 dd일");

			// Mail template
			StringBuffer htmlTemplete = new StringBuffer();
			String mail_CSS = readFileFromResourceFile(mailTmplCSS);
			
			String mailTmplHtmlStr = readFileFromResourceFile(mailTmplHtml);

			List<Map<String, String>> mailGroups = ctqService.getMailGroup();

			// 메일 그룹 -----
			for (Map<String, String> group : mailGroups) {

				String groupId = getMapVal(group, "groupid");
				String groupName = getMapVal(group, "groupname");
				String sendType = getMapVal(group, "sendtype");
				String onlyNG = getMapVal(group, "onlyng");
				int rowCount = Integer.valueOf(getMapVal(group, "rowcount"));
				String runHour = getMapVal(group, "hour");
				String runMin = getMapVal(group, "min");
				String testLocate = getMapVal(group, "testlocate");
				String testProv = getMapVal(group, "testprov");
				String testLocateName = getMapVal(group, "testlocatename");
				String testProvName = getMapVal(group, "testprovname");
				String all = getMapVal(group, "allproduct");

				logger.info(String.format("GROUPID[%s] SENDTYPE[%s] NGOPT[%s] ROWCOUNT[%s] HOUR[%s] MIN[%s]", groupId, sendType, onlyNG,
						rowCount, runHour, runMin));

				// 스케쥴 지정 시간이 아니면 수행 종료
				if (!isScheduleIsRunnableTime(runHour, runMin))
					continue;
				
				List<Map<String, String>> mailItems = null;
				logger.info(String.format("GROUPID[%s] ALL-PRODUCT-FLAG[%s]", groupId, all));
				if ( all.equals("Y")) {
					mailItems = ctqService.getCTQALLProduct();
				} else {
					mailItems = ctqService.getMailItem(groupId);
				}
				
				// Data 수집 Summary
				

				// 메일 아이템 ------
				for (Map<String, String> item : mailItems) {

					String productGroup = getMapVal(item, "productgroup");
					String productSpecName = getMapVal(item, "productspecname");
					
					logger.info(String.format("TESTPROV[%s] TESTLOCATE[%s] PRODUCTGROUP[%s] PRODUCTSPECNAME[%s]", testProv, testLocate, productGroup, productSpecName));

					List<Map<String, Object>> ctqDataList = 
							ctqService.getCTQProductData(productGroup, productSpecName, testLocate, testProv, sendType, onlyNG);

					StringBuffer htmlTable = new StringBuffer();
					StringBuffer htmlChartImage = new StringBuffer();
					int dataCount = ctqDataList.size();
					int no = 1;
					for (Map<String, Object> ctqData : ctqDataList) {
						// 표 HTML 생성
						String trtd = createTableHtml(no, ctqData);
						// 최근 순 요청 20.11.17 박지훈 상무
						// rowCount 존재시(= 0보다 클 경우), 최근데이터 순으로 데이터 표시
						// rowCount 0 인 경우, 제한 없음 (0 보다 작게 입력해도 마찬가지)
						if (rowCount > 0) {
							//logger.info(String.format("ROWLIMIT[%d] DATACNT[%d] NO[%d]", rowCount, dataCount, no));
							if ((dataCount - rowCount + 1) <= no) {
								//logger.info(String.format("INSERT HTML"));
								htmlTable.insert(0, trtd);
							}
						} else {
							htmlTable.insert(0, trtd);
						}
						no = no + 1;
					}
					
					// 메일 표/차트 생성
					if (dataCount > 0) {
						// Chart Image 생성
						String fileName = createChartLinkFilename(ctqDataList);
						if (!fileName.isEmpty()) {
							String chartHtml = String.format(
									"<div><img src=\"http://" + chartAPIServer + "/image/%s\" /></div>", fileName);
							htmlChartImage.append(chartHtml);
						}
						// 차트+표 내용 생성 
						String strBuf = mailTmplHtmlStr.replace("<!--@@{TABLE}@@-->", htmlTable.toString());
						strBuf = strBuf.replace("<!--@@{CHART}@@-->", htmlChartImage.toString());
						htmlTemplete.append(strBuf);
					} else {
						logger.warn("[CTQDailyMail] No has data.");
					}
					
				} // 메일 아이템
				
				// 메일발송처리 ----
				if (htmlTemplete.length() > 0) {
					// 메일 상단 메시지 생성
					String topMessage = todayFormatStr + "자 자동 알람 메일 입니다.<br> ";
					
					//topMessage += "<b>본 메일은 개발 적용 테스트 메일 입니다</b>";
					if (rowCount > 0) {
						topMessage += "설정된 표 데이터 행수 제한은 (" + rowCount + ") 입니다.<br>";
					} 
					topMessage += "(차트의 경우, 최대 측정 개수가 250개 초과인 경우, 최근 기준 250개까지 표시 됩니다.) <br>";
					topMessage += String.format("[메일발송그룹명칭 : %s] <br>", groupName);
					// topMessage += String.format("<b>CTQ 측정 데이터 : %s, %s, %s, %s</b>(0값 자동제외)<br>", productGroup, productSpecName, testLocateName, testProvName);
					List<String> products = mailItems.stream().map(m -> m.get("productspecname")).collect(Collectors.toList());
					String topSumHtml = getSummaryHtml(testProv, testProvName, products, sendType, onlyNG);
					htmlTemplete.insert(0, topSumHtml);
					htmlTemplete.insert(0, "<div><p>" + topMessage + "</p></div>");
					htmlTemplete.insert(0, mail_CSS);
					// 생성된 메일 템플릿 DB 저장
					int mailId = ctqService.getNextMailId();
					ctqService.reserveSendMail(mailId, groupId, testLocateName, testProvName, mailDate, htmlTemplete.toString());
					// 저장된 메일DB 발송
					ctqService.requestMailSend(mailId);
					logger.info("[CTQDailyMail] Send reservce mail.");
				} else {
					logger.warn("[CTQDailyMail] Html templete is empty.");
				}
				// 초기화
				htmlTemplete = new StringBuffer();
				
			} // 메일 그룹
			
		} catch(Exception e) {
			e.getStackTrace();
		} finally {
			// 중복 실행 방지 해제 
			scheduleRunned = false;
			logger.info("[CTQDailyMail] Scheduling END");
		}
		
	}
	
	public String getSummaryHtml(String testProv, String testProvName, List<String> products, String optSum, String optNG) throws Exception {
		
		String mail_Summary = readFileFromResourceFile(mailSummaryTmplHtml);
		
		// th 생성
		String tagSummaryHtml = "";
		String smy_trth = "<th>" + testProvName + "</th>";
		mail_Summary = mail_Summary.replace("<!--@@{TEST-PROV}-->", smy_trth);

		// td 생성
		String smy_trtd = "";
		for (String product : products) {

			List<Map<String, String>> info = ctqService.getProductInfo(product);
			
			smy_trtd += "<tr>";
			smy_trtd += "<td>" + product + "</td>";

			if (info.size() > 0) {
				
				String productGroup = getMapVal(info.get(0), "itemGroup");
				String productName = getMapVal(info.get(0), "itemName");

				smy_trtd += "<td>" + productGroup + "</td>";
				smy_trtd += "<td>" + productName + "</td>";

				List<Map<String, String>> ctqProductCountList = ctqService.getCTQProductDataCount(product, testProv, optSum, optNG);
				
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

			} else {
				smy_trtd += "<td colspan=\"2\">존재하지 않는 제품</td>";
				// smy_trtd += "<td>존재하지 않는 제품</td>";
				continue;
			}
			smy_trtd += "</tr>";
		}
		mail_Summary = mail_Summary.replace("<!--@@{TABLE}@@-->", smy_trtd);
		
		return mail_Summary;
		
	}

}
