package com.compact.base.domain.CTQ;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CTQMapper {
	
	public List getList();												// 목록 조회 (테스트 용) - 사용안함
	
	public List getCTQData(Map<String, Object> parameter);				// CTQ 측정 데이터 조회
	public List getCTQMailData(Map<String, Object> parameter);			// CTQ 메일 데이터 조회
	public List getItemGroup(Map<String, Object> parameter);			// CTQ 기준정보 그룹코드 조회
	public List getItemCode(Map<String, Object> parameter);				// CTQ 기준정보 코드정보 조회
	public List getTestProvCode(Map<String, Object> parameter);			// CTQ 시험항목 조회
	public List getCommonCode(Map<String, Object> parameter);			// CTQ 기준정보 조회
	public List getCTQDataCount(Map<String, Object> parameter);			// CTQ 데이터 수 조회
	public List getProductInfo(Map<String, Object> parameter);			// 제품정보 조회
	
	public List getMailGroup(Map<String, Object> parameter);			// 메일 그룹 목록 조회
	public List getMailItem(Map<String, Object> parameter);				// 메일 아이템 목록 조회
	public List getCTQProductData(Map<String, Object> parameter);		// CTQ 측정데이터 조회
	public List getCTQProduct(Map<String, Object> parameter);			// CTQ 기준정보 전체 제품코드 정보
	
	public int getNextMailId(Map<String, Object> parameter);			// 메일발송아이디발급
	public void reserveSendMailInMailId(Map<String, Object> parameter);	// 메일발송템플릿저장(메일아이디 필수)
	public void requestSendMailInMailId(Map<String, Object> parameter);	// 메일발송(메일아이디 필수)
	
	public int addMailImage(Map<String, Object> parameter);				// ChartImage
	public int reserveSendMail(Map<String, Object> parameter);			// 메일발송템플릿저장 - 사용안함
	public void requestSendMail();										// 메일발송(오늘 날짜 미발송 분에 대한 메일 전체 발송) - 사용안함

	
}
