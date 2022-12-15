package com.compact.yms.domain.analysis.DTO;

import java.awt.Polygon;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChartShareBothData {

	String cieName;
	
	Integer sort;
	
	Long chipCount;
	
	Double chipPercent;
	
	@JsonIgnore
	Polygon cieRectangle;
	
	List<ChartShareData> range;
	
}
