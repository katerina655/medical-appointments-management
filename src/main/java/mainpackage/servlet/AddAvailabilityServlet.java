package mainpackage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mainpackage.DbUtil;

@WebServlet("/addAvailability")
public class AddAvailabilityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private static final DateTimeFormatter HUMAN_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("addAvailability.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username"); 
        String dateTimeRaw = request.getParameter("dateTime");      

        try (Connection conn = DbUtil.getConnection()) {

            // βρίσκω doctor_id
            String sqlDoctor = "SELECT d.doctor_id FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.username=?";
            int doctorId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlDoctor)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) doctorId = rs.getInt("doctor_id");
                }
            }
            if (doctorId == -1) {
                response.sendRedirect("addAvailability.jsp?error=nodoctor");
                return;
            }

            if (dateTimeRaw == null || dateTimeRaw.isBlank()) {
                response.sendRedirect("addAvailability.jsp?error=nodate");
                return;
            }

           
            LocalDateTime ldt;
            try {
                ldt = LocalDateTime.parse(dateTimeRaw); 
            } catch (Exception pe) {
                response.sendRedirect("addAvailability.jsp?error=badformat");
                return;
            }

            
            if (ldt.isBefore(LocalDateTime.now())) {
                response.sendRedirect("addAvailability.jsp?error=past");
                return;
            }

            int hour = ldt.getHour();
            if (hour < 8 || hour >= 22) {
                response.sendRedirect("addAvailability.jsp?error=outsideHours");
                return;
            }

            int minute = ldt.getMinute();
            minute = (minute < 15) ? 0 : (minute < 45 ? 30 : 30); 
            ldt = ldt.withMinute(minute).withSecond(0).withNano(0);

            if (ldt.getHour() >= 22) {
                response.sendRedirect("addAvailability.jsp?error=outsideHours");
                return;
            }

            String sqlExists = "SELECT 1 FROM availabilities WHERE doctor_id=? AND available_date_time=?";
            boolean exists = false;
            try (PreparedStatement ps = conn.prepareStatement(sqlExists)) {
                ps.setInt(1, doctorId);
                ps.setTimestamp(2, Timestamp.valueOf(ldt));
                try (ResultSet rs = ps.executeQuery()) {
                    exists = rs.next();
                }
            }
            if (exists) {
                response.sendRedirect("addAvailability.jsp?error=duplicate");
                return;
            }

            // έλεγχος
            String sqlBooked = "SELECT 1 FROM appointments WHERE doctor_id=? AND date_time=? AND status <> 'CANCELED'";
            boolean booked = false;
            try (PreparedStatement ps = conn.prepareStatement(sqlBooked)) {
                ps.setInt(1, doctorId);
                ps.setTimestamp(2, Timestamp.valueOf(ldt));
                try (ResultSet rs = ps.executeQuery()) {
                    booked = rs.next();
                }
            }
            if (booked) {
                response.sendRedirect("addAvailability.jsp?error=alreadyBooked");
                return;
            }

            // εισαγωγή διαθεσιμότητας
            String sqlInsert = "INSERT INTO availabilities (doctor_id, available_date_time) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, doctorId);
                ps.setTimestamp(2, Timestamp.valueOf(ldt));
                ps.executeUpdate();
            }

            response.sendRedirect("addAvailability.jsp?msg=success&at=" + ldt.format(HUMAN_FMT).replace(" ", "%20"));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addAvailability.jsp?error=exception");
        }
    }
}
