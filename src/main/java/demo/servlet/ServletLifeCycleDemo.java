package demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletLifeCycleDemo extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6576918204894288850L;
	Connection conn;
	Statement stmt;
	ResultSet rs;

	String JDBC_DRIVER;
	String DB_URL;
	String USER;
	String PASS;

	public void init(ServletConfig sc) throws ServletException {
		try {
			super.init(sc);

			/* JDBC driver name and database URL */
//			JDBC_DRIVER = "org.postgresql.Driver";
//			DB_URL = "jdbc:postgresql://localhost:5432/learning";
//			/* Database credentials */
//			USER = "postgres";
//			PASS = "postgres";
			
			
			JDBC_DRIVER = "com.mysql.jdbc.Driver";
			DB_URL = "jdbc:mysql://localhost:3306/userinfo";
			/* Database credentials */
			USER = "root";
			PASS = "";
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			// Register JDBC driver with DriverManager
			/* Class.forName("org.postgresql.Driver"); */
			
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Initialize statement on Connection Object
			stmt = conn.createStatement();

			// Retrieving Values From Html Textboxes
			String u = request.getParameter("t1");
			String p = request.getParameter("p1");

			stmt.executeUpdate(" INSERT INTO user_table( name, password) VALUES (' " + u + " ',' " + p + " ')");
			rs = stmt.executeQuery("select * from user_table");

			out.println("<html><head><title>Login Page</title></head><body><center>");
			out.println(
					"<table border=2  bordercolor=black cellpadding=5><caption align=center><h2>Database Result</h2></caption>");

			out.println("<tr><th>UserName</th>");
			out.println("<th>PassWord</th></tr>");

			// Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String usrname = rs.getString(3);
				String pwd = rs.getString(4);

				out.println("<tr><td>" + usrname + "</td><td>" + pwd + "</td></tr>");
			}
			out.println("</center></table></body></html>");

		} // try
		catch (Exception e1) {
			// Handle errors for Class.forName
			out.print(e1);
		}
	}

	public void destroy() {

		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
