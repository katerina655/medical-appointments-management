<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, mainpackage.DbUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ακύρωση Ραντεβού</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .card {
            background-color: rgba(255, 255, 255, 0.95);
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="patientHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Ακύρωση Ραντεβού
            </span>
        </div>
    </nav>


    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Επιλέξτε ραντεβού για ακύρωση</h4>
                    <form action="cancelAppointment" method="post">

                        <!-- ντροπνταοτν με τα ραντεβού -->
                        <div class="mb-3">
                            <label class="form-label">Ραντεβού</label>
                            <select name="appointmentId" class="form-select" required>
                                <%
                                    String username = (String) session.getAttribute("username");
                                    try (Connection conn = DbUtil.getConnection()) {
                                        String sql = "SELECT a.appointment_id, a.date_time, u.name, u.surname " +
                                                     "FROM appointments a " +
                                                     "JOIN doctors d ON a.doctor_id=d.doctor_id " +
                                                     "JOIN users u ON d.user_id=u.user_id " +
                                                     "JOIN patients p ON a.patient_id=p.patient_id " +
                                                     "JOIN users u2 ON p.user_id=u2.user_id " +
                                                     "WHERE u2.username=? AND a.status='SCHEDULED' " +
                                                     "AND a.date_time > NOW()";
                                        PreparedStatement stmt = conn.prepareStatement(sql);
                                        stmt.setString(1, username);
                                        ResultSet rs = stmt.executeQuery();
                                        while (rs.next()) {
                                %>
                                    <option value="<%= rs.getInt("appointment_id") %>">
                                        <%= rs.getTimestamp("date_time") %> με <%= rs.getString("name") %> <%= rs.getString("surname") %>
                                    </option>
                                <%
                                        }
                                    } catch (Exception e) {
                                        out.println("<option disabled>Σφάλμα: " + e.getMessage() + "</option>");
                                    }
                                %>
                            </select>
                        </div>

                        <!-- κουμπί ακύρωσης -->
                        <button type="submit" class="btn btn-danger w-100">Ακύρωση</button>
                    </form>

                    <!-- μνύματα -->
                    <%
                        String msg = request.getParameter("msg");
                        String error = request.getParameter("error");
                        if (msg != null) {
                    %>
                        <div class="alert alert-success mt-3 text-center">Η ακύρωση έγινε επιτυχώς!</div>
                    <% } else if (error != null) { %>
                        <div class="alert alert-danger mt-3 text-center">Δεν μπορεί να ακυρωθεί (τουλάχιστον 3 μέρες πριν)!</div>
                    <% } %>

                    <div class="text-center mt-3">
                        <a href="patientHome.jsp" class="btn btn-outline-primary">⬅ Πίσω στο μενού</a>
                    </div>

                </div>
            </div>
        </div>
    </div>

</body>
</html>
