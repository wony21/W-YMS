package com.compact.yms.domain.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compact.yms.domain.login.dto.LoginUserObject;

@Service
public class LoginService {
	
	@Autowired
	LoginMapper loginMapper;

	public LoginUserObject getUser(String userId, String password) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("userId", userId);
		parameter.put("password", password);
		return loginMapper.getUser(parameter);
	}
	
}
