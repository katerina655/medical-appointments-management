<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .card {
            border-radius: 12px;
            background-color: rgba(255, 255, 255, 0.9);
        }
        h2 {
            font-weight: bold;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="adminHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Dashboard Διαχειριστή
            </span>
        </div>
    </nav>

    <!-- περιεχόμενο -->
    <div class="container mt-5">
        <h2 class="text-center mb-4">Στατιστικά Συστήματος</h2>

        <div class="row g-4">
            <!-- γιατροί -->
            <div class="col-md-4">
                <div class="card shadow text-center p-4">
                    <h4> Γιατροί</h4>
                    <p class="display-5 text-primary">
                        <%= request.getAttribute("doctorCount") %>
                    </p>
                </div>
            </div>

            <!-- αθενείς -->
            <div class="col-md-4">
                <div class="card shadow text-center p-4">
                    <h4>Ασθενείς</h4>
                    <p class="display-5 text-success">
                        <%= request.getAttribute("patientCount") %>
                    </p>
                </div>
            </div>

            <!-- συνολικά Ραντεβού -->
            <div class="col-md-4">
                <div class="card shadow text-center p-4">
                    <h4>Συνολικά Ραντεβού</h4>
                    <p class="display-5 text-info">
                        <%= request.getAttribute("totalAppointments") %>
                    </p>
                </div>
            </div>
        </div>

        <div class="row g-4 mt-3">
            <!-- ολοκληρωμένα -->
            <div class="col-md-6">
                <div class="card shadow text-center p-4">
                    <h4>Ολοκληρωμένα Ραντεβού</h4>
                    <p class="display-5 text-success">
                        <%= request.getAttribute("completedAppointments") %>
                    </p>
                </div>
            </div>

            <!-- ακυρωμένα -->
            <div class="col-md-6">
                <div class="card shadow text-center p-4">
                    <h4>Ακυρωμένα Ραντεβού</h4>
                    <p class="display-5 text-danger">
                        <%= request.getAttribute("cancelledAppointments") %>
                    </p>
                </div>
            </div>
        </div>

        <!-- επιστροφή -->
        <div class="text-center mt-4">
            <a href="adminHome.jsp" class="btn btn-outline-dark">⬅️ Επιστροφή στο Μενού</a>
        </div>
    </div>

</body>
</html>
