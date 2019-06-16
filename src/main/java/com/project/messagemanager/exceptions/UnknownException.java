package com.project.messagemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was an error processing the request. Please try again later.")
public class UnknownException extends RuntimeException {
   
	private static final long serialVersionUID = 968775645364758L;

}