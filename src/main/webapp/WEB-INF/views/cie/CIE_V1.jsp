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
<title>���� ������ ��ȸ</title>

</head>
<body>
<br/>
<div>	
  	<div id="root" class="ui container">
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
		        <div class="ui calendar" id="search-start-date" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="start-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>��ȸ������</label>
				<div class="ui calendar" id="search-end-date" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="end-date" control-tag="date-input" type="text" placeholder="��ȸ������">
					</div>
				</div>
		      </div>
				<!-- �޴��� -->
				<div class="field">
					<label>��ǰ�ڵ�</label>
					<input type="text" id="part-id" name="" placeholder="��ǰ�ڵ�">
				</div>
				<!-- ����ڸ� -->
				<div class="field">
					<label>�����ڵ�</label>
					<input type="text" id="eqp-id" name="" placeholder="�����ڵ�">
				</div>
				<!-- ��ȸ�ϱ� -->
				<div class="field">
					<label style="visibility: hidden;">��ȸ�ϱ�</label>
					<div id="btn-search" class="ui button" tabindex="0">
						<i class="search icon"></i>��ȸ�ϱ�
					</div>
				</div>
		    </div>
		    <!-- �ι�° ���� -->
		    <div class="five fields">
		      <div class="field">
		        <div class="ui checked checkbox">
				  <input type="checkbox" checked="true">
				  <label>LOT�� ������ ����</label>
				</div>
		      </div>
		      <div class="field">
		        <label></label>
		      </div>
				<!-- �޴��� -->
				<div class="field">
					<label></label>
				</div>
				<!-- ����ڸ� -->
				<div class="field">
					<label></label>
				</div>
				<!-- ��ȸ�ϱ� -->
				<div class="field">
				</div>
		    </div>
		  </div>
		</form>
		</div>
		<!-- ��ȸ���� �� -->
		
		<!-- �˻���� ���� -->
		<div class="row">
			<h4 class="ui dividing header">������</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="yms-tmpl-container">
				<script type="text/html" id="yms-template">
					<table class="ui red table">
						<thead>
							<tr>
								<th>����</th>
								<th>��ǰ�׷�</th>
								<th>��ǰ�ڵ�</th>
								<th>��ǰ��</th>
								<th>LOT</th>
								<th>TARGET</th>
								<th>�����ڵ�</th>
								<th>TrackIn</th>
								<th>TrackOut</th>
								<th>���α׷�</th>
								<th>��ü����</th>
								<th>��ǰ����</th>
								<th>����</th>
								<th>����ǥ</th>
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
									 data-partid="{{partid}}">����ǥ��ȸ
								</div>
							</td>
						</tr>
					{{/list}}
					{{^list}}
						<tr>
							<td colspan="13">
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
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/cie/CIE_V1.js?=${uuid}" charset="UTF-8"></script>
</html>