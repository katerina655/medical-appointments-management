<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, mainpackage.DbUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ακύρωση Ραντεβού (Ιατρός)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .card {
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 10px;
        }
        .form-label { font-weight: bold; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="doctorHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">Ακύρωση Ραντεβού</span>
        </div>
    </nav>

    <!-- μέιν περιεχόμενο -->
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-7">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Ακύρωση Ραντεβού</h4>

                    <!-- μηνύματα -->
                    <%
                        String msg = request.getParameter("msg");
                        String err = request.getParameter("error");
                        if (msg != null) {
                    %>
                        <div class="alert alert-success text-center">
                             Η ακύρωση έγινε επιτυχώς!
                        </div>
                    <% } else if (err != null) { %>
                        <div class="alert alert-danger text-center">
                             Δεν μπορεί να ακυρωθεί (πρέπει να είναι τουλάχιστον 3 ημέρες μετά από σήμερα).
                        </div>
                    <% } %>

                    <!-- Φόρμα -->
                    <form action="cancelAppointmentDoctor" method="post" class="mt-2">
                        <div class="mb-3">
                            <label class="form-label">Επιλέξτε ραντεβού για ακύρωση</label>
                            <select name="appointmentId" class="form-select" required>
                                <%
                                    String username = (String) session.getAttribute("username");
                                    try (Connection conn = DbUtil.getConnection()) {
                                        String sql =
                                            "SELECT a.appointment_id, a.date_time, u.name, u.surname " +
                                            "FROM appointments a " +
                                            "JOIN patients p ON a.patient_id = p.patient_id " +
                                            "JOIN users u ON p.user_id = u.user_id " +
                                            "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                                            "JOIN users du ON d.user_id = du.user_id " +
                                            "WHERE du.username=? AND a.status='SCHEDULED' " +
                                            "ORDER BY a.date_time ASC";
                                        PreparedStatement stmt = conn.prepareStatement(sql);
                                        stmt.setString(1, username);
                                        ResultSet rs = stmt.executeQuery();

                                        boolean hasAny = false;
                                        while (rs.next()) {
                                            hasAny = true;
                                %>
                                            <option value="<%= rs.getInt("appointment_id") %>">
                                                <%= rs.getTimestamp("date_time") %> με
                                                <%= rs.getString("name") %> <%= rs.getString("surname") %>
                                            </option>
                                <%
                                        }
                                        rs.getStatement().close();
                                        if (!hasAny) {
                                %>
                                            <option disabled>Δεν υπάρχουν προγραμματισμένα ραντεβού.</option>
                                <%
                                        }
                                    } catch (Exception e) {
                                %>
                                        <option disabled>Σφάλμα φόρτωσης ραντεβού: <%= e.getMessage() %></option>
                                <%
                                    }
                                %>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-danger w-100">Ακύρωση</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="doctorHome.jsp" class="btn btn-outline-secondary">⬅ Επιστροφή στο Μενού</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
