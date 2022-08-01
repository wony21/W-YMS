package com.compact.base.domain.chipmaster.file;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class ChipMasterMeasureInfo {
	
	public String masterNo;
	
	public String measureEqpId;
	
	public String measureJIG;
	
	public String temperature;
	
	public String measureIF;
	
	public String calibration;
	
	public String itTime;
	
	public String filter;
	
	public String vf;
	
	public String vfEqp;
	
	public String userId;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
