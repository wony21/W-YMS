package com.compact.yms.domain.analysis.DTO;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import math.geom2d.polygon.SimplePolygon2D;

@Data
@Getter
@Setter
public class ChartShareCieData {

	String groupId;

	String cieName;

	String x1;

	String y1;

	String x2;

	String y2;
	
	String x3;

	String y3;
	
	String x4;

	String y4;

	@JsonIgnore
	Polygon cieRectangle;

	Long chipCount;

	Double chipPercent;
}
