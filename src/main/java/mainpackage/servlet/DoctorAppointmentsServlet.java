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

@WebServlet("/viewAppointments")
public class DoctorAppointmentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        String filterDate = request.getParameter("date");
        String filterWeek = request.getParameter("week");

        try (Connection conn = DbUtil.getConnection()) {
            String sqlDoctor = "SELECT doctor_id FROM doctors d " +
                               "JOIN users u ON d.user_id=u.user_id WHERE u.username=?";
            PreparedStatement stmtDoctor = conn.prepareStatement(sqlDoctor);
            stmtDoctor.setString(1, username);
            ResultSet rsDoctor = stmtDoctor.executeQuery();

            if (rsDoctor.next()) {
                int doctorId = rsDoctor.getInt("doctor_id");

                String sql = "SELECT a.date_time, a.status, u.name, u.surname " +
                             "FROM appointments a " +
                             "JOIN patients p ON a.patient_id=p.patient_id " +
                             "JOIN users u ON p.user_id=u.user_id " +
                             "WHERE a.doctor_id=? ";

                if (filterDate != null && !filterDate.isEmpty()) {
                    sql += "AND DATE(a.date_time) = ? ";
                }

                if (filterWeek != null && !filterWeek.isEmpty()) {
                    sql += "AND a.date_time BETWEEN ? AND ? ";
                }

                sql += "ORDER BY a.date_time";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, doctorId);

                int paramIndex = 2;

                if (filterDate != null && !filterDate.isEmpty()) {
                    stmt.setDate(paramIndex++, java.sql.Date.valueOf(filterDate));
                }

                if (filterWeek != null && !filterWeek.isEmpty()) {
                    java.time.LocalDate chosenDate = java.time.LocalDate.parse(filterWeek);

                    java.time.LocalDate startOfWeek = chosenDate.with(java.time.DayOfWeek.MONDAY);
                    java.time.LocalDate endOfWeek = chosenDate.with(java.time.DayOfWeek.SUNDAY);

                    stmt.setDate(paramIndex++, java.sql.Date.valueOf(startOfWeek));
                    stmt.setDate(paramIndex++, java.sql.Date.valueOf(endOfWeek));
                }

                ResultSet rs = stmt.executeQuery();
                request.setAttribute("appointments", rs);
                request.getRequestDispatcher("doctorAppointments.jsp").forward(request, response);

            } else {
                response.sendRedirect("doctorHome.jsp?error=nodoctor");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("doctorHome.jsp?error=exception");
        }
    }
}
