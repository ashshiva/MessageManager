package com.project.messagemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
* @author  Ashwathi Shiva
* 
*/

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Message is not unique")
public class DuplicateMessageException extends RuntimeException {
	
	private static final long serialVersionUID = 186493815220502L;
}
