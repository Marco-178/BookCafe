<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>

<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/clubLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
        <script>
            let limit = 5;
            let step = 5;

            document.addEventListener('DOMContentLoaded', function() {
                sortThreads();
            });

            function showMoreResults() {
                let hiddenItems = document.querySelectorAll('.hidden');

                for (let i = 0; i < step && i < hiddenItems.length; i++) {
                    hiddenItems[i].classList.remove('hidden');
                }

                if (hiddenItems.length <= step) {
                    document.getElementById('showMoreBtn').style.display = 'none';
                }
            }

            function sortThreads() {
                let order = document.getElementById("sortOrder").value;
                let threadsArray = Array.from(document.querySelectorAll(".thread-list"));

                threadsArray.sort(function(a, b) {
                    let titleA = a.querySelector(".thread-item-title").textContent.toLowerCase();
                    let titleB = b.querySelector(".thread-item-title").textContent.toLowerCase();
                    let timestampA = new Date(a.querySelector(".threadCreationLocalDateTimestamp").textContent);
                    let timestampB = new Date(b.querySelector(".threadCreationLocalDateTimestamp").textContent);

                    if (order === "newest") {
                        return timestampB - timestampA;
                    } else if (order === "oldest") {
                        return timestampA - timestampB;
                    } else if (order === "title") {
                        return titleA.localeCompare(titleB);
                    }
                });

                // Rimuovi gli elementi esistenti e riaggiungi in ordine
                let container = document.getElementById("threadContainer");
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
                                <span class="subtitle">Data di Creazione: ${clubFormattedCreationTimestamp}</span>
                                <span class="subtitle">Moderatori:
                                    <c:forEach var="moderator" items="${clubMods}">
                                         ${moderator.username}
                                    </c:forEach>
                                </span>
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
                            <label for="categoryFilter">Filtra per categoria:</label>
                            <select id="categoryFilter" onchange="filterThreads()">
                                <option value="all">Tutte le categorie</option>
                                <option value="categoria1">Categoria 1</option>
                                <option value="categoria2">Categoria 2</option>
                                <option value="categoria3">Categoria 3</option>
                            </select>
                            <label for="sortOrder">Ordina per:</label>
                            <select id="sortOrder" onchange="sortThreads()">
                                <option value="newest" selected>Più recenti</option>
                                <option value="oldest">Più vecchi</option>
                                <option value="title">Titolo</option>
                            </select>
                        </div>
                        <div id="threadContainer">
                            <c:set var="limit" value="2"/>
                            <c:set var="count" value="0"/>
                            <c:forEach var="thread" items="${threadsList}" varStatus="status">
                                <c:if test="${count < limit}">
                                    <ul class="thread-item-container thread-list" onclick="sendThreadId('${thread.threadId}');">
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
                                            <span class="subtitle ">Data di Creazione: </span>
                                            <span class="threadCreationLocalDateTimestamp">${threadsformattedCreationTimestamps[status.index]}</span>
                                        </li>
                                        <li class="thread-item">
                                            <span class="subtitle ">Numero di risposte: </span>
                                            <span class="threadMessageCount">${threadsTotalMessages[status.index]}</span>
                                        </li>
                                        <c:forEach var="moderator" items="${clubMods}">
                                            <c:if test="${loggedUser.userType == 'admin' || (loggedUser.userType == 'moderator' && (loggedUser.userId == moderator.userId) )}">
                                                <div class="right">
                                                    <img class="delete-icon icon" style="margin-bottom: 4px; z-index: 10;" onclick="event.stopPropagation(); deleteThread(${thread.threadId});" src="<%=contextPath%>/assets/images/delete.png">
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                                <c:if test="${count >= limit}">
                                    <ul class="hidden thread-item-container thread-list" onclick="sendThreadId('${thread.threadId}');">
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
                                            <span class="threadCreationLocalDateTimestamp">${threadsformattedCreationTimestamps[status.index]}</span>
                                        </li>
                                        <li class="thread-item">
                                            <span class="subtitle ">Numero di risposte: </span>
                                            <span class="threadMessageCount">${threadsTotalMessages[status.index]}</span>
                                        </li
                                        <c:forEach var="moderator" items="${clubMods}">
                                            <c:if test="${loggedUser.userType == 'admin' || (loggedUser.userType == 'moderator' && (loggedUser.userId == moderator.userId) )}">
                                                <div class="right">
                                                    <img class="delete-icon icon" style="margin-bottom: 4px; z-index: 10;" onclick="event.stopPropagation(); deleteThread(${thread.threadId});" src="<%=contextPath%>/assets/images/delete.png">
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                                <c:set var="count" value="${count + 1}" />
                            </c:forEach>
                        </div>
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
            <input type="hidden" id="clubId" name="clubId" value="${club.clubId}"/>
            <input type="hidden" name="controllerAction" value="ThreadManagement.viewThread"/>
        </form>
    </body>
</html>
