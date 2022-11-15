package wyms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.poi.util.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.Directory;
import com.hierynomus.smbj.share.DiskShare;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkDictoryTest {

	String ftpServer = "192.168.205.208";

	String ftpPort = "21";

	String ftpUser = "ymsadmin";

	String ftpPassword = "WkaQhdtnsenqn01!";

	public static double calculateSD(double numArray[]) {

		double sum = 0.0, standardDeviation = 0.0;
		int length = numArray.length;

		for (double num : numArray) {
			sum += num;
		}

		double mean = sum / length;

		for (double num : numArray) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation / length);

	}

	// @Test
	public void ArrayTest() {

		double min = 0.01;
		double max = 0.2;
		int stepCount = 5;
		double step = (max - min) / stepCount;

		System.out.println("Step : " + step);

		double std = calculateSD(new double[] { min, max });

		System.out.println("std : " + std);

		int seq = 1;
		System.out.println(String.format("G%d : (none) - %f", seq++, min));
		double preValue = min;
		for (double i = min + step; i <= max; i += step) {
			System.out.println(String.format("G%d : %f - %f", seq++, preValue, i));
			preValue = i;
		}
		System.out.println(String.format("G%d : %f - (none)", seq++, max));

	}

	// @Test
	public void NetworkConnection() throws Exception {

		// \\\\192.168.32.61\\data_move\\TT\\INB\\T-ST0253\\2022\\01\\12\\211224-0002-001-005-21_202201120026.csv
		System.out.println("Network Test Module.");

		String user = "Administrator";
		String pass = "WkaQhdtnsenqn01!";
		String server = "192.168.32.61";
		String shareFolder = "data_move";
		String remoteFilePath = "\\TT\\INB\\T-ST0253\\2022\\01\\12\\211224-0002-001-005-21_202201120026.csv";
		String workPath = "D:\\ProgramTestPaths\\WORK" + "\\" + "211224-0002-001-005-21_202201120026.csv";

		SMBClient client = new SMBClient();
		Connection connection = client.connect(server);
		AuthenticationContext ac = new AuthenticationContext(user, pass.toCharArray(), null);
		Session session = connection.authenticate(ac);
		DiskShare share = (DiskShare) session.connectShare("data_move");
		com.hierynomus.smbj.share.File file = share.openFile(
				"TT\\INB\\T-ST0253\\2022\\01\\12\\211224-0002-001-005-21_202201120026.csv",
				EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);

		InputStream fin = new BufferedInputStream(file.getInputStream());
		byte[] fileData = IOUtils.toByteArray(fin);

		java.io.File workFile = new File(workPath);

		if (!workFile.exists()) {
			workFile.createNewFile();
		}

		FileUtils.writeByteArrayToFile(workFile, fileData);

		share.close();
		session.close();
		connection.close();
		client.close();

	}

	@Test
	public void FTPConnectTest() {

		FTPClient ftp = new FTPClient();
		try {

			FTPClientConfig config = new FTPClientConfig();
			int reply;

			log.info("ftp : {}", this.ftpServer);

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
				return;
			}
			ftp.changeWorkingDirectory("TT/T-ST0253");
			FTPFile[] files = ftp.listFiles();
			for (FTPFile file : files) {
				String filename = file.getName();
				log.info("FTP File: {}", filename);

			}
			ftp.logout();
			log.info("FTP: Logout");
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
					log.info("FTP: Disconnected");
				} catch (IOException ioe) {

				}
			}
		}

	}

	// @Test
	public void FileMergeTest() throws IOException {

		String filePath = "D:\\ProgramTestPaths\\Download";

		File filePathDir = new File(filePath);

		File[] files = filePathDir.listFiles();

		List<String> dataColumn = new ArrayList<>();

		List<Map<String, String>> data = new ArrayList<>();

		for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

			File file = files[fileIndex];

			String absoluteFilePath = file.getAbsolutePath();

			log.info(absoluteFilePath);

			List<String> lines = FileUtils.readLines(file.getAbsoluteFile());

			Boolean dataAreaFg = false;

			for (String line : lines) {

				if (line.startsWith("[Summary data]")) {
					break;
				}

				if (line.startsWith("No.") && dataColumn.size() == 0) {

					String[] columns = line.split(",");

					for (String col : columns) {

						dataColumn.add(col);
					}

					continue;
				}

				if (fileIndex == 0) {
					if (dataColumn.size() > 0 && line.length() > 0) {

						String[] items = line.split(",");

						Map<String, String> dataItem = new HashMap<String, String>();

						for (int i = 0; i < items.length; i++) {
							dataItem.put(dataColumn.get(i), items[i]);
						}
						data.add(dataItem);
					}
				} else {

					if (line.startsWith("1,")) {
						dataAreaFg = true;
					}

					if (dataColumn.size() > 0 && line.length() > 0 && dataAreaFg) {

						String[] items = line.split(",");

						Map<String, String> dataItem = new HashMap<String, String>();

						for (int i = 0; i < items.length; i++) {
							dataItem.put(dataColumn.get(i), items[i]);
						}
						data.add(dataItem);
					}
				}
			}
		}
		
		String fileFileName = files[0].getName();
		
		File outputFile = new File(filePath + "\\" + "Merge.csv");

		if (!outputFile.exists()) {
			outputFile.createNewFile();
		} else {
			outputFile.delete();
		}

		List<String> mergeData = new ArrayList<>();
		mergeData.add(String.join(",", dataColumn));
		for (String col : dataColumn) {
			System.out.print(String.format("%s\t", col));
		}
		System.out.println();
		for (Map<String, String> val : data) {
			String dataLine = "";
			for (String column : dataColumn) {
				String dataVal = val.get(column);
				if (dataVal.contains(",")) {
					dataVal = String.format("\"%s\"", dataVal);
				}
				dataLine += dataVal + ",";
				System.out.print(String.format("%s\t", dataVal));
			}
			if (dataLine.length() > 0 && dataLine.endsWith(",")) {
				dataLine = dataLine.substring(0, dataLine.length() - 1);
				mergeData.add(dataLine);
			}
			System.out.println();
		}

		FileUtils.writeLines(outputFile, mergeData);

	}

}
