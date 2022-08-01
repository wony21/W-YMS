package com.compact.base;

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
	public ApiResponse getYiedItemNames(@RequestParam String div, @RequestParam String stepSeq, @RequestParam String productSpecName,
			@RequestParam String startTime, @RequestParam String endTime, @RequestParam String lotID) throws Exception {

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
				analysisService.getShareTotalAnalysis(div, startTime, endTime, stepSeq, productSpecGroup, productSpecName,
						program, target, chipSpec, frameName, lotID, itemFilter, min, max, stepCount));
	}

}
