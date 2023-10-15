package com.example.service;

import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;


public interface UserService {
	public boolean signUp(SignUpForm form);

	public boolean unlockAccount(UnlockForm form);
	
	
	public String login(LoginForm form);
	
	public boolean forgotPwd(String email);
	
}
