<form name="logoutForm" action="Dispatcher" method="post">
    <input type="hidden" name="controllerAction" value="HomeManagement.logout"/>
</form>

<nav>
    <ul class="navbar">
        <li class="nav-item" <%= menuActiveLink.equals("Home")?"class=\"active\"":"" %> >
            <a href="Dispatcher?controllerAction=HomeManagement.view">Home</a>
        </li>
        <li class="nav-item" <%=menuActiveLink.equals("BookSearch")?"class=\"active\"":""%>>
            <a href="Dispatcher?controllerAction=BookSearch.view">Ricerca libri</a>
        </li>

        <% if(loggedOn){ %>
            <li class="nav-item right red" <%=menuActiveLink.equals("Profile")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserAccessManagement.viewProfile"><%=loggedUser.getUsername()%></a>
            </li>
            <li class="nav-item">
                <a href="javascript:logoutForm.submit()"> Logout </a>
            </li>
        <% } else{ %>
            <li class="nav-item right red" <%=menuActiveLink.equals("Login")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserAccessManagement.viewLogin">Accedi</a>
            </li>
            <li class="nav-item red" <%=menuActiveLink.equals("SignUp")?"class=\"active\"":""%>>
                <a href="Dispatcher?controllerAction=UserAccessManagement.viewSignUp">Registrati</a>
            </li>
        <% } %>
    </ul>
</nav>