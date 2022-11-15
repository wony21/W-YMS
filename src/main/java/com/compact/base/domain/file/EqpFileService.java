package com.compact.base.domain.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPFileFilters;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.compact.base.common.BaseService;
import com.compact.base.domain.file.dto.FileResponse;
import com.compact.base.domain.file.dto.YMS_FILEINDEX_10Y;
import com.hierynomus.protocol.commons.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EqpFileService extends BaseService {

	@Autowired
	EqpFileMapper mapper;

	@Value("${nas.ftp.host}")
	String ftpServer;
	@Value("${nas.ftp.port}")
	String ftpPort;
	@Value("${nas.ftp.user}")
	String ftpUser;
	@Value("${nas.ftp.password}")
	String ftpPassword;
	@Value("${nas.ftp.tmp.path")
	String ftpTempPath;

	public List getFileList(String startDate, String endDate, String site, String stepSeq, String div,
			String productGroup, String productSpecName, String machineName, String program, String lotID,
			String target, String chipSpec, String frameName, String pl, String lotFilter) {
		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("site", site);
		param.put("stepSeq", stepSeq);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		String[] stepSeqs = stepSeq.split(";");
		param.put("stepSeqs", stepSeq.isEmpty() ? null : stepSeqs);
		setArrayParam(param, "divs", div);
		setArrayParam(param, "productGroups", productGroup);
		setArrayParam(param, "productSpecNames", productSpecName);
		setArrayParam(param, "machineNames", machineName);
		setArrayParam(param, "programs", program);
		setArrayParam(param, "lotIds", lotID);
		setArrayParam(param, "targets", target);
		setArrayParam(param, "chipSpecs", chipSpec);
		setArrayParam(param, "frameNames", frameName);
		setArrayParam(param, "pls", pl);
		// Text Filter
		param.put("lotLike", lotFilter);

		return mapper.getFileList(param);
	}

	public FileResponse getFile(String fmKey) {

		FileResponse res = new FileResponse();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fmKey", fmKey);

		List<YMS_FILEINDEX_10Y> list = mapper.getFile10Y(param);

		for (YMS_FILEINDEX_10Y yms_FILEINDEX_10Y : list) {
			String filename = yms_FILEINDEX_10Y.getFilename();
			log.info("Filename : {}", filename);
		}

		if (list == null) {
			log.warn("File not found in [YMS_FILEINDEX_10Y] fmkey[{}]", fmKey);
			res.setIsError(true);
			res.setErrorMsg("데이터를 찾을 수 없습니다.(DB인덱스에 존재하지 않음");
			return res;
		}

		if (list.size() == 0) {
			log.error("Cannot found file data [YMS_FILEINDEX_10Y] fmkey[{}]", fmKey);
			res.setIsError(true);
			res.setErrorMsg("데이터를 찾을 수 없습니다.(DB)");
			return res;
		}

		FTPClient ftp = new FTPClient();
		try {

			FTPClientConfig config = new FTPClientConfig();
			int reply;
			// ftp connect
			ftp.connect(ftpServer, Integer.valueOf(this.ftpPort));
			log.info("FTP: Connect server [{}]", this.ftpServer);
			// ftp login
			ftp.login(this.ftpUser, this.ftpPassword);
			log.info("FTP: Login server [{}, {}]", this.ftpUser, this.ftpPassword);

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.error("FTP: FTP server refused connection.");
				return res;
			}

			String path = list.get(0).getPath();
			String fileName = list.get(0).getFilename();
			String remotePath = list.get(0).getRemoteFileName();

			log.info("FTP : Request file [{}]", remotePath);
			
			FTPFile[] files = ftp.listFiles(remotePath);

			log.info("FTP: Flie Count [{}]", files.length);

			if (files.length == 0) {
				res.setIsError(true);
				res.setErrorMsg("NAS 내 파일이 존재하지 않습니다");
				return res;
			}

			File tmpFile = File.createTempFile("tmp_", ".csv");
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			outputStream.flush();
			ftp.setBufferSize(4096);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (ftp.retrieveFile(remotePath, outputStream)) {
				log.info("FTP: Download file [{}] tmp[{}]", fileName, tmpFile.getAbsolutePath());
			} else {
				if (outputStream != null) {
					outputStream.close();
				}
				tmpFile.delete();
				log.error("FTP: Download error file[{}]", fileName);
				res.setIsError(true);
				res.setErrorMsg("FTP: 다운로드 실패(retriveFile)");
				return res;
			}
			IOUtils.closeQuietly(outputStream);

			ftp.logout();
			res.setOrgFileName(fileName);
			res.setFile(tmpFile);
			res.setIsError(false);

			return res;
		} catch (Exception e) {
			res.setIsError(true);
			res.setErrorMsg(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {

				}
			}
		}
		return res;
	}

	public FileResponse mergeFile(String[] fmKeys) {

		List<File> downloadFiles = new ArrayList<File>();

		FileResponse res = new FileResponse();

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("fmKeys", fmKeys);

		List<YMS_FILEINDEX_10Y> list = mapper.getFile10Y(param);

		for (YMS_FILEINDEX_10Y yms_FILEINDEX_10Y : list) {
			String filename = yms_FILEINDEX_10Y.getFilename();
			log.info("Filename : {}", filename);
		}

		if (list == null) {
			log.warn("File not found in [YMS_FILEINDEX_10Y] fmkey[{}]", StringUtils.join(fmKeys, ','));
			res.setIsError(true);
			res.setErrorMsg("데이터를 찾을 수 없습니다.(DB인덱스에 존재하지 않음");
			return res;
		}

		if (list.size() == 0) {
			log.error("Cannot found file data [YMS_FILEINDEX_10Y] fmkey[{}]", StringUtils.join(fmKeys, ','));
			res.setIsError(true);
			res.setErrorMsg("데이터를 찾을 수 없습니다.(DB)");
			return res;
		}

		FTPClient ftp = new FTPClient();

		try {

			FTPClientConfig config = new FTPClientConfig();
			int reply;
			// ftp connect
			ftp.connect(ftpServer, Integer.valueOf(this.ftpPort));
			log.info("FTP: Connect server [{}]", this.ftpServer);
			// ftp login
			ftp.login(this.ftpUser, this.ftpPassword);
			log.info("FTP: Login server [{}, {}]", this.ftpUser, this.ftpPassword);

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.error("FTP: FTP server refused connection.");
				return res;
			}

			/*******************************************************************************
			 * Download files
			 **********************/
			for (YMS_FILEINDEX_10Y fileIndex : list) {

				String path = fileIndex.getPath();
				String fileName = fileIndex.getFilename();
				String remotePath = fileIndex.getRemoteFileName();

				FTPFile[] files = ftp.listFiles(remotePath);

				log.info("FTP: Flie Count [{}]", files.length);

				if (files.length == 0) {
					res.setIsError(true);
					res.setErrorMsg("NAS 내 파일이 존재하지 않습니다");
					return res;
				}

				File tmpFile = File.createTempFile("tmp_", ".csv");
				FileOutputStream outputStream = new FileOutputStream(tmpFile);
				outputStream.flush();
				ftp.setBufferSize(4096);
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				if (ftp.retrieveFile(remotePath, outputStream)) {
					log.info("FTP: Download file [{}] tmp[{}]", fileName, tmpFile.getAbsolutePath());
					downloadFiles.add(tmpFile);
				} else {
					if (outputStream != null) {
						outputStream.close();
					}
					tmpFile.delete();
					log.error("FTP: Download error file[{}]", fileName);
					res.setIsError(true);
					res.setErrorMsg("FTP: 다운로드 실패(retriveFile)");
					return res;
				}
				IOUtils.closeQuietly(outputStream);
			}

			ftp.logout();

			/*******************************************************************************
			 * Merge files
			 **********************/

			List<String> dataColumnNames = new ArrayList<>();

			List<Map<String, String>> dataRows = new ArrayList<>();

			for (int i = 0; i < downloadFiles.size(); i++) {

				File downloadFile = downloadFiles.get(i);

				List<String> fileContents = FileUtils.readLines(downloadFile.getAbsoluteFile());

				Boolean columnAreaFg = false;

				Boolean dataAreaFg = false;

				for (String contents : fileContents) {

					if (columnAreaFg) {

						if (contents.toLowerCase().startsWith("[summary data]") || contents.length() == 0) {
							break;
						}
					}

					if (contents.toLowerCase().startsWith("no.")) {

						columnAreaFg = true;

						if (dataColumnNames.size() == 0) {

							String[] columns = contents.split(",");
							for (String column : columns) {
								dataColumnNames.add(column);
							}
						}

						continue;
					}

					if (0 == i) {

						if (columnAreaFg && contents.length() > 0) {

							Map<String, String> dataRow = new HashMap<>();
							String[] datas = contents.split(",");
							for (int j = 0; j < datas.length; j++) {
								dataRow.put(dataColumnNames.get(j), datas[j]);
							}
							dataRows.add(dataRow);
						}

					} else {

						if (contents.toLowerCase().startsWith("1,")) {
							dataAreaFg = true;
						}

						if (columnAreaFg && dataAreaFg && contents.length() > 0) {

							Map<String, String> dataRow = new HashMap<>();
							String[] datas = contents.split(",");
							for (int j = 0; j < datas.length; j++) {
								dataRow.put(dataColumnNames.get(j), datas[j]);
							}
							dataRows.add(dataRow);
						}
					}
				}
			}

			String dateFmt = DateFormatUtils.format(Calendar.getInstance(), "yyMMddHHmmss");

			String firstFilename = list.get(0).getLotId();

			String extension = FilenameUtils.getExtension(firstFilename);

			String mergetFileName = FilenameUtils.removeExtension(firstFilename) + "_merge_" + dateFmt + ".csv";

			File mergeFile = File.createTempFile("merge_", ".csv");

			if (!mergeFile.exists()) {
				mergeFile.createNewFile();
			} else {
				// error 중복된 파일 존재 (다시 시도)
			}

			List<String> mergeContents = new ArrayList<>();
			// FileUtils.writeStringToFile(mergeFile, String.join(",", dataColumnNames),
			// true);
			mergeContents.add(String.join(",", dataColumnNames));
			log.info("DataRow count[{}]", dataRows.size());
			for (Map<String, String> dataRow : dataRows) {

				String content = "";
				for (String columnName : dataColumnNames) {
					
					if (!dataRow.containsKey(columnName)) {
						log.error(String.format("Cannot found key[%s]", columnName));
						continue;
					}
					
					String rowValue = dataRow.get(columnName);
					if (rowValue.contains(",")) {
						rowValue = "\"" + rowValue + "\"";
					}
					content += rowValue + ",";
				}

				if (content.length() > 0 && content.endsWith(",")) {
					content = content.substring(0, content.length() - 1);
				}
				// FileUtils.writeStringToFile(mergeFile, content, true);
				mergeContents.add(content);
			}

			FileUtils.writeLines(mergeFile, mergeContents);

			res.setIsError(false);
			res.setFile(mergeFile);
			res.setOrgFileName(mergetFileName);

			return res;

		} catch (Exception e) {
			log.error(e.getMessage());
			res.setIsError(true);
			res.setErrorMsg(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {

				}
			}
		}

		return res;
	}

}
