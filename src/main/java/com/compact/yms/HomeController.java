package com.compact.yms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.FormattableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.compact.pop.domain.operation.OperationService;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.chipmaster.file.FileBulkParserService;
import com.compact.yms.domain.chipmaster.file.FileService;
import com.compact.yms.domain.chipmaster.user.User;
import com.compact.yms.domain.chipmaster.user.UserService;
import com.compact.yms.domain.common.CommonService;
import com.compact.yms.domain.common.SFTPService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.sqlserver.jdbc.StringUtils;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	CommonService commonService;

	@Autowired
	SFTPService sftpService;

	@Autowired
	FileService fileService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	OperationService operationService;
	
	@Autowired
	FileBulkParserService fileBulkParserService;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		return "login";
	}
	
	@RequestMapping(value = "/chipmaster", method = RequestMethod.GET)
	public String ChipMasterDownload(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model) {
		String errorMsg = "";
		String masterNo = request.getParameter("param");

		if ( StringUtils.isEmpty(masterNo)) {
			return "chipmaster/error";
		}
		
		logger.info("master no : " + masterNo);
		File file = fileService.getChipMasterFile(masterNo);
		
		if ( file == null ) {
			logger.info("file is null");
			return "chipmaster/error2";
		} else {
			long fileSize = Math.round(file.length() / 1024);
			String fileName = file.getName();
			DecimalFormat formatter = new DecimalFormat("#,###");
			String fmtFileSize = formatter.format(fileSize);
			model.addAttribute("fileName", fileName);
			model.addAttribute("fileSize", fmtFileSize);
		}
		
		model.addAttribute("masterNo", masterNo);
		
		return "chipmaster/cmdown";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String ChipMasterDownload(@RequestParam String errorMessage, Locale locale, Model model) {
		model.addAttribute("error", errorMessage);
		return "chipmaster/error";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List test() {
		return commonService.getTest();
	}

	@RequestMapping(value = "/ifcheck", method = RequestMethod.GET)
	public String ifcheck(Locale locale, Model model) {
		return "yield/IFCheck";
	}

	@RequestMapping(value = "/usage", method = RequestMethod.GET)
	public String usage(Locale locale, Model model) {
		return "yield/usage";
	}

	@RequestMapping(value = "/operation", method = RequestMethod.GET)
	public String operation(Locale locale, Model model) {
		return "operation/operation";
	}
	
	@RequestMapping(value = "/calendar", method = RequestMethod.GET)
	public String calendar(Locale locale, Model model) {
		return "calendar/calendar";
	}
	
	@RequestMapping(value = "/popIF", method = RequestMethod.GET)
	public String popIF(Locale locale, Model model) {
		return "IF/popIF";
	}

	@RequestMapping(value = "/api/common/yield/ymsfilem", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getYmsfileMaster(@RequestParam String schSttDt, @RequestParam String schEndDt,
			@RequestParam(required = false) String equipId, @RequestParam(required = false) String partId,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String lotId,
			@RequestParam(required = false) String fileName, @RequestParam(required = false) String lotType,
			@RequestParam(required = false) String pgmName) {
		return commonService.getFileM(schSttDt, schEndDt, equipId, partId, stepSeq, lotId, fileName, lotType, pgmName);
	}

	@RequestMapping(value = "/api/common/yield/ymsfileindex", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getYmsfileIndex(@RequestParam String schSttDt, @RequestParam String schEndDt,
			@RequestParam(required = false) String machineName, @RequestParam(required = false) String fileName) {
		return commonService.getFileIndex(schSttDt, schEndDt, machineName, fileName);
	}

	@RequestMapping(value = "/api/common/yield/ymsusemenucount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getYmsMenuUsageCount(@RequestParam String schSttDt, @RequestParam String schEndDt,
			@RequestParam(required = false) String menuNm, @RequestParam(required = false) String userNm) {
		return commonService.getMenuCount(schSttDt, schEndDt, menuNm, userNm);
	}

	@RequestMapping(value = "/api/common/yield/usermenucount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getYmsUserUsageCount(@RequestParam String schdt, @RequestParam(required = false) String menuNm,
			@RequestParam(required = false) String userNm) {
		return commonService.getUseMenuList(schdt, menuNm, userNm);
	}

	@RequestMapping(value = "/api/common/file/read", method = RequestMethod.GET)
	public void getYmsFile(@RequestParam String fileNm, HttpServletRequest request, HttpServletResponse response) {
		String outText = "";
		sftpService.setTargetFileName(fileNm);
		File downloadFile = sftpService.Connect();
		if (downloadFile == null) {
			return;
		}
		// 파일명 지정
		response.setContentType("application/octer-stream");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNm + "\"");
		try {
			OutputStream os = response.getOutputStream();
			FileInputStream fis = new FileInputStream(downloadFile.getAbsolutePath());

			int ncount = 0;
			byte[] bytes = new byte[512];

			while ((ncount = fis.read(bytes)) != -1) {
				os.write(bytes, 0, ncount);
			}
			fis.close();
			os.close();
			downloadFile.delete();

		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException");
		} catch (IOException ex) {
			System.out.println("IOException");
		}
	}

	@RequestMapping(value = "/api/common/mapping/nomapping", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getNotMappingOperations(@RequestParam String schSttDt, @RequestParam String schEndDt) {
		return commonService.getNotMappingOperations(schSttDt, schEndDt);
	}

	@RequestMapping(value = "/cm/std/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String uploadChipMasterController(@RequestParam String masterNo, @RequestParam String interfaceCd,
			@RequestParam MultipartFile file) {
		ApiResponse apiResponse = null;
		String resultMessage = "";
		try {

			logger.info("Upload Type : {}", interfaceCd);

			if (interfaceCd.equals("CMF")) {
				apiResponse = fileService.uploadFile(masterNo, file);
			} else if (interfaceCd.equals("POP")) {
				apiResponse = fileService.uploadChipMasterWithExcel(masterNo, file);
			}

		} catch (Exception e) {
			resultMessage = "UPLOAD : Error : " + e.getMessage();
//			try {
//				resultMessage = new String(resultMessage.getBytes("8859_1"), "UTF-8");
//			} catch (Exception e1) {
//				
//			}
			
			return resultMessage; 
		}

		if (apiResponse != null) {
			if (apiResponse.status == 200) {
				resultMessage = "OK";
			} else {
				resultMessage = apiResponse.getError();
			}
		} else {
			resultMessage = "UPLOAD PROCESS ERROR! - Controller";
		}
		
//		try {
//			resultMessage = new String(resultMessage.getBytes("8859_1"), "UTF-8");
//		} catch (Exception e1) {
//			
//		}
		
		return resultMessage;
	}

	@RequestMapping(value = "/cm/std/download", method = RequestMethod.GET, produces = "application/download; utf-8")
	public void downloadChipMaster(@RequestParam String masterNo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		File file = fileService.getChipMasterFile(masterNo);
		if (file == null) {
			response.sendError(500, "파일을 찾을 수 없습니다.");
		}

		// String fileName = new String(file.getName().getBytes("UTF-8"), "8859_1");
		String fileName = file.getName();
		fileName = URLEncoder.encode(fileName, "UTF-8");

		logger.info("download file name : " + fileName);

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);

		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		} finally {

			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					response.sendError(500, e.getMessage());
				}
			}

		} // try end;
		out.flush();
	}
	
	@RequestMapping(value = "/cm/std/delete", method = RequestMethod.DELETE, produces = "application/json; utf-8")
	@ResponseBody
	public ApiResponse deleteChipMaster(@RequestParam String masterNo) throws IOException {
		
		File file = fileService.getChipMasterFile(masterNo);
		if (file != null) {
			file.delete();
		}
		fileService.deleteMaster(masterNo);
		
		return ApiResponse.success("OK");
	}
	
	// public ApiResponse parseMasterExcel(@Reque)
	@RequestMapping(value = "/cm/std/parse", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String parseMasterExcel(@RequestParam MultipartFile file) throws JsonProcessingException {
		ApiResponse resp = fileService.parseMasterFile(file);
		ObjectMapper mapper = new ObjectMapper();
		//mapper.writeValueAsString(value);
		return mapper.writeValueAsString(resp);
	}
	
	
	@RequestMapping(value = "/api/common/add", method = RequestMethod.PUT, produces = "application/json; charset=UTF-8")
	public void AddTrans(@RequestBody List<User> userInfos) {
		logger.info("request add user");
		try {
			userService.AddUser(userInfos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// operationService
	
	@RequestMapping(value = "/api/common/operation", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List GetOperationList() {
		return operationService.getProcessOperation();
	}
	
	@RequestMapping(value = "/api/common/operation/add", method = RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public ApiResponse AddProcessOperation(@RequestBody List<Map> sqls) {
		
		try {
			for(Map param : sqls) {
				String sql = param.getOrDefault("insertSql", "").toString();
				logger.info(sql);
				operationService.addProcessOperation(sql);
			}
		} catch ( Exception ex ) {
			logger.error(ex.getMessage());
			return ApiResponse.error(ex.getMessage());
		}

		logger.info("정상완료");
		return ApiResponse.success("Ok");
	}
	
	@RequestMapping(value = "/cm/std/bulk/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String uploadBulkMasterFiles(@RequestParam MultipartFile file,
										@RequestParam String reqSite,
										@RequestParam String div,
										@RequestParam String productGroup,
										@RequestParam String status) throws JsonProcessingException {
		
		ApiResponse response = fileBulkParserService.moveToBulkUploadPath(reqSite, div, productGroup, status, file);
		ObjectMapper objMapper = new ObjectMapper();
		return objMapper.writeValueAsString(response);
		
	}
	
	
	
}
