package com.project.messagemanager.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.MessageNotFoundException;
import com.project.messagemanager.service.MessageService;

@AutoConfigureMockMvc
@WebMvcTest(MessageController.class)
@RunWith(SpringRunner.class)
public class MessageControllerTest {

	@Autowired
	private MockMvc mvc;

    @MockBean
    private MessageService service;

	
	@Test
	public void getAllMessagesTest() throws Exception {
		Message message = new Message("alexa");
		 
	    List<Message> allMessages = Arrays.asList(message);
	 
	    when(service.retrieveAllMessages()).thenReturn(allMessages);
	 
	    mvc.perform(get("/api/v1/messages"))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$", hasSize(1)))
	      .andExpect(jsonPath("$[0].message", is(message.getMessage())));
	}

	@Test
	public void getMessageTest() throws Exception {
		Message message = new Message("google");
		message.setId(1);
		
	    when(service.getMessage(1)).thenReturn(message);
		mvc.perform(get("/api/v1/messages/{messageId}", 1))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.id", is(1)))
	      .andExpect(jsonPath("$.message", is("google")));
	}
	
	@Test
	public void getMessageInvalidTest() throws Exception {
	    when(service.getMessage(1)).thenReturn(null);
	    mvc.perform(get("/api/v1/messages/{messageId}", 1))
	            .andExpect(status().isNotFound());
	    
	}
	
	@Test
	public void createMessageSuccessTest() throws Exception {
		String msg = "This world is awesome";
		Message message = new Message(msg);
		message.setId(5);
		when(service.saveMessage(any())).thenReturn(message);
		mvc.perform(post("/api/v1/messages")
				.content(marshalMessage(message))
				.contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(jsonPath("$.id", is(message.getId())))
	      .andExpect(jsonPath("$.message", is(msg)));
	}

	@Test
	public void createMessageFailureTest() throws Exception {
		Message message = new Message(null);
		message.setId(5);
		when(service.saveMessage(any())).thenReturn(message);
		mvc.perform(post("/api/v1/messages")
				.content(marshalMessage(message))
				.contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isBadRequest());
	}
	

	@Test
	public void deleteMessageSuccessTest() throws Exception {
		
		Message message = new Message("google");
		message.setId(1);
		
	    doNothing().when(service).deleteMessage(1);
		mvc.perform(delete("/api/v1/messages/{messageId}", 1)
	      .accept(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk());
	}
	
	@Test
	public void deleteMessageFailureTest() throws Exception {
		
	    doNothing().when(service).deleteMessage(-1);
		mvc.perform(delete("/api/v1/messages/{messageId}", -1)
	      .accept(MediaType.APPLICATION_JSON))
	      .andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateMessageSuccessTest() throws Exception {
		// creating and saving a message
		String msg = "This world is awesome";
		Message message = new Message(msg);
		message.setId(5);
		when(service.saveMessage(any())).thenReturn(message);
		mvc.perform(post("/api/v1/messages")
				.content(marshalMessage(message))
				.contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(jsonPath("$.id", is(message.getId())))
	      .andExpect(jsonPath("$.message", is(msg)));
		
		// updating the message we created above
		String updatedMsg = "Life is great!";
		Message updatedMessage = new Message(updatedMsg);
		updatedMessage.setId(5);
		when(service.updateMessage(any())).thenReturn(updatedMessage);
		mvc.perform(put("/api/v1/messages/{messageId}", updatedMessage.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(marshalMessage(updatedMessage)))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.id", is(updatedMessage.getId())))
	      .andExpect(jsonPath("$.message", is(updatedMsg)));
	}
	
	@Test
	public void updateMessageFailureInvalidIdTest() throws Exception {
		// creating and saving a message
		String msg = "This world is awesome";
		Message message = new Message(msg);
		message.setId(5);
		when(service.saveMessage(any())).thenReturn(message);
		mvc.perform(post("/api/v1/messages")
				.content(marshalMessage(message))
				.contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(jsonPath("$.id", is(message.getId())))
	      .andExpect(jsonPath("$.message", is(msg)));
		
		// updating the message we created above
		String updatedMsg = "Life is great!";
		Message updatedMessage = new Message(updatedMsg);
		updatedMessage.setId(5);
		when(service.updateMessage(any())).thenReturn(updatedMessage);
		mvc.perform(put("/api/v1/messages/{messageId}", 15)
				.contentType(MediaType.APPLICATION_JSON)
				.content(marshalMessage(updatedMessage)))
	      .andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateMessageFailureNullMessageTest() throws Exception {
		// creating and saving a message
		String msg = "This world is awesome";
		Message message = new Message(msg);
		message.setId(5);
		when(service.saveMessage(any())).thenReturn(message);
		mvc.perform(post("/api/v1/messages")
				.content(marshalMessage(message))
				.contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(jsonPath("$.id", is(message.getId())))
	      .andExpect(jsonPath("$.message", is(msg)));
		
		// updating the message we created above
		String updatedMsg = null;
		Message updatedMessage = new Message(updatedMsg);
		updatedMessage.setId(5);
		when(service.updateMessage(any())).thenReturn(updatedMessage);
		mvc.perform(put("/api/v1/messages/{messageId}", 15)
				.contentType(MediaType.APPLICATION_JSON)
				.content(marshalMessage(updatedMessage)))
	      .andExpect(status().isBadRequest());
	}
	
	
	private String marshalMessage(Message msg) {
        try {
            return new ObjectMapper().writeValueAsString(msg);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
