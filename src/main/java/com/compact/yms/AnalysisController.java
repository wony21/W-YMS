package com.compact.yms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalTime;
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
import org.springframework.web.multipart.MultipartFile;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.analysis.AnalysisService;
import com.compact.yms.domain.analysis.DTO.ChartShareData;
import com.compact.yms.domain.analysis.DTO.ItemName;
import com.compact.yms.domain.analysis.DTO.ShareReturnData;
import com.compact.yms.domain.file.dto.FileResponse;

import net.lingala.zip4j.util.FileUtils;

@Controller
@RequestMapping("/analysis")
public class AnalysisController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

	@Autowired
	AnalysisService analysisService;

	@RequestMapping(value = "/getFileData", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getFileData(@RequestParam String div, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String stepSeq, @RequestParam String productSpecGroup,
			@RequestParam String productSpecName, @RequestParam String program, @RequestParam String target,
			@RequestParam String chipSpec, @RequestParam String frameName, @RequestParam String lotID,
			@RequestParam String itemFilter, @RequestParam Double min, @RequestParam Double max,
			@RequestParam Integer stepCount) throws Exception {

		return ApiResponse.success("OK",
				analysisService.getYieldRawdata(div, startTime, endTime, stepSeq, productSpecGroup, productSpecName,
						program, target, chipSpec, frameName, lotID, itemFilter, min, max, stepCount));
	}

	@RequestMapping(value = "/getItemNames", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getYiedItemNames(@RequestParam String div, @RequestParam String stepSeq,
			@RequestParam String productSpecName, @RequestParam String startTime, @RequestParam String endTime,
			@RequestParam String lotID) throws Exception {

		return ApiResponse.success("OK",
				analysisService.getYiedItemNames(div, stepSeq, productSpecName, startTime, endTime, lotID));
	}
/*
	@RequestMapping(value = "/getChartChipAnalysis", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getChartChipAnalysis(@RequestParam String div, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String stepSeq, @RequestParam String productSpecGroup,
			@RequestParam String productSpecName, @RequestParam String program, @RequestParam String target,
			@RequestParam String chipSpec, @RequestParam String frameName, @RequestParam String lotID,
			@RequestParam String itemFilter, @RequestParam Double min, @RequestParam Double max,
			@RequestParam Integer stepCount) throws Exception {

		return ApiResponse.success("OK",
				analysisService.getShareTotalAnalysis(div, startTime, endTime, stepSeq, productSpecGroup,
						productSpecName, program, target, chipSpec, frameName, lotID, itemFilter, min, max, stepCount));
	}
*/
	@RequestMapping(value = "/measureItemName", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getItemName10Y(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType, @RequestParam(required = false) String div,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, @RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, @RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, @RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, @RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, @RequestParam(required = false) String itemFilter) {

		List<ItemName> list = analysisService.getMeasureItemNames(startDate, endDate, factoryName, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotID,
				itemFilter);

		return ApiResponse.success("OK", list);
	}

	@RequestMapping(value = "/getSeperatorCie", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorCie(@RequestParam String factoryName, @RequestParam String productSpecName,
			@RequestParam String program) {
		List list = analysisService.getSeperatorCie(factoryName, productSpecName, program);
		return ApiResponse.success("OK", list);
	}

	@RequestMapping(value = "/run/share/range", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse analysisShareRange(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType, @RequestParam(required = false) String div,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, @RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, @RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, @RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, @RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, @RequestParam(required = false) String itemName,
			@RequestParam(required = false) String shareLimit) throws IOException {

		LocalTime start = LocalTime.now();

		ShareReturnData data = analysisService.calculatorShareData(startDate, endDate, factoryName, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotID,
				itemName, shareLimit);

		LocalTime end = LocalTime.now();

		Duration duration = Duration.between(start, end);

		logger.info("duration [{}] sec(s)", duration.getSeconds());

		return ApiResponse.success("OK", "Share", data, duration.getSeconds());
	}

	@RequestMapping(value = "/run/share/cie", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse analysisShareCie(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType, @RequestParam(required = false) String div,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, @RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, @RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, @RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, @RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, @RequestParam(required = false) String itemName,
			@RequestParam(required = false) String cieLimit) throws IOException {

		LocalTime start = LocalTime.now();

		ShareReturnData data = analysisService.calculatorShareCieData(startDate, endDate, factoryName, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotID,
				itemName, cieLimit);

		LocalTime end = LocalTime.now();

		Duration duration = Duration.between(start, end);

		logger.info("duration [{}] sec(s)", duration.getSeconds());

		Boolean error = data.getError();
		String errorMessage = data.getErrorMessage();

		if (error) {
			return ApiResponse.error(errorMessage, "Share", data, duration.getSeconds());
		} else {
			return ApiResponse.success("OK", "Share", data, duration.getSeconds());
		}
	}

	@RequestMapping(value = "/run/share/both", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse analysisShareBoth(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType, @RequestParam(required = false) String div,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, @RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, @RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, @RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, @RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, @RequestParam(required = false) String itemName,
			@RequestParam(required = false) String shareLimit, @RequestParam(required = false) String cieLimit)
			throws IOException {

		LocalTime start = LocalTime.now();

		ShareReturnData data = analysisService.calculatorShareBothData(startDate, endDate, factoryName, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotID,
				itemName, shareLimit, cieLimit);

		LocalTime end = LocalTime.now();

		Duration duration = Duration.between(start, end);

		logger.info("duration [{}] sec(s)", duration.getSeconds());

		Boolean error = data.getError();
		String errorMessage = data.getErrorMessage();

		if (error) {
			return ApiResponse.error(errorMessage, "ShareBoth", data, duration.getSeconds());
		} else {
			return ApiResponse.success("OK", "ShareBoth", data, duration.getSeconds());
		}
	}

	@RequestMapping(value = "/run/history/save", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse saveAnalysisHistory(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, @RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType, @RequestParam(required = false) String div,
			@RequestParam(required = false) String stepSeq, @RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, @RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, @RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, @RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, @RequestParam(required = false) String frameName,
			@RequestParam(required = false) String subFrameName, @RequestParam(required = false) String intensity,
			@RequestParam(required = false) String pl, @RequestParam(required = false) String userId,
			@RequestParam MultipartFile file) {

		try {

			LocalTime start = LocalTime.now();

			File orgFile = new File(file.getOriginalFilename());

			file.transferTo(orgFile);

			analysisService.saveHistory(startDate, endDate, factoryName, div, stepSeq, productionType, productSpecGroup,
					productSpecName, program, target, chipSpec, frameName, subFrameName, intensity, pl, lotID, userId,
					orgFile);

			orgFile.delete();

			LocalTime end = LocalTime.now();

			Duration duration = Duration.between(start, end);

			logger.info("duration [{}] sec(s)", duration.getSeconds());

			return ApiResponse.success("OK");

		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}

	}

	@RequestMapping(value = "/run/history/get", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getRunHistory(@RequestParam(required = true) String startDate,
			@RequestParam(required = true) String endDate, @RequestParam(required = true) String userId,
			@RequestParam(required = true) String type,
			@RequestParam(required = false) String optSaveDate, @RequestParam(required = false) String cond1,
			@RequestParam(required = false) String cond2, @RequestParam(required = false) String cond3,
			@RequestParam(required = false) String cond4, @RequestParam(required = false) String cond5,
			@RequestParam(required = false) String cond6, @RequestParam(required = false) String cond7,
			@RequestParam(required = false) String cond8) {

		logger.info("Get History!");
		
		List list = analysisService.getHistory(startDate, endDate, userId, type, optSaveDate, cond1, cond2, cond3, cond4,
				cond5, cond6, cond7, cond8);

		return ApiResponse.success("OK", list);
	}

	@RequestMapping(value = "/run/share/getSharedUser", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getShareUsers(@RequestParam(required = true) String key) {
		List list = analysisService.getShareUserInKey(key);
		return ApiResponse.success("OK", list);
	}

	@RequestMapping(value = "/run/share/getUsers", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getAllUserForShare(@RequestParam(required = false) String filter) {
		List list = analysisService.getShareUsers(filter);
		return ApiResponse.success("OK", list);
	}

	@RequestMapping(value = "/run/share", method = RequestMethod.PUT, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse shareHistory(@RequestParam(required = true) String key,
			@RequestParam(required = true) String userIds) {
		return analysisService.shareHistory(key, userIds);
	}

	@RequestMapping(value = "/run/share", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse deleteHistoryShare(@RequestParam(required = true) String key,
			@RequestParam(required = true) String userIds) {
		return analysisService.removeShare(key, userIds);
	}

	@RequestMapping(value = "/run/share/memo", method = RequestMethod.PUT, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse addHistoryMemo(@RequestParam(required = true) String key,
			@RequestParam(required = true) String memo) {
		return analysisService.addHistoryMemo(key, memo);
	}

	@RequestMapping(value = "/run/history", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse deleteHistory(@RequestParam(required = true) String keys,
			@RequestParam(required = true) String userId) {
		return analysisService.deleteHistory(keys, userId);
	}

	@RequestMapping(value = "/run/history/file", method = RequestMethod.GET, produces = "application/download; utf-8")
	public void downloadHistoryFile(@RequestParam String key, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		FileResponse downloadResponse = analysisService.downloadHistoryFile(key);

		if (downloadResponse.getIsError()) {
			response.sendError(500, downloadResponse.getErrorMsg());
			return;
		}

		String fileName = downloadResponse.getOrgFileName();
		File file = downloadResponse.getFile();
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {

			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
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
