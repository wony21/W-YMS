package com.compact.yms.domain.CTQ.dto;

import java.util.List;

import lombok.Data;

@Data
public class CTQAlarmConfig {

	String cpk;
	
	String cl;
	
	String dataDays;
	
	String scheduleCycleHour;
	
	String alarmTrigger;
	
	String sendDailyAlarmMail;
	
	String checkStartHour;
	
	String checkStartMin;
	
	String allOfTestLoc;
	
	String allOfTestProv;
	
	String allOfProducts;
	
	String cpkRangeStart;
	
	String cpkRangeEnd;
	
	List testLocations;
	
	List testProvisions;
	
	List testProducts;
	
}
