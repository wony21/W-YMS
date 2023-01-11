package com.compact.yms.domain.CTQ;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.CTQ.dto.CTQAlarm;
import com.compact.yms.domain.CTQ.dto.CTQAlarmConfig;
import com.compact.yms.domain.CTQ.dto.Factor;
import com.compact.yms.scheduler.CTQScheduler;
import com.microsoft.sqlserver.jdbc.StringUtils;

@Service
@Component
public class CTQService {

	private static final Logger logger = LoggerFactory.getLogger(CTQService.class);

	@Autowired
	CTQMapper mapper;

	@Value("${image.path}")
	String uploadPath;

	public List getTest() {
		return mapper.getList();
	}

	public List getCTQData(String startDate, String endDate, String itemGroup, String itemCode, String testProv) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("itemGroup", itemGroup);
		param.put("itemCode", itemCode);
		param.put("testProv", testProv);
		return mapper.getCTQData(param);
	}

	public List getItemGroup(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		return mapper.getItemGroup(param);
	}

	public List getItemCode(String startDate, String endDate, String itemGroup) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("itemGroup", itemGroup);
		return mapper.getItemCode(param);
	}

	public List getTestProvCode(String startDate, String endDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		return mapper.getTestProvCode(param);
	}

	public void SaveFile(String mailNo, String start, String end, String testProv, String itemGroup, String itemCode,
			MultipartFile file) throws IllegalStateException, IOException {
		// 파일
		String fileNm = file.getOriginalFilename();
		File uploadFile = new File(uploadPath + "//" + fileNm);
		file.transferTo(uploadFile);

		// DB에 정보 입력
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("mailNo", mailNo);
		parameter.put("start", start);
		parameter.put("end", end);
		parameter.put("testProv", testProv);
		parameter.put("itemGroup", itemGroup);
		parameter.put("itemCode", itemCode);
		parameter.put("fileName", fileNm);
		mapper.addMailImage(parameter);
	}

	public List getCommonCode(String groupCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupCode", groupCode);
		return mapper.getCommonCode(param);
	}

	public void reserveSendMail(String mailDate, String mailBody) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailDate", mailDate);
		param.put("mailBody", mailBody);
		mapper.reserveSendMail(param);
	}

	public void reserveSendMail(int mailId, String groupId, String locate, String prov, String mailDate,
			String mailBody) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailId", mailId);
		param.put("groupId", groupId);
		param.put("locate", locate);
		param.put("prov", prov);
		param.put("mailDate", mailDate);
		param.put("mailBody", mailBody);
		mapper.reserveSendMailInMailId(param);
	}

	public List getCTQMailData(String itemGroup, String testProv, String summaryOption, String ngOption,
			List<String> products) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("itemGroup", itemGroup);
		param.put("testProv", testProv);
		param.put("summaryType", summaryOption);
		param.put("ngFlag", ngOption);
		param.put("products", products);
		// logger.debug("summaryType : ", summaryOption);
		return mapper.getCTQMailData(param);
	}

	public List getProductInfo(String product) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("product", product);
		return mapper.getProductInfo(param);
	}

	public List getCTQProductDataCount(String product, String testProv, String summaryOption, String ngOption) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("product", product);
		param.put("testProv", testProv);
		param.put("summaryType", summaryOption);
		param.put("ngFlag", ngOption);
		// logger.debug("summaryType : ", summaryOption);
		return mapper.getCTQDataCount(param);
	}

	public void requestMailSend() {
		mapper.requestSendMail();
	}

	public void requestMailSend(int mailId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailId", mailId);
		mapper.requestSendMailInMailId(param);
	}
	
	public void requestSendCTQNgMailInMailId(int mailId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailId", mailId);
		mapper.requestSendCTQNgMailInMailId(param);
	}

	public List getMailGroup() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getMailGroup(param);
	}

	public List getMailItem(String groupId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupId", groupId);
		return mapper.getMailItem(param);
	}

	public List getCTQProductData(String productGroup, String productSpecName, String testLocate, String testProv,
			String sumOpt, String ngOpt) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("productGroup", productGroup);
		param.put("productSpecName", productSpecName);
		param.put("testLocate", testLocate);
		param.put("testProv", testProv);
		param.put("sumOpt", sumOpt);
		param.put("ngOpt", ngOpt);
		return mapper.getCTQProductData(param);
	}

	public int getNextMailId() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getNextMailId(param);
	}

	public List getCTQALLProduct() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getCTQProduct(param);
	}

	public Factor getFactor(int factSize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("factSize", factSize);
		List<Factor> factors = mapper.getFactor(param);
		if (factors.size() > 0)
			return factors.get(0);
		else
			return null;
	}
	
	public List<Factor> getAllFactor() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getFactor(param);
	}

	public List<CTQAlarm> getCTQDataForAlram(String testLoc, String testProv, String dayCount, String product) {
		int fromDayCount = Integer.valueOf(dayCount);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("testLocate", testLoc);
		param.put("testProv", testProv);
		param.put("day", fromDayCount * -1);
		param.put("product", product);
		return mapper.getCTQDataForAlarm(param);
	}
	
	public CTQAlarmConfig getCTQAlarmConfig() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		List<CTQAlarmConfig> configObjects = mapper.getCTQAlarmConfig(parameter);
		if (configObjects.size() == 0) {
			logger.error("Cannot found CTQ alarm default configuration.");
			return null;
		}
		CTQAlarmConfig configObj = configObjects.get(0);
		
		parameter.put("all", configObj.getAllOfTestLoc());
		List testLocations = mapper.getCTQConfigTestLocation(parameter);
		configObj.setTestLocations(testLocations);
		
		parameter.put("all", configObj.getAllOfTestProv());
		List testProvisions = mapper.getCTQConfigTestProvision(parameter);
		configObj.setTestProvisions(testProvisions);
		
		parameter.put("all", configObj.getAllOfProducts());
		List testProducts = mapper.getCTQConfigTestProduct(parameter);
		configObj.setTestProducts(testProducts);
		
		return configObj;
	}
	
	public CamelCaseMap getCTQScheduleTime(String date, String hour) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("date", date);
		parameter.put("hour", hour);
		List<CamelCaseMap> list = mapper.getCTQAlarmScheduleHistory(parameter);
		if (list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public int addCTQAlarmLog(String logId, String logType, String value1, String value2, String value3, String value4, String value5, String value6) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("logId", logId);
		parameter.put("logType", logType);
		parameter.put("value1", value1);
		parameter.put("value2", value2);
		parameter.put("value3", value3);
		parameter.put("value4", value4);
		parameter.put("value5", value5);
		parameter.put("value6", value6);
		return mapper.addCTQAlarmLog(parameter);
	}
	
	public List<CamelCaseMap> getCTQAlarmLog(String logId, String logType, String value1, String value2, String value3, String value4, String value5, String value6) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("logId", logId);
		parameter.put("logType", logType);
		parameter.put("value1", value1);
		parameter.put("value2", value2);
		parameter.put("value3", value3);
		parameter.put("value4", value4);
		parameter.put("value5", value5);
		parameter.put("value6", value6);
		return mapper.getCTQAlarmLog(parameter);
	}
	
	public CamelCaseMap getCTQProductCpkRange(String productSpecName) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("productSpecName", productSpecName);
		List<CamelCaseMap> list = mapper.getCTQProductCpkRange(parameter);
		if (list.size() > 0) {
			return list.stream().findFirst().get();
		} else {
			return null;
		}
	}
}
