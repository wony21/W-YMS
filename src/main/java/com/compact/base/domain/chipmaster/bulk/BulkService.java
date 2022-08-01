package com.compact.base.domain.chipmaster.bulk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkService {
	
	@Autowired
	BulkMapper bulkMapper;
	
	public List<DropdownItem> getReqSite() {
		return getCommonCode("REQSITE");
	}
	
	public List<DropdownItem> getLine() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		return bulkMapper.getLine(parameter);
	}
	
	public List<DropdownItem> getProductGroup(String line) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("line", line);
		return bulkMapper.getProductGroup(parameter);
	}
	
	public List<DropdownItem> getCommonCode(String groupCode) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("groupCode", groupCode);
		return bulkMapper.getCommonCode(parameter);
	} 
	
	public List<DropdownItem> getDeployStatus() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		return bulkMapper.getDeployStatus(parameter);
	}
	
}
