<meta charset="utf-8"/>
<%
    String contextPath = request.getContextPath();
    long timestamp = System.currentTimeMillis();
%>

<meta content="width=device-width, initial-scale=1" name="viewport" />
<link rel="stylesheet" href="<%= contextPath %>/css/style.css?v=<%= timestamp %>" type="text/css" media="screen">
<title> BookCafe; - <%=menuActiveLink%> </title>