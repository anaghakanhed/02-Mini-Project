package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.DashboardResponse;
import com.example.binding.EnquiryForm;
import com.example.binding.EnquirySearchCriteria;
import com.example.constant.AppConstants;
import com.example.entity.StudentEnquriesEntity;
import com.example.repo.StudentEnqRepo;
import com.example.service.EnquiryService;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
public class EnquiryController {

	private static final String Integer = null;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private StudentEnqRepo studentEnqRepo;

	@GetMapping("/logout")
	public String logout() {
		httpSession.invalidate();
		return "index";
	}

	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		Integer userId = (Integer) httpSession.getAttribute(AppConstants.USER_ID);
		DashboardResponse dashboardData = enquiryService.getDashboardData(userId);
		model.addAttribute("dashboardData", dashboardData);
		return "dashboard";
	}

	@GetMapping("/addenquiry")
	public String addEnquiryPage(Model model) {
		init(model);
		return "add-enquiry";
	}

	private void init(Model model) {
		// get course for dropdown
		List<String> coursesNames = enquiryService.getCoursesNames();
		// get enq status for dropdown
		List<String> enqStatus = enquiryService.getEnqStatus();
		// create binding class obj
		EnquiryForm formObj = new EnquiryForm();
		model.addAttribute("courseName", coursesNames);
		model.addAttribute("enqStatus", enqStatus);
		model.addAttribute("formObj", formObj);
	}

	@PostMapping("/addEnq")
	public String addenq(@ModelAttribute("formObj") EnquiryForm formObj, Model model) {
		System.out.println(formObj);
		// save the data
		boolean status = enquiryService.saveEnquiry(formObj);
		if (status) {
			model.addAttribute("success", "Enquiry Added");
		} else {
			model.addAttribute("err", "Problem Occured");
		}
		return "add-enquiry";
	}

	@GetMapping("/viewenquiry")
	public String viewEnquiryPage(Model model) {
		init(model);
		List<StudentEnquriesEntity> enquires = enquiryService.getEnquiry();
		model.addAttribute("enquiries", enquires);
		return "view-enquiry";
	}

	@GetMapping("/filter-enquiries")
	public String getFilteredEnq(@RequestParam String cname, @RequestParam String mode, @RequestParam String status,
			Model model) {
		EnquirySearchCriteria criteria = new EnquirySearchCriteria();
		criteria.setCourseName(cname);
		criteria.setClassMode(mode);
		criteria.setEnqStatus(status);
		System.out.println(criteria);
		Integer userId = (Integer) httpSession.getAttribute(AppConstants.USER_ID);
		List<StudentEnquriesEntity> filteredEnq = enquiryService.getFilteredEnq(criteria, userId);
		model.addAttribute("enquiries", filteredEnq);
		return "filter-enquiries-page";
	}

	@GetMapping("/edit")
	public String editEnq(@RequestParam("enquiryId") Integer enquiryId, Model model) {
		Optional<StudentEnquriesEntity> findById = studentEnqRepo.findById(enquiryId);
		if (findById.isPresent()) {
			StudentEnquriesEntity studentEnqEntity = findById.get();
			// get courses for the drop down
			List<String> courseNames = enquiryService.getCoursesNames();
			// get enq status for drop down
			List<String> enqStatus = enquiryService.getEnqStatus();
			// create binding class object
			EnquiryForm formObj = new EnquiryForm();
			BeanUtils.copyProperties(studentEnqEntity, formObj);
			// set data in model object
			model.addAttribute("courseName", courseNames);
			model.addAttribute("enqStatus", enqStatus);
			model.addAttribute("formObj", formObj);
		}
		return "add-enquiry";
	}
}