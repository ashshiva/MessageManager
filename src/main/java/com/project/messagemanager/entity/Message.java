package com.project.messagemanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@Table(name="MESSAGE")
public class Message {

	private static final long serialVersionUID = 784054212145496L;
	
	private static final Logger logger = LogManager.getLogger(Message.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="MESSAGE", nullable=false, unique=true)
	@NotEmpty(message = "Message cannot be empty and must consist of atleast 1 aplhabet")
	private String message;
	
	@Column(name="ISPALINDROME")
	private Boolean isPalindrome;
	
	public Message(String message, Boolean isPalindrome) {
		super();
		this.message = message;
		this.isPalindrome = checkPalindrome(message);
	}
	
	protected Message() {}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.isPalindrome = checkPalindrome(message);
		this.message = message;
	}
	
	public Boolean isPalindrome() {
		return isPalindrome;
	}
	
	@Override
    public String toString() {
       return "Message[id="+id+", message="+message+", isPalindrome="+isPalindrome+"]";
    }
	
	// check if message is a palindrome
	public boolean checkPalindrome(String message) {
		String reversedMessage = new StringBuffer(message.toLowerCase()).reverse().toString();
		logger.info("Original String: {}, Reversed String: {}", message, reversedMessage);
		return reversedMessage.equals(message.toLowerCase());
	}
}
