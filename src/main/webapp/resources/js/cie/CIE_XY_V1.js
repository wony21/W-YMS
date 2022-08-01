/**
 * CIE_V1.js 
 * 
 *  20220622 최초생성
 */
let fnObj = {};
let ymsTmplContainer = $('#yms-tmpl-container').html();
let ymsTmpl = $('#yms-template').html();
let chartObj = $('#cieChart');
let cieChart;


fnObj.initView = function(){
	// 전제 틀 90% 규정화
	$('#root').css('width', '95%');
	
	// test
	$('#lotname').val('220612-0006-001-001');
	$('#part-id').val('6370082L');
	$('#program').val('ZFV1010C00-3030-BC-11');
	
	// alert(new Date());
	
	// 차트 초기화
	const zoomOptions = {
		pan: {
			enabled: true,
			mode: 'xy',
		},
		zoom: {
			wheel: {
				enabled: true,
			},
			pinch: {
				enabled: true
			},
			mode: 'xy',
			onZoomComplete({chart}) {
				chart.update('none');
			}
		}
	};
	
	
	const config = {
      type: 'scatter',
	  data: [],
	  options: {
	    scales: {
	    	
	    },
	    plugins: {
	      zoom: zoomOptions,
	      title: {
	        display: true,
	        position: 'bottom',
	      }
	    },
	    onClick(e) {
	      console.log(e.type);
	    }
	  }
	};
			
	cieChart = new Chart(chartObj, config);
};

fnObj.initEvent = function() {
	// 조회하기
	$('#btn-search').on('click', fnObj.fn.Search);
};

fnObj.fn = {
	
    Search: function() {
		
		var lotName = $('#lotname').val();
		var partId = $('#part-id').val();
		var program = $('#program').val();
		var param = {
			lotName: lotName,
			partId: partId,
			program: program
		};
		var seperatorCieObj = {
			type: 'get',
			url: '/cie/getSeperatorCIE',
			data: param,
			success: fnObj.fn.drawSeperatorCie,
			error: function(err) {
				console.log(err);
				return false;
			},	
		};
		var rawReqObj = {
			type: 'get',
			url: '/cie/getCieRawdata',
			data: param,
			success: fnObj.fn.drawCieXY,
			error: function(err) {
				console.log(err);
				return false;
			},	
		}
		// 분류기준서 
		$.ajax(seperatorCieObj);
		// 요청
		$.ajax(rawReqObj);
		
	},
	// 분류기준서 그리기
	drawSeperatorCie: function(res) {
		var data = res.data;
		
		var annotations = new Array();
		data.forEach((o, i) => {
			var line1 = {
					type: 'line',
					borderColor: 'rgb(101, 33, 171)',
					borderWidth: 1,
					//drawTime: 'beforeDatasetsDraw',
					xMin: Number(o.x1),
					xMax: Number(o.x2),
					xScaleID: 'x',
					yMin: Number(o.y1),
					yMax: Number(o.y2),
					yScaleID: 'y',
			};
			var line2 = {
					type: 'line',
					borderColor: 'rgb(101, 33, 171)',
					borderWidth: 1,
					//drawTime: 'beforeDatasetsDraw',
					xMin: Number(o.x2),
					xMax: Number(o.x3),
					xScaleID: 'x',
					yMin: Number(o.y2),
					yMax: Number(o.y3),
					yScaleID: 'y',
			};
			var line3 = {
					type: 'line',
					borderColor: 'rgb(101, 33, 171)',
					borderWidth: 1,
					//drawTime: 'beforeDatasetsDraw',
					xMin: Number(o.x3),
					xMax: Number(o.x4),
					xScaleID: 'x',
					yMin: Number(o.y3),
					yMax: Number(o.y4),
					yScaleID: 'y',
			};
			var line4 = {
					type: 'line',
					borderColor: 'rgb(101, 33, 171)',
					borderWidth: 1,
					//drawTime: 'beforeDatasetsDraw',
					xMin: Number(o.x4),
					xMax: Number(o.x1),
					xScaleID: 'x',
					yMin: Number(o.y4),
					yMax: Number(o.y1),
					yScaleID: 'y',
			};
			
			
			var minX = Math.min(o.x1, o.x2, o.x3, o.x4);
			var maxX = Math.max(o.x1, o.x2, o.x3, o.x4);
			var minY = Math.min(o.y1, o.y2, o.y3, o.y4);
			var maxY = Math.max(o.y1, o.y2, o.y3, o.y4);
			var centerX = (minX + maxX) / 2;
			var centerY = (minY + maxY) / 2;
			console.log('cie center', o.cie, centerX, centerY);
			var label = {
					type:'label',
					content: [ o.cie ],
					xValue: centerX,
					yValue: centerY
			};
			
			annotations.push(line1);
			annotations.push(line2);
			annotations.push(line3);
			annotations.push(line4);
			annotations.push(label);
		});
		
		cieChart.options.plugins.annotation.annotations = annotations;
		
		cieChart.update();
	},
	drawCieXY: function(res) {
		
		var data = res.data;
		
		var seriesData = [
			
		];
		
		data.forEach((o, i) => {
			
			var binCount = o.binName.length;
			
			for(var i = 0; i < binCount; i++) {
				
//				if ( sereisData.length == 0) {
//					
//					var firstObj = {
//						label: ,
//						data: ,
//					}
//					
//				} else {
//					
//				}
			}
			
			// o.binName
			// o.cieX
			// o.cieY
			
			// seriesData.filter(o => o.label === )
			
		});
		
		
		
		
		
		var data = res.data;
		
		console.log(data);
		
		var fmKey = data[0].FMKEY;
		
		var data2 = data.filter((o) => {
			return o.FMKEY === fmKey;
		});
		
		console.log('-- filter data --');
		
		console.log(data2);
		
		var cieX = data2.filter((o) => {
			return o.itemId === 'CIE X';
		});
		console.log('--- CIE X ---');
		console.log(cieX);

		var cieY = data2.filter((o) => {
			return o.itemId === 'CIE Y';
		});
		console.log('--- CIE Y ---');
		console.log(cieY);
		
		var cieXarr = cieX[0].mdata.split(',');
		var cieYarr = cieY[0].mdata.split(',');
		
		var valueCount = cieXarr.length;
		
		var xyArray = [];
		for(var i=0; i < valueCount; i++) {
			xyArray.push({
				x: Number(cieXarr[i]),
				y: Number(cieYarr[i]),
			});
		};
		
		console.log(xyArray);
		cieChart.data = {
				datasets: [{
					label: 'CIE XY',
					data: xyArray,
				}]
		};
	
		cieChart.update();
		
		// cieChart.data = chartDataArr;
		
//		var chartLabels = [];
//		var chartDataSet = [];
//		data.forEach((o, i) => {
//			chartLabels.push(o.x1);
//			chartLabels.push(o.x2);
//			chartLabels.push(o.x3);
//			chartLabels.push(o.x4);
//			chartDataSet.push({
//				label: [ o.x1, o.x2, o.x3, o.x4 ],
//				data: [ o.y1, o.y2, o.y3, o.y4 ]
//			});
//		});
//		
//		cieChart.data = {
//			datasets: chartDataSet,
//			labels : chartLabels
//		};
//		
//		cieChart.update();
	},
	
	
};

$(function(){
	fnObj.initView();
	fnObj.initEvent();
});



