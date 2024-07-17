<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<%
    String menuActiveLink = "Home";
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/homeLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <div class="main-content">
                <c:choose>
                    <c:when test="${loggedOn}">
                        <h1 class ="greetings"> Benvenuto ${loggedUser.username}! </h1>
                        <!-- TODO: if(ci sono eventi da consigliarti) -->
                        <p> Consulta in fondo alla pagina gli eventi che potrebbero interessarti. </p>
                        <br/>
                    </c:when>
                    <c:otherwise>
                        <h1 class ="greetings"> Benvenuto! </h1>
                        <p> Con Bookcafe puoi discutere con altri lettori dei tuoi libri preferiti, iscriverti a gruppi di lettura e informarti sugli eventi di lettura più vicini a te. </p>
                        <br/>
                    </c:otherwise>
                </c:choose>
                <h1>Letture proposte questo mese</h1>
                <section class="book-grid">
                    <c:forEach var="libro" items="${books}">
                        <article class="card stacked-card featured">
                            <div class="card__content">
                                <img class="card__img" src="<%=contextPath%>${libro.urlBookcoverImage}">
                                <h2 class="card_title">${libro.title}</h2>
                                <p class="card__desc">${libro.description}</p>
                            </div>
                        </article>
                    </c:forEach>
                </section>
            </div>
        </main>
        <%@include file="/include/footer.inc"%>
    </body>
</html>
