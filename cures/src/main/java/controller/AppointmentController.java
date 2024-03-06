package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.AppointmentDaoImpl;
import model.Appointment;

@RestController
@RequestMapping(path = "/appointments")
public class AppointmentController {

	//To add a new Appointment
	@RequestMapping(value = "/create", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody Integer SetAppointment(@RequestBody HashMap AppointmentMap,HttpServletRequest request ) throws Exception {

		return AppointmentDaoImpl.SetAppointment(AppointmentMap);
		
	}
	//To get all the Appointments
	@RequestMapping(value = "/get/all", produces = "application/json", method = RequestMethod.GET)  
	public @ResponseBody List<Appointment> getAppointments() {
		     return AppointmentDaoImpl.getAppointments();
				
    }
	//To get Appointments of a particular doctor
	@RequestMapping(value = "/get/{docID}", produces = "application/json", method = RequestMethod.GET)  
	public @ResponseBody List<Appointment> getAppointmentsOfDoc(@PathVariable int docID) {
		     return AppointmentDaoImpl.getAppointmentsOfDoc(docID);
				
    }
	
	//To get Total , unbooked slots and Completely Booked Dates of a particular doctor
	@RequestMapping(value = "/get/Slots/{DocID}", produces = "application/json", method = RequestMethod.GET)  
	public @ResponseBody Map<String, Object> findCompletelyBookedAndAvailableDates(@PathVariable int DocID) {
		     return AppointmentDaoImpl.findCompletelyBookedAndAvailableDates(DocID);
		
		
    }
	







}
	
