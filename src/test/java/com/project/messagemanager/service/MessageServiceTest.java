package com.project.messagemanager.service;

import static org.junit.Assert.assertEquals;
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
	public void getMessageTest() throws Exception {
		Message message = new Message("alexa");
		Optional<Message> opMessage = Optional.of(message);
	    when(messageRepository.findById(1)).thenReturn(opMessage);
	 
	    Message respMessage = messageService.getMessage(1);
	    
	    assertEquals(message.getMessage(), respMessage.getMessage());
	   	}
    
    
    
    

}
