package com.compact.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compact.base.common.BaseController;
import com.compact.base.common.api.ApiResponse;
import com.compact.base.domain.IF.POPIFService;
import com.compact.base.domain.IF.POPIFService.IF_FLAG;

@Controller
@RequestMapping("/popIF")
public class POPIFController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(POPIFController.class);
	
	@Autowired
	POPIFService popifService;
	
	@RequestMapping(value = "/elementYield", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse summaryElementTray(String factoryName, String factoryMonth) {
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryMonth);
			popifService.ElementIF(factoryName, factoryMonth);
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK");
	}
	
	@RequestMapping(value = "/elementTrayAssy", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementAssyIF(String factoryName, String factoryMonth) {
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryMonth);
			popifService.IF(IF_FLAG.IF_TB_ELEMENT_TRAY_ASSY, factoryName, factoryMonth);
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK");
	}
	
	@RequestMapping(value = "/elementYieldRaw", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementYieldRaw(String factoryName, String factoryMonth) {
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryMonth);
			popifService.IF(IF_FLAG.IF_YMS_ELEMENT_YIELD_RAW, factoryName, factoryMonth);
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK");
	}
	
	@RequestMapping(value = "/elementYieldPeriod", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementYieldPeriod(String factoryName, String factoryMonth) {
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryMonth);
			popifService.IF(IF_FLAG.IF_YMS_ELEMENT_YIELD_PERIOD, factoryName, factoryMonth);
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK");
	}
	
	@RequestMapping(value = "/elementTrayAssy/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementAssyIFDate(String factoryName, String startDate, String endDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + startDate + ":" + endDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_TB_ELEMENT_TRAY_ASSY, factoryName, startDate, endDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		} 
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/elementYieldRaw/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementYieldRawDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			//popifService.IF(IF_FLAG.IF_YMS_ELEMENT_YIELD_RAW, factoryName, factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_ELEMENT_YIELD_RAW, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/elementYieldPeriod/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse elementYieldPeriodDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if element yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_ELEMENT_YIELD_PERIOD, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/seperatorAssy/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse seperatorAssyIFDate(String factoryName, String startDate, String endDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if seperator yield");
			logger.info("request parameter is " + factoryName + ":" + startDate + ":" + endDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_TB_SEPERATOR_ASSY, factoryName, startDate, endDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		} 
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/seperatorYieldRaw/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse seperatorYieldRawDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if seperator yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			//popifService.IF(IF_FLAG.IF_YMS_ELEMENT_YIELD_RAW, factoryName, factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_SEPERATOR_YIELD_RAW, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/seperatorYieldPeriod/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse seperatorYieldPeriodDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if seperator yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_SEPERATOR_YIELD_PERIOD, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/tapingAssy/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse tapingAssyIFDate(String factoryName, String startDate, String endDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if taping yield");
			logger.info("request parameter is " + factoryName + ":" + startDate + ":" + endDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_TB_TAPING_ASSY, factoryName, startDate, endDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		} 
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/tapingYieldRaw/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse tapingYieldRawDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if taping yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			//popifService.IF(IF_FLAG.IF_YMS_ELEMENT_YIELD_RAW, factoryName, factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_TAPING_YIELD_RAW, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
	@RequestMapping(value = "/tapingYieldPeriod/day", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse tapingYieldPeriodDay(String factoryName, String factoryDate) {
		long beforeTime = 0l;
		long afterTime = 0l;
		long durationTime = 0l;
		try {
			logger.info("request pop if taping yield");
			logger.info("request parameter is " + factoryName + ":" + factoryDate);
			beforeTime = System.currentTimeMillis();
			popifService.IFOfDate(IF_FLAG.IF_YMS_TAPING_YIELD_PERIOD, factoryName, factoryDate, factoryDate);
			afterTime = System.currentTimeMillis();
			durationTime = (afterTime - beforeTime) / 1000;
		} catch (Exception e) {
			return ApiResponse.error(e.getMessage());
		}
		return ApiResponse.success("OK", durationTime);
	}
	
}
