package com.compact.base.domain.analysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.compact.base.domain.analysis.DTO.FileLocation;
import com.compact.base.domain.analysis.DTO.ItemName;
import com.compact.base.domain.analysis.DTO.TableShareData;


@Mapper
public interface AnalysisMapper {

	public List getFileLocation(Map<String, Object> parameter);		// NAS RawFile 위치
	//public List<FileLocation> getFilePathOnNAS(Map<String, Object> parameter);
	
	public List<ItemName> getMeasureItemNames(Map<String, Object> parameter);
	
	public List<FileLocation> getInfo(Map<String, Object> parameter);
}
