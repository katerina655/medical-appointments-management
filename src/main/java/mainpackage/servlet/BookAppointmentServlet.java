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

@WebServlet("/bookAppointment")
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String specialty = request.getParameter("specialty");

        try (Connection conn = DbUtil.getConnection()) {
            List<String> specialties = new ArrayList<>();
            PreparedStatement stmtSpec = conn.prepareStatement("SELECT DISTINCT specialty FROM doctors");
            ResultSet rsSpec = stmtSpec.executeQuery();
            while (rsSpec.next()) {
                specialties.add(rsSpec.getString("specialty"));
            }
            request.setAttribute("specialties", specialties);

            if (specialty != null && !specialty.isEmpty()) {
                List<String[]> doctors = new ArrayList<>();
                String sqlDoctors = "SELECT d.doctor_id, u.name, u.surname " +
                        "FROM doctors d JOIN users u ON d.user_id=u.user_id " +
                        "WHERE d.specialty=?";
                PreparedStatement stmtDocs = conn.prepareStatement(sqlDoctors);
                stmtDocs.setString(1, specialty);
                ResultSet rsDocs = stmtDocs.executeQuery();
                while (rsDocs.next()) {
                    doctors.add(new String[]{
                            rsDocs.getString("doctor_id"),
                            rsDocs.getString("name"),
                            rsDocs.getString("surname")
                    });
                }
                request.setAttribute("doctors", doctors);

                List<String[]> availabilities = new ArrayList<>();
                String sqlAvail = "SELECT a.availability_id, a.available_date_time, u.name, u.surname " +
                        "FROM availabilities a " +
                        "JOIN doctors d ON a.doctor_id=d.doctor_id " +
                        "JOIN users u ON d.user_id=u.user_id " +
                        "WHERE d.specialty=? AND a.available_date_time > NOW()";
                PreparedStatement stmtAvail = conn.prepareStatement(sqlAvail);
                stmtAvail.setString(1, specialty);
                ResultSet rsAvail = stmtAvail.executeQuery();
                while (rsAvail.next()) {
                    availabilities.add(new String[]{
                            rsAvail.getString("availability_id"),
                            rsAvail.getString("available_date_time"),
                            rsAvail.getString("name") + " " + rsAvail.getString("surname")
                    });
                }
                request.setAttribute("availabilities", availabilities);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("bookAppointment.jsp").forward(request, response);
    }

    //  κράτηση ραντεβού
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        String doctorIdStr = request.getParameter("doctorId");
        String availabilityIdStr = request.getParameter("availabilityId");
        String paymentMethod = request.getParameter("paymentMethod"); // 🔹 νέο πεδίο

        if (doctorIdStr == null || availabilityIdStr == null || paymentMethod == null ||
            doctorIdStr.isEmpty() || availabilityIdStr.isEmpty() || paymentMethod.isEmpty()) {
            response.sendRedirect("bookAppointment.jsp?error=missing");
            return;
        }

        int doctorId = Integer.parseInt(doctorIdStr);
        int availabilityId = Integer.parseInt(availabilityIdStr);

        try (Connection conn = DbUtil.getConnection()) {
            // βρίσκουμε  το patient_id
            String sqlPatient = "SELECT patient_id FROM patients p " +
                    "JOIN users u ON p.user_id=u.user_id WHERE u.username=?";
            PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient);
            stmtPatient.setString(1, username);
            ResultSet rsPatient = stmtPatient.executeQuery();

            if (rsPatient.next()) {
                int patientId = rsPatient.getInt("patient_id");

                // παίρνουμε την ώρα
                String sqlAvailability = "SELECT available_date_time FROM availabilities WHERE availability_id=?";
                PreparedStatement stmtAvail = conn.prepareStatement(sqlAvailability);
                stmtAvail.setInt(1, availabilityId);
                ResultSet rsAvail = stmtAvail.executeQuery();

                if (rsAvail.next()) {
                    java.sql.Timestamp dateTime = rsAvail.getTimestamp("available_date_time");

                    String sqlInsert = "INSERT INTO appointments (patient_id, doctor_id, date_time, status, payment_method) " +
                                       "VALUES (?, ?, ?, 'SCHEDULED', ?)";
                    PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                    stmtInsert.setInt(1, patientId);
                    stmtInsert.setInt(2, doctorId);
                    stmtInsert.setTimestamp(3, dateTime);
                    stmtInsert.setString(4, paymentMethod);
                    stmtInsert.executeUpdate();

                    String sqlDelete = "DELETE FROM availabilities WHERE availability_id=?";
                    PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete);
                    stmtDelete.setInt(1, availabilityId);
                    stmtDelete.executeUpdate();

                    response.sendRedirect("patientHome.jsp?msg=success");
                } else {
                    response.sendRedirect("bookAppointment.jsp?error=noavailability");
                }
            } else {
                response.sendRedirect("login.jsp?error=nopatient");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("bookAppointment.jsp?error=exception");
        }
    }
}
