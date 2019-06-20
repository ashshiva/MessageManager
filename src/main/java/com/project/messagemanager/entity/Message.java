package com.project.messagemanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import com.project.messagemanager.exceptions.EmptyMessageException;


/**
* @author  Ashwathi SShiva
* 
*/

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
	
	@CreationTimestamp
	@Column(name="CREATEDATE", updatable=false)
	private Date createdAt;
	
	@UpdateTimestamp
	@Column(name="MODIFIEDDATE")
	private Date updatedAt;
	
	public Message(String message) {
		super();
		this.message = message;
		this.isPalindrome = checkPalindrome(message);
	}
	
	protected Message() {}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) throws EmptyMessageException{
		this.isPalindrome = checkPalindrome(message);
		this.message = message;
	}
	
	public Boolean isPalindrome() {
		return isPalindrome;
	}
	
	@Override
    public String toString() {
       return "Message[Id="+id+", Message="+message+", isPalindrome="+isPalindrome+", CreatedAt="+createdAt+", UpdatedAt="+updatedAt+"]";
    }
	
	// check if message is a palindrome
	private boolean checkPalindrome(String message) {
		boolean isPalindrome = false;
		
		if(message != null && !message.trim().isEmpty()) {
			String reversedMessage = null;
			String origMessage = null;
			if(StringUtils.containsWhitespace(message)) {
				logger.info("\""+message+"\""+ " Contains whitespace");				
				// strip all whitespaces, compute reversed message and then compare with original message 
				// after stripping original message of whitespaces as well
				origMessage = StringUtils.trimAllWhitespace(message).toLowerCase();
			}
			else {
				logger.info("\""+message+"\""+ " does not contain whitespace");
				origMessage = message.toLowerCase();
			}
			reversedMessage = new StringBuffer(origMessage).reverse().toString();
			logger.info("Original String: {}, Reversed String: {}", message, reversedMessage);
			isPalindrome = reversedMessage.equals(origMessage);
		}
		// if message is null, return false anyway
		return isPalindrome;
	}
	
}
