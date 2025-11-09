<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Αρχική Ασθενή</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/patients1.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .menu-container {
            margin-top: 100px;
        }
        .card {
            background-color: rgba(255, 255, 255, 0.9);
        }
    </style>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Καλώς ήρθες, <%= session.getAttribute("name") %>!
            </span>
        </div>
    </nav>

    <!-- Κουμπιά μενού -->
    <div class="container menu-container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Μενού Ασθενή</h4>
                    <div class="d-grid gap-3">
                        <a href="patientHistory" class="btn btn-outline-primary btn-lg">Προβολή ιστορικού ραντεβού</a>
                        <a href="bookAppointment" class="btn btn-outline-success btn-lg"> Κλείσιμο νέου ραντεβού</a>
                        <a href="cancelAppointment" class="btn btn-outline-warning btn-lg">Ακύρωση ραντεβού</a>
                        <a href="doctorInfo" class="btn btn-outline-info btn-lg"> Πληροφορίες Γιατρών</a>
                        <a href="logout.jsp" class="btn btn-outline-danger btn-lg"> Αποσύνδεση</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
