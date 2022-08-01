/**
 * yield.js 
 * 
 *  20200306 최초생성
 */
let fnObj = {};
let ymsmenuuseTmpl = $('#yms-menu-use-template').html();
let ymsfileIndexTmpl = $('#yms-file-index-template').html();

let combo_itemGroup_Tmpl = $('#item-group-templete').html();
let combo_itemCode_Tmpl = $('#item-code-templete').html();
let combo_testProv_Tmpl = $('#test-prov-templete').html();

let chartObj = $('#myChart');
let menuChart;
const data = {
		labels: ''
}

/**
 * 콤보박스 초기화
 * @returns
 */
function initComboBoxes() {
	var start = $('#srch-start-date').val();
	var end = $('#srch-end-date').val();
	console.log('date:', start, end);
	if ( !start && !end ) {
		return false;
	}
	fnObj.fn.itemGroupComboBoxBinding();
	fnObj.fn.itemComboBoxBinding();
	fnObj.fn.testProvComboBoxBinding();
}

/**
 * 날짜 검색 조건 초기화
 * @returns
 */
function initCalendar() {
	
	$('#search-start-date').calendar({
		type: 'date',
		monthFirst: false,
		endCalendar: $('#search-end-date'),
		formatter: {
			date: function(date, settings) {
				if (!date) return '';
		        return getFormatDate(date);
			}
		},
		onChange: function(o){
			initComboBoxes();
		}
	});
	
	$('#search-end-date').calendar({
		type: 'date',
		monthFirst: false,
		startCalendar: $('#search-start-date'),
		formatter: {
			date: function(date, settings) {
				if (!date) return '';
		        return getFormatDate(date);
			}
		},
		onChange: function(o){
			console.log('-- end search date changed --');
			initComboBoxes();
		}
	});
}

fnObj.initView = function(){
	// 전제 틀 90% 규정화
	$('#root').css('width', '90%');
	// 차트 초기화
	menuChart = new Chart(chartObj, {
		type : 'line',
		data : [],
		options : {
			plugins: {
				legend: {
					labels: {
						usePointStyle: true,
					},
					display: false,
				}
			},
			animation: {
				onComplete: function() {
					console.log('-- complete --');
					const canvas = document.getElementById('myChart');
					const imgBase64 = canvas.toDataURL('image/png', 'image/octet-stream');
					console.log('-- href link setting --');
					$('#img-download').attr('href', imgBase64);
					var date = new Date();
					var fileName = date.getTime() + '.png';
					$('#img-download').attr('download', fileName);
				}
			}
		},
		
	});
	menuChart.update();
};



fnObj.initEvent = function() {
	// 제품코드
	$('#item-code-combo').dropdown();
	// 시험항목
	$('#test-prov-combo').dropdown();
	// 제품그룹
	$('#item-group-combo').dropdown({
		onChange: function(value, text, $selectedItem) {
			fnObj.fn.itemComboBoxBinding();
		}
	});
	// 검색날짜
	initCalendar();
	// 조회하기
	$('#btn-search').on('click', fnObj.fn.search);
	// 이미지 저장
	$('#btn-save-img').on('click', fnObj.fn.saveImage);
	
	// 범례 표시 설정
	$('#show-legend').checkbox({
		onChecked: function() {
			fnObj.fn.showChartLegend(true);
		},
		onUnchecked: function() {
			fnObj.fn.showChartLegend(false);
		},
	});
	
};

fnObj.fn = {
		showChartLegend: function(show) {
			if (!menuChart) {
				return true;
			}
			menuChart.options.plugins.legend.display = show;
			menuChart.update();
		},
		randColor: function() {
			return Math.floor(Math.random() * 255);
		},
		getColorStr: function() {
			return 'rgba(' + fnObj.fn.randColor() + ', ' + fnObj.fn.randColor() + ', ' + fnObj.fn.randColor() + ', 0.5)';
		},
		itemGroupComboBoxBinding: function () {
			var startDate = $('#srch-start-date').val();
			var endDate = $('#srch-end-date').val();
			console.log('startDate:', startDate);
			console.log('endDate:', endDate);
			var param = {
					startDate: startDate, 
					endDate: endDate,
			};
			// 로딩 표시
			$('#itemgroup').val('');
			$('#item-group-desc').text('로딩 중');
			$.ajax({
				type: 'GET',
				data: param, 
				url: '/itemGroup',
				success: function(res) {
					var html = Mustache.render(combo_itemGroup_Tmpl, {list: res.data});
					$('#item-group-container').html(html);
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err);
				},
				complete : function(res) {
					// 로딩 완료
					$('#item-group-desc').text('제품그룹');
				},
			});
		},
		itemComboBoxBinding: function () {
			var startDate = $('#srch-start-date').val();
			var endDate = $('#srch-end-date').val();
			var itemGroup = $('#itemgroup').val();
			if (!startDate && !endDate && !itemGroup) {
				return false;
			}
			var param = {
					startDate: startDate, 
					endDate: endDate,
					itemGroup: itemGroup,
			};
			// 로딩 표시
			$('#itemcode').val('');
			$('#item-code-desc').text('로딩 중');
			$.ajax({
				type: 'GET',
				data: param, 
				url: '/itemCode',
				success: function(res) {
					var html = Mustache.render(combo_itemCode_Tmpl, {list: res.data});
					$('#item-code-container').html(html);
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err);
				},
				complete : function(res) {
					$('#item-code-desc').text('제품');
				},
			});
		},
		testProvComboBoxBinding: function () {
			var startDate = $('#srch-start-date').val();
			var endDate = $('#srch-end-date').val();
			var param = {
					startDate: startDate, 
					endDate: endDate,
			};
			// 로딩 표시
			$('#testprov').val('');
			$('#test-prov-desc').text('로딩 중');
			$.ajax({
				type: 'GET',
				data: param, 
				url: '/testProv',
				success: function(res) {
					console.log('-- testProv --');
					console.log(res.data);
					var html = Mustache.render(combo_testProv_Tmpl, {list: res.data});
					$('#test-prov-container').html(html);
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err);
				},
				complete : function(res) {
					$('#test-prov-desc').text('시험항목');
				},
			});
		},
		// 검색하기
		search: function() {
			var startDate = $('#srch-start-date').val();
			var endDate = $('#srch-end-date').val();
			var itemGroup = $('#itemgroup').val();
			var itemCode = $('#itemcode').val();
			var testProv = $('#testprov').val();
			
			if (!startDate || !endDate || !itemGroup || !testProv) {
				alert('검색 조건을 확인 해 주세요. (제품은 필수가 아님)');
				return false;
			}
			
			// console.log(' request date :', date);
			var param = {
					startDate: startDate,
					endDate: endDate,
					itemGroup: itemGroup,
					itemCode: itemCode,
					testProv: testProv,
			};
			console.log(param);
			
			showLoading();
			$.ajax({
				type: 'GET',
				data: param, 
				url: '/getCTQData',
				success: function(res) {
					/*************************************
					 * Table
					 *************************************/
					var html = Mustache.render(ymsmenuuseTmpl, {list: res.data});
					$('#yms-menu-use-container').html(html);
					
					/*************************************
					 * Chart
					 *************************************/
					ChartDataBinding_of_dates(res.data);
					
	                return true;
				},
				error: function(err) {
					console.log(err);
					hideLoading();
				},
				complete : function(res) {
					hideLoading();
				},
			});
		},
		saveImage: function() {
		},
};

$(function(){
	fnObj.initView();
	fnObj.initEvent();
});

function ChartDataBinding_of_values(data) {
	// ChartDataSet
	var ChartDataSet = {
			labels: [
				'value1', 'value2', 'value3', 'value4', 'value5', 
				'value6', 'value7', 'value8', 'value9', 'value10',
				'value11', 'value12', 'value13', 'value14', 'value15',
				'value16', 'value17', 'value18', 'value19', 'value20',
			],
			dataset: [],
	}
	// Create Chart DataSet
	data.forEach(function(o, i){
		ChartDataSet.dataset.push({
			label: o.rowidx,
			data:[
				o.value1, o.value2, o.value3, o.value4, o.value5,
				o.value6, o.value7, o.value8, o.value9, o.value10,
				o.value11, o.value12, o.value13, o.value14, o.value15,
				o.value16, o.value17, o.value18, o.value19, o.value20,
			],
			backgroundColor: 'rgba(0, 0, 0, 0)',
			borderColor: 'rgba(0, 0, 0, 0)',
			borderWidth: 1,
			pointStyle: 'round',
			pointRadius: 5,
			pointBorderColor: fnObj.fn.getColorStr(),
			pointBackgroundColor: fnObj.fn.getColorStr(),
		});
	});
	// USL, LSL, UCL, LCL
	var LSL = { type: 'line', label: 'LSL', data: [], borderColor: 'rgba(255, 0, 0, 1)', borderWidth: 3, pointBorderColor: 'rgba(0,0,0,0)' };
	var USL = { type: 'line', label: 'USL', data: [], borderColor: 'rgba(0, 0, 255, 1)', borderWidth: 3, pointBorderColor: 'rgba(0,0,0,0)' };
	var LCL = { type: 'line', label: 'LCL', data: [], borderColor: 'rgba(255, 0, 0, 1)', borderWidth: 3, };
	var UCL = { type: 'line', label: 'UCL', data: [], borderColor: 'rgba(0, 0, 255, 1)', borderWidth: 3, };
	
	console.log('-- chart bind --');
	console.log(data);
	
	if ( data.length > 0 ) {
		for(var i=0; i< ChartDataSet.labels.length; i++) {
			LSL.data.push(data[0].minValue);
			USL.data.push(data[0].maxValue);
		}
		ChartDataSet.dataset.push(LSL);
		ChartDataSet.dataset.push(USL); 
	}
	// Chart DataBinding
	menuChart.data = {
			labels: ChartDataSet.labels,
			datasets: ChartDataSet.dataset,
	}
	// Chart Update and commit
	menuChart.update();
}

function ChartDataBinding_of_dates(data) {
	// ChartDataSet
	var ChartDataSet = {
			labels: [],
			dataset: [
				{ label : 'value1', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value2', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value3', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value4', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value5', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value6', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value7', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value8', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value9', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value10', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value11', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value12', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value13', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value14', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value15', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value16', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value17', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value18', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value19', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : 'value20', data: [], fill: false, borderColor: 'rgba(0, 0, 0, 0)', borderWidth: 1, pointStyle: 'round', pointRadius: 5, pointBorderColor: fnObj.fn.getColorStr(), pointBackgroundColor: fnObj.fn.getColorStr(), },
				{ label : '평균(AVG)', 
				  data: [], 
				  fill: 'rgba(0, 0, 0, 0)', 
				  borderColor: fnObj.fn.getColorStr(), 
				  borderWidth: 4, 
				  pointStyle: 'round', 
				  pointRadius: 5, 
				  pointBorderColor: fnObj.fn.getColorStr(), 
				  pointBackgroundColor: fnObj.fn.getColorStr(), 
				  tension:0.3 },
			],
	}
	// Create Chart DataSet
	data.forEach(function(o, i){
		
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
	var LSL = { type: 'line', label: 'LSL', data: [], borderColor: 'rgba(255, 0, 0, 1)', borderWidth: 3, pointBorderColor: 'rgba(0,0,0,0)' };
	var USL = { type: 'line', label: 'USL', data: [], borderColor: 'rgba(255, 0, 0, 1)', borderWidth: 3, pointBorderColor: 'rgba(0,0,0,0)' };
	var LCL = { type: 'line', label: 'LCL', data: [], borderColor: 'rgba(0, 0, 255, 1)', borderWidth: 3, };
	var UCL = { type: 'line', label: 'UCL', data: [], borderColor: 'rgba(0, 0, 255, 1)', borderWidth: 3, };
	
	console.log('-- chart bind --');
	console.log(data);
	
	if ( data.length > 0 ) {
		for(var i=0; i< ChartDataSet.labels.length; i++) {
			LSL.data.push(data[0].minValue);
			USL.data.push(data[0].maxValue);
		}
		ChartDataSet.dataset.push(LSL);
		ChartDataSet.dataset.push(USL);
		
		// Axis Y Scale
		var v = Math.abs(Number(data[0].maxValue) - Number(data[0].minValue));
		var axisYMin = Number(data[0].minValue) - (v * 0.2);
		var axisYMax = Number(data[0].maxValue) + (v * 0.2);
		menuChart.options.scales = {
				y: {
					min: axisYMin,
					max: axisYMax,
				},
		}
		console.log('data-min:', data[0].minValue, 'data-max', data[0].maxValue,'min:', axisYMin, 'max:', axisYMax);
		
	}
	// Chart DataBinding
	menuChart.data = {
			labels: ChartDataSet.labels,
			datasets: ChartDataSet.dataset,
	};
	
	var itemGroup = $('#itemgroup').val();
	var testProv = $('#test-prov-desc').text();
	menuChart.options.plugins.title = {
			display: true,
			text: itemGroup + ' ' + testProv,
	};
	
	// Chart Update and commit
	menuChart.update();
}



