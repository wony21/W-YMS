package com.compact.yms.domain.qm.yield.seperator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.common.BaseService;
import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.domain.analysis.AnalysisService;
import com.compact.yms.domain.analysis.DTO.CieNameInfo;
import com.compact.yms.domain.file.EqpFileService;
import com.compact.yms.domain.file.dto.FileResponse;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldChartDTO;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldCieDTO;
import com.compact.yms.domain.qm.yield.seperator.model.SeperatorCieDTO;
import com.compact.yms.domain.qm.yield.seperator.model.SeperatorIVDTO;
import com.compact.yms.utils.FTPUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeperatorYieldService extends BaseService {

	@Autowired
	SeperatorYieldMapper mapper;
	
	@Autowired
	AnalysisService anaService;
	
	@Autowired
	EqpFileService eqpService;
	
	@Autowired
	FTPUtils ftp;
	
	public List<String> getDateRange(String factoryName, String startDate, String endDate, String opt2) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("factoryName", factoryName);
		parameter.put("opt2", opt2);
		List<CamelCaseMap> list = mapper.getDateRange(parameter);
		List<String> returnValues = new ArrayList<String>();
		for(CamelCaseMap map : list) {
			String date = MapUtils.getString(map, "datadate");
			returnValues.add(date);
		}
		return returnValues;
	}
	
	public List<String> getPreviousDateRange(String factoryName, String startDate, String endDate, String opt3) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("factoryName", factoryName);
		parameter.put("opt3", opt3);
		List<CamelCaseMap> list = mapper.getPrevousDateRange(parameter);
		List<String> returnValues = new ArrayList<String>();
		for(CamelCaseMap map : list) {
			String date = MapUtils.getString(map, "datadate");
			returnValues.add(date);
		}
		return returnValues;
	}

	public SYieldChartDTO getSeperatorYield(
			String factoryName, String startDate, String endDate, String productionType, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl, String opt1, String opt2, String opt3
	) {

		SYieldChartDTO sepeYieldData = new SYieldChartDTO();
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		// 조회 데이터 기간
		List<String> dates = getDateRange(factoryName, startDate, endDate, opt2);
		parameter.put("dates", dates);
		if (dates.size() > 0) {
			int lastIndex = dates.size() - 1;
			if (opt2.equals("DAY")) {
				parameter.put("startDate", dates.get(0));
				parameter.put("endDate", dates.get(lastIndex));
			} else if (opt2.equals("WEEK")) {
				parameter.put("startWeek", dates.get(0));
				parameter.put("endWeek", dates.get(lastIndex));
			} else if (opt2.equals("MONTH")) {
				parameter.put("startMonth", dates.get(0));
				parameter.put("endMonth", dates.get(lastIndex));
			}
		}
		// 최근 데이터 기간
		List<String> previousDates = getPreviousDateRange(factoryName, startDate, endDate, opt3);
		parameter.put("previousDates", previousDates);
		if (previousDates.size() > 0) {
			parameter.put("preStartDate", previousDates.get(0));
			int lastIndex = previousDates.size() - 1;
			parameter.put("preEndDate", previousDates.get(lastIndex));
		}
		// opt1 : 단위그룹
		parameter.put("opt1", opt1);
		// opt2 : 데이터일구분(DAY,WEEK,MONTH)
		parameter.put("opt2", opt2);
		// opt3 : 최근데이터구분(PREMONTH, PREWEEK)
		parameter.put("opt3", opt3);
		
		sepeYieldData.setLabel(mapper.getChartLabel(parameter));
		sepeYieldData.setYield(mapper.getSeperatorYield(parameter));
		sepeYieldData.setTable(mapper.getSYieldTable(parameter));
		
		return sepeYieldData;
	}
	
	
	/**
	 * 분류기 수율 - 하단 선택시 선택 값 차트 값 반환
	 * @param factoryName
	 * @param startDate
	 * @param endDate
	 * @param productionType
	 * @param div
	 * @param productSpecGroup
	 * @param productSpecName
	 * @param vendor
	 * @param model
	 * @param machineName
	 * @param program
	 * @param target
	 * @param chipSpec
	 * @param frameName
	 * @param pl
	 * @param opt1
	 * @param opt2
	 * @param opt3
	 * @param opt4
	 * @param opt5
	 * @return
	 */
	public SYieldChartDTO getSeperatorSelectChart(
			String factoryName, String startDate, String endDate, String productionType, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl, String opt1, String opt2, String opt3, String opt4, String opt5
	) {

		SYieldChartDTO sepeYieldData = new SYieldChartDTO();
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		// 조회 데이터 기간
		List<String> dates = getDateRange(factoryName, startDate, endDate, opt2);
		parameter.put("dates", dates);
		if (dates.size() > 0) {
			int lastIndex = dates.size() - 1;
			if (opt2.equals("DAY")) {
				parameter.put("startDate", dates.get(0));
				parameter.put("endDate", dates.get(lastIndex));
			} else if (opt2.equals("WEEK")) {
				parameter.put("startWeek", dates.get(0));
				parameter.put("endWeek", dates.get(lastIndex));
			} else if (opt2.equals("MONTH")) {
				parameter.put("startMonth", dates.get(0));
				parameter.put("endMonth", dates.get(lastIndex));
			}
		}
		// 최근 데이터 기간
		List<String> previousDates = getPreviousDateRange(factoryName, startDate, endDate, opt3);
		parameter.put("previousDates", previousDates);
		if (previousDates.size() > 0) {
			parameter.put("preStartDate", previousDates.get(0));
			int lastIndex = previousDates.size() - 1;
			parameter.put("preEndDate", previousDates.get(lastIndex));
		}
		// opt1 : 단위그룹
		parameter.put("opt1", opt1);
		// opt2 : 데이터일구분(DAY,WEEK,MONTH)
		parameter.put("opt2", opt2);
		// opt3 : 최근데이터구분(PREMONTH, PREWEEK)
		parameter.put("opt3", opt3);
		// opt4 : 선택조건의 컬럼명
		// opt5 : 선택조건 필터값
		if (!opt5.toLowerCase().equals("total")) {
			parameter.put("opt4", opt4);
			parameter.put("opt5", opt5);
		}
		
		sepeYieldData.setLabel(mapper.getChartLabel(parameter));
		sepeYieldData.setYield(mapper.getSeperatorYield(parameter));
		
		return sepeYieldData;
	}
	
	public List getProgramBinItems(String factoryName, String productSpecName, String program, String opt1) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		parameter.put("opt1", opt1);
		return mapper.getProgramBinItems(parameter);
	}
	
	public List getSYieldDetailPeriod(String factoryName, String startDate, String endDate, String productionType, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		return mapper.getSYieldDetailPeriod(parameter);
	}
	
	public List getSYieldDetailPeriodOfBin(String factoryName, String startDate, String endDate, String productionType, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl, String binType, String binItems) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		parameter.put("binType", binType);
		setArrayParam(parameter, "binItems", binItems);
		return mapper.getSYieldDetailPeriodOfBin(parameter);
	}
	
	public List getProgramCie(String factoryName, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getProgramCie(parameter);
	}
	
	public List getSeperatorIV(String factoryName, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getSeperatorIV(parameter);
	}
	
	public List getSeperatorFiles(String factoryName, String lotId, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		setArrayParam(parameter, "lotIds", lotId);
		//parameter.put("lotId", lotId);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getSeperatorFiles(parameter);
	}
	
	public List getSorterFiles(String factoryName, String lotId, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		setArrayParam(parameter, "lotIds", lotId);
		//parameter.put("lotId", lotId);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getSorterData(parameter);
	}
	
	public List getSorterInfo(String factoryName, String lotId, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		setArrayParam(parameter, "lotIds", lotId);
		//parameter.put("lotId", lotId);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getSorterData(parameter);
	}
	
	public CamelCaseMap getCieItemName(String fmKey) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("fmKey", fmKey);
		List<CamelCaseMap> list = mapper.getCieItemName(parameter);
		if ( list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public SYieldCieDTO getSeperatorCieData(String factoryName, String lotId, String productSpecName, String program) throws IOException {
		
		FileResponse res = getCIERawdata(factoryName, lotId, productSpecName, program);
		File mergeFile = res.getFile();
		String rawdata = FileUtils.readFileToString(mergeFile);
		//---------------------------------------------------------
		// 분류 CIE 정보
		//---------------------------------------------------------
		SYieldCieDTO result = getCIEData(mergeFile, factoryName, lotId, productSpecName, program);
		result.setRawData(rawdata);
		//---------------------------------------------------------
		// ZFILM (SORTER 정보)
		//---------------------------------------------------------		
		SYieldCieDTO sorter = getSorterData(factoryName, lotId, productSpecName, program);
		if(!sorter.getError()) {
			result.setSubCieType(sorter.getSubCieType());
			result.setSubRawData(sorter.getSubRawData());
			result.setSubCieData(sorter.getSubCieData());
			result.setSubWidght(sorter.getSubWidght());
		}
		return result;
	}
	
	public FileResponse getCIERawdata(String factoryName, String lotId, String productSpecName, String program) {
		List<CamelCaseMap> fileInfos = getSeperatorFiles(factoryName, lotId, productSpecName, program);
		String[] fmKeys = fileInfos.stream().map(o -> MapUtils.getString(o, "fmkey")).toArray(String[]::new);
		if ( fmKeys.length == 0 ) {
			FileResponse res = new FileResponse();
			res.setIsError(true);
			res.setErrorMsg("FMKEY를 찾을 수 없습니다.");
			return res;
		}
		return eqpService.mergeFile(fmKeys);
	}
	
	public FileResponse getSorterRawFiles(String factoryName, String lotId, String productSpecName, String program) {
		List<CamelCaseMap> fileInfos = getSorterFiles(factoryName, lotId, productSpecName, program);
		String[] fmKeys = fileInfos.stream().map(o -> MapUtils.getString(o, "fmkey")).toArray(String[]::new);
		if ( fmKeys.length == 0 ) {
			FileResponse res = new FileResponse();
			res.setIsError(true);
			res.setErrorMsg("FMKEY를 찾을 수 없습니다.");
			return res;
		}
		return eqpService.mergeFile(fmKeys);
	}
	
	public SYieldCieDTO getCIEData(File mergeFile, String factoryName, String lotId, String productSpecName, String program) throws IOException {
		
		SYieldCieDTO sYieldCie = new SYieldCieDTO();
		String errMessage = "";
		sYieldCie.setError(false);
		//-------------------------------------------------
		// 분류기준서 좌표
		//-------------------------------------------------
		List<CamelCaseMap> programCie = getProgramCie(factoryName, productSpecName, program);
		
		if (programCie.size() == 0) {
			sYieldCie.setError(true);
			errMessage = String.format("분류기준서[POP_ERP_SEPERATOR_CIE]를 찾울 수 없습니다. (%s)(%s)", productSpecName, program);
			sYieldCie.setErrorMessage(errMessage);
			return sYieldCie;
		}
		sYieldCie.setProgramCie(programCie);
		//-------------------------------------------------
		// 분류기준서 INTENSITY, FLUX + CIE 값 (BIN번호별)
		//-------------------------------------------------
		List<SeperatorIVDTO> seperatorIV = getSeperatorIV(factoryName, productSpecName, program);
		if (seperatorIV.size() == 0) {
			sYieldCie.setError(true);
			errMessage = String.format("분류기준서[POP_ERP_SEPERATOR]를 찾울 수 없습니다. (%s)(%s)", productSpecName, program);
			sYieldCie.setErrorMessage(errMessage);
			return sYieldCie;
		}
		//-------------------------------------------------
		// CIE 좌표 데이터
		//-------------------------------------------------
		List<String> itemNamesInFile = SeperatorYieldUtils.getItemNames(mergeFile);
		CieNameInfo binNameInfo = anaService.getBinItemName(itemNamesInFile);
		if (binNameInfo == null) {
			log.error("Cannot found Bin name. Defition=> POP_ENUMDEFVALUE(ENUMNAME='CIE-BIN-NAME')");
			log.error("Itemname:[{}]", String.join(",", itemNamesInFile));
			sYieldCie.setError(true);
			sYieldCie.setErrorMessage(String.format("BIN에 해당되는 측정아이템명을 못찾았습니다."));
			return sYieldCie;
		}
		CieNameInfo nameInfo = anaService.getCieXYItemName(itemNamesInFile);
		if (nameInfo == null) {
			log.error("Cannot found CIE-X, CIE-Y name. Defition=> POP_ENUMDEFVALUE(ENUMNAME='CIE-XY-NAME')");
			log.error("Itemname:[{}]", String.join(",", itemNamesInFile));
			sYieldCie.setError(true);
			sYieldCie.setErrorMessage(String.format("CIEX, CIEY에 해당되는 측정아이템명을 못찾았습니다."));
			return sYieldCie;
		}
		String binName = binNameInfo.getBinName();
		String cieX = nameInfo.getCieX();
		String cieY = nameInfo.getCieY();
		log.info("Get measuredata for cie-x, cie-y");
		List<SeperatorCieDTO> measureItems = SeperatorYieldUtils.getItemPointValueList(mergeFile, binName, cieX, cieY, seperatorIV);
		sYieldCie.setCieData(measureItems);
		//-------------------------------------------------
		// 점유율 분석
		//-------------------------------------------------
		Integer totalQty = measureItems.size();
		List<SeperatorIVDTO> occCie = new ArrayList<>();
		log.info("점유율 분석 MAP 생성");
		SeperatorIVDTO outside = new SeperatorIVDTO();
		outside.setIv("-");
		outside.setIntensity("");
		outside.setCie("");
		outside.setQty(0);
		outside.setPer(0d);
		
		for(SeperatorIVDTO ivObj : seperatorIV) {
			String ivName = ivObj.getIv();
			String intensity = ivObj.getIntensity();
			String cie = ivObj.getCie();
			if ( intensity == null || cie == null ) {
				continue;
			}
			//log.info("점유율 분석 MAP 서치 IV[{}] CIE[{}]", new Object[] {intensity, cie});
			long dataCount = occCie.stream().filter(o -> o.getCie().equals(cie) && o.getIntensity().equals(intensity)).count();
			if(dataCount == 0) {
				SeperatorIVDTO target = new SeperatorIVDTO();
				target.setIv(String.format("%s %s", intensity, cie));
				target.setIntensity(intensity);
				target.setCie(cie);
				target.setQty(0);
				target.setPer(0d);
				occCie.add(target);
			}
		}
		occCie.add(outside);
		//log.info("점유율 분석 MAP 데이터 생성");
		for(SeperatorIVDTO occObj : occCie) {
			String ivName = occObj.getIv();
			String intensity = occObj.getIntensity();
			String cie = occObj.getCie();
			Integer qty = occObj.getQty();
			if ( intensity == null || cie == null ) {
				continue;
			}
			
			Stream<SeperatorCieDTO> occStream = measureItems.stream()
													.filter(o -> o.getIntensity() != null && o.getCie() != null)
													.filter(o -> o.getIntensity().equals(intensity) && o.getCie().equals(cie));
			if ( occStream != null ) {
				long dataCount = occStream.count();
				qty = (int) qty + (int)dataCount;
				occObj.setQty(qty);
				if (qty > 0) {
					Double percent = (double)  Math.round(((double)qty / (double)totalQty * 10000.0d))/ 100.0d;
					occObj.setPer(percent);
					//log.info("percent calculation IVNAME[{}] QTY[{}] TOTAL[{}] PERCENT[{}]", new Object[] { ivName, qty, totalQty, percent });
				}
			}
		}
		// outside
		int addedTotalQty = occCie.stream().filter(x -> !x.getIv().equals("-")).mapToInt(x -> x.getQty()).sum();
		int outsideQty = totalQty - addedTotalQty;
		outside.setQty(outsideQty);
		if ( outsideQty > 0) {
			Double percent = (double)  Math.round(((double)outsideQty / (double)totalQty * 10000.0d))/ 100.0d;
			outside.setPer(percent);
		}
		sYieldCie.setWidght(occCie);
		
		return sYieldCie;
	}
	
	public SYieldCieDTO getSorterData(String factoryName, String lotId, String productSpecName, String program) throws IOException {
		
		SYieldCieDTO result = new SYieldCieDTO();
		
		result.setError(false);
		
		List<CamelCaseMap> list = getSorterInfo(factoryName, lotId, productSpecName, program);
		List<File> sorterFiles = new ArrayList<>();
		String firstSorterBinItemName = "";
		String firstSorterCieXItemName = "";
		String firstSorterCieYItemName = "";
		if ( list.size() > 0 ) {
			//-------------------------------------------------
			// 분류기준서 INTENSITY, FLUX + CIE 값 (BIN번호별)
			//-------------------------------------------------
			List<SeperatorIVDTO> seperatorIV = getSeperatorIV(factoryName, productSpecName, program);
			
			List<SeperatorCieDTO> allData = new ArrayList<>();
			FTPClient ftpClient = ftp.connect();
			for(CamelCaseMap map : list) {
				String fmKey = MapUtils.getString(map, "fmkey");
				String combineNo = MapUtils.getString(map, "combineNo");
				String filePath = MapUtils.getString(map, "remotefilename");
				File file = ftp.downloadFile(ftpClient, filePath);
				sorterFiles.add(file);
				CamelCaseMap itemName = getCieItemName(fmKey);
				String binName = MapUtils.getString(itemName, "bin");
				String cieX = MapUtils.getString(itemName, "x");
				String cieY = MapUtils.getString(itemName, "y");
				if (firstSorterBinItemName.isEmpty())
					firstSorterBinItemName = binName;
				if (firstSorterCieXItemName.isEmpty())
					firstSorterCieXItemName = cieX;
				if (firstSorterCieYItemName.isEmpty())
					firstSorterCieYItemName = cieY;
				List<SeperatorCieDTO> measureItems = SeperatorYieldUtils.getItemPointSorterValueList(file, combineNo, binName, cieX, cieY, seperatorIV);
				allData.addAll(measureItems);
			}
			ftp.disconnect(ftpClient);
			if (allData.size() > 0) {
				result.setSubCieType("SORTER");
			}
			
			File[] files = sorterFiles.stream().map(x -> x).toArray(File[]::new);
			StringBuilder sorterContents 
						= SeperatorYieldUtils.mergeSorterFile(files, firstSorterBinItemName, firstSorterCieXItemName, firstSorterCieYItemName);
			result.setSubRawData(sorterContents.toString());
			result.setSubCieData(allData);
			
			//-------------------------------------------------
			// 점유율 분석
			//-------------------------------------------------
			Integer totalQty = allData.size();
			List<SeperatorIVDTO> occCie = new ArrayList<>();
			log.info("점유율 분석 MAP 생성");
			SeperatorIVDTO outside = new SeperatorIVDTO();
			outside.setIv("-");
			outside.setIntensity("");
			outside.setCie("");
			outside.setQty(0);
			outside.setPer(0d);
			
			for(SeperatorIVDTO ivObj : seperatorIV) {
				String ivName = ivObj.getIv();
				String intensity = ivObj.getIntensity();
				String cie = ivObj.getCie();
				if ( intensity == null || cie == null ) {
					continue;
				}
				//log.info("점유율 분석 MAP 서치 IV[{}] CIE[{}]", new Object[] {intensity, cie});
				long dataCount = occCie.stream().filter(o -> o.getCie().equals(cie) && o.getIntensity().equals(intensity)).count();
				if(dataCount == 0) {
					SeperatorIVDTO target = new SeperatorIVDTO();
					target.setIv(String.format("%s %s", intensity, cie));
					target.setIntensity(intensity);
					target.setCie(cie);
					target.setQty(0);
					target.setPer(0d);
					occCie.add(target);
				}
			}
			occCie.add(outside);
			//log.info("점유율 분석 MAP 데이터 생성");
			for(SeperatorIVDTO occObj : occCie) {
				String ivName = occObj.getIv();
				String intensity = occObj.getIntensity();
				String cie = occObj.getCie();
				Integer qty = occObj.getQty();
				if ( intensity == null || cie == null ) {
					continue;
				}
				
				Stream<SeperatorCieDTO> occStream = allData.stream()
														.filter(o -> o.getIntensity() != null && o.getCie() != null)
														.filter(o -> o.getIntensity().equals(intensity) && o.getCie().equals(cie));
				if ( occStream != null ) {
					long dataCount = occStream.count();
					qty = (int) qty + (int)dataCount;
					occObj.setQty(qty);
					if (qty > 0) {
						Double percent = (double)  Math.round(((double)qty / (double)totalQty * 10000.0d))/ 100.0d;
						occObj.setPer(percent);
						//log.info("percent calculation IVNAME[{}] QTY[{}] TOTAL[{}] PERCENT[{}]", new Object[] { ivName, qty, totalQty, percent });
					}
				}
			}
			// outside
			int addedTotalQty = occCie.stream().filter(x -> !x.getIv().equals("-")).mapToInt(x -> x.getQty()).sum();
			int outsideQty = totalQty - addedTotalQty;
			outside.setQty(outsideQty);
			if ( outsideQty > 0) {
				Double percent = (double)  Math.round(((double)outsideQty / (double)totalQty * 10000.0d))/ 100.0d;
				outside.setPer(percent);
			}
			result.setSubWidght(occCie);
		}
		
		return result;
	}
	
	/**
	 * 랭크별 수율 조회
	 * @param factoryName
	 * @param startDate
	 * @param endDate
	 * @param productionType
	 * @param div
	 * @param productSpecGroup
	 * @param productSpecName
	 * @param vendor
	 * @param model
	 * @param machineName
	 * @param program
	 * @param target
	 * @param chipSpec
	 * @param frameName
	 * @param pl
	 * @param opt1
	 * @param opt2
	 * @param opt3
	 * @return
	 */
	public SYieldChartDTO getSeperatorYieldOfRank(
			String factoryName, String startDate, String endDate, String productionType, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl, String opt1, String opt2, String opt3, 
			String opt4, String opt5, 
			String binType, String binItems
	) {

		SYieldChartDTO sepeYieldData = new SYieldChartDTO();
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		// 조회 데이터 기간
		List<String> dates = getDateRange(factoryName, startDate, endDate, opt2);
		parameter.put("dates", dates);
		if (dates.size() > 0) {
			int lastIndex = dates.size() - 1;
			if (opt2.equals("DAY")) {
				parameter.put("startDate", dates.get(0));
				parameter.put("endDate", dates.get(lastIndex));
			} else if (opt2.equals("WEEK")) {
				parameter.put("startWeek", dates.get(0));
				parameter.put("endWeek", dates.get(lastIndex));
			} else if (opt2.equals("MONTH")) {
				parameter.put("startMonth", dates.get(0));
				parameter.put("endMonth", dates.get(lastIndex));
			}
		}
		// 최근 데이터 기간
		List<String> previousDates = getPreviousDateRange(factoryName, startDate, endDate, opt3);
		parameter.put("previousDates", previousDates);
		if (previousDates.size() > 0) {
			parameter.put("preStartDate", previousDates.get(0));
			int lastIndex = previousDates.size() - 1;
			parameter.put("preEndDate", previousDates.get(lastIndex));
		}
		// opt1 : 단위그룹
		parameter.put("opt1", opt1);
		// opt2 : 데이터일구분(DAY,WEEK,MONTH)
		parameter.put("opt2", opt2);
		// opt3 : 최근데이터구분(PREMONTH, PREWEEK)
		parameter.put("opt3", opt3);
		
		parameter.put("opt4", "");
		parameter.put("opt5", "");
		if (opt5 != null) {
			if (!opt5.toLowerCase().equals("total")) {
				parameter.put("opt4", opt4);
				parameter.put("opt5", opt5);
			}
		}
		
		parameter.put("binType", binType);
		setArrayParam(parameter, "binItems", binItems);
		
		sepeYieldData.setLabel(mapper.getChartLabel(parameter));
		sepeYieldData.setYield(mapper.getSeperatorRankYield(parameter));
		sepeYieldData.setTable(mapper.getSYieldTableOfBin(parameter));
		
		return sepeYieldData;
	}

}
