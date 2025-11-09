package mainpackage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mainpackage.DbUtil;

@WebServlet("/doctorInfo")
public class DoctorInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String[]> doctors = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection()) {
            String sql = "SELECT u.name, u.surname, d.specialty, d.phone, d.office " +
                         "FROM doctors d JOIN users u ON d.user_id=u.user_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] row = new String[5];
                row[0] = rs.getString("name");
                row[1] = rs.getString("surname");
                row[2] = rs.getString("specialty");
                row[3] = rs.getString("phone");
                row[4] = rs.getString("office");
                doctors.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("doctors", doctors);
        request.getRequestDispatcher("doctorInfo.jsp").forward(request, response);
    }
}
