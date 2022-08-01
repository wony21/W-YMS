package com.compact.base.domain.analysis;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.compact.base.common.CamelCaseMap;
import com.compact.base.domain.CTQ.CTQService;
import com.compact.base.domain.analysis.DTO.ChartShareData;
import com.compact.base.domain.analysis.DTO.YieldFileData;
import com.compact.base.domain.analysis.DTO.YieldFileItemData;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

@Service
public class AnalysisService {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

	@Autowired
	AnalysisMapper mapper;

//	# NAS 접속정보
//	nas.host=192.168.32.61
//	nas.share.folder=data_move
//	nas.user=Administrator
//	nas.password=WkaQhdtnsenqn01!
	@Value("${nas.host}")
	String nasHost;

	@Value("${nas.share.folder}")
	String nasShareFolder;

	@Value("${nas.user}")
	String nasUser;

	@Value("${nas.password}")
	String nasPassword;

	@Value("${local.download.path}")
	String localDownloadPath;

	/**
	 * 수율 파일의 위치를 가져온다.
	 * 
	 * @param stepSeq         TT,TP,NM
	 * @param productSpecName 제품코드
	 * @param startTime       시작일
	 * @param endTime         종료일
	 * @param lotID           LOTID
	 * @return
	 */
	public List getFileRemoteLocation(String div, String stepSeq, String productSpecName, String startTime, String endTime,
			String lotID) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("div", div);
		param.put("stepSeq", stepSeq);
		param.put("productSpecName", productSpecName);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("lotID", lotID);
		/* stepSeq array */
		String[] stepSeqs = stepSeq.split(",");
		param.put("stepSeqs", stepSeq.isEmpty() ? null : stepSeqs);
		/* productSpecName array */
		String[] productSpecNames = productSpecName.split(",");
		param.put("productSpecNames", productSpecName.isEmpty() ? null : productSpecNames);
		/* LotID array */
		String[] lotIds = lotID.split(",");
		param.put("lotIds", lotID.isEmpty() ? null : lotIds);

		return mapper.getFileLocation(param);
	}

	public List getFileRemoteLocation(String div, String startTime, String endTime, String stepSeq,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String frameName, String lotID) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("div", div);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		/* stepSeq array */
		String[] stepSeqs = stepSeq.split(",");
		param.put("stepSeqs", stepSeq.isEmpty() ? null : stepSeqs);
		/* productSpecGroup array */
		String[] productSpecGroups = productSpecGroup.split(",");
		param.put("productSpecGroups", productSpecGroup.isEmpty() ? null : productSpecGroups);
		/* productSpecName array */
		String[] productSpecNames = productSpecName.split(",");
		param.put("productSpecNames", productSpecName.isEmpty() ? null : productSpecNames);
		/* program array */
		String[] programs = program.split(",");
		param.put("programs", program.isEmpty() ? null : programs);
		/* target array */
		String[] targets = target.split(",");
		param.put("targets", target.isEmpty() ? null : targets);
		/* chipSpec array */
		String[] chipSpecs = chipSpec.split(",");
		param.put("chipSpecs", chipSpec.isEmpty() ? null : chipSpecs);
		/* frameName array */
		String[] frameNames = frameName.split(",");
		param.put("frameNames", frameName.isEmpty() ? null : frameNames);
		/* LotID array */
		String[] lotIds = lotID.split(",");
		param.put("lotIds", lotID.isEmpty() ? null : lotIds);

		return mapper.getFileLocation(param);

	}

	/**
	 * NAS에 위치한 파일을 로컬로 다운로드 한다. 로컬의 임시디렉토리에 임시파일로 생성된다.
	 * 
	 * @param remoteFilePath 원격지(NAS)의 파일
	 * @return
	 * @throws Exception
	 */
	public File downloadRemoteFile(String remoteFilePath) throws Exception {

		java.io.File workFile = null;

		SMBClient client = new SMBClient();
		Connection connection = client.connect(nasHost);

		if (connection.isConnected()) {

			AuthenticationContext ac = new AuthenticationContext(nasUser, nasPassword.toCharArray(), null);
			Session session = connection.authenticate(ac);
			DiskShare share = (DiskShare) session.connectShare(nasShareFolder);
			if (share.isConnected()) {

				com.hierynomus.smbj.share.File remoteFile = share.openFile(remoteFilePath,
						EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN,
						null);

				InputStream fileInputStream = new BufferedInputStream(remoteFile.getInputStream());

				byte[] fileData = IOUtils.toByteArray(fileInputStream);

				workFile = java.io.File.createTempFile("tmp_", ".csv");

				FileUtils.writeByteArrayToFile(workFile, fileData);

				share.close();
			}
			session.close();
			connection.close();
		}

		client.close();

		return workFile;
	}

	/**
	 * 해당되는 수율 파일의 전체 측정 항목명을 가져온다.
	 * 
	 * @param stepSeq
	 * @param productSpecName
	 * @param startTime
	 * @param endTime
	 * @param lotID
	 * @return
	 * @throws Exception
	 */
	public List getYiedItemNames(String div, String stepSeq, String productSpecName, String startTime, String endTime, String lotID)
			throws Exception {

		List<String> allItemNameValues = new ArrayList<String>();

		List<CamelCaseMap> remoteFiles = this.getFileRemoteLocation(div, stepSeq, productSpecName, startTime, endTime, lotID);

		for (CamelCaseMap remoteFile : remoteFiles) {

			String remoteFileName = remoteFile.getString("remoteFileName");

			File downloadFile = this.downloadRemoteFile(remoteFileName);

			List<String> itemNameValues = AnalysisUtils.getItemNames(downloadFile);

			downloadFile.delete();

			for (String itemNameValue : itemNameValues) {

				if (!allItemNameValues.contains(itemNameValue)) {

					allItemNameValues.add(itemNameValue);
				}
			}
		}

		return allItemNameValues;
	}

	/**
	 * 해당되는 수율 데이터의 측정 항목을 반환한다. 각 LOT과 SEQ별로 구분하여 반환.
	 * 
	 * @param stepSeq
	 * @param productSpecName
	 * @param startTime
	 * @param endTime
	 * @param lotID
	 * @param itemFilter
	 * @return
	 * @throws Exception
	 */
	public List getYieldRawdata(String div, String startTime, String endTime, String stepSeq, String productSpecGroup,
			String productSpecName, String program, String target, String chipSpec, String frameName, String lotID,
			String itemFilter, double min, double max, int stepCount) throws Exception {

		// 조회할 아이템명
		String[] itemFilterNames = new String[0];
		logger.info("Request itemFilter [{}]", itemFilter);
		if (!itemFilter.isEmpty()) {
			itemFilterNames = itemFilter.split(",");
		}
		// 최종 반환 리스트
		List<YieldFileData> list = new ArrayList<YieldFileData>();
		// NAS 수율파일 정보
		List<CamelCaseMap> remoteFiles = this.getFileRemoteLocation(div, startTime, endTime, stepSeq, productSpecGroup,
				productSpecName, program, target, chipSpec, frameName, lotID);

		for (CamelCaseMap remoteFile : remoteFiles) {

			String lotIDinDB = remoteFile.getString("lotId");
			String lotSeq = remoteFile.getString("seq");
			String fileName = remoteFile.getString("fileName");
			String remoteFileName = remoteFile.getString("remoteFileName");
			// 파일 다운로드
			File downloadFile = this.downloadRemoteFile(remoteFileName);
			if (downloadFile != null) {

				String fileAllString = FileUtils.readFileToString(downloadFile);

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
				// 수율 파일 데이터 생성 및 데이터 바인딩
				YieldFileData fileData = new YieldFileData();
				fileData.setLotName(lotIDinDB);
				fileData.setSeq(lotSeq);
				fileData.setFileName(fileName);
				List<YieldFileItemData> fileItemDatas = new ArrayList<YieldFileItemData>();
				fileData.setRawData(fileItemDatas);

				// 측정 아이템 항목명 생성
				String[] itemNameValues = lines[itemNameIndex].split(",");

				for (String itemNameValue : itemNameValues) {

					// 측정 아이템 항목 지정 시
					if (!ArrayUtils.isEmpty(itemFilterNames)) {

						if (Arrays.asList(itemFilterNames).contains(itemNameValue)) {

							YieldFileItemData fileItemData = new YieldFileItemData();
							fileItemData.setItemName(itemNameValue);
							fileItemData.setItemValue(new ArrayList<Double>());
							fileItemDatas.add(fileItemData);
						}

					} else { // 측정 아이템 항목 미 지정 시

						YieldFileItemData fileItemData = new YieldFileItemData();
						fileItemData.setItemName(itemNameValue);
						fileItemData.setItemValue(new ArrayList<Double>());
						fileItemDatas.add(fileItemData);

					}
				}

				logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}]",
						new Object[] { measureIndex, itemNameIndex, itemStartIndex });

				// 측정 아이템 항목 찾은 경우, 순차적으로 데이터를 생성 한다.
				if (measureIndex >= 0 && itemNameIndex >= 0 && itemStartIndex >= 0) {

					for (int i = itemStartIndex; i < lines.length; i++) {

						if (lines[i].isEmpty())
							break;

						String[] itemParams = lines[i].split(",");

						if (!ArrayUtils.isEmpty(itemFilterNames)) {

							for (int j = 0; j < itemNameValues.length; j++) {

								if (Arrays.asList(itemFilterNames).contains(itemNameValues[j])) {

									logger.info("AcceptFilter : Index[{}] ItemName[{}] ItemValue[{}]",
											new Object[] { j, itemNameValues[j], itemParams[j] });

									for (YieldFileItemData fileItem : fileItemDatas) {

										if (fileItem.getItemName().equals(itemNameValues[j])) {

											fileItem.getItemValue().add(NumberUtils.toDouble(itemParams[j]));

										}
									}

								}
							}

						} else {

							for (int j = 0; j < itemNameValues.length; j++) {

								logger.info("NonFilter : Index[{}] ItemName[{}] ItemValue[{}]",
										new Object[] { j, itemNameValues[j], itemParams[j] });

								for (YieldFileItemData fileItem : fileItemDatas) {

									if (fileItem.getItemName().equals(itemNameValues[j])) {

										fileItem.getItemValue().add(NumberUtils.toDouble(itemParams[j]));

									}
								}
							}
						}
					}
				}
				// 파일별 RAWDATA 리스트에 추가
				list.add(fileData);
				// 다운로드 파일 삭제
				downloadFile.delete();
			}
		}
		return list;
	}

	public List getShareTotalAnalysis(String div, String startTime, String endTime, String stepSeq,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String frameName, String lotID, String itemFilter, double min, double max, int stepCount) throws Exception {

		// 측정 아이템 필터 생성, 필터 없으면 null 반환
		List<String> itemFilterList = AnalysisUtils.getItemFilter(itemFilter);

		// 데이터 파일 다운로드
		List<File> files = new ArrayList<File>();

		List<CamelCaseMap> fileListInfo = this.getFileRemoteLocation(div, startTime, endTime, stepSeq, productSpecGroup,
				productSpecName, program, target, chipSpec, frameName, lotID);

		for (CamelCaseMap remoteFileInfo : fileListInfo) {

			String lotName = remoteFileInfo.getString("lotId");
			String lotSeq = remoteFileInfo.getString("seq");
			String fileName = remoteFileInfo.getString("fileName");
			String remoteFileName = remoteFileInfo.getString("remoteFileName");
			// 파일 다운로드
			File downloadFile = this.downloadRemoteFile(remoteFileName);

			files.add(downloadFile);
		}

		logger.info("Download file count[{}]", new Object[] { files.size() });

		// 데이터 파일 아이템별 합산 => 우선 1개 항목 => n개 처리
		YieldFileItemData itemValueData = new YieldFileItemData();
		
		itemValueData.setItemName(itemFilterList.get(0));	// 1개 측정항목만 처리하자.
		itemValueData.setItemValue(new ArrayList<Double>());
		
		logger.info("Merge measure item[{}] value", new Object[] { itemValueData.getItemName() });
		
		for (File file : files) {
			
			logger.info("Merge filename[{}]", new Object[] { file.getName() });

			List<Double> measureValues = AnalysisUtils.getItemValueList(file, itemValueData.getItemName());

			itemValueData.getItemValue().addAll(measureValues);
		}
		
		logger.info("Chip Total Count [{}]", itemValueData.getItemValue().size());

		// 점유율 테이블 생성
		logger.info("Create share table. [{}][{}][{}]", new Object[] { min, max, stepCount });
		List<ChartShareData> shareLists = AnalysisUtils.createShareTable(min, max, stepCount, itemValueData.getItemName());
		
		// 점유 Count 생성
		// calculateChipCount
		logger.info("Calculate Chip Count. [{}][{}][{}]", new Object[] { min, max, stepCount });
		AnalysisUtils.calculateChipCount(shareLists, itemValueData.getItemValue());
		
		// 점유율 산출
		
		// 파일 삭제
		for (File file : files)
			file.delete();

		return shareLists;
	}

}
