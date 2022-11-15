package com.compact.base.domain.cond;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConditionMapper {
	
	List getSite(Map<String, Object> parameter);
	List getProductType(Map<String, Object> parameter);
	List getLine(Map<String, Object> parameter);
	List getStep(Map<String, Object> parameter);
	List getProductSpecGroup(Map<String, Object> parameter);
	List getProduct(Map<String, Object> parameter);
	List getProgram(Map<String, Object> parameter);
	List getTarget(Map<String, Object> parameter);
	List getChipSpec(Map<String, Object> parameter);
	List getFrameName(Map<String, Object> parameter);
	List getLots(Map<String, Object> parameter);
}
