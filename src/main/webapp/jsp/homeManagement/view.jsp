<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String menuActiveLink = "Home";
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/homeGrid.css?v=<%= timestamp %>" type="text/css" media="screen">
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <%if (loggedOn) {%>
            <h1 class ="greetings"> Benvenuto <%=loggedUser.getUsername()%>! </h1>
            <!-- TODO: if(ci sono eventi da consigliarti) -->
            <p> Consulta in fondo alla pagina gli eventi che potrebbero interessarti. </p>
            <br/>
            <%} else {%>
            <h1 class ="greetings"> Benvenuto! </h1>
            <p> Con Bookcafe puoi discutere con altri lettori dei tuoi libri preferiti, iscriverti a gruppi di lettura e informarti sugli eventi di lettura più vicini a te. </p>
            <br/>
            <%}%>
            <h1>Libri del mese</h1>
            <section class="book-grid">
                <%for(int i=0; i < 10; i++){ %>
                <article class="card stacked-card featured">
                    <div class="card__content">
                        <h2 class="card_title">Libro<%=i%></h2>
                        <p class="card__desc">Desc<%=i%></p>
                    </div>
                </article>
                <% } %>
            </section>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
