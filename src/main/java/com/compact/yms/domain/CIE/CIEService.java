package com.compact.yms.domain.CIE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.FileObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.compact.yms.common.CamelCaseMap;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.CIE.dto.CieXYGraphDataObj;
import com.compact.yms.domain.CIE.dto.CieXYRawdataObject;
import com.compact.yms.domain.CIE.dto.FileClobObject;
import com.compact.yms.domain.CIE.dto.SeperatorObj;
import com.compact.yms.scheduler.CTQScheduler;
import com.microsoft.sqlserver.jdbc.StringUtils;


@Service
@Component
public class CIEService {
	
	private static final Logger logger = LoggerFactory.getLogger(CIEService.class);
	
	@Autowired
	CIEMapper mapper;
	
	public List getFileM(String startDate, String endDate, String partId, String equipmentId) {
		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			String replaceSttDt = startDate.replace("-", "");
			String replaceEndDt	= endDate.replace("-", "");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("startDate", replaceSttDt);
			param.put("endDate", replaceEndDt);
			param.put("partId", partId);
			param.put("eqpId", equipmentId);
			return mapper.getList(param);
		}
		return null;
	}
	
	public List comapreProgram(String lotNo, String trayNo, String partId, String equipmentId, String program) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("lotNo", lotNo);
		param.put("trayNo", trayNo);
		param.put("partId", partId);
		param.put("eqpId", equipmentId);
		param.put("program", program);
		return mapper.compareProgram(param);
	}
	
	public List getFileDClob(String lotName, String partId, String program) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("lotName", lotName);
		param.put("partId", partId);
		param.put("program", program);
		List<FileClobObject> results = mapper.getFileDClob(param);
		List<SeperatorObj> seperatorObj = mapper.getSeperator(param);
		
		Map<String, List<FileClobObject>> mapByFmkey 
				= results.stream().collect(Collectors.groupingBy(FileClobObject::getFmKey));
		
		List<CieXYRawdataObject> dataResults = new ArrayList<CieXYRawdataObject>();
		for(Map.Entry<String, List<FileClobObject>> entry : mapByFmkey.entrySet()) {
			String fmKey = entry.getKey();
			List<FileClobObject> objectList = entry.getValue();
			CieXYRawdataObject newObject = new CieXYRawdataObject();
			
			for(FileClobObject obj : objectList) {

				if ( obj.getFlag().equals("BIN")) {
					newObject.setBinItemName(obj.getItemId());
					String[] mdataArray = obj.getMdata().split(",");
					List<String> binNames= new ArrayList<String>();
					for(String bin : mdataArray) {
						
						SeperatorObj findObj   
								= seperatorObj.stream().filter(o -> o.binNo.equals(bin)).findFirst().get();
						if ( findObj != null ) {
							binNames.add(findObj.getBinName());
						} else {
							binNames.add("-");
						}
					}
					newObject.setBinName(binNames);
					newObject.setBinNo(Arrays.asList(mdataArray));
				} else if ( obj.getFlag().equals("X")) {
					newObject.setCieXName(obj.getItemId());
					String[] mdataArray = obj.getMdata().split(",");
					List<Double> mdataNumArray =
							Arrays.asList(mdataArray).stream().map(Double::parseDouble).collect(Collectors.toList());
					newObject.setCieX(mdataNumArray);
				} else if ( obj.getFlag().equals("Y")) {
					newObject.setCieYName(obj.getItemId());
					String[] mdataArray = obj.getMdata().split(",");
					List<Double> mdataNumArray =
							Arrays.asList(mdataArray).stream().map(Double::parseDouble).collect(Collectors.toList());
					newObject.setCieY(mdataNumArray);
				}
			}
			dataResults.add(newObject);
		}
		
		return dataResults;
	}
	
	
	
	public List getSeperatorCie(String partId, String program) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("partId", partId);
		param.put("program", program);
		return mapper.getSeperatorCie(param);
	}
	
}
