<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Πρόγραμμα Ραντεβού</title>
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
        th {
            background-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="doctorHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">Πρόγραμμα Ραντεβού</span>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Πρόγραμμα Ραντεβού</h4>



                    <form method="get" action="viewAppointments" class="row g-3 mb-4">
                        <div class="col-md-5">
                            <label class="form-label">Φίλτρο ανά ημέρα:</label>
                            <input type="date" class="form-control" name="date">
                        </div>
                        <div class="col-md-5">
                            <label class="form-label">Φίλτρο ανά εβδομάδα:</label>
                            <input type="date" class="form-control" name="week">
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary w-100">Εφαρμογή</button>
                        </div>
                    </form>

                    <!-- Πίνακας ραντεβού -->
                    <div class="table-responsive">
                        <table class="table table-striped table-hover text-center">
                            <thead>
                                <tr>
                                    <th>Ημερομηνία / Ώρα</th>
                                    <th>Ασθενής</th>
                                    <th>Κατάσταση</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    ResultSet rs = (ResultSet) request.getAttribute("appointments");
                                    boolean hasData = false;
                                    if (rs != null) {
                                        while (rs.next()) {
                                            hasData = true;
                                %>
                                    <tr>
                                        <td><%= rs.getTimestamp("date_time") %></td>
                                        <td><%= rs.getString("name") %> <%= rs.getString("surname") %></td>
                                        <td>
                                            <% if ("SCHEDULED".equalsIgnoreCase(rs.getString("status"))) { %>
                                                <span class="badge bg-warning text-dark">Προγραμματισμένο</span>
                                            <% } else if ("COMPLETED".equalsIgnoreCase(rs.getString("status"))) { %>
                                                <span class="badge bg-success">Ολοκληρωμένο</span>
                                            <% } else if ("CANCELED".equalsIgnoreCase(rs.getString("status"))) { %>
                                                <span class="badge bg-danger">Ακυρωμένο</span>
                                            <% } else { %>
                                                <span class="badge bg-secondary"><%= rs.getString("status") %></span>
                                            <% } %>
                                        </td>
                                    </tr>
                                <%
                                        }
                                        rs.getStatement().close();
                                    }
                                    if (!hasData) {
                                %>
                                    <tr>
                                        <td colspan="3">Δεν βρέθηκαν ραντεβού.</td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <div class="text-center mt-3">
                        <a href="doctorHome.jsp" class="btn btn-outline-secondary">⬅ Επιστροφή στο Μενού</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
