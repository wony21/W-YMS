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
		      <!-- �����ڵ� -->
		    	<div class="field">
		    		<label>�����ڵ��</label>
		    		<input type="text" id="srch-eqp-id" name="" placeholder="�����ڵ��">
		    	</div>
		    	<!-- ��ǰ�ڵ� -->
		    	<div class="field">
		    		<label>��ǰ�ڵ�</label>
		    		<input type="text" id="srch-part-id" name="" placeholder="��ǰ�ڵ�">
		    	</div>
		    	<!-- stepseq -->
		    	<div class="field">
		    		<label>STEPSEQ</label>
		    		<input type="text" id="srch-step-seq" name="" placeholder="STEPSEQ">
		    	</div>
		    </div>
		  </div>
		  <div class="four fields">
		  	<!-- LOTID -->
	    	<div class="field">
	    		<label>LOTID</label>
	    		<input type="text" id="srch-lotid" name="" placeholder="LOTID">
	    	</div>
	    	<!-- LOTTYPE -->
	    	<div class="field">
	    		<label>LOTTYPE</label>
	    		<input type="text" id="srch-lottype" name="" placeholder="LOTTYPE">
	    	</div>
	    	<!-- PROGRAM -->
	    	<div class="field">
	    		<label>PROGRAM</label>
	    		<input type="text" id="srch-program" name="" placeholder="PROGRAM">
	    	</div>
		  	<!-- ���ϸ� -->
	    	<div class="field">
	    		<label>���ϸ�</label>
	    		<input type="text" id="srch-filename" name="" placeholder="���ϸ�(����� �˻�)">
	    	</div>
		  </div>
		  <!-- �˻��ϱ� -->
		  <div style="width: 100%;text-align: right;">
		  	  <div id="btn-find-log" class="ui button" tabindex="0">
			  	<i class="search icon"></i>
			  	���Ϻм��α�
		 	  </div>
			  <div id="btn-search" class="ui button" tabindex="0">
			  	<i class="search icon"></i>
			  	��ȸ�ϱ�
		 	  </div>
	 	  </div>
		</form>
		</div>
		<!-- ��ȸ���� �� -->
		
		<!-- �˻���� ���� -->
		<div class="row">
			<h4 class="ui dividing header">�˻����</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="yms-file-master-container">
				<script type="text/html" id="yms-file-master-template">
					<table class="ui red table">
						<thead>
							<tr>
								<th rowspan="2">LotID</th>
								<th rowspan="2">STEPSEQ</th>
								<th>TKIN�ð�</th>
								<th>���θ�</th>
								<th>�����ڵ�</th>
								<th rowspan="2">LOTTYPE</th>
								<th>PROGRAM</th>
								<th>��ü����</th>
								<th rowspan="2">STEPTYPE</th>
								<th rowspan="2">PGMTYPE</th>
							</tr>
							<tr>
								<th>TKOUT�ð�</th>
								<th>��ǰ�׷�</th>
								<th>��ǰ�ڵ�</th>
								<th>���ϸ�</th>
								<th>GOOD</th>
							</tr>
						</thead>
					<tbody>
					{{#list}}
						<tr>
							<td rowspan="2">{{lotId}}</td>
							<td rowspan="2">{{stepSeq}}</td>
							<td>{{tkInTime}}</td>
							<td>{{line}}</td>
							<td id="td-machine"><i class="primary info circle icon"></i>{{eqpId}}</td>
							<td rowspan="2">{{lotType}}</td>
							<td>{{program}}</td>
							<td>{{tQty}}</td>
							<td rowspan="2">{{stepType}}</td>
							<td rowspan="2">{{pgmType}}</td>
						</tr>
						<tr>
							<td>{{tkOutTime}}</td>
							<td>{{productSpecGroup}}</td>
							<td>{{partId}}</td>
							<td id="td-file"><a href="{{link}}"><i class="primary file alternate icon"></i>{{fileName}}</a></td>
							<td>{{gQty}}</td>
						</tr>
					{{/list}}
					{{^list}}
						<tr>
							<td colspan="12">
								��ȸ�� �����Ͱ� �������� �ʽ��ϴ�.
							</td>
						</tr>
					{{/list}}
					</tbody>
					</table>
				</script>
			</div><!-- yms-file-master-container -->
		<!-- �˻���� �� -->
		</div>
	</div>
  	<div id="load-activor" class="ui unactive dimmer">
		<div class="ui loader"></div>
	</div><!-- loader1 -->
	<!-- �˾� : YMS_FILEINDEX ��ȸ -->
	<div id="div-analize-result" class="ui modal" style="width:85%;">
		<div class="header">�ֱ� �α� Ȯ��</div>
		<div class="content">
		<div class="ui form">
			<input type="hidden" id="hid-hide-focus">
			<div class="row">
				<div class="four fields">
					<!-- �����ڵ� : �ֱٷα� -->
					<div class="field">
						<label>�����ڵ��</label> <input type="text" id="srch-log-eqpid" name="" placeholder="�����ڵ��">
					</div>
					<!-- ��ȸ ������ : �ֱٷα� -->
					<div class="field">
						<label>��ȸ������</label>
						<div class="ui calendar" id="schlogsttdt" control-tag="date">
							<div class="ui input left icon">
								<i class="calendar icon"></i> 
								<input id="srch-log-startdt" control-tag="date-input" type="text" placeholder="��ȸ������">
							</div>
						</div>
					</div>
					<!-- ��ȸ ������ : �ֱٷα� -->
					<div class="field">
						<label>��ȸ������</label>
						<div class="ui calendar" id="schlogenddt" control-tag="date">
							<div class="ui input left icon">
								<i class="calendar icon"></i>
								<input id="srch-log-enddt" control-tag="date-input" type="text" placeholder="��ȸ������">
							</div>
						</div>
					</div>
					<!-- ���ϸ� -->
			    	<div class="field">
			    		<label>���ϸ�</label>
			    		<input type="text" id="srch-log-filename" name="" placeholder="���ϸ�(����� �˻�)">
			    	</div>
				</div>
			</div>
			<div class="row">
				<div style="width:100%;text-align: end;padding-bottom: 1em;">
					<label style="opacity:0">��ȸ�ϱ�</label>
					<div id="btn-log-search" class="ui button" tabindex="0"><i class="search icon"></i>��ȸ�ϱ�</div>
				</div>
			</div>
			<div class="row">
				<div id="loader2" class="ui segment">
				<div id="yms-file-index-container">
					<script type="text/html" id="yms-file-index-template">
							<table class="ui blue table">
								<thead>
									<tr>
										<th rowspan="3">Category</th>
										<th>CreateTime</th>
										<th>MachineName</th>
										<th>Spec���</th>
										<th>���θ�</th>
										<th>Oper.</th>
										<th>���Ͼ��ε�</th>
										<th>�м�ó��</th>
									</tr>
									<tr>
										<th colspan="7">Filename</th>
									</tr>
									<tr>
										<th colspan="7">�����޽���</th>
									</tr>
								</thead>
							<tbody>
							{{#list}}
								<tr>
									<td rowspan="3">{{category}}</td>
									<td>{{createTime}}</td>
									<td>{{machineName}}</td>
									<td>{{regSpecInnerHtml}}</td>
									<td>{{line}}</td>
									<td>{{operName}}</td>
									<td>{{uploadInnerHtml}}</td>
									<td>{{procInnerHtml}}</td>
								</tr>
								<tr>
									<td colspan="7">{{fileName}}</td>
								</tr>
								<tr>
									<td colspan="7" class="ui medium red text">{{errMsg}}</td>
								</tr>
							{{/list}}
							{{^list}}
								<tr>
									<td colspan="7">
										��ȸ ����� �������� �ʽ��ϴ�.
									</td>
								</tr>
							{{/list}}
							</tbody>
							</table>
						</script>
					</div><!-- yms-file-index-container -->
						  <div id="load-activor2" class="ui unactive dimmer">
						    <div class="ui loader"></div>
						  </div>
					</div> <!-- loader2 -->
				</div> <!-- row -->
			</div> <!-- ui form -->
			</div><!-- content -->
		</div><!-- div-analize-result -->
		
		<!-- ���ϳ�����ȸ �˾� -->
		<div id="div-analize-file-result" class="ui modal" style="width:85%;">
		<div id="file-name-header" class="header">���ϳ���Ȯ��</div>
		<div class="content">
		<div class="ui form">
			<input type="hidden" id="hid-hide-focus">
			<div class="row">
				<label id="file-text"></label>
				</div> <!-- row -->
			</div> <!-- ui form -->
			</div><!-- content -->
		</div><!-- div-analize-result -->
		
	</div><!-- ui container -->
</div>

</body>
<!-- Custom library -->
<script src="js/common/common.js?=${uuid}" charset="UTF-8"></script>
<script src="js/yield/yield.js?=${uuid}" charset="UTF-8"></script>
</html>