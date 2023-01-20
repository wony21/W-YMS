package com.compact.yms.domain.qm.yield.seperator;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeperatorYieldMapper {

	
	
	List getChartLabel(Map<String, Object> parameters);
	
	List getSeperatorYield(Map<String, Object> parameters);
	
	
}
