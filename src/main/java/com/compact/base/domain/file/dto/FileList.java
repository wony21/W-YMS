package com.compact.base.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FileList {
	
	@JsonProperty
	public Boolean selected;
	
	@JsonProperty
	public String fmKey;
	
	@JsonProperty
	public String lotId;
	
	@JsonProperty
	public String slotNo;
	
	@JsonProperty
	public String div;
	
	@JsonProperty
	public String productSpecGroup;
	
	@JsonProperty
	public String partId;
	
	@JsonProperty
	public String program;
	
	@JsonProperty
	public String fileName;
	
	@JsonProperty
	public String stepSeq;
	
	@JsonProperty
	public String stepType;
	
	@JsonProperty
	public String tkInTime;
	
	@JsonProperty
	public String tkOutTime;
	
	@JsonProperty
	public String machineName;
	
	@JsonProperty
	public String eqpId;
	
	/*
	@JsonProperty
	public String lotType;
	*/
	
	@JsonProperty
	public String target;
	
	@JsonProperty
	public String chipSpec;
	
	@JsonProperty
	public String intensity;
	
	@JsonProperty
	public String pl;
	
	@JsonProperty
	public String frameName;
	
	@JsonProperty
	public String moldMachineName;
	

}
