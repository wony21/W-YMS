package com.compact.yms.domain.analysis.DTO;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChartShareData {

	String groupId;
	
	String rangeDescription;
	
	Integer sort;
	
	double min;
	
	double max;
	
	String itemName;
	
	Long chipCount;
	
	Double chipPercent;
	
}
