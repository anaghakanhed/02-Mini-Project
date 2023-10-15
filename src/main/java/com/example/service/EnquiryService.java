package com.example.service;

import java.util.List;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.entity.StudentEnquriesEntity;

public interface EnquiryService {

	public DashboardResponse getDashboardData(Integer userId);

	public List<String> getCoursesNames();

	public List<String> getEnqStatus();

	public boolean saveEnquiry(EnquiryForm form);

	public List<StudentEnquriesEntity> getEnquiry();

	public List<StudentEnquriesEntity> getFilteredEnq(EnquirySearchCriteria criteria, Integer userId);

}
