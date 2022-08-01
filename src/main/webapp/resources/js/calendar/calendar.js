/**
 * calendar.js 
 * 
 *  20200908 최초생성
 */
let fnObj = {};

fnObj.initView = function(){
	// 달력 생성 및 초기화
	$('#inline_calendar').calendar({
		type: 'date',
		selectAdjacentDays: true,
		text: {
			days:['일','월','화','수','목','금','토'],
			months: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			monthsShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			today: '오늘',
			now: '현재',
			am: '오전',
			pm: '오후'
		},
		onChange:function(date, datestr, settings, opt) {
			if (!date) return '';
			let dateFmt = date.toISOString().slice(0, 10);
			let id = 'day-' + dateFmt;
			let deleteId = 'del-day-' + dateFmt;
			let obj = $('#select-days').find('#'+ id);
			// console.log('123');
			if ( obj.length > 0 ) {
				//console.log('found');
				//$(obj).remove();
				//nothing..
			} else {
				//console.log('not found');
				let innerHtml = '<div id="' + id + '" class="ui image label">' + dateFmt + '<i id="' + deleteId + '" class="delete icon"></i></div>';
				$('#select-days').append(innerHtml);
				// set event
				$('#' + deleteId).click(function(o){
					let parent = $(this).parent();
					// 아이템 삭제
					$(parent).remove();
					// 일수 계산
					fnObj.fn.countDays();
				});
				// 일수 계산
				fnObj.fn.countDays();
			}
		},
		onSelect:function(date, mode){
			
		}
	});
};

fnObj.initEvent = function() {
	
};

fnObj.fn = {

	countDays: function() {
		let childDiv = $('#select-days').find('div');
		$('#inp-days').val(childDiv.length);
	},
};

$(function(){
	fnObj.initView();
	fnObj.initEvent();
});



