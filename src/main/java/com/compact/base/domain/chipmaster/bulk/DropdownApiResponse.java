package com.compact.base.domain.chipmaster.bulk;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DropdownApiResponse {
	
	@JsonProperty
	public Boolean success;
	
	@JsonProperty
	public List<DropdownItem> results;
	
	public DropdownApiResponse(Boolean status, List<DropdownItem> results) {
		this.success = status;
		this.results = results;
	}

}
