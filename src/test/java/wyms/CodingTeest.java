package wyms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.rank.Percentile.EstimationType;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.util.KthSelector;
import org.apache.commons.math3.util.MedianOf3PivotingStrategy;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodingTeest {
	
	@Test
	public void CycleTimeTest() throws ParseException {
		int hour = 8;
		int delay = 12;
		int count = 24/delay;
		Date date1 = DateUtils.parseDate(String.format("%02d00", hour), "HHmm");
		for(int i=0; i < count; i++) {
			Date scheduleHour = DateUtils.addHours(date1, delay * i);
			log.info("{}", DateFormatUtils.format(scheduleHour, "HH:mm"));
		}
	}
	
	//@Test
	public void testPercentile() {
		double[] seq = {1,2,3,4,5,6,7,8,9,10};
		
		//QUARTILE
//		DescriptiveStatistics ds = new DescriptiveStatistics();
//		for(double d : seq) {
//			ds.addValue(d);
//		}
//		double q1 = ds.getPercentile(25);
//		double q2 = ds.getPercentile(50);
//		double q3 = ds.getPercentile(75);
		
		DescriptiveStatistics ds = new DescriptiveStatistics();
//		Percentile p = new Percentile()
//				.withEstimationType(EstimationType.R_7)
//				.withNaNStrategy(NaNStrategy.REMOVED)
//				.withKthSelector(new KthSelector(new MedianOf3PivotingStrategy()));
//		p.setData(seq);
//		double q1 = p.evaluate(25);
//		double q2 = p.evaluate(50);
//		double q3 = p.evaluate(75);
//		
		Percentile pImpl = new Percentile()
								.withEstimationType(EstimationType.R_7)
								.withNaNStrategy(NaNStrategy.REMOVED)
								.withKthSelector(new KthSelector(new MedianOf3PivotingStrategy()));
		ds.setPercentileImpl(pImpl);
		for(double d : seq) {
			ds.addValue(d);
		}
		ds.getMin();
		ds.getMax();
		double q1 = ds.getPercentile(25);
		double q2 = ds.getPercentile(50);
		double q3 = ds.getPercentile(75);
		
		log.info("Q1[{}] Q2[{}] Q3[{}]", new Object[] { q1, q2, q3 });
	}
	
	

}
