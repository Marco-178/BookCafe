<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>

<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/clubLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
        <script>
            let limit = 5;
            let step = 5;

            function showMoreResults() {
                // Seleziona tutti gli elementi nascosti
                let hiddenItems = document.querySelectorAll('.hidden');

                // Mostra il prossimo gruppo di elementi
                for (let i = 0; i < step && i < hiddenItems.length; i++) {
                    hiddenItems[i].classList.remove('hidden');
                }

                // Nascondi il bottone se non ci sono più elementi da mostrare
                if (hiddenItems.length <= step) {
                    document.getElementById('showMoreBtn').style.display = 'none';
                }
            }

            function sortThreads() {
                var order = document.getElementById("sortOrder").value;
                var threadsArray = Array.from(document.querySelectorAll(".thread-list"));

                threadsArray.sort(function(a, b) {
                    var titleA = a.querySelector(".thread-link").textContent.toLowerCase();
                    var titleB = b.querySelector(".thread-link").textContent.toLowerCase();
                    var timestampA = new Date(a.querySelector(".thread-item:nth-child(3)").textContent);
                    var timestampB = new Date(b.querySelector(".thread-item:nth-child(3)").textContent);

                    if (order === "newest") {
                        return timestampB - timestampA; // Ordinamento per data (nuovo a vecchio)
                    } else if (order === "oldest") {
                        return timestampA - timestampB; // Ordinamento per data (vecchio a nuovo)
                    } else if (order === "title") {
                        return titleA.localeCompare(titleB); // Ordinamento alfabetico
                    }
                });

                // Rimuovi gli elementi esistenti e riaggiungi in ordine
                var container = document.querySelector("div"); // Assume che i thread siano in un div specifico
                threadsArray.forEach(function(thread) {
                    container.appendChild(thread); // Riaggiungi l'elemento ordinato
                });
            }

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
                <section class="first-content">
                    <article class="club-overview">
                        <div class="club-overview-content">
                            <h1 class="club-title">${club.clubName}</h1>
                            <hr>
                            <div class="club-info">
                                <span class="subtitle">Data di Creazione: <br>${threadFormattedCreationTimestamp}</span>
                                <span class="subtitle">Moderatore: <br>${threadFormattedCreationTimestamp}</span>
                            </div>
                            <hr>
                            <div class="club-text">
                                <div>${club.description}</div>
                            </div>
                        </div>
                    </article>
                    <!-- opzione iscrizione al club -->
                </section>
                <hr>
                <section>
                    <h2> discussioni del club </h2>
                    <div>
                        <div>
                            <!-- Filtri -->
                            <label for="categoryFilter">Filtra per categoria:</label>
                            <select id="categoryFilter" onchange="filterThreads()">
                                <option value="all">Tutte le categorie</option>
                                <option value="categoria1">Categoria 1</option>
                                <option value="categoria2">Categoria 2</option>
                                <option value="categoria3">Categoria 3</option>
                            </select>
                            <label for="sortOrder">Ordina per:</label>
                            <select id="sortOrder" onchange="sortThreads()">
                                <option value="newest">Più recenti</option>
                                <option value="oldest">Più vecchi</option>
                                <option value="title">Titolo</option>
                            </select>
                        </div>
                        <c:set var="limit" value="2"/>
                        <c:set var="count" value="0"/>
                        <c:forEach var="thread" items="${threadsList}">
                            <c:if test="${count < limit}">
                                <ul class="thread-item-container" onclick="sendThreadId('${thread.threadId}');">
                                    <li class="thread-item">
                                        <span>Titolo: </span>
                                        <span class="thread-item-title">${thread.title}</span>
                                    </li>
                                    <hr>
                                    <li class="thread-item">
                                        <span class="subtitle">Categoria: </span>
                                            ${thread.category}
                                    </li>
                                    <li class="thread-item">
                                        <span class="subtitle">Data di Creazione: </span>
                                            ${thread.creationTimestamp}
                                    </li>
                                </ul>
                            </c:if>
                            <c:if test="${count >= limit}"> <!--risultati nascosti -->
                                <ul class="thread-item-container hidden" onclick="sendThreadId('${thread.threadId}');">
                                    <li class="thread-item">
                                        <span>Titolo: </span>
                                        <span class="thread-item-title">${thread.title}</span>
                                    </li>
                                    <hr>
                                    <li class="thread-item">
                                        <span class="subtitle">Categoria: </span>
                                            ${thread.category}
                                    </li>
                                    <li class="thread-item">
                                        <span class="subtitle">Data di Creazione: </span>
                                            ${thread.creationTimestamp}
                                    </li>
                                </ul>
                            </c:if>
                            <c:set var="count" value="${count + 1}" />
                        </c:forEach>
                        <br class="hidden">
                    </div>
                    <button id="showMoreBtn" onclick="showMoreResults()">Mostra di più</button>
                </section>
                <hr>
                <h2> Eventi del Club </h2>
                <!-- link a eventi del club (una card che copre tutta la larghezza sull'evento più recente e un "vai a eventi" dove come primi risultati ci sono quelli del club) -->
            </div>
        </main>
        <%@include file="/include/footer.inc"%>
        <form id="threadForm" name="threadForm" action="Dispatcher" method="post">
            <input type="hidden" id="threadId" name="threadId" value="-1"/>
            <input type="hidden" name="controllerAction" value="ThreadManagement.viewThread"/>
        </form>
    </body>
</html>
