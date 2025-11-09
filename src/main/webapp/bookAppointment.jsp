<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Κλείσιμο Ραντεβού</title>
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

    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="patientHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Κλείσιμο Νέου Ραντεβού
            </span>
        </div>
    </nav>

    <!-- κεντρική Κάρτα -->
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4">Κλείσιμο Νέου Ραντεβού</h4>

                    <!-- φφόρμα -->
                    <form action="bookAppointment" method="post">

                        <!-- Επιλογή Ειδικότητας -->
                        <div class="mb-3">
                            <label class="form-label">Επιλογή Ειδικότητας</label>
                            <select name="specialty" class="form-select" onchange="reloadWithSpecialty(this.value)">
                                <option value="">--Επιλέξτε Ειδικότητα--</option>
                                <%
                                    List<String> specialties = (List<String>) request.getAttribute("specialties");
                                    String selectedSpecialty = request.getParameter("specialty");
                                    if (specialties != null) {
                                        for (String spec : specialties) {
                                %>
                                    <option value="<%= spec %>" <%= (spec.equals(selectedSpecialty) ? "selected" : "") %>>
                                        <%= spec %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <!-- επιλογή γιατρου -->
                        <div class="mb-3">
                            <label class="form-label">Επιλογή Ιατρού</label>
                            <select name="doctorId" class="form-select" required>
                                <%
                                    List<String[]> doctors = (List<String[]>) request.getAttribute("doctors");
                                    if (doctors != null) {
                                        for (String[] doc : doctors) {
                                %>
                                    <option value="<%= doc[0] %>"><%= doc[1] %> <%= doc[2] %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <!-- επππιλογή διαθεσιμότητας -->
                        <div class="mb-3">
                            <label class="form-label">Επιλογή Ημερομηνίας / Ώρας</label>
                            <select name="availabilityId" class="form-select" required>
                                <%
                                    List<String[]> availabilities = (List<String[]>) request.getAttribute("availabilities");
                                    if (availabilities != null) {
                                        for (String[] a : availabilities) {
                                %>
                                    <option value="<%= a[0] %>"><%= a[1] %> - <%= a[2] %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <!-- τρόπος πληρωμής -->
                        <div class="mb-3">
                            <label class="form-label">Τρόπος Πληρωμής</label>
                            <select name="paymentMethod" class="form-select" required>
                                <option value="">--Επιλέξτε Τρόπο--</option>
                                <option value="CARD">Κάρτα</option>
                                <option value="CASH">Μετρητά</option>
                            </select>
                        </div>

                        <!-- κουμπί -->
                        <button type="submit" class="btn btn-success w-100">Κλείσε Ραντεβού</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="patientHome.jsp" class="btn btn-outline-secondary">⬅Επιστροφή</a>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- όταν αλλάζει ειδικότητα -->
    <script>
        function reloadWithSpecialty(specialty) {
            if (specialty) {
                window.location.href = "bookAppointment?specialty=" + specialty;
            } else {
                window.location.href = "bookAppointment";
            }
        }
    </script>

</body>
</html>
