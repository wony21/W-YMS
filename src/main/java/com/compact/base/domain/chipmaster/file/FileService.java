package com.compact.base.domain.chipmaster.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.JCommander;
import com.compact.base.common.CamelCaseMap;
import com.compact.base.common.api.ApiResponse;
import com.compact.base.utils.ExcelUtils;
import com.microsoft.sqlserver.jdbc.StringUtils;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.sun.tools.javac.main.CommandLine;

@Service
public class FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	FileMapper mapper;

	@Value("${file.path}")
	String uploadPath;

	@Value("${drmdec}")
	String drmdecExePath;

	public ApiResponse uploadFile(String masterNo, MultipartFile file) {

		try {

			logger.info("Upload root path : {}", uploadPath);

			// 날짜별 디렉토리 생성
			Date today = new Date();
			String yyyy = DateFormatUtils.format(today, "yyyy");
			String mm = DateFormatUtils.format(today, "MM");
			String dd = DateFormatUtils.format(today, "dd");
			String datePath = uploadPath + "\\" + yyyy + "\\" + mm + "\\" + dd;
			File directory = new File(datePath);
			logger.info("Daily upload path : {}", datePath);

			if (!directory.exists()) {
				logger.info("Create daily upload directory : {}", directory.getAbsolutePath());
				if (directory.mkdirs()) {
					logger.info("Create daily upload directory [OK]");
				}
			}

			// 업로드 파일명 생성
			String fileNm = new String(file.getOriginalFilename().getBytes("8859_1"), "UTF-8");
			if (fileNm.endsWith(".dec")) {
				fileNm = fileNm.replace(".dec", "");
			}

			File uploadFile = new File(datePath + "//" + masterNo + "_" + fileNm);
			logger.info("File upload file path : {}", uploadFile.getAbsoluteFile());
			if (uploadFile.exists()) {
				uploadFile.delete();
				logger.info("Exists file (delete) : {}", uploadFile.getAbsolutePath());
			}

			// 업로드
			logger.info("Transfer to : {} ", uploadFile.getAbsoluteFile());
			file.transferTo(uploadFile);

			// DB File경로 업데이트
			String filePathForDb = uploadFile.getAbsolutePath();
			filePathForDb = filePathForDb.replace("\\", "/");
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("file", filePathForDb);
			parameter.put("masterNo", masterNo);
			logger.info("DB Update : ChipMaster file path.");
			mapper.updateFilePath(parameter);

		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}

		return ApiResponse.success("OK");
	}

	public File getChipMasterFile(String masterNo) {

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("masterNo", masterNo);
		logger.info("exexute!!");
		List<CamelCaseMap> list = mapper.getChipMasterFilePath(parameter);
		
		if ( list == null ) {
			logger.error("0");
			return null;
		} else {
			logger.info("0-1");
		}
		if (list.isEmpty()) {
			logger.error("1");
			return null;
		} else {
			logger.info("1-1");
		}
		if (list.size() <= 0) {
			logger.error("2");
			return null;
		} else {
			logger.info("2-1");
		}
		CamelCaseMap map = list.get(0);
		if ( map == null ) {
			return null;
		} else {
			
		}
		String filePath = (String)map.getOrDefault("fileName", "");
		if (filePath.isEmpty()) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		return file;
	}

	public ApiResponse uploadChipMasterWithExcel(String masterNo, MultipartFile file) {

		try {

			logger.info("Upload root path : {}", uploadPath);

			// 날짜별 디렉토리 생성
			Date today = new Date();
			String yyyy = DateFormatUtils.format(today, "yyyy");
			String mm = DateFormatUtils.format(today, "MM");
			String dd = DateFormatUtils.format(today, "dd");
			String datePath = uploadPath + "\\" + yyyy + "\\" + mm + "\\" + dd;
			File directory = new File(datePath);
			logger.info("Daily upload path : {}", datePath);

			if (!directory.exists()) {
				logger.info("Create daily upload directory : {}", directory.getAbsolutePath());
				if (directory.mkdirs()) {
					logger.info("Create daily upload directory [OK]");
				}
			}

			// 업로드 파일명 생성
			String fileNm = new String(file.getOriginalFilename().getBytes("8859_1"), "UTF-8");
			if (fileNm.endsWith(".dec")) {
				fileNm = fileNm.replace(".dec", "");
			}

			File uploadFile = new File(datePath + "//" + fileNm);
			logger.info("File upload file path : {}", uploadFile.getAbsoluteFile());
			if (uploadFile.exists()) {
				uploadFile.delete();
				logger.info("Exists file (delete) : {}", uploadFile.getAbsolutePath());
			}

			// 업로드
			logger.info("Transfer to : {} ", uploadFile.getAbsoluteFile());
			file.transferTo(uploadFile);

			// DRM 복호화
			// drmdecExecPath
			logger.info("DRM 복호화 수행...");
			String chipMasterFilePath = uploadFile.getAbsolutePath();
			Runtime runTime = Runtime.getRuntime();
			String[] argv = { this.drmdecExePath, "-f", chipMasterFilePath };
			Process drmProcess = runTime.exec(argv);
			drmProcess.waitFor();
			InputStream is = drmProcess.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
			String outputString = br.readLine();
			logger.info(outputString);
			br.close();
			bis.close();
			is.close();
			int exitCode = drmProcess.exitValue();
			if (exitCode != 0) {
				logger.error("DRM 복화화에 실패 하였습니다");
				return ApiResponse.error(500, "DRM 복화화에 실패 하였습니다");
			}
			Thread.sleep(3000);
			String drmDecFileName = chipMasterFilePath + ".dec";
			File drmDecFile = new File(drmDecFileName);
			if (!drmDecFile.exists()) {
				logger.error("복호화 된 파일을 찾을 수 없습니다.");
			}
			uploadFile.delete();
			drmDecFile.renameTo(uploadFile);

			logger.info("엑셀 파일 열기 : {}", uploadFile.getAbsolutePath());
			ExcelUtils xlsUtil = new ExcelUtils();
			xlsUtil.OpenExcel(uploadFile.getAbsolutePath());
			if (xlsUtil.WorkBook == null) {
				logger.error("엑셀 열기에 실패 하였습니다.");
				return ApiResponse.error(500, "엑셀 열기에 실패 하였습니다.");
			}
			xlsUtil.OpenSheetLike("성적서");
			if (xlsUtil.Sheet == null) {
				logger.error("칩 마스터 엑셀 파일에서 시트명[성적서]를 찾지 못하였습니다.");
				return ApiResponse.error(500, "칩 마스터 엑셀 파일에서 시트명[성적서]를 찾지 못하였습니다.");
			}

			ChipMasterM cmStdM = new ChipMasterM();

			String outMasterNo = "";
			// --- 칩 마스터 CM_STD_M ---
			logger.info("DB[CM_STD_M] INSERT START >>>");
			// 라인
			String div = xlsUtil.GetCellString(2, 8);
			cmStdM.setDiv(div);
			// 제품군
			String productSpecGroup = xlsUtil.GetCellString(4, 20);
			cmStdM.setProductSpecGroup(productSpecGroup);
			// 제품명
			String productSpecName = xlsUtil.GetCellString(3, 8);
			cmStdM.setProductSpecName(productSpecName);
			cmStdM.setMasterNm(productSpecName);
			// 제품코드
			String productSpecCode = xlsUtil.GetCellString(4, 8);
			cmStdM.setProductSpecCode(productSpecCode);
			// 타겟1
			String target1 = xlsUtil.GetCellString(4, 11);
			cmStdM.setTarget1(target1);
			// 개정일
			String revDate = xlsUtil.GetCellStringFromDate(2, 20);
			cmStdM.setRevDate(revDate);
			// 개정번호
//			String revNo = xlsUtil.GetCellString(3, 20);
//			cmStdM.setRevNo(revNo);
			// 문서번호
			String documentNo = xlsUtil.GetCellString(1, 20);
			cmStdM.setDocumentNo(documentNo);

			logger.info(cmStdM.toString());

			if (productSpecName.isEmpty()) {
				logger.error("잘못된 형식의 유형 입니다");
				return ApiResponse.error(500, "잘못된 형식 입니다.(제품명을 찾지 못했습니다)");
			}

			Map<String, Object> stdMParam = new HashMap<String, Object>();
			stdMParam.put("masterNo", cmStdM.getMasterNo());
			stdMParam.put("masterNm", cmStdM.getMasterNm());
			stdMParam.put("productSpecCode", cmStdM.getProductSpecCode());
			stdMParam.put("productSpecName", cmStdM.getProductSpecName());
			stdMParam.put("masterType", cmStdM.getMasterType());
			stdMParam.put("revNo", cmStdM.getRevNo());
			stdMParam.put("fileNm", "");
			stdMParam.put("productSpecGroup", cmStdM.getProductSpecGroup());
			stdMParam.put("documentNo", cmStdM.getDocumentNo());
			stdMParam.put("revDate", cmStdM.getRevDate());
			stdMParam.put("target1", cmStdM.getTarget1());
			stdMParam.put("target2", cmStdM.getTarget2());
			stdMParam.put("dcSeq", "");
			stdMParam.put("div", cmStdM.getDiv());
			stdMParam.put("userId", "SYSTEM");
			stdMParam.put("outMasterNo", outMasterNo);

			mapper.addCmStdM(stdMParam);
			outMasterNo = stdMParam.getOrDefault("outMasterNo", "").toString();
			logger.info("New Master No : {}", outMasterNo);

			if (outMasterNo.equals("9999999")) {
				String errMsg = String.format("이미 존재 하는 칩 마스터 입니다. MASTERNM[%s] TARGET1[%s] DIV[%s] PRODUCTGROUP[%s]",
						cmStdM.getMasterNm(), cmStdM.getTarget1(), cmStdM.getDiv(), cmStdM.getProductSpecGroup());
				logger.error(errMsg);
				return ApiResponse.error(errMsg);
			} else {
				logger.info("DB [CM_STD_M] INSERT OK");

			}
			logger.info("DB[CM_STD_M] INSERT END <<<");

			// --- 칩 마스터 CM_STD_MEASUREINFO ---
			logger.info("DB[CM_STD_MEASUREINFO] INSERT START >>>");
			ChipMasterMeasureInfo measureInfo = new ChipMasterMeasureInfo();
			measureInfo.setMasterNo(outMasterNo);
			// 계측장비
			String msrEqp = xlsUtil.GetCellString(6, 4);
			measureInfo.setMeasureEqpId(msrEqp);
			// 측정JIG
			String msrJIG = xlsUtil.GetCellString(7, 4);
			measureInfo.setMeasureJIG(msrJIG);
			// 온습도
			String msrTemp = xlsUtil.GetCellString(8, 4);
			measureInfo.setTemperature(msrTemp);
			// 측정IF
			String msrIF = xlsUtil.GetCellString(9, 4);
			measureInfo.setMeasureIF(msrIF);
			// Calibration
			String msrCali = xlsUtil.GetCellString(10, 4);
			measureInfo.setCalibration(msrCali);
			// I.T Time
			String msrItTime = xlsUtil.GetCellString(11, 4);
			measureInfo.setItTime(msrItTime);
			// Filter
			String msrFilter = xlsUtil.GetCellString(12, 4);
			measureInfo.setFilter(msrFilter);
			// Program
			String msrVf = xlsUtil.GetCellString(13, 4);
			measureInfo.setVf(msrVf);
			// VF 적용 설비
			String msrVfEqp = xlsUtil.GetCellString(14, 4);
			measureInfo.setVfEqp(msrVfEqp);

			logger.info(measureInfo.toString());

			Map<String, Object> msrParam = new HashMap<String, Object>();
			msrParam.put("masterNo", outMasterNo);
			msrParam.put("measureEqpId", measureInfo.getMeasureEqpId());
			msrParam.put("measureJIG", measureInfo.getMeasureJIG());
			msrParam.put("temperature", measureInfo.getTemperature());
			msrParam.put("measureIF", measureInfo.getMeasureIF());
			msrParam.put("calibration", measureInfo.getCalibration());
			msrParam.put("itTime", measureInfo.getItTime());
			msrParam.put("filter", measureInfo.getFilter());
			msrParam.put("vf", measureInfo.getVf());
			msrParam.put("vfEqpId", measureInfo.getVfEqp());
			msrParam.put("userId", "SYSTEM");
			msrParam.put("retObj", "");
			msrParam.put("retMsg", "");
			mapper.addCmMsrInfo(msrParam);
			logger.info("DB[CM_STD_MEASUREINFO] INSERT END <<<");

			// --- 칩 마스터 CM_STD_TOLERANCE ---
			logger.info("DB[CM_STD_TOLERANCE] INSERT START >>>");
			ChipMasterTolerance tolerance = new ChipMasterTolerance();
			tolerance.setMasterNo(outMasterNo);
			// CIE X
			String tolrCIEX = xlsUtil.GetCellNumberFormat(16, 4, "0.#####");
			tolerance.setCieX(tolrCIEX);
			// CIE Y
			String tolrCIEY = xlsUtil.GetCellNumberFormat(17, 4, "0.#####");
			tolerance.setCieY(tolrCIEY);
			// FLUX
			String tolrFlux = xlsUtil.GetCellNumberFormat(18, 4, "0.#####");
			tolerance.setFlux(tolrFlux);
			// VF
			String tolrVF = xlsUtil.GetCellNumberFormat(19, 4, "0.#####");
			tolerance.setVf(tolrVF);
			// WD
			String tolrWD = xlsUtil.GetCellNumberFormat(20, 4, "0.#####");
			tolerance.setWd(tolrWD);
			// WP
			String tolrWP = xlsUtil.GetCellNumberFormat(21, 4, "0.#####");
			tolerance.setWp(tolrWP);
			// CRI
			String tolrCRI = xlsUtil.GetCellNumberFormat(22, 4, "0.#####");
			tolerance.setCri(tolrCRI);

			logger.info(tolerance.toString());

			Map<String, Object> toleParam = new HashMap<String, Object>();
			toleParam.put("masterNo", outMasterNo);
			toleParam.put("cieX", tolerance.getCieX());
			toleParam.put("cieY", tolerance.getCieY());
			toleParam.put("flux", tolerance.getFlux());
			toleParam.put("vf", tolerance.getVf());
			toleParam.put("wd", tolerance.getWd());
			toleParam.put("wp", tolerance.getWp());
			toleParam.put("cri", tolerance.getCri());
			toleParam.put("userId", "SYSTEM");
			toleParam.put("retObj", "");
			toleParam.put("retMsg", "");
			mapper.addCmTolerance(toleParam);
			logger.info("DB Execute query");
			int tolrRET = Integer.valueOf(toleParam.get("retObj").toString());
			logger.info("tolrRET is [" + tolrRET + "]");
			if (tolrRET < 0) {
				String tolrMsg = toleParam.get("retMsg").toString();
				String errMsg = String.format("[%s] %s", outMasterNo, tolrMsg);
				logger.error(errMsg);
				deleteMaster(outMasterNo);
				return ApiResponse.error(errMsg);
			} else {
				logger.info("DB [CM_STD_TOLERANCE] INSERT OK");
			}
			logger.info("DB[CM_STD_TOLERANCE] INSERT END <<<");

			// --- 칩 마스터 CM_STD_PARTLIST ---
			logger.info("DB[CM_STD_PARTLIST] INSERT START >>>");
			ChipMasterPartList partList = new ChipMasterPartList();
			partList.setMasterNo(outMasterNo);
			// Type
			partList.setType(xlsUtil.GetCellString(24, 4));
			// Chip 명
			partList.setChipName(xlsUtil.GetCellString(25, 4));
			// WD 파장
			partList.setWd(xlsUtil.GetCellString(26, 4));
			// Phosphor
			partList.setPhosphor(xlsUtil.GetCellString(27, 4));
			// Lead Frame
			partList.setLeadFrame(xlsUtil.GetCellString(28, 4));

			logger.info(partList.toString());

			Map<String, Object> partParam = new HashMap<String, Object>();
			partParam.put("masterNo", outMasterNo);
			partParam.put("type", partList.getType());
			partParam.put("chipNm", partList.getChipName());
			partParam.put("wd", partList.getWd());
			partParam.put("phosphor", partList.getPhosphor());
			partParam.put("userId", "SYSTEM");
			partParam.put("retObj", "");
			partParam.put("retMsg", "");
			mapper.addCmPartlist(partParam);

			int partRET = Integer.valueOf(partParam.get("retObj").toString());
			if (partRET < 0) {
				String partMsg = partParam.get("retMsg").toString();
				String errMsg = String.format("[%s] %s", outMasterNo, partMsg);
				logger.error(errMsg);
				deleteMaster(outMasterNo);
				return ApiResponse.error(errMsg);
			}
			logger.info("DB[CM_STD_PARTLIST] INSERT END <<<");

			// -- DATA
			logger.info("DB[CM_STD_DATA] INSERT START >>>");
			int dataRow = 7;
			int dataCol = 9;
			List<ChipMasterData> datas = new ArrayList();
			for (int i = 0; i < 16; i++) {
				ChipMasterData data = new ChipMasterData();
				data.setMasterNo(outMasterNo);
				for (int j = 0; j < 6; j++) {
					logger.info("[" + i + "," + j + "]---- loop ---- start");
					String hdrName = xlsUtil.GetCellString(dataRow - 1, (dataCol + 2) + (j * 2));
					logger.info("Header Name : " + hdrName);
					if (hdrName == null) {
						logger.warn("NO EXIST HEADER >> Next step loop.");
						continue;
					}
					String dataHdrName = hdrName.replace(" ", "").toUpperCase();
					if (dataHdrName.isEmpty())
						continue;
					logger.info("data hdr : " + dataHdrName);
					String no = xlsUtil.GetCellNumberFormat(dataRow + i, dataCol, "0");
					data.setNo(no);
					// 보정치 => NO = 8000 시스템 규약
					if (i == 15) {
						data.setNo("8000");
					}
					String dataValue = xlsUtil.GetCellString(dataRow + (i * 2), (dataCol + 2) + (j * 2));

					if (dataValue.isEmpty() && StringUtils.isNumeric(dataValue)) {
						deleteMaster(outMasterNo);
						String errMsg = String.format("CM_STD_DATA 데이터 형식이 잘못 되었습니다. ITEM[%s] NO[%s] VALUE[%s]",
								hdrName, no, dataValue);
						return ApiResponse.error(errMsg);
					}

					if (StringUtils.isNumeric(dataValue)) {
						Double dblValue = Double.valueOf(dataValue);
						DecimalFormat decimalformat = new DecimalFormat("0.#####");
						dataValue = decimalformat.format(dblValue);
					}
					if (dataHdrName.contains("CIEX")) {
						data.setCieX(dataValue);
					} else if (dataHdrName.contains("CIEY")) {
						data.setCieY(dataValue);
					} else if (dataHdrName.contains("FLUX")) {
						data.setFlux(dataValue);
					} else if (dataHdrName.contains("VF")) {
						data.setVf(dataValue);
					} else if (dataHdrName.contains("WD")) {
						data.setWd(dataValue);
					} else if (dataHdrName.contains("WP")) {
						data.setWp(dataValue);
					} else if (dataHdrName.contains("CRI")) {
						data.setCri(dataValue);
					}
					logger.info("[" + i + "," + j + "]---- loop ---- end");
				}
				logger.info(data.toString());
				datas.add(data);
			}

			for (ChipMasterData data : datas) {

				Map<String, Object> dataParam = new HashMap<String, Object>();
				dataParam.put("masterNo", outMasterNo);
				dataParam.put("no", data.getNo());
				dataParam.put("cieX", data.getCieX());
				dataParam.put("cieY", data.getCieY());
				dataParam.put("flux", data.getFlux());
				dataParam.put("iv", "");
				dataParam.put("vf", data.getVf());
				dataParam.put("wd", data.getWd());
				dataParam.put("wp", data.getWp());
				dataParam.put("cri", data.getCri());
				dataParam.put("userId", "SYSTEM");
				dataParam.put("retObj", "");
				dataParam.put("retMsg", "");
				mapper.addCmData(dataParam);

			}

			logger.info("DB[CM_STD_DATA] INSERT END <<<");

			logger.info("EXCEL CLOSE");
			xlsUtil.Close();

			// DB File경로 업데이트
			String filePathForDb = uploadFile.getAbsolutePath();
			filePathForDb = filePathForDb.replace("\\", "/");
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("file", filePathForDb);
			parameter.put("masterNo", outMasterNo);
			logger.info("DB Update : ChipMaster file path.[" + filePathForDb + "]");
			mapper.updateFilePath(parameter);

		} catch (Exception e) {
			logger.error("Exception!!!");
			logger.error(e.getLocalizedMessage());
			logger.error(e.getMessage());
			return ApiResponse.error(e.getMessage());
		}

		return ApiResponse.success("OK");
	}

	public ApiResponse parseMasterFile(MultipartFile file) {

		// ArrayList<ChipMasterM> data = new ArrayList<ChipMasterM>();
		ArrayList<HashMap<String, Object>> mapList = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			File tempfile = File.createTempFile("temp_", ".xlsx");
			// String 
			file.transferTo(tempfile);
			
			logger.info("엑셀 파일 열기 : {}", tempfile.getAbsolutePath());
			ExcelUtils xlsUtil = new ExcelUtils();
			xlsUtil.OpenExcel(tempfile.getAbsolutePath());
			if (xlsUtil.WorkBook == null) {
				String errorMsg = "엑셀 열기에 실패 하였습니다."; 
				logger.error(errorMsg);
				return ApiResponse.error(0, errorMsg);
			}
			xlsUtil.OpenSheetLike("성적서");
			if (xlsUtil.Sheet == null) {
				String errorMsg = "칩 마스터 엑셀 파일에서 시트명[성적서]를 찾지 못하였습니다.";
				logger.error(errorMsg);
				// return ApiResponse.error(errorMsg);
				return ApiResponse.error(201, errorMsg);
			}

			// ChipMasterM cmStdM = new ChipMasterM();
			
			// data.add(cmStdM);
			
			String outMasterNo = "";
			// --- 칩 마스터 CM_STD_M ---
			// 라인
			String div = xlsUtil.GetCellString(2, 8);
			//cmStdM.setDiv(div);
			map.put("div", div);
			// 제품군
			String productSpecGroup = xlsUtil.GetCellString2(4, 20);
			//cmStdM.setProductSpecGroup(productSpecGroup);
			map.put("productSpecGroup", productSpecGroup);
			// 제품명
			String productSpecName = xlsUtil.GetCellString(3, 8);
			//cmStdM.setProductSpecName(productSpecName);
			//cmStdM.setMasterNm(productSpecName);
			if ( productSpecName.isEmpty()) {
				return ApiResponse.error(301, "제품명을 찾을 수 없습니다.");
			}
			map.put("productSpecName", productSpecName);
			// 제품코드
			String productSpecCode = xlsUtil.GetCellString(4, 8);
			//cmStdM.setProductSpecCode(productSpecCode);
			map.put("productSpecCode", productSpecCode);
			// 타겟1
			String target1 = xlsUtil.GetCellString(4, 11);
			// cmStdM.setTarget1(target1);
			map.put("target1", target1);
			
			// 개정일
			String revDate = xlsUtil.GetCellStringFromDate(2, 20);
			// cmStdM.setRevDate(revDate);
			map.put("revDate", revDate);
			
			// 개정번호
			String revNo = xlsUtil.GetCellString(3, 20);
			// cmStdM.setRevNo(revNo);
			map.put("revNo", revNo);
			
			// 문서번호
			String documentNo = xlsUtil.GetCellString(1, 20);
			// cmStdM.setDocumentNo(documentNo);
			map.put("documentNo", documentNo);

			// logger.info(cmStdM.toString());
			
			mapList.add(map);
			
			logger.info("EXCEL CLOSE");
			
			xlsUtil.Close();
			
			tempfile.deleteOnExit();

		} catch (Exception e) {
			logger.error("Exception!!!");
			logger.error(e.getLocalizedMessage());
			logger.error(e.getMessage());
			return ApiResponse.error(e.getMessage());
		}

		return ApiResponse.success("OK", mapList);
	}

	public void deleteMaster(String masterNo) {
		logger.info("마스터 데이터 삭제 [" + masterNo + "]");
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("masterNo", masterNo);
		mapper.deleteCmData(parameter);
	}
	
}
