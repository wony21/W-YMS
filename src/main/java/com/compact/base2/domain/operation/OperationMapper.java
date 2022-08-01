package com.compact.base2.domain.operation;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationMapper {
	
	public List getProcessOperation();
	public void addProcessOperation(Map<String, Object> parameter);
	
}

