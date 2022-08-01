package com.compact.base.domain.CIE.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CieXYGraphDataObj {
	
	@JsonProperty
	public String binNo;
	@JsonProperty
	public String binName;
	@JsonProperty
	public List<Double> cieX;
	@JsonProperty
	public List<Double> cieY;
}
