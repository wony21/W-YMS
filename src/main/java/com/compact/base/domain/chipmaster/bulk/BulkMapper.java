package com.compact.base.domain.chipmaster.bulk;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BulkMapper {
	
	List<DropdownItem> getCommonCode(Map<String, Object> parameter);
	
	List<DropdownItem> getLine(Map<String, Object> parameter);
	
	List<DropdownItem> getProductGroup(Map<String, Object> parameter);
	
	List<DropdownItem> getDeployStatus(Map<String, Object> parameter);

}
