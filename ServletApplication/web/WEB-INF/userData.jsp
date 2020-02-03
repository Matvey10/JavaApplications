<%@ page import="userpackage.User" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 29.01.2020
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User data</title>
</head>
<body>
<%
    User user = (User) request.getAttribute("user");
    out.println("<p>Hello, "+ user.getUserName() + "!, your user data:" + "</p>");
    out.println("<p>" + user.getUserName() + "</p>");
    out.println("<p>" + user.getUserName() + "</p>");
    out.println("<p>" + user.getGender() + "</p>");
    out.println("<p>" + user.getAge() + "</p>");
    out.println("<p>" + user.getPassword() + "</p>");
%>
</body>
</html>
