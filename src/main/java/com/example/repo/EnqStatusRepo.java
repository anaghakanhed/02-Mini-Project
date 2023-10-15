package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.EnquriryStatus;

public interface EnqStatusRepo extends JpaRepository<EnquriryStatus, Integer> {

	
}
