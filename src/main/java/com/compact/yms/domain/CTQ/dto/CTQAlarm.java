package com.compact.yms.domain.CTQ.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.function.Power;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CTQAlarm {
	
	String rowStamp;

	String site;

	String testProv;

	String testLoc;

	String item1;

	String item2;
	
	String div;
	
	String productSpecGroup;

	String productSpecName;

	String productSpecDesc;

	String chipName;

	String equip;

	Double SU;

	Double SL;

	Integer n;

	Double variance;

	Double groupStd;

	Double stdev;

	Double average;

	Double range;

	Double value1;

	Double value2;

	Double value3;

	Double value4;

	Double value5;

	Double value6;

	Double value7;

	Double value8;

	Double value9;

	Double value10;

	Double value11;

	Double value12;

	Double value13;

	Double value14;

	Double value15;

	Double value16;

	Double value17;

	Double value18;

	Double value19;

	Double value20;

	Double value21;

	Double value22;

	Double value23;

	Double value24;

	Double value25;

	Double value26;

	Double value27;

	Double value28;

	Double value29;

	Double value30;

	private List<Double> dataList = new ArrayList<>();

	private void createValueList() {
		if (dataList == null) {
			dataList = new ArrayList<>();
		}
		
		Double[] array = { value1, value2, value3, value4, value5, value6, value7, value8, value9,
				value11, value12, value12, value13, value14, value15, value16, value17, value18, value19, value20,
				value21, value22, value23, value24, value25, value26, value27, value28, value29, value30 };
		dataList = Arrays.asList(array);
	}

	/*
	 * 평균, 범위, 분산, 표준편차을 구한다.
	 */
	public void calculator() {
		// log.info("CTQ Row Calculator Started.");
		createValueList();
		
		double[] dataValues = dataList.stream().filter(x -> x != 0d).mapToDouble(x -> x).toArray();
		SummaryStatistics stats = new SummaryStatistics();
		for (double d : dataValues) {
			if (d != 0d)
				stats.addValue(d);
		}
		// 평균
		Double mean = stats.getMean();
		this.setAverage(mean);
		// log.info("AVG[{}]", this.getAverage());
		// 범위
		Double min = stats.getMin();
		Double max = stats.getMax();
		Double range = max - min;
		this.setRange(range);
		// log.info("RANGE[{}]", this.getRange());
		// 분산
		Double variance = stats.getVariance();
		this.setVariance(variance);
		// log.info("VARIANCE[{}]", variance);
		// 표준편차(전체)
		Double standardDev = stats.getStandardDeviation();
		this.setStdev(standardDev);
		// log.info("STDEV[{}]", standardDev);
		// 표준편차(군내)
		Power pow = new Power(0.5);
		Double groupStdev = pow.value(standardDev);
		this.setGroupStd(groupStdev);
		
		// log.info("GSTDEV[{}]", groupStdev);

		// log.info("{},{},{},{},{}", new Object[] { this.getAverage(), this.getRange(),
		// this.getVariance(), this.getStdev(), this.getGroupStd() });

	}

	
}
