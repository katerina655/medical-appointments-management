<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Ιατρικά Ραντεβού</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: url('images/medical-bg.jpg') no-repeat center center fixed;
            background-size: cover;
            font-family: Arial, sans-serif;
        }

        .card {
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center mt-5">
        <div class="col-md-4">
            <div class="card shadow-lg">
                <div class="card-header text-center bg-primary text-white">
                    <h4>Σύνδεση Χρήστη</h4>
                </div>
                <div class="card-body">
                    <!-- κείμοεο πάνω από τη φόρμα -->
                    <p class="text-muted text-center">
                        Συμπληρώστε τα στοιχεία σας για να συνδεθείτε στο σύστημα ιατρικών ραντεβού.
                    </p>

                    <!-- φόρμα σύνδεσης -->
                    <form action="login" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">Όνομα Χρήστη</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Κωδικός</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Είσοδος</button>
                    </form>

                    <!-- Μήνυμα λάθους -->
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null && !errorMessage.isEmpty()) {
                    %>
                        <div class="alert alert-danger mt-3 text-center">
                            <%= errorMessage %>
                        </div>
                    <% } %>

                    <!-- Κουμπί Εγγραφής Νέου Ασθενή -->
                    <div class="text-center mt-3">
                        <a href="registerPatient.jsp" class="btn btn-outline-success w-100">
                             Εγγραφή Νέου Ασθενή
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
