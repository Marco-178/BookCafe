<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String menuActiveLink = "Login | Password dimenticata";
%>
<html>
<head>
    <%@include file="/include/htmlHead.inc"%>
    <%boolean emailSent = false;%>
    <link rel="stylesheet" href="<%= contextPath %>/css/loginLayout.css?v=<%= timestamp %>" type="text/css" media="screen">

    <script>
        let email = ""
        function rememberMail(){
            email = document.getElementById('email').value;
            return true;
        }
    </script>
</head>

<body>
    <%@include file="/include/header.inc"%>
    <main class="container">
        <div class="main-content">
            <article class="login-card first-content">
                <h1>Password dimenticata</h1>
                    <form name="recoverForm" action="Dispatcher" method="post" onsubmit="rememberMail()">
                        <label for="email">Inserire e-mail: </label><br>
                        <input class="box" type="email" id="email" name="email" maxlength="60" placeholder="es. silvio.manisi@domain.com" required><br>
                    </form>
                    <input type="hidden" name="controllerAction" value="UserAccessManagement."/><br>
                    <input class="submit" type="submit" value="Invia">
                    <c:if test="${emailSent}">
                        <p style="color:mediumspringgreen">Abbiamo inviato un messaggio alla casella di posta che hai indicato. <a>Riprova</a>.</p>
                    </c:if>
            </article>
        </div>
    </main>
    <%@include file="/include/footer.inc"%>
</body>

</html>
