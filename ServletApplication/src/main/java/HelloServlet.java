import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies[0].getValue()!="")
        {
            request.setAttribute("userCookies", cookies);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/cookies.jsp");
            requestDispatcher.forward(request, response);
        }
        else
        {
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/signIn.html");
            requestDispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
