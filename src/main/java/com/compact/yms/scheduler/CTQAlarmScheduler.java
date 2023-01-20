package com.compact.yms.scheduler;

import java.io.File;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.analysis.function.Power;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.domain.CTQ.CTQService;
import com.compact.yms.domain.CTQ.dto.CTQAlarm;
import com.compact.yms.domain.CTQ.dto.CTQAlarmConfig;
import com.compact.yms.domain.CTQ.dto.Factor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CTQAlarmScheduler {

	@Autowired
	CTQService ctqService;

	Boolean isRunned = false;

	@Value("${sch.ctq.ng.cron.enable:true}")
	public Boolean isEnabled;

	@Value("${ctq.ng.mail.tmpl.css}")
	String mailTmplCSS;
	@Value("${ctq.ng.mail.tmpl.body.cpk}")
	String mailTmplCpk;
	@Value("${ctq.ng.mail.tmpl.body.cl}")
	String mailTmplCl;

	public String readFileFromResourceFile(String resourceName) throws Exception {
		ClassPathResource resource = new ClassPathResource(resourceName);
		File resourceFile = resource.getFile();
		String fileBody = FileUtils.readFileToString(resourceFile);
		return fileBody;
	}

	@Scheduled(cron = "${sch.ctq.alram.cron}")
	public void RunSchedule() {

		if (!isEnabled)
			return;
		
		if (isRunned)
			return;
		
		isRunned = true;

		log.info("********************** CTQ ALARM SCHEDULE RUN **********************");

		try {

			/*------------------------------------------------
			// 알람설정 기본정보 불러오기
			//-----------------------------------------------*/
			CTQAlarmConfig configObj = ctqService.getCTQAlarmConfig();
			if (configObj == null) {
				log.error("Alarm config information is null!!!");
				return;
			}

			if (!(configObj.getCpk().equals("Y") || configObj.getCl().equals("Y"))) {
				log.warn("All alarm check model is disabled.");
				return;
			} else {
				log.info("Alram checking model: CPK[{}] CL[{}]",
						new Object[] { configObj.getCpk(), configObj.getCl() });
			}
			/*------------------------------------------------
			// 알람 검출 시간 체크
			//-----------------------------------------------*/
			String checkStartHour = configObj.getCheckStartHour();
			String checkStartMin = configObj.getCheckStartMin();
			String cycleHour = configObj.getScheduleCycleHour();
			log.info("CycleHourTime is [{}]", cycleHour);

			if (!IsScheduleTime(checkStartHour, checkStartMin, cycleHour)) {
				log.info("Not execution schedule time!!!");
				return;
			}

			log.info("////////////////// CTQ ALARM CHECK START //////////////////");
			// Run Log
			String runHHmm = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH") + ":00";
			ctqService.addCTQAlarmLog("CTQ-ALARM", "CTQ", runHHmm, checkStartHour, checkStartMin, cycleHour, null, null);
			/*------------------------------------------------
			// 알람 대상 정보 불러오기 (시험위치, 시험항목, 제품코드)
			//-----------------------------------------------*/
			String dataCollectDayCount = configObj.getDataDays();
			if (!NumberUtils.isParsable(dataCollectDayCount)) {
				log.error("Invalid data collect day count [{}]", dataCollectDayCount);
				return;
			}
			List<CamelCaseMap> testLoc = configObj.getTestLocations();
			if (testLoc == null || testLoc.size() == 0) {
				log.error("No set testLoc config value.");
				return;
			}
			List<CamelCaseMap> testProv = configObj.getTestProvisions();
			if (testProv == null || testProv.size() == 0) {
				log.error("No set testProv config value.");
				return;
			}
			List<CamelCaseMap> testProduct = configObj.getTestProducts();
			if (testProduct == null || testProduct.size() == 0) {
				log.error("No set testProduct config value.");
				return;
			}
			/*------------------------------------------------
			// 알람 대상의 CTQ 데이터 조회 및 알람발생
			//-----------------------------------------------*/
			String htmlCpkTable = readFileFromResourceFile(this.mailTmplCpk);
			String htmlCpkTrTd = "";
			String htmlClTable = readFileFromResourceFile(this.mailTmplCl);
			String htmlClTrTd = "";
			int cpkNo = 1;
			int clNo = 1;
			for (CamelCaseMap loc : testLoc) {
				for (CamelCaseMap prov : testProv) {
					for (CamelCaseMap product : testProduct) {
						String locationCd = MapUtils.getString(loc, "code");
						String provisionCd = MapUtils.getString(prov, "code");
						String productCd = MapUtils.getString(product, "code");
						String locationNm = MapUtils.getString(loc, "value");
						String provisionNm = MapUtils.getString(prov, "value");
						//
						List<CTQAlarm> dataObjects = ctqService.getCTQDataForAlram(locationCd, provisionCd,
								dataCollectDayCount, productCd);

						Date now = new Date();
						String dataEndDate = DateFormatUtils.format(now, "yyyyMMdd");
						Date startDate = DateUtils.addDays(now, Integer.valueOf(dataCollectDayCount) * (-1));
						String dataStartDate = DateFormatUtils.format(startDate, "yyyyMMdd");

						log.info("[{}] DATACOUNT [{}]", new Object[] { productCd, dataObjects.size() });

						if (dataObjects.size() == 0) {
							log.info("No has data. next looping..");
							continue;
						}
						/*-----------------------------------------------------------
						// 통계 산출 (mean, range, std, lcl, cl, ucl, cpl, cpu, cpk
						//-----------------------------------------------------------*/
						// SummaryStatistics stats = new SummaryStatistics();
						for (CTQAlarm dataObj : dataObjects) {
							dataObj.calculator();
//							double[] doubleValues = dataObj.getDataList().stream().mapToDouble(x -> x).toArray();
//							for(double d : doubleValues) {
//								stats.addValue(d);
//							}
						}

						Double mean = dataObjects.stream().mapToDouble(o -> o.getAverage()).average().getAsDouble();
						Double range = dataObjects.stream().mapToDouble(o -> o.getRange()).average().getAsDouble();
						Double varic = dataObjects.stream().mapToDouble(o -> o.getVariance()).average().getAsDouble();
						Power pow = new Power(0.5d);
						Double groupStd = pow.value(varic);
						// Double std = stats.getStandardDeviation();

						int groupSize = dataObjects.stream().mapToInt(o -> o.getN()).findFirst().getAsInt();
						Factor factor = ctqService.getFactor(groupSize);

						Double LCL = mean - (factor.getA2() * range);
						Double CL = mean;
						Double UCL = mean + (factor.getA2() * range);

						Double SL = dataObjects.stream().mapToDouble(o -> o.getSL()).findFirst().getAsDouble();
						Double SU = dataObjects.stream().mapToDouble(o -> o.getSU()).findFirst().getAsDouble();
						Double CPU = (mean - SL) / (3 * groupStd);
						Double CPL = (SU - mean) / (3 * groupStd);
						Double CPK = Math.min(CPL, CPU);

						log.info("[{}] CPK[{}] LCL[{}] UCL[{}]", new Object[] { productCd, CPK, LCL, UCL });

						/* CPK 미달 */
						if (configObj.getCpk().equals("Y")) {
							Double cpkStart = Double.valueOf(configObj.getCpkRangeStart());
							Double cpkEnd = Double.valueOf(configObj.getCpkRangeEnd());
							CamelCaseMap productCpkRange = ctqService.getCTQProductCpkRange(productCd);
							if (productCpkRange != null) {
								log.info("Product Cpk exist!!!");
								cpkStart = MapUtils.getDouble(productCpkRange, "cpk1");
								cpkEnd = MapUtils.getDouble(productCpkRange, "cpk2");
							}
							//if (!(cpkStart < CPK && CPK <= cpkEnd)) {
							if (CPK < cpkStart) {
								log.error(
										"RuleOut: Out Of Cpk Range. DATE[{} ~ {}] PRODUCT[{}] LOC[{}] PROV[{}] Rule[{}~{}] Cpk[{}]",
										new Object[] { dataStartDate, dataEndDate, productCd, locationNm, provisionNm, cpkStart, cpkEnd, CPK });

								if (dataObjects.size() > 0) {
									CTQAlarm ctqObj = dataObjects.stream().findFirst().get();
									htmlCpkTrTd += createCpkTrTd(cpkNo, ctqObj.getDiv(), ctqObj.getProductSpecGroup(),
											ctqObj.getProductSpecName(), ctqObj.getProductSpecDesc(), ctqObj.getItem2(),
											ctqObj.getItem1(), dataStartDate, dataEndDate, SL, SU, mean, CPU, CPL, CPK,
											String.format("%.2f", cpkStart), String.format("%.2f", cpkEnd), "CPK미도달");
									cpkNo += 1;
								}
							}
						} else {
							htmlCpkTrTd = "<tr><td colspan=\"16\">알람 검출 미지정</td></tr>";
						}

						/* LCL UCL 이탈 */
						if (configObj.getCl().equals("Y")) {
							for (CTQAlarm dataObj : dataObjects) {
								if (!checkCL(dataObj, LCL, UCL)) {
									htmlClTrTd += createClTrTd(clNo, dataObj, dataStartDate, dataEndDate, LCL, UCL, CL);
									clNo += 1;
								}
							}
						} else {
							htmlClTrTd = "<tr><td colspan=\"32\">알람 검출 미지정</td></tr>";
						}
					}
				}
			}
			htmlCpkTable = htmlCpkTable.replace("<!--@{TABLE-DATA}-->", htmlCpkTrTd);
			htmlCpkTable = htmlCpkTable.replace("<!--@{DATA-COUNT}-->", String.valueOf(cpkNo - 1));
			htmlClTable = htmlClTable.replace("<!--@{TABLE-DATA}-->", htmlClTrTd);
			htmlClTable = htmlClTable.replace("<!--@{DATA-COUNT}-->", String.valueOf(clNo - 1));
			log.info("==================================================================");
			String htmlCss = readFileFromResourceFile(mailTmplCSS);
			StringBuffer mailHtml = new StringBuffer();
			mailHtml.append(htmlCss);
			mailHtml.append("본 메일을 발송 전용 메일 입니다.<br><br>");
			mailHtml.append(htmlCpkTable);
			mailHtml.append(htmlClTable);
			log.info(mailHtml.toString());
			// 생성된 메일 템플릿 DB 저장
			int mailId = ctqService.getNextMailId();
			ctqService.reserveSendMail(mailId, "CTQ-ALRAM", null, null, DateFormatUtils.format(new Date(), "yyyyMMdd"),
					mailHtml.toString());
			// 저장된 메일DB 발송
			ctqService.requestSendCTQNgMailInMailId(mailId);
			log.info("[CTQ NG MAIL] Send reservce mail.");
			log.info("==================================================================");

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			isRunned = false;
		}
	}

	/**
	 * 스케쥴 진행될 시간인지 확인 한다.
	 * 
	 * @param cycleTime 스케쥴 수행 매 시간
	 * @return
	 * @throws ParseException 
	 */
	public Boolean IsScheduleTime(String checkStartHour, String checkStartMin, String cycleTime) throws Exception {

		log.info("Check Schedule Time");
		Integer cycleHour = Integer.valueOf(cycleTime);

		Date now = new Date();
		String currentDate = DateFormatUtils.format(now, "yyyyMMdd");
		String currentHourTime = DateFormatUtils.format(now, "HHmm");

		log.info("Scheduled time check in CTQ logs");
		/* 이미 수행되었는지 로그에서 확인 */
		String runHHmm = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH") + ":00";
		List<CamelCaseMap> logs = ctqService.getCTQAlarmLog("CTQ-ALARM", "CTQ", runHHmm, checkStartHour, checkStartMin,
				cycleTime, null, null);
		if (logs.size() > 0) {
			log.info("Schedule is runable!!");
			return false;
		}
		
		log.info("Schedule CycleHour Check!!!");
		/* 주기 계산의 시간에 포함되는지 확인 */
		int scheduleCount = 24 / cycleHour;
		Date scheduleStartTime = DateUtils.parseDate(checkStartHour + checkStartMin, "HHmm");
		for(int i=0; i < scheduleCount; i++) {
			Date scheduleTime = DateUtils.addHours(scheduleStartTime, cycleHour * i);
			String scheduleTimeFmt = DateFormatUtils.format(scheduleTime, "HHmm");
			log.info("Schedule runable: {}", scheduleTimeFmt);
			if (scheduleTimeFmt.equals(currentHourTime)) {
				log.info("It's schedule runable time!!");
				return true;
			}
			//log.info("{}", DateFormatUtils.format(scheduleHour, "HH:mm"));
		}
		
		// log.info("Schedule CycleTime[{}]", cycleHour);
		log.info("Check Schedule Time End");
		return false;
	}
	
	public String createCpkTrTd(int cpkNo, String div, String productSpecGroup, String productSpecName,
			String productSpecDesc, String testLoc, String testProv, String dataStart, String dataEnd, Double sl,
			Double su, Double avg, Double cpu, Double cpl, Double cpk, String cpkStart, String cpkEnd, String msg) {

		if (Double.isNaN(cpk)) {
			return "";
		}
		String trtd = "<tr>";
		trtd += "<td>" + cpkNo + "</td>";
		trtd += "<td>" + div + "</td>";
		trtd += "<td>" + productSpecGroup + "</td>";
		trtd += "<td>" + productSpecName + "</td>";
		trtd += "<td>" + productSpecDesc + "</td>";
		trtd += "<td>" + testLoc + "</td>";
		trtd += "<td>" + testProv + "</td>";
		trtd += "<td>" + dataStart + "~" + dataEnd + "</td>";
		trtd += "<td>" + String.format("%.2f", sl) + "</td>";
		trtd += "<td>" + String.format("%.2f", su) + "</td>";
		trtd += "<td>" + String.format("%.2f", avg) + "</td>";
		trtd += "<td>" + String.format("%.2f", cpu) + "</td>";
		trtd += "<td>" + String.format("%.2f", cpl) + "</td>";
		trtd += "<td class=\"blue-color\">" + String.format("%s-%s", cpkStart, cpkEnd) + "</td>";
		trtd += "<td class=\"red-color\">" + String.format("%.2f", cpk) + "</td>";
		trtd += "<td>" + msg + "</td>";
		trtd += "</tr>";
		return trtd;
	}

	public Boolean checkCL(CTQAlarm obj, Double lcl, Double ucl) {
		if (lcl == null) {
			return true; // pass
		}
		if (ucl == null) {
			return true; // pass
		}
		int groupSize = obj.getN();
		if (groupSize == 0) {
			return true; // pass
		}
		Class objClass = CTQAlarm.class;
		for (int i = 1; i <= groupSize; i++) {
			String methodName = String.format("getValue%d", i);
			try {
				Method method = objClass.getMethod(methodName, null);
				Double value = (Double) method.invoke(obj, null);
				if (value != null) {
					if (!(lcl <= value && value <= ucl)) {
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public String createClTrTd(int clNo, CTQAlarm obj, String dataStart, String dataEnd, Double lcl, Double ucl,
			Double cl) {
		String trtd = "<tr>";
		trtd += "<td>" + clNo + "</td>";
		trtd += "<td>" + obj.getDiv() + "</td>";
		trtd += "<td>" + obj.getProductSpecGroup() + "</td>";
		trtd += "<td>" + obj.getProductSpecName() + "</td>";
		trtd += "<td>" + obj.getProductSpecDesc() + "</td>";
		trtd += "<td>" + obj.getItem2() + "</td>";
		trtd += "<td>" + obj.getItem1() + "</td>";
		trtd += "<td>" + String.format("%s~%s", dataStart, dataEnd) + "</td>";
		trtd += "<td class=\"blue-color\">" + String.format("%.2f", lcl) + "</td>";
		trtd += "<td class=\"blue-color\">" + String.format("%.2f", cl) + "</td>";
		trtd += "<td class=\"blue-color\">" + String.format("%.2f", ucl) + "</td>";
		trtd += "<td>" + String.format("%.2f", obj.getAverage()) + "</td>";
		trtd += highlightValue(lcl, ucl, obj.getValue1());
		trtd += highlightValue(lcl, ucl, obj.getValue2());
		trtd += highlightValue(lcl, ucl, obj.getValue3());
		trtd += highlightValue(lcl, ucl, obj.getValue4());
		trtd += highlightValue(lcl, ucl, obj.getValue5());
		trtd += highlightValue(lcl, ucl, obj.getValue6());
		trtd += highlightValue(lcl, ucl, obj.getValue7());
		trtd += highlightValue(lcl, ucl, obj.getValue8());
		trtd += highlightValue(lcl, ucl, obj.getValue9());
		trtd += highlightValue(lcl, ucl, obj.getValue10());
		trtd += highlightValue(lcl, ucl, obj.getValue11());
		trtd += highlightValue(lcl, ucl, obj.getValue12());
		trtd += highlightValue(lcl, ucl, obj.getValue13());
		trtd += highlightValue(lcl, ucl, obj.getValue14());
		trtd += highlightValue(lcl, ucl, obj.getValue15());
		trtd += highlightValue(lcl, ucl, obj.getValue16());
		trtd += highlightValue(lcl, ucl, obj.getValue17());
		trtd += highlightValue(lcl, ucl, obj.getValue18());
		trtd += highlightValue(lcl, ucl, obj.getValue19());
		trtd += highlightValue(lcl, ucl, obj.getValue20());
		trtd += "</tr>";
		return trtd;
	}

	public String highlightValue(Double lcl, Double ucl, Double value) {
		if (value == null) {
			return "<td></td>";
		}
		if (value == 0d) {
			return "<td>" + value + "</td>";
		}

		if (!(lcl <= value && value <= ucl)) {
			return "<td class=\"red-color\">" + value + "</td>";
		} else {
			return "<td>" + value + "</td>";
		}
	}

}
