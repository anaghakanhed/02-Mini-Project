package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class CourseEntity {
	@Id
	@GeneratedValue
	private Integer courseId;
	private String courseName;
}
