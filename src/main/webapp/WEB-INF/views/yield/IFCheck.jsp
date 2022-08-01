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
		<!-- 조회조건 시작 -->
		<form class="ui form">
		  <h4 class="ui dividing header">검색조건</h4>
		  <div class="field">
		  	<!-- 조회기간 -->
		    <div class="five fields">
		      <div class="field">
		        <label>조회시작일</label>
		        <div class="ui calendar" id="schsttdt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-start-date" control-tag="date-input" type="text" placeholder="조회시작일">
					</div>
				</div>
		      </div>
		      <div class="field">
		        <label>조회종료일</label>
				<div class="ui calendar" id="schenddt" control-tag="date">
					<div class="ui input left icon">
						<i class="calendar icon"></i>
						<input id="srch-end-date" control-tag="date-input" type="text" placeholder="조회종료일">
					</div>
				</div>
		      </div>
		      <!-- 설비코드 -->
		    	<div class="field">
		    		<label>설비코드명</label>
		    		<input type="text" id="srch-eqp-id" name="" placeholder="설비코드명">
		    	</div>
		    	<!-- 제품코드 -->
		    	<div class="field">
		    		<label>제품코드</label>
		    		<input type="text" id="srch-part-id" name="" placeholder="제품코드">
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
		  	<!-- 파일명 -->
	    	<div class="field">
	    		<label>파일명</label>
	    		<input type="text" id="srch-filename" name="" placeholder="파일명(유사어 검색)">
	    	</div>
		  </div>
		  <!-- 검색하기 -->
		  <div style="width: 100%;text-align: right;">
		  	  <div id="btn-find-log" class="ui button" tabindex="0">
			  	<i class="search icon"></i>
			  	파일분석로그
		 	  </div>
			  <div id="btn-search" class="ui button" tabindex="0">
			  	<i class="search icon"></i>
			  	조회하기
		 	  </div>
	 	  </div>
		</form>
		</div>
		<!-- 조회조건 끝 -->
		
		<!-- 검색결과 시작 -->
		<div class="row">
			<h4 class="ui dividing header">검색결과</h4>
			<input type="hidden" id="hid-start-date">
			<input type="hidden" id="hid-end-date">
			<div id="yms-file-master-container">
				<script type="text/html" id="yms-file-master-template">
					<table class="ui red table">
						<thead>
							<tr>
								<th rowspan="2">LotID</th>
								<th rowspan="2">STEPSEQ</th>
								<th>TKIN시간</th>
								<th>라인명</th>
								<th>설비코드</th>
								<th rowspan="2">LOTTYPE</th>
								<th>PROGRAM</th>
								<th>전체수량</th>
								<th rowspan="2">STEPTYPE</th>
								<th rowspan="2">PGMTYPE</th>
							</tr>
							<tr>
								<th>TKOUT시간</th>
								<th>제품그룹</th>
								<th>제품코드</th>
								<th>파일명</th>
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
								조회된 데이터가 존재하지 않습니다.
							</td>
						</tr>
					{{/list}}
					</tbody>
					</table>
				</script>
			</div><!-- yms-file-master-container -->
		<!-- 검색결과 끝 -->
		</div>
	</div>
  	<div id="load-activor" class="ui unactive dimmer">
		<div class="ui loader"></div>
	</div><!-- loader1 -->
	<!-- 팝업 : YMS_FILEINDEX 조회 -->
	<div id="div-analize-result" class="ui modal" style="width:85%;">
		<div class="header">최근 로그 확인</div>
		<div class="content">
		<div class="ui form">
			<input type="hidden" id="hid-hide-focus">
			<div class="row">
				<div class="four fields">
					<!-- 설비코드 : 최근로그 -->
					<div class="field">
						<label>설비코드명</label> <input type="text" id="srch-log-eqpid" name="" placeholder="설비코드명">
					</div>
					<!-- 조회 시작일 : 최근로그 -->
					<div class="field">
						<label>조회시작일</label>
						<div class="ui calendar" id="schlogsttdt" control-tag="date">
							<div class="ui input left icon">
								<i class="calendar icon"></i> 
								<input id="srch-log-startdt" control-tag="date-input" type="text" placeholder="조회시작일">
							</div>
						</div>
					</div>
					<!-- 조회 종료일 : 최근로그 -->
					<div class="field">
						<label>조회종료일</label>
						<div class="ui calendar" id="schlogenddt" control-tag="date">
							<div class="ui input left icon">
								<i class="calendar icon"></i>
								<input id="srch-log-enddt" control-tag="date-input" type="text" placeholder="조회종료일">
							</div>
						</div>
					</div>
					<!-- 파일명 -->
			    	<div class="field">
			    		<label>파일명</label>
			    		<input type="text" id="srch-log-filename" name="" placeholder="파일명(유사어 검색)">
			    	</div>
				</div>
			</div>
			<div class="row">
				<div style="width:100%;text-align: end;padding-bottom: 1em;">
					<label style="opacity:0">조회하기</label>
					<div id="btn-log-search" class="ui button" tabindex="0"><i class="search icon"></i>조회하기</div>
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
										<th>Spec등록</th>
										<th>라인명</th>
										<th>Oper.</th>
										<th>파일업로드</th>
										<th>분석처리</th>
									</tr>
									<tr>
										<th colspan="7">Filename</th>
									</tr>
									<tr>
										<th colspan="7">오류메시지</th>
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
										조회 결과가 존재하지 않습니다.
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
		
		<!-- 파일내용조회 팝업 -->
		<div id="div-analize-file-result" class="ui modal" style="width:85%;">
		<div id="file-name-header" class="header">파일내용확인</div>
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