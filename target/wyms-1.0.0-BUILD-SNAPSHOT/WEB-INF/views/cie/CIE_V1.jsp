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
<title>설비 데이터 조회</title>

</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
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
		        <div class="ui calendar" id="search-start-date" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="start-date" control-tag="date-input" type="text" placeholder="조회시작일">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>조회종료일</label>
				<div class="ui calendar" id="search-end-date" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="end-date" control-tag="date-input" type="text" placeholder="조회종료일">
					</div>
				</div>
		      </div>
				<!-- 메뉴명 -->
				<div class="field">
					<label>제품코드</label>
					<input type="text" id="part-id" name="" placeholder="제품코드">
				</div>
				<!-- 사용자명 -->
				<div class="field">
					<label>설비코드</label>
					<input type="text" id="eqp-id" name="" placeholder="설비코드">
				</div>
				<!-- 조회하기 -->
				<div class="field">
					<label style="visibility: hidden;">조회하기</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>조회하기
					</div>
				</div>
		    </div>
		    <!-- 두번째 조건 -->
		    <div class="five fields">
		      <div class="field">
		        <div class="ui checked checkbox">
				  <input type="checkbox" checked="true">
				  <label>LOT별 데이터 취합</label>
				</div>
		      </div>
		      <div class="field">
		        <label></label>
		      </div>
				<!-- 메뉴명 -->
				<div class="field">
					<label></label>
				</div>
				<!-- 사용자명 -->
				<div class="field">
					<label></label>
				</div>
				<!-- 조회하기 -->
				<div class="field">
				</div>
		    </div>
		  </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		
		<!-- 검색결과 시작 -->
		<div class="row">
			<h4 class="ui dividing header">데이터</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="yms-tmpl-container">
				<script type="text/html" id="yms-template">
					<table class="ui red table">
						<thead>
							<tr>
								<th>라인</th>
								<th>제품그룹</th>
								<th>제품코드</th>
								<th>제품명</th>
								<th>LOT</th>
								<th>TARGET</th>
								<th>설비코드</th>
								<th>TrackIn</th>
								<th>TrackOut</th>
								<th>프로그램</th>
								<th>전체수량</th>
								<th>양품수량</th>
								<th>수율</th>
								<th>색좌표</th>
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
							<td>{{div}}</td>
							<td>{{productspecgroup}}</td>
							<td>{{partid}}</td>
							<td>{{product}}</td>
							<td>{{lotid}}</td>
							<td>{{target}}</td>
							<td>{{eqpid}}</td>
							<td>{{tkintime}}</td>
							<td>{{tkouttime}}</td>
							<td>{{program}}</td>
							<td>{{tqty}}</td>
							<td>{{gqty}}</td>
							<td>{{yield}}%</td>
							<td><div id="btn-cie" class="ui button blue" tabindex="0" 
									 data-lot="{{lotid}}" 
								     data-eqp="{{eqpid}}" 
									 data-pgm="{{program}}"
									 data-partid="{{partid}}">색좌표조회
								</div>
							</td>
						</tr>
					{{/list}}
					{{^list}}
						<tr>
							<td colspan="13">
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
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/cie/CIE_V1.js?=${uuid}" charset="UTF-8"></script>
</html>