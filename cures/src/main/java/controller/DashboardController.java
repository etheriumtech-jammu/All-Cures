package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dao.ArticleDaoImpl;
import model.Registration;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.CookieManager;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

	@Autowired
	private ArticleDaoImpl articleDaoImpl;

	@RequestMapping(path = "/articlecount", produces = "application/json",method = RequestMethod.GET)
	public @ResponseBody Map<String, Integer> getDashboardDetails(HttpServletRequest request) {
		HttpServletRequest req =(HttpServletRequest ) request;
		HttpSession session= req.getSession(true);
		int reg_id = 0;
		if(session.getAttribute(Constant.USER) != null){
			Constant.log("#########USER IS IN SESSION########", 0);
			Registration user = (Registration) session.getAttribute(Constant.USER);
			reg_id = user.getRegistration_id();
			System.out.println(reg_id);
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("published_article", articleDaoImpl.findPublishedArticle(reg_id).size());
		map.put("draft_article", articleDaoImpl.findDraftAricle(reg_id).size());
		map.put("approval_article", articleDaoImpl.findApprovalArticle(reg_id).size());
		map.put("review_article", articleDaoImpl.findReviwArticle(reg_id).size());
		
		
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