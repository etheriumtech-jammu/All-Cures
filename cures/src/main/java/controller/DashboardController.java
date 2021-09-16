package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ArticleDaoImpl;
import model.Article;
import model.Registration;
import util.Constant;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	@RequestMapping(path = "/articlecount", produces = "application/json",method = RequestMethod.GET)
	public @ResponseBody Map<String, ArrayList<Article>> getDashboardDetails(HttpServletRequest request) {
		HttpServletRequest req =(HttpServletRequest ) request;
		HttpSession session= req.getSession(true);
		int reg_id = 0;
		Registration user = null;
		if(session.getAttribute(Constant.USER) != null){
			Constant.log("#########USER IS IN SESSION########", 0);
			user = (Registration) session.getAttribute(Constant.USER);
			reg_id = user.getRegistration_id();
			System.out.println(reg_id);
		}
		HashMap<String, ArrayList<Article>> map = new HashMap<String, ArrayList<Article>>();
		map.put("published_article", articleDaoImpl.findPublishedArticle(user));
		map.put("draft_article", articleDaoImpl.findDraftAricle(user));
		map.put("approval_article", articleDaoImpl.findApprovalArticle(user));
		map.put("review_article", articleDaoImpl.findReviwArticle(user));
		
		
//		String cacheCityString = null;
//		String cachepinString = null;
//
//		MemcachedClient mcc = null;
//		String address = Constant.ADDRESS;
//
//		try {
//			mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
//		System.out.println("Connection to server sucessfully");
//		System.out.println("Get from Cache cityname:"+"'"+mcc.gets(Constant.CITY)+"'");
//		System.out.println("Get from Cache pincode:"+"'"+mcc.gets(Constant.PIN)+"'");
//		cacheCityString =  (""+mcc.get(Constant.CITY)+"").toString();
//		cachepinString  =(""+mcc.get(Constant.PIN)+"").toString();
//		
//
//	    //System.out.println("add status:"+mcc.add("ct", 900, "Delhi").done);  
//		System.out.println("Adding up in cache:"+ mcc.add(Constant.dashboard_review_count,360000 ,map.get("review_article")).getStatus());
//
//	    System.out.println("Get from Cache ct:"+mcc.get(Constant.dashboard_review_count)); 
		
		
		return map;
	}

}