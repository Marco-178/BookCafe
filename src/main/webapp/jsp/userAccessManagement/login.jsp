<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    User loggedUser = (User) request.getAttribute("loggedUser");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    String menuActiveLink = "Login";
%>
<html>
<head>
    <script>
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
        window.addEventListener("load", onLoadHandler);
    </script>

    <%@include file="/include/htmlHead.inc"%>

</head>
<body>
 <%@include file="/include/header.inc"%>

    <main>
        <section id="login" class="clearfix">
        <% if(!loggedOn){ %>
            <form name="logonForm" action="Dispatcher" method="post">
                <label for="username">Nome utente: </label>
                <input type="text" id="username"  name="username" maxlength="40" required>
                <br>
                <label for="password">Password: </label>
                <input type="password" id="password" name="password" maxlength="40" required>
                <input type="hidden" name="controllerAction" value="UserAccessManagement.logon"/>
                <br>
                <input type="submit" value="Ok">
            </form>
        <% } else { %>
            <p> Benvenuto, <%=loggedUser.getUsername()%></p>
        <% }%>
        </section>
    </main>

    <%@include file="/include/footer.inc"%>

</body>
</html>
