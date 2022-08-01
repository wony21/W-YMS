package com.compact.base.domain.analysis.DTO;

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
	
	double min;
	
	double max;
	
	String itemName;
	
	Long chipCount;
	
	BigDecimal chipPercent;
	
}
