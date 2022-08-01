package com.compact.base.domain.CIE.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CompareResultObject {

	@JsonProperty
	public String lotName;
	
	@JsonProperty
	public String orgProgram;
	
	@JsonProperty
	public List programs;
	
}
