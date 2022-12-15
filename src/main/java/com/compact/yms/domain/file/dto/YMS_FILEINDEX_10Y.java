package com.compact.yms.domain.file.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class YMS_FILEINDEX_10Y {
	
	String fmKey;
	
	String subFmKey;
	
	String lotId;
	
	String seq;
	
	String filename;
	
	String lotName;
	
	String waferID;
	
	String stepSeq;
	
	String stepType;
	
	String tkInTime;
	
	String tkOutTime;
	
	String eqpID;
	
	String div;
	
	String productGroup;
	
	String productSpecName;
	
	String program;
	
	String path;
	
	String remoteFileName;
	
	String status;
	
	String errMsg;
	
	BigDecimal bytes;
	
}
