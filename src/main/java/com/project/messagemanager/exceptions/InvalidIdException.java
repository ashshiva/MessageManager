package com.project.messagemanager.exceptions;

/**
* @author  Ashwathi SShiva
* 
*/

public class InvalidIdException extends RuntimeException {
	private static final long serialVersionUID = 624895175328640L;

	public InvalidIdException(String message, Throwable err) {
		super(message, err);
	}
	
	public InvalidIdException(String message) {
		super(message);
	}
}
