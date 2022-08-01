package com.compact.base.domain.common;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;

import lombok.Getter;
import lombok.Setter;

@Service
public class SFTPService {

	private static final Logger logger = LoggerFactory.getLogger(SFTPService.class);

	public String host = "192.168.32.52";
	public int port = 22;
	public String ftp_user = "sscyms";
	public String ftp_password = "yms2015";
	
	public String dataPath = "/work/_dat/_bck";
	public String downloadPath = "c:\\tmp";
	

	@Getter
	@Setter
	public String targetFileName;

	public File Connect() {

		String outText = "";
		JSch jSch = new JSch();
		Session session = null;
		File downloadFile = null;
		try {

			// Session
			session = jSch.getSession(ftp_user, host, port);
			session.setPassword(ftp_password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			logger.info("session conncect ok.");
			// Sftp Channel
			Channel channel = session.openChannel("sftp");
			channel.connect();
			if (!channel.isConnected()) {
				logger.error("channel is not connected.");
				return downloadFile;
			}
			ChannelSftp sftp = (ChannelSftp) channel;
			logger.info("sftp channel opened.");
			// Get data directory list.
			List<LsEntry> dirEntries = sftp.ls(dataPath);
			for (LsEntry ent : dirEntries) {
				if (!ent.getAttrs().isDir())
					continue;
				String directoryName = ent.getFilename();
				if (directoryName.equals(".") || directoryName.equals("..")) {
					continue;
				}

				Boolean exist = FileExist(sftp, dataPath + "/" + directoryName, targetFileName);
				if (exist) {
					System.out.println(String.format("file[%s] directory[%s] data file found!!", targetFileName, directoryName));
					downloadFile = File.createTempFile("tmp_", ".dat", new File(downloadPath));
					if (downloadFile.exists()) {
						downloadFile.delete();
					}
					sftp.get(dataPath + "/" + directoryName + "/" + targetFileName, downloadFile.getAbsolutePath());
					//outText = FileUtils.readFileToString(downloadFile, "utf-8");
					break;
				} else {
					logger.info("directory[%s] pass..", directoryName);
				}
			}

			sftp.disconnect();
			logger.info("sftp disconnect.");

		} catch (Exception e) {

			logger.error(e.getMessage());
		}
		
		return downloadFile;
	}
	
	private Boolean FileExist(final ChannelSftp sftp, final String path, final String findFile) throws SftpException {
		final Vector<LsEntry> vector = new Vector<LsEntry>();
		LsEntrySelector selector = new LsEntrySelector() {

			@Override
			public int select(LsEntry entry) {
				final String filename = entry.getFilename();
				if (filename.equals(".") || filename.equals("..")) {
					return CONTINUE;
				}
				if (filename.equals(findFile)) {
					vector.addElement(entry);
					return BREAK;
				}
				return 0;
			}
		};
		sftp.ls(path, selector);
		return vector.size() > 0;
	}

}
