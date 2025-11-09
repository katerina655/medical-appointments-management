<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Εγγραφή Ασθενή</title>
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
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">Εγγραφή Ασθενή</span>
        </div>
    </nav>

    <!-- Φόρμα -->
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4"> Νέα Εγγραφή Ασθενή</h4>

                    <% String error = request.getParameter("error"); %>
                    <% if ("exists".equals(error)) { %>
                        <div class="alert alert-danger"> Το username ή ο ΑΜΚΑ χρησιμοποιείται ήδη!</div>
                    <% } else if ("failed".equals(error)) { %>
                        <div class="alert alert-danger"> Αποτυχία καταχώρησης. Προσπαθήστε ξανά.</div>
                    <% } else if ("exception".equals(error)) { %>
                        <div class="alert alert-danger"> Σφάλμα συστήματος!</div>
                    <% } %>

                    <form action="registerPatient" method="post">
                        <div class="mb-3">
                            <label class="form-label">Όνομα Χρήστη</label>
                            <input type="text" class="form-control" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Κωδικός</label>
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
                            <label class="form-label">ΑΜΚΑ</label>
                            <input type="text" class="form-control" name="amka" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Διεύθυνση</label>
                            <input type="text" class="form-control" name="address" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Τηλέφωνο</label>
                            <input type="text" class="form-control" name="phone" required>
                        </div>

                        <button type="submit" class="btn btn-success w-100"> Εγγραφή</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="login.jsp" class="btn btn-outline-secondary">Επιστροφή στο Login</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
