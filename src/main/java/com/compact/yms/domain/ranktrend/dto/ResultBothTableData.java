package com.compact.yms.domain.ranktrend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResultBothTableData {

	@JsonProperty(index = 0)
	String factoryName;
	@JsonProperty(index = 1)
	String lotId;
	@JsonProperty(index = 2)
	String stepSeq;
	@JsonProperty(index = 3)
	String tkInTime;
	@JsonProperty(index = 4)
	String tkOutTime;
	@JsonProperty(index = 5)
	String eqpId;
	@JsonProperty(index = 6)
	String div;
	@JsonProperty(index = 7)
	String productSpecGroup;
	@JsonProperty(index = 8)
	String productSpecName;
	@JsonProperty(index = 9)
	String program;
	@JsonProperty(index = 10)
	String lotType;
	@JsonProperty(index = 11)
	String target;
	@JsonProperty(index = 12)
	String chipSpec;
	@JsonProperty(index = 13)
	String chipName;
	@JsonProperty(index = 14)
	String intensity;
	@JsonProperty(index = 15)
	String pl;
	@JsonProperty(index = 16)
	String moldMachineName;
	@JsonProperty(index = 17)
	String moldEventUser;
	@JsonProperty(index = 18)
	String afcMachineName;
	@JsonProperty(index = 19)
	String afcEventUser;
	@JsonProperty(index = 20)
	String frameName;
	@JsonProperty(index = 21)
	String totalProportion;
	@JsonProperty(index = 22)
	String ratio1;
	@JsonProperty(index = 23)
	String phospor1Name;
	@JsonProperty(index = 24)
	String ratio2;
	@JsonProperty(index = 25)
	String phospor2Name;
	@JsonProperty(index = 26)
	String ratio3;
	@JsonProperty(index = 27)
	String phospor3Name;
	@JsonProperty(index = 32)
	String type;
	@JsonProperty(index = 33)
	String itemId;
	@JsonProperty(index = 34)
	String measureId;
	@JsonProperty(index = 35)
	String dataValue;
	@JsonIgnore
	String remoteFileName;
	
}
