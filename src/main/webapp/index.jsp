<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="tribeio">
<head><title>Tomitribe.io</title>
    <script>
        // doc base
        (function () {
            var contextPath = '<%=request.getContextPath()%>';
            var reqUrl = '<%=request.getRequestURL()%>'
                    .replace(/^http:/, '')
                    .replace(/^https:/, '')
                    .replace(/^\/\//, '')
                    .replace(/^[^\/]*/, '')
                    .replace(new RegExp('^' + contextPath, "i"), '');
            var baseUrl = document.location.pathname.replace(new RegExp(reqUrl + '$', 'i'), '');
            document.write("<base href='//" + document.location.hostname + baseUrl + "/' />");
        }());
    </script>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.3/normalize.min.css"/>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"/>
    <link rel="stylesheet"
          href="//fonts.googleapis.com/css?family=Lato:400,400italic,700,700italic,900,900italic,300italic,300"/>
    <link rel="stylesheet" href="thirdparty/highlight/default.css"/>
    <link rel="stylesheet" href="app/style/sprite.css"/>
    <link rel="stylesheet" href="app/style/main.css"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width">
</head>
<body>
<ng-view autoscroll="true"></ng-view>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script type="text/javascript" src="thirdparty/highlight/highlight.pack.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.js"></script>
<script type="text/javascript" src="app/js/app.min.js"></script>
</body>
</html>
