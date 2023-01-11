package com.compact.yms.domain.login;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.compact.yms.domain.login.dto.LoginUserObject;

@Mapper
public interface LoginMapper {

	LoginUserObject getUser(Map<String, Object> parameter);
	
}
