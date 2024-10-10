package com.ohgiraffers.chat.repository;

import com.ohgiraffers.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message, Long> {
}