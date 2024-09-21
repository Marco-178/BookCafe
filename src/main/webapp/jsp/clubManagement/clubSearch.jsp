<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/searchLayout.css?v=<%= timestamp %>" type="text/css" media="screen">

    <script>
        function sendClubId(club_id){
            console.log("id:",club_id);
            document.clubView.clubId.value = club_id;
            document.clubView.submit();
        }
    </script>
    <style>
        .showSubscribedClubs{
            display: none;
        }
    </style>
</head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <div class="main-content">
                <section class="showSubscribedClubs"> <!--TODO if(loggedUser subscribedTo club) show section e club qui  -->
                    <h1> I miei club </h1>
                </section>
                <section>
                    <h1> Tutti i club </h1>
                    <search role="search">
                        <label for="clubSearch">Inserire nome club: </label><br>
                        <input class="box" type="text" id="clubSearch"  name="clubSearch" maxlength="40" required><br>
                    </search>
                    <div>
                        <label for="filterClubSearch"> Filtra la ricerca, includendo solo: </label><br>
                        <input type="checkbox" id="filterClubSearch" name="filterClubSearch" value="Data creazione">
                        <p>Ordina i risultati per</p>
                        <label for="sortClubSearch"> Data creazione </label><br>
                        <input type="radio" id="sortClubSearch" name="sortClubSearch" value="Data creazione">
                    </div>
                    <ul>
                        <c:forEach var="club" items="${clubsList}"> <!-- TODO visualizzare i primi tot risultati e continuare solo per esplicita richiesta utente -->
                            <li><a class="link" href="javascript:sendClubId(${club.clubId});">${club.clubName}</a></li>
                            <li>${club.creationDate}</li>
                            <li>${club.description}</li>
                            <br>
                        </c:forEach>
                    </ul>
                </section>
            </div>
        </main>
        <%@include file="/include/footer.inc"%>

        <form name="clubView" method="post" action="Dispatcher?controllerAction=ClubManagement.viewClub">
            <input type="hidden" name="clubId"/>
        </form>
    </body>
</html>
