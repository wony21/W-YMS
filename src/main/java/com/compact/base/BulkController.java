package com.compact.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.base.common.BaseController;
import com.compact.base.domain.chipmaster.bulk.BulkService;
import com.compact.base.domain.chipmaster.bulk.DropdownApiResponse;
import com.compact.base.domain.chipmaster.bulk.DropdownItem;

@Controller
@RequestMapping("/api/cm")
public class BulkController extends BaseController {

	@Autowired
	public BulkService bulkService;
	
	
	@RequestMapping(value="/getReqSite", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public DropdownApiResponse getReqSiteList() {
		List<DropdownItem> list = bulkService.getReqSite();
		DropdownApiResponse response = new DropdownApiResponse(true, list);
		return response;
	}
	
	@RequestMapping(value="/getLine", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public DropdownApiResponse getLine() {
		List<DropdownItem> list = bulkService.getLine();
		DropdownApiResponse response = new DropdownApiResponse(true, list);
		return response;
	}
	
	@RequestMapping(value="/getProductGroup", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public DropdownApiResponse getProductGroup(@RequestParam(required = false) String line) {
		List<DropdownItem> list = bulkService.getProductGroup(line);
		DropdownApiResponse response = new DropdownApiResponse(true, list);
		return response;
	}
	
	@RequestMapping(value="/getDeployStatus", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public DropdownApiResponse getStatusCode() {
		List<DropdownItem> list = bulkService.getDeployStatus();
		DropdownApiResponse response = new DropdownApiResponse(true, list);
		return response;
	}

}
