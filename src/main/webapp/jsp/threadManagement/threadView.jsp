<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Club"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/threadLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
        <script>
            document.addEventListener('DOMContentLoaded', () => {

                const modal = document.querySelector('#messageDialog');
                const openModal = document.querySelector('.open-button');
                const closeModal = document.querySelector('.close-button');

                openModal.addEventListener('click', () => {
                    modal.showModal();
                });

                closeModal.addEventListener('click', () => {
                    modal.close();
                });
            });

            console.log("thread userID: ", ${thread.userId});

            function sendComment(){
                const commentForm = document.getElementById("commentForm");
                const writeCommentForm = document.getElementById("writeComment");
                writeCommentForm.commentTextArea.value = commentForm.commentTextArea.value;
                writeCommentForm.threadId.value = "${thread.threadId}";
                writeCommentForm.submit();
            }

            function deleteComment(messageId){
                const deleteCommentForm = document.getElementById("deleteComment");
                deleteCommentForm.threadId.value = "${thread.threadId}";
                deleteCommentForm.messageId.value = messageId;
                deleteCommentForm.submit();
            }

            function toChatterProfile(selectedUserId){
                document.toChatterProfile.profileId.value = selectedUserId;
                document.toChatterProfile.submit();
            }

            function sendClubId(club_id){
                console.log("id:",club_id);
                document.clubView.clubId.value = club_id;
                document.clubView.submit();
            }
        </script>
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <a class="link" style="margin-left: 5%; margin-top: 20px; display:inline-block" onclick="sendClubId(${clubId})"> &#x2B05 Torna al club</a>
            <div class="main-content">
                <section>
                    <h1 class="first-content thread-title">${thread.title}</h1>
                    <article>
                        <img class="threadUser-image" src="<%=contextPath%>${threadUser.urlProfilePicture}" alt="image of user: ${threadUser.username}" onclick="toChatterProfile(${threadUser.userId});">
                        <span>${threadUser.username}</span>
                        <div class="thread-card">
                            <div class="thread-content">
                                <div class="thread-info">
                                    <span class="subtitle">Categoria: <br>${thread.category}</span>
                                    <span class="subtitle">Data di Creazione: <br>${threadFormattedCreationTimestamp}</span>
                                    <span class="subtitle">Ultima risposta: <br>${formattedLastReply}</span>
                                </div>
                                <hr>
                                <div class="thread-text">
                                    <div>${thread.content}</div>
                                </div>
                            </div>
                        </div>
                    </article>
                </section>
                <hr>
                <section>
                    <div style="margin-bottom: 50px;">
                        <c:forEach var="message" items="${messages}" varStatus="status"> <!-- TODO visualizzare i primi tot risultati e continuare solo per esplicita richiesta utente -->
                            <c:set var="chatter" value="${chatters[status.index]}"/>
                            <article>
                                <img class="chatter-image" src="<%=contextPath%>${chatter.urlProfilePicture}" alt="image of user: ${chatter.username}" onclick="toChatterProfile(${chatter.userId});">
                                <span style="margin-left: 1%">${chatter.username}</span>
                                <div class="message-card">
                                    <div class="message-content">
                                        <div class="message-info">
                                            <span class="subtitle"> Data di Creazione: ${formattedCreationTimestamps[status.index]}</span>
                                            <c:forEach var="moderator" items="${clubMods}">
                                                <c:if test="${loggedUser.userType == 'admin' || (loggedUser.userId == message.userId) || (loggedUser.userType == 'moderator' && (loggedUser.userId == moderator.userId) )}">
                                                    <div class="right">
                                                        <img class="delete-icon icon" onclick="deleteComment(${message.messageId});" src="<%=contextPath%>/assets/images/delete.png">
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                        <hr>
                                        <div class="message-text">
                                            <div>${message.content}</div>
                                            <br>
                                        </div>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                    <c:if test="${loggedOn}"> <!-- TODO: e iscritto al club -->
                        <button class="add-comment open-button"> <img style="height:20px; width:20px; vertical-align: middle;" src="<%=contextPath%>/assets/images/add.png" alt="plus sign"> Aggiungi un commento </button>
                    </c:if>
                </section>
            </div>
        </main>

        <dialog id="messageDialog" class="modal dialog-card">
            <div class="dialog-flex">
                <div>
                    <img class="chatter-image" src="<%=contextPath%>${loggedUser.urlProfilePicture}" alt="image of your profile" onclick="toChatterProfile(${loggedUser.userId});">
                </div>
                <div>
                    <h2>Lascia un commento</h2>
                    <form id="commentForm" name="commentForm" method="dialog">
                        <label for="commentTextArea">Testo del commento:</label>
                        <br>
                        <textarea id="commentTextArea" name="commentTextArea" required></textarea>
                        <br>
                        <button style="display: inline-block;" class="close-button">Chiudi</button>
                        <button style="display: inline-block; justify-content: end;" type="button" onclick="sendComment();">Invia</button>
                    </form>
                </div>
            </div>
        </dialog>
        <%@include file="/include/footer.inc"%>

        <form id="writeComment" name="writeComment" method="post" action="Dispatcher?controllerAction=ThreadManagement.writeComment">
            <input type="hidden" name="commentTextArea" value="">
            <input type="hidden" name="threadId" value="">
        </form>

        <form id="deleteComment" name="deleteComment" method="post" action="Dispatcher?controllerAction=ThreadManagement.deleteComment">
            <input type="hidden" name="threadId" value=""/>
            <input type="hidden" name="messageId" value=""/>
        </form>

        <form name="toChatterProfile" method="post" action="Dispatcher?controllerAction=UserAccessManagement.viewProfile">
            <input type="hidden" name="profileId">
        </form>

        <form name="clubView" method="post" action="Dispatcher?controllerAction=ClubManagement.viewClub">
            <input type="hidden" name="clubId"/>
        </form>
    </body>
</html>
