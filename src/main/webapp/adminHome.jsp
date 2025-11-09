<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Αρχική Διαχειριστή</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .menu-container {
            margin-top: 100px;
        }
        .card {
            background-color: rgba(255, 255, 255, 0.95);
            border-radius: 10px;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Καλώς ήρθες, <%= session.getAttribute("name") %>!
            </span>
        </div>
    </nav>

    <!-- μηνύματα -->
    <div class="container mt-3">
        <%
            String msg = request.getParameter("msg");
            if ("deleted".equals(msg)) {
        %>
            <div class="alert alert-success text-center"> Ο γιατρός διαγράφηκε επιτυχώς!</div>
        <% } else if ("error".equals(msg)) { %>
            <div class="alert alert-danger text-center"> Σφάλμα κατά τη διαγραφή!</div>
        <% } else if ("success".equals(msg)) { %>
            <div class="alert alert-success text-center">Ο χρήστης καταχωρήθηκε επιτυχώς!</div>
        <% } %>
    </div>

    <!-- Κουμπιά μενού -->
    <div class="container menu-container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Μενού Διαχειριστή</h4>
                    <div class="d-grid gap-3">
                        <a href="addUser" class="btn btn-outline-success btn-lg">👤 Εισαγωγή Νέου Χρήστη</a>
                        <a href="deleteDoctor.jsp" class="btn btn-outline-danger btn-lg">🩺 Διαγραφή Ιατρού</a>
                        <a href="adminDashboard" class="btn btn-outline-primary btn-lg">📊 Dashboard</a>
                        <a href="logout.jsp" class="btn btn-outline-secondary btn-lg">🚪 Αποσύνδεση</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
