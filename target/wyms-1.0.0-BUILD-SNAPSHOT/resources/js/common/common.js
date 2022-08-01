/**
 * 공통 JS 함수 모음 
 * 20.03.09 : 최초생성
 */

/**
 *  yyyyMMdd 포맷으로 반환
 */
function getFormatDate(date){
    var year = date.getFullYear();              //yyyy
    var month = (1 + date.getMonth());          //M
    month = month >= 10 ? month : '0' + month;  //month 두자리로 저장
    var day = date.getDate();                   //d
    day = day >= 10 ? day : '0' + day;          //day 두자리로 저장
    return  [year, month, day].join('-');
}

function showLoading() {
	$('#load-activor').attr('class', 'ui active dimmer');
}

function hideLoading() {
	$('#load-activor').attr('class', 'ui unactive dimmer');
}

function dataURLtoFile(dataurl, fileName) {
	var arr = dataurl.split(','),
    mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[1]), 
    n = bstr.length, 
    u8arr = new Uint8Array(n);
    
	while(n--){
	    u8arr[n] = bstr.charCodeAt(n);
	}

	return new File([u8arr], fileName, {type:mime});
}
