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
<link rel="stylesheet" type="text/css" href="js/semantic/dist/semantic.min.css">
<script src="js/semantic/dist/semantic.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.min.js" integrity="sha256-MPgtcamIpCPKRRm1ppJHkvtNBAuE71xcOM+MmQytXi8=" crossorigin="anonymous"></script>
<!-- chart js cdn -->
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
<title>I/F Check</title>

</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
  		<tf:menu></tf:menu>
  		<div id="loader1" class="ui segment">
  		<div class="row">
		<!-- 조회조건 시작 -->
		<form class="ui form">
		  <h4 class="ui dividing header">검색조건</h4>
		  <div class="field">
		  	<!-- 조회기간 -->
		    <div class="five fields">
		      <div class="field">
		        <label>조회시작일</label>
		        <div class="ui calendar" id="schsttdt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-start-date" control-tag="date-input" type="text" placeholder="조회시작일">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>조회종료일</label>
				<div class="ui calendar" id="schenddt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-end-date" control-tag="date-input" type="text" placeholder="조회종료일">
					</div>
				</div>
		      </div>
				<!-- 메뉴명 -->
				<div class="field">
					<label>메뉴</label>
					<input type="text" id="srch-menu-name" name="" placeholder="메뉴명">
				</div>
				<!-- 사용자명 -->
				<div class="field">
					<label>사용자명</label>
					<input type="text" id="srch-user-name" name="" placeholder="사용자명">
				</div>
				<!-- 조회하기 -->
				<div class="field">
					<label style="visibility: hidden;">조회하기</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>조회하기
					</div>
				</div>
		    </div>
		  </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		
		<!-- 검색결과 : Chart -->
		<div class="row">
			<h4 class="ui dividing header">검색결과</h4>
			<canvas id="myChart" style="width: 100%; height: 400px;"></canvas>
		</div>
		<!-- 검색결과 시작 -->
		<div class="row">
			<h4 class="ui dividing header">데이터</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="yms-menu-use-container">
				<script type="text/html" id="yms-menu-use-template">
					<table class="ui red table">
						<thead>
							<tr>
								<th>FACTORYNAME</th>
								<th>USERID</th>
								<th>USERNAME</th>
								<th>STARTDATE</th>
								<th>MODULEID</th>
								<th>ENDDATE</th>
								<th>MODULENAME</th>
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
							<td>{{factoryname}}</td>
							<td>{{userid}}</td>
							<td>{{username}}</td>
							<td>{{startdate}}</td>
							<td>{{moduleid}}</td>
							<td>{{enddate}}</td>
							<td>{{modulename}}</td>
						</tr>
					{{/list}}
					{{^list}}
						<tr>
							<td colspan="7">
								조회된 데이터가 존재하지 않습니다.
							</td>
						</tr>
					{{/list}}
					</tbody>
					</table>
				</script> <!-- yms-menu-use-template -->
			</div><!-- yms-menu-use-container -->
		<!-- 검색결과 끝 -->
		</div>
	</div>
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/yield/usage.js?=${uuid}" charset="UTF-8"></script>
</html>