/**
 * CIE_V1.js 
 * 
 *  20220622 최초생성
 */
let fnObj = {};
let ymsTmplContainer = $('#yms-tmpl-container').html();
let ymsTmpl = $('#yms-template').html();

fnObj.initView = function(){
	// 전제 틀 90% 규정화
	$('#root').css('width', '95%');
	
	// 기간 컨트롤 초기화
	$('#search-start-date').calendar({
		type: 'date',
		monthFirst: false,
		formatter: {
			date: function(date, settings) {
				if (!date) return getFormatDate(new Date());
				return getFormatDate(date);
			},
		},
		endCalendar: $('#search-end-date'),
	});
	
	// default - test용
	$('#part-id').val('6370082L');
	// $('#eqp-id').val('T-ST0265');
	
	
	$('#search-end-date').calendar({
		type: 'date',
		monthFirst: false,
		formatter: {
			date: function(date, settings) {
				if (!date) return getFormatDate(new Date());
				return getFormatDate(date);
			},
		},
		startCalendar: $('#search-start-date'),
	});
	
	// 날짜 오늘 날짜로 초기화
	$('[control-tag="date-input"]').val(getFormatDate(new Date()));
};

fnObj.initEvent = function() {
	// 검색버튼
	$('#btn-search').on('click', function(e){
		console.log('click');
		fnObj.fn.Search();
	});
	// 색좌표 조회 버튼
	$(document.body).on('click', 'input[id="btn-cie"]', function(event){
		console.log('click');
	});
	
	// 검색결과 마우스 진입 시
	$(document.body).on('mouseenter', 'td[id="td-machine"]', function(event){
		$(this).css('cursor','pointer');
	});
	// 검색결과 마우스 아웃 시
	$(document.body).on('mouseleave', 'td[id="td-machine"]', function(event){
		$(this).css('cursor','pointer');
	});
	// 검색결과 선택 하여 정보 조회
	$(document.body).on('click', 'td[id="td-machine"]', function(event){
		fnObj.fn.initSearchLog();
		fnObj.fn.onEqpIdClicked($(this));
	});
	// 검색결과 마우스 진입 시 : 파일명
	$(document.body).on('mouseenter', 'td[id="td-file"]', function(event){
		$(this).css('cursor','pointer');
	});
	// 검색결과 마우스 아웃 시 : 파일명
	$(document.body).on('mouseleave', 'td[id="td-file"]', function(event){
		$(this).css('cursor','pointer');
	});
	// 검색결과 선택 하여 정보 조회
	$(document.body).on('click', 'td[id="td-file"]', function(event){
		fnObj.fn.initSearchLog();
		fnObj.fn.onFileClicked($(this));
	});
	// 파일전송로그 확인
	$(document.body).on('click', '#btn-find-log', function(event){
		fnObj.fn.initSearchLog();
		$('#div-analize-result').modal('show');
		$('[control-tag="date"]')
		  .calendar({
		    monthFirst: false,
		    type: 'date',
		    formatter: {
		      date: function (date, settings) {
		        if (!date) return '';
		        return getFormatDate(date);
		      }
		    }
		  });
	});
	// 최근로그확인 검색 
	$('#btn-log-search').on('click', function(e){
		let data = fnObj.fn.getSearchFileIndexCond();
		if (!data.schSttDt && !data.schEndDt) {
			alert('조회 일자를 입력하세요.');
			return false;
		}
		if (!data.machineName && !data.fileName) {
			alert('설비코드 혹은 파일명을 입력하세요.');
			return false;
		}
		fnObj.fn.searchFileIndex(data);
	});
	
};

fnObj.fn = {
		
		Search: function() {
			// 파라메터 생성
			var startDate = $('#start-date').val();
			var endDate = $('#end-date').val();
			var partId = $('#part-id').val();
			var equipmentId = $('#eqp-id').val();
			console.log('PartId', partId);
			console.log('EquipmentId', equipmentId);
			if (!partId) {
				alert('제품코드를 입력하세요.');
				return false;
			}
//			if (!equipmentId) {
//				alert('설비코드를 입력하세요.');
//				return false;
//			}
			var param = {
				startDate: startDate,
				endDate: endDate,
				partId: partId,
				equipmentId: equipmentId
			};
			// ajax 요청 객체 생성
			var requestObj = {
				type: 'get',
				url: '/cie/getlist',
				data: param,
				success: fnObj.fn.requestOnSuccess,
				error: function(err) {
					console.log(err);
					return false;
				},
			};
			// 목록 요청
			$.ajax(requestObj);
			
		},
		requestOnSuccess: function(res) {
			// 템플릿 결과 출력
			var html = Mustache.render(ymsTmpl, {list: res.data});
			$('#yms-tmpl-container').html(html);
			// 색좌표 조회 버튼 이벤트 연결
			$('[id="btn-cie"]').each(function(k, i){
				$(this).on('click', fnObj.fn.SearchCie);
			});
		},
		SearchCie: function() {
			var lot = $(this).data('lot');
			var program = $(this).data('pgm');
			var equipmentId = $(this).data('eqp');
			var partId = $(this).data('partid');
			var param = {
				lotName: lot,
				partId: partId,
				equipmentId: equipmentId,
				program: program
			};
			var ajaxObj = {
				type: 'get',
				url: '/cie/compareProgramName',
				data: param,
				success: fnObj.fn.compareProgram,
				error: function(err) {
					console.log(err);
					return false;
				},	
			};
			// 요청
			$.ajax(ajaxObj);
		},
		compareProgram: function(res) {
			// 결과 데이터 판정
			var data = res.data;
			console.log(data);
			if (data.length == 0) {
				console.log('error');
				return false;
			}
			// 프로그램명 체크
			var lotName = data.lotName;
			var orgProgram = data.orgProgram;
			var programs = data.programs;
			if ( programs.length == 0 ) {
				console.log("동일한 프로그램 사용.");
				fnObj.fn.getCieRawData(lotName, orgProgram);
			} else {
				console.log("서로 다른 프로그램 사용.");
			}
			return true;
		},
		getCieRawData: function(lotName, program) {
			var param = {
				lotName: lotName
			}
			var ajaxObj = {
				type: 'get',
				url: '/cie/getCieRawdata',
				data: param,
				success: function(res) {
					console.log(res.data);
				},
				error: function(err) {
					console.log(err);
					return false;
				},	
			};
			// 요청
			$.ajax(ajaxObj);
		}
};

$(function(){
	fnObj.initView();
	fnObj.initEvent();
});



