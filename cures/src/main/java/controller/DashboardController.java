package controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dao.ArticleDaoImpl;
import model.Article;
import model.Article_dc_name;
import model.Registration;
import util.ArticleUtils;
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
//		MemcachedClient mcc = null;
//		String address = Constant.ADDRESS;
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
	
	
	@RequestMapping(value = "/imageupload/{type}/{id}", produces = "application/json", method = RequestMethod.POST)
	public @ResponseBody int imageUpload(@RequestParam CommonsMultipartFile File, HttpServletRequest request,
			HttpSession session,@PathVariable(required = true) String type, @PathVariable(required = true) Integer id) {
//		@RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = "application/json")
//		public @ResponseBody HashMap uploadFile(@RequestParam CommonsMultipartFile image, HttpServletRequest request,
//				HttpSession session) {
			//String path = session.getServletContext().getRealPath("/uitest");
			String curesProperties = "cures.properties";
			Properties prop = null;
			try {
				prop = new ArticleUtils().readPropertiesFile(curesProperties);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("ARTICLES_UPLOAD_DIR : " + prop.getProperty("ARTICLES_UPLOAD_DIR"));
			String cures_articleimages = prop.getProperty("cures_articleimages");
			String path = System.getProperty( "catalina.base" ) + "/webapps/"+cures_articleimages;

			System.out.println(path);
			// path = path+"/uitest";
//			String filename = File.getOriginalFilename();
			String filename = "";
			
			if(type.equalsIgnoreCase("article")) {
				Article_dc_name artDet = (Article_dc_name) articleDaoImpl.getArticleDetails(id);
				filename =  artDet.getContent_location().replace(".json", ".png");
			}else if(type.equalsIgnoreCase("doctor")) {
				filename = path+"/doctors/"+id+".png";
			}else if(type.equalsIgnoreCase("patient")) {
				filename = path+"/patients/"+id+".png";
			}
			else {
				return 0;
			}

			System.out.println(filename);
			try {
				byte barr[] = File.getBytes();

				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(filename));
				bout.write(barr);
				bout.flush();
				bout.close();

			} catch (Exception e) {
				System.out.println(e);
			}
			HashMap hm = new HashMap();
			hm.put("success", 1);
			HashMap hm2 = new HashMap();
			String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();

			System.out.println(baseUrl);
			hm2.put("url", baseUrl + "/"+cures_articleimages+"/" + filename);
			hm.put("file", hm2);
			return 1;
		}

}