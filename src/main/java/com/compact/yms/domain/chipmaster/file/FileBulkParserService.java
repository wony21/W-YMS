package com.compact.yms.domain.chipmaster.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.compact.yms.common.CommonData;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.utils.ExcelUtils;
import com.sun.tools.internal.ws.processor.util.DirectoryUtil;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.UnzipParameters;

@Service
@Component
@Configurable
public class FileBulkParserService {

	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	FileMapper mapper;

	@Value("${file.path}")
	String uploadPath;

	@Value("${drmdec}")
	String drmdecExePath;

	/*
	 * master.bulk.file.path=D:\\ChipMasterStorage\\Bulk\\Upload
	 * master.bulk.file.work=D:\\ChipMasterStorage\\Bulk\\Work
	 * master.bulk.file.error=D:\\ChipMasterStorage\\Bulk\\Error
	 * master.bulk.file.comp=D:\\ChipMasterStorage\\Bulk\\Comp
	 */
	@Value("${master.bulk.file.path}")
	String bulkUploadPath;

	@Value("${master.bulk.file.work}")
	String bulkWorkPath;

	@Value("${master.bulk.file.error}")
	String bulkErrorPath;

	@Value("${master.bulk.file.comp}")
	String bulkCompPath;

	/**
	 * 칩마스터 엑셀파일 DRM 복호화
	 * 
	 * @param chipMasterFilePath 원본마스터 엑셀파일
	 * @return
	 * @throws Exception
	 */
	public File getDrmFile(String chipMasterFilePath) throws Exception {

		logger.info("DRM 복호화 수행...");
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
			return null;
		}
		Thread.sleep(3000);
		String drmDecFileName = chipMasterFilePath + ".dec";
		File drmDecFile = new File(drmDecFileName);
		if (!drmDecFile.exists()) {
			logger.error("복호화 된 파일을 찾을 수 없습니다");
		}
		return drmDecFile;
	}

	public void moveFromUploadToWork() {

		logger.info("Get master bulk target files.");

		File uploadDir = new File(this.bulkUploadPath);

		File[] zipFiles = uploadDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".zip");
			}
		});

		logger.info("---> file count : [{}]", zipFiles.length);

		for (File zip : zipFiles) {
			File destFile = new File(this.bulkWorkPath + "\\" + zip.getName());
			try {
				FileUtils.moveFile(zip, destFile);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public void unZipFiles() throws Exception {

		logger.info("Unzip master target files.");

		File workDir = new File(this.bulkWorkPath);

		File[] zipFiles = workDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".zip");
			}
		});

		logger.info("Unzip master target files count [{}]", zipFiles.length);

		for (File zip : zipFiles) {

			// 파일명으로 디렉토리 생성
			int extensionPosition = zip.getName().lastIndexOf('.');
			String folderName = zip.getName().substring(0, extensionPosition);
			folderName = this.bulkWorkPath + "\\" + folderName;
			logger.info("target dir [{}]", folderName);
			File dir = new File(folderName);
			if (!dir.exists()) {
				dir.mkdir();
			}

			// 생성된 디렉토리 안에 압축 해제
			ZipFile zipFile = new ZipFile(zip);
			zipFile.setCharset(Charset.forName("euc-kr"));
			Charset charset = zipFile.getCharset();
			logger.info("Charset is [{}]", charset.displayName());
			zipFile.extractAll(folderName);
			zipFile.close();

			// 압축 완료 후 정상이면 파일 삭제
			zip.delete();

		}
	}

	public void DecryptDRM() {

		File workDir = new File(this.bulkWorkPath);

		File[] workTargetDir = workDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				logger.info(String.format("IsDir[%s]\tName[%s]Dir[%s]", dir.isDirectory(), name, dir.getName()));
				return dir.isDirectory();
			}
		});

		for (File subDir : workTargetDir) {

			if (!subDir.isDirectory())
				continue;

			File[] masterDrmFiles = subDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xlsx") || name.endsWith(".xls");
				}
			});
			logger.info(String.format("[%s] directory in file count[%d].", subDir.getName(), masterDrmFiles.length));
			// 대상없음
			if (masterDrmFiles.length == 0)
				continue;

			for (File file : masterDrmFiles) {

				try {

					logger.info(String.format("DRM file decrypting [%s]", file.getAbsolutePath()));

					File masterFile = getDrmFile(file.getAbsolutePath());

					if (masterFile == null) {
						logger.error(String.format("DRM Fail >> continue process [%s]", file.getAbsoluteFile()));
						continue;
					}

					file.delete();

					logger.info(String.format("Rename to decrption file to [%s]", file.getAbsolutePath()));

					masterFile.renameTo(file);

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			logger.info("Directory : [{}]", subDir.getName());
		}

	}

	public String Insert_CM_STD_M(ChipMasterM cmStdM) {
		String outMasterNo = "";

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
		stdMParam.put("status", cmStdM.getStatus());
		stdMParam.put("reqSite", cmStdM.getReqSite());
		stdMParam.put("subKey", cmStdM.getSubKey());
		stdMParam.put("regType", cmStdM.getRegType());
		stdMParam.put("userId", "SYSTEM");
		stdMParam.put("outMasterNo", outMasterNo);

		int ret = mapper.addCmStdM_v5(stdMParam);
		logger.info("DB insert result [{}]", ret);
		// 2. DB저장 및 마스터 번호 생성(발급)
		outMasterNo = stdMParam.getOrDefault("outMasterNo", "").toString();
		logger.info("New Master No : {}", outMasterNo);

		if (outMasterNo.equals("9999999")) {
			String errMsg = String.format("이미 존재 하는 칩 마스터 입니다. MASTERNM[%s] TARGET1[%s] DIV[%s] PRODUCTGROUP[%s]",
					cmStdM.getMasterNm(), cmStdM.getTarget1(), cmStdM.getDiv(), cmStdM.getProductSpecGroup());
			logger.error(errMsg);
			// move to error
			return null;
		} else {
			logger.info("DB [CM_STD_M] INSERT OK");
		}

		return outMasterNo;

	}

	public ChipMasterM parsingExcel(String line, String productGroup, String requestSite, String status,
			File masterFile) throws IOException {

		String orgFileName = masterFile.getName();

		ExcelUtils xlsUtil = new ExcelUtils();

		xlsUtil.OpenExcel(masterFile.getAbsolutePath());

		if (xlsUtil.WorkBook == null) {
			logger.error("엑셀 열기에 실패 하였습니다.");
			return null;
		}

		xlsUtil.OpenSheetLike("성적서");

		if (xlsUtil.Sheet == null) {
			logger.error("칩 마스터 엑셀 파일에서 시트명[성적서]를 찾지 못하였습니다.");
			return null;
		}

		// 1. 파일에서 CM_STD_M 데이터 생성
		ChipMasterM cmStdM = new ChipMasterM();

		String outMasterNo = "";
		// --- 칩 마스터 CM_STD_M ---
		logger.info("DB[CM_STD_M] INSERT START >>>");
		// 라인은 우선 순위가 엑셀 안의 값
		String div = xlsUtil.GetCellString(2, 8);
		if (div.equals(line)) {
			cmStdM.setDiv(line);
		} else {
			cmStdM.setDiv(div);
		}
		
		// Gold/Silver
		if (orgFileName.toUpperCase().contains("GOLDEN")) {
			cmStdM.setMasterType(CommonData.MASTERTYPE.Golden);
		} else if (orgFileName.toUpperCase().contains("SILVER1")) {
			cmStdM.setMasterType(CommonData.MASTERTYPE.Silver1);
		} else if (orgFileName.toUpperCase().contains("SILVER2")) {
			cmStdM.setMasterType(CommonData.MASTERTYPE.Silver2);
		} else if (orgFileName.toUpperCase().contains("SILVER3")) {
			cmStdM.setMasterType(CommonData.MASTERTYPE.Silver3);
		} else if (orgFileName.toUpperCase().contains("SILVER4")) {
			cmStdM.setMasterType(CommonData.MASTERTYPE.Silver4);
		}

		// 제품군은 우선 순위가 사용자 선택 값
		String productSpecGroup = xlsUtil.GetCellString(4, 8);
		if (productGroup.equals(productSpecGroup)) {
			cmStdM.setProductSpecGroup(productGroup);
		} else {
			cmStdM.setProductSpecGroup(productGroup);
		}
		// 제품명
		String productSpecName = xlsUtil.GetCellString(31, 8);
		cmStdM.setProductSpecName(productSpecName);
		
		// 마스터명 = 파일명
		String masterName = masterFile.getName();
		String ext = masterName.substring(masterName.lastIndexOf(".") + 1);
		masterName = masterName.replace("." + ext, "");
		cmStdM.setMasterNm(masterName);
		// 제품코드
		String productSpecCode = xlsUtil.GetCellString(31, 3);
		cmStdM.setProductSpecCode(productSpecCode);
		// 타겟1
		String target1 = xlsUtil.GetCellString(32, 3);
		cmStdM.setTarget1(target1);
		// 개정일
		String revDate = xlsUtil.GetCellStringFromDate(2, 20);
		cmStdM.setRevDate(revDate);
		// 개정번호
		String revNo = xlsUtil.GetCellString(3, 20);
		if (!revNo.isEmpty()) {
			cmStdM.setRevNo(Integer.valueOf(revNo));
		}
		// 문서번호
		String documentNo = xlsUtil.GetCellString(1, 20);
		cmStdM.setDocumentNo(documentNo);
		// 요청업체
		cmStdM.setReqSite(requestSite);

		// 등록타입
		int verifyCode = 2; // 시작실 : 1, 검증(배포) : 2
		cmStdM.setRegType(verifyCode);

		// STATUS
		String deployCode = "A"; // 가용(배포)
		String QAStorageCode = "Q"; // QA보관
		cmStdM.setStatus(status);

		xlsUtil.Close();

		logger.info(cmStdM.toString());

//		if (productSpecName.isEmpty()) {
//			logger.error("잘못된 형식 입니다.(제품명을 찾지 못했습니다.)");
//			// movt to error
//			return null;
//		}

		return cmStdM;
	}

	public File moveToError(File file) {

		String datePath = createDailyDir(this.bulkErrorPath);
		File directory = new File(datePath);
		logger.info("Daily upload path : {}", datePath);

		if (!directory.exists()) {
			logger.info("Create daily upload directory : {}", directory.getAbsolutePath());
			if (directory.mkdirs()) {
				logger.info("Create daily upload directory [OK]");
			}
		}

		// move to comp directory
		File finalMasterFile = new File(datePath + "\\" + file.getName());
		file.renameTo(finalMasterFile);

		return finalMasterFile;

	}

	public File moveToWebUpload(String masterNo, File masterFile) {

		// 날짜별 디렉토리 생성
		String datePath = createDailyDir(uploadPath);
		File directory = new File(datePath);
		logger.info("Daily upload path : {}", datePath);

		if (!directory.exists()) {
			logger.info("Create daily upload directory : {}", directory.getAbsolutePath());
			if (directory.mkdirs()) {
				logger.info("Create daily upload directory [OK]");
			}
		}
		// move to comp directory
		File finalMasterFile = new File(datePath + "\\" + masterNo + "_" + masterFile.getName());
		masterFile.renameTo(finalMasterFile);
		// FileUtils.copyFile(masterFile, finalMasterFile);

		return finalMasterFile;

	}

	public String createDailyDir(String targetDir) {
		Date today = new Date();
		String yyyy = DateFormatUtils.format(today, "yyyy");
		String mm = DateFormatUtils.format(today, "MM");
		String dd = DateFormatUtils.format(today, "dd");
		String datePath = targetDir + "\\" + yyyy + "\\" + mm + "\\" + dd;
		return datePath;
	}

	public void DBInsert() throws IOException {

		File workDir = new File(this.bulkWorkPath);

		for (File subDir : workDir.listFiles()) {
			if (!subDir.isDirectory())
				continue;

			File[] masterFiles = subDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xlsx") || name.endsWith(".xls");
				}
			});

			// SSV_JMTOP_3030_20211123
			String subDirname = subDir.getName();
			String[] dirParam = subDirname.split("_");

			if (dirParam.length != 5) {
				logger.warn("filename rule is not matched. (요청업체_라인_제품군_날짜)");
				continue;
			}

			// 요청업체
			String requestSite = dirParam[0];
			// Line
			String line = dirParam[1];
			// ProductGroup
			String productGroup = dirParam[2];
			// Status
			String status = dirParam[3];

			for (File masterFile : masterFiles) {

				ChipMasterM cmStdM = parsingExcel(line, productGroup, requestSite, status, masterFile);

				if (cmStdM == null) {
					// move to error
					moveToError(masterFile);
					continue;
				}

				String outMasterNo = Insert_CM_STD_M(cmStdM);

				if (outMasterNo.isEmpty()) {
					// move to error
					moveToError(masterFile);
					continue;
				}

				// move to complete
				File webUploadFile = moveToWebUpload(outMasterNo, masterFile);

				// Update file path
				String uploadFinalPath = webUploadFile.getAbsolutePath();
				Map<String, Object> parameter = new HashMap<String, Object>();
				parameter.put("masterNo", outMasterNo);
				parameter.put("file", uploadFinalPath);
				mapper.updateFilePath(parameter);

			}
		}
	}

	public void deleteFileOrFolder(File fileOrDir) {

		File[] subFileOrDir = fileOrDir.listFiles();

		for (File file : subFileOrDir) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteFileOrFolder(file);
				file.delete();
			}
		}

		fileOrDir.delete();
	}

	public void ClearWorkDirectory() {

		File workDir = new File(this.bulkWorkPath);

		for (File workSubFile : workDir.listFiles()) {

			deleteFileOrFolder(workSubFile);
		}
	}

	public ApiResponse moveToBulkUploadPath(String reqSite, String div, String productGroup, String status,
			MultipartFile file) {

		String todayYMDhms = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		String renameFilename = String.format("%s_%s_%s_%s_%s.zip", reqSite, div, productGroup, status, todayYMDhms);
		File renameFile = new File(bulkUploadPath + "\\" + renameFilename);

		try {

			logger.info(String.format("transferTo [%s] to [%s].", file.getOriginalFilename(),
					renameFile.getAbsolutePath()));

			file.transferTo(renameFile);

			ZipFile zipFile = new ZipFile(renameFile);

			if (!zipFile.isValidZipFile()) {
				zipFile.close();
				logger.warn("Is Not a zipfile. Only needs zipfile [%s]", renameFile);
				renameFile.delete();
				logger.warn("File deleted [%s]", renameFile);
				return ApiResponse.error("ZIP압축 파일이 아닙니다");
			}

			zipFile.close();

			return ApiResponse.success("OK");

		} catch (IllegalStateException e) {
			logger.error(e.getMessage());
			return ApiResponse.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
			return ApiResponse.error(e.getMessage());
		}

	}

}
