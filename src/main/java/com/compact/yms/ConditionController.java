package com.compact.yms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.QueryCond.QueryCondService;
import com.compact.yms.domain.common.CommonService;
import com.compact.yms.domain.cond.ConditionService;

@Controller
@RequestMapping("/cond")
public class ConditionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ConditionController.class);

	@Autowired
	ConditionService conditionService;

	@Autowired
	CommonService commonService;
	
	@Autowired
	QueryCondService condService;

	@RequestMapping(value = "/getSite", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSite(@RequestParam String factoryName) {
		return ApiResponse.success("OK", conditionService.getSite(factoryName));
	}
	
	@RequestMapping(value = "/getProductType", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProductType() {
		return ApiResponse.success("OK", conditionService.getProductType());
	}

	@RequestMapping(value = "/getLine", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getLine(@RequestParam String factoryName, @RequestParam String div) {
		return ApiResponse.success("OK", conditionService.getLine(factoryName, div));
	}

	@RequestMapping(value = "/getStep", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getStep(@RequestParam String factoryName) {
		return ApiResponse.success("OK", conditionService.getStep(factoryName));
	}

	@RequestMapping(value = "/getProductGroup", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProductGroup(@RequestParam String factoryName, @RequestParam String div,
			@RequestParam String productSpecGroup) {
		return ApiResponse.success("OK", conditionService.getProductGroup(factoryName, div, productSpecGroup));
	}

	@RequestMapping(value = "/getProduct", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProduct(@RequestParam String factoryName, @RequestParam String div,
			@RequestParam String startTime, @RequestParam String endTime, @RequestParam String stepSeq, @RequestParam String productionType,
			@RequestParam String productSpecGroup, @RequestParam String product) {
		return ApiResponse.success("OK",
				conditionService.getProduct(factoryName, div, stepSeq, productionType, startTime, endTime, productSpecGroup, product));
	}

	@RequestMapping(value = "/getProgram", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProgram(@RequestParam String factoryName, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String div, @RequestParam String stepSeq,@RequestParam String productionType,
			@RequestParam String productSpecGroup, @RequestParam String product, @RequestParam String program) {
		return ApiResponse.success("OK", conditionService.getProgram(factoryName, div, stepSeq, productionType, startTime, endTime,
				productSpecGroup, product, program));
	}

	@RequestMapping(value = "/getTarget", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getTarget(@RequestParam String factoryName, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String stepSeq, @RequestParam String productionType,@RequestParam String div,
			@RequestParam String productSpecGroup, @RequestParam String product, @RequestParam String program,
			@RequestParam String target) {
		return ApiResponse.success("OK", conditionService.getTarget(factoryName, div, stepSeq, productionType, startTime, endTime,
				productSpecGroup, product, program, target));
	}

	@RequestMapping(value = "/getChipSpec", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getChipSpec(@RequestParam String factoryName, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String div, @RequestParam String stepSeq,@RequestParam String productionType,
			@RequestParam String productSpecGroup, @RequestParam String product, @RequestParam String program,
			@RequestParam String target, @RequestParam String chipSpec) {
		return ApiResponse.success("OK", conditionService.getChipSpec(factoryName, div, stepSeq, productionType, startTime, endTime,
				productSpecGroup, product, program, target, chipSpec));
	}

	@RequestMapping(value = "/getFrameName", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getFrameName(@RequestParam String factoryName, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String div, @RequestParam String stepSeq,@RequestParam String productionType,
			@RequestParam String productSpecGroup, @RequestParam String product, @RequestParam String program,
			@RequestParam String target, @RequestParam String chipSpec, @RequestParam String frameName) {
		return ApiResponse.success("OK", conditionService.getFrameName(factoryName, div, stepSeq, productionType, startTime, endTime,
				productSpecGroup, product, program, target, chipSpec, frameName));
	}

	@RequestMapping(value = "/getLotId", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getLotId(@RequestParam String factoryName, @RequestParam String startTime,
			@RequestParam String endTime, @RequestParam String div, @RequestParam String stepSeq,@RequestParam String productionType,
			@RequestParam String productSpecGroup, @RequestParam String product, @RequestParam String program,
			@RequestParam String target, @RequestParam String chipSpec, @RequestParam String frameName,
			@RequestParam String lotId) {
		return ApiResponse.success("OK", conditionService.getLotIds(factoryName, div, stepSeq, productionType, startTime, endTime,
				productSpecGroup, product, program, target, chipSpec, frameName, lotId));
	}
	
	@RequestMapping(value = "division", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getDivision(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String div, 
			@RequestParam(required = false) String stepSeq,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName) {
		return condService.getLine(startDate, endDate, stepSeq, factoryName);
	}
	

	@RequestMapping(value = "/{type}/list", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getCond10Y(@PathVariable String type, 
			@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate,
			@RequestParam(required = false) String productionType,
			@RequestParam(required = false) String div, 
			@RequestParam(required = false) String stepSeq,
			@RequestParam(required = false) String productSpecGroup,
			@RequestParam(required = false) String productSpecName, 
			@RequestParam(required = false) String lotID,
			@RequestParam(required = false) String lotText, 
			@RequestParam(required = false) String machine,
			@RequestParam(required = false) String program, 
			@RequestParam(required = false) String target,
			@RequestParam(required = false) String chipSpec, 
			@RequestParam(required = false) String frameName,
			@RequestParam(required = false) String pl) {

		if ("stepSeq".equals(type)) {
			return condService.getStepSeq(startDate, endDate, stepSeq);
		} else if ("stepType".equals(type)) {
			return condService.getStepType(startDate, endDate, stepSeq, "");
		} else if ("site".equals(type)) {
			return condService.getSite();
		} else if ("div".equals(type)) {
			return condService.getLine(startDate, endDate, stepSeq, factoryName);
		} else if ("productSpecGroup".equals(type)) {
			return condService.getProductSpecGroup(startDate, endDate, stepSeq, factoryName, div, productSpecGroup);
		} else if ("productSpecName".equals(type)) {
			return condService.getProductSpecName(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup, productSpecName);
		} else if ("productSpecNameTypeA".equals(type)) {
			return condService.getProductSpecNameTypeA(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup, productSpecName);
		} else if ("lotId".equals(type)) {
			/*
			return condService.getLOTID(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID);
			*/
			return condService.getLOTID(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup, productSpecName,
					lotID, lotText, machine, program, target, chipSpec, frameName, pl);
		} else if ("machine".equals(type)) {
			return condService.getMachine(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID, lotText, machine);
		} else if ("program".equals(type)) {
			return condService.getProgram(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID, lotText, machine, program);
		} else if ("target".equals(type)) {
			return condService.getTarget(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID, lotText, machine, program, target);
		} else if ("chipSpec".equals(type)) {
			return condService.getChipSpec(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID, lotText, machine, program, target, chipSpec);
		} else if ("frameName".equals(type)) {
			return condService.getFrameName(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup,
					productSpecName, lotID, lotText, machine, program, target, chipSpec, frameName);
		} else if ("pl".equals(type)) {
			return condService.getPL(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup, productSpecName,
					lotID, lotText, machine, program, target, chipSpec, frameName, pl);
		} else if ("reelBatchId".equals(type)) {
			return condService.getReelBatchID(startDate, endDate, stepSeq, productionType, factoryName, div, productSpecGroup, productSpecName,
					lotID, lotText, machine, program, target);
		}

		return ApiResponse.error("일치하는정보없음");
	}
	
	
}
