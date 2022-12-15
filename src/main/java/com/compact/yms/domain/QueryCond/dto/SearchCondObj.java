package com.compact.yms.domain.QueryCond.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SearchCondObj {

	@JsonProperty
	String display;
	
	@JsonProperty
	String value;
	
}
