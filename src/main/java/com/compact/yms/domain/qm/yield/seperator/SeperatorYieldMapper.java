package com.compact.yms.domain.qm.yield.seperator;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeperatorYieldMapper {

	List getDateRange(Map<String, Object> parameters);
	List getPrevousDateRange(Map<String, Object> parameters);
	List getChartLabel(Map<String, Object> parameters);
	List getSeperatorYield(Map<String, Object> parameters);
	List getSYieldTable(Map<String, Object> parameters);
	List getProgramBinItems(Map<String, Object> parameters);
	List getSYieldDetailPeriod(Map<String, Object> parameters);
	
	List getSeperatorYieldOfBin(Map<String, Object> parameters);
	List getSeperatorRankYield(Map<String, Object> parameters);
	List getSYieldTableOfBin(Map<String, Object> parameters);
	List getSYieldDetailPeriodOfBin(Map<String, Object> parameters);
	
	List getProgramCie(Map<String, Object> parameters);
	List getSeperatorIV(Map<String, Object> parameters);
	List getSeperatorFiles(Map<String, Object> parameters);
	
	List getSorterData(Map<String, Object> parameters);
	List getCieItemName(Map<String, Object> parameters);
	
}
