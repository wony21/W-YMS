/**
 * operation.js
 * 
 * 20200727 최초생성
 */
let fnObj = {};
let addSql = [];
let ymsnomappingTmpl = $('#yms-no-mapping-template').html();

let menuChart;

fnObj.initView = function() {
	// 전제 틀 90% 규정화
	$('#root').css('width', '90%');
	// UI Calendar 초기화
	$('[control-tag="date"]').calendar({
		monthFirst : false,
		type : 'date',
		formatter : {
			date : function(date, settings) {
				if (!date)
					return '';
				return getFormatDate(date);
			}
		}
	});
	// 날짜 오늘 날짜로 초기화
	$('[control-tag="date-input"]').val(getFormatDate(new Date()));

};

fnObj.initEvent = function() {
	// 검색버튼
	$('#btn-search').on('click', function(e) {
		console.log('click');
		fnObj.fn.search();
	});
	// sql 생성
	$('#btn-sql').on('click', function(e) {
		fnObj.fn.create_sql();
	});
	// insert sql 생성
	$('#btn-insert-sql').on('click', function(e) {
		fnObj.fn.insert_sql();
	});

	// 검색결과 마우스 진입 시
	$(document.body).on('mouseenter', 'td[id="td-machine"]', function(event) {
		$(this).css('cursor', 'pointer');
	});
	// 검색결과 마우스 아웃 시
	$(document.body).on('mouseleave', 'td[id="td-machine"]', function(event) {
		$(this).css('cursor', 'pointer');
	});
	// 검색결과 선택 하여 정보 조회
	$(document.body).on('click', 'td[id="td-machine"]', function(event) {
		fnObj.fn.initSearchLog();
		fnObj.fn.onEqpIdClicked($(this));
	});
	// 검색결과 마우스 진입 시 : 파일명
	$(document.body).on('mouseenter', 'td[id="td-file"]', function(event) {
		$(this).css('cursor', 'pointer');
	});
	// 검색결과 마우스 아웃 시 : 파일명
	$(document.body).on('mouseleave', 'td[id="td-file"]', function(event) {
		$(this).css('cursor', 'pointer');
	});
	// 검색결과 선택 하여 정보 조회
	$(document.body).on('click', 'td[id="td-file"]', function(event) {
		fnObj.fn.initSearchLog();
		fnObj.fn.onFileClicked($(this));
	});
	// 파일전송로그 확인
	$(document.body).on('click', '#btn-find-log', function(event) {
		fnObj.fn.initSearchLog();
		$('#div-analize-result').modal('show');
		$('[control-tag="date"]').calendar({
			monthFirst : false,
			type : 'date',
			formatter : {
				date : function(date, settings) {
					if (!date)
						return '';
					return getFormatDate(date);
				}
			}
		});
	});
	// 최근로그확인 검색
	$('#btn-log-search').on('click', function(e) {
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
	initSearchLog : function() {
		$("#srch-log-eqpid").val('');
		$("#srch-log-filename").val('');
		$('[control-tag="date"]').calendar({
			monthFirst : false,
			type : 'date',
			formatter : {
				date : function(date, settings) {
					if (!date)
						return '';
					return getFormatDate(date);
				}
			}
		});
		var html = Mustache.render(ymsfileIndexTmpl, {
			list : []
		});
		$('#yms-file-index-container').html(html);
	},
	// 검색하기
	search : function() {

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
			type : 'GET',
			url : '/api/common/mapping/nomapping',
			data : data,
			success : function(res) {
				var html = Mustache.render(ymsnomappingTmpl, {
					list : res.data
				});
				$('#yms-no-mapping-container').html(html);
				console.log(res.data);
				return true;
			},
			error : function(err) {
				console.log(err);
				alert(err);
			},
			complete : function(res) {
				$('#load-activor').attr('class', 'ui unactive dimmer');
				// var html = Mustache.render(ymsnomappingTmpl, {list: []});
				// $('#yms-no-mapping-container').html(html);
			},
		});

	},
	getSearchCond : function() {
		let srchSttDt = $('#srch-start-date').val();
		let srchEndDt = $('#srch-end-date').val();
		$('#hid-start-date').val(srchSttDt);
		$('#hid-end-date').val(srchEndDt);
		return {
			schSttDt : $('#hid-start-date').val(),
			schEndDt : $('#hid-end-date').val()
		}
	},
	// sql 생성
	create_sql : function() {

		addSql = [];
		let total_sql = '';
		let sql;
		let sql_insert_into = '';
		let sql_values = '';

		let table = $('#operation-table');
		console.log(table);
		if (table.length <= 0) {
			return;
		}

		// column명 추출
		sql_insert_into = 'INSERT INTO YMS_PROCESSOPERATIONSPEC_USER ( '
		let thead = table[0].rows[0].children;
		for ( var i in thead) {
			if (thead[i].tagName == "TH") {
				let column = thead[i].innerText;
				// console.log(column);
				sql_insert_into += column + ', ';
			}
		}
		sql_insert_into = sql_insert_into.substring(0,
				sql_insert_into.length - 2);
		sql_insert_into += ') VALUES (';

		// data 추출
		let tbody = table[0].rows;
		for ( var i in tbody) {

			if (i == 0)
				continue;

			let tr = tbody[i].children;
			for ( var j in tr) {
				if (tr[j].tagName == "TD") {
					let value = tr[j].innerText;
					if (j == 8 || j == 16) {
						sql_values += "TO_DATE('" + value
								+ "','YYYY-MM-DD HH24:MI:SS'),";
					} else {
						sql_values += "'" + value + "',";
					}
				}
			}

			if (sql_values.length > 0) {

				sql_values = sql_values.substring(0, sql_values.length - 1);
				sql = sql_insert_into + sql_values + ')';
				console.log(sql);
				addSql.push({"insertSql" : sql});
				sql = sql + ";";
				total_sql += sql + "<br><br>";
				
			}
			sql_values = '';
		}
		// $('#sql-text').attr('value', total_sql);
		$('#td-sql-text').html(total_sql);
	},
	// insert sql
	insert_sql : function() {
		console.log(addSql);
		$.ajax({
			type : 'PUT',
			contentType: "application/json",
			url : '/api/common/operation/add',
			data : JSON.stringify(addSql),
			success : function(res) {
				alert('등록이 정상 완료 되었습니다.');
			},
			error : function(err) {
				
			},
			complete : function(res) {
				
			},
		});
	},
};

$(function() {
	fnObj.initView();
	fnObj.initEvent();
});
