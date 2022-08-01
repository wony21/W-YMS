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
<!-- <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.css"> -->
<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script> -->
<script src="/js/node_modules/chart.js/dist/chart.min.js"></script>
<script src="/js/node_modules/hammerjs/hammer.min.js"></script>
<script src="/js/node_modules/chartjs-plugin-annotation/dist/chartjs-plugin-annotation.min.js"></script>
<script src="/js/node_modules/chartjs-plugin-zoom/dist/chartjs-plugin-zoom.min.js"></script>
<title>색 좌표 조회</title>

</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
  		<div class="row">
		<!-- 조회조건 시작 -->
		<form class="ui form">
		  <h4 class="ui dividing header">검색조건</h4>
		  <div class="field">
		  	<!-- 조회기간 -->
		    <div class="five fields">
				<!-- LOTNAME -->
				<div class="field">
					<label>LOTNAME</label>
					<input type="text" id="lotname" name="" placeholder="LOT명">
				</div>
				<!-- 제품코드 -->
				<div class="field">
					<label>제품코드</label>
					<input type="text" id="part-id" name="" placeholder="제품코드">
			    </div>
				<!-- Program -->
				<div class="field">
					<label>프로그램명</label>
					<input type="text" id="program" name="" placeholder="프로그램명">
				</div>
				<!-- 조회하기 -->
				<div class="field">
					<label style="visibility: hidden;">조회하기</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>조회하기
					</div>
				</div>
				
			    <div class="field">
			    </div>
		    </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		
  		<!-- 검색결과 : Chart -->
		<div class="row">
			<h4 class="ui dividing header">검색결과</h4>
			<canvas id="cieChart" style="width: 100%; height: 650px;"></canvas>
		</div>
  	</div>
</div>

</body>
<!-- Custom library -->
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/cie/CIE_XY_V1.js?=${uuid}" charset="UTF-8"></script>
</html>