package com.project.messagemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.messagemanager.entity.Message;

/**
* @author  Ashwathi SShiva
* 
*/

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

}
