/**
 * 
 */
 var fnObj = {}
 var fnPromise = {}
 var fnLog = {}
 let cmpObj = {
	elementTrayAssy: false,
	elementYieldRaw: false,
	elementYieldSum: false,
}
 
$(() => {

	initEvent();
	initView();
	
});


function initEvent() {
	// $('#summary-button').on('click', fnObj.runProcess);
	
	$('#summary-button').on('click', fnObj.runProcessAsync);
	$('#test-button').on('click', fnObj.testFunction);
	$('#test-button2').on('click', fnObj.clearFunction);
}

function initView() {
	fnObj.initCalendar();
	fnObj.initProgress();
}

fnObj = {
	initCalendar: function() {
		$('#start-calendar').calendar({
			type: 'date',
			endCalendar: $('#end-calendar'),
			monthFirst: false,
		    formatter: {
		      date: function (date, settings) {
		        if (!date) return '';
		        return getFormatDate(date);
		      }
		    },
		    text : getCalendarKorean(),
		});
		$('#start-calendar').calendar('set date', new Date());
		$('#end-calendar').calendar({
			type: 'date',
			startCalendar: $('#start-calendar'),
			monthFirst: false,
		    formatter: {
		      date: function (date, settings) {
		        if (!date) return '';
		        return getFormatDate(date);
		      }
		    },
		    text : getCalendarKorean(),
		});
		$('#end-calendar').calendar('set date', new Date());
	},
	initProgress: function() {
		$('#total-progress').progress({
			total: 3,
		});
		$('#detail-progress').progress({
			total: 100,
		});
	},
	runProcess: function() {
		fnLog.clearLog();
		
		$(this).css('pointer-events', 'none');
		$('#total-progress').removeClass('error');
		$('#detail-progress').removeClass('error');
		
		var start = $('#start-calendar').calendar('get date');
		var end = $('#end-calendar').calendar('get date');
		var startFmt = getFormatDateYMD(start);
		var endFmt = getFormatDateYMD(end);
		// console.log(`${startFmt} ${endFmt}`);
		
		var params = {
			factoryName: '8100',
			startDate: startFmt,
			endDate: endFmt,
		}
		
		$('#detail-progress').progress('increment', 20);
		
		// TB_ELEMENT_TRAY_ASSY 데이터 이관
		fnLog.addLog('TB_ELEMENT_TRAY_ASSY', 'POP DB to YMS DB 데이터 이관 실행.');
		$.post('/popIF/elementTrayAssy/day', params, function(data, status){
			console.log(data);
			cmpObj.elementTrayAssy = true;
			if (data.status !== 200) {
				$('#detail-progress').addClass('error');
				fnLog.addLog('TB_ELEMENT_TRAY_ASSY', data.error);
			} else {
				var durationFmt = Number(data.duration).toLocaleString('ko-KR');
				fnLog.addLog('TB_ELEMENT_TRAY_ASSY', `POP DB to YMS DB 데이터 이관완료.(소요시간: ${durationFmt}초)`);
			}
			$('#detail-progress').progress('increment', 80);
			$(this).css('pointer-events', 'all');
		});
		/*
		console.log('click increment!');
		$('#total-progress').progress('increment', 1);
		
		var val = $('#total-progress').data('val');
		console.log($('#total-progress'));
		*/
	},
	runProcessAsync: async function() {
		
		fnObj.setMode('start');
		
		var fctName = '8100';
		var start = $('#start-calendar').calendar('get date');
		var end = $('#end-calendar').calendar('get date');
		var startFmt = getFormatDateYMD(start);
		var endFmt = getFormatDateYMD(end);
		var days = betweenDayCount(start, end);
		
		$('#total-progress').progress('reset');
		$('#detail-progress').progress('reset');
		
		fnObj.clearFunction();
		
		var elmCheckStatus = $('#elem-check-status').checkbox('is checked');
		var sprCheckStatus = $('#seper-check-status').checkbox('is checked');
		var tpgCheckStatus = $('#taping-check-status').checkbox('is checked');
		
		$('#total-progress').progress('set total', (elmCheckStatus ? 1 : 0) + (sprCheckStatus ? 1 : 0) + (tpgCheckStatus ? 1 : 0));
		
		console.log(`${elmCheckStatus} ${sprCheckStatus} ${tpgCheckStatus}`);

		if (elmCheckStatus) {
			
			var elmPOPDB = $('#elm-popif-check-status').checkbox('is checked');
			var elmRaw = $('#elm-raw-check-status').checkbox('is checked');
			var elmSum = $('#elm-sum-check-status').checkbox('is checked'); 
			
			$('#detail-progress').progress('set total', (elmPOPDB ? 1 : 0) + (elmRaw ? days+1 : 0) + (elmSum ? days+1 : 0));
			
			if (elmPOPDB) {
				fnLog.addLog('[POPDB] > [YMSDB]',`TB_ELEMENT_TRAY_ASSY 데이터를 POP에서 가져옵니다...(시작일: ${startFmt} 종료일: ${endFmt})`);
				var result = await fnPromise.requestElementTrayAssy(fctName, startFmt, endFmt);
				if (result.status == 200) {
					fnLog.addLog('[POPDB] > [YMSDB]',`TB_ELEMENT_TRAY_ASSY 데이터를 POP에서 정상적으로 가져왔습니다.(시작일: ${startFmt} 종료일: ${endFmt})`);
				} else {
					fnLog.addErrorLog('[POPDB] > [YMSDB]','TB_ELEMENT_TRAY_ASSY 데이터를 POP에서 가져오기를 실패하였습니다. <br />' + data.message);
					fnObj.setMode('end');
					return false;
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			if (elmRaw) {
				fnLog.addLog('[YMS 공정수율 RAW생성]',`일별 공정수율 RAW 데이터를 생성합니다...`);
				
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[공정수율RAW생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestElementYieldRaw(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[공정수율RAW생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[공정수율RAW생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
			}
			
			if (elmSum) {
				fnLog.addLog('[공정수율 통계생성]',`일별 공정수율 통계(일/주/월) 데이터를 생성합니다...`);
				
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[공정수율 통계생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestElementYieldPeriod(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[공정수율 통계생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[공정수율 통계생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			$('#total-progress').progress('increment', 1);
		}
		
		if (sprCheckStatus) {
			
			var sprPOPDB = $('#spr-popif-check-status').checkbox('is checked');
			var sprRaw = $('#spr-raw-check-status').checkbox('is checked');
			var sprSum = $('#spr-sum-check-status').checkbox('is checked'); 
			
			$('#detail-progress').progress('reset');
			$('#detail-progress').progress('set total', (sprPOPDB ? 1 : 0) + (sprRaw ? days+1 : 0) + (sprSum ? days+1 : 0));
			
			if (sprPOPDB) {
				console.log('seperator pop i/f.');
				fnLog.addLog('[POPDB] > [YMSDB]',`TB_SEPERATOR_ASSY 데이터를 POP에서 가져옵니다...(시작일: ${startFmt} 종료일: ${endFmt})`);
				var result = await fnPromise.requestSeperatorAssy(fctName, startFmt, endFmt);
				if (result.status == 200) {
					fnLog.addLog('[POPDB] > [YMSDB]',`TB_SEPERATOR_ASSY 데이터를 POP에서 정상적으로 가져왔습니다.(시작일: ${startFmt} 종료일: ${endFmt})`);
				} else {
					fnLog.addErrorLog('[POPDB] > [YMSDB]','TB_SEPERATOR_ASSY 데이터를 POP에서 가져오기를 실패하였습니다. <br />' + data.message);
					fnObj.setMode('end');
					return false;
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			if (sprRaw) {
				console.log('create seperator raw data.');
				fnLog.addLog('[YMS 분류수율 RAW생성]',`일별 분류수율 RAW 데이터를 생성합니다...`);
				
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[분류수율RAW생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestSeperatorYieldRaw(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[분류수율RAW생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[분류수율RAW생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
			}
			
			if (sprSum) {
				console.log('create seperator sum data.');
				fnLog.addLog('[분류수율 통계생성]',`일별 분류수율 통계(일/주/월) 데이터를 생성합니다...`);
				
				console.log(fctDate);
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[분류수율 통계생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestSeperatorYieldPeriod(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[분류수율 통계생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[분류수율 통계생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			$('#total-progress').progress('increment', 1);
		}
		
		if (tpgCheckStatus) {
			
			var tpgPOPDB = $('#tpg-popif-check-status').checkbox('is checked');
			var tpgRaw = $('#tpg-raw-check-status').checkbox('is checked');
			var tpgSum = $('#tpg-sum-check-status').checkbox('is checked');
			
			$('#detail-progress').progress('reset');
			$('#detail-progress').progress('set total', (tpgPOPDB ? 1 : 0) + (tpgRaw ? days+1 : 0) + (tpgSum ? days+1 : 0));
			
			if (tpgPOPDB) {
				fnLog.addLog('[POPDB] > [YMSDB]',`TB_TAPING_ASSY 데이터를 POP에서 가져옵니다...(시작일: ${startFmt} 종료일: ${endFmt})`);
				var result = await fnPromise.requestTapingAssy(fctName, startFmt, endFmt);
				if (result.status == 200) {
					fnLog.addLog('[POPDB] > [YMSDB]',`TB_TAPING_ASSY 데이터를 POP에서 정상적으로 가져왔습니다.(시작일: ${startFmt} 종료일: ${endFmt})`);
				} else {
					fnLog.addErrorLog('[POPDB] > [YMSDB]','TB_TAPING_ASSY 데이터를 POP에서 가져오기를 실패하였습니다. <br />' + data.message);
					fnObj.setMode('end');
					return false;
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			if (tpgRaw) {
				fnLog.addLog('[YMS 테이핑수율 RAW생성]',`일별 테이핑수율 RAW 데이터를 생성합니다...`);
				
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[테이핑수율RAW생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestTapingYieldRaw(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[테이핑수율RAW생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[테이핑수율RAW생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
			}
			
			if (tpgSum) {
				fnLog.addLog('[테이핑수율 통계생성]',`일별 테이핑수율 통계(일/주/월) 데이터를 생성합니다...`);
				for(var i=0; i<=days; i++) {
					var fctDate = new Date(start.valueOf());
					fctDate.setDate(fctDate.getDate() + i);
					var fctDateFmt = getFormatDateYMD(fctDate);
					fnLog.addLog(`[테이핑수율 통계생성]`, `${fctDateFmt} 생성 중...`);
					var rawRst = await fnPromise.requestTapingYieldPeriod(fctName, fctDateFmt);
					$('#detail-progress').progress('increment', 1);
					if (rawRst.status == 200) {
						fnLog.addLog(`[테이핑수율 통계생성]`, `${fctDateFmt} 생성 완료`);
					} else {
						fnLog.addErrorLog(`[테이핑수율 통계생성]`, `${fctDateFmt} 생성 오류. <br>` + rawRst.message);
						fnObj.setMode('end');
						return false;
					}
				}
				$('#detail-progress').progress('increment', 1);
			}
			
			$('#total-progress').progress('increment', 1);
		}
		
		fnLog.addLog('[작업종료]', '모든 작업 진행이 종료 되었습니다.');
		
		fnObj.setMode('end');
	},
	testFunction: function() {
		// fnLog.addLog('test', '테스트 로그 입니다.');
		var start = $('#start-calendar').calendar('get date');
		console.log(start);
		
		var fctDate = new Date(start.valueOf());
		
		
	},
	clearFunction: function() {
		fnLog.clearLog();
	},
	checkComplete: function() {
		if (cmpObj.elementTrayAssy && cmpObj.elementTrayAssy && cmpObj.elementTrayAssy) {
			fnLog.addLog('Compelted', '모든 작업 진행이 종료 되었습니다.');
		}
	},
	setMode: function(status) {
		if ( status === 'start') {
			$('#summary-button').css('pointer-events', 'none');
			$('#status-view-icon').attr('class', 'spinner loading icon');
			$('#status-text').text('작업상태: DO WORKING...');
		} else if ( status === 'end' ) {
			$('#summary-button').css('pointer-events', 'all');
			$('#status-view-icon').attr('class', 'tree icon');
			$('#status-text').text('작업상태: IDLE');
		}
	},
}

fnPromise = {
	requestElementTrayAssy: function(factoryName, startDate, endDate) {
		return new Promise((resolve, reject) => {
			console.log(`${factoryName} ${startDate} ${endDate}`);
			var params = {
				factoryName: factoryName,
				startDate: startDate,
				endDate: endDate,
			}
			var url = '/popIF/elementTrayAssy/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err.data);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestElementYieldRaw: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/elementYieldRaw/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestElementYieldPeriod: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/elementYieldPeriod/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestSeperatorAssy: function(factoryName, startDate, endDate) {
		return new Promise((resolve, reject) => {
			console.log(`${factoryName} ${startDate} ${endDate}`);
			var params = {
				factoryName: factoryName,
				startDate: startDate,
				endDate: endDate,
			}
			var url = '/popIF/seperatorAssy/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err.data);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestSeperatorYieldRaw: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/seperatorYieldRaw/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestSeperatorYieldPeriod: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/seperatorYieldPeriod/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestTapingAssy: function(factoryName, startDate, endDate) {
		return new Promise((resolve, reject) => {
			console.log(`${factoryName} ${startDate} ${endDate}`);
			var params = {
				factoryName: factoryName,
				startDate: startDate,
				endDate: endDate,
			}
			var url = '/popIF/tapingAssy/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err.data);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestTapingYieldRaw: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/tapingYieldRaw/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
	requestTapingYieldPeriod: function(factoryName, factoryDate) {
		return new Promise((resolve, reject) => {
			var params = {
				factoryName: factoryName,
				factoryDate: factoryDate,
			}
			var url = '/popIF/tapingYieldPeriod/day';
			
			$.ajax({
				url: url,
				data: params,
				type: 'post',
				async: true,
				success: function(data) {
					resolve(data);
				},
				error: function(err) {
					reject(err);
				},
				complete: function(data, textStatus) {
					console.log('complate');
				}
			});
		});
	},
}

fnLog = {
	addLog: function(header, message) {
		var now = new Date();
		var nowFmt = dateFormat(now, 'yyyy-mm-dd hh:MM:ss.TT');
		var item = `
			<a class="item">
				<i class="right triangle icon"></i>
				<div class="content">
					<div class="header">[${nowFmt}] ${header}</div>
					<div class="description">${message}</div>
				</div>
			</a>
		`;
		
		$('#log').append(item);
	},
	addErrorLog: function(header, message) {
		var now = new Date();
		var nowFmt = dateFormat(now, 'yyyy-mm-dd hh:MM:ss.TT');
		var item = `
			<a class="item">
				<i class="right triangle icon"></i>
				<div class="content">
					<div class="header error">[${nowFmt}] ${header}</div>
					<div class="description error">${message}</div>
				</div>
			</a>
		`;
		
		$('#log').append(item);
	},
	clearLog: function() {
		$('#log').empty();
	}
}
