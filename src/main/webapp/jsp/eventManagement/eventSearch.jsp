<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Event"%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/searchLayout.css?v=<%= timestamp %>" type="text/css" media="screen">

    <script>
        function sendEventId(event_id){
            console.log("id:",event_id);
            document.eventView.eventId.value = event_id;
            document.eventView.submit();
        }
    </script>
    <style>
        .showSubscribedEvents{
            display: none;
        }
    </style>
</head>
<body>
    <%@include file="/include/header.inc"%>
    <main class="container">
        <div class="main-content">
            <section class="showSubscribedEvents"> <!--TODO if(loggedUser subscribedTo club) show section e club qui  -->
                <h1> I miei Eventi </h1>
            </section>
            <section>
                <h1> Tutti gli eventi </h1>
                <search role="search">
                    <label for="eventSearch">Inserire nome club: </label><br>
                    <input class="box" type="text" id="eventSearch"  name="eventSearch" maxlength="40" required><br>
                </search>
                <div>
                    <label for="filterEventSearch"> Filtra la ricerca, includendo solo: </label><br>
                    <input type="checkbox" id="filterEventSearch" name="filterEventSearch" value="Data creazione">
                    <p>Ordina i risultati per</p>
                    <label for="sortEventSearch"> Data creazione </label><br>
                    <input type="radio" id="sortEventSearch" name="sortEventSearch" value="Data creazione">
                </div>
                <ul>
                    <c:forEach var="event" items="${eventsList}"> <!-- TODO visualizzare i primi tot risultati e continuare solo per esplicita richiesta utente -->
                        <li><a class="link" href="javascript:sendEventId(${event.eventId});">${event.eventName}</a></li>
                        <br>
                    </c:forEach>
                </ul>
            </section>
        </div>
    </main>
    <%@include file="/include/footer.inc"%>

    <form name="eventView" method="post" action="Dispatcher?controllerAction=EventManagement.viewEvent">
        <input type="hidden" name="eventId"/>
    </form>
</body>
</html>
