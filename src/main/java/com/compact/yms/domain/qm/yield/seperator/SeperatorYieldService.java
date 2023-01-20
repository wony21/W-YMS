package com.compact.yms.domain.qm.yield.seperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.common.BaseService;
import com.compact.yms.domain.qm.yield.seperator.model.SYieldChartDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeperatorYieldService extends BaseService {

	@Autowired
	SeperatorYieldMapper mapper;

	public SYieldChartDTO getSeperatorYield(
			String factoryName, String startDate, String endDate, String div, String productSpecGroup,
			String productSpecName, String vendor, String model, String machineName, String program, String target,
			String chipSpec, String frameName, String pl, String opt1, String opt2, String opt3
	) {

		SYieldChartDTO sepeYieldData = new SYieldChartDTO();
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", "TT");
		// parameter.put("productionType", productionType);
		parameter.put("div", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "vendors", vendor);
		setArrayParam(parameter, "models", model);
		setArrayParam(parameter, "machineNames", machineName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		
		sepeYieldData.setLabel(mapper.getChartLabel(parameter));
		sepeYieldData.setYield(mapper.getSeperatorYield(parameter));
		
		return sepeYieldData;
	}

}
