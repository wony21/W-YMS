/**
 * 칩 마스터 벌크 입력
 */
var initObj = {};
var fnObj = {};

$(() => {
	initObj.initView();
	initObj.initEvent();
});

/*
 * 초기화 
 */
initObj = {
		initView : function() {
			fnInit.reqSiteDataBinding();
			fnInit.lineDataBinding();
			fnInit.productGroupDataBinding();
			fnInit.deployStatusDataBinding();
			fnInit.itemAddValidSetting();
		},
		initEvent : function() {
			
			// 목록에 항목 추가 팝업
			$('#btn-add').on('click', fnObj.showItemAdd);
			// 파일 선택
			//$('#btn-file').on('click', fnObj.showFileDlg);
			// 파일 선택 표시
			//$('#upload-file').on('change', fnObj.changeFile);
			// 항목 추가 
			$('#btn-item-add').on('click', fnObj.ItemAdd);
			// 선택항목 목록에서 제거
			$('#btn-remove').on('click', fnObj.RemoveItems);
			// 업로드
			$('#btn-upload').on('click', fnObj.UploadFiles);
			
		
		},
		initItemAddDlg : function() {
			
		},
};

fnInit = {
		// 요청업체
		reqSiteDataBinding : function () {
			$.get('/api/cm/getReqSite', function(data){
				$('#req-site-dropdown').dropdown({
					values: data.results
				});
			});
		},
		// 라인
		lineDataBinding : function () {
			var response = $.get('/api/cm/getLine', function(lineData){
				//console.log(lineData);
				$('#req-line-dropdown').dropdown({
					values: lineData.results,
					onChange: function(value, text, choice) {
						var line = value;
						$.get('/api/cm/getProductGroup?line=' + line, function(data){
							var productGroupList = data.results;
							$('#req-productgroup-dropdown').dropdown({
								values: productGroupList,
							});
							$('#req-productgroup-default').text('제품그룹');
						});
					}
				});
			});
		},
		// 제품그룹
		productGroupDataBinding : function () {
			
			$.get('/api/cm/getProductGroup', function(data){
				$('#req-productgroup-dropdown').dropdown({
					values: data.results
				});
			});
		},
		// 배포상태
		deployStatusDataBinding : function () {
			
			$.get('/api/cm/getDeployStatus', function(data){
				$('#req-deploy-dropdown').dropdown({
					values: data.results
				});
			});
		},
		// 유효성 검사(팝업)
		itemAddValidSetting : function () {
			$('#item-form').form({
				fields: {
					reqSite: {
						identifier: 'reqSite',
						rules: [
							{
								type: 'empty',
								prompt: '요청업체를 선택 하세요.',
							}
						]
					},
					reqLine: {
						identifier: 'reqLine',
						rules: [
							{
								type: 'empty',
								prompt: '라인을 선택 하세요.',
							}
						]
					},
					reqProductGroup: {
						identifier: 'reqProductGroup',
						rules: [
							{
								type: 'empty',
								prompt: '제품그룹을 선택 하세요.',
							}
						]
					},
					reqDeploy: {
						identifier: 'reqDeploy',
						rules: [
							{
								type: 'empty',
								prompt: '배포(검증) 상태를 선택 하세요.',
							}
						]
					},
				},
				inline: true,
				on: 'blur',
			});
		},
}

/*
 * 로직
 */
fnObj = {
		// 팝업
		showItemAdd : function() {
			
			$('#req-site-dropdown').dropdown('restore defaults');
			$('#req-line-dropdown').dropdown('restore defaults');
			$('#req-productgroup-dropdown').dropdown('restore defaults');
			$('#req-deploy-dropdown').dropdown('restore defaults');
			$('#file-path').text(null);
			$('#upload-file').val(null);
			
			$('#req-site-default').text('요청업체');
			$('#req-line-default').text('라인');
			$('#req-productgroup-default').text('제품그룹');
			$('#req-deploy-default').text('배포(검증)상태');
			
			$('#item-form').form('reset');
			
			$('#item-add-dlg').modal({
				autofocus: false,
				onShow: initObj.initItemAddDlg,
			}).modal('show');
			
		},
		// 파일첨부
		showFileDlg : function() {
			console.log('-- on file click --');
			var idNum = $(this).attr('data-id');
			var fileId = '#upload-file-' + idNum;
			console.log(fileId);
			$(fileId).trigger('click');
		},
		// 파일첨부(표시)
		changeFile : function() {
			console.log('-- on change --');
			var idNum = $(this).attr('data-id');
			var fileId = '#upload-file-' + idNum;
			var labelId = '#upload-label-' + idNum;
			var filePath = $(fileId).val();
			$(labelId).text(filePath);
			$(this).text('변경');
		},
		// 항목추가
		ItemAdd : function(){
			
			// Validation
			if ($('#item-form').form('validate form')) {
				
				// Add Item
				var site = $('#req-site').val();
				var line = $('#req-line').val();
				var productGroup = $('#req-productgroup').val();
				var deployStatus = $('#req-deploy').val();
				
				var rowCount = fnObj.GetRowCount();
				var uploadFileId = 'upload-file-' + rowCount;
				var uploadLabelId = 'upload-label-' + rowCount;
				
				var markup = '<tr>';
				markup += '<td><input id="file-check" type="checkbox" /></td>';
				markup += '<td>' + site + '</td>';
				markup += '<td>' + line + '</td>';
				markup += '<td>' + productGroup + '</td>';
				markup += '<td>' + deployStatus + '</td>';
				markup += '<td>';
				markup += ' <input id="' + uploadFileId + '" type="file" class="ui blue horizontal label" name="reqFile" data-id="' + rowCount + '"> ';
				markup += '</td>';
				markup += '<td><a class="ui teal label">대기</label></td>';
				markup += '</tr>';
				
				$('#upload-table-body').append(markup);
				
			} else {
				
				return false;
			}
		},
		GetRowCount : function() {
			var rowCount = $('input:checkbox[id="file-check"]').length;
			return rowCount;
		},
		// 선택항목제거
		RemoveItems : function() {
			
			var checkedCount = $('input:checkbox[id="file-check"]:checked').length;
			
			if ( checkedCount === 0 ) {
				return;
			}
			
			$('input:checkbox[id="file-check"]:checked').each(function(i, o){
				var parentTD = o.parentElement;
				var parentTR = parentTD.parentElement; 
				parentTR.remove();			
			});
		},
		UploadFiles : function() {
			
			console.log('-- upload button click --');
			$('#upload-table-body > tr').each(function(i, o){
				// tr
				var tr = $(o)[0];
				console.log($(tr));
				var reqSite = tr.cells[1].innerText;
				var reqLine = tr.cells[2].innerText;
				var reqProductGroup = tr.cells[3].innerText;
				var reqDeploy = tr.cells[4].innerText;
				var reqFile = tr.childNodes[5].children[0].files[0];
				// [0].childNodes[5].childNodes[1]
				var statusCell = $(tr)[0].cells[6];
				
				// console.log($(tr.cells[5]));
				
				console.log(reqFile);
				
				statusCell.innerHTML = '<a class="ui yellow label">전송 요청</label>';
							
				var formData = new FormData();
				formData.append('reqSite', reqSite);
				formData.append('div', reqLine);
				formData.append('productGroup', reqProductGroup);
				formData.append('status', reqDeploy);
				formData.append('file', reqFile);
				
				console.log(formData);
				
				$.ajax({
					type: 'post',
					url: '/cm/std/bulk/upload',
					data: formData,
					contentType: false,
					processData: false,
					success: function(req, res, data) {
						console.log('-- success --');
						console.log(req);
						console.log(res);
						console.log(data);
						
						// console.log('-- 전송완료 처리 --');
						$(tr)[0].cells[6].innerHTML = '<a class="ui green label">전송완료</label>';
					},
					
				});
			});
			
		}
};


