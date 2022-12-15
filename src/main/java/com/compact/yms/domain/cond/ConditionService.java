package com.compact.yms.domain.cond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {

	private static final Logger logger = LoggerFactory.getLogger(ConditionService.class);

	@Autowired
	ConditionMapper mapper;

	public List getSite(String factoryName) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);

		return mapper.getSite(param);
	}

	public List getProductType() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.getProductType(param);
	}

	public List getLine(String factoryName, String div) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		/* div array */
		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		return mapper.getLine(param);
	}

	public List getStep(String factoryName) {
		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);

		return mapper.getStep(param);
	}

	public List getProductGroup(String factoryName, String div, String productGroup) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		/* div array */
		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		param.put("productSpecGroup", productGroup);

		return mapper.getProductSpecGroup(param);
	}

	public List getProduct(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		/* div array */
		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		/* array */
		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		param.put("productSpecName", product);

		return mapper.getProduct(param);
	}

	public List getProgram(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product, String program) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		String[] products = product.split(",");
		param.put("productSpecNames", product.isEmpty() ? null : products);

		param.put("program", program);

		return mapper.getProgram(param);
	}

	public List getTarget(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product, String program, String target) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		String[] products = product.split(",");
		param.put("productSpecNames", product.isEmpty() ? null : products);

		String[] programs = program.split(",");
		param.put("programs", program.isEmpty() ? null : programs);

		param.put("target", target);

		return mapper.getTarget(param);
	}

	public List getChipSpec(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product, String program, String target, String chipSpec) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		String[] products = product.split(",");
		param.put("productSpecNames", product.isEmpty() ? null : products);

		String[] programs = program.split(",");
		param.put("programs", program.isEmpty() ? null : programs);

		String[] targets = target.split(",");
		param.put("targets", target.isEmpty() ? null : targets);

		param.put("chipSpec", chipSpec);

		return mapper.getChipSpec(param);
	}

	public List getFrameName(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product, String program, String target, String chipSpec, String frameName) {

		Map<String, Object> param = new HashMap<String, Object>();
		/* single variant */
		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		String[] products = product.split(",");
		param.put("productSpecNames", product.isEmpty() ? null : products);

		String[] programs = program.split(",");
		param.put("programs", program.isEmpty() ? null : programs);

		String[] targets = target.split(",");
		param.put("targets", target.isEmpty() ? null : targets);

		param.put("frameName", frameName);

		return mapper.getFrameName(param);

	}

	public List getLotIds(String factoryName, String div, String stepSeq, String productionType, String startTime, String endTime,
			String productGroup, String product, String program, String target, String chipSpec, String frameName,
			String lotId) {

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("factoryName", factoryName);
		param.put("stepSeq", stepSeq);
		String[] productionTypes = productionType.split(",");
		param.put("productionTypes", productionType.isEmpty() ? null : productionTypes);
		param.put("startTime", startTime);
		param.put("endTime", endTime);

		String[] divs = div.split(",");
		param.put("divs", div.isEmpty() ? null : divs);

		String[] productGroups = productGroup.split(",");
		param.put("productSpecGroups", productGroup.isEmpty() ? null : productGroups);

		String[] products = product.split(",");
		param.put("productSpecNames", product.isEmpty() ? null : products);

		String[] programs = program.split(",");
		param.put("programs", program.isEmpty() ? null : programs);

		String[] targets = target.split(",");
		param.put("targets", target.isEmpty() ? null : targets);

		String[] chipSpecs = chipSpec.split(",");
		param.put("chipSpecs", chipSpec.isEmpty() ? null : chipSpecs);

		String[] frameNames = frameName.split(",");
		param.put("frameNames", frameName.isEmpty() ? null : frameNames);

		param.put("lotId", lotId);

		return mapper.getLots(param);

	}

}
