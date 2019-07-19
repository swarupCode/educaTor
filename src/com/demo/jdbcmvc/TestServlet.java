package com.demo.jdbcmvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */



@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Step 1: Setup the PrintWriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		// Step 2: Get a connection to the db
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			
			// Step 3: Create SQL statements
			String sql = "select * from student";
			myStmt = myConn.createStatement();
			
			// Step 4: Execute SQL Query
			myRs = myStmt.executeQuery(sql);
			
			// Step 5: Process the result set
			while(myRs.next()) {
				String fName = myRs.getString("first_name");
				String email = myRs.getString("email");
				out.println(fName+" - "+email+"<br/>");
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
