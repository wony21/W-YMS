package com.compact.base.common;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

	@Autowired
	protected SqlSession sqlSession;

	protected void setArrayParam(Map<String, Object> param, String paramName, String value) {
		if (value != null) {
			if (!value.equals("ALL")) {
				String[] arrayValue = value.split(";");
				param.put(paramName, value.isEmpty() ? null : arrayValue);
			}
		}
	}

	protected void setCustomCond(Map<String, Object> param, String paramName, String value) {
		if (value.isEmpty())
			return;
		String[] valueParam = value.split("┐");
		if ( valueParam.length == 2) {
			param.put(paramName, "Y");
			param.put(paramName + "Column", valueParam[0]);
			param.put(paramName + "Value", valueParam[1]);
		}
	}
}
