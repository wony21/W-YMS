package com.compact.yms.domain.qm.yield.seperator.model;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class SYieldCieDTO {

	Boolean error;
	
	String errorMessage;
	
	// 점유율 
	List widght;
	
	List subWidght;
	
	// 분류기준서 좌표
	List programCie;
	// Cie X, Y
	List<SeperatorCieDTO> cieData;
	
	/**
	 * 참조 데이터
	 * SORTER
	 * PIG
	 */
	String subCieType;
	//
	List<SeperatorCieDTO> subCieData;
	
	// 원본데이터
	String rawData;
		
	String subRawData;
}
