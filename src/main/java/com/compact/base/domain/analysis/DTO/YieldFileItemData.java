package com.compact.base.domain.analysis.DTO;

import java.awt.Point;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import math.geom2d.Point2D;

@Data
@Getter
@Setter
public class YieldFileItemData {

	String itemName;
	
	List<Double> itemValue;
	
	List<Point> itemPointValue;
	
	List<ShareDataObject> itemBothValue;
	
}
