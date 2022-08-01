package com.compact.base;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.compact.base.common.api.ApiResponse;
import com.compact.base.domain.CTQ.CTQService;

@Controller
public class CTQController {
	
	private static final Logger logger = LoggerFactory.getLogger(CTQController.class);
	
	@Autowired
	private CTQService cTQService;
	
	@RequestMapping(value = "/ctq", method = RequestMethod.GET)
	public String CTQPage() {
		return "ctq/ctq";
	}
	
	@RequestMapping(value = "/ctq/mail", method = RequestMethod.GET)
	public String CTQMailPage(Model model) {
		//model.addAttribute("date", date);
		return "ctq/CTQForMail";
	}
	
	@RequestMapping(value = "/ctq/chart", method = RequestMethod.GET)
	public String CTQChart(Model model,
						   @RequestParam String start,
						   @RequestParam String end,
						   @RequestParam String testProv,
						   @RequestParam String itemGroup,
						   @RequestParam String itemCode) {
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("testProv", testProv);
		model.addAttribute("itemGroup", itemGroup);
		model.addAttribute("itemCode", itemCode);
		return "ctq/CTQChart";
	}
	
	public void ConvertChart(HttpServletRequest request, HttpServletResponse reponse) {
		
	}
	
	@RequestMapping(value = "/getPullTest", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getPullTestList() {
		return ApiResponse.success("", cTQService.getTest());
	}
	
	@RequestMapping(value = "/getCTQData", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getCTQData(@RequestParam String startDate, 
								  @RequestParam String endDate,
								  @RequestParam String itemGroup, 
								  @RequestParam String itemCode, 
								  @RequestParam String testProv) {
		return ApiResponse.success("OK", cTQService.getCTQData(startDate, endDate, itemGroup, itemCode, testProv));
	}
	
	@RequestMapping(value = "/itemGroup", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getItemGroup(@RequestParam String startDate, 
			  					    @RequestParam String endDate) {
		return ApiResponse.success("OK", cTQService.getItemGroup(startDate, endDate));
	}
	
	@RequestMapping(value = "/itemCode", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getItemCode(@RequestParam String startDate, 
			  					   @RequestParam String endDate,
			  					   @RequestParam String itemGroup) {
		return ApiResponse.success("OK", cTQService.getItemCode(startDate, endDate, itemGroup));
	}
	
	@RequestMapping(value = "/testProv", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApiResponse getTestProv(@RequestParam String startDate, 
			   					   @RequestParam String endDate) {
		return ApiResponse.success("OK", cTQService.getTestProvCode(startDate, endDate));
	}
	
	@RequestMapping(value = "/ctq/chart/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String uploadChipMasterController(@RequestParam String mailNo, 
											 @RequestParam String start,
											 @RequestParam String end,
											 @RequestParam String testProv,
											 @RequestParam String itemGroup,
											 @RequestParam String itemCode,
											 @RequestParam MultipartFile file) {
		try {
			cTQService.SaveFile(mailNo, start, end, testProv, itemGroup, itemCode, file);
		} catch (Exception e) {
			return e.getMessage(); 
		}

		return file.getOriginalFilename() + " Upload OK!!";
	}
	
//	@RequestMapping(value = "/ctq/chart/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@ResponseBody
//	public byte[] getImageWithMediaType() throws IOException {
//		
//		//InputStream in = getClass().getResourceAsStream(name)
//		//return IOUtils.toByteArray(in);
//	}
	
}
