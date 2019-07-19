<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Student Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>EducaTor University</h2>
		</div>
	</div>


	<div id="container">
		<div id="content">
		
		<!-- put new button: Add Student -->
		<input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp'; return false;"
			   class="add-student-button"
		/>
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>				
				</tr>
				<c:forEach var="student" items="${STUDENT_LIST}">
					
					<!-- setup a link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"></c:param>
						<c:param name="studentId" value="${student.id}"></c:param>
					</c:url>
					
					<!-- setup a link for each student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"></c:param>
						<c:param name="studentId" value="${student.id}"></c:param>
					</c:url>
					
					<tr>
						<td>${student.firstName}</td>
						<td>${student.lastName}</td>
						<td>${student.email}</td>
						<td>
							<a href="${tempLink}">Update</a>
							 |
							<a href="${deleteLink}"
							onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a>
						</td>
					</tr>
				</c:forEach>			
			
			</table>
		</div>
	</div>
</body>
</html>