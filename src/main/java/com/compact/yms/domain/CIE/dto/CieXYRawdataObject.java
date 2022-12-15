package com.compact.yms.domain.CIE.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CieXYRawdataObject {
	
	@JsonProperty
	public String fmKey;
	
	@JsonProperty
	public String binItemName;
	
	@JsonProperty
	public String cieXName;
	
	@JsonProperty
	public String cieYName;
	
	@JsonProperty
	public List<String> binNo;
	
	@JsonProperty
	public List<String> binName;
	
	@JsonProperty
	public List<Double> cieX;
	
	@JsonProperty
	public List<Double> cieY;
	
}
