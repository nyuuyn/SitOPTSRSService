<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>SRS Test Service</title>
</head>
<body>

	<h1>Subscriptions:</h1>
	<br />

	<p>
		<c:forEach var="sub" items="${it}">
		Situation: ${sub.situationId} <br />
		Object: ${sub.objectId} <br />
		Correlation: ${sub.correlation} <br />
		Endpoint: ${sub.endpoint} <br />
			<form name="notifyForm" action="./rest" method="POST">
				<input name="Situation" type="hidden" value="${sub.situationId}">
				<input name="Object" type="hidden" value="${sub.objectId}">
				<input name="Correlation" type="hidden" value="${sub.correlation}">
				<input name="Endpoint" type="hidden" value="${sub.endpoint}">
				<input type="submit" value="Notify" />
			</form>
		</c:forEach>
	</p>

</body>
</html>