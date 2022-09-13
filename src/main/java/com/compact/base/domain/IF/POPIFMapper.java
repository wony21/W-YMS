package com.compact.base.domain.IF;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface POPIFMapper {
	public void deleteTbElementTrayAssy(Map<String, Object> parameter);
	public void insertTbElementTrayAssy(Map<String, Object> parameter);
	public void deleteElemntYieldRaw(Map<String, Object> parameter);
	public void insertElemntYieldRaw(Map<String, Object> parameter);
	public void callElementYield(Map<String, Object> parameter);
	
	public void deleteTbSeperatorAssy(Map<String, Object> parameter);
	public void insertTbSeperatorAssy(Map<String, Object> parameter);
	public void deleteSeperatorYieldRaw(Map<String, Object> parameter);
	public void insertSeperatorYieldRaw(Map<String, Object> parameter);
	public void callSeperatorYield(Map<String, Object> parameter);
	
	public void deleteTbTapingAssy(Map<String, Object> parameter);
	public void insertTbTapingAssy(Map<String, Object> parameter);
	public void deleteTapingYieldRaw(Map<String, Object> parameter);
	public void insertTapingYieldRaw(Map<String, Object> parameter);
	public void callTapingYield(Map<String, Object> parameter);
	
}
