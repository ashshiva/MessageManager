package com.project.messagemanager.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.InvalidIdException;
import com.project.messagemanager.exceptions.MessageNotFoundException;
import com.project.messagemanager.repository.MessageRepository;

/**
* @author  Ashwathi SShiva
* 
*/

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
	public void getMessageSuccessTest() throws Exception, MessageNotFoundException {
		Message message = new Message("alexa");
		Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(1)).thenReturn(opMessage);
	 
	    Message respMessage = messageService.getMessage(1);
	    
	    assertEquals(message.getMessage(), respMessage.getMessage());
	}
    
    @Test(expected = InvalidIdException.class)
	public void getMessageNullIdTest() throws Exception, MessageNotFoundException {
	    messageService.getMessage(null);
	}
    
    @Test(expected = InvalidIdException.class)
   	public void getMessageInvalidIdTest() throws Exception, MessageNotFoundException {
   	    messageService.getMessage(-5);
   	}
    
    @Test
    public void createMessageSuccessTest() throws EmptyMessageException {
    	Message message = new Message("google");
    	
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	
    }
    
    @Test
    public void createMessagePalindromeTest() throws EmptyMessageException {
    	Message message = new Message("google");
    	
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	Message respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertFalse(message.isPalindrome());
    	  	
    	message = new Message("madam");
    	
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertTrue(message.isPalindrome());
    	
    	message = new Message("Never Odd Or Even");
    	
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertTrue(message.isPalindrome());
    	
    	message = new Message("hello world");
    	
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	respMessage = messageService.saveMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    	assertNotNull(message.isPalindrome());
    	assertFalse(message.isPalindrome());
    }
    
    @Test(expected = EmptyMessageException.class)
    public void createMessageNullMessageTest() throws EmptyMessageException {
    	Message message = new Message(null);
    		messageService.saveMessage(message);
    }
    
    @Test
    public void updateMessageSuccessTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("google");
    	message.setId(2);    	
    	message.setMessage("alexa");
    	
    	Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(2)).thenReturn(opMessage);
    	// update the message
    	when(messageRepository.saveAndFlush(message)).thenReturn(message);
    	Message respMessage = messageService.updateMessage(message);
    	
    	assertEquals(message.getMessage(), respMessage.getMessage());
    	assertEquals(message.getId(), respMessage.getId());
    }
    
    @Test(expected = EmptyMessageException.class)
    public void updateMessageNullMessageTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message(null);
    	message.setId(2);
    	
    	// update the message
    		messageService.updateMessage(message);
    }
    
    @Test(expected = InvalidIdException.class)
    public void updateMessageInvalidMessageIdTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("google");
    	
    	messageService.updateMessage(message);
    	
    }
    
    @Test(expected = MessageNotFoundException.class)
    public void updateMessageNonExistentMessageTest() throws EmptyMessageException, MessageNotFoundException {
    	Message message = new Message("hello");
    	message.setId(2);
		when(messageRepository.findById(2)).thenThrow(MessageNotFoundException.class);
		messageService.updateMessage(message);
    }
    
    @Test
    public void deleteMessageSuccessTest() throws MessageNotFoundException {
    	Message message = new Message("google");
    	message.setId(2);
    	
    	Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(2)).thenReturn(opMessage);
    	doNothing().when(messageRepository).deleteById(message.getId());
    	messageService.deleteMessage(2);
    }
    
    @Test(expected = InvalidIdException.class)
    public void deleteMessageInvalidIDMessageTest() throws MessageNotFoundException {
		messageService.deleteMessage(-1);
    }
    
    @Test(expected = InvalidIdException.class)
    public void deleteMessageNullIdTest() throws InvalidIdException, MessageNotFoundException {
    	messageService.deleteMessage(null);
    }
    
    @Test(expected = MessageNotFoundException.class)
    public void deleteMessageNonExistentMessageTest() throws MessageNotFoundException {
		when(messageRepository.findById(2)).thenThrow(MessageNotFoundException.class);
		messageService.deleteMessage(2);
    }

}
