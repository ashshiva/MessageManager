package com.project.messagemanager;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.project.messagemanager.controller.MessageController;
import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.DuplicateMessageException;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.InvalidIdException;
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
	
	@Before
	public void setUp() throws Exception {
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
    public void testAddMessageSuccess() throws URISyntaxException, EmptyMessageException, MethodArgumentNotValidException {
		ResponseEntity<Message> response = null;
        Message message = new Message("test");
		response= messageController.createMessage(message);
         
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
		response1= messageController.createMessage(message1);
        // Verify that our create request succeeds
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        // 2nd message
        Message message2= new Message("world");
		response2 = messageController.createMessage(message2);
        
        // Verify that our create request succeeds
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        
        // test GET request
        ResponseEntity<List<Message>> getResponse = messageController.getAllMessages();
        assertEquals(HttpStatus.OK,getResponse.getStatusCode());
	    List<Message> list = getResponse.getBody();
	    assertEquals(2, list.size());
	}

	// POST test with null message
	@Test(expected = ConstraintViolationException.class)
    public void testAddNullMessage() throws URISyntaxException, EmptyMessageException, MethodArgumentNotValidException {
		Message message = new Message(null);
		messageController.createMessage(message);
    }
	
	@Test(expected = ConstraintViolationException.class)
    public void testAddEmptyMessage() throws URISyntaxException, MethodArgumentNotValidException {
		Message message = new Message("");
		messageController.createMessage(message);
	}

	// PUT test
	@SuppressWarnings("unchecked")
	@Test
    public void testUpdateMessage() throws URISyntaxException, MethodArgumentNotValidException {
		Message message = new Message("papap");
		ResponseEntity<Message> response= messageController.createMessage(message);
		
		//Verify that our request succeeds
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Message messageToBeUpdated=response.getBody();
		messageToBeUpdated.setMessage("thisisatest");
		response = (ResponseEntity<Message>) messageController.updateMessage(messageToBeUpdated, response.getBody().getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = ConstraintViolationException.class)
    public void testUpdateMessageIncorrectId() throws URISyntaxException, MethodArgumentNotValidException {
		Message message = new Message("error");
		ResponseEntity<Message> response = messageController.createMessage(message);
			
		//Verify that our request succeeds
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		Message messageToBeUpdated = response.getBody();
		messageToBeUpdated.setMessage("thisisatest");
		response = (ResponseEntity<Message>) messageController.updateMessage(messageToBeUpdated, -2);
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
	
	  @Test(expected = MessageNotFoundException.class)
	  public void testDeleteMessageIncorrectId() throws URISyntaxException, MethodArgumentNotValidException {
		  ResponseEntity<Message> response = null;
			Message message = new Message("delete");
	
			response= messageController.createMessage(message);
			
			//Verify that our request succeeds
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			
			response = (ResponseEntity<Message>) messageController.deleteMessage(500);
		}
}
