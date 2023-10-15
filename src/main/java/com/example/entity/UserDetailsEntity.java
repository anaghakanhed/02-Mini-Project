package com.example.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ait_user_dt")
public class UserDetailsEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	private String name;
	private String email;
	private Long phno;
	private String pwd;
	private String acc_Status;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<StudentEnquriesEntity> enquiries;
}
