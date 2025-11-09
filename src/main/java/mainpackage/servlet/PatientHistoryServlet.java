package mainpackage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;   
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mainpackage.DbUtil;

@WebServlet("/patientHistory")  
public class PatientHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp?error=notloggedin");
            return;
        }

        String username = (String) session.getAttribute("username");

        List<String[]> appointments = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection()) {
            String sql =
                "SELECT a.date_time, a.status, u.name AS doctor_name, u.surname AS doctor_surname " +
                "FROM appointments a " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN users up ON p.user_id = up.user_id " +
                "WHERE up.username = ? " +
                "ORDER BY a.date_time DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[3];
                row[0] = rs.getString("date_time");
                row[1] = rs.getString("status");
                row[2] = rs.getString("doctor_name") + " " + rs.getString("doctor_surname");
                appointments.add(row);
            }

            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("appointmentsHistory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }
}
