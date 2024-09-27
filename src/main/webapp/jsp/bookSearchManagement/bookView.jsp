<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.Book"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/bookLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            document.writeReview.modifyReview.value = -1;

            const modal = document.querySelector('#reviewDialog');
            const openModal = document.querySelector('.open-button');
            const closeModal = document.querySelector('.close-button');

            openModal.addEventListener('click', () => {
                modal.showModal();
            });

            closeModal.addEventListener('click', () => {
                document.writeReview.modifyReview = -1;
                modal.close();
            });
        });

        function sendReview_BookViewInfo(ISBN){
            document.writeReview.bookISBN.value = ISBN;
            document.writeReview.rating.value = document.reviewForm.rating.value;
            document.writeReview.reviewText.value = document.reviewForm.reviewText.value;
            document.writeReview.submit();
        }

        function modifyReview(selectedReviewId, reviewText){
            document.writeReview.modifyReview.value = selectedReviewId;
            document.getElementById("reviewText").value = reviewText;
            console.log("prova: ", reviewText);
        }

        function removeReview(selectedReviewId){
            document.removeReview.bookISBN.value = ${book.ISBN};
            document.removeReview.modifyReview.value = selectedReviewId;
            document.removeReview.submit();
        }

        function toReviewerProfile(selectedUserId){
            document.toReviewerProfile.profileId.value = selectedUserId;
            document.toReviewerProfile.submit();
        }
    </script>
</head>
<body>
    <%@include file="/include/header.inc"%>
    <main class="container">
        <div class="main-content">
            <h1 class="book-title">
                <span style="font-family: sans-serif;">${book.title} </span>
                <span class="subtitle">&nbsp; - ${book.publicationYear}, Ed. ${book.yearEdition}</span>
            </h1>
            <h2 class="subtitle" style="margin-top: 0.4rem; margin-bottom: 1rem;">Autore: <span class="author">${book.author}</span></h2>
            <section class="book-card book-view-grid">
                <div class="book-view-item">
                    <img class="book-view-image" src="<%=contextPath%>${book.urlBookcoverImage}" alt="image of book ${book.title}">
                </div>
                <ul class="book-view-item book-view-description-grid" style="list-style-type: none">
                    <li>
                        <div> <span class="mini-title">Genere:</span> ${book.genre} </div>
                        <div>
                            <c:choose>
                                <c:when test="${not empty meanReviews}">
                                    <span class="mini-title">Media recensioni:</span>
                                    <c:choose>
                                        <c:when test="${meanReviews >= 4.6}">
                                            <img class="rating-icon" src="<%=contextPath%>/assets/images/rating5.png" alt="5 Star Rating">
                                        </c:when>
                                        <c:when test="${meanReviews >= 3.6}">
                                            <img class="rating-icon" src="<%=contextPath%>/assets/images/rating4.png" alt="4 Star Rating">
                                        </c:when>
                                        <c:when test="${meanReviews >= 2.6}">
                                            <img class="rating-icon" src="<%=contextPath%>/assets/images/rating3.png" alt="3 Star Rating">
                                        </c:when>
                                        <c:when test="${meanReviews >= 1.6}">
                                            <img class="rating-icon" src="<%=contextPath%>/assets/images/rating2.png" alt="2 Star Rating">
                                        </c:when>
                                        <c:otherwise>
                                            <img class="rating-icon" src="<%=contextPath%>/assets/images/rating1.png" alt="1 Star Rating">
                                        </c:otherwise>
                                    </c:choose>
                                    <span><fmt:formatNumber value="${meanReviews}" pattern="#0.0" /></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="subtitle">Nessuna recensione disponibile.</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </li>

                    <li>
                        <h3> Descrizione </h3>
                        <span> ${book.description} </span>
                    </li>

                    <li>
                        <h3> Dettagli libro </h3>
                        <ul class="information-grid" style="list-style-type: none">
                            <li> <span class="subtitle">ISBN</span> <br>${book.ISBN}</li>
                            <li>
                                <span class="subtitle">Prezzo di copertina </span>
                                <br>
                                <span style="font-family: sans-serif; color: #b12704;">
                                    € <fmt:formatNumber value="${book.bookPrice}" pattern="#0.00" />
                                </span>
                            </li>
                            <li>
                                <span class="subtitle">Prezzo ebook </span>
                                <br>
                                <span style="font-family: sans-serif; color: #b12704;">
                                    € <fmt:formatNumber value="${book.ebookPrice}" pattern="#0.00" />
                                </span>
                            </li>
                            <li> <span class="subtitle">Numero di pagine </span> <br>${book.numPages}</li>
                            <li> <span class="subtitle">Autore </span> <br>${book.author}</li>
                            <li> <span class="subtitle">Anno di pubblicazione </span> <br>${book.publicationYear}</li>
                            <li> <span class="subtitle">Anno edizione </span><br>${book.yearEdition}</li>
                        </ul>
                    </li>
                </ul>
            </section>
            <hr>
            <h2 class="mini-title">Recensioni dalla community</h2>
            <section>
                <c:choose>
                    <c:when test="${not empty reviews}">
                        <c:forEach var="review" items="${reviews}" varStatus="status">
                            <article class="review-card">
                                <c:set var="reviewer" value="${reviewers[status.index]}" />
                                <img class="reviewer-image" src="<%=contextPath%>${reviewer.urlProfilePicture}" alt="image of user: ${reviewer.username}" onclick="location.href='javascript:toReviewerProfile(${reviewer.userId});'">
                                <div>
                                    <div style="max-height: 40px">
                                        <c:choose>
                                            <c:when test="${review.rating >= 4.5}">
                                                <img class="rating-icon" src="<%=contextPath%>/assets/images/rating5.png" alt="5 Star Rating">
                                            </c:when>
                                            <c:when test="${review.rating >= 3.5}">
                                                <img class="rating-icon" src="<%=contextPath%>/assets/images/rating4.png" alt="4 Star Rating">
                                            </c:when>
                                            <c:when test="${review.rating >= 2.5}">
                                                <img class="rating-icon" src="<%=contextPath%>/assets/images/rating3.png" alt="3 Star Rating">
                                            </c:when>
                                            <c:when test="${review.rating >= 1.5}">
                                                <img class="rating-icon" src="<%=contextPath%>/assets/images/rating2.png" alt="2 Star Rating">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="rating-icon" src="<%=contextPath%>/assets/images/rating1.png" alt="1 Star Rating">
                                            </c:otherwise>
                                        </c:choose>
                                        <span><fmt:formatNumber value="${review.rating}" pattern="#0.0" /></span>
                                    </div>
                                    <div>da <span class="author">${reviewer.username}</span></div>
                                    <hr>
                                    <p> ${review.testo} </p>
                                        <div class="right">
                                            <c:if test="${loggedOn && loggedUserReviewId == review.reviewId}">
                                                <button class="edit-review-button open-button" onclick="modifyReview(${review.reviewId}, '${fn:escapeXml(review.testo)}');"> Modifica </button>
                                            </c:if>
                                            <c:if test="${loggedOn && (loggedUserReviewId == review.reviewId || loggedUser.userType == 'admin' || loggedUser.userType == 'moderator')}">
                                                <button class="delete-review-button" onclick="location.href='javascript:removeReview(${review.reviewId});'"> Elimina </button>
                                            </c:if>
                                        </div>
                                </div>
                            </article>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="color:#b12704">Nessuna recensione disponibile</p>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${loggedOn}">
                        <c:if test="${loggedUserReviewId == null || loggedUserReviewId < 0}">
                            <p style="margin-top: 40px; font-style: italic;"> Hai letto il libro? Aggiungi la tua recensione! </p>
                                <button class="add-review-plus-button open-button"> <img style="height:20px; width:20px; vertical-align: middle;" src="<%=contextPath%>/assets/images/add.png" alt="plus sign"> </button>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <p style="margin-top: 40px; font-style: italic;"> Hai letto il libro? <a href="Dispatcher?controllerAction=UserAccessManagement.viewLogin">Accedi al sito</a> e aggiungi la tua recensione! </p>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>
    <dialog id="reviewDialog" class="modal dialog-card">
        <div class="dialog-flex">
            <div>
                <img class="reviewer-image" src="<%=contextPath%>${loggedUser.urlProfilePicture}" alt="image of your profile">
            </div>
            <div>
                <h2>Lascia una recensione</h2>
                <form name="reviewForm" method="dialog">
                    <fieldset name="rating" class="rating">
                        <legend>Valutazione:</legend>
                        <input type="radio" name="rating" title="star5" value="5" />
                        <input type="radio" name="rating" title="star4" value="4" />
                        <input type="radio" name="rating" title="star3" value="3" checked />
                        <input type="radio" name="rating" title="star2" value="2" />
                        <input type="radio" name="rating" title="star1" value="1" />
                    </fieldset>
                    <br>
                    <label for="reviewText">Testo della recensione:</label>
                    <br>
                    <textarea id="reviewText" required></textarea>
                    <br>
                    <button style="display: inline-block;" class="close-button">Chiudi</button>
                    <button style="display: inline-block; justify-content: end;" type="submit" onclick="location.href='javascript:sendReview_BookViewInfo(${book.ISBN});';">Invia</button>
                </form>
            </div>
        </div>
    </dialog>
    <%@include file="/include/footer.inc"%>

    <form name="writeReview" method="post" action="Dispatcher?controllerAction=ReviewManagement.writeReview">
        <input type="hidden" name="bookISBN"/>
        <input type="hidden" name="rating"/>
        <input type="hidden" name="reviewText"/>
        <input type="hidden" name="modifyReview"/>
    </form>

    <form name="removeReview" method="post" action="Dispatcher?controllerAction=ReviewManagement.removeReview">
        <input type="hidden" name="bookISBN"/>
        <input type="hidden" name="modifyReview"/>
    </form>

    <form name="toReviewerProfile" method="post" action="Dispatcher?controllerAction=UserAccessManagement.viewProfile">
        <input type="hidden" name="profileId">
    </form>
</body>
</html>
