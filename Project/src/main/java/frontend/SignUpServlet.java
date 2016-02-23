package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
@WebServlet(urlPatterns = "user")
public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("login");
        String password = request.getParameter("password");

        Map<String, Object> pageVariables = new HashMap<>();
        if (name == null || password == null ) {
            pageVariables.put("signUpStatus", "Enter your login/password");
        } else if (accountService.addUser(name, new UserProfile(name, password, ""))) {
            pageVariables.put("signUpStatus", "New user " + name + " created");
        } else {
            pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
        }

        response.getWriter().println(PageGenerator.getPage("sign_up_Post.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Map<String, Object> pageVariables = new HashMap<>();
        if (String.valueOf(login).equals("") || String.valueOf(password).equals("")|| String.valueOf(email).equals("")) {
            pageVariables.put("signUpStatus", "Enter your login/password");
        } else if (accountService.addUser(login, new UserProfile(login, password, email))) {
            System.out.append("User created: {").append(String.valueOf(login)).append(", ")
                    .append(String.valueOf(password)).append(", ")
                    .append(String.valueOf(email)).append("}").append('\n');
            pageVariables.put("signUpStatus", "New user " + login + " created");
        } else {
            pageVariables.put("signUpStatus", "User with name: " + login + " already exists");
        }

        response.getWriter().println(PageGenerator.getPage("sign_up_Post.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
