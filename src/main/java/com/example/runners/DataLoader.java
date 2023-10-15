package com.example.runners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.entity.CourseEntity;
import com.example.entity.EnquriryStatus;
import com.example.repo.CourseRepo;
import com.example.repo.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner{
 
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		courseRepo.deleteAll(); 
		CourseEntity c1= new CourseEntity();
		c1.setCourseName("Java");
		
		CourseEntity c2= new CourseEntity();
		c2.setCourseName("Aws");
		
		CourseEntity c3= new CourseEntity();
		c3.setCourseName("DevOps");
		
		courseRepo.saveAll(Arrays.asList(c1,c2,c3));
		
		
		enqStatusRepo.deleteAll();
		EnquriryStatus e1= new EnquriryStatus();
		e1.setStatusName("New");
		
		EnquriryStatus e2= new EnquriryStatus();
		e2.setStatusName("Enrolled");
		
		EnquriryStatus e3= new EnquriryStatus();
		e3.setStatusName("Lost");
		
		enqStatusRepo.saveAll(Arrays.asList(e1,e2,e3));
	}

}
