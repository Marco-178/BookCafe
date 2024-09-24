<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/userLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
</head>
<body>
    <%@include file="/include/header.inc"%>
    <main class="container">
        <div class="main-content">
            <h1 class="user-title">Profilo di <span style="color: darkgoldenrod">${loggedUser.username}</span></h1>
            <section class="user-card user-view-grid">
                <ul>
                    <li>Prova</li>
                </ul>

                <ul>
                    <li>Prova</li>
                </ul>
            </section>
        </div>
    </main>
    <%@include file="/include/footer.inc"%>

</body>
</html>
