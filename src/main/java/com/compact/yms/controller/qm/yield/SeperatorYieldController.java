package com.compact.yms.controller.qm.yield;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.qm.yield.seperator.SeperatorYieldService;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldChartDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/qm/yield/seperator")
public class SeperatorYieldController extends BaseController {

	@Autowired
	SeperatorYieldService service;

	@RequestMapping(value = "/data", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getSperatorYield(@RequestParam(required = true) String factoryName,
			@RequestParam(required = true) String startDate, 
			@RequestParam(required = true) String endDate, 
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

		SYieldChartDTO data = service.getSeperatorYield(factoryName, startDate, endDate, div, productSpecGroup, productSpecName,
				vendor, model, machine, program, target, chipSpec, frameName, pl, opt1, opt2, opt3);

		return ApiResponse.success("OK", "SYieldData", data);
	}
}
