package wyms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.EnumSet;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.junit.Test;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

public class NetworkDictoryTest {
	
	public static double calculateSD(double numArray[]) {
		
		double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
		
	}
	
	@Test
	public void ArrayTest() {
		
		double min = 0.01;
		double max = 0.2;
		int stepCount = 5;
		double step = (max - min) / stepCount;
		
		System.out.println("Step : " + step);
		
		double std = calculateSD(new double[] {min, max} );
		
		System.out.println("std : " + std);

		int seq = 1;
		System.out.println(String.format("G%d : (none) - %f", seq++, min));
		double preValue = min;
		for(double i=min + step; i<= max; i += step) {
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
		com.hierynomus.smbj.share.File file = share.openFile("TT\\INB\\T-ST0253\\2022\\01\\12\\211224-0002-001-005-21_202201120026.csv", 
								EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
		
		InputStream fin = new BufferedInputStream(file.getInputStream());
		byte[] fileData = IOUtils.toByteArray(fin);
		
		java.io.File workFile = new File(workPath);
		
		if(!workFile.exists()) {
			workFile.createNewFile();
		}
		
		FileUtils.writeByteArrayToFile(workFile, fileData);
		
		share.close();
		session.close();
		connection.close();
		client.close();
		
	}

}
