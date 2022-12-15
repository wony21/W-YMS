package com.compact.base.domain.ranktrend;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.compact.base.domain.ranktrend.dto.ResultTableData;

@Mapper
public interface RankTrendMapper {

	List<ResultTableData> getRankTrendOfSeperator(Map<String, Object> parameters);
	
	
}
