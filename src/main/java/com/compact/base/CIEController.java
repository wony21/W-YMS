package com.compact.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.base.common.api.ApiResponse;
import com.compact.base.domain.CIE.CIEService;
import com.compact.base.domain.CIE.dto.CompareResultObject;

@Controller
@RequestMapping("/cie")
public class CIEController {
	
	private static final Logger logger = LoggerFactory.getLogger(CIEController.class);
	
	@Autowired
	private CIEService cieService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String CIEPage() {
		return "cie/CIE_V1";
	}
	
	@RequestMapping(value = "/xy", method = RequestMethod.GET)
	public String CIEXYPage() {
		return "cie/CIE_XY_V1";
	}
	
	@RequestMapping(value = "/getlist", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getlist(@RequestParam String startDate, 
							   @RequestParam String endDate,
							   @RequestParam String partId, 
							   @RequestParam String equipmentId) {
		return ApiResponse.success("OK", cieService.getFileM(startDate, endDate, partId, equipmentId));
	}
	
	@RequestMapping(value = "/compareProgramName", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getProgramNames(@RequestParam String lotName, 
									   @RequestParam String partId,
									   @RequestParam String equipmentId, 
									   @RequestParam String program) {
		String[] arrLotName = lotName.split("-");
		if ( arrLotName.length != 4) {
			return ApiResponse.error("Require lotname length 4, seperator charictor is '-'.");
		}
		String lotNo = String.format("%s-%s-%s", arrLotName[0], arrLotName[1], arrLotName[2]);
		String trayNo = arrLotName[3];
		
		CompareResultObject resultObj = new CompareResultObject();
		resultObj.setLotName(lotName);
		resultObj.setOrgProgram(program);
		resultObj.setPrograms(cieService.comapreProgram(lotNo, trayNo, partId, equipmentId, program));
		return ApiResponse.success("OK", resultObj);
	}
	
	@RequestMapping(value = "/getSeperatorCIE", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getSeperatorCIE(@RequestParam String partId, 
									   @RequestParam String program) {
		return ApiResponse.success("OK", cieService.getSeperatorCie(partId, program));
	}
	
	@RequestMapping(value = "/getCieRawdata", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getCieRawdata(@RequestParam String lotName,
									 @RequestParam String partId,
									 @RequestParam String program) {
		return ApiResponse.success("OK", cieService.getFileDClob(lotName, partId, program));
	}
}
