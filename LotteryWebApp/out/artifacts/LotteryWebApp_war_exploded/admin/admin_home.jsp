<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
</head>
<body>
<h1>Admin Account</h1>

<p><%= request.getAttribute("message") %></p>

<h1>User Details</h1>

<p><%=request.getAttribute("data")%></p><br><br>

<a href="index.jsp">Home Page</a>
</body>
</html>
