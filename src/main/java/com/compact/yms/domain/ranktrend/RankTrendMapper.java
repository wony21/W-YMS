package com.compact.yms.domain.ranktrend;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.compact.yms.domain.ranktrend.dto.ResultBothTableData;
import com.compact.yms.domain.ranktrend.dto.ResultMeasureTableData;
import com.compact.yms.domain.ranktrend.dto.ResultTableData;

@Mapper
public interface RankTrendMapper {

	List getMItem(Map<String, Object> parameters);
	
	List getMItemTaping(Map<String, Object> parameters);
	
	List<ResultTableData> getRankTrendOfSeperator(Map<String, Object> parameters);
	
	List<ResultTableData> getRankTrendOfSeperatorTaping(Map<String, Object> parameters);
	
	List<ResultMeasureTableData> getRankTrendOfMeasure(Map<String, Object> parameters);
	
	List<ResultMeasureTableData> getRankTrendOfMeasureTaping(Map<String, Object> parameters);
	
	List<ResultBothTableData> getRankTrendOfBoth(Map<String, Object> parameters);
	
	List<ResultBothTableData> getRankTrendOfBothTaping(Map<String, Object> parameters);
	
	List getFileLocation(Map<String, Object> parameters);
	
	List getBinNo(Map<String, Object> parameters);
	
	List getBinNoTaping(Map<String, Object> parameters);
	
	List getRankName(Map<String, Object> parameters);
	
}
