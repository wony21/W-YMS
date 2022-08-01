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
				<!-- 제품코드 -->
				<div class="field">
					<label>제품코드</label>
					<input type="text" id="srch-product" name="" placeholder="제품코드">
				</div>
				<!-- 조회하기 -->
				<div class="field">
					<label style="visibility: hidden;">조회하기</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>조회하기
					</div>
					<div id="btn-sql" class="ui button" tabindex="0">
						<i class="search icon"></i>SQL생성
					</div>
					<!-- 
					<div id="btn-insert-sql" class="ui button" tabindex="0">
						<i class="search icon"></i>데이터입력
					</div>
					 -->
				</div>
		    </div>
		  </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		
		<!-- sql -->
		<div class="row">
			<h4 class="ui dividing header">SQL</h4>
			<table class="ui red table">
				<thead>
					<tr>
						<th>SQL</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td id="td-sql-text">
							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<!-- 검색결과 시작 -->
		<div class="row">
			<h4 class="ui dividing header">데이터</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="outline" style="overflow:scroll;">
			<div id="yms-no-mapping-container">
				<script type="text/html" id="yms-no-mapping-template">
					<table id="operation-table" class="ui red table">
						<thead>
							<tr>
								<th>FACTORYNAME</th>
								<th>PROCESSOPERATIONNAME</th>
								<th>PROCESSOPERATIONVERSION</th>
								<th>DESCRIPTION</th>
								<th>CHECKSTATE</th>
								<th>ACTIVESTATE</th>
								<th>CREATETIME</th>
								<th>CREATEUSER</th>
								<th>CHECKOUTTIME</th>
								<th>CHECKOUTUSER</th>
								<th>PROCESSOPERATIONTYPE</th>
								<th>DETAILPROCESSOPERATIONTYPE</th>
								<th>PROCESSOPERATIONGROUP</th>
								<th>PROCESSOPERATIONUNIT</th>
								<th>ISLOGINREQUIRED</th>
								<th>DEFAULTAREANAME</th>
								<th>UPDATETIME</th>
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
							<td>{{factoryname}}</td>
							<td>{{processoperationname}}</td>
							<td>{{processoperationversion}}</td>
							<td>{{description}}</td>
							<td>{{checkstate}}</td>
							<td>{{activestate}}</td>
							<td>{{createtime}}</td>
							<td>{{createuser}}</td>
							<td>{{checkouttime}}</td>
							<td>{{checkoutuser}}</td>
							<td>{{processoperationtype}}</td>
							<td>{{detailprocessoperationtype}}</td>
							<td>{{processoperationgroup}}</td>
							<td>{{processoperationunit}}</td>
							<td>{{isloginrequired}}</td>
							<td>{{defaultareaname}}</td>
							<td>{{updatetime}}</td>
						</tr>
					{{/list}}
					{{^list}}
						<tr>
							<td colspan="17">
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
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/operation/operation.js?=${uuid}" charset="UTF-8"></script>
</html>