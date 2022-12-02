package com.compact.base.domain.analysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.compact.base.domain.analysis.DTO.CieNameInfo;
import com.compact.base.domain.analysis.DTO.FileLocation;
import com.compact.base.domain.analysis.DTO.ItemName;
import com.compact.base.domain.analysis.DTO.RunHistoryData;
import com.compact.base.domain.analysis.DTO.SeperatorData;
import com.compact.base.domain.analysis.DTO.ShareUserListData;
import com.compact.base.domain.analysis.DTO.TableShareData;


@Mapper
public interface AnalysisMapper {

	public List getFileLocation(Map<String, Object> parameter);		// NAS RawFile 위치
	//public List<FileLocation> getFilePathOnNAS(Map<String, Object> parameter);
	
	public List<ItemName> getMeasureItemNames(Map<String, Object> parameter);
	
	public List<CieNameInfo> getCieXYItemName(Map<String, Object> parameter);
	
	public List<FileLocation> getInfo(Map<String, Object> parameter);
	
	public List<SeperatorData> getSeperatorCIE(Map<String, Object> parameter);
	
	public List<RunHistoryData> getHistory(Map<String, Object> parameter);
	
	public List<RunHistoryData> getHistoryOnKey(Map<String, Object> parameter);
	
	public void addHistory(Map<String, Object> parameter);
	
	public void deleteHistory(Map<String, Object> parameter);
	
	public List<ShareUserListData> getAllUserListForShare(Map<String, Object> parameter);
	
	public List<ShareUserListData> getShareUserLists(Map<String, Object> parameter);
	
	public void shareHistory(Map<String, Object> parameter);
	
	public void setShareHistoryFlag(Map<String, Object> parameter);
	
	public void deleteHistoryShare(Map<String, Object> parameter);
	
	public void setHistoryMemo(Map<String, Object> parameter);
	
	
	
	
}
