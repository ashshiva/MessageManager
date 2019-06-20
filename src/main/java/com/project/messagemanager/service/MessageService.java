/**
 * 
 */
package com.project.messagemanager.service;

import java.util.List;

import com.project.messagemanager.entity.Message;
import com.project.messagemanager.exceptions.DuplicateMessageException;
import com.project.messagemanager.exceptions.EmptyMessageException;
import com.project.messagemanager.exceptions.InvalidIdException;
import com.project.messagemanager.exceptions.MessageNotFoundException;

/**
* @author  Ashwathi SShiva
* 
*/

public interface MessageService {

	public List<Message> retrieveAllMessages()throws Exception;
	
	public Message getMessage(Integer id) throws MessageNotFoundException;
	
	public Message saveMessage(Message message) throws EmptyMessageException, DuplicateMessageException;
	
	public Message updateMessage(Message message) throws EmptyMessageException, DuplicateMessageException, MessageNotFoundException;
	
	public void deleteMessage(Integer id) throws MessageNotFoundException, InvalidIdException ;
}
