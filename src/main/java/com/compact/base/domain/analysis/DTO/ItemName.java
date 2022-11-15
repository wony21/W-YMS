package com.compact.base.domain.analysis.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ItemName {

	@JsonProperty
	String itemName;
}
