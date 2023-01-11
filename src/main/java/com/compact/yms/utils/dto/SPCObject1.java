package com.compact.yms.utils.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Power;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.compact.yms.domain.CTQ.dto.Factor;
import com.jcraft.jsch.Logger;
import com.mysql.cj.log.Log;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/*
 * CL, CPK 산출용 데이터
 */
@Slf4j
@Data
public class SPCObject1 {

	List<Double> dataValues;

	Long groupSize;

	Double mean;

	Double range;

	Double varience;

	Double groupStd;

	Double std;

	Double sl;

	Double su;

	Double ucl;

	Double cl;

	Double lcl;

	Double cpl;

	Double cpu;

	Double cpk;

	Factor factor;

	public void add(Double value) {
		if (dataValues == null)
			dataValues = new ArrayList<>();
		dataValues.add(value);
	}

	public void calculator() {

		if (dataValues == null)
			return;
		if (dataValues.isEmpty())
			return;

		SummaryStatistics stats = new SummaryStatistics();
		for (Double d : dataValues) {
			stats.addValue(d);
		}

		long n = stats.getN();
		this.setGroupSize(n);
		// log.info("groupsize");

		Double x = stats.getMean();
		this.setMean(x);
		// log.info("average");

		Double r = stats.getMax() - stats.getMin();
		this.setRange(r);
		// log.info("range");

		Double v = stats.getVariance();
		this.setVarience(v);
		// log.info("varience");

		Double std = stats.getStandardDeviation();
		this.setStd(std);
		// log.info("std");

		Power pow = new Power(0.5d);
		Double gStd = pow.value(v);
		this.setGroupStd(gStd);
		//log.info("gstd");
//		if (factor != null) {
//			Double ucl = x + (factor.getA2() * r);
//			Double lcl = x - (factor.getA2() * r);
//			Double cl = x;
//			this.setUcl(ucl);
//			this.setLcl(lcl);
//			this.setCl(cl);
//			//log.info("set cl");
//		}

//		Double sl = this.getSl();
//		Double su = this.getSu();
//
//		Double cpl = (x - sl) / (3 * gStd);
//		Double cpu = (su - x) / (3 * gStd);
//		Double cpk = Math.min(cpl, cpu);
//		this.setCpl(cpl);
//		this.setCpu(cpu);
//		this.setCpk(cpk);
		//log.info("set cpk");

	}

}
