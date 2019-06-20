package com.project.messagemanager.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.InvalidIdException;
import com.project.messagemanager.exceptions.MessageNotFoundException;
import com.project.messagemanager.exceptions.UnknownException;
import com.project.messagemanager.repository.MessageRepository;

/**
* @author  Ashwathi SShiva
* 
*/

@Service
public class MessageServiceImpl implements MessageService {

	private static final long serialVersionUID = 307548962231458L;
	
	@Autowired
	private MessageRepository messageRepository;
	
	private static final Logger logger = LogManager.getLogger(MessageServiceImpl.class);
	
	// Retrieve all messages
	@Override
	public List<Message> retrieveAllMessages() throws Exception {
		List<Message> messages = null;
		logger.info("retrieving messages..");
		try {
			messages = messageRepository.findAll();
			logger.info("Retrieved messages!");
		} catch(Exception ex) {
			logger.error("An unknown exception occurred: "+ex);
			throw new UnknownException();
		}
		return messages;
	}

	// Retrieve a specific message
	@Override
	public Message getMessage(Integer id) throws MessageNotFoundException {
		logger.info("Finding message..");
		Message message = null;
		try {
			this.validateId(id);
			Optional<Message> oMessage = messageRepository.findById(id);
			if(oMessage!=null) {
				message = oMessage.get();
				logger.info("Found message!");
			}
		} catch(NoSuchElementException ex) {
			logger.error("Message Not found! "+ex);
			throw new MessageNotFoundException();
		}
		return message;
	}

	// Create a message
	@Override
	public Message saveMessage(Message message) throws EmptyMessageException {
		logger.info("Creating message...");
		
		if(message == null || message.getMessage() == null || message.getMessage().trim().isEmpty()) {
			logger.error("Message is null");
			throw new EmptyMessageException();
		}
		
		Message createdMessage = messageRepository.saveAndFlush(message);	
		logger.info("Created message: "+createdMessage);
		return createdMessage;
	}

	// Update a message
	@Override
	public Message updateMessage(Message updatedMessage) throws MessageNotFoundException, EmptyMessageException {
		logger.info("Updating message...");
		
		// just for validation
		if(updatedMessage.getMessage() == null || updatedMessage.getMessage().isEmpty()) {
			logger.error("Message is null");
			throw new EmptyMessageException();
		}
		this.getMessage(updatedMessage.getId()); // checks if the message exists
		
		updatedMessage = messageRepository.saveAndFlush(updatedMessage);
		logger.info("Updated message: "+updatedMessage);
		return updatedMessage;
	}

	// Delete a message
	@Override
	public void deleteMessage(Integer id) throws MessageNotFoundException, InvalidIdException {
		logger.info("Deleting message...");
		// just for validation
		this.getMessage(id);
		
		messageRepository.deleteById(id);
		logger.info("Deleted message!");
	}
	
	// Validate the Id of a message
	public void validateId(Integer id) throws InvalidIdException {
		if(id == null || id <= 0) { 
			logger.error("Message id is either null or negative");
			throw new InvalidIdException("Message Id is invalid or null");
		}
	}

}
