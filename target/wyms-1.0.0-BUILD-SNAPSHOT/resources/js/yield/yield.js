/**
 * yield.js 
 * 
 *  20200306 최초생성
 */
let fnObj = {};
let ymsfilemTmpl = $('#yms-file-master-template').html();
let ymsfileIndexTmpl = $('#yms-file-index-template').html();

fnObj.initView = function(){
	// 전제 틀 90% 규정화
	$('#root').css('width', '90%');
	// UI Calendar 초기화
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
	// 날짜 오늘 날짜로 초기화
	$('[control-tag="date-input"]').val(getFormatDate(new Date()));
};

fnObj.initEvent = function() {
	// 검색버튼
	$('#btn-search').on('click', function(e){
		fnObj.fn.search();
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
		// 초기화
		initSearchLog: function() {
			$("#srch-log-eqpid").val('');
            $("#srch-log-filename").val('');
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
            var html = Mustache.render(ymsfileIndexTmpl, {list: []});
            $('#yms-file-index-container').html(html);
		},
		// 검색하기
		search: function() {
			
			let data = fnObj.fn.getSearchCond();
			if (!data.schSttDt) {
				alert('검색시작일을 입력하세요.');
				return false;
			}
			if (!data.schEndDt) {
				alert('검색종료일을 입력하세요.');
				return false;
			}
			$('#load-activor').attr('class', 'ui active dimmer');
			$.ajax({
				type: 'GET',
				url: '/api/common/yield/ymsfilem',
				data: data,
				success: function(res) {
					let proto = document.location.protocol;
					let link = document.location.host;
					res.data.forEach(function(n){
						n.link = proto + '//' + link + '/api/common/file/read?fileNm=' + n.fileName;
					});
	                var html = Mustache.render(ymsfilemTmpl, {list: res.data});
	                $('#yms-file-master-container').html(html);
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err);
				},
				complete : function(res) {
					$('#load-activor').attr('class', 'ui unactive dimmer');
				},
			});
			
		},
		getSearchCond: function() {
			let srchSttDt = $('#srch-start-date').val();
			let srchEndDt = $('#srch-end-date').val();
			$('#hid-start-date').val(srchSttDt);
			$('#hid-end-date').val(srchEndDt);
			return {
				schSttDt: $('#hid-start-date').val(),
				schEndDt: $('#hid-end-date').val(),
				equipId: $('#srch-eqp-id').val(),
				partId: $('#srch-part-id').val(),
				stepSeq: $('#srch-step-seq').val(),
				lotId : $('#srch-lotid').val(),
				fileName : $('#srch-filename').val(),
				lotType : $('#srch-lottype').val(),
				pgmName : $('#srch-program').val(),
			}
		},
		searchFileIndex : function(param) {
			let machine = param.machineName;
			let fileName = param.fileName;
			
			if(machine) {
				$('#srch-log-eqpid').val(machine);	
			}
			if ( fileName ) {
				$('#srch-log-filename').val(fileName);
			}
			
			$('#load-activor2').attr('class', 'ui active dimmer');
			$.ajax({
				type: 'GET',
				url: '/api/common/yield/ymsfileindex',
				data: param,
				success: function(res) {
					//fnObj.fn.initSearchLog();
					
					res.data.forEach(function(n){
						// Machine 스펙 정보 등록 여부 
						if (n.regSpecFlag == 'YES') {
							n.regSpecInnerHtml = '%REG%';
						} else {
							n.regSpecInnerHtml = '%NOT-REG%';
						}
						
						// File FTP 전송  
						if (n.uploadFlag == 'C') {
							n.uploadInnerHtml = '%OK%';
						} else if (n.uploadFlg == 'E') {
							n.uploadInnerHtml = '%ERR%'; 
						} else {
							n.uploadInnerHtml = '%NA%:' + n.uploadFlag;
						}
						
						// File Proc*C 처리
						if (n.procFlag == 'C') {
							n.procInnerHtml = '%OK%';
						} else if (n.procFlag == 'E') {
							n.procInnerHtml = '%ERR%'; 
						} else {
							n.procInnerHtml = '%NA%(값:' + n.procFlag + ')';
						}
						
					});
	                var html = Mustache.render(ymsfileIndexTmpl, {list: res.data});
	                html = html.replace(/%OK%/g, '<i class="green smile outline icon"></i>정상');
	                html = html.replace(/%ERR%/g, '<i class="red frown outline icon"></i>오류');
	                html = html.replace(/%NA%/g, '<i class="meh outline icon"></i>알수없음');
	                html = html.replace(/%REG%/g, '<i class="green smile outline icon"></i>등록');
	                html = html.replace(/%NOT-REG%/g, '<i class="red frown outline icon"></i>미등록');
	                $('#yms-file-index-container').html(html);
	                $('#div-analize-result').modal('show');
	                
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err.error);
				},
				complete : function(res) {
					// resize event 발생 
					window.dispatchEvent(new Event('resize'));
					
					$('#load-activor2').attr('class', 'ui unactive dimmer');
				},
			});
		},
		getSearchFileIndexCond : function () {
			return {
				schSttDt: $('#srch-log-startdt').val(),
				schEndDt: $('#srch-log-enddt').val(),
				machineName: $('#srch-log-eqpid').val(),
				fileName: $('#srch-log-filename').val(),
			}
		},
		getFile : function(param) {
			return;
			
			let fileName = param.fileName;
			
			$('#load-activor').attr('class', 'ui active dimmer');
			$.ajax({
				type: 'GET',
				url: '/api/common/file/read',
				data: param,
				success: function(res) {
					/*
					let filetext = res.message.replace(/(\n|\r\n)/g, '<br>');
					
					$('#file-name-header').text(fileName);
					$('#file-text').text(filetext);
	                $('#div-analize-file-result').modal('show');
	                */
	                return true;
				},
				error: function(err) {
					console.log(err);
					alert(err.error);
				},
				complete : function(res) {
					// resize event 발생 
					window.dispatchEvent(new Event('resize'));
					$('#load-activor').attr('class', 'ui unactive dimmer');
				},
			});
		},
		onEqpIdClicked: function (td) {
			/* 20200422 이 전 내용 백업 
			let schSttDt = $('#hid-start-date').val();
			let schEndDt = $('#hid-end-date').val();
			let lotId = $(td)[0].parentElement.cells[0].textContent;
			let partId = $(td)[0].parentElement.nextElementSibling.cells[1].textContent;
			let machineName = $(td)[0].textContent;
			
			let param = {
					schSttDt: schSttDt,
					schEndDt: schEndDt,
					//lotId: lotId,
					machineName: machineName
			}
			fnObj.fn.searchFileIndex(param);
			*/	
			console.log($(td));
			let schSttDt = $('#hid-start-date').val();
			let schEndDt = $('#hid-end-date').val();
			let machineName = $(td)[0].textContent;
			let fileName = $(td)[0].parentElement.nextElementSibling.cells[3].textContent;
			let param = {
					schSttDt: schSttDt,
					schEndDt: schEndDt,
					machineName : machineName,
					fileName: fileName
			}
			fnObj.fn.searchFileIndex(param);
		},
		onFileClicked: function (td) {
			
			let schSttDt = $('#hid-start-date').val();
			let schEndDt = $('#hid-end-date').val();
			let machineName = $(td)[0].parentElement.previousElementSibling.cells[4].textContent;
			let fileName = $(td)[0].textContent;
			
			let param = {
					//schSttDt: schSttDt,
					//schEndDt: schEndDt,
					//machineName : machineName,
					fileNm : fileName
			}
			fnObj.fn.getFile(param);
			
		},
};

$(function(){
	fnObj.initView();
	fnObj.initEvent();
});



