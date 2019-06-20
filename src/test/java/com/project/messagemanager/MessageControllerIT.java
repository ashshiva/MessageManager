package com.project.messagemanager;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.project.messagemanager.controller.MessageController;
import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.MessageNotFoundException;

/**
* @author  Ashwathi SShiva
* 
*/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
		  locations = "classpath:application.properties")
public class MessageControllerIT {

	@Autowired
	private MessageController messageController;
	
	@LocalServerPort
	private int port=8080;
	
	private URL base;
	
	@Autowired
	private TestRestTemplate template;
	
	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:"+port+"/");
	}
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(messageController).isNotNull();

	}

	// GET test
	@Test
	public void getAllMessagesEmptyData() throws Exception {
		ResponseEntity<List<Message>> response = messageController.getAllMessages();
	    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	    List<Message> list = response.getBody();
	    assertThat(list.isEmpty());
	}
	
	// POST tests
	@Test
    public void testAddMessageSuccess() throws URISyntaxException, EmptyMessageException {
		ResponseEntity<Message> response = null;
        Message message = new Message("test");
        try {
			response= messageController.createMessage(message);
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}
         
        //Verify that our request succeeds
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
	
	// POST test and GET test
	@Test
	public void getAllMessagesWithData() throws Exception {
		ResponseEntity<Message> response1 = null;
		ResponseEntity<Message> response2 = null;
		// posting messages, then retrieving them
		// 1st message
		Message message1 = new Message("hello");
		try {
			response1= messageController.createMessage(message1);
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}      
        // Verify that our create request succeeds
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        // 2nd message
        Message message2= new Message("world");
		try {
			response2 = messageController.createMessage(message2);
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}
        
        // Verify that our create request succeeds
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        
        // test GET request
        ResponseEntity<List<Message>> getResponse = messageController.getAllMessages();
        assertEquals(HttpStatus.OK,getResponse.getStatusCode());
	    List<Message> list = getResponse.getBody();
	    assertEquals(2, list.size());
	}

	// POST test with null message
	@Test
    public void testAddNullMessage() throws URISyntaxException, EmptyMessageException {
		ResponseEntity<Message> response = null;
		Message message;
		try {
			message = new Message(null);
			response= messageController.createMessage(message);
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}catch(Exception ex) {
			assertTrue(ex instanceof ConstraintViolationException);
		}
    }
	
	@Test
    public void testAddEmptyMessage() throws URISyntaxException {
		ResponseEntity<Message> response = null;
		Message message = new Message("");
		try {
			response= messageController.createMessage(message);
		} catch (MethodArgumentNotValidException e) {
			e.printStackTrace();
		}catch(Exception ex) {
			assertTrue(ex instanceof ConstraintViolationException);
		}
	}

	// PUT test
	@SuppressWarnings("unchecked")
	@Test
    public void testUpdateMessage() throws URISyntaxException {
		ResponseEntity<Message> response = null;
		Message message = new Message("papap");
		try {
			response= messageController.createMessage(message);
			
			//Verify that our request succeeds
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			Message messageToBeUpdated=response.getBody();
			messageToBeUpdated.setMessage("thisisatest");
			response = (ResponseEntity<Message>) messageController.updateMessage(messageToBeUpdated, response.getBody().getId());
			assertEquals(HttpStatus.OK, response.getStatusCode());
		} catch (MethodArgumentNotValidException e) {
		
		}catch(Exception ex) {
			
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
    public void testUpdateMessageIncorrectId() throws URISyntaxException {
		ResponseEntity<Message> response = null;
		Message message = new Message("error");

			try {
				response= messageController.createMessage(message);
			} catch (MethodArgumentNotValidException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//Verify that our request succeeds
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			Message messageToBeUpdated=response.getBody();
			try {
				messageToBeUpdated.setMessage("thisisatest");
			} catch (EmptyMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				response = (ResponseEntity<Message>) messageController.updateMessage(messageToBeUpdated, -2);
			} catch (MethodArgumentNotValidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception ex) {
				assertTrue(ex instanceof ConstraintViolationException);
				
				// update should have failed
				assertEquals("thisisatest", response.getBody().getMessage());
			}
	}

	// DELETE test
	  @Test
	  public void testDeleteMessage() throws URISyntaxException, MethodArgumentNotValidException {
			ResponseEntity<Message> response = null;
			Message message = new Message("delete");
	
			response= messageController.createMessage(message);
			
			//Verify that our request succeeds
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			response = (ResponseEntity<Message>) messageController.deleteMessage(response.getBody().getId());
			assertEquals(HttpStatus.OK, response.getStatusCode());
		}
	
	  @Test
	  public void testDeleteMessageIncorrectId() throws URISyntaxException, MethodArgumentNotValidException {
		  ResponseEntity<Message> response = null;
			Message message = new Message("delete");
	
			response= messageController.createMessage(message);
			
			//Verify that our request succeeds
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			response = (ResponseEntity<Message>) messageController.deleteMessage(500);
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		}
}
