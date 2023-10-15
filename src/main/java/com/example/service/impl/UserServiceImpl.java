package com.example.service.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;
import com.example.constant.AppConstants;
import com.example.entity.UserDetailsEntity;
import com.example.repo.UserDtlsRepo;
import com.example.service.UserService;
import com.example.util.EmailUtils;
import com.example.util.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDtlsRepo userDtlsRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private HttpSession httpSession;

	@Override
	public boolean signUp(SignUpForm form) {

		UserDetailsEntity user = userDtlsRepo.findByEmail(form.getEmail());

		if (user != null) {
			return false;
		}
		// TODO: copy data from binding object to entity object

		UserDetailsEntity entity = new UserDetailsEntity();
		BeanUtils.copyProperties(form, entity);

		// TODO: Generate random password and set to object
		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);

		// TODO: set account status as Locked
		entity.setAcc_Status(AppConstants.STR_LOCKED);

		// TODO: insert record
		userDtlsRepo.save(entity);

		// TODO: send email to user to unlock the account
		String to = form.getEmail();
		String subject = AppConstants.UNLOCK_YOUR_ACCOUNT;
		StringBuffer body = new StringBuffer("");
		body.append("<h1>Used below temporary password to unlock your account</h1>");
		body.append("Temporary Password" + tempPwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:9090/unlock?email=" + to + "\">Click hear to unlock your account</a>");
		emailUtils.sendEmail(subject, body.toString(), to);
		return true;
	}

	@Override
	public boolean unlockAccount(UnlockForm form) {
		UserDetailsEntity entity = userDtlsRepo.findByEmail(form.getEmail());
		if (entity.getPwd().equals(form.getTempPwd())) {
			entity.setPwd(form.getNewPwd());
			entity.setAcc_Status(AppConstants.STR_UNLOCKED);
			userDtlsRepo.save(entity);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String login(LoginForm form) {
		UserDetailsEntity entity = userDtlsRepo.findByEmailAndPwd(form.getEmail(), form.getPwd());
		if (entity == null) {
			return AppConstants.INVALID_CREDENTIAL;
		}

		if (entity.getAcc_Status().equals(AppConstants.STR_LOCKED)) {
			return AppConstants.ACC_LOCKED_MSG;
		}

		httpSession.setAttribute(AppConstants.USER_ID, entity.getUserId());

		return "success";
	}

	@Override
	public boolean forgotPwd(String email) {

		// Check record presence in db with given email
		UserDetailsEntity entity = userDtlsRepo.findByEmail(email);

		// If record not available send error msg or return false
		if (entity == null) {
			return false;
		}
		// If record available send pwd to email and send success msg or return true
		String subject = AppConstants.RECOVER_PASSWORD;
		String body = "Your Pwd :: " + entity.getPwd();
		emailUtils.sendEmail(email, subject, body);
		return true;
	}

}
