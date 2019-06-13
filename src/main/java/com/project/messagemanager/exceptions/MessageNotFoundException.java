package com.project.messagemanager.exceptions;

public class MessageNotFoundException extends Exception {
	
	private static final long serialVersionUID = 105896412374152L;
	
	public MessageNotFoundException(String message, Throwable err) {
		super(message, err);
	}
	
	public MessageNotFoundException(String message) {
		super(message);
	}
}
