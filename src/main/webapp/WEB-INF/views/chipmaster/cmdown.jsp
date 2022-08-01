<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.util.UUID" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>

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
	<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap" rel="stylesheet">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="js/semantic/dist/semantic.min.css">
	<script src="js/semantic/dist/semantic.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.min.js" integrity="sha256-MPgtcamIpCPKRRm1ppJHkvtNBAuE71xcOM+MmQytXi8=" crossorigin="anonymous"></script>
	<title>칩 마스터 다운로드</title>
</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
  	<!-- 칩 마스터 다운로드 정보 표시 -->
  	<h2 class="ui header">
	  <div class="content">
		    칩 마스터 다운로드
	  </div>
	</h2>
	<form id="form-download" method="get" action="/cm/std/download">
	  	<i class="caret right icon"></i>
	  	<label id="master-no-desc">칩 마스터 번호 : </label>
	  	<label id="master-no" name="master-no">${masterNo}</label>
	  	<input type="hidden" name="masterNo" value=${masterNo}>
	  	<br>
	  	<i class="caret right icon"></i>
	  	<label id="filename-desc">파일명 : </label>
	  	<label id="filename">${fileName}</label>
	  	<br>
	  	<i class="caret right icon"></i>
	  	<label id="filesize-desc">파일 사이즈 : </label>
	  	<label id="filesize">${fileSize}KB</label>
	  	<hr>
	  	<!-- 다운로드 -->
  		<button id="btn-download" class="ui primary basic button">다운로드</button>
  	</form>
  	<!-- 오류메시지 -->
  	<div id="error-msg" class="ui negative message transition hidden">
	<i class="close icon"></i>
	  <div class="header">
	    칩 마스터 다운로드에 실패 하였습니다. 
	  </div>
	  <p>POP에서 생성된 칩 마스터는 마스터 성적서 파일이 존재 하지 않아 다운로드 받을 수 없습니다.</p>
	</div>
	</div>
	<!-- <label id="test-text">${param}</label> -->
</div>

</body>
<!-- Custom library -->
<script src="./js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="./js/chipmaster/chipmaster.js?=${uuid}" charset="UTF-8"></script>

</html>
