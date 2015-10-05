<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="tribeio">
<head><title>Tomitribe.io</title>
    <script>
        // doc base
        (function () {
            var contextPath = '<%=request.getContextPath()%>';
            var result = '';
            if(document.location.href === '<%=request.getRequestURL()%>') {
                if(document.location.port) {
                    result = "//" + document.location.hostname + ":" + document.location.port + contextPath + "/";
                } else {
                    result = "//" + document.location.hostname + contextPath + "/";
                }
            } else {
                var reqUrl = '<%=request.getRequestURL()%>'
                        .replace(/^http:/, '')
                        .replace(/^https:/, '')
                        .replace(/^\/\//, '')
                        .replace(/^[^\/]*/, '')
                        .replace(new RegExp('^' + contextPath, "i"), '');
                var baseUrl = document.location.pathname.replace(new RegExp(reqUrl + '$', 'i'), '');
                if(document.location.port) {
                    result = "//" + document.location.hostname + ":" + document.location.port + baseUrl + "/";
                } else {
                    result = "//" + document.location.hostname + baseUrl + "/";
                }
            }
            document.write("<base href='" + result + "' />");
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
<script>
    // Google Analytics
    (function() {
        try {
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            ga('create', 'UA-38816470-2', 'auto');
            ga('send', 'pageview');
        } catch(e) {
            window.console.log(e);
        }
    }());
</script>
<ng-view autoscroll="true"></ng-view>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script type="text/javascript" src="thirdparty/highlight/highlight.pack.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.js"></script>
<script type="text/javascript" src="app/js/app.min.js"></script>
</body>
</html>
