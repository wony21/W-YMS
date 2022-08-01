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
  		<!-- 조회조건 시작 -->
  		<div class="ui segment">
	  		<div class="fields">
		    	<div class="field">
					<div class="ui calendar" id="inline_calendar"></div>
		    	</div>
		    	<br />
		    	<div class="two fields">
			    	<div class="field">
			    		<div class="ui input">
							<input id="inp-days" type="text" placeholder="근무일수">
						</div>
			    	</div>
			    	<!-- 
			    	<div class="field">
			    		<div id="btn-find-log" class="ui button" tabindex="0">
						  	<i class="search icon"></i>
						  	비우기
					 	  </div>
			    	</div>
			    	 -->
		    	</div>
		    	<br />
		    	<div class="field">
		    		<div id="select-days" class="ui piled segment">
		    			
		    		</div>
		    	</div>
		    </div>
		</div>
		<!-- 조회조건 끝 -->
	</div>
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/calendar/calendar.js?=${uuid}" charset="UTF-8"></script>
</html>