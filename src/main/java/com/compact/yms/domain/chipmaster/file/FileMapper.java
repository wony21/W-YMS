package com.compact.yms.domain.chipmaster.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FileMapper {
	
	public void addCmStdM(Map<String, Object> parameter);
	public int addCmStdM_v5(Map<String, Object> parameter);
	public void addCmMsrInfo(Map<String, Object> parameter);
	public void addCmTolerance(Map<String, Object> parameter);
	public void addCmPartlist(Map<String, Object> parameter);
	public void addCmData(Map<String, Object> parameter);
	public void deleteCmData(Map<String, Object> parameter);
	public void updateFilePath(Map<String, Object> parameter);
	public List getChipMasterFilePath(Map<String, Object> parameter);
}
