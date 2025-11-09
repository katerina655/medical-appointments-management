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

@WebServlet("/patientList")
public class PatientListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        List<String[]> patients = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection()) {
            String sql = "SELECT p.patient_id, u.name, u.surname, p.amka, p.phone, a.date_time, a.status " +
                         "FROM appointments a " +
                         "JOIN patients p ON a.patient_id = p.patient_id " +
                         "JOIN users u ON p.user_id = u.user_id " +
                         "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                         "JOIN users du ON d.user_id = du.user_id " +
                         "WHERE du.username = ? " +
                         "ORDER BY a.date_time DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString("name");
                row[1] = rs.getString("surname");
                row[2] = rs.getString("amka");     
                row[3] = rs.getString("phone");
                row[4] = rs.getTimestamp("date_time").toString();
                row[5] = rs.getString("status");
                patients.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("patients", patients);
        request.getRequestDispatcher("patientList.jsp").forward(request, response);
    }
}
