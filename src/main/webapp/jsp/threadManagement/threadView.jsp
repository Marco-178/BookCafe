<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
</head>
<body>
<%@include file="/include/header.inc"%>
<main class="container">
    <div class="main-content">
        <section class="thread-message">
            <h1>${thread.content}</h1>
            <p>${message.content}</p>
        </section>
        <section>
            <ul>
                <c:forEach var="message" items="${messagesList}"> <!-- TODO visualizzare i primi tot risultati e continuare solo per esplicita richiesta utente -->
                    <li>${message.content}</li>
                    <br>
                </c:forEach>
            </ul>
        </section>
    </div>
</main>
<%@include file="/include/footer.inc"%>
</body>
</html>
