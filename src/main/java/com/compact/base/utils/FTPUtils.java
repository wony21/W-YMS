package com.compact.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FTPUtils {

	private static final Logger logger = LoggerFactory.getLogger(FTPUtils.class);

	@Value("${nas.ftp.host}")
	String ftpServer;
	@Value("${nas.ftp.port}")
	String ftpPort;
	@Value("${nas.ftp.user}")
	String ftpUser;
	@Value("${nas.ftp.password}")
	String ftpPassword;
	@Value("${nas.ftp.tmp.path}")
	String ftpTempPath;
	@Value("${nas.ftp.save.path}")
	String ftpSavePath;

	FTPClient ftp;

	public boolean connect() {

		ftp = new FTPClient();
		try {

			FTPClientConfig config = new FTPClientConfig();
			int reply;
			// ftp connect
			logger.info("FTP: Connect server [{}]", this.ftpServer);
			ftp.connect(ftpServer, Integer.valueOf(this.ftpPort));
			// ftp login
			ftp.login(this.ftpUser, this.ftpPassword);
			logger.info("FTP: Login server [{}, {}]", this.ftpUser, this.ftpPassword);

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				logger.error("FTP: FTP server refused connection.");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	public File getFile(String remotePath) throws IOException {

		logger.info("FTP : Request find file[{}]", remotePath);

		FTPFile[] files = ftp.listFiles(remotePath);
		logger.info("FTP: Flie Count [{}]", files.length);
		if (files.length == 0) {
			return null;
		}
		File tmpFile = File.createTempFile("tmp_", ".csv");
		logger.info("TEMP:[{}]", tmpFile.getName());
		FileOutputStream outputStream = new FileOutputStream(tmpFile);
		outputStream.flush();
		ftp.setBufferSize(4096);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		if (ftp.retrieveFile(remotePath, outputStream)) {
			IOUtils.closeQuietly(outputStream);
			logger.info("FTP: Download file [{}] tmp[{}]", remotePath, tmpFile.getAbsolutePath());
			return tmpFile;
		} else {
			IOUtils.closeQuietly(outputStream);
			tmpFile.delete();
			logger.error("FTP: Download error file[{}]", remotePath);
			return null;
		}
	}

	public void disconnect() {
		try {
			if (ftp != null) {
				if (ftp.isConnected()) {
					ftp.logout();
					ftp.disconnect();
				}
			}
		} catch (Exception e) {
		}
	}

	public boolean exist(String path) throws IOException {
		FTPFile[] ftpFiles = ftp.listDirectories(path);
		return ftpFiles.length > 0;
	}

	public void makeDirectory(String path) throws IOException {
		ftp.makeDirectory(path);
	}

	public Boolean upload(String uploadPath, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(uploadPath);
			return ftp.storeFile(file.getName(), fis);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			fis.close();
		}
		return false;
	}

}