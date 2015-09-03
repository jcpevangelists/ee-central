<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<html ng-app="tribeio">
<head><title>Tomitribe.io</title>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.3/normalize.min.css"/>
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans"/>
    <link rel="stylesheet" href="app/style/sprite.css"/>
    <link rel="stylesheet" href="app/style/main.css"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
</head>
<body>
<ng-view></ng-view>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.js"></script>
<script type="text/javascript" src="app/app.min.js"></script>
</body>
</html>
