package com.compact.yms.domain.QueryCond;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QueryCondMapper {

	public List getStepSeq(Map<String, Object> parameter);
	public List getStepType(Map<String, Object> parameter);
	public List getSite(Map<String, Object> parameter);
	public List getLine(Map<String, Object> parameter);
	public List getProductSpecGroup(Map<String, Object> parameter);
	public List getProductSpecName(Map<String, Object> parameter);
	public List getLOTID(Map<String, Object> parameter);
	public List getMachine(Map<String, Object> parameter);
	public List getProgram(Map<String, Object> parameter);
	public List getTarget(Map<String, Object> parameter);
	public List getIntensity(Map<String, Object> parameter);
	public List getChipSpec(Map<String, Object> parameter);
	public List getFrameName(Map<String, Object> parameter);
	public List getPL(Map<String, Object> parameter);
	public List getReelBatchID(Map<String, Object> parameter);
	
}
