package com.compact.base.domain.analysis;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compact.base.AnalysisController;
import com.compact.base.domain.analysis.DTO.ChartShareData;
import com.compact.base.domain.analysis.DTO.YieldFileItemData;

public class AnalysisUtils {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisUtils.class);

	public static List<String> getItemNames(File file) throws IOException {

		List<String> ItemNames = new ArrayList<String>();

		String fileAllString = FileUtils.readFileToString(file);

		String[] lines = fileAllString.split("\r\n");

		// Find measure data row index.
		int measureIndex = -1;
		int itemNameIndex = -1;
		for (int i = 0; i < lines.length; i++) {

			if (lines[i].toLowerCase().startsWith("[measurement data]")) {
				measureIndex = i;
			} else if (measureIndex >= 0 && lines[i].toLowerCase().startsWith("no.")) {
				itemNameIndex = i;
				break;
			}
		}

		String[] itemNameValues = lines[itemNameIndex].split(",");
		for (String itemNameValue : itemNameValues) {
			ItemNames.add(itemNameValue);
		}

		return ItemNames;
	}

	public static List<String> getItemFilter(String itemFilter) {

		if (!itemFilter.isEmpty()) {
			return Arrays.asList(itemFilter.split(","));
		} else {
			return null;
		}
	}

	public static List<Double> getItemValueList(File file, String itemName) throws IOException {

		List<Double> itemValues = new ArrayList<Double>();

		String fileAllString = FileUtils.readFileToString(file);

		String[] lines = fileAllString.split("\r\n");

		// 각 데이터의 행 번호를 추출한다.
		int measureIndex = -1;
		int itemNameIndex = -1;
		int itemStartIndex = -1;
		for (int i = 0; i < lines.length; i++) {

			if (lines[i].toLowerCase().startsWith("[measurement data]")) {
				measureIndex = i;
			} else if (measureIndex >= 0 && lines[i].toLowerCase().startsWith("no.")) {
				itemNameIndex = i;
			} else if (itemNameIndex >= 0 && lines[i].toLowerCase().startsWith("1")) {
				itemStartIndex = i;
				break;
			}
		}

//		logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}].",
//				new Object[] { measureIndex, itemNameIndex, itemStartIndex });

		if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {

			for (int i = itemStartIndex; i < lines.length; i++) {

				if (lines[i].isEmpty())
					break;

				String[] itemParams = lines[i].split(",");

				for (int j = 0; j < itemParams.length; j++) {

					String crntItemName = lines[itemNameIndex].split(",")[j];

					if (crntItemName.equals(itemName)) {

						Double measureValue = NumberUtils.toDouble(itemParams[j], -99999);

						if (measureValue != -99999) {
							// logger.info("Add mesuredata item[{}] value[{}].", new Object[] { itemName,
							// measureValue } );
							itemValues.add(measureValue);
						}
					}
				}
			}
		}

//		logger.info("Item value count [{}] added.", new Object[] { itemValues.size() });

		return itemValues;
	}

	public static List<ChartShareData> createShareTable(Double min, Double max, int stepCount, String itemName) {

		List<ChartShareData> shareTable = new ArrayList<ChartShareData>();

		Double increaseValue = (max - min) / stepCount;

		int seq = 1;

		ChartShareData first = new ChartShareData();

		first.setGroupId(String.format("G%d", seq++));

		first.setItemName(itemName);

		first.setMin(-99999);

		first.setMax(min);

		first.setChipCount(0L);

		first.setChipPercent(new BigDecimal(0));

		first.setRangeDescription(String.format(" ~ %.3f", min));

		shareTable.add(first);

		double preValue = min;

		for (double i = min + increaseValue; i < max; i += increaseValue) {

			ChartShareData range = new ChartShareData();

			range.setGroupId(String.format("G%d", seq++));

			range.setItemName(itemName);

			range.setMin(preValue);

			range.setMax(i);

			range.setRangeDescription(String.format("%.3f ~ %.3f", preValue, i));

			range.setChipCount(0L);

			range.setChipPercent(new BigDecimal(0));

			shareTable.add(range);

			preValue = i;

		}

		ChartShareData last = new ChartShareData();

		last.setGroupId(String.format("G%d", seq++));

		last.setItemName(itemName);

		last.setMin(max);

		last.setMax(99999);

		last.setChipCount(0L);

		last.setChipPercent(new BigDecimal(0));

		last.setRangeDescription(String.format("%.3f ~ ", max));

		shareTable.add(last);

		return shareTable;

	}

	public static void calculateChipCount(List<ChartShareData> shareTable, List<Double> totalMeasureDatas) {

		for (ChartShareData shareObj : shareTable) {

			Stream<Double> foundData = totalMeasureDatas.stream()
					.filter(o -> shareObj.getMin() < o && o <= shareObj.getMax());

			if (foundData != null) {

				Long cnt = foundData.count();

//				logger.info("Find chip count min[{}] max[{}] count[{}]",
//						new Object[] { shareObj.getMin(), shareObj.getMax(), cnt });

				Long chipCount = shareObj.getChipCount();

//				logger.info("chipCount[{}]", chipCount);

				shareObj.setChipCount(chipCount + cnt);

				
			} else {

//				logger.warn("Cannot found chip count min[{}] max[{}] count[{}] percent[{}]", new Object[] {
//						shareObj.getMin(), shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });
			}
		}

		Long TotalChipCount = shareTable.stream().mapToLong(i -> i.getChipCount()).sum();

		logger.info("Chip TotalCount [{}]", TotalChipCount);

//		logger.info("CHIPCOUNT\tCHIPPER");

		for (ChartShareData shareObj : shareTable) {

			Long chipCount = shareObj.getChipCount();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);
			
			if (!bChipCount.equals(BigDecimal.ZERO)) {
				
				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				shareObj.setChipPercent(chipPer);
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());
			
			logger.info("[{}] min[{}] max[{}] count[{}] percent[{}]", new Object[] { 
					shareObj.getRangeDescription(),
					shareObj.getMin(),
					shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });

		}
	}
}
