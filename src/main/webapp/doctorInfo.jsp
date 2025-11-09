<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Πληροφορίες Γιατρών</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <h2 class="text-center mb-4">Πληροφορίες Γιατρών</h2>

    <table class="table table-bordered table-striped shadow">
        <thead class="table-primary">
            <tr>
                <th>Όνομα</th>
                <th>Επώνυμο</th>
                <th>Ειδικότητα</th>
                <th>Τηλέφωνο</th>
                <th>Γραφείο</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<String[]> doctors = (List<String[]>) request.getAttribute("doctors");
                if (doctors != null && !doctors.isEmpty()) {
                    for (String[] doc : doctors) {
            %>
                        <tr>
                            <td><%= doc[0] %></td>
                            <td><%= doc[1] %></td>
                            <td><%= doc[2] %></td>
                            <td><%= doc[3] %></td>
                            <td><%= doc[4] %></td>
                        </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="5" class="text-center text-muted">Δεν υπάρχουν γιατροί στη βάση.</td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <div class="text-center mt-3">
        <a href="patientHome.jsp" class="btn btn-secondary">Επιστροφή στο Μενού</a>
    </div>
</div>

</body>
</html>
