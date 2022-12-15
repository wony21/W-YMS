package com.compact.yms.domain.analysis.DTO;

import java.io.File;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class YieldFileInfoData {
	
	String lotName;
	
	String seq;
	
	String fileName;
	
	File file;

}
