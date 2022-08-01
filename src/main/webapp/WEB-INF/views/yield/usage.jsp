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
		<!-- ��ȸ���� ���� -->
		<form class="ui form">
		  <h4 class="ui dividing header">�˻�����</h4>
		  <div class="field">
		  	<!-- ��ȸ�Ⱓ -->
		    <div class="five fields">
		      <div class="field">
		        <label>��ȸ������</label>
		        <div class="ui calendar" id="schsttdt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-start-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>��ȸ������</label>
				<div class="ui calendar" id="schenddt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-end-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
				<!-- �޴��� -->
				<div class="field">
					<label>�޴�</label>
					<input type="text" id="srch-menu-name" name="" placeholder="�޴���">
				</div>
				<!-- ����ڸ� -->
				<div class="field">
					<label>����ڸ�</label>
					<input type="text" id="srch-user-name" name="" placeholder="����ڸ�">
				</div>
				<!-- ��ȸ�ϱ� -->
				<div class="field">
					<label style="visibility: hidden;">��ȸ�ϱ�</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>��ȸ�ϱ�
					</div>
				</div>
		    </div>
		  </div>
		</form>
		</div>
		<!-- ��ȸ���� �� -->
		
		<!-- �˻���� : Chart -->
		<div class="row">
			<h4 class="ui dividing header">�˻����</h4>
			<canvas id="myChart" style="width: 100%; height: 400px;"></canvas>
		</div>
		<!-- �˻���� ���� -->
		<div class="row">
			<h4 class="ui dividing header">������</h4>
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
								��ȸ�� �����Ͱ� �������� �ʽ��ϴ�.
							</td>
						</tr>
					{{/list}}
					</tbody>
					</table>
				</script> <!-- yms-menu-use-template -->
			</div><!-- yms-menu-use-container -->
		<!-- �˻���� �� -->
		</div>
	</div>
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/yield/usage.js?=${uuid}" charset="UTF-8"></script>
</html>