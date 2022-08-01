package com.compact.base.domain.chipmaster.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DropdownItem {
	
	@JsonProperty
	public String name;
	
	@JsonProperty
	public String value;
	
	@JsonProperty
	public String text;
	
}
