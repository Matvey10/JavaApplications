import userpackage.User;
import userpackage.Users;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class FirstServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        Users users = Users.getUsers();
        String name = request.getParameter("username");
        String age = request.getParameter("userage");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        /*User user = new User(name,password,gender,age);
        Users.setUser(user);*/
        Cookie nameCookie = new Cookie("nameCookie", name);
        response.addCookie(nameCookie);
        Cookie ageCookie = new Cookie("ageCookie", age);
        response.addCookie(ageCookie);
        Cookie genderCookie = new Cookie("genderCookie", gender);
        response.addCookie(genderCookie);
       try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://5432/usersDB",
                    "postgres", "vma3243652");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("INSERT INTO userInfo (userPassword, userNAme, userGender, userAge, id)" +
                    "values (password, name, gender, age, 1)");
            stmt.close();
            User user = new User(name,password,gender,age);
            Users.setUser(user);
            request.setAttribute("user", user);
            request.setAttribute("users", users);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/userData.jsp");
            requestDispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
            /*request.setAttribute("user", user);
            request.setAttribute("users", users);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/userData.jsp");
            requestDispatcher.forward(request, response);*/
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
