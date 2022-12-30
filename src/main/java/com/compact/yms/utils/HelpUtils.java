package com.compact.yms.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class HelpUtils {
	
	public static final String parseDate(String str, String format, String outputFormat) throws ParseException {
		Date date = DateUtils.parseDate(str, format);
		return DateFormatUtils.format(date, outputFormat);
	}

}
