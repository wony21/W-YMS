// 차트
var CTQChart;
// 업로드 완료 Flag (한번만 업로드 하고 안한다)
var uploadFlag = false;

$(function() {
	initView();
	initEvent();
});

/**
 * 뷰 초기화
 * @returns
 */
function initView() {
	createChart();
	requestDrawChart();
}

/**
 * 이벤트 초기화
 * @returns
 */
function initEvent() {

}
/**
 * 차트 생성하기
 * @returns
 */
function createChart() {
	const chartCanvas = $('#ctq-chart');
	
	CTQChart = new Chart(chartCanvas, {
		type : 'line',
		data : [],
		options : {
			plugins : {
				legend : {
					labels : {
						usePointStyle : true,
					},
					display : false,
				}
			},
			animation : {
				onComplete : function(context) {
					if (context.initial) {
						// console.log('Initial animation finished');
						const canvas = document.getElementById('ctq-chart');
						const base64Url = canvas.toDataURL('image/png', 'image/octet-stream');
						const decodImg = atob(base64Url.split(',')[1]);
						var array = [];
						for(var i=0; i<decodImg.length; i++) {
							array.push(decodImg.charCodeAt(i));
						}
						
						const file = new Blob([new Uint8Array(array)], {type:'image/png'});
						const fileName = new Date().getTime() + '.png';
						
						var mailNo = "0";
						var start = $('#start-date').val();
						var end = $('#end-date').val();
						var testProv = $('#test-prov').val();
						var itemGroup = $('#item-group').val();
						var itemCode = $('#item-code').val();
						
						var formData = new FormData();
						formData.append('mailNo', "0");
						formData.append('start', start);
						formData.append('end', end);
						formData.append('testProv', testProv);
						formData.append('itemGroup', itemGroup);
						formData.append('itemCode', itemCode);
						formData.append('file', file, fileName);
						
						$.ajax({
							type: 'POST',
							url: '/ctq/chart/upload',
							cache: false,
							data: formData,
							processData: false,
							contentType: false,
							success: function(data){
								console.log(data);
							}
						});
						
					} else {
						console.log('animation finished');
					}
				}
			}
		}
	});
}

/**
 * 차트 그리기 요청
 * @returns
 */
function requestDrawChart() {
	var start = $('#start-date').val();
	var end = $('#end-date').val();
	var testProv = $('#test-prov').val();
	var itemGroup = $('#item-group').val();
	var itemCode = $('#item-code').val();
	drawChart(start, end, testProv, itemGroup, itemCode);
}
/**
 * 차트 그리기
 * @param start 시작일
 * @param end 종료일
 * @param testProv 테스트코드
 * @param itemGroup 제품그룹
 * @param itemCode 제품코드
 * @returns
 */
function drawChart(start, end, testProv, itemGroup, itemCode) {
	var param = {
		startDate : start,
		endDate : end,
		testProv : testProv,
		itemGroup : itemGroup,
		itemCode : itemCode,
	};

	$.get('/getCTQData', param, function(res) {
		console.log(res);
		ChartDataBinding_of_dates(res.data);
	});
}
/**
 * 랜덤색상 가져오기
 * @returns
 */
function RandomColor() {
	return 'rgba(' + Math.floor(Math.random() * 255) + ', '
			+ Math.floor(Math.random() * 255) + ', '
			+ Math.floor(Math.random() * 255) + ', 0.5)';
}
/**
 * 차트 데이터 바인딩
 * @param data
 * @returns
 */
function ChartDataBinding_of_dates(data) {
	// ChartDataSet
	var ChartDataSet = {
		labels : [],
		dataset : [ {
			label : 'value1',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value2',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value3',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value4',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value5',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value6',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value7',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value8',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value9',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value10',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value11',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value12',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value13',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value14',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value15',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value16',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value17',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value18',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value19',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : 'value20',
			data : [],
			fill : false,
			borderColor : 'rgba(0, 0, 0, 0)',
			borderWidth : 1,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
		}, {
			label : '평균(AVG)',
			data : [],
			fill : 'rgba(0, 0, 0, 0)',
			borderColor : RandomColor(),
			borderWidth : 4,
			pointStyle : 'round',
			pointRadius : 5,
			pointBorderColor : RandomColor(),
			pointBackgroundColor : RandomColor(),
			tension : 0.3
		}, ],
	}
	// Create Chart DataSet
	data.forEach(function(o, i) {

		ChartDataSet.labels.push(o.ctqDate);
		ChartDataSet.dataset[0].data.push(o.value1);
		ChartDataSet.dataset[1].data.push(o.value2);
		ChartDataSet.dataset[2].data.push(o.value3);
		ChartDataSet.dataset[3].data.push(o.value4);
		ChartDataSet.dataset[4].data.push(o.value5);
		ChartDataSet.dataset[5].data.push(o.value6);
		ChartDataSet.dataset[6].data.push(o.value7);
		ChartDataSet.dataset[7].data.push(o.value8);
		ChartDataSet.dataset[8].data.push(o.value9);
		ChartDataSet.dataset[9].data.push(o.value10);
		ChartDataSet.dataset[10].data.push(o.value11);
		ChartDataSet.dataset[11].data.push(o.value12);
		ChartDataSet.dataset[12].data.push(o.value13);
		ChartDataSet.dataset[13].data.push(o.value14);
		ChartDataSet.dataset[14].data.push(o.value15);
		ChartDataSet.dataset[15].data.push(o.value16);
		ChartDataSet.dataset[16].data.push(o.value17);
		ChartDataSet.dataset[17].data.push(o.value18);
		ChartDataSet.dataset[18].data.push(o.value19);
		ChartDataSet.dataset[19].data.push(o.value20);
		// 평균값
		ChartDataSet.dataset[20].data.push(o.avgValue);

	});
	// USL, LSL, UCL, LCL
	var LSL = {
		type : 'line',
		label : 'LSL',
		data : [],
		borderColor : 'rgba(255, 0, 0, 1)',
		borderWidth : 3,
		pointBorderColor : 'rgba(0,0,0,0)'
	};
	var USL = {
		type : 'line',
		label : 'USL',
		data : [],
		borderColor : 'rgba(255, 0, 0, 1)',
		borderWidth : 3,
		pointBorderColor : 'rgba(0,0,0,0)'
	};
	var LCL = {
		type : 'line',
		label : 'LCL',
		data : [],
		borderColor : 'rgba(0, 0, 255, 1)',
		borderWidth : 3,
	};
	var UCL = {
		type : 'line',
		label : 'UCL',
		data : [],
		borderColor : 'rgba(0, 0, 255, 1)',
		borderWidth : 3,
	};

	console.log('-- chart bind --');
	console.log(data);

	if (data.length > 0) {
		for (var i = 0; i < ChartDataSet.labels.length; i++) {
			LSL.data.push(data[0].minValue);
			USL.data.push(data[0].maxValue);
		}
		ChartDataSet.dataset.push(LSL);
		ChartDataSet.dataset.push(USL);

		// Axis Y Scale
		var v = Math.abs(Number(data[0].maxValue) - Number(data[0].minValue));
		var axisYMin = Number(data[0].minValue) - (v * 0.2);
		var axisYMax = Number(data[0].maxValue) + (v * 0.2);
		CTQChart.options.scales = {
			y : {
				min : axisYMin,
				max : axisYMax,
			},
		}
		console.log('data-min:', data[0].minValue, 'data-max',
				data[0].maxValue, 'min:', axisYMin, 'max:', axisYMax);

	}
	// Chart DataBinding
	CTQChart.data = {
		labels : ChartDataSet.labels,
		datasets : ChartDataSet.dataset,
	};

	var itemGroup = $('#item-group').val();
	// var testProv = $('#test-prov-desc').text();
	CTQChart.options.plugins.title = {
		display : true,
		text : itemGroup,
	};

	// Chart Update and commit
	CTQChart.update();
}
