package com.compact.base.domain.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.base.common.BaseService;
import com.compact.base.common.api.ApiResponse;
import com.jcraft.jsch.Logger;
import com.microsoft.sqlserver.jdbc.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonService extends BaseService {

	@Autowired
	CommonMapper mapper;

	public List getTest() {
		return mapper.test();
	}

	public ApiResponse getFileM(String sttdt, String enddt, String eqpId, String partId, String stepSeq, String lotId,
			String fileName, String lotType, String pgmName) {
		if (StringUtils.isEmpty(sttdt) || StringUtils.isEmpty(enddt)) {
			return ApiResponse.error("날짜를 입력하시기 바랍니다");
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("schsttdt", sttdt.replace("-", ""));
		parameter.put("schenddt", enddt.replace("-", ""));
		parameter.put("eqpId", eqpId);
		parameter.put("partId", partId);
		parameter.put("stepSeq", stepSeq);
		parameter.put("lotId", lotId);
		parameter.put("fileName", fileName);
		parameter.put("lotType", lotType);
		parameter.put("pgmName", pgmName);
		return ApiResponse.success("", mapper.getFileM(parameter));
	}

	public ApiResponse getFileIndex(String sttdt, String enddt, String machineName, String fileName) {
		if (StringUtils.isEmpty(sttdt) || StringUtils.isEmpty(enddt)) {
			return ApiResponse.error("날짜를 입력하시기 바랍니다");
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("schsttdt", sttdt.replace("-", ""));
		parameter.put("schenddt", enddt.replace("-", ""));
		parameter.put("machineName", machineName);
		parameter.put("fileName", fileName);
		return ApiResponse.success("", mapper.getFileIndex(parameter));
	}

	public ApiResponse getMenuCount(String sttdt, String enddt, String menuNm, String userNm) {

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("schsttdt", sttdt.replace("-", ""));
		parameter.put("schenddt", enddt.replace("-", ""));
		parameter.put("menuNm", menuNm);
		parameter.put("userNm", userNm);
		return ApiResponse.success("", mapper.getDailyMenuUsageCount(parameter));
	}

	public ApiResponse getUseMenuList(String schdt, String menuNm, String userNm) {

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("schdt", schdt.replace(".", ""));
		parameter.put("menuNm", menuNm);
		parameter.put("userNm", userNm);
		return ApiResponse.success("", mapper.getDailyMenuCountList(parameter));
	}

	public ApiResponse getNotMappingOperations(String schSttDt, String schEndDt) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("schSttDt", schSttDt.replace("-", ""));
		parameter.put("schEndDt", schEndDt.replace("-", ""));
		return ApiResponse.success("", mapper.getNotMappingOperations(parameter));
	}

	public ApiResponse getProcessOperationSpec(String operation, String line) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("operation", operation);
		parameter.put("line", line);
		return ApiResponse.success("", mapper.getProcessOperationSpec(parameter));
	}

	
}
