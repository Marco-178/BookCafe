<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>
<%@ page import="java.time.LocalDate" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    LocalDate today = LocalDate.now();
    LocalDate allowedYear = today.minusYears(18);
%>

<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/userLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
    <script>
        window.addEventListener("load", onLoadHandler);
        let countriesData = {};
        var loggedUser = {
            userType: '${loggedUser.userType}'
        };
        document.addEventListener("DOMContentLoaded", function() {
            const editProfileButton = document.querySelector(".edit-profile-button");
            if(loggedUser.userType === 'admin'){
                const formAdminElements = document.querySelectorAll('.formAdmin');
                const formAdminSubmit = document.getElementById('submit');
                formAdminElements.forEach(el => {
                    el.style.display = 'inline';
                });
                formAdminSubmit.style.display = 'inline';
            }
            if (editProfileButton) {
                editProfileButton.addEventListener("click", function () {
                    const infoElements = document.querySelectorAll('.info');
                    const formElements = document.querySelectorAll('.form');

                    infoElements.forEach(el => {
                        el.style.display = (el.style.display === 'none') ? 'inline' : 'none';
                    });

                    formElements.forEach(el => {
                        el.style.display = (el.style.display === 'none') ? 'inline' : 'none';
                    });
                    if(loggedUser.userType === 'admin') {
                        const userTypeForm = document.getElementById('userType');
                        const formAdminSubmit = document.getElementById('submit');
                        userTypeForm.style.display = 'inline';
                        formAdminSubmit.style.display = 'inline';
                    }
                });
            }
            else{
                console.error("Bottone modifica non trovato");
            }
        });

        function onLoadHandler() {
            loadCountries();
        }

        async function loadCountries(){
            const countrySelect = document.getElementById('nation');
            try {
                const response = await fetch('<%=contextPath%>/assets/countries.json');
                countriesData = await response.json();

                for (const country in countriesData) {
                    const option = document.createElement('option');
                    option.value = country;
                    option.textContent = country;
                    countrySelect.appendChild(option);
                }

            } catch (error) {
                console.error('Errore nel caricamento del file JSON:', error);
            }
        }

        function validateForm() {
            let password = document.getElementById("password").value;
            let selectedCountry = document.getElementById('nation').value;
            let cityName = document.getElementById('city').value.trim();
            let cityInput = document.getElementById('city').value;
            let email = document.getElementById('email').value;
            let regex = /^(?=.*[A-Za-z])[A-Za-z\s\'\-]+$/;
            let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@$%^&*()_+\[\]{};:])[A-Za-z\d!@$%^&*()_+\[\]{};:]{8,}$/;

            if(!regex.test(cityInput)){
                alert("Il nome di una città può contenere solo caratteri");
                return false;
            }
            else if(!checkCity(cityName, selectedCountry, countriesData)){
                alert("La città '"+cityName+"' non è valida per il Paese '"+selectedCountry+"'.");
                return false;
            }
            if (!emailRegex.test(email)) {
                alert("Il nome di una città può contenere solo caratteri");
                return false;
            }
            if (!passwordRegex.test(password)) {
                alert("La password deve avere almeno 8 caratteri, almeno una lettera maiuscola, una lettera minuscola un numero e un carattere speciale.\ncaratteri speciali ammessi: ! @ $ % ^ & * () _ + [] {} :");
                return false;
            }
            return true;
        }

        function checkCity(cityName, selectedCountry, countriesData){
            if(countriesData[selectedCountry].includes(cityName)) return true;
            else{
                console.error(cityName+" Non è inclusa in JSON:"+countriesData[selectedCountry])
            }
        }

        function applyErrorClass(){
            <c:if test="${errorMessage}">
            let message = ""+${errorMessage};
            switch(message) {
                case "DuplicatedUsernameException":
                    document.getElementById('username').setAttribute('class', 'error-input');
                    break;
                case "InvalidBirthdateException":
                    document.getElementById('birthdate').setAttribute('class', 'error-input');
                    break;
                case "IOException":
                    document.getElementById('pictureUpload').setAttribute('class', 'error-input');
                    break;
                case "IllegalArgumentException":
                    document.getElementById('pictureUpload').setAttribute('class', 'error-input');
                    break;
            }
            </c:if>
        }

        function removeErrorClass() {
            document.getElementById('birthdate').removeAttribute('error-input')
            document.getElementById('pictureUpload').removeAttribute('error-input')
            document.getElementById('pictureUpload').removeAttribute('error-input')
        }
    </script>
</head>
<body>
    <%@include file="/include/header.inc"%>
    <main class="container">
        <div class="main-content">
            <h1 class="user-title">Profilo di <span style="color: darkgoldenrod">${viewedUser.username}</span></h1>
            <section class="user-card">
                <form name="modifyProfile" type="hidden" action="Dispatcher" method="post" onsubmit="return validateForm()" enctype="multipart/form-data">
                    <div class="user-view-grid">
                            <ul style="list-style-type: none; display: grid; gap: 1.5rem;">
                                <img class="profile-image" src="<%=contextPath%>${viewedUser.urlProfilePicture}" alt="image of user ${viewedUser.username}">
                                <label class="form" style="display: none" for="pictureUpload">Cambia:</label>
                                <input class="form box" style="display: none" type="file" id="pictureUpload" name="pictureUpload">
                                <li>
                                    <span  class="subtitle">E-mail</span>
                                    <br>
                                    <span class="info formAdmin">${viewedUser.email}</span>
                                    <input class="form box" style="display: none" type="email" id="email"  name="email" maxlength="60" value="${loggedUser.email}" required>
                                </li>
                                <li>
                                    <c:if test="${loggedUser.userId == viewedUser.userId}">
                                            <span  class="subtitle">Password</span>
                                            <br>
                                            <input class="info formAdmin box" type="password" id="disabledPass" name="disabledPass" value="${loggedUser.password}" disabled>
                                        <input class="form box" style="display: none" type="password" id="password" name="password" value="${loggedUser.password}" minlength="8" maxlength="40" required>
                                    </c:if>
                                </li>
                                <li class="info form formAdmin">
                                    <span  class="subtitle">Data di iscrizione</span>
                                    <br>
                                    <span>${viewedUser.subscriptionDate}</span>
                                </li>
                                <li>
                                    <span  class="subtitle">Data di nascita</span>
                                    <br>
                                    <span class="info formAdmin">${viewedUser.birthDate}</span>
                                    <input class="form" type="date" id="birthdate" name="birthdate" value="${viewedUser.birthDate}" style="padding-left:20px; display:none; padding-right:10px;" min="1941-01-01" max="<%=allowedYear%>"><br>
                                </li>
                            </ul>

                            <ul style="list-style-type: none; display: grid; gap: 1.5rem;">
                                <li>
                                    <span  class="subtitle">Nazione di residenza</span>
                                    <br>
                                    <span class="info formAdmin">${viewedUser.nation}</span>
                                    <select class="form box" id="nation" name="nation" onchange="showCityInput()" style="display:none;" required>
                                        <option value="${viewedUser.nation}">${viewedUser.nation}</option>
                                    </select>
                                </li>
                                <li>
                                    <span  class="subtitle">Città</span>
                                    <br>
                                    <span class="info formAdmin">${viewedUser.city}</span>
                                    <input class="box form" type="text" id="city" name="city" value="${loggedUser.city}" style="display:none;" required>
                                </li>
                                <li class="info form formAdmin">
                                    <span  class="subtitle">Ultimo accesso</span>
                                    <br>
                                    <span >${formattedLastAccess}</span>
                                </li>
                                <li>
                                    <span  class="subtitle">Ruolo utente</span>
                                    <br>
                                    <span class="info form">${viewedUser.userType}</span>
                                    <c:choose>
                                        <c:when test="${loggedUser.userType == 'admin'}">
                                            <select class="form box formAdmin" id="userType" name="userType" style="display:none;">
                                                <option value="${viewedUser.userType}" selected>${viewedUser.userType}</option>
                                                <option value="admin">admin</option>
                                                <option value="normal">normal</option>
                                                <option value="moderator">moderator</option>
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" id="userType" name="userType" value="${viewedUser.userType}">
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <span  class="subtitle">biografia</span>
                                    <br>
                                    <c:choose>
                                        <c:when test="${not empty viewedUser.biography && viewedUser.biography != 'null'}">
                                            <span class="info formAdmin">${viewedUser.biography}</span>
                                            <textarea class="form textArea" style="display:none" id="biography" name="biography">${viewedUser.biography}</textarea>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="info formAdmin">Nessuna biografia disponibile ☹</span>
                                            <textarea class="form textArea" style="display:none" id="biography" name="biography"></textarea>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </ul>
                    </div>
                    <div class="right">
                        <c:if test="${loggedUser.userId == viewedUser.userId}">
                            <button id="edit-profile-button info" class="edit-profile-button" type="button"> <span class="info">Modifica</span> <span class="form" style="display: none">Annulla</span> </button>
                        </c:if>
                        <c:if test="${loggedUser.userId == viewedUser.userId || loggedUser.userType == 'admin'}">
                            <button class="edit-profile-button form formAdmin" style="display: none" id="submit" type="submit"> Invia </button>
                        </c:if>
                        <input type="hidden" name="controllerAction" value="UserAccessManagement.modifyProfile"/>
                    </div>
                    <input type="hidden" name="username" value="${viewedUser.username}"> <!-- Serve per salvare il file della eventuale nuova immagine profilo -->
                    <input type="hidden" name="profileId" value="${viewedUser.userId}"/> <!-- Serve per prelevare utente da DB in caso di promozione (bastava anche username) -->
                </form>
                <form name="deleteProfile" type="hidden" action="Dispatcher" method="post">
                    <div class="right">
                        <c:if test="${loggedUser.userId == viewedUser.userId}">
                            <button class="delete-profile-button" type="submit"> Elimina Profilo </button>
                            <input type="hidden" name="profileId" value="${viewedUser.userId}"/>
                            <input type="hidden" name="controllerAction" value="UserAccessManagement.deleteProfile"/>
                        </c:if>
                    </div>
                </form>
                <div class="right">
                    <c:if test="${loggedUser.userType == 'admin' && loggedUser.userId != viewedUser.userId}">
                        <form name="banProfile" type="hidden" action="Dispatcher" method="post">
                            <button class="delete-profile-button formAdmin"> Ban dell'utente </button>
                            <input type="hidden" name="profileId" value="${viewedUser.userId}"/>
                            <input type="hidden" name="controllerAction" value="UserAccessManagement.banProfile"/>
                        </form>
                    </c:if>
                </div>
                <c:choose>
                    <c:when test="${not empty errorMessage}">
                        <script>
                            applyErrorClass();
                        </script>
                        <p style="color: red;">${errorMessage}</p>
                    </c:when>
                    <c:otherwise>
                        <script>
                            removeErrorClass();
                        </script>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>
    <%@include file="/include/footer.inc"%>
</body>
</html>
