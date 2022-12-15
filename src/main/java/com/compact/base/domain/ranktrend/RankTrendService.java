package com.compact.base.domain.ranktrend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.base.common.BaseService;
import com.compact.base2.domain.operation.OperationMapper;
import com.compact.base2.domain.operation.OperationService;

@Service
public class RankTrendService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(RankTrendService.class);

	@Autowired
	RankTrendMapper mapper;
	
	@Autowired
	OperationMapper popMapper;
	
	
	public List getRankTendOfSeperator(String factoryName, String startDate, String endDate, String div, String stepSeq,
			String productionType, String productSpecGroup, String productSpecName, String program, String target,
			String chipSpec, String intensity, String pl, String frameName, String subFrameName, String lotId,
			String type, String opt1, String filters) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "intensities", intensity);
		setArrayParam(parameter, "pls", pl);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "subFrameNames", subFrameName);
		setArrayParam(parameter, "lotIds", lotId);
		parameter.put("type", type);
		parameter.put("opt1", opt1);
		setArrayParam(parameter, "filters", filters);
		return mapper.getRankTrendOfSeperator(parameter);
	}
	
	public List getPopCodeList(String groupCode) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("groupCd", groupCode);
		return popMapper.getCodeList(parameter);
	}

}
