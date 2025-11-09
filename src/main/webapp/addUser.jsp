<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Εισαγωγή Νέου Χρήστη</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: Arial, sans-serif;
        }
        .form-container {
            margin-top: 80px;
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
            <a class="navbar-brand" href="adminHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                ➕ Εισαγωγή Νέου Χρήστη
            </span>
        </div>
    </nav>

    <!-- Φόρμα εισαγωγής -->
    <div class="container form-container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Στοιχεία Χρήστη</h4>

                    <form action="addUser" method="post">
                        <div class="mb-3">
                            <label class="form-label">Username</label>
                            <input type="text" class="form-control" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" class="form-control" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Όνομα</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Επώνυμο</label>
                            <input type="text" class="form-control" name="surname" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Φύλο</label>
                            <select class="form-select" name="gender" required>
                                <option value="MALE">Άνδρας</option>
                                <option value="FEMALE">Γυναίκα</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Ρόλος</label>
                            <select class="form-select" name="role" required onchange="toggleExtraFields(this.value)">
                                <option value="PATIENT" selected>Ασθενής</option>
                                <option value="DOCTOR">Γιατρός</option>
                                <option value="ADMIN">Διαχειριστής</option>
                            </select>
                        </div>

                        <!-- εxtra για Ασθενείς -->
                        <div id="patientFields" style="display:none;">
                            <div class="mb-3">
                                <label class="form-label">ΑΜΚΑ</label>
                                <input type="text" class="form-control" name="amka">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Διεύθυνση</label>
                                <input type="text" class="form-control" name="address">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Τηλέφωνο</label>
                                <input type="text" class="form-control" name="phonePatient">
                            </div>
                        </div>

                        <!-- εxtra για Γιατρούς -->
                        <div id="doctorFields" style="display:none;">
                            <div class="mb-3">
                                <label class="form-label">Ειδικότητα</label>
                                <input type="text" class="form-control" name="specialty">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Τηλέφωνο</label>
                                <input type="text" class="form-control" name="phoneDoctor">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Γραφείο</label>
                                <input type="text" class="form-control" name="office">
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success"> Καταχώρηση</button>
                            <a href="adminHome.jsp" class="btn btn-secondary"> Επιστροφή</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleExtraFields(role) {
            document.getElementById("patientFields").style.display = (role === "PATIENT") ? "block" : "none";
            document.getElementById("doctorFields").style.display = (role === "DOCTOR") ? "block" : "none";
        }

        window.onload = function() {
            var roleSelect = document.querySelector("select[name='role']");
            toggleExtraFields(roleSelect.value);
        };
    </script>

</body>
</html>
