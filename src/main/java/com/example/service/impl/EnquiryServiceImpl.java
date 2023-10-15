package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.constant.AppConstants;
import com.example.entity.CourseEntity;
import com.example.entity.EnquriryStatus;
import com.example.entity.StudentEnquriesEntity;
import com.example.entity.UserDetailsEntity;
import com.example.repo.CourseRepo;
import com.example.repo.EnqStatusRepo;
import com.example.repo.StudentEnqRepo;
import com.example.repo.UserDtlsRepo;
import com.example.service.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private CourseRepo courseRepo;

	@Autowired
	private EnqStatusRepo statusRepo;

	@Autowired
	private EnqStatusRepo enqStatusRepo;

	@Autowired
	private StudentEnqRepo studentEnqRepo;

	@Autowired
	private HttpSession httpSession;

	@Override
	public DashboardResponse getDashboardData(Integer userId) {

		DashboardResponse response = new DashboardResponse();

		Optional<UserDetailsEntity> findById = userDtlsRepo.findById(userId);
		if (findById.isPresent()) {
			UserDetailsEntity userEntity = findById.get();
			List<StudentEnquriesEntity> enquiries = userEntity.getEnquiries();

			Integer totalCnt = enquiries.size();

			Integer enrolledCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();

			Integer lostCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("Lost"))
					.collect(Collectors.toList()).size();

			response.setTotalEnquries(totalCnt);
			response.setEnrolledCnt(enrolledCnt);
			response.setLostCnt(lostCnt);
		}
		return response;
	}

	@Override
	public List<String> getCoursesNames() {
		List<CourseEntity> findAll = courseRepo.findAll();
		List<String> names = new ArrayList<>();
		for (CourseEntity entity : findAll) {
			names.add(entity.getCourseName());

		}
		return names;
	}

	@Override
	public List<String> getEnqStatus() {
		List<EnquriryStatus> findAll = statusRepo.findAll();
		List<String> statusList = new ArrayList<>();
		for (EnquriryStatus entity : findAll) {
			statusList.add(entity.getStatusName());
		}
		return statusList;
	}

	@Override
	public boolean saveEnquiry(EnquiryForm form) {

		StudentEnquriesEntity enqEntity = new StudentEnquriesEntity();
		BeanUtils.copyProperties(form, enqEntity);
		Integer userId = (Integer) httpSession.getAttribute(AppConstants.USER_ID);
		UserDetailsEntity userDetailsEntity = userDtlsRepo.findById(userId).get();
		enqEntity.setUser(userDetailsEntity);
		studentEnqRepo.save(enqEntity);
		return true;
	}

	@Override
	public List<StudentEnquriesEntity> getEnquiry() {
		Integer userId = (Integer) httpSession.getAttribute(AppConstants.USER_ID);

		Optional<UserDetailsEntity> findById = userDtlsRepo.findById(userId);

		UserDetailsEntity userDetailsEntity = findById.get();

		List<StudentEnquriesEntity> enquiries = userDetailsEntity.getEnquiries();

		return enquiries;

	}

	@Override
	public List<StudentEnquriesEntity> getFilteredEnq(EnquirySearchCriteria criteria, Integer userId) {
		Optional<UserDetailsEntity> findById = userDtlsRepo.findById(userId);
		if (findById.isPresent()) {

			UserDetailsEntity userDtlsEntity = findById.get();

			List<StudentEnquriesEntity> enquiries = userDtlsEntity.getEnquiries();

			// filter Logic

			if (null != criteria.getCourseName() & !"".equals(criteria.getCourseName())) {
				enquiries = enquiries.stream().filter(e -> e.getCourseName().equals(criteria.getCourseName()))
						.collect(Collectors.toList());
			}

			if (null != criteria.getEnqStatus() & !"".equals(criteria.getEnqStatus())) {
				enquiries = enquiries.stream().filter(e -> e.getEnqStatus().equals(criteria.getEnqStatus()))
						.collect(Collectors.toList());
			}

			if (null != criteria.getClassMode() & !"".equals(criteria.getClassMode())) {
				enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());
			}

			return enquiries;
		}
		return null;
	}

}