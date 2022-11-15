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
import org.dozer.DozerBeanMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compact.base.AnalysisController;
import com.compact.base.domain.analysis.DTO.ChartShareData;
import com.compact.base.domain.analysis.DTO.FileLocation;
import com.compact.base.domain.analysis.DTO.TableShareData;
import com.compact.base.domain.analysis.DTO.YieldFileItemData;

public class AnalysisUtils {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisUtils.class);

	/**
	 * 파일에서 ITEMID의 값을 추출하여 반환한다.
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
	 * 파일에서 측정항목에 대한 값을 반환한다.
	 * 값이 숫자형이 아닌 경우에는 포함하지 않는다.
	 * @param file
	 * @param itemName
	 * @return 측정값 리스트
	 * @throws IOException
	 */
	public static List<Double> getItemValueList(File file, String itemName) throws IOException {

		List<Double> itemValues = new ArrayList<Double>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		int measureIndex = -1;
		int itemNameIndex = -1;
		int itemStartIndex = -1;
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
		}
		if (itemNameIndex == -1) {
			logger.error("Cannot found measure item name index. [{}][{}]", itemName, file.getName());
		}
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
		}

//		logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}].",
//				new Object[] { measureIndex, itemNameIndex, itemStartIndex });

		if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				if (contentLines[i].isEmpty()) break;
				String[] itemParams = contentLines[i].split(",");
				for (int j = 0; j < itemParams.length; j++) {
					String crntItemName = contentLines[itemNameIndex].split(",")[j];
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

	/**
	 * MIN~MAX 범위 데이터 테이블 맵을 생성한다.
	 * @param min
	 * @param max
	 * @param stepCount
	 * @param itemName
	 * @return MIN_MAX Map
	 */
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

	/**
	 * MinMax형 Table Map을 생성한다.
	 * 처음과 마지막의 Limit가 없는 부분은 -99999, 99999로 구분
	 * @param JsonString UI Client단에서 Json형식의 Map
	 * @param itemName 검출할 측정 아이템명
	 * @return
	 */
	public static List<ChartShareData> parseMinMaxTable(String JsonString, String itemName) {
		logger.info("Json:[{}]", JsonString);
		JSONArray jsonArrayObj = new JSONArray(JsonString);
		int length = jsonArrayObj.length();
		List<ChartShareData> shares = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject obj = jsonArrayObj.getJSONObject(i);
			String groupId = obj.get("group").toString();
			String flag = obj.get("flag").toString();
			Double min = -99999d;
			Double max = 999999d;
			String rangeDescription = "";
			if (flag.equals("first")) {
				max = Double.valueOf(obj.get("max").toString());
				rangeDescription = String.format(" ~ %.3f", max);
			} else if (flag.equals("last")) {
				min = Double.valueOf(obj.get("min").toString());
				rangeDescription = String.format("%.3f ~", min);
			} else {
				min = Double.valueOf(obj.get("min").toString());
				max = Double.valueOf(obj.get("max").toString());
				rangeDescription = String.format("%.3f ~ %.3f", min, max);
			}
			if (!groupId.isEmpty()) {
				ChartShareData chartObj = new ChartShareData();
				chartObj.setGroupId(groupId);
				chartObj.setItemName(itemName);
				chartObj.setMin(min);
				chartObj.setMax(max);
				chartObj.setRangeDescription(rangeDescription);
				chartObj.setChipCount(0L);
				chartObj.setChipPercent(new BigDecimal(0));
				shares.add(chartObj);
			}
		}
		return shares;
	}

	/**
	 * 전체 Chip Count의 점유율을 분석 한다.
	 * @param shareTable
	 * @param totalMeasureDatas
	 */
	public static void calculateChipCount(List<ChartShareData> shareTable, List<Double> totalMeasureDatas) {

		for (ChartShareData shareObj : shareTable) {

			//logger.info("Min[{}]", shareObj.getMin());
			//logger.info("Max[{}]", shareObj.getMax());

			Stream<Double> foundData = totalMeasureDatas.stream()
					.filter(o -> shareObj.getMin() < o && o <= shareObj.getMax());

			if (foundData != null) {

				Long cnt = foundData.count();

				logger.info("Find chip count min[{}] max[{}] count[{}]",
						new Object[] { shareObj.getMin(), shareObj.getMax(), cnt });

				Long chipCount = shareObj.getChipCount();

				logger.info("chipCount[{}]", chipCount);

				shareObj.setChipCount(chipCount + cnt);

			} else {

				logger.warn("Cannot found chip count min[{}] max[{}] count[{}] percent[{}]", new Object[] {
						shareObj.getMin(), shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });
			}
		}

		Long TotalChipCount = shareTable.stream().mapToLong(i -> i.getChipCount()).sum();

		logger.info("Chip TotalCount [{}]", TotalChipCount);

		logger.info("CHIPCOUNT\tCHIPPER");

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

			logger.info("[{}] min[{}] max[{}] count[{}] percent[{}]", new Object[] { shareObj.getGroupId(),
					shareObj.getMin(), shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });

		}
	}

	/**
	 * Lot에 대한 Chip Count 점유율을 분석한다.
	 * @param shareTable
	 * @param info
	 * @param totalMeasureDatas
	 * @return
	 */
	public static List<TableShareData> calculatorChipCountOfLot(List<ChartShareData> shareTable, FileLocation info,
			List<Double> totalMeasureDatas) {

		List<TableShareData> tableDatas = new ArrayList<>();

		for (ChartShareData shareObj : shareTable) {

			DozerBeanMapper mapper = new DozerBeanMapper();

			TableShareData tableData = mapper.map(info, TableShareData.class);

			//logger.info("LOT[{}]", tableData.getLotId());

			tableDatas.add(tableData);

			tableData.setGroupName(shareObj.getGroupId());

			tableData.setRange(shareObj.getRangeDescription());

			tableData.setChipCnt(0L);

			tableData.setChipPer(new BigDecimal(0));

			//logger.info("Min[{}]", shareObj.getMin());
			//logger.info("Max[{}]", shareObj.getMax());

			Stream<Double> foundData = totalMeasureDatas.stream()
					.filter(o -> shareObj.getMin() < o && o <= shareObj.getMax());

			if (foundData != null) {

				Long cnt = foundData.count();

				logger.info("Find chip count min[{}] max[{}] count[{}]",
						new Object[] { shareObj.getMin(), shareObj.getMax(), cnt });

				Long chipCount = tableData.getChipCnt();

				logger.info("chipCount[{}]", chipCount);

				tableData.setChipCnt(cnt);

			} else {

				logger.warn("Cannot found chip count min[{}] max[{}] count[{}] percent[{}]", new Object[] {
						shareObj.getMin(), shareObj.getMax(), tableData.getChipCnt(), tableData.getChipPer() });
			}
		}

		Long TotalChipCount = tableDatas.stream().mapToLong(i -> i.getChipCnt()).sum();

		logger.info("Chip TotalCount [{}]", TotalChipCount);

		logger.info("CHIPCOUNT\tCHIPPER");

		for (TableShareData tableData : tableDatas) {

			Long chipCount = tableData.getChipCnt();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);

			tableData.setTChipCnt(TotalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				tableData.setChipPer(chipPer);
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());

			logger.info("LOTID[{}] [{}][{}] count[{}] percent[{}]", new Object[] { tableData.getLotId(),
					tableData.getGroupName(), tableData.getRange(), tableData.getChipCnt(), tableData.getChipPer() });

		}

		return tableDatas;
	}
}
