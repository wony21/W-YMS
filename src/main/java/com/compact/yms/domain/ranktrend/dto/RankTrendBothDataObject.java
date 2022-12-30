package com.compact.yms.domain.ranktrend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RankTrendBothDataObject {

	String measureItemName;
	
	String itemName;
	
	Double dataValue;
	
}
