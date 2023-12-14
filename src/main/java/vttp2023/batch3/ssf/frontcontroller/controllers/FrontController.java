package vttp2023.batch3.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import vttp2023.batch3.ssf.frontcontroller.models.Captcha;
import vttp2023.batch3.ssf.frontcontroller.models.User;
import vttp2023.batch3.ssf.frontcontroller.services.AuthenticationService;

@Log
@Controller
@RequestMapping
public class FrontController {
	@Autowired
	private AuthenticationService authSvc;

	// TODO: Task 2, Task 3, Task 4, Task 6
	@GetMapping
	public String getHome(Model model) {
		model.addAttribute("user", new User());
		return "view0";
	}

	@PostMapping("/login")
	public String postLogin(@Valid @ModelAttribute User user,
		BindingResult result,
		@RequestBody MultiValueMap<String, String> request,
		Model model,
		HttpSession session) {

		try {
			if (authSvc.isLocked(user.getUsername())) {
				return "redirect:/locked-out?username=%s".formatted(user.getUsername());
			}
			String captchaInput = request.getFirst("captchaInput");
			Object retrievedCaptcha = session.getAttribute("captcha");
			if (retrievedCaptcha != null && 
				captchaInput != null) {
					System.out.println("dadada");
				if (!(((Captcha) retrievedCaptcha).validateCaptcha(Integer.parseInt(captchaInput)))) {
					result.addError(new ObjectError("captchaError", "Invalid Captcha value"));
					Captcha captcha = new Captcha();
					model.addAttribute("captcha", captcha);
					session.setAttribute("captcha", captcha);
				}
			}

			if (result.hasErrors()) {
				return "view0";
			}

			authSvc.authenticate(user.getUsername(), user.getPassword());
			return "view1";
		} catch (Exception e) {
			e.printStackTrace();
			log.warning(e.getMessage());
			authSvc.logFailedLoginAttempt(user.getUsername());
			if (authSvc.findFailedLoginAttempts(user.getUsername()) >= 1) {
				Captcha captcha = new Captcha();
				model.addAttribute("captcha", captcha);
				session.setAttribute("captcha", captcha);
			}
			return "view0";
		}
	}

	@GetMapping("/locked-out")
	public String getLockedOut(@RequestParam String username, Model model) {
		if (!authSvc.isLocked(username)) {
			return "redirect:/";
		}
		model.addAttribute("username", username);
		return "view2";
	}
	
}
