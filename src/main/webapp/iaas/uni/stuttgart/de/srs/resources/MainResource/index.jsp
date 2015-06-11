<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script src="/srsTestService/ext/jquery/js/jquery.js"></script>
<style>
@CHARSET "US-ASCII";
/*----- Tabs -----*/
.tabs {
	width: 100%;
	display: inline-block;
}
/* 	taken from http://inspirationalpixels.com/tutorials/creating-tabs-with-html-css-and-jquery (9th June 2015) */
/*----- Tab Links -----*/
/* Clearfix */
.tab-links:after {
	display: block;
	clear: both;
	content: '';
}

.tab-links li {
	margin: 0px 5px;
	float: left;
	list-style: none;
}

.tab-links a {
	padding: 9px 15px;
	display: inline-block;
	border-radius: 3px 3px 0px 0px;
}

.tab-links a:hover {
	text-decoration: none;
}

li.active a, li.active a:hover {
	background: #fff;
	color: #4c4c4c;
}

/*----- Content of Tabs -----*/
.tab-content {
	padding: 15px;
	border-radius: 3px;
}

.tab {
	display: none;
}

.tab.active {
	display: block;
}
</style>
<script>
	// taken from http://inspirationalpixels.com/tutorials/creating-tabs-with-html-css-and-jquery (9th June 2015)
	jQuery(document).ready(
			function() {
				jQuery('.tabs .tab-links a').on(
						'click',
						function(e) {
							var currentAttrValue = jQuery(this).attr('href');

							// Show/Hide Tabs
							jQuery('.tabs ' + currentAttrValue).show()
									.siblings().hide();

							// Change/remove current tab to active
							jQuery(this).parent('li').addClass('active')
									.siblings().removeClass('active');

							e.preventDefault();
						});
			});
</script>
<title>SRS Test Service</title>
</head>
<body>

	<div class="tabs">
		<ul class="tab-links">
			<li class="active"><a href="#tab1">Subscriptions</a></li>
			<li><a href="#tab2">Situations</a></li>
			<li><a href="#tab3">Objects</a></li>
		</ul>

		<div class="tab-content">
			<div id="tab1" class="tab active">
				<p>
					<c:forEach var="sub" items="${it.subs}">
						Situation: ${sub.situationId} <br />
						Object: ${sub.objectId} <br />
						Correlation: ${sub.correlation} <br />
						Endpoint: ${sub.endpoint} <br />
						<form name="notifyForm" action="./rest" method="POST">
							<input name="Situation" type="hidden" value="${sub.situationId}">
							<input name="Object" type="hidden" value="${sub.objectId}">
							<input name="Correlation" type="hidden"
								value="${sub.correlation}"> <input name="Endpoint"
								type="hidden" value="${sub.endpoint}"> <input
								type="submit" value="Notify" />
						</form>
					</c:forEach>
				</p>
			</div>

			<div id="tab2" class="tab">
				<c:forEach var="sit" items="${it.sits}">
					Situation: ${sit.id} <br />
					ObservedProperties: <br />
					<c:forEach var="property" items="${sit.observedProperties}">
						${property},
					</c:forEach>
					<br />
					<br />
				</c:forEach>
			</div>

			<div id="tab3" class="tab">
				<c:forEach var="obj" items="${it.objs}">
					Object: ${obj.id} <br />
					Properties: <br />
					<form id="updateObjectForm" action="./rest/object" method="POST">
						<input name="objectId" value="${obj.id}" type="hidden" />
						<c:forEach var="property" items="${obj.properties}">
							${property.key} = 
							<select name="${property.key}" form="updateObjectForm">
								<c:choose>
									<c:when test="${property.value}">
										<option value="true" selected="selected">True</option>
										<option value="false">False</option>
									</c:when>
									<c:otherwise>
										<option value="true">True</option>
										<option value="false" selected="selected">False</option>
									</c:otherwise>
								</c:choose>
							</select>
						</c:forEach>
						<br /> <input type="submit" value="Update" />
					</form>
					<br />
					<br />
				</c:forEach>
			</div>

		</div>
	</div>



</body>
</html>