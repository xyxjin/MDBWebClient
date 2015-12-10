package com.theopentutorials.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.theopentutorials.mdb.to.Employee;

import org.jboss.logging.Logger;

public class ServletMessageProducer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(getClass());
       
    public ServletMessageProducer() {
        super();
    }

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) 
			throws ServletException, IOException {
		log.info("http servlet entry with doGet!!!!");
		final String QUEUE_LOOKUP = "queue/MyQueue";
		final String CONNECTION_FACTORY = "ConnectionFactory";
		
		PrintWriter out = response.getWriter();
		try{
			Context context = new InitialContext();
			QueueConnectionFactory factory = 
				(QueueConnectionFactory)context.lookup(CONNECTION_FACTORY);
			QueueConnection connection = factory.createQueueConnection();
			QueueSession session = 
				connection.createQueueSession(false, 
					QueueSession.AUTO_ACKNOWLEDGE);
			
			Queue queue = (Queue)context.lookup(QUEUE_LOOKUP);
			QueueSender sender = session.createSender(queue);
			
			//1. Sending TextMessage to the Queue 
			TextMessage message = session.createTextMessage();
			message.setText("Hello EJB3 MDB Queue!!!");
			sender.send(message);
			out.println("1. Sent TextMessage to the Queue");
			
			//2. Sending ObjectMessage to the Queue
			ObjectMessage objMsg = session.createObjectMessage();
			
			Employee employee = new Employee();
			employee.setId(2163);
			employee.setName("Kumar");
			employee.setDesignation("CTO");
			employee.setSalary(100000);
			objMsg.setObject(employee);						
			sender.send(objMsg);
			out.println("2. Sent ObjectMessage to the Queue");
			
			session.close();
			connection.close();
			context.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("http servlet entry with doPost!!!!");
		doGet(request, response);
	}
}