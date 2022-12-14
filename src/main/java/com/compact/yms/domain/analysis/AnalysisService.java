package com.compact.yms.domain.analysis;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compact.yms.common.BaseService;
import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.common.CommonData;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.CTQ.CTQService;
import com.compact.yms.domain.analysis.DTO.ChartShareBothData;
import com.compact.yms.domain.analysis.DTO.ChartShareCieData;
import com.compact.yms.domain.analysis.DTO.ChartShareData;
import com.compact.yms.domain.analysis.DTO.CieNameInfo;
import com.compact.yms.domain.analysis.DTO.FileLocation;
import com.compact.yms.domain.analysis.DTO.ItemName;
import com.compact.yms.domain.analysis.DTO.RunHistoryData;
import com.compact.yms.domain.analysis.DTO.SeperatorData;
import com.compact.yms.domain.analysis.DTO.ShareDataObject;
import com.compact.yms.domain.analysis.DTO.ShareReturnData;
import com.compact.yms.domain.analysis.DTO.ShareUserListData;
import com.compact.yms.domain.analysis.DTO.TableShareData;
import com.compact.yms.domain.analysis.DTO.YieldFileData;
import com.compact.yms.domain.analysis.DTO.YieldFileInfoData;
import com.compact.yms.domain.analysis.DTO.YieldFileItemData;
import com.compact.yms.domain.file.dto.FileResponse;
import com.compact.yms.utils.FTPUtils;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import lombok.extern.slf4j.Slf4j;
import math.geom2d.Point2D;

@Slf4j
@Service
public class AnalysisService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

	@Autowired
	AnalysisMapper mapper;

//	# NAS ????????????
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
	@Value("${nas.ftp.save.path}")
	String ftpHistorySavePath;

	@Autowired
	FTPUtils ftpUtils;

	/**
	 * ?????? ????????? ????????? ????????????.
	 * 
	 * @param stepSeq         TT,TP,NM
	 * @param productSpecName ????????????
	 * @param startTime       ?????????
	 * @param endTime         ?????????
	 * @param lotID           LOTID
	 * @return
	 */
	public List getFileRemoteLocation(String div, String stepSeq, String productSpecName, String startTime,
			String endTime, String lotID) {

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

	public List<FileLocation> getRemoteFile(String startDate, String endDate, String factoryName, String div,
			String productionType, String stepSeq, String productSpecGroup, String productSpecName, String program,
			String target, String chipSpec, String frameName, String pl, String lotID, String itemName)
			throws IOException {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startTime", startDate);
		parameter.put("endTime", endDate);
		parameter.put("factoryName", factoryName);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "lotIds", lotID);
		List<FileLocation> list = mapper.getInfo(parameter);

		logger.info("Start file downloading FileCount[{}]", list.size());

		FTPClient ftpClient = ftpUtils.connect();
		for (FileLocation fileInfo : list) {
			String remoteFilePath = fileInfo.getRemoteFileName();
			File file = ftpUtils.downloadFile(ftpClient, remoteFilePath);
			fileInfo.setItemId(itemName);
			fileInfo.setFile(file);
		}
		ftpUtils.disconnect(ftpClient);

		return list;
	}

	/**
	 * NAS??? ????????? ????????? ????????? ???????????? ??????. ????????? ????????????????????? ??????????????? ????????????.
	 * 
	 * @param remoteFilePath ?????????(NAS)??? ??????
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
	 * ???????????? ?????? ????????? ?????? ?????? ???????????? ????????????.
	 * 
	 * @param stepSeq
	 * @param productSpecName
	 * @param startTime
	 * @param endTime
	 * @param lotID
	 * @return
	 * @throws Exception
	 */
	public List getYiedItemNames(String div, String stepSeq, String productSpecName, String startTime, String endTime,
			String lotID) throws Exception {

		List<String> allItemNameValues = new ArrayList<String>();

		List<CamelCaseMap> remoteFiles = this.getFileRemoteLocation(div, stepSeq, productSpecName, startTime, endTime,
				lotID);

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
	 * ???????????? ?????? ???????????? ?????? ????????? ????????????. ??? LOT??? SEQ?????? ???????????? ??????.
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

		// ????????? ????????????
		String[] itemFilterNames = new String[0];
		logger.info("Request itemFilter [{}]", itemFilter);
		if (!itemFilter.isEmpty()) {
			itemFilterNames = itemFilter.split(",");
		}
		// ?????? ?????? ?????????
		List<YieldFileData> list = new ArrayList<YieldFileData>();
		// NAS ???????????? ??????
		List<CamelCaseMap> remoteFiles = this.getFileRemoteLocation(div, startTime, endTime, stepSeq, productSpecGroup,
				productSpecName, program, target, chipSpec, frameName, lotID);

		for (CamelCaseMap remoteFile : remoteFiles) {

			String lotIDinDB = remoteFile.getString("lotId");
			String lotSeq = remoteFile.getString("seq");
			String fileName = remoteFile.getString("fileName");
			String remoteFileName = remoteFile.getString("remoteFileName");
			// ?????? ????????????
			File downloadFile = this.downloadRemoteFile(remoteFileName);
			if (downloadFile != null) {

				String fileAllString = FileUtils.readFileToString(downloadFile);

				String[] lines = fileAllString.split("\r\n");

				// ??? ???????????? ??? ????????? ????????????.
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
				// ?????? ?????? ????????? ?????? ??? ????????? ?????????
				YieldFileData fileData = new YieldFileData();
				fileData.setLotName(lotIDinDB);
				fileData.setSeq(lotSeq);
				fileData.setFileName(fileName);
				List<YieldFileItemData> fileItemDatas = new ArrayList<YieldFileItemData>();
				fileData.setRawData(fileItemDatas);

				// ?????? ????????? ????????? ??????
				String[] itemNameValues = lines[itemNameIndex].split(",");

				for (String itemNameValue : itemNameValues) {

					// ?????? ????????? ?????? ?????? ???
					if (!ArrayUtils.isEmpty(itemFilterNames)) {

						if (Arrays.asList(itemFilterNames).contains(itemNameValue)) {

							YieldFileItemData fileItemData = new YieldFileItemData();
							fileItemData.setItemName(itemNameValue);
							fileItemData.setItemValue(new ArrayList<Double>());
							fileItemDatas.add(fileItemData);
						}

					} else { // ?????? ????????? ?????? ??? ?????? ???

						YieldFileItemData fileItemData = new YieldFileItemData();
						fileItemData.setItemName(itemNameValue);
						fileItemData.setItemValue(new ArrayList<Double>());
						fileItemDatas.add(fileItemData);

					}
				}

				logger.info("measureIndex[{}] itemNameIndex[{}] itemStartIndex[{}]",
						new Object[] { measureIndex, itemNameIndex, itemStartIndex });

				// ?????? ????????? ?????? ?????? ??????, ??????????????? ???????????? ?????? ??????.
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
				// ????????? RAWDATA ???????????? ??????
				list.add(fileData);
				// ???????????? ?????? ??????
				downloadFile.delete();
			}
		}
		return list;
	}
/*
	public List getShareTotalAnalysis(String div, String startTime, String endTime, String stepSeq,
			String productSpecGroup, String productSpecName, String program, String target, String chipSpec,
			String frameName, String lotID, String itemFilter, double min, double max, int stepCount) throws Exception {

		// ?????? ????????? ?????? ??????, ?????? ????????? null ??????
		List<String> itemFilterList = AnalysisUtils.getItemFilter(itemFilter);

		// ????????? ?????? ????????????
		List<File> files = new ArrayList<File>();

		List<CamelCaseMap> fileListInfo = this.getFileRemoteLocation(div, startTime, endTime, stepSeq, productSpecGroup,
				productSpecName, program, target, chipSpec, frameName, lotID);

		// FTP Connect
		if (!ftpUtils.connect()) {
			logger.error("FTP Connect Failed!!!");
			return null;
		}
		for (CamelCaseMap remoteFileInfo : fileListInfo) {

			String lotName = remoteFileInfo.getString("lotId");
			String lotSeq = remoteFileInfo.getString("seq");
			String fileName = remoteFileInfo.getString("fileName");
			String remoteFileName = remoteFileInfo.getString("remoteFileName");
			// ?????? ????????????
			File downloadFile = ftpUtils.getFile(remoteFileName);

			if (downloadFile != null) {
				files.add(downloadFile);
			}
		}
		// FTP Disconnect
		ftpUtils.disconnect();

		logger.info("Download file count[{}]", new Object[] { files.size() });

		// ????????? ?????? ???????????? ?????? => ?????? 1??? ?????? => n??? ??????
		YieldFileItemData itemValueData = new YieldFileItemData();

		itemValueData.setItemName(itemFilterList.get(0)); // ????????? ???????????? ???????????? ??????
		itemValueData.setItemValue(new ArrayList<Double>());

		logger.info("Merge measure item[{}] value", new Object[] { itemValueData.getItemName() });

		for (File file : files) {

			logger.info("Merge filename[{}]", new Object[] { file.getName() });

			List<Double> measureValues = AnalysisUtils.getItemValueList(file, itemValueData.getItemName());

			itemValueData.getItemValue().addAll(measureValues);
		}

		logger.info("Chip Total Count [{}]", itemValueData.getItemValue().size());

		// ????????? ????????? ??????
		logger.info("Create share table. [{}][{}][{}]", new Object[] { min, max, stepCount });
		List<ChartShareData> shareLists = AnalysisUtils.createShareTable(min, max, stepCount,
				itemValueData.getItemName());

		// ?????? Count ??????
		logger.info("Calculate Chip Count. [{}][{}][{}]", new Object[] { min, max, stepCount });
		AnalysisUtils.calculateChipCount(shareLists, itemValueData.getItemValue());

		// ?????? ??????
		for (File file : files)
			file.delete();

		return shareLists;
	}
*/
	public List<SeperatorData> getSeperatorCie(String factoryName, String productSpecName, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("productSpecName", productSpecName);
		parameter.put("program", program);
		return mapper.getSeperatorCIE(parameter);
	}

	// getCieXYItemName
	public CieNameInfo getCieXYItemName(List<String> itemNames) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("cieXArr", itemNames);
		parameter.put("cieYArr", itemNames);
		List<CieNameInfo> info = mapper.getCieXYItemName(parameter);
		if (info != null) {
			if (info.size() > 0) {
				return info.get(0);
			}
		}
		return null;
	}

	/*****************************************************************************
	 * ?????? ??????
	 *****************************************************************************/
	public List<ItemName> getMeasureItemNames(String startDate, String endDate, String factoryName, String div,
			String stepSeq, String productionType, String productSpecGroup, String productSpecName, String program,
			String target, String chipSpec, String frameName, String pl, String lotId, String itemName) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", factoryName);
		setArrayParam(parameter, "divs", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lotId);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "pls", pl);
		return mapper.getMeasureItemNames(parameter);
	}

	public ShareReturnData calculatorShareData(String startDate, String endDate, String factoryName, String div,
			String stepSeq, String productionType, String productSpecGroup, String productSpecName, String program,
			String target, String chipSpec, String frameName, String pl, String lotId, String itemName,
			String rangeMinMaxTable) throws IOException {

		List<FileLocation> fileInfos = this.getRemoteFile(startDate, endDate, factoryName, div, productionType, stepSeq,
				productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotId, itemName);

		int downloadFileSize = fileInfos.size();

		if (downloadFileSize == 0) {
			logger.error("download file count [{}]", downloadFileSize);
		}

		// ????????? ????????? ??????
		List<ChartShareData> shareLists = AnalysisUtils.parseMinMaxTable(rangeMinMaxTable, itemName);
		logger.info("Create Share Limited Min~Max table data...OK");

		List<TableShareData> tableLists = new ArrayList<>();
		YieldFileItemData itemValueData = new YieldFileItemData();
		itemValueData.setItemName(itemName); // ????????? ???????????? ???????????? ??????
		itemValueData.setItemValue(new ArrayList<Double>());
		for (FileLocation location : fileInfos) {
			logger.info("Get Measuredata and ChipOfLot LOT[{}]", location.getLotId());
			File file = location.getFile();
			List<Double> measureValues = AnalysisUtils.getItemValueList(file, itemValueData.getItemName());
			// LOT (?????????) ????????? ??????
			List<TableShareData> tableData = AnalysisUtils.occRangeChipCount(shareLists, location, measureValues);
			tableLists.addAll(tableData);
			itemValueData.getItemValue().addAll(measureValues);
		}
		logger.info("Chip Total Count [{}]", itemValueData.getItemValue().size());

		// ?????? ?????? Count ??????
		AnalysisUtils.occRangeChipCountTotal(shareLists);
		logger.info("Calculator ChipCount...OK");

		// ?????? ??????
		for (FileLocation location : fileInfos) {
			File file = location.getFile();
			file.delete();
		}

		ShareReturnData returnObj = new ShareReturnData();
		returnObj.setChart(shareLists);
		returnObj.setTable(tableLists);

		return returnObj;
	}

	public ShareReturnData calculatorShareCieData(String startDate, String endDate, String factoryName, String div,
			String stepSeq, String productionType, String productSpecGroup, String productSpecName, String program,
			String target, String chipSpec, String frameName, String pl, String lotId, String itemName, String rangeCie)
			throws IOException {

		ShareReturnData returnObj = new ShareReturnData();
		returnObj.setError(false);

		List<FileLocation> fileInfos = this.getRemoteFile(startDate, endDate, factoryName, div, productionType, stepSeq,
				productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotId, itemName);

		int downloadFileSize = fileInfos.size();

		if (downloadFileSize == 0) {
			logger.error("download file count [{}]", downloadFileSize);
		}

		// ????????? ????????? ??????
		List<ChartShareCieData> shareLists = AnalysisUtils.parseCieTable(rangeCie);
		logger.info("Create Share Limited Min~Max table data...OK");

		List<TableShareData> tableLists = new ArrayList<>();
		YieldFileItemData itemValueData = new YieldFileItemData();
		itemValueData.setItemName(itemName); // ????????? ???????????? ???????????? ??????
		itemValueData.setItemPointValue(new ArrayList<>());
		for (FileLocation location : fileInfos) {
			logger.info("Get Measuredata and ChipOfLot LOT[{}]", location.getLotId());
			File file = location.getFile();

			List<String> itemNamesInFile = AnalysisUtils.getItemNames(file);
			CieNameInfo nameInfo = this.getCieXYItemName(itemNamesInFile);
			if (nameInfo == null) {
				logger.error("Cannot found CIE-X, CIE-Y name. Defition=> POP_ENUMDEFVALUE(ENUMNAME='CIE-XY-NAME')");
				logger.error("Itemname:[{}]", String.join(",", itemNamesInFile));
				returnObj.setError(true);
				returnObj.setErrorMessage(String.format("CIEX, CIEY??? ???????????? ????????????????????? ??????????????????. (LOT:%s) (FILE:%s)",
						location.getLotId(), location.getFileName()));
				return returnObj;
			}
			String cieX = nameInfo.getCieX();
			String cieY = nameInfo.getCieY();
			logger.info("Get measuredata for cie-x, cie-y");
			List<Point> measureValues = AnalysisUtils.getItemPointValueList(file, cieX, cieY);
			logger.info("Get measuredata chip count.");
			List<TableShareData> tableData = AnalysisUtils.occCieChipCount(shareLists, location, measureValues);
			tableLists.addAll(tableData);
			itemValueData.getItemPointValue().addAll(measureValues);
		}
		logger.info("FILECOUNT[{}] CHIPCOUNT[{}]",
				new Object[] { fileInfos.size(), itemValueData.getItemPointValue().size() });

		// ?????? ????????? ??????
		AnalysisUtils.occCieChipCountTotal(shareLists);
		logger.info("Calculator ChipCount...OK");

		// ?????? ??????
		for (FileLocation location : fileInfos) {
			File file = location.getFile();
			file.delete();
		}

		returnObj.setChart(shareLists);
		returnObj.setTable(tableLists);

		return returnObj;
	}

	public ShareReturnData calculatorShareBothData(String startDate, String endDate, String factoryName, String div,
			String stepSeq, String productionType, String productSpecGroup, String productSpecName, String program,
			String target, String chipSpec, String frameName, String pl, String lotId, String itemName,
			String rangeMinMax, String rangeCie) throws IOException {

		ShareReturnData returnObj = new ShareReturnData();
		returnObj.setError(false);

		List<FileLocation> fileInfos = this.getRemoteFile(startDate, endDate, factoryName, div, productionType, stepSeq,
				productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotId, itemName);

		int downloadFileSize = fileInfos.size();

		if (downloadFileSize == 0) {
			logger.error("download file count [{}]", downloadFileSize);
		}

		// ????????? ????????? ??????
		List<ChartShareBothData> shareLists = AnalysisUtils.parseBothTable(rangeCie, rangeMinMax, itemName);
		logger.info("Create min-max, cie range limit table...OK");

//		shareLists.get(0).getRange().get(0).setChipCount(10000L);
//		for(ChartShareBothData obj1 : shareLists) {
//			List<ChartShareData> list = obj1.getRange();
//			for(ChartShareData obj2 : list) {
//				logger.info("CIE[{}] GROUP[{}] RANGE[{}] CHIP[{}]", new Object[] {
//						obj1.getCieName(), obj2.getGroupId(), obj2.getRangeDescription(), obj2.getChipCount()
//				});
//			}
//		}

		List<TableShareData> tableLists = new ArrayList<>();
		YieldFileItemData itemValueData = new YieldFileItemData();
		itemValueData.setItemName(itemName); // ????????? ???????????? ???????????? ??????
		itemValueData.setItemBothValue(new ArrayList<>());
		for (FileLocation location : fileInfos) {
			logger.info("Get Measuredata and ChipOfLot LOT[{}]", location.getLotId());
			File file = location.getFile();

			List<String> itemNamesInFile = AnalysisUtils.getItemNames(file);
			CieNameInfo nameInfo = this.getCieXYItemName(itemNamesInFile);
			if (nameInfo == null) {
				logger.error("Cannot found CIE-X, CIE-Y name. Defition=> POP_ENUMDEFVALUE(ENUMNAME='CIE-XY-NAME')");
				logger.error("Itemname:[{}]", String.join(",", itemNamesInFile));
				returnObj.setError(true);
				returnObj.setErrorMessage(String.format("CIEX, CIEY??? ???????????? ????????????????????? ??????????????????. (LOT:%s) (FILE:%s)",
						location.getLotId(), location.getFileName()));
				return returnObj;
			}
			String cieX = nameInfo.getCieX();
			String cieY = nameInfo.getCieY();
			logger.info("Get measuredata for cie-x, cie-y");
			List<ShareDataObject> measureValues = AnalysisUtils.getItemBothValueList(file, itemName, cieX, cieY);
			List<TableShareData> tableData = AnalysisUtils.occBothChipCount(shareLists, location, measureValues);
			tableLists.addAll(tableData);
			itemValueData.getItemBothValue().addAll(measureValues);
		}
		logger.info("FILECOUNT[{}] CHIPCOUNT[{}]",
				new Object[] { fileInfos.size(), itemValueData.getItemBothValue().size() });

		// ?????? ????????? ??????
		AnalysisUtils.occBothChipCountTotal(shareLists);
		logger.info("Calculator ChipCount...OK");

		// ?????? ??????
		for (FileLocation location : fileInfos) {
			File file = location.getFile();
			file.delete();
		}

		returnObj.setChart(shareLists);
		returnObj.setTable(tableLists);

		return returnObj;
	}

	public void saveHistory(String startDate, String endDate, String factoryName, String div, String stepSeq,
			String productionType, String productSpecGroup, String productSpecName, String program, String target,
			String chipSpec, String frameName, String subFrameName, String intensity, String pl, String lotId,
			String userId, File file) throws IOException {
		String savePath = String.format("%s/%s", ftpHistorySavePath, userId);
		logger.info("Save history file {}", savePath);
		FTPClient ftpClient = ftpUtils.connect();
		if (!ftpUtils.exist(ftpClient, savePath)) {
			ftpUtils.makeDirectory(ftpClient, savePath);
		}
		if (ftpUtils.upload(ftpClient, savePath, file)) {
			ftpUtils.disconnect(ftpClient);
			Long fileSize = file.length();
			String orgFileName = file.getName();
			String extention = FileNameUtils.getExtension(orgFileName);
			String key = orgFileName.replace("." + extention, "");
			String saveType = key.split("_")[0];
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("key", key);
			parameter.put("saveType", saveType);
			parameter.put("startDate", startDate);
			parameter.put("endDate", endDate);
			parameter.put("stepSeq", stepSeq);
			parameter.put("productionType", productionType);
			parameter.put("factoryName", factoryName);
			parameter.put("div", div);
			parameter.put("productSpecGroup", productSpecGroup);
			parameter.put("productSpecName", productSpecName);
			parameter.put("lotId", lotId);
			parameter.put("program", program);
			parameter.put("target", target);
			parameter.put("chipSpec", chipSpec);
			parameter.put("frameName", frameName);
			parameter.put("intensity", intensity);
			parameter.put("pl", pl);
			parameter.put("subFrameName", subFrameName);
			parameter.put("userId", userId);
			parameter.put("bytes", fileSize.intValue());
			parameter.put("filePath", savePath + "/" + orgFileName);
			mapper.addHistory(parameter);
		}
	}

	public List<RunHistoryData> getHistory(String startDate, String endDate, String userId, String type, String optSaveDate,
			String cond1, String cond2, String cond3, String cond4, String cond5, String cond6, String cond7,
			String cond8) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("optSaveDate", optSaveDate);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("userId", userId.toLowerCase());
		parameter.put("type", type);
		setCustomCond(parameter, "cond1", cond1);
		setCustomCond(parameter, "cond2", cond2);
		setCustomCond(parameter, "cond3", cond3);
		setCustomCond(parameter, "cond4", cond4);
		setCustomCond(parameter, "cond5", cond5);
		setCustomCond(parameter, "cond6", cond6);
		setCustomCond(parameter, "cond7", cond7);
		setCustomCond(parameter, "cond8", cond8);
		return mapper.getHistory(parameter);
	}

	public List<ShareUserListData> getShareUsers(String filter) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("filter", filter);
		return mapper.getAllUserListForShare(parameter);
	}

	public List<ShareUserListData> getShareUserInKey(String key) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("key", key);
		return mapper.getShareUserLists(parameter);
	}

	public ApiResponse shareHistory(String key, String userIds) {
		try {
			String[] userIdArray = userIds.split(";");
			for (String userId : userIdArray) {
				Map<String, Object> shareParam = new HashMap<String, Object>();
				shareParam.put("key", key);
				shareParam.put("userId", userId);
				mapper.shareHistory(shareParam);
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("key", key);
			parameters.put("shared", "Y");
			mapper.setShareHistoryFlag(parameters);
			return ApiResponse.success("OK");
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}

	public ApiResponse removeShare(String key, String userIds) {
		try {
			String[] userIdArray = userIds.split(";");
			for (String userId : userIdArray) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				parameter.put("key", key);
				parameter.put("userId", userId);
				mapper.deleteHistoryShare(parameter);
			}
			Map<String, Object> getParam = new HashMap<String, Object>();
			getParam.put("key", key);
			List<ShareUserListData> list = mapper.getShareUserLists(getParam);
			int sharedCount = list.size();
			if (sharedCount == 0) {
				Map<String, Object> sharedParam = new HashMap<String, Object>();
				sharedParam.put("key", key);
				sharedParam.put("shared", "N");
				mapper.setShareHistoryFlag(sharedParam);
			}
			return ApiResponse.success("OK");
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}

	public ApiResponse addHistoryMemo(String key, String memo) {
		try {
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("key", key);
			parameter.put("comments", memo);
			mapper.setHistoryMemo(parameter);
			return ApiResponse.success("OK");
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}

	@Transactional
	public ApiResponse deleteHistory(String keys, String userId) {
		try {

			List<String> cannotKey = new ArrayList<>();
			String[] keyArray = keys.split(";");

			if (keyArray.length == 1) {
				String key = keyArray[0];
				Map<String, Object> getParam = new HashMap<String, Object>();
				getParam.put("key", key);
				List<RunHistoryData> list = mapper.getHistoryOnKey(getParam);
				int listSize = list.size();
				if (listSize == 0) {
					return ApiResponse.error("?????? ????????? ?????? ??? ????????????.");
				}
				RunHistoryData deleteObj = list.get(0);
				String createUserId = deleteObj.getUserId();
				if (createUserId.equals(userId) || userId.equals(CommonData.SuperUser.Id)) {
					Map<String, Object> parameter = new HashMap<String, Object>();
					parameter.put("key", key);
					parameter.put("delYn", "Y");
					mapper.deleteHistory(parameter);
					return ApiResponse.success("????????? ?????? ???????????????.");
				} else {
					return ApiResponse.error("?????? ????????? ????????????.");
				}
			} else {
				String message = "????????? ?????? ???????????????.";
				for (String key : keyArray) {
					Map<String, Object> getParam = new HashMap<String, Object>();
					getParam.put("key", key);
					getParam.put("userId", userId);
					List<RunHistoryData> list = mapper.getHistoryOnKey(getParam);
					if (list.size() > 0) {
						if (list.get(0).getUserId().equals(userId)) {
							Map<String, Object> parameter = new HashMap<String, Object>();
							parameter.put("key", key);
							parameter.put("delYn", "Y");
							mapper.deleteHistory(parameter);
						} else {
							cannotKey.add(key);
						}
					}
				}

				if (cannotKey.size() > 0) {
					message = String.format("????????? ?????? ????????????, ????????????????????? ????????? ???????????????.\r\n(??????KEY:%s)",
							String.join(",", keyArray));
				}
				return ApiResponse.success(message);
			}
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}

	public FileResponse downloadHistoryFile(String key) {
		FileResponse response = new FileResponse();
		response.setIsError(false);
		response.setErrorMsg("");
		// DB?????? FILE?????? ????????????
		Map<String, Object> getParam = new HashMap<String, Object>();
		getParam.put("key", key);
		List<RunHistoryData> list = mapper.getHistoryOnKey(getParam);
		if (list.size() <= 0) {
			response.setIsError(true);
			response.setErrorMsg("?????? ???????????? ???????????? ????????????.");
			return response;
		}
		// ?????? ????????????
		String filePath = list.get(0).getFilePath();
		try {
			FTPClient ftpClient = ftpUtils.connect();
			File file = ftpUtils.downloadFile(ftpClient, filePath);
			if (file != null) {
				response.setOrgFileName(file.getName());
				response.setFile(file);
			}
			ftpUtils.disconnect(ftpClient);
		} catch (Exception e) {
			response.setIsError(true);
			response.setErrorMsg(e.getMessage());
			return response;
		}
		return response;
	}
}
