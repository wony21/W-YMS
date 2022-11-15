package com.compact.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.base.common.BaseController;
import com.compact.base.common.api.ApiResponse;
import com.compact.base.domain.file.EqpFileService;
import com.compact.base.domain.file.dto.FileResponse;

import net.lingala.zip4j.util.FileUtils;

@Controller
@RequestMapping("/equipment")
public class EqpFileController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(EqpFileController.class);

	@Autowired
	EqpFileService eqpFileService;

	@RequestMapping(value = "/getFileList", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getFileList(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String site, @RequestParam String stepSeq, @RequestParam String div,
			@RequestParam String productGroups, @RequestParam String productSpecNames,
			@RequestParam String machineNames, @RequestParam String programs, @RequestParam String lotID,
			@RequestParam String targets, @RequestParam String chipSpecs, @RequestParam String frameNames,
			@RequestParam String pls, @RequestParam String lotLike) {

		try {

			logger.info("[div] {}", div);
			logger.info("[productGroup] {}", productGroups);
			logger.info("[Product] {}", productSpecNames);

			List list = eqpFileService.getFileList(startDate, endDate, site, stepSeq, div, productGroups,
					productSpecNames, machineNames, programs, lotID, targets, chipSpecs, frameNames, pls, lotLike);

			if (list != null) {
				return ApiResponse.success("OK", list);
			} else {
				return ApiResponse.error("검색된 정보가 존재하지 않습니다.");
			}

		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getFileTest", method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ApiResponse getFileTest(@RequestParam String fmKey) {
		
		return ApiResponse.success("OK", null);
	}
	
	@RequestMapping(value="/error", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse error(@RequestParam String errmsg, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Content-Status", "ERROR");
		return ApiResponse.error(errmsg);
	}
	
	@RequestMapping(value = "/getFile", method = RequestMethod.GET, produces = "application/download; utf-8")
	public void downloadFile(@RequestParam String fmKey, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		FileResponse fileRes = eqpFileService.getFile(fmKey);
		
		if ( fileRes == null ) {
			response.sendError(500, "null 반환");
			return;
		}
		
		if (fileRes.getIsError()) {
			String errmsg = URLEncoder.encode(fileRes.getErrorMsg(), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
			return;
		}
		
		String fileName = fileRes.getOrgFileName();
		File file = fileRes.getFile();
		if (file == null) {
			logger.error("파일이 존재하지 않습니다. [{}]", fileName);
			String errmsg = URLEncoder.encode(String.format("파일이 존재하지 않습니다. [{}]", fileName), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
			return;
		}
		
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
			logger.error(e.getMessage());
			String errmsg = URLEncoder.encode(e.getMessage(), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
		} finally {

			if (fis != null) {
				try {
					fis.close();
					
				} catch (Exception e) {
					// response.sendError(500, e.getMessage());
				}
			}
			
			try {
				out.flush();
				file.delete();
				logger.info("delete filename : {}", fileName);
			} catch (Exception e) {
				
			}
		} 
	}
	
	@RequestMapping(value = "/getMergeFile", method = RequestMethod.GET, produces = "application/download; utf-8")
	public void downloadMergeFile(@RequestParam String fmKey, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String[] fmKeys = fmKey.split(",");
		
		FileResponse fileRes = eqpFileService.mergeFile(fmKeys);
		
		if ( fileRes == null ) {
			response.sendError(500, "null 반환");
			return;
		}
		
		if (fileRes.getIsError()) {
			String errmsg = URLEncoder.encode(fileRes.getErrorMsg(), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
			return;
		}
		
		String fileName = fileRes.getOrgFileName();
		File file = fileRes.getFile();
		if (file == null) {
			logger.error("파일이 존재하지 않습니다. [{}]", fileName);
			String errmsg = URLEncoder.encode(String.format("파일이 존재하지 않습니다. [{}]", fileName), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
			return;
		}
		
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
			logger.error(e.getMessage());
			String errmsg = URLEncoder.encode(e.getMessage(), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
		} finally {

			if (fis != null) {
				try {
					fis.close();
					
				} catch (Exception e) {
					// response.sendError(500, e.getMessage());
				}
			}
			
			try {
				out.flush();
				file.delete();
				logger.info("delete filename : {}", fileName);
			} catch (Exception e) {
				
			}
		} 
	}

}
