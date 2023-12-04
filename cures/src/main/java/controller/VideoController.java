package controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.VideoDaoImpl;

@RestController
@RequestMapping(path = "/video")
public class VideoController {

	@RequestMapping(value = "/doctor/availabilty", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddSchedule(@RequestBody HashMap ScheduleMap,HttpServletRequest request ) throws Exception {
return 1;
//		return VideoDaoImpl.InsertSchedule(ScheduleMap);
	}
	
}
