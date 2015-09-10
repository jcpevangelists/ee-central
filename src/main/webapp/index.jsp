<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}"/>
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}"/>
<c:set var="scheme" value="${req.scheme}"/>
<html ng-app="tribeio">
<head><title>Tomitribe.io</title>
    <base href="${fn:substring(url, fn:length(scheme) + 1, fn:length(url) - fn:length(uri))}${req.contextPath}/"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.3/normalize.min.css"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Lato:400,400italic,700,700italic,900,900italic,300italic,300"/>
    <link rel="stylesheet" href="app/style/sprite.css"/>
    <link rel="stylesheet" href="app/style/main.css"/>
    <link rel="stylesheet" href="thirdparty/prism/prism.css"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
</head>
<body>
<ng-view></ng-view>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script type="text/javascript" src="thirdparty/prism/prism.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/prism/0.0.1/prism.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.js"></script>
<script type="text/javascript" src="app/app.min.js"></script>
</body>
</html>
