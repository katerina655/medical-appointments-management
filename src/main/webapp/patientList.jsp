<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Λίστα Ασθενών</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <h2 class="text-center mb-4">Λίστα Ασθενών</h2>

    <table class="table table-bordered table-striped shadow">
        <thead class="table-primary">
            <tr>
                <th>Όνομα</th>
                <th>Επώνυμο</th>
                <th>ΑΜΚΑ</th>
                <th>Τηλέφωνο</th>
                <th>Ημερομηνία Ραντεβού</th>
                <th>Κατάσταση</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<String[]> patients = (List<String[]>) request.getAttribute("patients");
                if (patients != null && !patients.isEmpty()) {
                    for (String[] p : patients) {
            %>
                        <tr>
                            <td><%= p[0] %></td>
                            <td><%= p[1] %></td>
                            <td><%= p[2] != null ? p[2] : "-" %></td>
                            <td><%= p[3] != null ? p[3] : "-" %></td>
                            <td><%= p[4] %></td>
                            <td><%= p[5] %></td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="6" class="text-center text-muted">Δεν υπάρχουν ασθενείς.</td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <div class="text-center mt-3">
        <a href="doctorHome.jsp" class="btn btn-secondary">Επιστροφή στο Μενού</a>
    </div>
</div>

</body>
</html>
