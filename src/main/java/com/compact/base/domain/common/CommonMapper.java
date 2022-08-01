package com.compact.base.domain.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CommonMapper {
	
	public List test();
	public List getFileM(Map<String, Object> parameter);
	public List getFileIndex(Map<String, Object> parameter);
	public List getDailyMenuUsageCount(Map<String, Object> parameter);
	public List getDailyMenuCountList(Map<String, Object> parameter);
	
	public List getNotMappingOperations(Map<String, Object> parameter);
	public List getProcessOperationSpec(Map<String, Object> parameter);

}
