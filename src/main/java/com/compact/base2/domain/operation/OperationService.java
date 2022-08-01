package com.compact.base2.domain.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.base.common.api.ApiResponse;

@Service
public class OperationService {
	
	@Autowired
	OperationMapper mapper;
	
	public List getProcessOperation() {
		return mapper.getProcessOperation();
	}
	
	public ApiResponse addProcessOperation(String insertSql) {
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("insertSql", insertSql);
		mapper.addProcessOperation(parameter);
		return ApiResponse.success("Ok");
		
	}
	
}
