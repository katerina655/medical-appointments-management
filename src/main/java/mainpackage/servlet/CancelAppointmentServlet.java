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
import javax.servlet.http.HttpSession;

import mainpackage.DbUtil;

@WebServlet("/cancelAppointment")
public class CancelAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("cancelAppointment.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try (Connection conn = DbUtil.getConnection()) {
            String sqlPatient = "SELECT patient_id FROM patients p " +
                                "JOIN users u ON p.user_id=u.user_id WHERE u.username=?";
            PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient);
            stmtPatient.setString(1, username);
            ResultSet rsPatient = stmtPatient.executeQuery();

            if (rsPatient.next()) {
                int patientId = rsPatient.getInt("patient_id");

                String sqlCheck = "SELECT date_time FROM appointments WHERE appointment_id=? AND patient_id=?";
                PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
                stmtCheck.setInt(1, appointmentId);
                stmtCheck.setInt(2, patientId);
                ResultSet rsCheck = stmtCheck.executeQuery();

                if (rsCheck.next()) {
                    Timestamp dateTime = rsCheck.getTimestamp("date_time");
                    Timestamp now = new Timestamp(System.currentTimeMillis());

                    long diff = dateTime.getTime() - now.getTime();
                    long days = diff / (1000 * 60 * 60 * 24);

                    if (days >= 3) {
                        String sqlCancel = "UPDATE appointments SET status='CANCELED' WHERE appointment_id=?";
                        PreparedStatement stmtCancel = conn.prepareStatement(sqlCancel);
                        stmtCancel.setInt(1, appointmentId);
                        stmtCancel.executeUpdate();

                        response.sendRedirect("cancelAppointment.jsp?msg=success");
                    } else {
                        response.sendRedirect("cancelAppointment.jsp?error=toosoon");
                    }
                } else {
                    response.sendRedirect("cancelAppointment.jsp?error=notfound");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cancelAppointment.jsp?error=exception");
        }
    }
}
