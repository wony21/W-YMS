package com.compact.yms.controller.qm.yield;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.TempFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.file.dto.FileResponse;
import com.compact.yms.domain.qm.yield.seperator.SeperatorYieldService;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldChartDTO;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldCieDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/qm/yield/seperator")
public class SeperatorYieldController extends BaseController {

	@Autowired
	SeperatorYieldService service;

	@RequestMapping(value = "/data", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSperatorYield(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String vendor,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, 
			@RequestParam(required = false) String opt1,
			@RequestParam(required = false) String opt2, 
			@RequestParam(required = false) String opt3) {

		SYieldChartDTO data = service.getSeperatorYield(factoryName, startDate, endDate, productionType, div, productSpecGroup, productSpecName,
				vendor, model, machine, program, target, chipSpec, frameName, pl, opt1, opt2, opt3);

		return ApiResponse.success("OK", "SYieldData", data);
	}
	
	@RequestMapping(value = "/selectChart", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorChartSelect(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String vendor,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, 
			@RequestParam(required = false) String opt1,
			@RequestParam(required = false) String opt2, 
			@RequestParam(required = false) String opt3,
			@RequestParam(required = false) String opt4,
			@RequestParam(required = false) String opt5) {

		SYieldChartDTO data = service.getSeperatorSelectChart(factoryName, startDate, endDate, productionType, div, productSpecGroup, productSpecName,
				vendor, model, machine, program, target, chipSpec, frameName, pl, opt1, opt2, opt3, opt4, opt5);

		return ApiResponse.success("OK", "SYieldDataSelect", data);
	}
	
	@RequestMapping(value = "/binItems", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorBinItems(@RequestParam(required = true) String factoryName, 
											@RequestParam(required = true) String productSpecName, 
											@RequestParam(required = true) String program,
											@RequestParam(required = true) String opt1) {
		
		List list = service.getProgramBinItems(factoryName, productSpecName, program, opt1);
		
		return ApiResponse.success("OK", list);
		
	}
	
	/**
	 * 기간별 상세 수율 집계
	 * @param factoryName
	 * @param startDate
	 * @param endDate
	 * @param productionType
	 * @param div
	 * @param productSpecGroup
	 * @param productSpecName
	 * @param vendor
	 * @param model
	 * @param machine
	 * @param program
	 * @param target
	 * @param chipSpec
	 * @param frameName
	 * @param pl
	 * @return
	 */
	@RequestMapping(value = "/detail/period", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorYieldDetailPeriod(
			@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String vendor,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl) {

		List list = service.getSYieldDetailPeriod(factoryName, startDate, endDate, productionType, div, productSpecGroup, productSpecName,
																	vendor, model, machine, program, target, chipSpec, frameName, pl);

		return ApiResponse.success("OK", list);
	}
	
	@RequestMapping(value = "/detail/period/bin", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorYieldDetailPeriodOfBin(
			@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String vendor,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl,
			@RequestParam(required = false) String binType,
			@RequestParam(required = false) String binItems) {

		List list = service.getSYieldDetailPeriodOfBin(factoryName, startDate, endDate, productionType, div, productSpecGroup, productSpecName,
																	vendor, model, machine, program, target, chipSpec, frameName, pl, binType, binItems);

		return ApiResponse.success("OK", list);
	}
	
	@RequestMapping(value = "/getRawdata", method = RequestMethod.GET, produces = "application/download; utf-8")
	public void downloadMergeFile(@RequestParam String factoryName, 
								  @RequestParam String lotID, 
								  @RequestParam String productSpecName, 
								  @RequestParam String program,
								  HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		FileResponse fileRes = service.getCIERawdata(factoryName, lotID, productSpecName, program);
		
		if (fileRes == null) {
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
			log.error("파일이 존재하지 않습니다. [{}]", fileName);
			String errmsg = URLEncoder.encode(String.format("파일이 존재하지 않습니다. [{}]", fileName), "UTF-8");
			response.sendRedirect(String.format("/equipment/error?errmsg=%s", errmsg));
			return;
		}
		
		fileName = URLEncoder.encode(fileName, "UTF-8");
		log.info("download file name : " + fileName);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);

		} catch (Exception e) {
			log.error(e.getMessage());
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
				log.info("delete filename : {}", fileName);
			} catch (Exception e) {
				
			}
		}
	}
	
	@RequestMapping(value = "/getCieData", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse getCieData(@RequestParam String factoryName, 
								  @RequestParam String lotID, 
								  @RequestParam String productSpecName, 
								  @RequestParam String program,
								  @RequestParam MultipartFile file) throws IOException {
		
		File tmpFile = TempFile.createTempFile("tmp_", ".dat");
		file.transferTo(tmpFile);
		SYieldCieDTO result = service.getCIEData(tmpFile, factoryName, lotID, productSpecName, program);
		tmpFile.delete();
		return ApiResponse.success("ok", "SYieldCieData", result);
	}
	
	// getSeperatorCieData
	
	@RequestMapping(value = "/getSYieldCieData", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSYieldCieData(@RequestParam String factoryName, 
								  @RequestParam String lotID, 
								  @RequestParam String productSpecName, 
								  @RequestParam String program) throws IOException {
		
		SYieldCieDTO result = service.getSeperatorCieData(factoryName, lotID, productSpecName, program);
		return ApiResponse.success("ok", "SYieldCieData", result);
	}
	
	@RequestMapping(value = "/data/bin", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSeperatorYieldOfRank(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String vendor,
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl, 
			@RequestParam(required = false) String opt1,
			@RequestParam(required = false) String opt2, 
			@RequestParam(required = false) String opt3,
			@RequestParam(required = false) String opt4,
			@RequestParam(required = false) String opt5,
			@RequestParam(required = false) String binType,
			@RequestParam(required = false) String binItems) {

		SYieldChartDTO data = service.getSeperatorYieldOfRank(factoryName, startDate, endDate, productionType, div, productSpecGroup, productSpecName,
				vendor, model, machine, program, target, chipSpec, frameName, pl, opt1, opt2, opt3, opt4, opt5, binType, binItems);

		return ApiResponse.success("OK", "SYieldDataOfBin", data);
	}
	
	
}
