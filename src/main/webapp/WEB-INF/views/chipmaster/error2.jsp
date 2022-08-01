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
  	<!--  -->
  	<h2 class="ui center aligned icon header">
	  <i class="circular exclamation red icon"></i>
	  잘못된 요청입니다.
	  <br><div class="sub header">다운로드 가능한 파일이 존재하지 않습니다.</div>
	</h2>
	
	
</div>

</body>
<!-- Custom library -->
<script src="./js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="./js/chipmaster/chipmaster.js?=${uuid}" charset="UTF-8"></script>

</html>
