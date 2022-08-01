package com.compact.base.domain.CIE.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FileClobObject {
	
	public String fmKey;

	public String itemId;
	
	public String dataType;
	
	public String flag;
	
	public String mdata;
}
