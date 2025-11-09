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

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DbUtil.getConnection()) {

            // αριθμός Γιατρών
            PreparedStatement stmtDoctors = conn.prepareStatement("SELECT COUNT(*) FROM doctors");
            ResultSet rsDoctors = stmtDoctors.executeQuery();
            int doctorCount = rsDoctors.next() ? rsDoctors.getInt(1) : 0;

            // αριθμός Ασθενών
            PreparedStatement stmtPatients = conn.prepareStatement("SELECT COUNT(*) FROM patients");
            ResultSet rsPatients = stmtPatients.executeQuery();
            int patientCount = rsPatients.next() ? rsPatients.getInt(1) : 0;

            // συνολικά Ραντεβού
            PreparedStatement stmtTotalAppointments = conn.prepareStatement("SELECT COUNT(*) FROM appointments");
            ResultSet rsTotalAppointments = stmtTotalAppointments.executeQuery();
            int totalAppointments = rsTotalAppointments.next() ? rsTotalAppointments.getInt(1) : 0;

            // ολοκληρωμένα Ραντεβού
            PreparedStatement stmtCompleted = conn.prepareStatement(
                    "SELECT COUNT(*) FROM appointments WHERE status='COMPLETED'");
            ResultSet rsCompleted = stmtCompleted.executeQuery();
            int completedAppointments = rsCompleted.next() ? rsCompleted.getInt(1) : 0;

            // ακυρωμένα Ραντεβού
            PreparedStatement stmtCancelled = conn.prepareStatement(
                    "SELECT COUNT(*) FROM appointments WHERE status='CANCELLED'");
            ResultSet rsCancelled = stmtCancelled.executeQuery();
            int cancelledAppointments = rsCancelled.next() ? rsCancelled.getInt(1) : 0;

            request.setAttribute("doctorCount", doctorCount);
            request.setAttribute("patientCount", patientCount);
            request.setAttribute("totalAppointments", totalAppointments);
            request.setAttribute("completedAppointments", completedAppointments);
            request.setAttribute("cancelledAppointments", cancelledAppointments);

            // προώθηση στο JSP
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminHome.jsp?error=dashboard");
        }
    }
}
