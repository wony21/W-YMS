package com.compact.yms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.yms.common.BaseController;
import com.compact.yms.common.api.ApiResponse;
import com.compact.yms.domain.login.LoginService;
import com.compact.yms.domain.login.dto.LoginUserObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Autowired
	LoginService loginService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public ApiResponse login(String userId, String password) {

		LoginUserObject loginUser = loginService.getUser(userId, password);
		if (loginUser == null) {
			return ApiResponse.error("일치하는 사용자가 없습니다");
		} else {
			return ApiResponse.success("LoginOK", loginUser);
		}
	}

}
