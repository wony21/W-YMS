<%@ page import="com.compact.base.common.SessionUtils"%>
<%@ page import="com.compact.base.security.CustomUserDetails" %>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="java.util.UUID" %>
<%
	String uuid = UUID.randomUUID().toString();
	request.setAttribute("uuid", uuid);
	
	CustomUserDetails userDetails = SessionUtils.getCurrentUser();
	String userId = userDetails.getUserCd();
	String userNm = userDetails.getUsername();
	request.setAttribute("userId", userId);
	request.setAttribute("userNm", userNm);
%>
<html>
<head>
	<title></title>
</head>
<body>
<h1>
	Login
</h1>
<P>
USER ID : ${userId} <br />
USER NAME : ${userNm} <br />
</P>
</body>
</html>
