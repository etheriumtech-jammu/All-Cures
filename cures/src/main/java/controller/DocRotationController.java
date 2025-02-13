package controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.DocRotationDaoImpl;
import service.WeeklyRotationScheduler;


@RestController
@RequestMapping(path = "/rotation")
public class DocRotationController {

	@RequestMapping(value = "/doctors/list", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String, Object>> getDoctorsList(HttpServletRequest request,@RequestParam(required=false) Integer userID,@RequestParam(required=false) Integer offset) throws Exception {
    
		WeeklyRotationScheduler.rotateResults();
		return DocRotationDaoImpl.getDoctorsList();
	}

}
