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
	<link rel="shortcut icon" href="#">
	<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR&display=swap" rel="stylesheet">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/js/semantic/dist/semantic.min.css">
	<script src="/js/semantic/dist/semantic.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.min.js" integrity="sha256-MPgtcamIpCPKRRm1ppJHkvtNBAuE71xcOM+MmQytXi8=" crossorigin="anonymous"></script>
	<!-- chart js cdn -->
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.css">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.5.1/chart.min.js"></script>
	<!-- file saver cdn -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/FileSaver.js/2.0.5/FileSaver.min.js"></script>
	<title>CTQ NG 이력 조회</title>
</head>
<body>
<div>	
  	<div id="root" class="ui container">
  		<input type="hidden" id="start-date" value='${start}'>
  		<input type="hidden" id="end-date" value='${end}'>
  		<input type="hidden" id="test-prov" value='${testProv}'>
  		<input type="hidden" id="item-group" value='${itemGroup}'>
  		<input type="hidden" id="item-code" value='${itemCode}'>
		<!-- 검색결과 : Chart -->
		<div class="row">
			<div style="width:800px; height:350px;">
				<canvas id="ctq-chart"></canvas>
			</div>
		</div>
	</div>
</div>

</body>
<!-- Custom library -->
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/ctq/ctqChart.js?=${uuid}" charset="UTF-8"></script>
</html>