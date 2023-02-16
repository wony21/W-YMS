package com.compact.yms.domain.ranktrend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RankTrendUtils {

	private static final Logger logger = LoggerFactory.getLogger(RankTrendUtils.class);

	/**
	 * 파일에서 ITEMID의 값을 추출하여 반환한다.
	 * 
	 * @param file
	 * @return 측정ITEMID
	 * @throws IOException
	 */
	public static List<String> getItemNames(File file) throws IOException {
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		int measureIndex = -1;
		int itemNameIndex = -1;
		for (int i = 0; i < contentLines.length; i++) {
			if (contentLines[i].toLowerCase().startsWith("[measurement data]")) {
				measureIndex = i;
			} else if (measureIndex >= 0 && contentLines[i].toLowerCase().startsWith("no.")) {
				itemNameIndex = i;
				break;
			}
		}
		String[] itemNameValues = contentLines[itemNameIndex].split(",");
		return Arrays.asList(itemNameValues);
	}

	/**
	 * 콤마(,)로 구분된 Filter데이터를 List형식으로 변환한다.
	 * 
	 * @param itemFilter
	 * @return 변환된 ItemFilter 리스트
	 */
	public static List<String> getItemFilter(String itemFilter) {
		if (!itemFilter.isEmpty()) {
			return Arrays.asList(itemFilter.split(","));
		} else {
			return null;
		}
	}

	/**
	 * 파일에서 분석대상 측정값을 map형식으로 반환한다.
	 * 
	 * @param file         분석하려는 파일
	 * @param rankNames    BIN번호의 측정아이템명의 목록(ERP_SEPERATOR에서 가져온다)
	 * @param measureItems 분석대상 측정아이템명
	 * @param rankIds      분석대상 BIN번호
	 * @return (측정아이템명),(측정값리스트) MAP 반환
	 * @throws Exception
	 */
	public static Map<String, List<Double>> parseMeasureDataMap(File file, List<String> rankNames,
			List<String> measureItems, List<String> rankIds) throws Exception {

		Map<String, List<Double>> map = new HashMap<String, List<Double>>();
		for (String measureId : measureItems) {
			map.put(measureId, new ArrayList<Double>());
		}

		List<Double> itemValues = new ArrayList<Double>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// ----------------------------------------------------------
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		// ----------------------------------------------------------
		int measureIndex = -1;
		int itemNameIndex = -1;
		int itemStartIndex = -1;
		int rankColIndex = -1;
		for (int i = 0; i < contentLines.length; i++) {
			if (contentLines[i].toLowerCase().startsWith("[measurement data]")) {
				measureIndex = i;
			} else if (measureIndex >= 0 && contentLines[i].toLowerCase().startsWith("no.")) {
				itemNameIndex = i;
			} else if (itemNameIndex >= 0 && contentLines[i].toLowerCase().startsWith("1")) {
				itemStartIndex = i;
				break;
			}
		}
		// 각 행번호 유효성 검사
		if (measureIndex == -1) {
			logger.error("Cannot found measure data index. [{}]", file.getName());
			return null;
		}
		if (itemNameIndex == -1) {
			logger.error("Cannot found measure item name index. [{}]", file.getName());
			return null;
		}
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
			return null;
		}

		// ----------------------------------------------------------
		// Rank Column 찾기
		// ----------------------------------------------------------
		String[] itemNames = contentLines[itemNameIndex].split(",");
		for (int i = 0; i < itemNames.length; i++) {
			if (rankNames.contains(itemNames[i])) {
				rankColIndex = i;
				logger.info(String.format("RankID[%s]", itemNames[i]));
				break;
			}
		}
		if (rankColIndex == -1) {
			logger.error("Cannot found rankId.");
			return null;
		}

		// ----------------------------------------------------------
		// 측정값 데이터를 추출
		// ----------------------------------------------------------
		if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				if (contentLines[i].isEmpty())
					break;

				String[] itemParams = contentLines[i].split(",");

				String binNo = itemParams[rankColIndex];

				// logger.info("FileBinNo[{}] RankIds{} -- [{}]", new Object[] { binNo,
				// ArrayUtils.toString(rankIds), rankIds.get(0) });

				if (rankIds.contains(binNo)) {

					// logger.info("BinNo[{}] Found!!!", binNo);
					for (int j = 0; j < itemParams.length; j++) {

						String itemName = contentLines[itemNameIndex].split(",")[j];

						if (measureItems.contains(itemName)) {
							// logger.info("MeasureItemname[{}] Found!!!", measureItems);
							Double measureValue = NumberUtils.toDouble(itemParams[j], -99999);

							if (measureValue != -99999) {

								if (map.containsKey(itemName)) {
									List<Double> dataValues = map.get(itemName);
									//logger.info("measureValue data add!!");
									dataValues.add(measureValue);
								}
							}
						}
					}
				}
			}
		}

		return map;
	}

	public enum Stat {
		Avg, Std, Median
	}

	public static Map<String, Double> statisticsDataMap(Map<String, List<Double>> map, Stat mode) {

		Map<String, Double> dataMap = new HashMap<>();

		for (String keyValue : map.keySet()) {

			Double dataValue = 0d;
			List<Double> dataList = map.get(keyValue);
			logger.info("Both statistics ITEM[{}] COUNT[{}]", new Object[] {
					keyValue, dataList.size() });
			if (dataList.size() > 0) {

				if (mode.equals(Stat.Avg)) {
					dataValue = dataList.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
				} else if (mode.equals(Stat.Std)) {
					Collections.sort(dataList);
					double[] sortedData = dataList.stream().mapToDouble(i -> i).toArray();
					StandardDeviation std = new StandardDeviation();
					dataValue = std.evaluate(sortedData);
				} else if (mode.equals(Stat.Median)) {
					Collections.sort(dataList);
					double[] sortedData = dataList.stream().mapToDouble(i -> i).toArray();
					Median median = new Median();
					dataValue = median.evaluate(sortedData);
				}
			}
			dataMap.put(keyValue, dataValue);
		}

		return dataMap;
	}

	/*
	 * public static List<ResultBothTableData> getMeasureItemValues(File file,
	 * List<String> measureItemNames, List<String> itemNames) {
	 * 
	 * List<ResultBothTableData> list = new ArrayList<>(); for(String meausureItem :
	 * measureItemNames) { for(String itemName : itemNames) { ResultBothTableData
	 * obj = new ResultBothTableData(); obj.setMeasureItem(meausureItem);
	 * obj.setItemId(itemName); obj.setDataValue(""); list.add(obj); } } }
	 */
}
