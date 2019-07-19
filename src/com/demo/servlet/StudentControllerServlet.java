package com.demo.servlet;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.demo.dao.StudentDbUtil;

import com.demo.bean.Student;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private StudentDbUtil studentDbUtil;
    
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
    
    

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		
		//create our student db util ... and pass in the conn pool / datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);			
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		try {
			//read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if command is missing, then default to listing students
			if(theCommand == null) {
				theCommand = "LIST";
			}
			
			//route to appropriate method
			switch (theCommand) {
			
			case "LIST":
				listStudents(request, response);
				break;
			
			case "ADD":
				addStudent(request, response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
				break;
			}
			//1- list the students... in MVC fashion
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student id from form data
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		
		//delete student from db
		studentDbUtil.deleteStudent(studentId);
		
		//send them back to the "list students" page
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read student id from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String fName = request.getParameter("firstName");
		String lName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a Student object
		Student updateStudent = new Student(id, fName, lName, email);
		
		//pass the student to db util - perform update on db
		StudentDbUtil.updateStudent(updateStudent);
		
		//send them back to the "list students" page
		listStudents(request, response);
		
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* this will populate the student data in the update student form at the beginning */
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//get student in the database (db util)
		Student theStudent = studentDbUtil.getstudent(theStudentId);
		
		//place the student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher rd = request.getRequestDispatcher("/update-student-form.jsp");
		rd.forward(request, response);  
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		String fName = request.getParameter("firstName");
		String lName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student object 
		
		Student newStudent = new Student(fName, lName, email);
		
		//add the student to the db
		StudentDbUtil.addStudent(newStudent);
		
		//send back to main page (the student list)
		listStudents(request, response);
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Student> students = studentDbUtil.getStudents();
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher rd = request.getRequestDispatcher("/list-students.jsp");
		rd.forward(request, response);
		
	}


}
