package com.compact.base.domain.chipmaster.file;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class ChipMasterPartList {
	
	public String masterNo;
	
	public String type;
	
	public String chipName;
	
	public String wd;
	
	public String phosphor;
	
	public String leadFrame;
	
	public String userId;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
