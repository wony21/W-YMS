package wyms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodingTeest {
	
	@Test
	public void CycleTimeTest() throws ParseException {
		int hour = 8;
		int delay = 3;
		int count = 24/delay;
		Date date1 = DateUtils.parseDate(String.format("%02d00", hour), "HHmm");
		for(int i=0; i < count; i++) {
			Date scheduleHour = DateUtils.addHours(date1, delay * i);
			log.info("{}", DateFormatUtils.format(scheduleHour, "HH:mm"));
		}
	}

}
