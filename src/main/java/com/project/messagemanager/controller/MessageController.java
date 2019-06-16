package com.project.messagemanager.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.MessageNotFoundException;
import com.project.messagemanager.exceptions.UnknownException;
import com.project.messagemanager.service.MessageService;

@RestController
@Validated
public class MessageController {

	private static final long serialVersionUID = 743656762145678L;
	
	@Autowired
	private MessageService messageService;
	
	private static final Logger logger = LogManager.getLogger(MessageController.class);
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	
	@RequestMapping(
			value = "/api/v1/messages", 
			method=RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<List<Message>> getAllMessages() {
		logger.info("Retrieving all messages...");
		try {
		List<Message> messages = messageService.retrieveAllMessages();
		logger.info("All messages retrieved!");
		return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
		} catch(Exception ex) {
			logger.error("An unknown exception occurred: "+ex);
			throw new UnknownException();
		}
	}
	
	@RequestMapping(
			value = "/api/v1/messages/{messageId}", 
			method=RequestMethod.GET,
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<?> getMessage(
			@PathVariable(name="messageId") @Min(1) Integer messageId) {
		logger.info("Finding message...");
		try {
			Message message = messageService.getMessage(messageId);
			logger.info("Specific message retrieved!");
			if(message == null || message.getId() == null)
				throw new MessageNotFoundException();
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		} catch(MessageNotFoundException ex) {
			logger.error("MessageNotFoundException occurred: "+ex);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
		} catch(Exception ex) {
			logger.error("An unknown exception occurred: "+ex);
			throw new UnknownException();
		}
	}
	
	@RequestMapping(
			value = "/api/v1/messages", 
			method=RequestMethod.POST,
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<Message> createMessage(
			@Valid @RequestBody Message message) 
			throws MethodArgumentNotValidException {
		logger.info("Saving messsage...");
		try {
			Message savedMessage = messageService.saveMessage(message);
			logger.info("Message created!");
			return new ResponseEntity<Message>(savedMessage, HttpStatus.CREATED);
		} catch(EmptyMessageException ex) {
			logger.error("Message is null! "+ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(Exception ex) {
			logger.error("An unknown exception occurred: "+ex);
			throw new UnknownException();
		}
		
	}
	
	@RequestMapping(
			value = "api/v1/messages/{messageId}", 
			method=RequestMethod.PUT,
			consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<?> updateMessage(
			@Valid @RequestBody Message message, 
			@PathVariable(name="messageId") @Min(1) Integer messageId) 
			throws MethodArgumentNotValidException  {
		logger.info("Updating messsage...");
		try {
			if(messageId != message.getId()) {
				logger.error("Incoming messageId is not the same as the messageId from the message to be modified");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			Message updatedMessage = messageService.updateMessage(message);	
			logger.info("Message Updated!");
			return new ResponseEntity<Message>(updatedMessage, HttpStatus.OK);
		} catch(MessageNotFoundException ex) {
			logger.error("Message not found: "+ex);
			return new ResponseEntity<Message>(HttpStatus.NOT_FOUND);		
		} catch(EmptyMessageException ex) {
			logger.error("Message is null/blank: "+ex);
			return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);	
		}catch(Exception ex) {
			logger.error("An unknown exception occurred: "+ex);
			throw new UnknownException();
		}
	}
	
	@RequestMapping(
			value = "api/v1/messages/{messageId}", 
			method=RequestMethod.DELETE
			)
	public ResponseEntity<Message> deleteMessage(
			@PathVariable(name="messageId") @Min(1) Integer messageId) {
		logger.info("Deleting message...");
		try {
			messageService.deleteMessage(messageId);
			logger.info("Message deleted!");
			return new ResponseEntity<Message>(HttpStatus.OK);
		} catch(MessageNotFoundException ex) {
			logger.error("Message not found: "+ex);
			return new ResponseEntity<Message>(HttpStatus.NOT_FOUND);			
		} catch(ConstraintViolationException ex) {
			logger.error("Invalid message id supplied: "+ex);
			return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
		} catch(Exception ex) {
			logger.error("An unknown exception occurred: ",ex);
			throw new UnknownException();
		}	
	}
	
	// TODO: 
	// README  
	
	// tests - DONE
	// build CLI - DONE
	// add annotations to entity, every property.- DONE
	// add everything into git - DONE
	// dockerize app - DONE
	// implement hashcode() serialUID on top of every class - did not implement hashcode(), but did include seriaUID.-  DONE
	// design patterns - DONE
	// handle validations - DONE
	// exception handling - DONE
	// id datatype? make UUID - made Integer, decided not to go with UUID - DONE
	// find out why <Message, Integer> in respository code syntax, how to write it for composite primary keys - DONE
	// return object in create/update/delete flow - DONE
	// palindrome logic - DONE
	// api versioning - /api/v1/  - DONE
	// change 'find' in method names  to 'get' - DONE
	// enable swagger; check latest version. - DONE
	// logger to log messages.. - DONE
	// produces= application/json, consumes=application/json  - DONE
	// fix palindrome logic - DONE
	// try to combine save/update calls in service class - DONE
}
