package com.compact.yms.domain.login.dto;

import lombok.Data;

@Data
public class LoginUserObject {
	
	String userId;
	
	String userName;
	
	String userGroupName;
	
	String password;
	
	String factoryName;
	
}
