<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Αρχική Γιατρού</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .menu-container {
            margin-top: 100px;
        }
        .card {
            background-color: rgba(255, 255, 255, 0.95);
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

    <!-- μενού γιατρου -->
    <div class="container menu-container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Μενού Γιατρού</h4>
                    <div class="d-grid gap-3">
                        <a href="addAvailability" class="btn btn-outline-success btn-lg"> Καταχώρηση Διαθεσιμότητας</a>
                        <a href="viewAppointments" class="btn btn-outline-primary btn-lg"> Προβολή Ραντεβού</a>
                        <a href="cancelAppointmentDoctor" class="btn btn-outline-warning btn-lg">Ακύρωση Ραντεβού</a>
                        <a href="patientList" class="btn btn-outline-info btn-lg">Λίστα Ασθενών</a>
                        <a href="logout.jsp" class="btn btn-outline-danger btn-lg"> Αποσύνδεση</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
