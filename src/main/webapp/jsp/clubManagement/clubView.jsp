<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>

<%
    String menuActiveLink = "panoramica Club";
%>
<form id="threadForm" name="threadForm" action="Dispatcher" method="post">
    <input type="hidden" id="threadId" name="threadId" value="-1"/>
    <input type="hidden" name="controllerAction" value="ThreadManagement.viewThread"/>
</form>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/searchLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
        <script>
            function sendThreadId(threadId){
                document.getElementById("threadId").value = threadId;
                document.getElementById("threadForm").submit();
            }
        </script>
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <div class="main-content">
                <section class="club-overview">
                    <h1>${club.clubName}</h1>
                    <!-- opzione iscrizione al club -->
                    <!-- link a eventi del club (una card che copre tutta la larghezza sull'evento più recente e un "vai a eventi" dove come primi risultati ci sono quelli del club) -->
                </section>
                <section>
                    <!-- discussioni del club -->
                    <ul>
                        <c:forEach var="thread" items="${threadsList}"> <!-- TODO visualizzare i primi tot risultati e continuare solo per esplicita richiesta utente -->
                            <li><a class="link" href="javascript:sendThreadId(${thread.threadId});">${thread.threadId}${thread.content}</a></li>
                            <li>${thread.category}</li>
                            <li>${thread.creationTimestamp}</li>
                            <br>
                        </c:forEach>
                    </ul>
                </section>
            </div>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
