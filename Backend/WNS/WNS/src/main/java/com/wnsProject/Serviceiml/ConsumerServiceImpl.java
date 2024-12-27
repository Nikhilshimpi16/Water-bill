package com.wnsProject.Serviceiml;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.wnsProject.Entity.Consumer;
import com.wnsProject.Entity.Employee;
import com.wnsProject.Repository.ConsumerRepository;
import com.wnsProject.Service.ConsumerService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ConsumerServiceImpl implements ConsumerService {
	
	@Autowired
	private ConsumerRepository consumerRepository;
	
	 // automatic ConsumerNo show on Form
	 public String generateConsumerNo() {
	        Optional<Consumer> lastConsumer = consumerRepository.findTopByOrderByIdDesc();
	        if (lastConsumer.isPresent()) {
	            String lastConsumerNo = lastConsumer.get().getConsumerNo();
	            int newNumber = Integer.parseInt(lastConsumerNo.split("-")[1]) + 1;
	            return "C-" + newNumber;
	        }
	        return "C-1"; // Start with C-1 if no consumers exist
	    }

	 public List<Consumer> getAllConsumers() {
	        return consumerRepository.findAll();
	    }
	 
	 
	 public Consumer getConsumerByConsumerNo(String consumerNo) {
	        return consumerRepository.findByConsumerNo(consumerNo)
	                .orElseThrow(() -> new RuntimeException("Consumer not found with ConsumerNo: " + consumerNo));
	    }

	
	    public Consumer addConsumer(Consumer consumer) {
	        // Generate ConsumerNo in a "Sr.No" format
	        consumer.setConsumerNo("C-" + (consumerRepository.count() + 1));
	        return consumerRepository.save(consumer);
	    }

	    public Consumer updateConsumer(Long id, Consumer updatedConsumer) {
	        Optional<Consumer> consumerOpt = consumerRepository.findById(id);
	        if (consumerOpt.isPresent()) {
	            Consumer consumer = consumerOpt.get();
	            consumer.setConsumerName(updatedConsumer.getConsumerName());
	            consumer.setAddressLine1(updatedConsumer.getAddressLine1());
	            consumer.setAddressLine2(updatedConsumer.getAddressLine2());
	            consumer.setMobileNo(updatedConsumer.getMobileNo());
	            consumer.setAge(updatedConsumer.getAge());
	            consumer.setBusinessOrJob(updatedConsumer.getBusinessOrJob());
	            consumer.setDateOfTapConnected(updatedConsumer.getDateOfTapConnected());
	            consumer.setMeterNo(updatedConsumer.getMeterNo());
	            consumer.setWaterCourseName(updatedConsumer.getWaterCourseName());
	            consumer.setEmail(updatedConsumer.getEmail());
	            return consumerRepository.save(consumer);
	        }
	        return null;
	    }

	    public void deleteConsumer(Long id) {
	        consumerRepository.deleteById(id);
	    }
	    
	    public void exportReportAsPdf(HttpServletResponse response) throws DocumentException, IOException {
	        List<Consumer> consumers = consumerRepository.findAll();

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
	        PdfPTable table = new PdfPTable(11);
	        table.addCell("consumerNo");
	        table.addCell("consumerName");
	        table.addCell("addressLine1");
	        table.addCell("addressLine2");
	        table.addCell("mobileNo");
	        table.addCell(" age");
	        table.addCell("businessOrJob");
	        table.addCell("dateOfTapConnected");
	        table.addCell("meterNo");
	        table.addCell("waterCourseName");
	        table.addCell("email");

	        // Add employee data to the table
	        for (Consumer consumer : consumers) {
	            table.addCell(consumer.getConsumerNo());
	            table.addCell(consumer.getConsumerName());
	            table.addCell(consumer.getAddressLine1());
	            table.addCell(consumer.getAddressLine2());
	            table.addCell(consumer.getMobileNo());
	            table.addCell(consumer.getAge());
	            table.addCell(consumer.getBusinessOrJob());
	            table.addCell(consumer.getDateOfTapConnected().toString());
	            table.addCell(consumer.getMeterNo());
	            table.addCell(consumer.getWaterCourseName());
	            table.addCell(consumer.getEmail());

	        }

	        // Add the table to the document
	        document.add(table);

	        // Close the document
	        document.close();
	    }

		

		
}
