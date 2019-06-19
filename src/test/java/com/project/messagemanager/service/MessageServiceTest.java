package com.project.messagemanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.InvalidIdException;
import com.project.messagemanager.exceptions.MessageNotFoundException;
import com.project.messagemanager.repository.MessageRepository;

@RunWith(SpringRunner.class)
public class MessageServiceTest {
		
	@InjectMocks
	private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
	public void retrieveAllMessagesTest() throws Exception {
		Message message = new Message("alexa");
		 
	    List<Message> allMessages = Arrays.asList(message);
	 
	    when(messageRepository.findAll()).thenReturn(allMessages);
	 
	    List<Message> listMessages= messageService.retrieveAllMessages();
	    
	    assertEquals(1,listMessages.size());
	    
	}
    
    @Test
	public void getMessageSuccessTest() throws Exception {
		Message message = new Message("alexa");
		Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(1)).thenReturn(opMessage);
	 
	    Message respMessage = messageService.getMessage(1);
	    
	    assertEquals(message.getMessage(), respMessage.getMessage());
	}
    
    @Test
	public void getMessageNullIdTest() throws Exception {
		Message message = new Message("alexa");
		Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(1)).thenReturn(opMessage);
	    try {
	    	Message respMessage = messageService.getMessage(null);
	    } catch(Exception ex) {
	    	assertTrue(ex instanceof InvalidIdException);
	    }
	}
    
    @Test
   	public void getMessageInvalidIdTest() throws Exception {
   		Message message = new Message("alexa");
   		Optional<Message> opMessage = Optional.of(message);
   	    when(messageRepository.findById(1)).thenReturn(opMessage);
   	    try {
   	    	Message respMessage = messageService.getMessage(-5);
   	    } catch(Exception ex) {
   	    	assertTrue(ex instanceof InvalidIdException);
   	    }
   	}
    
    @Test
    public void createMessageSuccessTest() throws EmptyMessageException {
    	Message message = new Message("google");
    	
    	when(messageRepository.save(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	
    }
    
    @Test
    public void createMessagePalindromeTest() throws EmptyMessageException {
    	Message message = new Message("google");
    	
    	when(messageRepository.save(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertFalse(message.isPalindrome());
    	
    	message = new Message("Never Odd Or Even");
    	
    	when(messageRepository.save(message)).thenReturn(message);
    	respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertTrue(message.isPalindrome());
    }
    
    @Test
    public void createMessageNullMessageTest() throws EmptyMessageException {
    	Message message = new Message(null);
    	
    	when(messageRepository.save(message)).thenReturn(message);
    	try {
    		Message respMessage = messageService.saveMessage(message);
    	}catch(Exception ex) {
    	assertTrue(ex instanceof EmptyMessageException);	
    	}
    }
    
    @Test
    public void updateMessageSuccessTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	// create a message
    	when(messageRepository.save(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	
    	message.setMessage("alexa");
    	Message updatedMessage = message;
    	
    	// update the message
    	when(messageRepository.save(message)).thenReturn(message);
    	respMessage = messageService.updateMessage(updatedMessage);
    	
    	assertEquals(updatedMessage.getMessage(), respMessage.getMessage());
    	assertEquals(updatedMessage.getId(), respMessage.getId());
    }
    
    @Test
    public void updateMessageNullMessageTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	// create a message
    	when(messageRepository.save(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	
    	message.setMessage(null);
    	Message updatedMessage = message;
    	
    	// update the message
    	when(messageRepository.save(message)).thenReturn(message);
    	try {
    	respMessage = messageService.updateMessage(updatedMessage);
    	} catch(Exception ex) {
    		assertTrue(ex instanceof EmptyMessageException);
    	}	
    }
    
    @Test
    public void updateMessageInvalidMessageIdTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	// create a message
    	when(messageRepository.save(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	
    	message.setMessage("google");
    	message.setId(5);
    	Message updatedMessage = message;
    	
    	// update the message
    	when(messageRepository.save(message)).thenReturn(message);
    	try {
    	respMessage = messageService.updateMessage(updatedMessage);
    	} catch(Exception ex) {
    		assertTrue(ex instanceof InvalidIdException);
    	}
    	
    }
    
    @Test
    public void deleteMessageSuccessTest() throws Exception {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(2)).thenReturn(opMessage);
    	doNothing().when(messageRepository).deleteById(message.getId());
    	messageService.deleteMessage(2);
    }
    
    @Test
    public void deleteMessageNonExistentMessageTest() throws Exception {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(2)).thenReturn(opMessage);
    	doNothing().when(messageRepository).deleteById(message.getId());
    	try {
    	messageService.deleteMessage(5);
    	} catch(Exception ex) {
    		assertTrue(ex instanceof MessageNotFoundException);
    	}
    }
    
    @Test
    public void deleteMessageNullIdTest() throws Exception {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(2)).thenReturn(opMessage);
    	doNothing().when(messageRepository).deleteById(message.getId());
    	try {
    	messageService.deleteMessage(null);
    	} catch(Exception ex) {
    		assertTrue(ex instanceof InvalidIdException);
    	}
    }

}
