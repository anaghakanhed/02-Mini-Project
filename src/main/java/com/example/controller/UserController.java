package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.binding.LoginForm;
import com.example.binding.SignUpForm;
import com.example.binding.UnlockForm;
import com.example.repo.UserDtlsRepo;
import com.example.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public String handleSignUp(@ModelAttribute("user") SignUpForm form, Model model) {
		boolean status = userService.signUp(form);
		if (status) {
			model.addAttribute("succmsg", "Account Created, Check Your Email");
		} else {
			model.addAttribute("errormsg", "Choose unique email");
		}
		return "signup";
	}

	@GetMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("loginForm") LoginForm loginForm, Model model) {
		String status = userService.login(loginForm);
		if (status.contains("success")) {
			// If login is successfull it will redirect to controller method"
			return "redirect:/dashboard";
		}
		model.addAttribute("errmsg", status);
		return "login";
	}

	@GetMapping("/forgotpwd")
	public String forgotPwdPage() {
		return "forgotPwd";
	}

	@PostMapping("/forgotPwd")
	public String forgotPwd(@RequestParam("email") String email, Model model) {
		System.out.println(email);
		boolean status = userService.forgotPwd(email);
		if (status) {
			model.addAttribute("success", "Pwd send to your email");
		} else {
			model.addAttribute("errmsg", "Invalid Email");
		}
		return "forgotPwd";
	}

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {
		UnlockForm unlockFormObj = new UnlockForm();
		unlockFormObj.setEmail(email);
		model.addAttribute("unlock", unlockFormObj);
		return "unlock";
	}

	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm unlock, Model model) {
		if (unlock.getNewPwd().equals(unlock.getConfirmedPwd())) {
			boolean status = userService.unlockAccount(unlock);
			if (status) {
				model.addAttribute("success", "Your account unlock successfully");
			} else {
				model.addAttribute("errmsg", "Given temporary password is incorrect, check your email");
			}
		} else {
			model.addAttribute("errmsg", "New pwd and Conform pwd should be same");
		}
		return "unlock";
	}
}
