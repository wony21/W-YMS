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
<script src="js/node_modules/dateformat/lib/dateformat.js"></script>
<script src="js/node_modules/moment/min/moment.min.js"></script>
<link rel="stylesheet" type="text/css" href="js/semantic/dist/semantic.min.css">
<link rel="stylesheet" type="text/css" href="css/common.css">
<script src="js/semantic/dist/semantic.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.min.js" integrity="sha256-MPgtcamIpCPKRRm1ppJHkvtNBAuE71xcOM+MmQytXi8=" crossorigin="anonymous"></script>
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
		  <h4 class="ui dividing header">통계조건입력</h4>
		  <div class="field">
		  	<!-- 조회기간 -->
		    <div class="three fields">
		      <div class="field">
		        <label>통계시작일</label>
		        <div class="ui calendar" id="start-calendar">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-start-date" control-tag="date-input" type="text" placeholder="조회시작일">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>통계종료일</label>
				<div class="ui calendar" id="end-calendar">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-end-date" control-tag="date-input" type="text" placeholder="조회종료일">
					</div>
				</div>
		      </div>
		    </div>
		    <div class="three fields">
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="elem-check-status">
				      		<input type="checkbox" name="elem-check-status">
				      		<label>공정수율</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="elm-popif-check-status">
				      		<input type="checkbox" name="elem-pop-if">
				      		<label>POP원본데이터 이관</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="elm-raw-check-status">
				      		<input type="checkbox" name="elem-yield-raw">
				      		<label>수율RAW생성</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="elm-sum-check-status">
				      		<input type="checkbox" name="elem-yield-sum">
				      		<label>일/주/월별 통계생성</label>
				      	</div>
			    	</div>
		    	</div>
		    <!-- </div>
		    <div class="field"> -->
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="seper-check-status">
				      		<input type="checkbox" name="seper-check-status">
				      		<label>분류수율</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="spr-popif-check-status">
				      		<input type="checkbox" name="seper-pop-if">
				      		<label>POP원본데이터 이관</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="spr-raw-check-status">
				      		<input type="checkbox" name="seper-yield-raw"> 
				      		<label>수율RAW생성</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="spr-sum-check-status">
				      		<input type="checkbox" name="seper-yield-sum">
				      		<label>일/주/월별 통계생성</label>
				      	</div>
			    	</div>
		    	</div>
		    <!-- </div>
		    <div class="field"> -->
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="taping-check-status">
				      		<input type="checkbox" name="taping-check-status">
				      		<label>테이핑수율</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="tpg-popif-check-status">
				      		<input type="checkbox" name="taping-pop-if">
				      		<label>POP원본데이터 이관</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="tpg-raw-check-status">
				      		<input type="checkbox" name="taping-yield-raw">
				      		<label>수율RAW생성</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="tpg-sum-check-status">
				      		<input type="checkbox" name="taping-yield-sum">
				      		<label>일/주/월별 통계생성</label>
				      	</div>
			    	</div>
			    </div>
		    </div>
		    <div class="row">
			  	<div class="field" style="display:flex; justify-content:flex-end;">
					<div id="summary-button" class="ui button" tabindex="0">
				  		<i class="search icon"></i>
				  		통계수행
					</div>
					<div id="test-button" class="ui button" tabindex="0">
				  		<i class="search icon"></i>
				  		테스트버튼
					</div>
					<!-- <div id="test-button2" class="ui button" tabindex="0">
				  	<i class="search icon"></i>
				  		테스트버튼2
					</div> -->
			  	</div>
		  	</div>
		  </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		<h5>
			<i id="status-view-icon" class="tree icon"></i>
			<a id="status-text" class="status-text">작업상태: IDLE</a>
		</h5>
		<h4 class="ui dividing header"></h4>
		<div id="total-progress" class="ui progress">
			<div class="bar">
				<div class="progress"></div>
			</div>
			<div class="label">전체진행도</div>
		</div> 
		<div id="detail-progress" class="ui progress">
			<div class="bar">
				<div class="progress"></div>
			</div>
			<div class="label">세부단계진행도</div>
		</div>
		
		
		<!-- 검색결과 시작 -->
		<div class="row">
			<h4 class="ui dividing header">통계로그</h4>
			<div id="log" class="ul list">
			
			</div>
		</div>
		</div>
		
	</div><!-- ui container -->
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/popIF/popIF.js?=${uuid}" charset="UTF-8"></script>
</html>