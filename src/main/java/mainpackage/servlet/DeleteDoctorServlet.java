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

@WebServlet("/deleteDoctor")
public class DeleteDoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int doctorId = Integer.parseInt(request.getParameter("doctorId"));

        try (Connection conn = DbUtil.getConnection()) {
            // βρίσκουμε το user_id του γιατρού
            String sqlFindUser = "SELECT user_id FROM doctors WHERE doctor_id=?";
            PreparedStatement stmtFind = conn.prepareStatement(sqlFindUser);
            stmtFind.setInt(1, doctorId);
            ResultSet rs = stmtFind.executeQuery();

            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }

            if (userId != -1) {
                // διαγράφουμε  τον γιατρό
                String sqlDeleteDoctor = "DELETE FROM doctors WHERE doctor_id=?";
                PreparedStatement stmtDeleteDoctor = conn.prepareStatement(sqlDeleteDoctor);
                stmtDeleteDoctor.setInt(1, doctorId);
                stmtDeleteDoctor.executeUpdate();

                // διιαγράφουμε  τον χρήστη
                String sqlDeleteUser = "DELETE FROM users WHERE user_id=?";
                PreparedStatement stmtDeleteUser = conn.prepareStatement(sqlDeleteUser);
                stmtDeleteUser.setInt(1, userId);
                stmtDeleteUser.executeUpdate();

                response.sendRedirect("adminHome.jsp?msg=deleted");
            } else {
                response.sendRedirect("adminHome.jsp?msg=notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminHome.jsp?msg=error");
        }
    }
}
