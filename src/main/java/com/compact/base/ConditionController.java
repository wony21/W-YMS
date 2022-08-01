package com.compact.base;

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
import com.compact.base.domain.cond.ConditionService;

@Controller
@RequestMapping("/cond")
public class ConditionController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ConditionController.class);

	@Autowired
	ConditionService conditionService;

	@RequestMapping(value = "/getSite", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSite(@RequestParam String factoryName) {
		return ApiResponse.success("OK", conditionService.getSite(factoryName));
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
			@RequestParam String productGroup) {
		return ApiResponse.success("OK", conditionService.getProductGroup(factoryName, div, productGroup));
	}

	@RequestMapping(value = "/getProduct", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProduct(@RequestParam String factoryName, 
								  @RequestParam String div,  
								  @RequestParam String startTime,
								  @RequestParam String endTime, 
								  @RequestParam String stepSeq,
								  @RequestParam String productGroup,
								  @RequestParam String product) {
		return ApiResponse.success("OK",
				conditionService.getProduct(factoryName, div, stepSeq, startTime, endTime, productGroup, product));
	}

	@RequestMapping(value = "/getProgram", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getProgram(@RequestParam String factoryName, 
								  @RequestParam String startTime,
								  @RequestParam String endTime, 
								  @RequestParam String div, 
								  @RequestParam String stepSeq,
								  @RequestParam String productGroup,
								  @RequestParam String product, 
								  @RequestParam String program) {
		return ApiResponse.success("OK",
				conditionService.getProgram(factoryName, div, stepSeq, startTime, endTime, productGroup, product, program));
	}

	@RequestMapping(value = "/getTarget", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getTarget(@RequestParam String factoryName, 
								 @RequestParam String startTime,
								 @RequestParam String endTime, 
								 @RequestParam String stepSeq, 
								 @RequestParam String div, 
								 @RequestParam String productGroup,
								 @RequestParam String product, 
								 @RequestParam String program, 
								 @RequestParam String target) {
		return ApiResponse.success("OK", conditionService.getTarget(factoryName, div, stepSeq, startTime, endTime, productGroup,
				product, program, target));
	}

	@RequestMapping(value = "/getChipSpec", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getChipSpec(@RequestParam String factoryName, 
								   @RequestParam String startTime,
								   @RequestParam String endTime, 
								   @RequestParam String div, 
								   @RequestParam String stepSeq, 
								   @RequestParam String productGroup,
								   @RequestParam String product, 
								   @RequestParam String program, 
								   @RequestParam String target, 
								   @RequestParam String chipSpec) {
		return ApiResponse.success("OK", conditionService.getChipSpec(factoryName, div, stepSeq, startTime, endTime,
				productGroup, product, program, target, chipSpec));
	}

	@RequestMapping(value = "/getFrameName", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getFrameName(@RequestParam String factoryName, 
									@RequestParam String startTime,
									@RequestParam String endTime, 
									@RequestParam String div, 
									@RequestParam String stepSeq, 
									@RequestParam String productGroup,
									@RequestParam String product, 
									@RequestParam String program, 
									@RequestParam String target,
									@RequestParam String chipSpec,
									@RequestParam String frameName) {
		return ApiResponse.success("OK",
				conditionService.getFrameName(factoryName, div, stepSeq, startTime, endTime, productGroup, product, program, target, chipSpec, frameName));
	}

	@RequestMapping(value = "/getLotId", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getLotId(@RequestParam String factoryName, 
								@RequestParam String startTime,
								@RequestParam String endTime, 
								@RequestParam String div, 
								@RequestParam String stepSeq, 
								@RequestParam String productGroup,
								@RequestParam String product, 
								@RequestParam String program, 
								@RequestParam String target, 
								@RequestParam String chipSpec,
								@RequestParam String frameName, 
								@RequestParam String lotId) {
		return ApiResponse.success("OK", conditionService.getLotIds(factoryName, div, stepSeq, startTime, endTime, productGroup,
				product, program, target, chipSpec, frameName, lotId));
	}

}
