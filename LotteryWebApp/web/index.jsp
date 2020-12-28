<%--
  Created by IntelliJ IDEA.
  User: johnmace
  Date: 21/10/2020
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Home</title>
  </head>
  <body>

  <h1>Home Page</h1>

  <h2>Create Account</h2>
  <form action="CreateAccount" method="post">
      <label for="firstname">First name:</label><br>
      <input type="text" id="firstname" name="firstname"><br>
      <label for="lastname">Last name:</label><br>
      <input type="text" id="lastname" name="lastname"><br>
      <label for="username">Username:</label><br>
      <input type="text" id="username" name="username"><br>
      <label for="phone">Phone:</label><br>
      <input type="text" id="phone" name="phone" pattern="\d{2}[-]\d{4}[-]\d{7}"><br>
      <label for="email">E-mail:</label><br>
      <input type="email" id="email" name="email"><br>
      <label for="password">Password:</label><br>
      <input type="password" id="password" name="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}"><br><br>
      <label>Select Type</label>
      <select name="role" id = "role">
          <option value="public">Public</option>
          <option value="admin">Admin</option>
      </select><br><br>
      <input type="submit" value="Submit">
  </form>

  <h2>Login</h2>
  <form action="UserLogin" method="post">
      <label for="usernamelogin">Username:</label><br>
      <input type="text" id="usernamelogin" name="username"><br>
      <label for="passwordlogin">Password:</label><br>
      <input type="password" id="passwordlogin" name="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}"><br><br>
      <input type="submit" id="submitlogin" value="Submit">
  </form>

</html>
