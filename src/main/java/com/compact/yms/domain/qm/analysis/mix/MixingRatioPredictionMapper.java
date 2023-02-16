package com.compact.yms.domain.qm.analysis.mix;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MixingRatioPredictionMapper {

	List getMixRawDataCIE(Map<String, Object> parameter);
	List getLots(Map<String, Object> parameter);
	
	
}
