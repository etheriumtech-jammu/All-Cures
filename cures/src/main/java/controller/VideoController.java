package controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.SponsoredAdsDaoImpl;
import dao.VideoDaoImpl;
import model.AvailabilitySchedule;
import model.ServiceContract;
import model.VideoFailure;
@RestController
@RequestMapping(path = "/video")
public class VideoController {

	@RequestMapping(value = "/add/doctor/schedule", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddSchedule(@RequestBody HashMap ScheduleMap,HttpServletRequest request ) throws Exception {

	//	return VideoDaoImpl.InsertSchedule(ScheduleMap);
		return 1;
	}
	
	@RequestMapping(value = "/get/all/schedules", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<AvailabilitySchedule> allschedules(HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getSchedules();
	}
	
	@RequestMapping(value = "update/schedule/{DocID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateSchedule(@PathVariable(name = "DocID") Integer DocID, @RequestBody HashMap ScheduleMap, HttpServletRequest request) {
	
	return VideoDaoImpl.updateSchedule(DocID, ScheduleMap);
		
	}
	
	@RequestMapping(value = "delete/schedule/{DocID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteSchedule(@PathVariable int DocID,  HttpServletRequest request) {
	
	return VideoDaoImpl.deleteSchedule(DocID);
	
	}
	
	@RequestMapping(value = "/get/schedule/{DocID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<AvailabilitySchedule> getschedule(@PathVariable int DocID,HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getSchedule(DocID);
	}

	@RequestMapping(value = "/add/failure/reason", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddFailure(@RequestBody HashMap FailureMap,HttpServletRequest request ) throws Exception {

		return VideoDaoImpl.InsertFailure(FailureMap);
		
	}
	
	@RequestMapping(value = "/get/all/failures/reasons", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<VideoFailure> allfailures(HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getFailures();
	}
	
	@RequestMapping(value = "update/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateFailure(@PathVariable(name = "FailureID") Integer FailureID, @RequestBody HashMap FailureMap, HttpServletRequest request) {
	
	return VideoDaoImpl.updateFailure(FailureID, FailureMap);
		
	}
	
	@RequestMapping(value = "delete/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteFailure(@PathVariable int FailureID,  HttpServletRequest request) {
	
	return VideoDaoImpl.deleteFailure(FailureID);
	
	}
	
	@RequestMapping(value = "/get/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<VideoFailure> getfailure(@PathVariable int FailureID,HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getFailure(FailureID);
	}
	
	
}
