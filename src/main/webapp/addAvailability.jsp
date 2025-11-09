<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Καταχώρηση Διαθεσιμότητας</title>
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
        .form-label {
            font-weight: bold;
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="doctorHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Καταχώρηση Διαθεσιμότητας
            </span>
        </div>
    </nav>

    <!-- κεντρικό περιεχόμενο -->
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">➕ Καταχώρηση Διαθεσιμότητας</h4>


                    <%
                        String msg = request.getParameter("msg");
                        String error = request.getParameter("error");
                        String at = request.getParameter("at");

                        if ("success".equals(msg)) {
                    %>
                        <div class="alert alert-success text-center">
                             Η διαθεσιμότητα στις <%= at != null ? at : "" %> καταχωρήθηκε με επιτυχία!
                        </div>
                    <% } else if ("past".equals(error)) { %>
                        <div class="alert alert-danger text-center"> Δεν μπορείτε να καταχωρήσετε στο παρελθόν.</div>
                    <% } else if ("outsideHours".equals(error)) { %>
                        <div class="alert alert-danger text-center"> Επιτρέπονται ώρες μόνο 08:00 – 21:30.</div>
                    <% } else if ("duplicate".equals(error)) { %>
                        <div class="alert alert-warning text-center"> Υπάρχει ήδη διαθεσιμότητα σε αυτή την ώρα.</div>
                    <% } else if ("alreadyBooked".equals(error)) { %>
                        <div class="alert alert-warning text-center"> Υπάρχει ήδη ραντεβού σε αυτή την ώρα.</div>
                    <% } else if ("exception".equals(error)) { %>
                        <div class="alert alert-danger text-center"> Σφάλμα κατά την καταχώρηση. Προσπαθήστε ξανά.</div>
                    <% } %>

                    <!-- φφόρμα -->
                    <form action="addAvailability" method="post">
                        <div class="mb-3">
                            <label for="dateTime" class="form-label">Ημερομηνία και Ώρα</label>
                            <input type="datetime-local" class="form-control" id="dateTime" name="dateTime" 
                                   required step="1800">
                        </div>
                        <button type="submit" class="btn btn-success w-100">Καταχώρησε</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="doctorHome.jsp" class="btn btn-outline-secondary">⬅️ Επιστροφή στο Μενού</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        (function () {
            const pad = n => String(n).padStart(2, '0');
            const d = new Date();
            d.setMinutes(d.getMinutes() + (30 - (d.getMinutes() % 30)) % 30, 0, 0);

            const val = d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate())
                      + 'T' + pad(d.getHours()) + ':' + pad(d.getMinutes());

            document.getElementById('dateTime').min = val;
        })();
    </script>

</body>
</html>
