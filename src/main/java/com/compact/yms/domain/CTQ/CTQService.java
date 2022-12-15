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

import com.compact.yms.common.api.ApiResponse;
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
	
	public void SaveFile(String mailNo, String start, String end, 
						 String testProv, String itemGroup, String itemCode, MultipartFile file) throws IllegalStateException, IOException {
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
	
	public void reserveSendMail(int mailId, String groupId, String locate, String prov, String mailDate, String mailBody) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mailId", mailId);
		param.put("groupId", groupId);
		param.put("locate", locate);
		param.put("prov", prov);
		param.put("mailDate", mailDate);
		param.put("mailBody", mailBody);
		mapper.reserveSendMailInMailId(param);
	}
	
	public List getCTQMailData(String itemGroup, String testProv, String summaryOption, String ngOption, List<String> products) {
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
	
	public List getMailGroup() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getMailGroup(param);
	}
	
	public List getMailItem(String groupId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupId", groupId);
		return mapper.getMailItem(param);
	}
	
	public List getCTQProductData(String productGroup, String productSpecName, String testLocate, String testProv, String sumOpt, String ngOpt) {
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
	
		
//	public byte[] GetFile(String mailNo) {
//		// File file = new File
//	}
	
}
