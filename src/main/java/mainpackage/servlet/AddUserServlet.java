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

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("addUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String gender = request.getParameter("gender");
        String role = request.getParameter("role");

        try (Connection conn = DbUtil.getConnection()) {
            // δημιουργούμε salt και hash
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);

          
            String sqlInsertUser = "INSERT INTO users (username, password_hash, password_salt, name, surname, gender, role) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING user_id";
            PreparedStatement stmtUser = conn.prepareStatement(sqlInsertUser);
            stmtUser.setString(1, username);
            stmtUser.setString(2, hashedPassword);
            stmtUser.setString(3, salt);
            stmtUser.setString(4, name);
            stmtUser.setString(5, surname);
            stmtUser.setString(6, gender);
            stmtUser.setString(7, role);

            ResultSet rs = stmtUser.executeQuery();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }

            // ανάλογα με το ρόλο, συμπληρώνουμε τον κατάλληλο πίνακα
            if ("PATIENT".equalsIgnoreCase(role)) {
                String amka = request.getParameter("amka");
                String address = request.getParameter("address");
                String phone = request.getParameter("phonePatient");

                String sqlPatient = "INSERT INTO patients (user_id, amka, address, phone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient);
                stmtPatient.setInt(1, userId);
                stmtPatient.setString(2, amka);
                stmtPatient.setString(3, address);
                stmtPatient.setString(4, phone);
                stmtPatient.executeUpdate();

            } else if ("DOCTOR".equalsIgnoreCase(role)) {
                String specialty = request.getParameter("specialty");
                String phone = request.getParameter("phoneDoctor");
                String office = request.getParameter("office");

                String sqlDoctor = "INSERT INTO doctors (user_id, specialty, phone, office) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtDoctor = conn.prepareStatement(sqlDoctor);
                stmtDoctor.setInt(1, userId);
                stmtDoctor.setString(2, specialty);
                stmtDoctor.setString(3, phone);
                stmtDoctor.setString(4, office);
                stmtDoctor.executeUpdate();
            }

            response.sendRedirect("adminHome.jsp?msg=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addUser.jsp?error=exception");
        }
    }
}
