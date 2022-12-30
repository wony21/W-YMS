package com.compact.yms.domain.ranktrend.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RankTrendResultData {
	
	List chart;
	
	List table;
	
	Boolean error;
	
	String errorMessage;

}
