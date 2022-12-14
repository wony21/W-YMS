package com.compact.yms.domain.analysis.DTO;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TableShareData {

	String stepSeq;
	
	String lotId;
	
	String tkInTime;
	
	String tkOutTime;
	
	String eqpId;
	
	String partId;
	
	String empNo;
	
	String program;
	
	String lotType;
	
	String chipSpec;
	
	String leadFrame;
	
	String target;
	
	String moEqp;
	
	Long tChipCnt;
	
	String fmKey;
	
	String itemId;
	
	String groupName;
	
	String subGroupName;
	
	String range;
	
	Long chipCnt;
	
	Double chipPer;
	
	Long value;
	
}
