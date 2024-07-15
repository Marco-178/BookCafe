<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<%
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "signUp";
%>
<html>
    <head>
        <script>

            <%@include file="/include/htmlHead.inc"%>

        </script>
        <title> BookCafé - Registrazione </title>
    </head>

    <body>
        <%@include file="/include/header.inc"%>

    </body>
</html>
