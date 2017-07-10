package net.gongple.gsecu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(value="/admin/h", method=RequestMethod.GET)
	public String highAdmin(Model model) {
		model.addAttribute("msg", "최고");
		return "admin/admin";
	}

	@RequestMapping(value="/admin/m", method=RequestMethod.GET)
	public String middleAdmin(Model model) {
		model.addAttribute("msg", "중간");
		return "admin/admin";
	}

	@RequestMapping(value="/admin/l", method=RequestMethod.GET)
	public String lowAdmin(Model model) {
		model.addAttribute("msg", "실무");
		return "admin/admin";
	}
	
	@RequestMapping(value="/user/in", method=RequestMethod.GET)
	public String in(Model model) {
		model.addAttribute("msg", "IN");
		return "user";
	}
	
	@RequestMapping(value="/user/out", method=RequestMethod.GET)
	public String out(Model model) {
		model.addAttribute("msg", "OUT");
		return "user";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

}