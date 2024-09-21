<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>
<%@ page import="java.time.LocalDate" %>

<%
    LocalDate today = LocalDate.now();
    LocalDate allowedYear = today.minusYears(18);
%>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <link rel="stylesheet" href="<%= contextPath %>/css/loginLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
    <script>
        window.addEventListener("load", onLoadHandler);
        let countriesData = {};
        function onLoadHandler() {
            let usernameTextField = document.querySelector("#username");
            let usernameTextFieldMsg = "Lo username \xE8 obbligatorio.";
            let passwordTextField = document.querySelector("#password");
            let passwordTextFieldMsg = "La password \xE8 obbligatoria.";

            if (usernameTextField != undefined && passwordTextField != undefined ) {
                usernameTextField.setCustomValidity(usernameTextFieldMsg);
                usernameTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? usernameTextFieldMsg : "");
                });
                passwordTextField.setCustomValidity(passwordTextFieldMsg);
                passwordTextField.addEventListener("change", function () {
                    this.setCustomValidity(this.validity.valueMissing ? passwordTextFieldMsg : "");
                });
            }
        }
        // conferma password
        function validateForm() {
            let password = document.getElementById("password").value;
            let passConfirm = document.getElementById("confirm_password").value;
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
            else if(password != passConfirm){
                alert("La password e la conferma password non coincidono.");
                return false;
            }
            // rimuovo il conferma password dal submit
            const input = document.getElementById('confirm_password');
            input.parentNode.removeChild(input);
            return true;
        }

        function checkCity(cityName, selectedCountry, countriesData){
            if(countriesData[selectedCountry].includes(cityName)) return true;
            else{
                console.error(cityName+" Non è inclusa in JSON:"+countriesData[selectedCountry])
            }
        }

        function showCityInput() {
            let selectedCountry = document.getElementById("nation").value;

            if (selectedCountry !== "") {
                document.getElementById('cityInput').classList.remove('hide-selection');
            } else {
                document.getElementById('cityInput').classList.add('hide-selection');
            }
        }

        // gestione select nazioni
        document.addEventListener('DOMContentLoaded', async () => {
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
        });

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
            document.getElementById('username').removeAttribute('error-input')
            document.getElementById('birthdate').removeAttribute('error-input')
            document.getElementById('pictureUpload').removeAttribute('error-input')
            document.getElementById('pictureUpload').removeAttribute('error-input')
        }
    </script>
    <style>
        @media(min-width: 65rem){
            .login-card {
                flex: 1 1 calc(50% - 1rem); /* Due colonne con spazio tra di loro */
            }
            .login-flex{
                flex-wrap: nowrap;
            }
        }
        .hide-selection{
            display: none;
        }
        .error-input{
            border-bottom: 3px solid rgba(255, 0, 0, 0.4);
        }
    </style>
</head>
<body>
<%@include file="/include/header.inc"%>
<main class="container">
    <div class="main-content">
        <section id="login" class="clearfix login-flex first-content">
            <article class="login-card">
                <h1 style="font-size:20px; font-weight: bold"> <strong>Registrazione</strong> </h1>
                <c:choose>
                    <c:when test="${not loggedOn}">
                        <p>Inserisci le tue credenziali o se sei già iscritto <a href="Dispatcher?controllerAction=UserAccessManagement.viewLogin">clicca qui </a>per accedere.</p>
                        <form name="signUp" action="Dispatcher" method="post" onsubmit="return validateForm()" enctype="multipart/form-data">
                            <fieldset>
                                <legend>Generale</legend>
                                <label for="username">Inserire Nome utente: </label><br>
                                <input class="box" type="text" id="username"  name="username" maxlength="40" placeholder="es. utente1" required><br>

                                <label for="birthdate">Data di nascita:</label><br>
                                <input type="date" id="birthdate" name="birthdate" style="padding-left:20px; padding-right:10px;" min="1941-01-01" max="<%=allowedYear%>" required><br>

                                <label for="nation">Seleziona la nazione di residenza:</label><br>
                                <select id="nation" name="nation" onchange="showCityInput()" class="box" required>
                                    <option value="">Seleziona la nazione di residenza...</option>
                                </select>

                                <div id="cityInput" class="hide-selection">
                                    <label for="city">Seleziona la città più vicina:</label><br>
                                    <input class="box" type="text" id="city" name="city" placeholder="Es. Ferrara" required>
                                </div><br>

                                <label for="email">Inserire e-mail: </label><br>
                                <input class="box" type="email" id="email"  name="email" maxlength="60" placeholder="es. silvio.manisi@domain.com" required><br>

                                <label for="password">Inserire Password: </label><br>
                                <input class="box" type="password" id="password" name="password" placeholder="es. Password1@" minlength="8" maxlength="40" required><br>

                                <label for="confirm_password">Conferma Password: </label><br>
                                <input class="box" type="password" id="confirm_password" name="confirm_password" minlength="8" maxlength="40" required>
                            </fieldset>

                            <fieldset>
                                <legend>Facoltativo</legend>
                                <label for="pictureUpload">Foto profilo:</label> <!-- TODO verificare che funzioni -->
                                <input type="file" id="pictureUpload" name="photo">
                            </fieldset>

                            <input type="hidden" name="controllerAction" value="UserAccessManagement.signUp"/><br>
                            <input class="submit" type="submit" value="Registrati">
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
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p> Benvenuto, ${loggedUser.username} ${loggedUser.getUserId()}! Puoi continuare a navigare sul sito: <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a></p>
                    </c:otherwise>
                </c:choose>
            </article>
            <img src="<%=contextPath%>/assets/images/draw.gif" alt="Animated image of a pencil drawing">
        </section>
    </div>
</main>

<%@include file="/include/footer.inc"%>

</body>
</html>
