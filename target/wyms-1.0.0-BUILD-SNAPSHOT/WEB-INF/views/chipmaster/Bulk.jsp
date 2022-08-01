<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.util.UUID"%>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags"%>

<%
	String uuid = UUID.randomUUID().toString();
	request.setAttribute("uuid", uuid);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" href="#">
<link
	href="https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap"
	rel="stylesheet">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/js/semantic/dist/semantic.min.css">
<script src="/js/semantic/dist/semantic.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.min.js"
	integrity="sha256-MPgtcamIpCPKRRm1ppJHkvtNBAuE71xcOM+MmQytXi8="
	crossorigin="anonymous"></script>
<link rel="stylesheet" type="text/css" href="/css/chipmaster/bulk.css">
<title>칩 마스터 Bulk 등록</title>
</head>
<body>
	<div id="root" class="ui grid">
		<div class="column">
			<div class="row">
				<h4 class="ui dividing header">칩 마스터 파일 업로드</h4>
				<div class="ui form">
					<div class="field">
						<div class="two fields">
							<div class="field">
								<div id="btn-add" class="ui button basic primary">목록에 항목 추가</div>
								<div id="btn-remove" class="ui button basic negative">목록에 선택항목 제거</div>
							</div>
							<div class="field">
								<div id="btn-upload" class="ui button secondary">업로드</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<!-- 업로드 목록 -->
				<table id="upload-table" class="ui very basic collapsing celled table">
					<thead id="upload-table-header">
						<tr>
							<th>선택</th>
							<th>요청업체</th>
							<th>라인</th>
							<th>제품그룹</th>
							<th>배포상태</th>
							<th>파일경로</th>
							<th>전송결과</th>
						</tr>
					</thead>
					<tbody id="upload-table-body">
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 팝업 -->
	<div id="item-add-dlg" class="ui modal">
    	<div class="header">항목 추가</div>
    	<div class="content">
    		<form id="item-form" class="ui form segment">
    			<div class="field">
    				<label>업로드 할 마스터 파일의 기준정보를 선택 하세요.</label>
    			</div>
    			<div class="field">
    				<!-- 요청업체 -->
    				<div id="req-site-dropdown" class="ui selection dropdown">
						<input type="hidden" id="req-site" name="reqSite">
						<i class="dropdown icon"></i>
						<div id="req-site-default" class="default text">요청업체</div>
						<div id="req-site-item-box" class="menu">
						</div>
					</div>
    			</div>
    			<div class="field">
    				<!-- 라인 -->
    				<div id="req-line-dropdown" class="ui selection dropdown">
						<input type="hidden" id="req-line" name="reqLine">
						<i class="dropdown icon"></i>
						<div id="req-line-default" class="default text">라인</div>
						<div id="req-line-item-box" class="menu">
						</div>
					</div>
    			</div>
    			<div class="field">
    				<!-- 제품그룹 -->
    				<div id="req-productgroup-dropdown" class="ui selection dropdown">
						<input type="hidden" id="req-productgroup" name="reqProductGroup">
						<i class="dropdown icon"></i>
						<div id="req-productgroup-default" class="default text">제품그룹</div>
						<div id="req-productgroup-item-box" class="menu"></div>
					</div>
    			</div>
    			<div class="field">
    				<!-- 등록(배포) 상태 -->
    				<div id="req-deploy-dropdown" class="ui selection dropdown">
						<input type="hidden" id="req-deploy" name="reqDeploy">
						<i class="dropdown icon"></i>
						<div id="req-deploy-default" class="default text">등록(배포) 상태</div>
						<div id="req-deploy-item-box" class="menu"></div>
					</div>
    			</div>
    			<!-- <div class="field">
    				파일 등록
    				<input id="upload-file" type="file" name="reqFile" style="display:none;"> 
    				<button id="btn-file" class="ui blue approve button">파일선택</button>
    			</div>
    			<div class="field">
    				<label id="file-path">(파일을 선택 하세요)</label>
    			</div> -->
    		</form>
    	</div>
	    <div class="actions">
	        <button id="btn-item-add" class="ui green approve button">추가</button>
	        <button id="btn-cancel" class="ui red deny button">닫기</button>
	    </div>
	</div>
</body>
<!-- Custom library -->
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/chipmaster/bulk.js?=${uuid}" charset="UTF-8"></script>

</html>