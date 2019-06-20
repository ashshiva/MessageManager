package com.project.messagemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
* @author  Ashwathi Shiva
* 
*/

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Message is not found")
public class MessageNotFoundException extends Exception {
	
	private static final long serialVersionUID = 105896412374152L;
}
