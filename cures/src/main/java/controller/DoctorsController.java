package controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.DoctorsDaoImpl_New;

@RestController
@RequestMapping(path = "/doctors")
public class DoctorsController {

	@Autowired
	private DoctorsDaoImpl_New doctorsDaoImpl;

//	@RequestMapping(value = "/verification/{docid}/{verified}/{uprn}/{registration_number}", produces = "application/json", method = RequestMethod.GET)
//	public @ResponseBody int verfiyDoctor(@PathVariable String docid, @PathVariable String verified,
//			@PathVariable String uprn, @PathVariable String registration_number) {
//		return doctorsDaoImpl.verifyDoctor(docid, verified, uprn, registration_number);
//	}

	@RequestMapping(value = "/verification", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int verfiyDoctor(@RequestBody HashMap verficationDatamap) {
		return doctorsDaoImpl.verifyDoctor(verficationDatamap);
	}
	
	@RequestMapping(value = "/updateprofile", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int updateProfile(@RequestBody HashMap profileMap) {
		return doctorsDaoImpl.updateProfile(profileMap);
	}

	@RequestMapping(value = "/{docid}/url", produces = "application/json", method = RequestMethod.GET)
	    public String getNextUrl(@PathVariable Integer docid) {
	        return doctorsDaoImpl.getNextVideoUrl(docid);
	    }

}
