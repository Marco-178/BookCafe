<%@page session="false"%>
<%String contextPath=request.getContextPath();%>
<!DOCTYPE HTML>
<html lang="it-IT">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="0; url=<%=contextPath%>/Dispatcher">
    <script type="text/javascript">
        function onLoadHandler() {
            window.location.href = "<%=contextPath%>/Dispatcher";
        }
        window.addEventListener("load", onLoadHandler);
    </script>
    <title>Page Redirection</title>
</head>
    <body>
        Se non vieni reindirizzato automaticamente al sito, clicca qui: <a href='<%=contextPath%>/Dispatcher'>link</a>
        <img src="<%=request.getContextPath()%>/assets/images/loading-kittens.gif" style="display: block; margin: 0 auto;" alt="loading picture of kittens";>
    </body>
</html>
