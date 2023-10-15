package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.CourseEntity;

public interface CourseRepo extends JpaRepository<CourseEntity, Integer> {

}
