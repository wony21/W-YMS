package com.compact.base;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RouteController {

	@RequestMapping(value = "/cm/bulk", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		return "/chipmaster/Bulk";
	}
	
}
