<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*, mainpackage.DbUtil" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Διαγραφή Ιατρού</title>
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

    <nav class="navbar navbar-expand-lg navbar-dark bg-danger">
        <div class="container-fluid">
            <a class="navbar-brand" href="adminHome.jsp">Ιατρικά Ραντεβού</a>
            <span class="navbar-text text-white">
                Διαγραφή Ιατρού
            </span>
        </div>
    </nav>

    <!-- φόρμα δδιαγραφής -->
    <div class="container form-container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-lg p-4">
                    <h4 class="text-center mb-4 text-danger">Επιλογή Ιατρού για Διαγραφή</h4>

                    <!-- Μηνύματα -->
                    <%
                        String msg = request.getParameter("msg");
                        String error = request.getParameter("error");

                        if ("success".equals(msg)) {
                    %>
                        <div class="alert alert-success text-center"> Ο γιατρός διαγράφηκε επιτυχώς!</div>
                    <%
                        } else if ("notfound".equals(error)) {
                    %>
                        <div class="alert alert-warning text-center"> Δεν βρέθηκε ο γιατρός!</div>
                    <%
                        } else if ("exception".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center"> Σφάλμα κατά τη διαγραφή. Προσπαθήστε ξανά.</div>
                    <%
                        }
                    %>

                    <form action="deleteDoctor" method="post">
                        <div class="mb-3">
                            <label class="form-label">Ιατρός</label>
                            <select class="form-select" name="doctorId" required>
                                <%
                                    try (Connection conn = DbUtil.getConnection()) {
                                        String sql = "SELECT d.doctor_id, u.name, u.surname, d.specialty " +
                                                     "FROM doctors d JOIN users u ON d.user_id=u.user_id";
                                        PreparedStatement stmt = conn.prepareStatement(sql);
                                        ResultSet rs = stmt.executeQuery();
                                        while (rs.next()) {
                                %>
                                    <option value="<%= rs.getInt("doctor_id") %>">
                                        <%= rs.getString("name") %> <%= rs.getString("surname") %> 
                                        - <%= rs.getString("specialty") %>
                                    </option>
                                <%
                                        }
                                    } catch (Exception e) {
                                        out.println("<div class='alert alert-danger mt-3'>Σφάλμα: " + e.getMessage() + "</div>");
                                    }
                                %>
                            </select>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-danger"> Διαγραφή</button>
                            <a href="adminHome.jsp" class="btn btn-secondary"> Επιστροφή</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
