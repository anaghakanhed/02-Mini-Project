package com.example.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name="ait_student_enquries")
public class StudentEnquriesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enquiryId;
	private String studentName;
	private String studentPhn;
	private String classMode;
	private String courseName;
	private String enqStatus;
	@CreationTimestamp
	private LocalDate createdDate;

	@UpdateTimestamp
	private LocalDate updatedDate;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserDetailsEntity user;
}
