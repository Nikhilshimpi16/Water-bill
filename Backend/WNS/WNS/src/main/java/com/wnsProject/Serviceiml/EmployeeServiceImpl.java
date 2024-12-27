package com.wnsProject.Serviceiml;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wnsProject.Entity.Employee;
import com.wnsProject.Entity.UserRights;
import com.wnsProject.Repository.EmployeeRepository;
import com.wnsProject.Repository.UserRightsRepository;
import com.wnsProject.Service.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
	private UserRightsRepository userRightsRepository;
	
	@Autowired
	private JavaMailSender mailSender;

	   public List<Employee> getAllEmployees() {
	        return employeeRepository.findAll();
	    }

	    public Employee getEmployeeById(Long id) {
	        Optional<Employee> employee = employeeRepository.findById(id);
	        return employee.orElse(null);
	    }

	    public Employee saveEmployee(MultipartFile photo, String employeeJson) throws IOException {
	        ObjectMapper objectMapper = new ObjectMapper();
	        Employee employee = objectMapper.readValue(employeeJson, Employee.class);

	        // Generate username and password
	        String username = employee.getEmpCode();
	        String password = UUID.randomUUID().toString().substring(0, 8);

	        // Save photo as bytes
	        employee.setPhotoUrl(photo.getBytes());
	        employee.setPassword(password);
	        employeeRepository.save(employee);

	        // Send email with username and password
	        sendEmail(employee.getEmail(), username, password);

	        return employee;
	    }
	    
	    private void sendEmail (String Email , String username , String password)
	    {
	    	SimpleMailMessage message = new SimpleMailMessage();
	    	message.setTo(Email);
	    	message.setSubject("Your Employee Account Details");
	        message.setText("Welcome to the company!\n\nYour account details are:\nUsername: " + username + "\nPassword: " + password);
	        
	        mailSender.send(message);
	    }
	    
	    
	    public Employee updateEmployee(Long id, Employee employeeDetails) {
	        Employee employee = getEmployeeById(id);
	        if (employee != null) {
	            employee.setEmpCode(employeeDetails.getEmpCode());
	            employee.setEmpName(employeeDetails.getEmpName());
	            employee.setDepartment(employeeDetails.getDepartment());
	            employee.setDesignation(employeeDetails.getDesignation());
	            employee.setGrade(employeeDetails.getGrade());
	            employee.setJoiningDate(employeeDetails.getJoiningDate());
	            employee.setOfficeDate(employeeDetails.getOfficeDate());
	            employee.setGender(employeeDetails.getGender());
	            employee.setPhotoUrl(employeeDetails.getPhotoUrl());
	            return employeeRepository.save(employee);
	        }
	        return null;
	    }

	    @Override
	    public boolean validateEmployee(String empCode, String password) {
	        return employeeRepository.findByEmpCodeAndPassword(empCode, password).isPresent();
	    }
	    
	    // also this get the On Dashbaord
	    @Override
	    public List<UserRights> getEmployeeRights(String empCode) {
	        return userRightsRepository.findByEmpCode(empCode);
	    }
//	    public List<String> getRightsByUsername(String empName) {
//	        return employeeRepository.findRightsByEmpName(empName);
//	    }
	    
	    public void deleteEmployee(Long id) {
	        employeeRepository.deleteById(id);
	    }
	    
	    //download report 
	    public void exportReportAsPdf(HttpServletResponse response) throws DocumentException, IOException {
	        List<Employee> employees = employeeRepository.findAll();

	        // Set the response headers to indicate it's a PDF file
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=\"employee_report.pdf\"");

	        // Creating the document
	        Document document = new Document();
	        PdfWriter.getInstance(document, response.getOutputStream());

	        // Open the document to start writing
	        document.open();

	        // Title for the report
	        document.add(new Paragraph("Employee Report"));

	        // Create a table with 9 columns (EmpCode, EmpName, Department, Designation, Grade, JoiningDate, OfficeDate, Gender, Category)
	        PdfPTable table = new PdfPTable(9);
	        table.addCell("EmpCode");
	        table.addCell("EmpName");
	        table.addCell("Department");
	        table.addCell("Designation");
	        table.addCell("Grade");
	        table.addCell("Joining Date");
	        table.addCell("Office Date");
	        table.addCell("Gender");
	        table.addCell("Category");

	        // Add employee data to the table
	        for (Employee employee : employees) {
	            table.addCell(employee.getEmpCode());
	            table.addCell(employee.getEmpName());
	            table.addCell(employee.getDepartment());
	            table.addCell(employee.getDesignation());
	            table.addCell(employee.getGrade());
	            table.addCell(employee.getJoiningDate().toString());
	            table.addCell(employee.getOfficeDate().toString());
	            table.addCell(employee.getGender());
	            table.addCell(employee.getCategory());
	        }

	        // Add the table to the document
	        document.add(table);

	        // Close the document
	        document.close();
	    }
	    
	    // User Rigths Section
	    
//	    public Map<String, Object> login(String username, String password) {
//	        // Perform authentication logic
//	        Employee employee = employeeRepository.findByUsernameAndPassword(username, password)
//	                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//	        // Fetch assigned forms
//	        List<String> assignedForms = userRightsService.getAssignedForms(username);
//
//	        // Return response
//	        Map<String, Object> response = new HashMap<>();
//	        response.put("employee", employee);
//	        response.put("forms", assignedForms);
//	        return response;
//	    }
	}




