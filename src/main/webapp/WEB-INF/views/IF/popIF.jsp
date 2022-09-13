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
		<!-- ��ȸ���� ���� -->
		<form class="ui form">
		  <h4 class="ui dividing header">��������Է�</h4>
		  <div class="field">
		  	<!-- ��ȸ�Ⱓ -->
		    <div class="three fields">
		      <div class="field">
		        <label>��������</label>
		        <div class="ui calendar" id="start-calendar">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-start-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>���������</label>
				<div class="ui calendar" id="end-calendar">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-end-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
		    </div>
		    <div class="three fields">
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="elem-check-status">
				      		<input type="checkbox" name="elem-check-status">
				      		<label>��������</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="elm-popif-check-status">
				      		<input type="checkbox" name="elem-pop-if">
				      		<label>POP���������� �̰�</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="elm-raw-check-status">
				      		<input type="checkbox" name="elem-yield-raw">
				      		<label>����RAW����</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="elm-sum-check-status">
				      		<input type="checkbox" name="elem-yield-sum">
				      		<label>��/��/���� ������</label>
				      	</div>
			    	</div>
		    	</div>
		    <!-- </div>
		    <div class="field"> -->
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="seper-check-status">
				      		<input type="checkbox" name="seper-check-status">
				      		<label>�з�����</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="spr-popif-check-status">
				      		<input type="checkbox" name="seper-pop-if">
				      		<label>POP���������� �̰�</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="spr-raw-check-status">
				      		<input type="checkbox" name="seper-yield-raw"> 
				      		<label>����RAW����</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="spr-sum-check-status">
				      		<input type="checkbox" name="seper-yield-sum">
				      		<label>��/��/���� ������</label>
				      	</div>
			    	</div>
		    	</div>
		    <!-- </div>
		    <div class="field"> -->
		    	<div class="field">
			    	<h5 class="ui top attached header">
			    		<div class="ui checkbox" style="margin-right:10px;" id="taping-check-status">
				      		<input type="checkbox" name="taping-check-status">
				      		<label>�����μ���</label>
				      	</div>
			    	</h5>
			    	<div class="ui attached segment row-list">
			    		<div class="ui checkbox" style="margin-right:10px;" id="tpg-popif-check-status">
				      		<input type="checkbox" name="taping-pop-if">
				      		<label>POP���������� �̰�</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="tpg-raw-check-status">
				      		<input type="checkbox" name="taping-yield-raw">
				      		<label>����RAW����</label>
				      	</div>
				      	<div class="ui checkbox" style="margin-right:10px;" id="tpg-sum-check-status">
				      		<input type="checkbox" name="taping-yield-sum">
				      		<label>��/��/���� ������</label>
				      	</div>
			    	</div>
			    </div>
		    </div>
		    <div class="row">
			  	<div class="field" style="display:flex; justify-content:flex-end;">
					<div id="summary-button" class="ui button" tabindex="0">
				  		<i class="search icon"></i>
				  		������
					</div>
					<div id="test-button" class="ui button" tabindex="0">
				  		<i class="search icon"></i>
				  		�׽�Ʈ��ư
					</div>
					<!-- <div id="test-button2" class="ui button" tabindex="0">
				  	<i class="search icon"></i>
				  		�׽�Ʈ��ư2
					</div> -->
			  	</div>
		  	</div>
		  </div>
		</form>
		</div>
		<!-- ��ȸ���� �� -->
		<h5>
			<i id="status-view-icon" class="tree icon"></i>
			<a id="status-text" class="status-text">�۾�����: IDLE</a>
		</h5>
		<h4 class="ui dividing header"></h4>
		<div id="total-progress" class="ui progress">
			<div class="bar">
				<div class="progress"></div>
			</div>
			<div class="label">��ü���൵</div>
		</div> 
		<div id="detail-progress" class="ui progress">
			<div class="bar">
				<div class="progress"></div>
			</div>
			<div class="label">���δܰ����൵</div>
		</div>
		
		
		<!-- �˻���� ���� -->
		<div class="row">
			<h4 class="ui dividing header">���α�</h4>
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