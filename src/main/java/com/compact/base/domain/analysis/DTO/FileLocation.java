package com.compact.base.domain.analysis.DTO;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class FileLocation {
	
	String lotId;
	
	String seq;
	
	String remoteFileName;
	
	String fileName;
	
	String stepSeq;
	
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
	
	Double tChipCnt;
	
	String fmKey;
	
	String itemId;
	
	File file;
	
}
