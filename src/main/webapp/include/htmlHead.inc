<meta charset="utf-8"/>
<%
    String contextPath = request.getContextPath();
    long timestamp = System.currentTimeMillis();
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="stylesheet" href="<%= contextPath %>/css/style.css?v=<%= timestamp %>" type="text/css" media="screen">
<link rel="shortcut icon" href="<%=contextPath%>/assets/images/favicon.ico" >
<link rel="icon" href="<%=contextPath%>/assets/images/animated_favicon.gif" type="image/gif" >
<title> BookCafe - <%=menuActiveLink%> </title>