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

import mainpackage.DbUtil;
import mainpackage.security.PasswordUtil;

@WebServlet("/registerPatient")
public class RegisterPatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String gender = request.getParameter("gender");
        String amka = request.getParameter("amka");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        try (Connection conn = DbUtil.getConnection()) {
            // ελεγχος αν υπάαρχειε ήδη το username ή ΑΜΚΑ
            String checkSql = "SELECT 1 FROM users u JOIN patients p ON u.user_id=p.user_id " +
                              "WHERE u.username=? OR p.amka=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            checkStmt.setString(2, amka);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                response.sendRedirect("registerPatient.jsp?error=exists");
                return;
            }

            // δημιουργία σαλτ χασ
            String salt = PasswordUtil.generateSalt();
            String hash = PasswordUtil.hashPassword(password, salt);

            // εισαγωγή στον  users
            String sqlUser = "INSERT INTO users (username, password_hash, password_salt, name, surname, gender, role) " +
                             "VALUES (?, ?, ?, ?, ?, ?, 'PATIENT') RETURNING user_id";
            PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
            stmtUser.setString(1, username);
            stmtUser.setString(2, hash);
            stmtUser.setString(3, salt);
            stmtUser.setString(4, name);
            stmtUser.setString(5, surname);
            stmtUser.setString(6, gender);
            ResultSet rsUser = stmtUser.executeQuery();

            if (rsUser.next()) {
                int userId = rsUser.getInt("user_id");

                // Εισαγωγή στον  patients
                String sqlPatient = "INSERT INTO patients (user_id, amka, address, phone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient);
                stmtPatient.setInt(1, userId);
                stmtPatient.setString(2, amka);
                stmtPatient.setString(3, address);
                stmtPatient.setString(4, phone);
                stmtPatient.executeUpdate();

                response.sendRedirect("login.jsp?msg=registered");
            } else {
                response.sendRedirect("registerPatient.jsp?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("registerPatient.jsp?error=exception");
        }
    }
}
