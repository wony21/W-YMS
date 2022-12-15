package com.compact.yms.domain.file.dto;

import java.io.File;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class FileResponse {
	
	Boolean IsError;
	
	String ErrorMsg;
	
	String orgFileName;
	
	File file;
	
}
