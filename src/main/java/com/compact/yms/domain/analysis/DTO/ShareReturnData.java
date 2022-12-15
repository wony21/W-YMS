package com.compact.yms.domain.analysis.DTO;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ShareReturnData {
	
	List chart;
	
	List<TableShareData> table;
	
	Boolean error;
	
	String errorMessage;
	
}
