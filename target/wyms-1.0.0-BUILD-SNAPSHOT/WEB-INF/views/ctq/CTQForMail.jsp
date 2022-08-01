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
	<title>CTQ NG �̷� ��ȸ</title>
</head>
<body>
<div>	
  	<div id="root" class="ui container">
  		<div id="load-activor" class="ui unactive dimmer">
			<div class="ui loader"></div>
		</div>
  		<h2 class="ui header">CTQ NG �̷�</h2>
  		<!-- �˻� ���� ���� -->
  		<div class="ui form">
  			<div class="fields">
  				<!-- ��ȸ������ -->
  				<div class="field">
  					<label>��ȸ������</label>
	  				<div class="ui calendar" id="search-start-date" control-tag="date">
						<div class="ui input left icon">
							<i class="calendar icon"></i>
							<input id="srch-start-date" control-tag="date-input" type="text" placeholder="��ȸ������">
						</div>
					</div>
	  			</div>
	  			<!-- ��ȸ������ -->
	  			<div class="field">
	  				<label>��ȸ������</label>
	  				<div class="ui calendar" id="search-end-date" control-tag="date">
						<div class="ui input left icon">
							<i class="calendar icon"></i>
							<input id="srch-end-date" control-tag="date-input" type="text" placeholder="��ȸ������">
						</div>
					</div>
	  			</div>
	  			<!-- �����׸� -->
  				<div class="field">
  					<label>�����׸�</label>
  					<div id="test-prov-combo" class="ui selection dropdown" style="min-width: 180px;">
						 <input type="hidden" id="testprov" name="testprov">
						 <i class="dropdown icon"></i>
						 <div id="test-prov-desc" class="default text">�����׸�</div>
						 <div id="test-prov-container" class="menu">
							<script type="text/html" id="test-prov-templete">
							{{#list}}
								<div class="item" data-value="{{code}}">{{descr}}</div>
							{{/list}}
							</script>
						</div>
					</div>
  				</div>
	  			<!-- ��ǰ�׷� -->
	  			<div class="field">
  					<label>��ǰ�׷�</label>
  					<div id="item-group-combo" class="ui selection dropdown">
					  <input type="hidden" id="itemgroup" name="itemgroup">
					  <i class="dropdown icon"></i>
					  <div id="item-group-desc" class="default text">��ǰ�׷�</div>
					  <div id="item-group-container" class="menu">
					    	<script type="text/html" id="item-group-templete">
							{{#list}}
								<div class="item" data-value="{{itemGroup}}">{{itemGroup}}</div>
							{{/list}}
							</script>
						</div>
					</div>
  				</div>
	  			<!-- ��ǰ -->
	  			<div class="field">
  					<label>��ǰ</label>
  					<div id="item-code-combo" class="ui selection dropdown" style="min-width: 240px;" >
					  <input type="hidden" id="itemcode" name="itemcode">
					  <i class="dropdown icon"></i>
					  <div id="item-code-desc" class="default text">��ǰ</div>
					  <div id="item-code-container" class="menu">
					    	<script type="text/html" id="item-code-templete">
							{{#list}}
								<div class="item" data-value="{{itemCode}}">{{descr}}</div>
							{{/list}}
							</script>
						</div>
					</div>
  				</div>
  				<!-- ����ǥ�� -->
	  			<div class="field">
  					<label>����ǥ��</label>
  					<div class="ui checkbox" id="show-legend" >
					  <input type="checkbox" name="show-legend">
					  <label>����ǥ��</label>
					</div>
  				</div>
  			</div>
  			<div class="fields">
  				<div class="field">
  					<div id="btn-search" class="ui button" tabindex="0">
					  	<i class="search icon"></i>
					  	��ȸ�ϱ�
					</div>
					<div id="btn-save-img" class="ui button" tabindex="0">
					  	<i class="search icon"></i>
					  	<a href="#" id="img-download" download="">�̹��� ����</a>
					</div>
					
  				</div>
  			</div>
  		</div>
  		<!-- �˻� ���� �� -->
		<!-- �˻���� : Chart -->
		<div class="row">
			<canvas id="myChart" style="width: 100%; height: 500px; margin-bottom:25px;"></canvas>
		</div>
		<!-- �˻���� : Table -->
		<div class="row">
			<div id="yms-menu-use-container">
				<script type="text/html" id="yms-menu-use-template">
					<table class="ui red table" style="font-size:0.7em;">
						<thead>
							<tr>
								<th>SITE</th>
								<th>��¥</th>
								<th>��ǰ�ڵ�</th>
								<th>��ǰ��</th>
								<th>�����׸�</th>
								<th>������ġ</th>
								<th>Ĩ��</th>
								<th>�����</th>
								<th>����MIN</th>
								<th>����MAX</th>
								<th>������MIN</th>
								<th>������MAX</th>
								<th>��1</th>
								<th>��2</th>
								<th>��3</th>
								<th>��4</th>
								<th>��5</th>
								<th>��6</th>
								<th>��7</th>
								<th>��8</th>
								<th>��9</th>
								<th>��10</th>
								<th>��11</th>
								<th>��12</th>
								<th>��13</th>
								<th>��14</th>
								<th>��15</th>
								<th>��16</th>
								<th>��17</th>
								<th>��18</th>
								<th>��19</th>
								<th>��20</th>
								<th>NG����</th>
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
								<td>{{site}}</td>
								<td>{{ctqDate}}</td>
								<td>{{itemCode}}</td>
								<td>{{itemName}}</td>
								<td>{{item1}}</td>
								<td>{{item2}}</td>
								<td>{{chipName}}</td>
								<td>{{equip}}</td>
								<td>{{minValue}}</td>
								<td>{{maxValue}}</td>
								<td></td>
								<td></td>
								<td>{{value1}}</td>
								<td>{{value2}}</td>
								<td>{{value3}}</td>
								<td>{{value4}}</td>
								<td>{{value5}}</td>
								<td>{{value6}}</td>
								<td>{{value7}}</td>
								<td>{{value8}}</td>
								<td>{{value9}}</td>
								<td>{{value10}}</td>
								<td>{{value11}}</td>
								<td>{{value12}}</td>
								<td>{{value13}}</td>
								<td>{{value14}}</td>
								<td>{{value15}}</td>
								<td>{{value16}}</td>
								<td>{{value17}}</td>
								<td>{{value18}}</td>
								<td>{{value19}}</td>
								<td>{{value20}}</td>
								<td>{{ng}}</td>
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
<script src="/js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="/js/ctq/ctq.js?=${uuid}" charset="UTF-8"></script>
</html>