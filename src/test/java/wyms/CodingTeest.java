package wyms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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
		List<Map<String, Object>> maps = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("val", null);
		maps.add(map);
		
		Double data = MapUtils.getDouble(map, "val");
		log.info("{}", data);
		
		long count = maps.stream().filter(o -> MapUtils.getDouble(o, "val") != null).count();
		log.info("count[{}]", count);
		
		Double dval = (count > 0) ? (maps.stream().mapToDouble(o -> MapUtils.getDouble(o, "val")).findFirst().orElse(-99999d)) : (-99999d);
		
		log.info("{}", dval);
		
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
