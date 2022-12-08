package controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.AnalyticsDao;



@RestController
@RequestMapping(path = "/analytics")
public class AnalyticsController {


	@RequestMapping(value = "/all", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  getDetails( @RequestParam(required = false) String column,@RequestParam(required = false) String order , HttpServletRequest request) {
		// HttpServletRequest req = (HttpServletRequest) request;
	// 	HttpSession session = req.getSession(true);
		
		
		return AnalyticsDao.getDetails(column,order);

	}
	
	@RequestMapping(value = "/article", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  Articlecounts(@RequestParam(required = false) String column,@RequestParam(required = false) String order , HttpServletRequest request) {
		//HttpServletRequest req = (HttpServletRequest) request;
	// 	HttpSession session = req.getSession(true);
		
		
		return AnalyticsDao.Articlecount(column,order);

	}
	
	
	@RequestMapping(value = "/whatsapp", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List Count( @RequestParam(required = false) String column,@RequestParam(required = false) String order ,HttpServletRequest request) {
		//HttpServletRequest req = (HttpServletRequest) request;
	// 	HttpSession session = req.getSession(true);
		
		
		return AnalyticsDao.Countwhatsapp(column,order);

	}
	
	@RequestMapping(value = "/top", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  Top( HttpServletRequest request) {
		//HttpServletRequest req = (HttpServletRequest) request;
	// 	HttpSession session = req.getSession(true);
		
		
		return AnalyticsDao.mostvisited();
		
	}
	
	@RequestMapping(value = "/range", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  Range(@RequestParam(required = false) Date limit1,@RequestParam(required = false) Date limit2,@RequestParam(required = false) String column,@RequestParam(required = false) String order , HttpServletRequest request) {
		//HttpServletRequest req = (HttpServletRequest) request;
	// 	HttpSession session = req.getSession(true);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = dateFormat.format(limit1);
	    String strDate2;
		
		System.out.println(strDate1);
		if (limit2==null)
		{
			Date d=new Date();
			 strDate2 = dateFormat.format(d);
		}
		else
		{
		 strDate2 = dateFormat.format(limit2);
		}
		
		System.out.println(strDate2);
		
		return AnalyticsDao.Daterange(strDate1,strDate2,column,order);

	}
	
	
	@RequestMapping(value = "/recordarticle", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  RecordArticle( HttpServletRequest request) {
	
		return AnalyticsDao.recordarticle();
			
	}
	
	@RequestMapping(value = "/rating", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  Rating(@RequestParam(required = false) Date limit1,@RequestParam(required = false) Date limit2, HttpServletRequest request) {
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = dateFormat.format(limit1);
	   String strDate2=dateFormat.format(limit2);
		System.out.println("hello");
		return AnalyticsDao.rating1(strDate1,strDate2);
		
	}
			
	
	@RequestMapping(value = "/comment", produces = "application/json", method = RequestMethod.GET)
	public @ResponseBody List  Comment(@RequestParam(required = false) Date limit1,@RequestParam(required = false) Date limit2, HttpServletRequest request) {
	
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate1 = dateFormat.format(limit1);
	   String strDate2=dateFormat.format(limit2);
	
		return AnalyticsDao.comment(strDate1,strDate2);
		
	}
			
}
