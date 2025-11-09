<%@ page contentType="text/html; charset=UTF-8" %>
<%
    session.invalidate(); // Τερματίζει το session
    response.sendRedirect("login.jsp");
%>
