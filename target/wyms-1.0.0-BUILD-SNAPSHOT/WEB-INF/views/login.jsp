<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="java.util.UUID" %>
<%
	String uuid = UUID.randomUUID().toString();
	request.setAttribute("uuid", uuid);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Login Page</title>
</head>
<body>
<form id="login-form" name="login-form" method="post" action="loginProcess">
	<input type="text" name="id" width="150px" /><br />
	<input type="password" name="password" width="150px" />
	<button id="btn-login" type="button">LOG-IN</button>
</form>
<!-- Custom library -->
<script src="js/boot4/jquery-3.3.1.min.js"></script>
<script src="js/login.js?=${uuid}"></script>
</body>
</html>