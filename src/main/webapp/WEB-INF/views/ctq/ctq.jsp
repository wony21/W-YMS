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
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.5.1/chart.min.js"></script>
	<title>I/F Check</title>
</head>
<body>
<div>	
  	<div id="root" class="ui container">
  		<tf:menu></tf:menu>
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
								<th>����</th>
								<th>ORDER NO</th>
								<th>�����ð�</th>
								<th>����</th>
								<th>TEST CODE</th>
								<th>��ǰ��</th>
								<th>��ǰ�ڵ�</th>
								<th>TRAY</th>
								<th>����</th>
								<th>MAX</th>
								<th>MIN</th>
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
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
								<td>{{rowidx}}</td>
								<td>{{orderNo}}</td>
								<td>{{createDttm}}</td>
								<td>{{div}}</td>
								<td>{{testCode}}</td>
								<td>{{itemName}}</td>
								<td>{{itemCode}}</td>
								<td>{{tray}}</td>
								<td>{{equip}}</td>
								<td>{{maxValue}}</td>
								<td>{{minValue}}</td>
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
		<!-- �˻���� : Chart -->
		<div class="row">
			<h4 class="ui dividing header">�˻����</h4>
			<canvas id="myChart" style="width: 100%; height: 400px;"></canvas>
		</div>
	</div>
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/ctq/ctq.js?=${uuid}" charset="UTF-8"></script>
</html>