package mainpackage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mainpackage.DbUtil;
import mainpackage.security.PasswordUtil; 

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DbUtil.getConnection()) {
            // πζαίρνουμε τα στουιχεία του χρήστη από τη βάση
            String sql = "SELECT username, name, role, password_hash, password_salt FROM users WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String storedSalt = rs.getString("password_salt");

                boolean valid = false;

                try {
                    valid = PasswordUtil.verifyPassword(password, storedSalt, storedHash);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (!valid && storedHash != null && storedHash.equals(password)) {
                    valid = true;
                }

                if (valid) {

                    HttpSession session = request.getSession();
                    session.setAttribute("username", rs.getString("username"));
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("role", rs.getString("role"));

                    String role = rs.getString("role");
                    if ("PATIENT".equalsIgnoreCase(role)) {
                        response.sendRedirect("patientHome.jsp");
                    } else if ("DOCTOR".equalsIgnoreCase(role)) {
                        response.sendRedirect("doctorHome.jsp");
                    } else if ("ADMIN".equalsIgnoreCase(role)) {
                        response.sendRedirect("adminHome.jsp");
                    } else {
                        response.sendRedirect("login.jsp?error=unknownrole");
                    }
                } else {
                    // Λάθος κώδικος 
                    request.setAttribute("errorMessage", "Λάθος username ή password!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Λάθος username ή password!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Σφάλμα σύνδεσης στη βάση!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
