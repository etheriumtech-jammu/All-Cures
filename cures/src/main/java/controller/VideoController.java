package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping(path = "/video")
public class VideoController {
@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	
	public @ResponseBody int getCityDetails() {
		
		return 1;

	}
}
