package com.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.StudentEnquriesEntity;
import com.example.entity.UserDetailsEntity;

public interface StudentEnqRepo extends JpaRepository<StudentEnquriesEntity, Integer>{
	
	public List<StudentEnquriesEntity> findByUser(UserDetailsEntity user);
}
