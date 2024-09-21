<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<html>
    <head>
        <%@include file="/include/htmlHead.inc"%>
        <link rel="stylesheet" href="<%= contextPath %>/css/loginLayout.css?v=<%= timestamp %>" type="text/css" media="screen">
        <script>
            function onLoadHandler() {
                applicationMessage = "Username e password errati!";
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
            window.addEventListener("load", onLoadHandler);
        </script>
        <style>
            @media(min-width: 50rem){
                .login-card {
                    flex: 1 1 calc(50% - 1rem); /* Due colonne con spazio tra di loro */
                }
                .login-flex{
                    flex-wrap: nowrap;
                }
            }
        </style>
    </head>
    <body>
        <%@include file="/include/header.inc"%>
        <main class="container">
            <div class="main-content">
                <section id="login" class="clearfix login-flex first-content">
                    <article class="login-card">
                        <h1 style="font-size:20px; font-weight: bold"> <strong>Login</strong> </h1>
                        <c:choose>
                            <c:when test="${not loggedOn}">
                                <p>Inserisci le tue credenziali o <a href="Dispatcher?controllerAction=UserAccessManagement.viewSignUp">clicca qui </a>per registrarti.</p>
                                <form name="logonForm" action="Dispatcher" method="post">
                                    <label for="username">Nome utente: </label>
                                    <br>
                                    <input class="box" type="text" id="username"  name="username" maxlength="40" placeholder="es. utente1" required>
                                    <br>
                                    <label for="password">Password: </label>
                                    <br>
                                    <input class="box" type="password" id="password" name="password" placeholder="es. Password1@" maxlength="40" required>
                                    <input type="hidden" name="controllerAction" value="UserAccessManagement.logon"/>
                                    <br>
                                    <input class="submit" type="submit" value="Accedi">
                                </form>
                                <p>Password dimenticata?<a href="Dispatcher?controllerAction=UserAccessManagement.viewPasswordRecovery"> clicca qui </a></p>
                            </c:when>
                            <c:otherwise>
                                <p> Benvenuto, ${loggedUser.username} Puoi continuare a navigare sul sito: <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a></p>
                            </c:otherwise>
                        </c:choose>
                    </article>
                    <img src="<%=contextPath%>/assets/images/transp-book.gif" height="350" alt="Animated image of a book">
                </section>
            </div>
        </main>

        <%@include file="/include/footer.inc"%>

    </body>
</html>
