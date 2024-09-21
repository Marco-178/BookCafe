<form name="logoutForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="UserAccessManagement.logout"/>
</form>

<%@ page import="com.ma.isw.bookcafe.model.mo.User"%>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        <c:if test="${loggedOn}">
            let urlProfilePicture = null;
            let fullUrlPic;
            if (urlProfilePicture != null)
                fullUrlPic = "<%=contextPath%>" + urlProfilePicture;
            else
                fullUrlPic = "<%=contextPath%>/assets/images/profile/default_propic.png";
            let imageElement = document.getElementById('propic');
            imageElement.src = fullUrlPic;
        </c:if>
    });
</script>

<nav>
    <ul class="navbar">
        <li class="nav-item">
            <a class="navbar-option" href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
        </li>
        <li class="nav-item">
            <a class="navbar-option" href="Dispatcher?controllerAction=ClubManagement.viewClubsList">Club</a>
        </li>
        <li class="nav-item">
            <a class="navbar-option" href="Dispatcher?controllerAction=EventManagement.viewEventsList">Eventi</a>
        </li>

        <c:choose>
            <c:when test="${loggedOn}">
                <span class="nav-item right">
                    <li style="list-style-type: none">
                        <a href="Dispatcher?controllerAction=UserAccessManagement.viewProfile"><img class="propic" id="propic" src="" alt="foto profilo"></a>
                        <a class="navbar-option" href="Dispatcher?controllerAction=UserAccessManagement.viewProfile">${loggedUser.username}</a>
                    </li>
                </span>
                <li class="nav-item">
                    <a class="navbar-option" href="javascript:logoutForm.submit()"> Logout </a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="nav-item right">
                    <a class="navbar-option" href="Dispatcher?controllerAction=UserAccessManagement.viewLogin">Accedi</a>
                </li>
                <li class="nav-item red">
                    <a class="navbar-option" href="Dispatcher?controllerAction=UserAccessManagement.viewSignUp">Registrati</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>