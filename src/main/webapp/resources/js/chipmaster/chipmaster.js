/**
 * 20200928 최초생성
 */
let fnObj = {};

fnObj.initView = function() {
	
};

fnObj.initEvent = function() {
	// 검색버튼
	$('#btn-download').on('click', function(e) {
		console.log('click');
		fnObj.fn.download();
	});
	
	$('.message .close')
	  .on('click', function() {
	    $(this).closest('.message').transition('fade');
	  });
};

fnObj.fn = {
	// 칩 마스터 파일 다운로드 
	download : function() {
		
//		let masterNo = $('#masterNo').text();
//		let formData = new FormData();
//		formData.append('masterNo', masterNo);
//		$('#form-download').submit();
		
//		let masterNo = $('#master-no').text();
//		let data = {masterNo : masterNo};
//		
//		$.ajax({
//			type : 'GET',
//			url : '/cm/std/download',
//			data : {masterNo : masterNo},
//			xhrFields: {
//	            responseType: 'blob'
//	        },
//	        success: function (data, status, xhr) {
//
//	        	var headerName = 'Content-Disposition';
//	        	var header = xhr.getResponseHeader(headerName);
//	        	console.log(header);
//	        	var startIndex = header.indexOf("filename=") + 10; // Adjust '+ 10' if filename is not the right one.
//	        	var endIndex = header.length - 1; //Check if '- 1' is necessary
//	        	var filename = header.substring(startIndex, endIndex - 1);
//	        	console.log(filename);
//	            var a = document.createElement('a');
//	            var url = window.URL.createObjectURL(data);
//	            a.href = url;
//	            a.download = decodeURIComponent(filename);
//	            //document.body.append(a);
//	            this.element.append(a);
//	            a.click();
//	            a.remove();
//	            window.URL.revokeObjectURL(url);
//	        },
//			error : function(xhr, status, error) {
//				//console.log('--- error ---');
//				//console.log(xhr);
//				//alert(request.responseText);
//				$('#error-msg').attr('class', 'ui negative message transition');
//			},
//			complete : function(res) {
//				
//			},
//		});

	},
};

$(function() {
	fnObj.initView();
	fnObj.initEvent();
});
