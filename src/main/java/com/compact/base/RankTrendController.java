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
import com.compact.base.domain.ranktrend.RankTrendService;

@Controller
@RequestMapping("/ranktrend")
public class RankTrendController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RankTrendController.class);

	@Autowired
	RankTrendService rankTrendService;

	@RequestMapping(value = "/anay/seperator", method = RequestMethod.GET, produces = APPLICATION_JSON)
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
			@RequestParam(required = false) String lotId, 
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String opt1, 
			@RequestParam(required = true) String filter) {

		List data = rankTrendService.getRankTendOfSeperator(factoryName, startDate, endDate, div, stepSeq,
				productionType, productSpecGroup, productSpecName, program, target, chipSpec, intensity, pl, frameName,
				subFrameName, lotId, type, opt1, filter);

		return ApiResponse.success("ok", data);
	}
	
	@RequestMapping(value = "/codelist", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getRankTendOfSeperator(@RequestParam(required = true) String groupCode) {

		List data = rankTrendService.getPopCodeList(groupCode);
		return ApiResponse.success("ok", data);
	}
	
	
}
