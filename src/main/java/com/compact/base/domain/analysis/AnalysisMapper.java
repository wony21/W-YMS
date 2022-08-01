package com.compact.base.domain.analysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AnalysisMapper {

	public List getFileLocation(Map<String, Object> parameter);		// NAS RawFile 위치
	
}
