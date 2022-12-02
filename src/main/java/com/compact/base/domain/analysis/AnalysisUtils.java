package com.compact.base.domain.analysis;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
import com.compact.base.domain.analysis.DTO.ChartShareBothData;
import com.compact.base.domain.analysis.DTO.ChartShareCieData;
import com.compact.base.domain.analysis.DTO.ChartShareData;
import com.compact.base.domain.analysis.DTO.FileLocation;
import com.compact.base.domain.analysis.DTO.ShareDataObject;
import com.compact.base.domain.analysis.DTO.TableShareData;
import com.compact.base.domain.analysis.DTO.YieldFileItemData;

import math.geom2d.Point2D;
import math.geom2d.polygon.SimplePolygon2D;

public class AnalysisUtils {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisUtils.class);

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
	 * 파일에서 측정항목에 대한 값을 반환한다. 값이 숫자형이 아닌 경우에는 포함하지 않는다.
	 * 
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
				if (contentLines[i].isEmpty())
					break;
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

	public static List<Point> getItemPointValueList(File file, String cieX, String cieY) throws IOException {

		List<Point> itemValues = new ArrayList<Point>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		int measureIndex = -1;
		int itemNameIndex = -1;
		int itemStartIndex = -1;
		int cieXIndex = -1;
		int cieYIndex = -1;
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
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
		}

		// Find CIE-X CIE-Y Position
		String[] itemNames = contentLines[itemNameIndex].split(",");
		for (int i = 0; i < itemNames.length; i++) {
			if (itemNames[i].toLowerCase().equals(cieX.toLowerCase())) {
				cieXIndex = i;
			} else if (itemNames[i].toLowerCase().equals(cieY.toLowerCase())) {
				cieYIndex = i;
			}
		}

		// 선행으로 검사하기 때문에 사실상 의미는 없다.
		if (cieXIndex == -1) {
			logger.error("Cannot found cie-x index. [{}]", file.getName());
		}
		if (cieYIndex == -1) {
			logger.error("Cannot found cie-y index. (no has '1') [{}]", file.getName());
		}

//		logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}].",
//				new Object[] { measureIndex, itemNameIndex, itemStartIndex });

		if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				if (contentLines[i].isEmpty())
					break;

				String[] itemParams = contentLines[i].split(",");
				String cieXValue = itemParams[cieXIndex];
				String cieYValue = itemParams[cieYIndex];
				if (!cieXValue.isEmpty() && !cieYValue.isEmpty()) {
					Double x = NumberUtils.toDouble(cieXValue);
					Double y = NumberUtils.toDouble(cieYValue);
					Point point = new Point();
					point.setLocation(replaceDoubleData(x), replaceDoubleData(y));
					itemValues.add(point);
				}
			}
		}
//		logger.info("Item value count [{}] added.", new Object[] { itemValues.size() });
		return itemValues;
	}

	public static List<ShareDataObject> getItemBothValueList(File file, String itemName, String cieX, String cieY)
			throws IOException {

		List<ShareDataObject> itemValues = new ArrayList<ShareDataObject>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		int measureIndex = -1;
		int itemNameIndex = -1;
		int itemStartIndex = -1;
		int cieXIndex = -1;
		int cieYIndex = -1;
		int itemIndex = -1;
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
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
		}

		// Find CIE-X CIE-Y Position
		String[] itemNames = contentLines[itemNameIndex].split(",");
		for (int i = 0; i < itemNames.length; i++) {
			if (itemNames[i].toLowerCase().equals(cieX.toLowerCase())) {
				cieXIndex = i;
			} else if (itemNames[i].toLowerCase().equals(cieY.toLowerCase())) {
				cieYIndex = i;
			} else if (itemNames[i].toLowerCase().equals(itemName.toLowerCase())) {
				itemIndex = i;
			}
		}

		// 선행으로 검사하기 때문에 사실상 의미는 없다.
		if (cieXIndex == -1) {
			logger.error("Cannot found cie-x index. [{}]", file.getName());
		}
		if (cieYIndex == -1) {
			logger.error("Cannot found cie-y index. (no has '1') [{}]", file.getName());
		}
		if (cieYIndex == -1) {
			logger.error("Cannot found item index. [{}] [{}]", itemName, file.getName());
		}

//		logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}].",
//				new Object[] { measureIndex, itemNameIndex, itemStartIndex });

		if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				if (contentLines[i].isEmpty())
					break;

				String[] itemParams = contentLines[i].split(",");
				String cieXValue = itemParams[cieXIndex];
				String cieYValue = itemParams[cieYIndex];
				String itemValue = itemParams[itemIndex];
				if (!cieXValue.isEmpty() && !cieYValue.isEmpty()) {
					Double x = NumberUtils.toDouble(cieXValue);
					Double y = NumberUtils.toDouble(cieYValue);
					Double value = NumberUtils.toDouble(itemValue);
					Point point = new Point();
					point.setLocation(replaceDoubleData(x), replaceDoubleData(y));
					ShareDataObject newObj = new ShareDataObject();
					newObj.setItemValue(value);
					newObj.setCieValue(point);
					itemValues.add(newObj);
					/*
					logger.info("ItemValue : ITEMNAME[{}] VALUE[{}] CIE-X[{}:{}] CIE-Y[{}:{}]",
							new Object[] { itemName, value, cieX, x, cieY, y });
					*/
				}
			}
		}
//		logger.info("Item value count [{}] added.", new Object[] { itemValues.size() });
		return itemValues;
	}

	/**
	 * 점유율 분석을 위한 보정치 100000을 곱한다.
	 * 
	 * @param data
	 * @return
	 */
	public static Integer replaceDoubleData(Double data) {

		double d = data == 0d ? 1d : data;

		Double ret = data * 100000;

		return ret.intValue();
	}

	/**
	 * MIN~MAX 범위 데이터 테이블 맵을 생성한다.
	 * 
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

		first.setChipPercent(0d);

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

			range.setChipPercent(0d);

			shareTable.add(range);

			preValue = i;

		}

		ChartShareData last = new ChartShareData();

		last.setGroupId(String.format("G%d", seq++));

		last.setItemName(itemName);

		last.setMin(max);

		last.setMax(99999);

		last.setChipCount(0L);

		last.setChipPercent(0d);

		last.setRangeDescription(String.format("%.3f ~ ", max));

		shareTable.add(last);

		return shareTable;

	}

	/**
	 * MinMax형 Table Map을 생성한다. 처음과 마지막의 Limit가 없는 부분은 -99999, 99999로 구분
	 * 
	 * @param JsonString UI Client단에서 Json형식의 Map
	 * @param itemName   검출할 측정 아이템명
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
				chartObj.setChipPercent(0d);
				shares.add(chartObj);
			}
		}
		return shares;
	}

	/**
	 * MinMax형 Table Map을 생성한다. 처음과 마지막의 Limit가 없는 부분은 -99999, 99999로 구분
	 * 
	 * @param JsonString UI Client단에서 Json형식의 Map
	 * @param itemName   검출할 측정 아이템명
	 * @return
	 */
	public static List<ChartShareCieData> parseCieTable(String JsonString) {
		logger.info("Json:[{}]", JsonString);
		JSONArray jsonArrayObj = new JSONArray(JsonString);
		int length = jsonArrayObj.length();
		List<ChartShareCieData> shares = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			JSONObject obj = jsonArrayObj.getJSONObject(i);
			String name = obj.get("name").toString();
			Double x1 = obj.getDouble("x1");
			Double y1 = obj.getDouble("y1");
			Double x2 = obj.getDouble("x2");
			Double y2 = obj.getDouble("y2");
			Double x3 = obj.getDouble("x3");
			Double y3 = obj.getDouble("y3");
			Double x4 = obj.getDouble("x4");
			Double y4 = obj.getDouble("y4");
			if (!name.isEmpty()) {
				ChartShareCieData chartObj = new ChartShareCieData();
				chartObj.setGroupId(name);
				chartObj.setCieName(name);
				chartObj.setX1(x1.toString());
				chartObj.setY1(y1.toString());
				chartObj.setX2(x2.toString());
				chartObj.setY2(y2.toString());
				chartObj.setX3(x3.toString());
				chartObj.setY3(y3.toString());
				chartObj.setX4(x4.toString());
				chartObj.setY4(y4.toString());

				Polygon poly = new Polygon();
				poly.addPoint(replaceDoubleData(x1), replaceDoubleData(y1));
				poly.addPoint(replaceDoubleData(x2), replaceDoubleData(y2));
				poly.addPoint(replaceDoubleData(x3), replaceDoubleData(y3));
				poly.addPoint(replaceDoubleData(x4), replaceDoubleData(y4));

				chartObj.setCieRectangle(poly);
				chartObj.setChipCount(0L);
				chartObj.setChipPercent(0d);
				shares.add(chartObj);
			}
		}
		// outside
		ChartShareCieData outside = new ChartShareCieData();
		outside.setGroupId("outside");
		outside.setCieName("outside");
		outside.setChipCount(0L);
		outside.setChipPercent(0D);
		outside.setX1("-");
		outside.setY1("-");
		outside.setX2("-");
		outside.setY2("-");
		outside.setX3("-");
		outside.setY3("-");
		outside.setX4("-");
		outside.setY4("-");
		shares.add(outside);
		return shares;
	}
	
	public static List<ChartShareData> createChartShareDataForBoth(String rangeJson, String itemName) {
		JSONArray rangeJsonArray = new JSONArray(rangeJson);
		int rangeLength = rangeJsonArray.length();
		List<ChartShareData> rangeLists = new ArrayList<>();
		for (int j = 0; j < rangeLength; j++) {
			JSONObject rangeJsonObj = rangeJsonArray.getJSONObject(j);
			String groupId = rangeJsonObj.getString("group");
			String flag = rangeJsonObj.getString("flag");
			Double min = -99999d;
			Double max = 999999d;
			String rangeDesc = "";
			if (flag.equals("first")) {
				max = Double.valueOf(rangeJsonObj.get("max").toString());
				rangeDesc = String.format(" ~ %.3f", max);
			} else if (flag.equals("last")) {
				min = Double.valueOf(rangeJsonObj.get("min").toString());
				rangeDesc = String.format("%.3f ~", min);
			} else {
				min = Double.valueOf(rangeJsonObj.get("min").toString());
				max = Double.valueOf(rangeJsonObj.get("max").toString());
				rangeDesc = String.format("%.3f ~ %.3f", min, max);
			}
			ChartShareData newObj = new ChartShareData();
			newObj.setGroupId(groupId);
			newObj.setItemName(itemName);
			newObj.setRangeDescription(rangeDesc);
			newObj.setMin(min);
			newObj.setMax(max);
			newObj.setSort(j);
			newObj.setChipCount(0L);
			newObj.setChipPercent(0d);
			rangeLists.add(newObj);
		}
		return rangeLists;
	}

	public static List<ChartShareBothData> parseBothTable(String cieJson, String rangeJson, String itemName) {
		logger.info("[MODE:BOTH] ParseBoth Limited Table.");
		
		JSONArray cieJsonArray = new JSONArray(cieJson);
		int cieArrayLength = cieJsonArray.length();
		List<ChartShareBothData> shares = new ArrayList<>();
		for (int i = 0; i < cieArrayLength; i++) {
			JSONObject cieJsonObj = cieJsonArray.getJSONObject(i);
			String cieName = cieJsonObj.getString("name").toString();
			Double x1 = cieJsonObj.getDouble("x1");
			Double y1 = cieJsonObj.getDouble("y1");
			Double x2 = cieJsonObj.getDouble("x2");
			Double y2 = cieJsonObj.getDouble("y2");
			Double x3 = cieJsonObj.getDouble("x3");
			Double y3 = cieJsonObj.getDouble("y3");
			Double x4 = cieJsonObj.getDouble("x4");
			Double y4 = cieJsonObj.getDouble("y4");
			Polygon cieRectangle = new Polygon();
			cieRectangle.addPoint(replaceDoubleData(x1), replaceDoubleData(y1));
			cieRectangle.addPoint(replaceDoubleData(x2), replaceDoubleData(y2));
			cieRectangle.addPoint(replaceDoubleData(x3), replaceDoubleData(y3));
			cieRectangle.addPoint(replaceDoubleData(x4), replaceDoubleData(y4));
			List<ChartShareData> ranges = createChartShareDataForBoth(rangeJson, itemName);
			ChartShareBothData newObj = new ChartShareBothData();
			newObj.setCieName(cieName);
			newObj.setCieRectangle(cieRectangle);
			newObj.setSort(i);
			newObj.setChipCount(0L);
			newObj.setChipPercent(0D);
			newObj.setRange(ranges);
			shares.add(newObj);
		}
		// outside
		ChartShareBothData outsideObj = new ChartShareBothData();
		outsideObj.setCieName("outside");
		outsideObj.setChipCount(0L);
		outsideObj.setChipPercent(0D);
		outsideObj.setSort(cieArrayLength + 1);
		List<ChartShareData> outsideRange = createChartShareDataForBoth(rangeJson, itemName);
		outsideObj.setRange(outsideRange);
		shares.add(outsideObj);
		// Log
		for (ChartShareBothData share : shares) {
			List<ChartShareData> ranges = share.getRange();
			for (ChartShareData range : ranges) {
				String cieName = share.getCieName();
				String groupId = range.getGroupId();
				String item = range.getItemName();
				String rangeName = range.getRangeDescription();
				//logger.info("CIE[{}] GROUP[{}] ITEM[{}] RANGE[{}]", new Object[] { cieName, groupId, item, rangeName });
			}
		}

		return shares;
	}

	/**
	 * 전체 Chip Count의 점유율을 분석 한다.
	 * 
	 * @param shareTable
	 * @param totalMeasureDatas
	 */
	public static void calculateChipCount(List<ChartShareData> shareTable, List<Double> totalMeasureDatas) {

		for (ChartShareData shareObj : shareTable) {

			// logger.info("Min[{}]", shareObj.getMin());
			// logger.info("Max[{}]", shareObj.getMax());

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

		logger.info("Chip percent calculating start...");

		for (ChartShareData shareObj : shareTable) {

			Long chipCount = shareObj.getChipCount();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				shareObj.setChipPercent(chipPer.doubleValue());
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());

			logger.info("[{}] min[{}] max[{}] count[{}] percent[{}]", new Object[] { shareObj.getGroupId(),
					shareObj.getMin(), shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });

		}

		logger.info("Chip percent calculating end...");
	}

	/**
	 * 전체 Cie Chip Count의 점유율을 분석 한다.
	 * 
	 * @param shareTable
	 * @param totalMeasureDatas
	 */
	public static void calculateCieChipCount(List<ChartShareCieData> shareTable, List<Point> totalMeasureDatas) {

		@SuppressWarnings("unchecked")
		List<Point> measureData = (List<Point>) ((ArrayList<Point>) totalMeasureDatas).clone();

		Long totalChipCount = Long.valueOf(measureData.size());

		logger.info("CIE-XY measure data total count[{}]", totalChipCount);

		for (ChartShareCieData shareObj : shareTable) {

			List<Point> containPoints = measureData.stream().filter(o -> shareObj.getCieRectangle().contains(o))
					.collect(Collectors.toList());

			if (containPoints != null) {
				Long chipCount = Long.valueOf(containPoints.size());
				logger.info("Found Chip Count CIE[{}] COUNT[{}]", shareObj.getGroupId(), chipCount);
				shareObj.setChipCount(chipCount);
				measureData.removeAll(containPoints);
			} else {

				logger.warn("Cannot found chip count CIE[{}] COUNT[{}] PERCENT[{}]",
						new Object[] { shareObj.getGroupId(), shareObj.getChipCount(), shareObj.getChipPercent() });
			}
		}

		// outside
		Long outsideChipCount = Long.valueOf(measureData.size());
		logger.info("outside chip count[{}]", outsideChipCount);
		ChartShareCieData outside = new ChartShareCieData();
		outside.setGroupId("outside");
		outside.setCieName("outside");
		outside.setChipCount(outsideChipCount);
		outside.setChipPercent(0D);
		shareTable.add(outside);

		logger.info("CIE Chip percent calculating start...");

		for (ChartShareCieData shareObj : shareTable) {

			Long chipCount = shareObj.getChipCount();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(totalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				shareObj.setChipPercent(chipPer.doubleValue());
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());

			logger.info("CIE[{}] count[{}] percent[{}]",
					new Object[] { shareObj.getGroupId(), shareObj.getChipCount(), shareObj.getChipPercent() });

		}

		logger.info("CIE Chip percent calculating end...");
	}

	/**
	 * Lot에 대한 Chip Count 점유율을 분석한다.
	 * 
	 * @param shareTable
	 * @param info
	 * @param totalMeasureDatas
	 * @return
	 */
	public static List<TableShareData> occRangeChipCount(List<ChartShareData> shareTable, FileLocation info,
			List<Double> totalMeasureDatas) {

		List<TableShareData> tableDatas = new ArrayList<>();

		for (ChartShareData shareObj : shareTable) {

			DozerBeanMapper mapper = new DozerBeanMapper();

			TableShareData tableData = mapper.map(info, TableShareData.class);

			// logger.info("LOT[{}]", tableData.getLotId());

			tableDatas.add(tableData);

			tableData.setGroupName(shareObj.getGroupId());

			tableData.setRange(shareObj.getRangeDescription());

			tableData.setChipCnt(0L);

			tableData.setChipPer(0D);

			// logger.info("Min[{}]", shareObj.getMin());
			// logger.info("Max[{}]", shareObj.getMax());

			Stream<Double> foundData = totalMeasureDatas.stream()
					.filter(o -> shareObj.getMin() < o && o <= shareObj.getMax());

			if (foundData != null) {

				Long cnt = foundData.count();

				logger.info("Find chip count min[{}] max[{}] count[{}]",
						new Object[] { shareObj.getMin(), shareObj.getMax(), cnt });

				Long chipCount = tableData.getChipCnt();

				logger.info("chipCount[{}]", chipCount);

				tableData.setChipCnt(cnt);

				tableData.setValue(cnt);

				Long chipCountOfAll = shareObj.getChipCount() + cnt;

				shareObj.setChipCount(chipCountOfAll);

			} else {

				logger.warn("Cannot found chip count min[{}] max[{}] count[{}] percent[{}]", new Object[] {
						shareObj.getMin(), shareObj.getMax(), tableData.getChipCnt(), tableData.getChipPer() });
			}
		}

		Long TotalChipCount = tableDatas.stream().mapToLong(i -> i.getChipCnt()).sum();

		logger.info("Lot Chip TotalCount [{}]", TotalChipCount);

		logger.info("Lot Chip percent calculating start...");

		for (TableShareData tableData : tableDatas) {

			Long chipCount = tableData.getChipCnt();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);

			tableData.setTChipCnt(TotalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				tableData.setChipPer(chipPer.doubleValue());
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());

			logger.info("LOTID[{}] [{}][{}] count[{}] percent[{}]", new Object[] { tableData.getLotId(),
					tableData.getGroupName(), tableData.getRange(), tableData.getChipCnt(), tableData.getChipPer() });

		}

		logger.info("Lot Chip percent calculating end...");

		return tableDatas;
	}

	public static void occRangeChipCountTotal(List<ChartShareData> shareTable) {

		Long TotalChipCount = shareTable.stream().mapToLong(i -> i.getChipCount()).sum();

		logger.info("Chip TotalCount [{}]", TotalChipCount);

		logger.info("Chip percent calculating start...");

		for (ChartShareData shareObj : shareTable) {

			Long chipCount = shareObj.getChipCount();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				shareObj.setChipPercent(chipPer.doubleValue());
			}

//			logger.info("{}\t{}", shareObj.getChipCount(), shareObj.getChipPercent());

			logger.info("[{}] min[{}] max[{}] count[{}] percent[{}]", new Object[] { shareObj.getGroupId(),
					shareObj.getMin(), shareObj.getMax(), shareObj.getChipCount(), shareObj.getChipPercent() });

		}

		logger.info("Chip percent calculating end...");
	}

	public static List<TableShareData> occCieChipCount(List<ChartShareCieData> shareTable, FileLocation info,
			List<Point> totalMeasureDatas) {

		@SuppressWarnings("unchecked")
		List<Point> measureData = (List<Point>) ((ArrayList<Point>) totalMeasureDatas).clone();

		List<TableShareData> tableDatas = new ArrayList<>();

		Long totalChipCount = Long.valueOf(measureData.size());

		DozerBeanMapper mapper = new DozerBeanMapper();

		for (ChartShareCieData shareObj : shareTable) {

			if (shareObj.getCieName().equals("outside")) {
				continue;
			}

			TableShareData tableData = mapper.map(info, TableShareData.class);
			// logger.info("LOT[{}]", tableData.getLotId());
			tableDatas.add(tableData);
			tableData.setGroupName(shareObj.getGroupId());
			tableData.setRange(shareObj.getCieName());
			tableData.setChipCnt(0L);
			tableData.setChipPer(0D);

			List<Point> containPoints = measureData.stream().filter(o -> shareObj.getCieRectangle().contains(o))
					.collect(Collectors.toList());

			if (containPoints != null) {
				Long chipCount = Long.valueOf(containPoints.size());
				tableData.setChipCnt(chipCount);
				tableData.setValue(chipCount);
				measureData.removeAll(containPoints);
				// 전체 데이터
				Long chipCountOfAll = shareObj.getChipCount() + chipCount;
				shareObj.setChipCount(chipCountOfAll);
			} else {

				logger.warn("Cannot found chip count cie[{}] count[{}] percent[{}]",
						new Object[] { shareObj.getGroupId(), tableData.getChipCnt(), tableData.getChipPer() });
			}
		}
		// outside
		Long outsideChipCount = Long.valueOf(measureData.size());
		logger.info("outside chip count[{}]", outsideChipCount);
		TableShareData outside = mapper.map(info, TableShareData.class);
		outside.setGroupName("outside");
		outside.setRange("outside");
		outside.setChipCnt(outsideChipCount);
		outside.setValue(outsideChipCount);
		outside.setChipPer(0D);
		tableDatas.add(outside);

		ChartShareCieData outsideTotal = shareTable.stream().filter(o -> o.getCieName().equals("outside")).findFirst()
				.get();
		Long totalOutsideChipCount = outsideTotal.getChipCount() + outsideChipCount;
		outsideTotal.setChipCount(totalOutsideChipCount);

		logger.info("Lot Chip TotalCount [{}]", totalChipCount);
		logger.info("Lot Chip percent calculating start...");

		for (TableShareData tableData : tableDatas) {

			Long chipCount = tableData.getChipCnt();
			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);
			BigDecimal bTotalCount = BigDecimal.valueOf(totalChipCount);
			tableData.setTChipCnt(totalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				tableData.setChipPer(chipPer.doubleValue());
			}

			logger.info("LOTID[{}] [{}][{}] count[{}] percent[{}]", new Object[] { tableData.getLotId(),
					tableData.getGroupName(), tableData.getRange(), tableData.getChipCnt(), tableData.getChipPer() });
		}

		logger.info("Lot Chip percent calculating end...");

		return tableDatas;
	}

	public static void occCieChipCountTotal(List<ChartShareCieData> shareTable) {

		Long TotalChipCount = shareTable.stream().mapToLong(i -> i.getChipCount()).sum();

		logger.info("Chip TotalCount [{}]", TotalChipCount);

		logger.info("Chip percent calculating start...");

		for (ChartShareCieData share : shareTable) {

			Long chipCount = share.getChipCount();

			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);

			BigDecimal bTotalCount = BigDecimal.valueOf(TotalChipCount);

			if (!bChipCount.equals(BigDecimal.ZERO)) {

				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN)
						.multiply(new BigDecimal(100));

				share.setChipPercent(chipPer.doubleValue());
			}

			logger.info("[{}] count[{}] percent[{}]",
					new Object[] { share.getCieName(), share.getChipCount(), share.getChipPercent() });

		}
	}
	
	public static List<TableShareData> occBothChipCount(List<ChartShareBothData> shareTable, FileLocation info,
			List<ShareDataObject> totalMeasureDatas) {
		
		List<TableShareData> tables = new ArrayList<>();
		List<ShareDataObject> measureData = (List<ShareDataObject>) ((ArrayList<ShareDataObject>) totalMeasureDatas).clone();
		Long totChupCount = Long.valueOf(measureData.size());
		DozerBeanMapper mapper = new DozerBeanMapper();
		for(ChartShareBothData bothObj : shareTable) {
			if (bothObj.getCieName().equals("outside")) {
				continue;
			}
			// Check Contains In Rectangle
			List<ShareDataObject> containPts = measureData.stream().filter(o -> bothObj.getCieRectangle().contains(o.getCieValue())).collect(Collectors.toList());	
			// Check Contains In Range
			if (containPts != null) {
				
				Long cieChipCount = bothObj.getChipCount() + Long.valueOf(containPts.size());
				bothObj.setChipCount(cieChipCount);
				
				// logger.info("CIE[{}] CHIPCOUNT[{}]", bothObj.getCieName(), bothObj.getChipCount());
				
				for(ChartShareData rangeObj : bothObj.getRange()) {

					TableShareData tableData = mapper.map(info, TableShareData.class);
					tableData.setGroupName(rangeObj.getGroupId());
					tableData.setSubGroupName(bothObj.getCieName());
					tableData.setRange(rangeObj.getRangeDescription());
					tableData.setChipCnt(0L);
					tableData.setChipPer(0D);
					tableData.setValue(0L);
					tables.add(tableData);
					
					List<ShareDataObject> containRange = containPts.stream().filter(o -> rangeObj.getMin() < o.getItemValue() && o.getItemValue() <= rangeObj.getMax()).collect(Collectors.toList());
					if (containRange != null) {
						Long chipCount = Long.valueOf(containRange.size());
						Long totChipCount = bothObj.getChipCount() + chipCount;
						Long rangeChipCount = rangeObj.getChipCount() + chipCount;
						tableData.setChipCnt(chipCount);
						tableData.setValue(chipCount);
						rangeObj.setChipCount(rangeChipCount);
						//bothObj.setChipCount(totChipCount);
						measureData.removeAll(containRange);
						
						// logger.info("CIE[{}] GROUP[{}] RANGE[{}] CHIP[{}]", new Object[] { bothObj.getCieName(), rangeObj.getGroupId(), rangeObj.getRangeDescription(), rangeObj.getChipCount() });
						
					}
				}
				measureData.removeAll(containPts);
			}
		}
		// outside
		ChartShareBothData outsideObj = shareTable.stream().filter(o -> o.getCieName().equals("outside")).findFirst().get();
		outsideObj.setChipCount(Long.valueOf(measureData.size()));
		for(ChartShareData rangeObj : outsideObj.getRange()) {

			TableShareData tableData = mapper.map(info, TableShareData.class);
			tableData.setGroupName(rangeObj.getGroupId());
			tableData.setSubGroupName(outsideObj.getCieName());
			tableData.setRange(rangeObj.getRangeDescription());
			tableData.setChipCnt(0L);
			tableData.setChipPer(0D);
			tableData.setValue(0L);
			tables.add(tableData);
			
			List<ShareDataObject> containRange = measureData.stream().filter(o -> rangeObj.getMin() < o.getItemValue() && o.getItemValue() <= rangeObj.getMax()).collect(Collectors.toList());
			if (containRange != null) {
				Long chipCount = Long.valueOf(containRange.size());
				Long totChipCount = outsideObj.getChipCount() + chipCount;
				Long rangeChipCount = rangeObj.getChipCount() + chipCount;
				tableData.setChipCnt(chipCount);
				tableData.setValue(chipCount);
				rangeObj.setChipCount(rangeChipCount);
				outsideObj.setChipCount(totChipCount);
			}
		}
		
		for (TableShareData tableData : tables) {
			Long chipCount = tableData.getChipCnt();
			BigDecimal bChipCount = BigDecimal.valueOf(chipCount);
			BigDecimal bTotalCount = BigDecimal.valueOf(totChupCount);
			tableData.setTChipCnt(totChupCount);
			if (!bChipCount.equals(BigDecimal.ZERO)) {
				BigDecimal chipPer = bChipCount.divide(bTotalCount, 5, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
				tableData.setChipPer(chipPer.doubleValue());
			}
			
			/*
			logger.info("LOTID[{}] CIE[{}] GROUP[{}] CHIP[{}] PERCENT[{}]", new Object[] {
					tableData.getLotId(), tableData.getSubGroupName(), tableData.getGroupName(), tableData.getChipCnt(), tableData.getChipPer()
			});
			*/
		}
		
		return tables;
	}
	
	public static void occBothChipCountTotal(List<ChartShareBothData> shareTable) {
		
		Long totChipCount = shareTable.stream().mapToLong(o -> o.getRange().stream().mapToLong(x -> x.getChipCount()).sum()).sum();
		
		logger.info("TOTAL CHIP COUNT[{}]", totChipCount);

		for (ChartShareBothData share : shareTable) {
			
			BigDecimal totChipDecimal = BigDecimal.valueOf(totChipCount);
			
			Long cieChipCount = share.getChipCount();
			BigDecimal cieChipDecimal = BigDecimal.valueOf(cieChipCount);
			if(!cieChipDecimal.equals(BigDecimal.ZERO)) {
				BigDecimal percent = cieChipDecimal.divide(totChipDecimal, 5, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
				share.setChipPercent(percent.doubleValue());
			}
			//logger.info("CIE[{}] CHIP[{}] PER[{}]", new Object[] { share.getCieName(), share.getChipCount(), share.getChipPercent() });
			
			for(ChartShareData range : share.getRange() ) {
				
				Long chipCount = range.getChipCount();
				BigDecimal chipDecimal = BigDecimal.valueOf(chipCount);
				
				if(!chipDecimal.equals(BigDecimal.ZERO)) {
					BigDecimal percent = chipDecimal.divide(totChipDecimal, 5, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
					range.setChipPercent(percent.doubleValue());
				}
				//logger.info("CIE[{}] GROUP[{}] RANGE[{}] CHIP[{}] PER[{}]", new Object[] { share.getCieName(), range.getGroupId(), range.getRangeDescription(), range.getChipCount(), range.getChipPercent() });
			}
		}
	}
}
