package mainpackage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainpackage.DbUtil;

@WebServlet("/cancelAppointmentDoctor")
public class CancelAppointmentDoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("cancelAppointmentDoctor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try (Connection conn = DbUtil.getConnection()) {
            String sql = "SELECT date_time FROM appointments WHERE appointment_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp dateTime = rs.getTimestamp("date_time");

                long diffMillis = dateTime.getTime() - System.currentTimeMillis();
                long diffDays = diffMillis / (1000 * 60 * 60 * 24);

                if (diffDays >= 3) {
                    String sqlUpdate = "UPDATE appointments SET status='CANCELED' WHERE appointment_id=?";
                    PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                    stmtUpdate.setInt(1, appointmentId);
                    stmtUpdate.executeUpdate();

                    response.sendRedirect("cancelAppointmentDoctor.jsp?msg=success");
                } else {
                    response.sendRedirect("cancelAppointmentDoctor.jsp?error=tooclose");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cancelAppointmentDoctor.jsp?error=exception");
        }
    }
}
