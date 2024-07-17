<form name="logoutForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
</form>

<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<% User loggedUser = (User) request.getAttribute("loggedUser");
%>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        //let urlProfilePicture = %>;
        let urlProfilePicture = null;
        let fullUrlPic;
        if(urlProfilePicture != null)
            fullUrlPic = "<%=contextPath%>"+urlProfilePicture;
        else
            fullUrlPic = "<%=contextPath%>/assets/images/profile/default_propic.png";
        let imageElement = document.getElementById('propic');
        imageElement.src=fullUrlPic;
    });
</script>

<nav>
    <ul class="navbar">
        <li class="nav-item" <%= menuActiveLink.equals("Home")?"class=\"active\"":"" %> >
            <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
        </li>
        <li class="nav-item" <%=menuActiveLink.equals("Club")?"class=\"active\"":""%>>
            <a href="Dispatcher?controllerAction=clubSearch.view">Club</a>
        </li>
        <li class="nav-item" <%=menuActiveLink.equals("Events")?"class=\"active\"":""%>>
            <a href="Dispatcher?controllerAction=eventSearch.view">Eventi</a>
        </li>

        <c:choose>
            <c:when test="${loggedOn}">
                <span class="nav-item right">
                    <img style="width:20px; height: 20px" id="propic" src="" alt="foto_profilo">
                    <li class="red" style="list-style-type: none" <%=menuActiveLink.equals("Profile")?"class=\"active\"":""%>>
                        <a href="Dispatcher?controllerAction=UserAccessManagement.viewProfile">${loggedUser.username}</a>
                    </li>
                </span>
                <li class="nav-item">
                    <a href="javascript:logoutForm.submit()"> Logout </a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="nav-item right red" <%=menuActiveLink.equals("Login")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=UserAccessManagement.viewLogin">Accedi</a>
                </li>
                <li class="nav-item red" <%=menuActiveLink.equals("SignUp")?"class=\"active\"":""%>>
                    <a href="Dispatcher?controllerAction=UserAccessManagement.viewSignUp">Registrati</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>