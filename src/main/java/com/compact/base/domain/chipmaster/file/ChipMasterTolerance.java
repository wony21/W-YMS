package com.compact.base.domain.chipmaster.file;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class ChipMasterTolerance {
	
	public String masterNo;
	
	public String cieX;
	
	public String cieY;
	
	public String flux;
	
	public String vf;
	
	public String wd;
	
	public String wp;
	
	public String cri;
	
	public String userId;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
