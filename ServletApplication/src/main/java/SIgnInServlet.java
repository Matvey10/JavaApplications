import userpackage.User;
import userpackage.Users;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/signIn")
public class SIgnInServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Users users = Users.getUsers();
        String name = request.getParameter("username");
        System.out.println(name);
        String password = request.getParameter("password");
        User user = Users.getUserByName(name);
        Cookie nameCookie = new Cookie("nameCookie", name);
        response.addCookie(nameCookie);
        Cookie passwordCookie = new Cookie("passwordCookie", user.getPassword());
        response.addCookie(passwordCookie);
        Cookie genderCookie = new Cookie("genderCookie", user.getGender());
        response.addCookie(genderCookie);
        Cookie ageCookie = new Cookie("ageCookie", user.getAge());
        response.addCookie(ageCookie);
       /* try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://5432/usersDB",
                    "postgres", "vma3243652");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM userInfo u" +
                    "WHERE u.userName=name");
            User us = new User(rs.getString("userName"), rs.getString("userPassword"), rs.getString("userGender"),
                    rs.getString("userAge"));
            request.setAttribute("user", user);
            request.setAttribute("users", users);
            request.setAttribute("us", us);
            stmt.close();
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/userData.jsp");
            requestDispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        request.setAttribute("user", user);
        request.setAttribute("users", users);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/userData.jsp");
        requestDispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
