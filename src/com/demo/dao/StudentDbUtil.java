package com.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.demo.bean.Student;

public class StudentDbUtil {

	private static DataSource dataSource;
	
	public StudentDbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents() throws Exception {
		
		List<Student> data = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
		con = dataSource.getConnection();
		String sql = "select * from student order by last_name";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String email = rs.getString("email");
			
			Student tempStudent = new Student(id, firstName, lastName, email);
			
			data.add(tempStudent);
		}
		
		return data;
		}
		finally {
			// close JDBC objects
			close(con, stmt, rs);
			
		}
		
	}

	private static void close(Connection con, Statement stmt, ResultSet rs) {
		try {
			if(rs!=null) {
				rs.close();
			}
			if(con!=null) {
				con.close(); //doesn't really close it... just puts it back to connection pool
			}
			if(stmt!=null) {
				stmt.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void addStudent(Student newStudent) throws Exception{

		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			
		//get db connection
			con = dataSource.getConnection();
			
		//create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email)"
					   + "values (?, ?, ?)";
			
			stmt = con.prepareStatement(sql);
			
		//set the param values for the student
			stmt.setString(1, newStudent.getFirstName());
			stmt.setString(2, newStudent.getLastName());
			stmt.setString(3, newStudent.getEmail());
			
		//execute sql insert
			stmt.execute();
		}
		finally {
			//clean up jdbc objects
			close(con, stmt, null);
		}
		
	}

	public Student getstudent(String theStudentId) throws Exception {

		Student theStudent = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int studentId;
		
		try {
			//convert studentid to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to db
			conn = dataSource.getConnection();
			
			//create sql to get selected students
			String sql = "select * from student where id = ?";
			
			//create prepared statement
			stmt = conn.prepareStatement(sql);
			
			//set params
			stmt.setInt(1, studentId);
			
			//execute statement
			stmt.execute();
			
			//retrieve data from rs row
			rs = stmt.executeQuery();
			if(rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
			
			//use the student id during construction
			theStudent = new Student(studentId, firstName, lastName, email);			
			}
			else {
				throw new Exception("Could not find student id: "+studentId);
			}
			return theStudent;
		}
		finally {
			close(conn, stmt, rs);
		}
		
		
	}

	public static void updateStudent(Student updateStudent) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			//get connection to db
			conn = dataSource.getConnection();
			
			//create sql to get selected students
			String sql = "update student set first_name=?, last_name=?, email=? where id=?";
			
			//create prepared statement
			stmt = conn.prepareStatement(sql);
			
			//set params
			stmt.setString(1, updateStudent.getFirstName());
			stmt.setString(2, updateStudent.getLastName());
			stmt.setString(3, updateStudent.getEmail());
			stmt.setInt(4, updateStudent.getId());
			
			//execute statement
			stmt.execute();
		}
		finally {
			close(conn, stmt, null);
		}
	}

	public void deleteStudent(int studentId) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			//get connection to db
			conn = dataSource.getConnection();
			
			//create sql to get selected students
			String sql = "delete from student where id=?";
			
			//create prepared statement
			stmt = conn.prepareStatement(sql);
			
			//set params
			stmt.setInt(1, studentId);
			
			//execute statement
			stmt.execute();
		}
		finally {
			close(conn, stmt, null);
		}
		
	}
}
