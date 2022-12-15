package com.compact.yms.domain.chipmaster.user;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	public void AddUser(User parameter);
	
	public void AddUserLast(User parameter);
	
}
