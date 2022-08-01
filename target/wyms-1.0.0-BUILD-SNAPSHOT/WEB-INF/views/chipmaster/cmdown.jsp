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
	<title>Ĩ ������ �ٿ�ε�</title>
</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
  	<!-- Ĩ ������ �ٿ�ε� ���� ǥ�� -->
  	<h2 class="ui header">
	  <div class="content">
		    Ĩ ������ �ٿ�ε�
	  </div>
	</h2>
	<form id="form-download" method="get" action="/cm/std/download">
	  	<i class="caret right icon"></i>
	  	<label id="master-no-desc">Ĩ ������ ��ȣ : </label>
	  	<label id="master-no" name="master-no">${masterNo}</label>
	  	<input type="hidden" name="masterNo" value=${masterNo}>
	  	<br>
	  	<i class="caret right icon"></i>
	  	<label id="filename-desc">���ϸ� : </label>
	  	<label id="filename">${fileName}</label>
	  	<br>
	  	<i class="caret right icon"></i>
	  	<label id="filesize-desc">���� ������ : </label>
	  	<label id="filesize">${fileSize}KB</label>
	  	<hr>
	  	<!-- �ٿ�ε� -->
  		<button id="btn-download" class="ui primary basic button">�ٿ�ε�</button>
  	</form>
  	<!-- �����޽��� -->
  	<div id="error-msg" class="ui negative message transition hidden">
	<i class="close icon"></i>
	  <div class="header">
	    Ĩ ������ �ٿ�ε忡 ���� �Ͽ����ϴ�. 
	  </div>
	  <p>POP���� ������ Ĩ �����ʹ� ������ ������ ������ ���� ���� �ʾ� �ٿ�ε� ���� �� �����ϴ�.</p>
	</div>
	</div>
	<!-- <label id="test-text">${param}</label> -->
</div>

</body>
<!-- Custom library -->
<script src="./js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="./js/chipmaster/chipmaster.js?=${uuid}" charset="UTF-8"></script>

</html>
