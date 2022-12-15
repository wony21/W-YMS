package com.compact.yms.utils;

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

	public FTPClient connect() {

		FTPClient ftp = new FTPClient();
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
				return ftp;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ftp;
		}

		return ftp;
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
	
	public void disconnect(FTPClient ftpClient) {
		try {
			if (ftpClient != null) {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			}
		} catch (Exception e) {
		}
	}
	
	
	public File downloadFile(FTPClient ftpClient, String remotePath) throws IOException {
		
		try {
			logger.info("FTP : Request find file[{}]", remotePath);
			FTPFile[] files = ftpClient.listFiles(remotePath);
			logger.info("FTP: Flie Count [{}]", files.length);
			if (files.length == 0) {
				return null;
			}
			File tmpFile = File.createTempFile("tmp_", ".csv");
			logger.info("TEMP:[{}]", tmpFile.getName());
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			outputStream.flush();
			ftpClient.setBufferSize(4096);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			if (ftpClient.retrieveFile(remotePath, outputStream)) {
				IOUtils.closeQuietly(outputStream);
				logger.info("FTP: Download file [{}] tmp[{}]", remotePath, tmpFile.getAbsolutePath());
				return tmpFile;
			} else {
				IOUtils.closeQuietly(outputStream);
				tmpFile.delete();
				logger.error("FTP: Download error file[{}]", remotePath);
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			
		}
		return null;
		
	}

	public boolean exist(FTPClient ftpClient, String path) throws IOException {
		FTPFile[] ftpFiles = ftpClient.listDirectories(path);
		return ftpFiles.length > 0;
	}

	public void makeDirectory(FTPClient ftpClient, String path) throws IOException {
		ftpClient.makeDirectory(path);
	}

	public Boolean upload(FTPClient ftpClient, String uploadPath, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(uploadPath);
			return ftpClient.storeFile(file.getName(), fis);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			fis.close();
		}
		return false;
	}

}
