package com.compact.yms.controller.qm.analysis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.qm.analysis.mix.MixingRatioPredictionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/qm/analysis/mixing")
public class MixingRatioPredictionController extends BaseController {

	@Autowired
	MixingRatioPredictionService service;

	@RequestMapping(value = "/data/cieX", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getMixCieRawDataCIE_X(@RequestParam(required = true) String factoryName,
											@RequestParam(required = true) String startDate, 
											@RequestParam(required = true) String endDate,
											@RequestParam(required = false) String stepSeq,
											@RequestParam(required = false) String productionType,
											@RequestParam(required = false) String div,
											@RequestParam(required = false) String productSpecGroup,
											@RequestParam(required = false) String productSpecName, 
											@RequestParam(required = false) String target,
											@RequestParam(required = false) String lotIds,
											@RequestParam(required = false) String lotText) {

		List data = service.
						getMixRawdataCieX(factoryName, stepSeq, productionType, startDate, endDate, div, productSpecGroup, productSpecName, target, lotIds, lotText);

		return ApiResponse.success("OK", "MixingRawdata", data);
	}
	
	@RequestMapping(value = "/data/cieY", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getMixCieRawDataCIE_Y(@RequestParam(required = true) String factoryName,
											@RequestParam(required = true) String startDate, 
											@RequestParam(required = true) String endDate,
											@RequestParam(required = false) String stepSeq,
											@RequestParam(required = false) String productionType,
											@RequestParam(required = false) String div,
											@RequestParam(required = false) String productSpecGroup,
											@RequestParam(required = false) String productSpecName, 
											@RequestParam(required = false) String target,
											@RequestParam(required = false) String lotIds,
											@RequestParam(required = false) String lotText) {

		List data = service.
						getMixRawdataCieY(factoryName, stepSeq, productionType, startDate, endDate, div, productSpecGroup, productSpecName, target, lotIds, lotText);

		return ApiResponse.success("OK", "MixingRawdata", data);
	}
	
	@RequestMapping(value = "/lots", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse getMixLots(@RequestParam(required = true) String startDate, 
											@RequestParam(required = true) String endDate,
											@RequestParam(required = false) String stepSeq,
											@RequestParam(required = false) String div,
											@RequestParam(required = false) String productSpecName, 
											@RequestParam(required = false) String target) {

		List data = service.getLots(stepSeq, startDate, endDate, productSpecName, target);

		return ApiResponse.success("OK", "data", data);
	}
}
