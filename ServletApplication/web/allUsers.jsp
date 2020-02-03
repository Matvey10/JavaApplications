<%@ page import="userpackage.User" %>
<%@ page import="userpackage.Users" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 29.01.2020
  Time: 11:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Users users = (Users) request.getAttribute("users");
    List<User> userList = Users.getListOfUsers();
    out.println("<p>All users:</p>");
    for(int i=0; i<Users.getListOfUsers().size(); i++) {
        out.println("<p>" + userList.get(i).getUserName() + "</p>");
        out.println("<p>" + userList.get(i).getGender() + "</p>");
        out.println("<p>" + userList.get(i).getAge() + "</p>");
        out.println("<p>" + userList.get(i).getPassword() + "</p>");
    }
%>
<a href="index.html">главная страница</a>
</body>
</html>
