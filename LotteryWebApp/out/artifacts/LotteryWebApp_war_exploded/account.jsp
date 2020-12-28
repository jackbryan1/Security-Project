<%--
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
<h1>User Account</h1>

<p><%= request.getAttribute("message") %></p>

<h1>User Details</h1>

<p><%= "Firstname: " + session.getAttribute("firstname")%></p>
<p><%= "Lastname: " + session.getAttribute("lastname")%></p>
<p><%= "Username: " + session.getAttribute("username")%></>
<p><%= "E-Mail: " + session.getAttribute("email")%></p>

<form action="AddUserNumbers" method="post">
    <h1>Lottery Numbers</h1>
    <input type="text" id="num1" name="num1" pattern = "([0-9]|[1-5][0-9]|60)">
    <input type="text" id="num2" name="num2" pattern = "([0-9]|[1-5][0-9]|60)">
    <input type="text" id="num3" name="num3" pattern = "([0-9]|[1-5][0-9]|60)">
    <input type="text" id="num4" name="num4" pattern = "([0-9]|[1-5][0-9]|60)">
    <input type="text" id="num5" name="num5" pattern = "([0-9]|[1-5][0-9]|60)">
    <input type="text" id="num6" name="num6" pattern = "([0-9]|[1-5][0-9]|60)"><br><br>
    <input type="submit" value="Submit">
    <input type="button" value="Random" onclick="RandomNumbers()"><br>
</form>

<h1>Draws</h1>
<p><%
    String[] draws = (String[]) session.getAttribute("draws");
    if (draws != null){
        for (int i = 0; i < draws.length; i++) {
            out.println("Draw " + (i+1) + ": " + draws[i] + "<br><br>");
    }
    }%></p><br>

<form action="GetUserNumbers" method="post">
    <input type="submit" value="Get Draws"><br>
</form>

<form action="CheckWin" method="post">
    <input type="submit" value="Check Draws"><br>
</form>

<a href="index.jsp">Home Page</a>

<script>
    function RandomNumbers() {
        document.getElementById("num1").value = Math.floor(Math.random() * 61);
        document.getElementById("num2").value = Math.floor(Math.random() * 61);
        document.getElementById("num3").value = Math.floor(Math.random() * 61);
        document.getElementById("num4").value = Math.floor(Math.random() * 61);
        document.getElementById("num5").value = Math.floor(Math.random() * 61);
        document.getElementById("num6").value = Math.floor(Math.random() * 61);
    }
</script>


</body>
</html>
