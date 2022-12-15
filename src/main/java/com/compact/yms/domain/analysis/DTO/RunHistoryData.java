package com.compact.yms.domain.analysis.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RunHistoryData {

	String key;
	
	String factoryName;
	
	String div;
	
	String startDt;
	
	String endDt;
	
	String stepSeq;
	
	String lotType;
	
	String productSpecGroup;
	
	String productSpecName;
	
	String program;
	
	String target;
	
	String chipSpec;
	
	String frameName;
	
	String intensity;
	
	String pl;
	
	String lotId;
	
	String userId;
	
	String createTime;
	
	String bytes;
	
	String comments;
	
	String filePath;
	
}
