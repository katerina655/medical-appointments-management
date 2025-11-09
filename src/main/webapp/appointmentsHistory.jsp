<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ιστορικό Ραντεβού</title>
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
                Ιστορικό Ραντεβού
            </span>
        </div>
    </nav>

    <!-- πίνακας ιστορικού -->
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Ιστορικό Ραντεβού</h4>
                    
                    <table class="table table-bordered table-striped text-center align-middle">
                        <thead class="table-primary">
                            <tr>
                                <th>Ημερομηνία</th>
                                <th>Κατάσταση</th>
                                <th>Γιατρός</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            java.util.List<String[]> appointments =
                                (java.util.List<String[]>) request.getAttribute("appointments");

                            if (appointments != null && !appointments.isEmpty()) {
                                for (String[] row : appointments) {
                        %>
                            <tr>
                                <td><%= row[0] %></td>
                                <td>
                                    <% if ("COMPLETED".equalsIgnoreCase(row[1])) { %>
                                        <span class="badge bg-success">Ολοκληρώθηκε</span>
                                    <% } else if ("SCHEDULED".equalsIgnoreCase(row[1])) { %>
                                        <span class="badge bg-warning text-dark">Προγραμματισμένο</span>
                                    <% } else { %>
                                        <span class="badge bg-secondary"><%= row[1] %></span>
                                    <% } %>
                                </td>
                                <td><%= row[2] %></td>
                            </tr>
                        <% 
                                }
                            } else { 
                        %>
                            <tr>
                                <td colspan="3" class="text-muted">Δεν βρέθηκαν ραντεβού.</td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>

                    <div class="text-center mt-3">
                        <a href="patientHome.jsp" class="btn btn-outline-primary">⬅ Πίσω στο μενού</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
