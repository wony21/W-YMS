package com.compact.yms.domain.qm.yield.seperator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compact.yms.domain.qm.yield.seperator.model.SeperatorCieDTO;
import com.compact.yms.domain.qm.yield.seperator.model.SeperatorIVDTO;

public class SeperatorYieldUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SeperatorYieldUtils.class);

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
		String[] itemNameValues = contentLines[0].split(",");
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

	public static List<SeperatorCieDTO> getItemPointValueList(File file, String bin, String cieX, String cieY, List<SeperatorIVDTO> binInfos) throws IOException {

		List<SeperatorCieDTO> itemValues = new ArrayList<SeperatorCieDTO>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		int itemNameIndex = 0;
		int itemStartIndex = -1;
		int binIndex = -1;
		int cieXIndex = -1;
		int cieYIndex = -1;
		for (int i = 0; i < contentLines.length; i++) {
			if (itemNameIndex >= 0 && contentLines[i].toLowerCase().trim().startsWith("1")) {
				itemStartIndex = i;
				break;
			}
		}

		// 각 행번호 유효성 검사
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
		}

		// Find CIE-X CIE-Y Position
		String[] itemNames = contentLines[itemNameIndex].split(",");
		for (int i = 0; i < itemNames.length; i++) {
			String itemTrimName = StringUtils.deleteWhitespace(itemNames[i]).toLowerCase();
			if (itemTrimName.equals(StringUtils.deleteWhitespace(cieX).toLowerCase())) {
				cieXIndex = i;
			} else if (itemTrimName.equals(StringUtils.deleteWhitespace(cieY).toLowerCase())) {
				cieYIndex = i;
			} else if (itemTrimName.equals(StringUtils.deleteWhitespace(bin).toLowerCase())) {
				binIndex = i;
			}
		}

		// 선행으로 검사하기 때문에 사실상 의미는 없다.
		if (cieXIndex == -1) {
			logger.error("Cannot found cie-x index. [{}]", file.getName());
		}
		if (cieYIndex == -1) {
			logger.error("Cannot found cie-y index. (no has '1') [{}]", file.getName());
		}
		if (binIndex == -1) {
			logger.error("Cannot found bin index. [{}]", file.getName());
		}

		logger.info("itemNameIndex[{}] itemStartIndex[{}].",
				new Object[] { itemNameIndex, itemStartIndex });

		if (itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				
				if (contentLines[i].isEmpty())
					break;

				String[] itemParams = contentLines[i].split(",");
				
				int itemParamLength = itemParams.length;
				
				if (itemParamLength < cieXIndex || itemParamLength < cieYIndex || itemParamLength < binIndex) {
					logger.warn("Invalid item param length. ItemParamLen[{}] Cie-X Index[{}] Cie-Y Index[{}] Bin Index[{}]", 
									new Object[] { itemParamLength, cieXIndex, cieYIndex, binIndex });
					continue;
				}
				
				String cieXValue = itemParams[cieXIndex].trim();
				String cieYValue = itemParams[cieYIndex].trim();
				String binValue = itemParams[binIndex].trim();
				
				logger.info("ROW[{}] BIN[{}] CIE-X[{}] CIE-Y[{}]", new Object[] { i, binValue, cieXValue, cieYValue });
				
				String ivValue = "-";
				
				Optional<SeperatorIVDTO> ivObj = binInfos.stream().filter(o -> o.getBinNo().equals(binValue)).findFirst();
				if (ivObj.isPresent()) {
					ivValue = ivObj.get().getIv();
				}
				
				if (!cieXValue.isEmpty() && !cieYValue.isEmpty()) {
					Double x = NumberUtils.toDouble(cieXValue);
					Double y = NumberUtils.toDouble(cieYValue);
					if (x != 0d && y != 0d) {
						SeperatorCieDTO obj = new SeperatorCieDTO();
						obj.setBinNo(binValue);
						obj.setCieX(x);
						obj.setCieY(y);
						if (ivObj.isPresent()) {
							obj.setIntensity(ivObj.get().getIntensity());
							obj.setCie(ivObj.get().getCie());
						} else {
							obj.setIntensity("-");
							obj.setCie("-");
						}
						obj.setIvName(ivValue);
						itemValues.add(obj);
					}
				}
			}
		}
		logger.info("Item value count [{}] added.", new Object[] { itemValues.size() });
		return itemValues;
	}
	
	public static List<SeperatorCieDTO> getItemPointSorterValueList(File file, String combineNo, String bin, String cieX, String cieY, List<SeperatorIVDTO> seperatorIV) throws IOException {

		List<SeperatorCieDTO> itemValues = new ArrayList<SeperatorCieDTO>();
		String fileContents = FileUtils.readFileToString(file);
		String[] contentLines = fileContents.split("\r\n");
		// 측정구분문자열의 행, ITEMID행, 측정값 시작 행번호를 추출
		int itemNameIndex = 0;
		int itemStartIndex = -1;
		int binIndex = -1;
		int cieXIndex = -1;
		int cieYIndex = -1;
		for (int i = 0; i < contentLines.length; i++) {
			if (StringUtils.deleteWhitespace(contentLines[i]).toLowerCase().contains(StringUtils.deleteWhitespace(bin.toLowerCase()))
			&& StringUtils.deleteWhitespace(contentLines[i]).toLowerCase().contains(StringUtils.deleteWhitespace(cieX.toLowerCase()))
			&& StringUtils.deleteWhitespace(contentLines[i]).toLowerCase().contains(StringUtils.deleteWhitespace(cieY.toLowerCase()))) {
					itemNameIndex = i;
					itemStartIndex = (i + 1);
					break;
			}
		}
		
		logger.info("Sorter ItemNameIndex[{}] ItemStartIndex[{}]", itemNameIndex, itemStartIndex);
		
		// 각 행번호 유효성 검사
		if (itemStartIndex == -1) {
			logger.error("Cannot found measure item start index. (no has '1') [{}]", file.getName());
		}

		// Find CIE-X CIE-Y Position
		String[] itemNames = contentLines[itemNameIndex].split(",");
		for (int i = 0; i < itemNames.length; i++) {
			if (StringUtils.deleteWhitespace(itemNames[i]).toLowerCase().equals(StringUtils.deleteWhitespace(cieX).toLowerCase())) {
				cieXIndex = i;
			} else if (StringUtils.deleteWhitespace(itemNames[i]).toLowerCase().equals(StringUtils.deleteWhitespace(cieY).toLowerCase())) {
				cieYIndex = i;
			} else if ((StringUtils.deleteWhitespace(itemNames[i]).toLowerCase().equals(StringUtils.deleteWhitespace(bin).toLowerCase()))) {
				binIndex = i;
			}
		}

		// 선행으로 검사하기 때문에 사실상 의미는 없다.
		if (cieXIndex == -1) {
			logger.error("Cannot found cie-x index. [{}]", file.getName());
		}
		if (cieYIndex == -1) {
			logger.error("Cannot found cie-y index. (no has '1') [{}]", file.getName());
		}
		if (binIndex == -1) {
			logger.error("Cannot found bin index. [{}]", file.getName());
		}

		logger.info("itemNameIndex[{}] itemStartIndex[{}].",
				new Object[] { itemNameIndex, itemStartIndex });

		if (itemNameIndex >= 0 && itemStartIndex >= 0) {
			for (int i = itemStartIndex; i < contentLines.length; i++) {
				
				if (contentLines[i].isEmpty())
					break;

				String[] itemParams = contentLines[i].split(",");
				
				int itemParamLength = itemParams.length;
				
				if (itemParamLength < cieXIndex || itemParamLength < cieYIndex || itemParamLength < binIndex) {
					logger.warn("Invalid item param length. ItemParamLen[{}] Cie-X Index[{}] Cie-Y Index[{}] Bin Index[{}]", 
									new Object[] { itemParamLength, cieXIndex, cieYIndex, binIndex });
					continue;
				}
				
				String cieXValue = itemParams[cieXIndex].trim();
				String cieYValue = itemParams[cieYIndex].trim();
				String binValue = itemParams[binIndex].trim();
				
				logger.info("ROW[{}] COMBINENO[{}] BIN[{}] CIE-X[{}] CIE-Y[{}]", new Object[] { i, combineNo, binValue, cieXValue, cieYValue });
				
				if (!cieXValue.isEmpty() && !cieYValue.isEmpty()) {
					
					String ivValue = "-";
					
					SeperatorIVDTO ivObj = seperatorIV.stream().filter(o -> o.getBinNo().trim().equals(binValue)).findFirst().orElse(null);
					if (ivObj != null) {
						ivValue = ivObj.getIv();
					}
					Double x = NumberUtils.toDouble(cieXValue);
					Double y = NumberUtils.toDouble(cieYValue);
					if (x != 0d && y != 0d) {
						SeperatorCieDTO obj = new SeperatorCieDTO();
						obj.setBinNo(binValue);
						obj.setCieX(x);
						obj.setCieY(y);
						obj.setIntensity(ivObj.getIntensity());
						obj.setCie(ivObj.getCie());
						obj.setIvName(ivValue);
						obj.setCombineNo(combineNo);
						itemValues.add(obj);
					}
				}
			}
		}
		logger.info("Item value count [{}] added.", new Object[] { itemValues.size() });
		return itemValues;
	}
	
	public static StringBuilder mergeSorterFile(File[] file, String bin, String cieX, String cieY) throws IOException {
		
		logger.info("BIN[{}] CIE-X[{}] CIE-Y[{}]", new Object[] { bin, cieX, cieY});
		
		StringBuilder builder = new StringBuilder();
		
		int fileLength = file.length; 
		if (fileLength == 0) {
			logger.warn("Soter file count is Zero.");
			return builder;
		}
		int ITEMNAME_ROWIDX = 0;
		int ITEM_ROWIDX = -1;
		String[] itemNameParams = null;
		for(int i=0; i < fileLength; i++) {
			
			logger.info("FileIndex[{}] File[{}]", new Object[] { i, file[i].getName() });
			
			String fileContents = FileUtils.readFileToString(file[i]);
			String[] contentLines = fileContents.split("\r\n");
			
			for(int j=0; j<contentLines.length; j++) {
				if (StringUtils.deleteWhitespace(contentLines[j]).toLowerCase().contains(StringUtils.deleteWhitespace(bin.toLowerCase()))
				&& StringUtils.deleteWhitespace(contentLines[j]).toLowerCase().contains(StringUtils.deleteWhitespace(cieX.toLowerCase()))
				&& StringUtils.deleteWhitespace(contentLines[j]).toLowerCase().contains(StringUtils.deleteWhitespace(cieY.toLowerCase())))  {
					ITEMNAME_ROWIDX = j;
					ITEM_ROWIDX = (j + 1);
					logger.info("[{}] FILE ITEM LINE : {}", new Object[] { j, contentLines[j] });
					break;
				}
			}
			
			if (i == 0) {
				logger.info("ITEMNAMEROW[{}] ITEMSTARTROW[{}]", ITEMNAME_ROWIDX, ITEM_ROWIDX);
				builder.append(contentLines[ITEMNAME_ROWIDX] + "\r\n");
				itemNameParams = contentLines[ITEMNAME_ROWIDX].split(",");
				for(int j=ITEM_ROWIDX; j<contentLines.length; j++) {
					if(contentLines[j].isEmpty()) {
						break;
					}
					builder.append(contentLines[j] + "\r\n");
				}
			} else {
				
				if (itemNameParams == null) {
					logger.error("Cannot found ItemName Line [{}]", file[i].getName());
					continue;
				}
				
				String[] currentItemname = contentLines[ITEMNAME_ROWIDX].split(",");
				
				for(int j=ITEM_ROWIDX; j<contentLines.length; j++) {
					if (contentLines[j].isEmpty()) {
						continue;
					}
					
					String[] itemValues = contentLines[j].split(",");
					String itemLineContent = "";
					
					for(int k=0; k < itemNameParams.length; k++) {
						
						String baseItemName = itemNameParams[k];
						if (k < currentItemname.length) {
							
							String targetItemName = currentItemname[k];
							if ( k < itemValues.length ) {
								if(baseItemName.equals(targetItemName)) {
									itemLineContent += String.format("%s,", itemValues[k]);
								} else {
									logger.error("itemname is not equers [{}][{}]", new Object[] { baseItemName, targetItemName });
									itemLineContent += ",";
								}
							} else {
								logger.error("[{}] itemValue length less then k. len[{}] k[{}]", new Object[] { targetItemName, itemValues.length, k  });
								itemLineContent += ",";
							}
						}
					}
					if(itemLineContent.length() > 0 && itemLineContent.endsWith(",")) {
						itemLineContent = itemLineContent.substring(0, itemLineContent.length() - 1);
					}
					builder.append(itemLineContent + "\r\n");
				}
			}
		}
		
		return builder;
	}
	
}
