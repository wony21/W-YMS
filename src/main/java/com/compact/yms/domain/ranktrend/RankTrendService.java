package com.compact.yms.domain.ranktrend;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.util.KthSelector;
import org.apache.commons.math3.util.MedianOf3PivotingStrategy;
import org.apache.commons.net.ftp.FTPClient;
import org.dozer.DozerBeanMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.common.BaseService;
import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.domain.ranktrend.RankTrendUtils.Stat;
import com.compact.yms.domain.ranktrend.dto.RankTrendGraphResult;
import com.compact.yms.domain.ranktrend.dto.RankTrendResultData;
import com.compact.yms.domain.ranktrend.dto.ResultBothTableData;
import com.compact.yms.domain.ranktrend.dto.ResultMeasureTableData;
import com.compact.yms.domain.ranktrend.dto.ResultTableData;
import com.compact.yms.utils.FTPUtils;
import com.compact.yms.utils.HelpUtils;

@Service
public class RankTrendService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(RankTrendService.class);

	@Autowired
	RankTrendMapper mapper;

	@Autowired
	FTPUtils ftpUtils;

	public List getMItem(
			String factoryName, String startDate, String endDate, String div, String stepSeq, String productionType,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String intensity, String pl, String frameName, String subFrameName, String lotId, String type, String filter
	) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "intensities", intensity);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "subFrameNames", subFrameName);
		setArrayParam(parameter, "lotIds", lotId);
		parameter.put("type", type);
		parameter.put("filter", filter);
		if (stepSeq.contains("TP")) {
			return mapper.getMItemTaping(parameter);
		} else {
			return mapper.getMItem(parameter);
		}

	}

	public List<CamelCaseMap> CDF(
			String dataTable, String groupKeyName, String subGroupKeyName, String labelKeyName, String dataKeyName
	) throws Exception {

		logger.info("Start CDF");

		List<CamelCaseMap> CDFList = new ArrayList<>();

		List<RankTrendGraphResult> inputList = new ArrayList<>();
		JSONArray jsonArrayObj = new JSONArray(dataTable);
		int jsonLength = jsonArrayObj.length();
		for (int i = 0; i < jsonLength; i++) {

			JSONObject jObj = jsonArrayObj.getJSONObject(i);
			String groupName = jObj.getString(groupKeyName);
			String subGroupName = jObj.getString(subGroupKeyName);
			String dataValueStr = jObj.getString(dataKeyName);

			RankTrendGraphResult inputObj = new RankTrendGraphResult();
			inputObj.setGroupName(groupName);
			inputObj.setSubGroupName(subGroupName);
			Double dataValue = Double.valueOf(dataValueStr);
			inputObj.setDataValue(dataValue);
			inputList.add(inputObj);
		}

		/**
		 * 카테고리(대그룹)
		 */
		Map<String, List<RankTrendGraphResult>> groupData = inputList.stream()
				.collect(Collectors.groupingBy(RankTrendGraphResult::getGroupName));

		int groupCount = groupData.size();
		logger.info("GroupSize[{}]", groupCount);

		for (String groupKey : groupData.keySet()) {

			List<RankTrendGraphResult> groupList = groupData.get(groupKey);

			/**
			 * 카테고리(중그룹=Series)
			 */
			Map<String, List<RankTrendGraphResult>> subGroupData = groupList.stream()
					.collect(Collectors.groupingBy(RankTrendGraphResult::getSubGroupName));

			logger.info("SubGroupSize[{}]", subGroupData.size());

			for (String subGroupKey : subGroupData.keySet()) {

				List<RankTrendGraphResult> subGroupList = subGroupData.get(subGroupKey);
				int subGroupSize = subGroupList.size();

				List<Double> sequence = subGroupList.stream().map(RankTrendGraphResult::getDataValue).distinct()
						.sorted().collect(Collectors.toList());

				SummaryStatistics stats = new SummaryStatistics();
				for (Double data : sequence) {
					logger.info("DATAVALUE [{}]", data);
					stats.addValue(data);
				}

				Double mean = sequence.stream().mapToDouble(v -> v).average().orElse(0);
				Double stdev = stats.getStandardDeviation();
				if (stdev == 0d) {
					logger.warn("[{}][{}] stdev value is 0.", groupKey, subGroupKey);
					continue;
				}
				NormalDistribution normDist = new NormalDistribution(mean, stdev);
				for (Double data : sequence) {
					Double cdf = normDist.cumulativeProbability(data);
					RankTrendGraphResult CDFObj = new RankTrendGraphResult();
					CDFObj.setGroupName(groupKey);
					CDFObj.setSubGroupName(subGroupKey);
					CDFObj.setLabel(String.format("%.3f", data));
					CDFObj.setDataValue(cdf);

					logger.info(
							String.format("Group[%s] GroupCnt[%d] SubGroup[%s] SubGroupCnt[%d] Label[%s] Value[%s] ",
									groupKey, groupCount, subGroupKey, subGroupSize, String.format("%.3f", data),
									String.format("%.5f", cdf)));

					CamelCaseMap itemMap = new CamelCaseMap();
					itemMap.put(groupKeyName, groupKey);
					itemMap.put(subGroupKeyName, subGroupKey);
					itemMap.put(labelKeyName, String.format("%.3f", data));
					itemMap.put(dataKeyName, String.format("%.5f", cdf));
					CDFList.add(itemMap);
				}
			}
		}

		logger.info("CDF calculator size [{}]", CDFList.size());

		return CDFList;
	}

	public List<CamelCaseMap> getBoxPlot(
			String dataTable, String groupKeyName, String subGroupKeyName, String labelKeyName, String dataKeyName
	) throws Exception {

		logger.info("Start BoxPlot [{}][{}][{}][{}]", new Object[] {
				groupKeyName, subGroupKeyName, labelKeyName, dataKeyName });
		logger.info("[JSON] {}", dataTable);

		List<CamelCaseMap> boxPlots = new ArrayList<>();

		List<RankTrendGraphResult> inputList = new ArrayList<>();
		JSONArray jsonArrayObj = new JSONArray(dataTable);
		int jsonLength = jsonArrayObj.length();
		for (int i = 0; i < jsonLength; i++) {

			JSONObject jObj = jsonArrayObj.getJSONObject(i);
			String groupName = jObj.getString(groupKeyName);
			String subGroupName = jObj.getString(subGroupKeyName);
			String dataValueStr = jObj.getString(dataKeyName);
			String labelName = jObj.getString(labelKeyName);

			RankTrendGraphResult inputObj = new RankTrendGraphResult();
			inputObj.setGroupName(groupName);
			inputObj.setSubGroupName(subGroupName);
			inputObj.setLabel(labelName);
			Double dataValue = Double.valueOf(dataValueStr);
			inputObj.setDataValue(dataValue);
			inputList.add(inputObj);
		}

		/**
		 * 카테고리(대그룹)
		 */
		Map<String, List<RankTrendGraphResult>> groupData = inputList.stream()
				.collect(Collectors.groupingBy(RankTrendGraphResult::getGroupName));

		int groupCount = groupData.size();
		logger.info("GroupSize[{}]", groupCount);

		for (String groupKey : groupData.keySet()) {

			List<RankTrendGraphResult> groupList = groupData.get(groupKey);

			/**
			 * 카테고리(중그룹=Series)
			 */
			Map<String, List<RankTrendGraphResult>> subGroupData = groupList.stream()
					.collect(Collectors.groupingBy(RankTrendGraphResult::getSubGroupName));

			logger.info("SubGroupSize[{}]", subGroupData.size());

			for (String subGroupKey : subGroupData.keySet()) {

				List<RankTrendGraphResult> subGroupList = subGroupData.get(subGroupKey);

				/* 소그룹 (Label) */
				Map<String, List<RankTrendGraphResult>> labelGroupData = subGroupList.stream()
						.collect(Collectors.groupingBy(RankTrendGraphResult::getLabel));
				for (String labelGroupKey : labelGroupData.keySet()) {

					Double min;
					Double max;
					Double Q1;
					Double median;
					Double Q3;

					List<RankTrendGraphResult> dataList = labelGroupData.get(labelGroupKey);
					int subGroupSize = dataList.size();

					List<Double> sequence = dataList.stream().map(RankTrendGraphResult::getDataValue).sorted()
							.collect(Collectors.toList());

					DescriptiveStatistics stats = new DescriptiveStatistics();
					for (Double data : sequence) {
						stats.addValue(data);
					}

					Percentile percentileImpl = new Percentile().withEstimationType(EstimationType.R_7)
							.withNaNStrategy(NaNStrategy.REMOVED)
							.withKthSelector(new KthSelector(new MedianOf3PivotingStrategy()));
					stats.setPercentileImpl(percentileImpl);

					min = stats.getMin();
					max = stats.getMax();
					Q1 = stats.getPercentile(25);
					median = stats.getPercentile(50);
					Q3 = stats.getPercentile(75);
					// MIN
					CamelCaseMap boxPlotItem = new CamelCaseMap();
					boxPlotItem.put(groupKeyName, groupKey);
					boxPlotItem.put(subGroupKeyName, subGroupKey);
					boxPlotItem.put(labelKeyName, labelGroupKey);
					boxPlotItem.put("min", min);
					boxPlotItem.put("max", max);
					boxPlotItem.put("q1", Q1);
					boxPlotItem.put("median", median);
					boxPlotItem.put("q3", Q3);
					boxPlots.add(boxPlotItem);

					logger.info("GROUP[{}] SUBGROUP[{}] LABEL[{}] MIN[{}] MAX[{}] Q1[{}] Q2[{}] Q3[{}]", new Object[] {
							groupKey, subGroupKey, labelGroupKey, min, max, Q1, median, Q3 });
				}
			}
		}

		logger.info("BoxPlots calculator size [{}]", boxPlots.size());

		return boxPlots;
	}

	public RankTrendResultData getRankTendOfSeperator(
			String factoryName, String startDate, String endDate, String div, String stepSeq, String productionType,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String intensity, String pl, String frameName, String subFrameName, String lotId, String type, String opt1,
			String filters
	) throws ParseException {

		RankTrendResultData rankTrendResultObj = new RankTrendResultData();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "intensities", intensity);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "subFrameNames", subFrameName);
		setArrayParam(parameter, "lotIds", lotId);
		parameter.put("type", type);
		parameter.put("opt1", opt1);
		setArrayParam(parameter, "filters", filters);
		List<ResultTableData> tableLists = null;
		if (stepSeq.contains("TP")) {
			tableLists = mapper.getRankTrendOfSeperatorTaping(parameter);
		} else {
			tableLists = mapper.getRankTrendOfSeperator(parameter);
		}

		rankTrendResultObj.setTable(tableLists);

		DozerBeanMapper mapper = new DozerBeanMapper();
		List<ResultTableData> chartLists = new ArrayList<>();
		for (ResultTableData obj : tableLists) {
			ResultTableData newObj = mapper.map(obj, ResultTableData.class);
			String tkInTime = HelpUtils.parseDate(newObj.getTkInTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkInTime(tkInTime);
			String tkOutTime = HelpUtils.parseDate(newObj.getTkOutTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkOutTime(tkOutTime);
			chartLists.add(newObj);
		}
		rankTrendResultObj.setChart(chartLists);

		return rankTrendResultObj;
	}

	public RankTrendResultData getRankTendOfMeasure(
			String factoryName, String startDate, String endDate, String div, String stepSeq, String productionType,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String intensity, String pl, String frameName, String subFrameName, String lotId, String type,
			String itemName
	) throws ParseException {

		RankTrendResultData rankTrendResultObj = new RankTrendResultData();
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "intensities", intensity);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "subFrameNames", subFrameName);
		setArrayParam(parameter, "lotIds", lotId);
		setArrayParam(parameter, "items", itemName);
		parameter.put("type", type);
		List<ResultMeasureTableData> tableLists = null;
		if (stepSeq.contains("TP")) {
			tableLists = mapper.getRankTrendOfMeasureTaping(parameter);
		} else {
			tableLists = mapper.getRankTrendOfMeasure(parameter);
		}

		rankTrendResultObj.setTable(tableLists);

		DozerBeanMapper mapper = new DozerBeanMapper();
		List<ResultMeasureTableData> chartLists = new ArrayList<>();
		for (ResultMeasureTableData obj : tableLists) {
			ResultMeasureTableData newObj = mapper.map(obj, ResultMeasureTableData.class);
			String tkInTime = HelpUtils.parseDate(newObj.getTkInTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkInTime(tkInTime);
			String tkOutTime = HelpUtils.parseDate(newObj.getTkOutTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkOutTime(tkOutTime);
			chartLists.add(newObj);
		}
		rankTrendResultObj.setChart(chartLists);

		return rankTrendResultObj;
	}

	public List<String> getBinNo(
			String factoryName, String productSpecName, String program, String type, String filters
	) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		parameter.put("type", type);
		setArrayParam(parameter, "filters", filters);
		List<CamelCaseMap> list = mapper.getBinNo(parameter);
		List<String> returnValues = new ArrayList<>();
		for (CamelCaseMap obj : list) {
			returnValues.add(obj.getString("binno"));
		}
		return returnValues;
	}

	public List<String> getBinNoTaping(
			String factoryName, String productSpecName, String program, String startDate, String endDate, String type,
			String filters
	) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("type", type);
		setArrayParam(parameter, "filters", filters);
		List<CamelCaseMap> list = mapper.getBinNoTaping(parameter);
		List<String> returnValues = new ArrayList<>();
		for (CamelCaseMap obj : list) {
			returnValues.add(obj.getString("binno"));
		}
		return returnValues;
	}

	public List<String> getRankName() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		List<CamelCaseMap> list = mapper.getRankName(parameter);
		List<String> returnValues = new ArrayList<>();
		for (CamelCaseMap obj : list) {
			returnValues.add(obj.getString("rankName"));
		}
		return returnValues;
	}

	public RankTrendResultData getRankTrendOfBoth(
			String factoryName, String startDate, String endDate, String div, String stepSeq, String productionType,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String intensity, String pl, String frameName, String subFrameName, String lotId, String type,
			String dataType, String itemName, String measureItem
	) throws Exception {

		logger.info("Get Target in Database");

		RankTrendResultData rankTrendResultObj = new RankTrendResultData();

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		parameter.put("type", type);
		parameter.put("itemName", itemName);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "intensities", intensity);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "subFrameNames", subFrameName);
		setArrayParam(parameter, "lotIds", lotId);
		setArrayParam(parameter, "measureItems", measureItem);

		List<ResultBothTableData> bothTableData = null;
		if (stepSeq.contains("TP")) {
			bothTableData = mapper.getRankTrendOfBothTaping(parameter);
		} else {
			bothTableData = mapper.getRankTrendOfBoth(parameter);
		}

		logger.info("Data Count[{}]", bothTableData.size());

		Map<String, List<ResultBothTableData>> group = bothTableData.stream()
				.collect(Collectors.groupingBy(ResultBothTableData::getRemoteFileName));

		logger.info("Data Groupsize[{}]", group.size());

		List<String> rankNames = getRankName();
		List<String> measureNames = Arrays.asList(measureItem.split(";"));
		logger.info("Ranknames : {}", String.join(",", rankNames));
		logger.info("MeasureItemTarget : {}", String.join(",", measureNames));
		Stat mode = Stat.Avg;
		if (dataType.equals("AVG")) {
			mode = Stat.Avg;
		} else if (dataType.equals("STD")) {
			mode = Stat.Std;
		} else if (dataType.equals("MEDIAN")) {
			mode = Stat.Median;
		}
		FTPClient ftpClient = ftpUtils.connect();
		for (String remoteFileName : group.keySet()) {
			List<ResultBothTableData> list = group.get(remoteFileName);
			if (list.size() == 0) {
				continue;
			}
			String partId = list.get(0).getProductSpecName();
			String programName = list.get(0).getProgram();
			logger.info("Get BinNumbers [{}][{}]", partId, programName);
			List<String> binNos = null;
			if (stepSeq.contains("TP")) {
				binNos = getBinNoTaping(factoryName, partId, programName, startDate, endDate, type, itemName);
			} else {
				binNos = getBinNo(factoryName, partId, programName, type, itemName);
			}

			logger.info("Download filename[{}]", remoteFileName);

			File downloadFile = ftpUtils.downloadFile(ftpClient, remoteFileName);

			logger.info("Parse MeasureDataMap");
			Map<String, List<Double>> parseMap = RankTrendUtils.parseMeasureDataMap(downloadFile, rankNames,
					measureNames, binNos);
			if (parseMap == null) {
				logger.error("Cannot found parseMap Data[{}]", remoteFileName);
				continue;
			}
			logger.info("MeasureDataMap data count[{}]", parseMap.size());
			logger.info("StatisticsDataMap");
			Map<String, Double> dataMap = RankTrendUtils.statisticsDataMap(parseMap, mode);
			for (ResultBothTableData obj : list) {
				String measureId = obj.getMeasureId();
				Double dataValue = dataMap.get(measureId);
				obj.setDataValue(dataValue.toString());
			}
		}
		ftpUtils.disconnect(ftpClient);

		rankTrendResultObj.setTable(bothTableData);

		DozerBeanMapper mapper = new DozerBeanMapper();
		List<ResultBothTableData> chartLists = new ArrayList<>();
		for (ResultBothTableData obj : bothTableData) {
			ResultBothTableData newObj = mapper.map(obj, ResultBothTableData.class);
			String tkInTime = HelpUtils.parseDate(newObj.getTkInTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkInTime(tkInTime);
			String tkOutTime = HelpUtils.parseDate(newObj.getTkOutTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			newObj.setTkOutTime(tkOutTime);
			chartLists.add(newObj);
		}
		rankTrendResultObj.setChart(chartLists);

		return rankTrendResultObj;
	}

}
