package com.wnsProject.Service;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.wnsProject.Entity.Consumer;

import jakarta.servlet.http.HttpServletResponse;

public interface ConsumerService {
	
	public String generateConsumerNo();
	 public Consumer addConsumer(Consumer consumer);
	  public Consumer updateConsumer(Long id, Consumer updatedConsumer);
	  public void deleteConsumer(Long id);
	  public List<Consumer> getAllConsumers();
		public void exportReportAsPdf(HttpServletResponse response) throws DocumentException, IOException;
		public Consumer getConsumerByConsumerNo(String consumerNo);


}
