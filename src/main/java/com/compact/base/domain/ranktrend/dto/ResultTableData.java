package com.compact.base.domain.ranktrend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResultTableData {

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
	String frameName;
	@JsonProperty(index = 17)
	String moldMachineName;
	@JsonProperty(index = 18)
	String moldEventUser;
	@JsonProperty(index = 19)
	String totalProportion;
	@JsonProperty(index = 20)
	String ratio1;
	@JsonProperty(index = 21)
	String phospor1Name;
	@JsonProperty(index = 22)
	String ratio2;
	@JsonProperty(index = 23)
	String phospor2Name;
	@JsonProperty(index = 24)
	String ratio3;
	@JsonProperty(index = 25)
	String phospor3Name;
	@JsonProperty(index = 26)
	String tQty;
	@JsonProperty(index = 27)
	String gQty;
	@JsonProperty(index = 28)
	String bQty;
	@JsonProperty(index = 29)
	String sLotNo;
	@JsonProperty(index = 30)
	String itemId;
	@JsonProperty(index = 31)
	String yield;
	@JsonProperty(index = 32)
	String afcMachineName;
	@JsonProperty(index = 33)
	String afcEventUser;
	@JsonProperty(index = 34)
	String nmMachineName;
	@JsonProperty(index = 35)
	String nmEventUser;
	@JsonProperty(index = 36)
	String bgMachineName;
	@JsonProperty(index = 37)
	String bgEventUser;
	@JsonProperty(index = 38)
	String bEquip;
	@JsonProperty(index = 39)
	String okNgDiv;
	@JsonProperty(index = 40)
	String avgValue;
	@JsonProperty(index = 41)
	String minValue;
	@JsonProperty(index = 42)
	String maxValue;
	@JsonProperty(index = 43)
	String maxMinValue;
	@JsonProperty(index = 44)
	String xStd;
	@JsonProperty(index = 45)
	String xAvg;
	@JsonProperty(index = 46)
	String yStd;
	@JsonProperty(index = 47)
	String yAvg;
	@JsonProperty(index = 48)
	String ivStd;
	@JsonProperty(index = 49)
	String ivAvg;
	@JsonProperty(index = 50)
	String vfStd;
	@JsonProperty(index = 51)
	String vfAvg;
	@JsonProperty(index = 52)
	String lmpStd;
	@JsonProperty(index = 53)
	String lmpAvg;
	@JsonProperty(index = 54)
	String lmdStd;
	@JsonProperty(index = 55)
	String lmdAvg;
	@JsonProperty(index = 56)
	String criStd;
	@JsonProperty(index = 57)
	String cirAvg;
	@JsonProperty(index = 58)
	String r9Std;
	@JsonProperty(index = 59)
	String r9Avg;
	@JsonProperty(index = 60)
	String dataValue;
}
