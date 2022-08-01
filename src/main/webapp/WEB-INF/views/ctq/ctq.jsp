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
								<th>순번</th>
								<th>ORDER NO</th>
								<th>측정시간</th>
								<th>라인</th>
								<th>TEST CODE</th>
								<th>제품명</th>
								<th>제품코드</th>
								<th>TRAY</th>
								<th>설비</th>
								<th>MAX</th>
								<th>MIN</th>
								<th>값1</th>
								<th>값2</th>
								<th>값3</th>
								<th>값4</th>
								<th>값5</th>
								<th>값6</th>
								<th>값7</th>
								<th>값8</th>
								<th>값9</th>
								<th>값10</th>
								<th>값11</th>
								<th>값12</th>
								<th>값13</th>
								<th>값14</th>
								<th>값15</th>
								<th>값16</th>
								<th>값17</th>
								<th>값18</th>
								<th>값19</th>
								<th>값20</th>
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
		<!-- 검색결과 : Chart -->
		<div class="row">
			<h4 class="ui dividing header">검색결과</h4>
			<canvas id="myChart" style="width: 100%; height: 400px;"></canvas>
		</div>
	</div>
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/ctq/ctq.js?=${uuid}" charset="UTF-8"></script>
</html>