package controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/error")
public class ErrorController {

//	@RequestMapping(value = "/error404", method = RequestMethod.GET)
//	public @ResponseBody ModelAndView error404() {
//		System.out.println("in get Errocontroller>>>>>>");
//		String projectUrl = "/ui/index.html";
//		return new ModelAndView("redirect:" + projectUrl);
//	}

	@RequestMapping(value = "/error404", method = RequestMethod.GET)
	public HttpServletResponse error404(HttpServletResponse httpServletResponse) throws IOException {
		System.out.println("in get Errocontroller>>>>>>");
		String projectUrl = "/cures/";
		httpServletResponse.setHeader("Location", projectUrl);
		httpServletResponse.setStatus(302);
		// Redirecting to a Different URL
		//httpServletResponse.sendRedirect("http://192.168.29.160:8080/ui/index.html");
		httpServletResponse.sendRedirect(projectUrl);
		return httpServletResponse;
	}

	@RequestMapping(value = "/error404", method = RequestMethod.POST)
	public @ResponseBody ModelAndView error404Post() {
		System.out.println("in post Errocontroller>>>>>>");
		String projectUrl = "/cures/error404.html";
		return new ModelAndView("redirect:" + projectUrl);

	}

}