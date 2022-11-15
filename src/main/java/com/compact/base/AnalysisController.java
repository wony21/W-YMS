package com.compact.base;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.base.common.BaseController;
import com.compact.base.common.api.ApiResponse;
import com.compact.base.domain.analysis.AnalysisService;
import com.compact.base.domain.analysis.DTO.ChartShareData;
import com.compact.base.domain.analysis.DTO.ItemName;
import com.compact.base.domain.analysis.DTO.ShareReturnData;

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
			@RequestParam(required = false) String pl, @RequestParam(required = false) String itemName, @RequestParam(required = false) String shareLimit) throws IOException {

		LocalTime start = LocalTime.now();
		
		ShareReturnData data = analysisService.calculatorShareData(startDate, endDate, factoryName, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, frameName, pl, lotID,
				itemName, shareLimit);

		LocalTime end = LocalTime.now();
		
		Duration duration = Duration.between(start, end);
		
		logger.info("duration [{}] sec(s)", duration.getSeconds());
		
		return ApiResponse.success("OK", "Share", data, duration.getSeconds());
	}

}
