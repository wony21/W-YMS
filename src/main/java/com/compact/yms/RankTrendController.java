package com.compact.yms;

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

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.analysis.DTO.ShareReturnData;
import com.compact.yms.domain.ranktrend.RankTrendService;
import com.compact.yms.domain.ranktrend.dto.RankTrendResultData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ranktrend")
public class RankTrendController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RankTrendController.class);

	@Autowired
	RankTrendService rankTrendService;
	
	@RequestMapping(value = "/mitem", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getMItem(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String div, 
			@RequestParam(required = true) String stepSeq,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String program,
			@RequestParam(required = false) String target, 
			@RequestParam(required = false) String chipSpec,
			@RequestParam(required = false) String intensity, 
			@RequestParam(required = false) String pl,
			@RequestParam(required = false) String frameName, 
			@RequestParam(required = false) String subFrameName,
			@RequestParam(required = false) String lotID, 
			@RequestParam(required = true) String type,
			@RequestParam(required = false) String filter) {
		List data = rankTrendService.getMItem(factoryName, startDate, endDate, 
						div, stepSeq, productionType, productSpecGroup, productSpecName, program, target, 
						chipSpec, intensity, pl, frameName, subFrameName, lotID, type, filter);
		
		return ApiResponse.success("ok", data);
	}
	

	@RequestMapping(value = "/anay/seperator", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse getRankTendOfSeperator(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String div, 
			@RequestParam(required = true) String stepSeq,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String program,
			@RequestParam(required = false) String target, 
			@RequestParam(required = false) String chipSpec,
			@RequestParam(required = false) String intensity, 
			@RequestParam(required = false) String pl,
			@RequestParam(required = false) String frameName, 
			@RequestParam(required = false) String subFrameName,
			@RequestParam(required = false, name = "lotID") String lotId, 
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String opt1, 
			@RequestParam(required = true) String filter) {

		
		LocalTime start = LocalTime.now();
		RankTrendResultData retObj = new RankTrendResultData();
		try {
			retObj = rankTrendService.getRankTendOfSeperator(factoryName, startDate, endDate, div, stepSeq,
					productionType, productSpecGroup, productSpecName, program, target, chipSpec, intensity, pl, frameName,
					subFrameName, lotId, type, opt1, filter);
			retObj.setError(false);
			retObj.setErrorMessage("");
			
			LocalTime end = LocalTime.now();

			Duration duration = Duration.between(start, end);

			logger.info("duration [{}] sec(s)", duration.getSeconds());

			if (retObj.getError()) {
				return ApiResponse.error(retObj.getErrorMessage(), "RankTrendSeperator", retObj, duration.getSeconds());
			} else {
				return ApiResponse.success("OK", "RankTrendSeperator", retObj, duration.getSeconds());
			}
			
		} catch(Exception e) {
			retObj.setError(true);
			retObj.setErrorMessage(e.getMessage());
		}

		return ApiResponse.success("ok", retObj);
	}
	
	@RequestMapping(value = "/anay/measure", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse getRankTendOfMeasure(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String div, 
			@RequestParam(required = true) String stepSeq,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String program,
			@RequestParam(required = false) String target, 
			@RequestParam(required = false) String chipSpec,
			@RequestParam(required = false) String intensity, 
			@RequestParam(required = false) String pl,
			@RequestParam(required = false) String frameName, 
			@RequestParam(required = false) String subFrameName,
			@RequestParam(required = false, name = "lotID") String lotId, 
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String itemName) {

		
		LocalTime start = LocalTime.now();
		RankTrendResultData retObj = new RankTrendResultData();
		try {
			retObj = rankTrendService.getRankTendOfMeasure(factoryName, startDate, endDate, div, stepSeq,
					productionType, productSpecGroup, productSpecName, program, target, chipSpec, intensity, pl, frameName,
					subFrameName, lotId, type, itemName);
			retObj.setError(false);
			retObj.setErrorMessage("");
			
			LocalTime end = LocalTime.now();

			Duration duration = Duration.between(start, end);

			logger.info("duration [{}] sec(s)", duration.getSeconds());

			if (retObj.getError()) {
				return ApiResponse.error(retObj.getErrorMessage(), "RankTrendMeasure", retObj, duration.getSeconds());
			} else {
				return ApiResponse.success("OK", "RankTrendMeasure", retObj, duration.getSeconds());
			}
			
		} catch(Exception e) {
			retObj.setError(true);
			retObj.setErrorMessage(e.getMessage());
		}

		return ApiResponse.success("ok", retObj);
	}
	
	@RequestMapping(value = "/anay/both", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse getRankTendOfBoth(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String div, 
			@RequestParam(required = true) String stepSeq,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String program,
			@RequestParam(required = false) String target, 
			@RequestParam(required = false) String chipSpec,
			@RequestParam(required = false) String intensity, 
			@RequestParam(required = false) String pl,
			@RequestParam(required = false) String frameName, 
			@RequestParam(required = false) String subFrameName,
			@RequestParam(required = false, name = "lotID") String lotId, 
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String dataType,
			@RequestParam(required = true) String itemName,
			@RequestParam(required = true) String measureItem) {
		
		LocalTime start = LocalTime.now();
		RankTrendResultData retObj = new RankTrendResultData();
		try {

			retObj = rankTrendService.getRankTrendOfBoth(factoryName, startDate, endDate, div, stepSeq, productionType, productSpecGroup, productSpecName, program, target, chipSpec, intensity, pl, frameName, subFrameName, lotId, type, dataType, itemName, measureItem);
			retObj.setError(false);
			retObj.setErrorMessage("");
			
			LocalTime end = LocalTime.now();

			Duration duration = Duration.between(start, end);

			logger.info("duration [{}] sec(s)", duration.getSeconds());

			if (retObj.getError()) {
				return ApiResponse.error(retObj.getErrorMessage(), "RankTrendBoth", retObj, duration.getSeconds());
			} else {
				return ApiResponse.success("OK", "RankTrendBoth", retObj, duration.getSeconds());
			}
			
		} catch(Exception e) {
			retObj.setError(true);
			retObj.setErrorMessage(e.getMessage());
			logger.error(e.getMessage());
		}

		return ApiResponse.success("ok", retObj);
	}
	
	@RequestMapping(value = "/anay/cdf", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse CDF(@RequestParam String dataTable,
						   @RequestParam String groupName,
						   @RequestParam String subGroupName,
						   @RequestParam String labelName,
						   @RequestParam String dataName) {

		LocalTime start = LocalTime.now();
		List data = null;
		try {
			
			data = rankTrendService.CDF(dataTable, groupName, subGroupName, labelName, dataName);
			
		} catch(Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		
		LocalTime end = LocalTime.now();
		Duration duration = Duration.between(start, end);

		logger.info("duration [{}] sec(s)", duration.getSeconds());

		return ApiResponse.success("OK", "CDF", data, duration.getSeconds());
	}
	
}
