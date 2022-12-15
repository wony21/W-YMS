package com.compact.yms.domain.chipmaster.file;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChipMasterM {

	@JsonProperty
	public String masterNo;
	
	@JsonProperty
	public String masterNm;
	
	@JsonProperty
	public String target1;
	
	@JsonProperty
	public String target2;

	@JsonProperty
	public String productSpecCode;
	
	@JsonProperty
	public String productSpecName;
	
	@JsonProperty
	public String masterType;
	
	@JsonProperty
	public int revNo;
	
	@JsonProperty
	public String div;
	
	@JsonProperty
	public String productSpecGroup;
	
	@JsonProperty
	public String status;
	
	@JsonProperty
	public String exchange;
	
	@JsonProperty
	public String extension;
	
	@JsonProperty
	public String documentNo;
	
	@JsonProperty
	public String revDate;
	
	@JsonProperty
	public String filename;
	
	@JsonProperty
	public String subKey;
	
	@JsonProperty
	public int regType;
	
	@JsonProperty
	public String reqSite;
	
	@JsonProperty
	public String verify;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}
	
	
}
