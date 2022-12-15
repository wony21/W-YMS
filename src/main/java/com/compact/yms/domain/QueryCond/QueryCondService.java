package com.compact.yms.domain.QueryCond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.common.BaseService;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.QueryCond.dto.SearchCondObj;
import com.jcraft.jsch.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QueryCondService extends BaseService {

	@Autowired
	QueryCondMapper mapper;

	public void preSettingList(List<SearchCondObj> list) {
		if (list.size() == 0) {
			SearchCondObj obj = new SearchCondObj();
			obj.setDisplay("ALL");
			obj.setValue("ALL");
			list.add(obj);
		}
	}

	/*****************************************************************
	 * 장기 데이터용 파일 조회 조건 / 2022.10.25
	 *****************************************************************/
	public ApiResponse getStepSeq(String startDate, String endDate, String stepSeq) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("stepSeq", stepSeq);
		List<SearchCondObj> list = mapper.getStepSeq(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getStepType(String startDate, String endDate, String stepSeq, String stepType) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		parameter.put("stepType", stepType);
		List<SearchCondObj> list = mapper.getStepType(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getSite() {
		Map<String, Object> parameter = new HashMap<String, Object>();
		List<SearchCondObj> list = mapper.getSite(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getLine(String startDate, String endDate, String stepSeq, String site) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "sites", site);
		List<SearchCondObj> list = mapper.getLine(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getProductSpecGroup(String startDate, String endDate, String stepSeq, String site, String line, String productSpecGroup) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		//setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		parameter.put("productSpecGroup", productSpecGroup);
		List<SearchCondObj> list = mapper.getProductSpecGroup(parameter);
		//preSettingList(list);
		return ApiResponse.success("OK", list);
	}
	
	public ApiResponse getProductSpecName(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		log.info("ProductionType:" + productionType);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		//setArrayParam(parameter, "productSpecNames", productSpecName);
		parameter.put("productSpecName", productSpecName);
		List<SearchCondObj> list = mapper.getProductSpecName(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}
	
	public ApiResponse getProductSpecNameTypeA(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		//setArrayParam(parameter, "productSpecNames", productSpecName);
		parameter.put("productSpecName", productSpecName);
		setArrayParam(parameter, "opt", "1");
		List<SearchCondObj> list = mapper.getProductSpecName(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getLOTID(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lot) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotId", lot);
		List<SearchCondObj> list = mapper.getLOTID(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getMachine(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		//setArrayParam(parameter, "machines", machine);
		parameter.put("machine", machine);
		List<SearchCondObj> list = mapper.getMachine(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getProgram(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine, String program) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		//setArrayParam(parameter, "programs", program);
		parameter.put("program", program);
		List<SearchCondObj> list = mapper.getProgram(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getTarget(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		//setArrayParam(parameter, "targets", target);
		parameter.put("target", target);
		List<SearchCondObj> list = mapper.getTarget(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getChipSpec(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target, String chipSpec) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		//setArrayParam(parameter, "chipSpecs", chipSpec);
		parameter.put("chipSpec", chipSpec);
		List<SearchCondObj> list = mapper.getChipSpec(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getFrameName(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target, String chipSpec, String frameName) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		//setArrayParam(parameter, "frameNames", frameName);
		parameter.put("frameName", frameName);
		List<SearchCondObj> list = mapper.getFrameName(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

	public ApiResponse getPL(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target, String chipSpec, String frameName, String pl) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		//setArrayParam(parameter, "pls", pl);
		parameter.put("pl", pl);
		List<SearchCondObj> list = mapper.getPL(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}
	
	public ApiResponse getLOTID(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target, String chipSpec, String frameName, String pl) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		setArrayParam(parameter, "chipSpecs", chipSpec);
		setArrayParam(parameter, "frameNames", frameName);
		setArrayParam(parameter, "pls", pl);
		//setArrayParam(parameter, "lotIds", lots);
		parameter.put("lotText", lotText);
		List<SearchCondObj> list = mapper.getLOTID(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}
	
	// getReelBatchID
	public ApiResponse getReelBatchID(String startDate, String endDate, String stepSeq, String productionType, String site, String line,
			String productSpecGroup, String productSpecName, String lots, String lotText, String machine,
			String program, String target) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		parameter.put("lotText", lotText);
		setArrayParam(parameter, "stepSeqs", stepSeq);
		setArrayParam(parameter, "productionTypes", productionType);
		setArrayParam(parameter, "sites", site);
		setArrayParam(parameter, "divs", line);
		setArrayParam(parameter, "productSpecGroups", productSpecGroup);
		setArrayParam(parameter, "productSpecNames", productSpecName);
		setArrayParam(parameter, "lotIds", lots);
		setArrayParam(parameter, "machines", machine);
		setArrayParam(parameter, "programs", program);
		setArrayParam(parameter, "targets", target);
		List<SearchCondObj> list = mapper.getReelBatchID(parameter);
//		preSettingList(list);
		return ApiResponse.success("OK", list);
	}

}
