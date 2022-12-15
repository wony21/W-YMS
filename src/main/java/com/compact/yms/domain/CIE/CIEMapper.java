package com.compact.yms.domain.CIE;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CIEMapper {
	
	public List getList(Map<String, Object> parameter);				// 설비 수집데이터 조회
	public List compareProgram(Map<String, Object> parameter); 		// 수집프로그램명 비교
	public List getFileDClob(Map<String, Object> parameter);		// CLOB 데이터 조회
	
	public List getSeperatorCie(Map<String, Object> parameter);		// 분류기준서 좌표 값
	public List getSeperator(Map<String, Object> parameter);		// 분류기준서
}
