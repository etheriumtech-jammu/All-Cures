package controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import dao.SponsoredAdsDaoImpl;
import dao.VideoDaoImpl;
import model.AvailabilitySchedule;
import model.ServiceContract;
import model.VideoFailure;
import service.DailyCoService;
import model.VideoLeads;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
@RestController
@RequestMapping(path = "/video")
public class VideoController {

	 @Autowired
	    private DailyCoService dailyCoService;

	@RequestMapping(value = "/add/doctor/schedule", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddSchedule(@RequestBody HashMap ScheduleMap,HttpServletRequest request ) throws Exception {

		return VideoDaoImpl.InsertSchedule(ScheduleMap);
	//	return 1;
	}
	
	@RequestMapping(value = "/get/all/schedules", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<AvailabilitySchedule> allschedules(HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getSchedules();
	}
	
	@RequestMapping(value = "/update/schedule/{DocID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateSchedule(@PathVariable(name = "DocID") Integer DocID, @RequestBody HashMap ScheduleMap, HttpServletRequest request) {
	
	return VideoDaoImpl.updateSchedule(DocID, ScheduleMap);
		
	}
	
	@RequestMapping(value = "/delete/schedule/{DocID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteSchedule(@PathVariable int DocID,  HttpServletRequest request) {
	
	return VideoDaoImpl.deleteSchedule(DocID);
	
	}
	
	@RequestMapping(value = "/get/schedule/{DocID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<AvailabilitySchedule> getschedule(@PathVariable int DocID,HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getSchedule(DocID);
	}

	@RequestMapping(value = "/get/{DocID}/availability", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody int getavailability(@PathVariable int DocID,HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getAvailability(DocID);
		
	}
	
	@RequestMapping(value = "/add/failure/reason", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer AddFailure(@RequestBody HashMap FailureMap,HttpServletRequest request ) throws Exception {

		return VideoDaoImpl.InsertFailure(FailureMap);
		
	}
	
	@RequestMapping(value = "/get/all/failure/reasons", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<VideoFailure> allfailures(HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getFailures();
	}
	
	@RequestMapping(value = "/update/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.PUT)
	public @ResponseBody int updateFailure(@PathVariable(name = "FailureID") Integer FailureID, @RequestBody HashMap FailureMap, HttpServletRequest request) {
	
	return VideoDaoImpl.updateFailure(FailureID, FailureMap);
		
	}
	
	@RequestMapping(value = "/delete/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.DELETE)
	public @ResponseBody int deleteFailure(@PathVariable int FailureID,  HttpServletRequest request) {
	
	return VideoDaoImpl.deleteFailure(FailureID);
	
	}
	
	@RequestMapping(value = "/get/failure/reason/{FailureID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<VideoFailure> getfailure(@PathVariable int FailureID,HttpServletRequest request) throws Exception {

		return VideoDaoImpl.getFailure(FailureID);
	}

	@RequestMapping(value = "/create/room/{DocID}", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody String CreateRoom(@PathVariable int DocID,HttpServletRequest request) throws Exception {
		String meeting=dailyCoService.createMeeting();
		int res= VideoDaoImpl.sendEmail(DocID,0,meeting,null,null);
		if(res==1) {
			return meeting;
		}
		else {
			return "Error";
		}
    }

	@RequestMapping(value = "/get/doctors/list", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String, Object>> getDoctorsList(HttpServletRequest request,@RequestParam(required=false) Integer userID,@RequestParam(required=false) Integer offset) throws Exception {
    
		return VideoDaoImpl.getDoctorsList(offset);
	}

	@RequestMapping(value = "/get/doctors", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String, Object>> getDoctors(HttpServletRequest request,@RequestParam(required=false) Integer userID,@RequestParam(required=false) Integer offset,@RequestParam(required=false) Integer medTypeID) throws Exception {
    
		return VideoDaoImpl.getDoctors(offset,medTypeID);
	}

	@RequestMapping(value = "/post/leads", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int postLeads(HttpServletRequest request,@RequestParam(required=false) Integer userID,@RequestParam(required=false) Integer docID) throws Exception {

		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Create a UserActivity instance
		VideoLeads userActivity = new VideoLeads(userID,docID, timestamp);
        
        // Save user activity
        return saveUserActivity(userActivity);
		
	}
	private int saveUserActivity(VideoLeads userActivity) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.buildSessionFactory();
		try {
		Transaction tx = session.beginTransaction();
		session.save(userActivity);
		tx.commit();
		return 1;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	
	}

	@RequestMapping(value = "/consult/counts", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int consultCounts(HttpServletRequest request) throws Exception {

		return VideoDaoImpl.incrementCount();
		
	}

}
