<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 28.01.2020
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UserInfo</title>
</head>
<body>
<%
for (Cookie c : (Cookie[]) request.getAttribute("userCookies"))
{
    out.println("<p>" + c.getValue() + "</p>");
}
%>
<br></br>
<form action="delCookie" method="POST">
    <input type="submit" value="EXIT" />
</form>

<a href="allUsers.jsp">все пользователи</a>
</body>
</html>
