package com.compact.base.domain.analysis.DTO;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class YieldFileData {

	String lotName;
	
	String seq;
	
	String fileName;
	
	List<YieldFileItemData> rawData;
	
	
}
