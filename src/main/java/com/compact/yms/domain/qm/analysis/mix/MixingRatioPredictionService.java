package com.compact.yms.domain.qm.analysis.mix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.common.BaseService;

@Service
public class MixingRatioPredictionService extends BaseService {
	
	@Autowired
	MixingRatioPredictionMapper mapper;
	
	public List getMixRawdataCieX(String factoryName, String stepSeq, String productionType,
								  String startDate, String endDate,
								  String div, String productSpecGroup, String productSpecName,
								  String target,
								  String lotIds, String lotText) {
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		parameter.put("productSpecGroup", productSpecGroup);
		parameter.put("productSpecName", productSpecName);
		parameter.put("target", target);
		setArrayParam(parameter, "lotIds", lotIds);
		parameter.put("lotText", lotText);
		parameter.put("cie", "CIE_X");
		return mapper.getMixRawDataCIE(parameter);
	}
	
	public List getMixRawdataCieY(String factoryName, String stepSeq, String productionType,
								  String startDate, String endDate,
								  String div, String productSpecGroup, String productSpecName,
								  String target,
								  String lotIds, String lotText) {

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("factoryName", factoryName);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productionType", productionType);
		parameter.put("div", div);
		parameter.put("productSpecGroup", productSpecGroup);
		parameter.put("productSpecName", productSpecName);
		parameter.put("target", target);
		setArrayParam(parameter, "lotIds", lotIds);
		parameter.put("lotText", lotText);
		parameter.put("cie", "CIE_Y");
		return mapper.getMixRawDataCIE(parameter);
	}
	
	public List getLots(String stepSeq, String startDate, String endDate, String productSpecName, String target) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		parameter.put("productSpecName", productSpecName);
		parameter.put("target", target);
		return mapper.getLots(parameter);
	}
	
	

}
