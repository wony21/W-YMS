package com.compact.yms.domain.IF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class POPIFService {
	
	private static final Logger logger = LoggerFactory.getLogger(POPIFService.class);
	@Autowired
	POPIFMapper popifMapper;
	
	public enum IF_FLAG {
		IF_TB_ELEMENT_TRAY_ASSY,
		IF_YMS_ELEMENT_YIELD_RAW,
		IF_YMS_ELEMENT_YIELD_PERIOD,
		IF_TB_SEPERATOR_ASSY,
		IF_YMS_SEPERATOR_YIELD_RAW,
		IF_YMS_SEPERATOR_YIELD_PERIOD,
		IF_TB_TAPING_ASSY,
		IF_YMS_TAPING_YIELD_RAW,
		IF_YMS_TAPING_YIELD_PERIOD,
	}
	
	@Transactional
	public void ElementIF(String factoryName, String factoryMonth) {
		
		try {
			
			if (factoryMonth.length() < 6) {
				logger.error("[%s] is invalid format month.", factoryMonth);
			}
			int year = Integer.valueOf(factoryMonth.substring(0, 4));
			int month = Integer.valueOf(factoryMonth.substring(4, 6)) - 1; // month-1
			// 월 시작, 끝 Calendar 가져오기
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.set(year, month, 1);
			int lastDay = startCalendar.getActualMaximum(Calendar.DATE);
			Calendar endCalendar =  Calendar.getInstance();
			endCalendar.set(year, month, lastDay);
			// 날짜 Formatting
			String startTime = DateFormatUtils.format(startCalendar, "yyyyMMdd");
			String endTime = DateFormatUtils.format(endCalendar, "yyyyMMdd");
			String factoryDate;
			
			logger.info("====== START ELEMENT I/F =====");
			
			logger.info("[STEP1] TB_ELEMENT_TRAY_ASSY@POPDB ==> POP_TB_ELEMENT_TRAY_ASSY");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("factoryName", factoryName);
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			popifMapper.deleteTbElementTrayAssy(param);
			popifMapper.insertTbElementTrayAssy(param);
			
			logger.info("[STEP2] ELEMENT YIELD SUMMARY");
			for(int i = 1; i <= lastDay; i++) {
				startCalendar.add(Calendar.DATE, i-1);
				factoryDate = DateFormatUtils.format(startCalendar, "yyyyMMdd");
				
				Map<String, Object> param2 = new HashMap<String, Object>();
				param2.put("factoryName", factoryName);
				param2.put("factoryDate", factoryDate);
				
				logger.info("[STEP2-1] YMS_ELEMENT_YIELD [{}]", factoryDate);
				popifMapper.deleteElemntYieldRaw(param2);
				popifMapper.insertElemntYieldRaw(param2);
				
				logger.info("[STEP2-2] P_ELEMENT_YIELD [{}]", factoryDate);
				popifMapper.callElementYield(param2);
			}

			logger.info("====== END ELEMENT I/F =====");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void IF(IF_FLAG flag, String factoryName, String factoryMonth) {
		
		try {
			if (factoryMonth.length() < 6) {
				logger.error("[%s] is invalid format month.", factoryMonth);
			}
			int year = Integer.valueOf(factoryMonth.substring(0, 4));
			int month = Integer.valueOf(factoryMonth.substring(4, 6)) - 1; // month-1
			// 월 시작, 끝 Calendar 가져오기
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.set(year, month, 1);
			int lastDay = startCalendar.getActualMaximum(Calendar.DATE);
			Calendar endCalendar =  Calendar.getInstance();
			endCalendar.set(year, month, lastDay);
			// 날짜 Formatting
			String startTime = DateFormatUtils.format(startCalendar, "yyyyMMdd");
			String endTime = DateFormatUtils.format(endCalendar, "yyyyMMdd");
			
			switch (flag) {
			case IF_TB_ELEMENT_TRAY_ASSY:
				logger.info("TB_ELEMENT_TRAY_ASSY [{}][{}][{}]", new String[] { factoryName, startTime, endTime });
				elementTrayAssyIF(factoryName, startTime, endTime);
				break;
			case IF_YMS_ELEMENT_YIELD_RAW:
				logger.info("YMS_ELEMENT_YIELD [{}][{}][{}]", new String[] { factoryName, startTime, endTime });
				elementYieldIF(factoryName, startCalendar, endCalendar);
				break;
			case IF_YMS_ELEMENT_YIELD_PERIOD:
				logger.info("P_ELEMENT_YIELD [{}][{}][{}]", new String[] { factoryName, startTime, endTime });
				elementYieldPeriod(factoryName, startCalendar, endCalendar);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void elementYieldIF(String factoryName, Calendar startCalendar, Calendar endCalendar) {
		
		long dayCount = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / (24*60*60);
		
		for(int i = 1; i <= dayCount; i++) {
			startCalendar.add(Calendar.DATE, i-1);
			String factoryDate = DateFormatUtils.format(startCalendar, "yyyyMMdd");
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("factoryName", factoryName);
			param.put("factoryDate", factoryDate);
			
			logger.info("YMS_ELEMENT_YIELD [{}]", factoryDate);
			popifMapper.deleteElemntYieldRaw(param);
			popifMapper.insertElemntYieldRaw(param);
		}
	}
	
	@Transactional
	public void elementYieldPeriod(String factoryName, Calendar startCalendar, Calendar endCalendar) {
		
		long dayCount = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / (24*60*60);
		
		for(int i = 1; i <= dayCount; i++) {
			startCalendar.add(Calendar.DATE, i-1);
			String factoryDate = DateFormatUtils.format(startCalendar, "yyyyMMdd");
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("factoryName", factoryName);
			param.put("factoryDate", factoryDate);
			
			logger.info("P_ELEMENT_YIELD [{}]", factoryDate);
			popifMapper.callElementYield(param);
			
		}
	}
	
	public void IFOfDate(IF_FLAG flag, String factoryName, String startDate, String endDate) throws Exception {
		
		try {
			DateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
			// 월 시작, 끝 Calendar 가져오기
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(dateFmt.parse(startDate));
			Calendar endCalendar =  Calendar.getInstance();
			startCalendar.setTime(dateFmt.parse(endDate));
			
			switch (flag) {
			case IF_TB_ELEMENT_TRAY_ASSY:
				logger.info("TB_ELEMENT_TRAY_ASSY [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				elementTrayAssyIF(factoryName, startDate, endDate);
				break;
			case IF_YMS_ELEMENT_YIELD_RAW:
				logger.info("YMS_ELEMENT_YIELD RAW [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				elementYieldRaw(factoryName, startDate);
				break;
			case IF_YMS_ELEMENT_YIELD_PERIOD:
				logger.info("P_ELEMENT_YIELD SUM [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				elementYieldPeriod(factoryName, startDate);
				break;
			case IF_TB_SEPERATOR_ASSY:
				logger.info("TB_SEPERATOR_ASSY [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				seperatorAssyIF(factoryName, startDate, endDate);
				break;
			case IF_YMS_SEPERATOR_YIELD_RAW:
				logger.info("YMS_SEPERATOR_YIELD RAW [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				seperatorYieldRaw(factoryName, startDate);
				break;
			case IF_YMS_SEPERATOR_YIELD_PERIOD:
				logger.info("P_SEPERATOR_YIELD SUM [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				seperatorYieldPeriod(factoryName, startDate);
				break;
			case IF_TB_TAPING_ASSY:
				logger.info("TB_TAPING_ASSY [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				tapingAssyIF(factoryName, startDate, endDate);
				break;
			case IF_YMS_TAPING_YIELD_RAW:
				logger.info("YMS_TAPING_YIELD RAW [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				tapingYieldRaw(factoryName, startDate);
				break;
			case IF_YMS_TAPING_YIELD_PERIOD:
				logger.info("P_TAPING_YIELD SUM [{}][{}][{}]", new String[] { factoryName, startDate, endDate });
				tapingYieldPeriod(factoryName, startDate);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Transactional
	public void elementTrayAssyIF(String factoryName, String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("startTime", startDate);
		param.put("endTime", endDate);
		popifMapper.deleteTbElementTrayAssy(param);
		popifMapper.insertTbElementTrayAssy(param);
	}
	
	@Transactional
	public void seperatorAssyIF(String factoryName, String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("startTime", startDate);
		param.put("endTime", endDate);
		popifMapper.deleteTbSeperatorAssy(param);
		popifMapper.insertTbSeperatorAssy(param);
	}
	
	@Transactional
	public void tapingAssyIF(String factoryName, String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("startTime", startDate);
		param.put("endTime", endDate);
		popifMapper.deleteTbTapingAssy(param);
		popifMapper.insertTbTapingAssy(param);
	}
	
	@Transactional
	public void elementYieldRaw(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("YMS_ELEMENT_YIELD [{}]", factoryDate);
		popifMapper.deleteElemntYieldRaw(param);
		popifMapper.insertElemntYieldRaw(param);
	}
	
	@Transactional
	public void seperatorYieldRaw(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("YMS_SEPERATOR_YIELD [{}]", factoryDate);
		popifMapper.deleteSeperatorYieldRaw(param);
		popifMapper.insertSeperatorYieldRaw(param);
	}
	
	@Transactional
	public void tapingYieldRaw(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("YMS_TAPING_YIELD [{}]", factoryDate);
		popifMapper.deleteTapingYieldRaw(param);
		popifMapper.insertTapingYieldRaw(param);
	}
	
	@Transactional
	public void elementYieldPeriod(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("P_ELEMENT_YIELD [{}]", factoryDate);
		popifMapper.callElementYield(param);
	}
	
	@Transactional
	public void seperatorYieldPeriod(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("P_SEPERATOR_YIELD [{}]", factoryDate);
		popifMapper.callSeperatorYield(param);
	}
	
	@Transactional
	public void tapingYieldPeriod(String factoryName, String factoryDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factoryName", factoryName);
		param.put("factoryDate", factoryDate);
		logger.info("P_TAPING_YIELD [{}]", factoryDate);
		popifMapper.callTapingYield(param);
	}

}
