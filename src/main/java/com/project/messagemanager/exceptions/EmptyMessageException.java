package com.project.messagemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No message supplied!")
public class EmptyMessageException extends Exception{
	private static final long serialVersionUID = 597856383928495L;
}
