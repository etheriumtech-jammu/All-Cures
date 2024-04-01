package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import dao.ArticleDaoImpl;
import dao.ContentDaoImpl;
import model.Article;
import model.Article_dc_name;
import model.Doctor;
import model.Registration;
import util.Constant;
import util.SolrUtil;
import util.ArticleUtils;

/**
 * Servlet implementation class ContentActionController
 * 
 * The workflow for the Content is: Created --> In	Review --> Published
 * THe Author Info should be stored in the Author table and the author should be either looked up or entered into the DB; Entering Author
 * Info should only be allowed for the Admin/Editorial users of the site and for no one else; This info would be used for looking up the 
 * Author on the Article Display Page on the Site, once the Article is Published but the author still can't login till they register, at 
 * which time, the author is just like a doctor or a patient
 * 
 * In case a person (doctor or patient) is authoring, their registration_id should be used as the author_id for the article and they have to be 
 * signed in to the site to author
 * 
 * TODO: Disclaimer and Copyright related stuff
 * TODO: Article Publication Status related stuff
 */
public class ContentActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final int defAuthorRegId = 9999999;
	ContentDaoImpl contentDao = new ContentDaoImpl();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContentActionController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public int createArticle(HttpServletRequest request, HttpServletResponse response, Registration user) throws IOException{
		String requestJsonStr = IOUtils.toString(request.getInputStream(), "UTF-8");
		ObjectMapper mapper = new ObjectMapper();
    	Map<String,Object> requestJsonMap = mapper.readValue(requestJsonStr, Map.class);
    	
    	String title= (String) requestJsonMap.get("title");
    	Constant.log("Creating Article with Title:"+title, 1);
    	
		Article_dc_name artExistingTitle = new ArticleDaoImpl().getArticleDetails(title);
		
		if (null != artExistingTitle.getTitle() && artExistingTitle.getTitle().equalsIgnoreCase(title)) {
			Constant.log("Article Title already exist for article_id "+artExistingTitle.getArticle_id(), 0); 
			return -3;
		}
    	
    	String artFrndlyNm= (String) requestJsonMap.get("friendlyName");
    	Constant.log("Creating Article with Friendly Name:"+artFrndlyNm, 0);
		int iLang= requestJsonMap.get("language") !=null ? (int) requestJsonMap.get("language") : 1;//English By Default
		Constant.log("strLanguage:"+iLang, 0);
//		int iLang = 1; //English By Default
//		if(lang != null && !"".equals(lang.trim())){
//			iLang = Integer.parseInt(lang);
//			Constant.log("intLanguage:"+iLang, 0);
//		}
//		Constant.log("Creating Article with Language:"+lang, 0);
		String articleContent= (String) requestJsonMap.get("articleContent");
			//TODO: Capture Metadata of the Article and Persist in backend
		String subHead=(String) requestJsonMap.get("subHeading");
		Constant.log("Creating Article with subHeading:"+subHead, 0);
		//TODO: Need to create FK constraint in the DB for Content Types
		String content_type=(String) requestJsonMap.get("contentType");
//		int iContentType = 1; //Article By Default
//		if(content_type != null && !"".equals(content_type.trim())){
//			iContentType = Integer.parseInt(content_type);
//		}
		Constant.log("Creating Article with Content Type:"+content_type, 0);
		
		String type=(String) ""+requestJsonMap.get("type");
		Constant.log("Creating Article with Type:"+type, 0);
		

		
		String featured_article=(String) requestJsonMap.get("featured_article");
		Constant.log("Creating Article with Featured Article:"+featured_article, 0);
		
		String status= (String) ""+ requestJsonMap.get("articleStatus");
		System.out.println("Test articleStatus Val"+status);
		int iStatus = 1; //WIP By Default
		if(status  != null && !"".equals(status.trim())){
			iStatus = Integer.parseInt(status);
		}
		Constant.log("Creating Article with Status:"+status, 0);
		//disclaimer (should be an id)
		int iDiscId=(int) requestJsonMap.get("disclaimerId");
//		int iDiscId = -1; //Negative indicates error
//		if(d_loc != null && !"".equals(d_loc.trim())){
//			iDiscId = Integer.parseInt(d_loc);
//		}
		Constant.log("Creating Article with Disclaimer:"+iDiscId, 0);
		//copyright (should be an id)
		int iCopyId=(int) requestJsonMap.get("copyId");;
//		int iCopyId = -1; //Negative indicates error
//		if(c_loc != null && !"".equals(c_loc.trim())){
//			iCopyId = Integer.parseInt(c_loc);
//		}
		Constant.log("Creating Article with Copyright:"+iCopyId, 0);
		
		String authIdS = (String) ""+requestJsonMap.get("authById");;
//		int iAuthId = -1; //Negative indicates error
//		if(authId != null && !"".equals(authId.trim())){
//			iAuthId = Integer.parseInt(authId);
//		}
		Constant.log("Creating Article with Author:"+authIdS, 0);
		String keyword=(String) requestJsonMap.get("keywords");
		String window_title=(String) requestJsonMap.get("winTitle");;
		String articlecontent= (String) (String) requestJsonMap.get("articleContent");
		System.out.println("##############articlecontent###"+articlecontent);
		String countryId = (String) requestJsonMap.get("countryId");;
		int iCountryId = -1; //Negative indicates error
		if(countryId != null && !"".equals(countryId.trim())){
			iCountryId = Integer.parseInt(countryId);
		}
		Constant.log("Creating Article with Authors:"+authIdS, 0);
		
		String diseaseConditionId = (String) requestJsonMap.get("diseaseConditionId");;
		int iDiseaseConditionId = -1; //Negative indicates error
		if(diseaseConditionId != null && !"".equals(diseaseConditionId.trim())){
			iDiseaseConditionId = Integer.parseInt(diseaseConditionId);
		}
		Constant.log("Creating Article with Author:"+diseaseConditionId, 0);
		
		String promoId = (String) requestJsonMap.get("promoId");;
		int ipromoId = -1; //Negative indicates error
		int promoStage = -1; //Negative indicates error
		if(promoId != null && !"".equals(promoId.trim())){
			ipromoId = Integer.parseInt(promoId);
			promoStage = 0 ; // promoStage < 0 or null ==> No promo applied, promo promoStage = 0 ==> promo applied no paid, promoStage =1 ==> promo applied and paid
		}
		Constant.log("Creating Article with promoId:"+promoId, 0);
		
		int imedicineTypeId= requestJsonMap.get("medicine_type") !=null ? (int) requestJsonMap.get("medicine_type") : -1;//-1 By Default

		Constant.log("Creating Article with imedicineTypeId:"+imedicineTypeId, 0);
		
		Constant.log("Saving Content in Dao", 1);
		String comments= (String) requestJsonMap.get("comments");
		Constant.log("comments:"+comments, 0);
		//User Object in Session is coming Null Here and Causing a Null Pointer Exception on the next line
		//This should never be the case as the user has to be logged  in to create an article
		int result=-1;		
		if(user == null){
			Constant.log("Missing user object in session; User is not logged In; Send to login", 0); 
			result = -5;
		}else{
			//check DEFAULT i.e type =1 and disease_condition id is unique
			if (type.contains("1")) {
				List<Article> countMatchArticles = contentDao.findByArticleTypeAndDC(iDiseaseConditionId);
				if(countMatchArticles.size()>0) {
					Constant.log("Default Article for Disease_condition_id already present", 0); 
					return -2;
				} 
			}
			//TODO: Remove this hardcoding	
			Constant.log("User object is in session; User is logged In; Adding Article Now", 0);
			boolean bResult = contentDao.createArticle(iStatus, iLang, iDiscId, iCopyId, authIdS, title, artFrndlyNm, subHead, 
					content_type, keyword, window_title, null, user.getRegistration_id().intValue(), articlecontent, iDiseaseConditionId, iCountryId,comments,
					ipromoId,promoStage,type,imedicineTypeId,featured_article);
			if(bResult == true){
				result = 1;
			}
		}    	
		return result;
    }

    public Article findArticle(int artId, int regId){
    	Article article = null;
		article = contentDao.findByArticleId(artId);
		return article;
    }
    
    public boolean updateArticle(HttpServletRequest request, HttpServletResponse response, int articleId, int userId){
    	String title= request.getParameter("title");
		String author_fname= request.getParameter("firstName");
		String author_mname= request.getParameter("middleName");
		String author_lname= request.getParameter("lastName");
		String lang= request.getParameter("language");
		String address = request.getParameter("address");
		String no = request.getParameter("phoneNumber");
		String articleContent= request.getParameter("articleContent");
		String content_loc= request.getParameter("content_loc");
		String email= request.getParameter("email");
		String featured_article= request.getParameter("featured_article");
		//String address= request.getParameter("address");
		//String no = request.getParameter("t_number");		
		String subhead="";
		String content_type="";
		String articlefname="";		
		//String no="99999999999"	;	
		//String address= "";
		String status= "";
		String d_loc="";
		String c_loc="";
		String keyword="";
		String window_title="";
		
		boolean result=contentDao.updateArticle(status, lang, d_loc, c_loc, author_fname, author_mname, author_lname, email, address, no, title, articlefname, subhead, content_type, keyword, window_title, content_loc, articleContent, articleId, userId);
		return result;			
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * ToDo: Implement the following:
	 * 			1. Dashboard Functionality - Get All Articles Published By this User - Done
	 * 			2. Write an Article - cmd=createArticle - Done 
	 * 			3. Edit/Update Article - cmd = updateArticle - Done
	 * 			4. Promote Article - cmd = promoteArticle
	 * 			5. Demote Article - cmd = demoteArticle 
	 * 			6. Find Article - cmd = findArticle - Done
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String article_id = request.getParameter("article_id") == null ? "" : request.getParameter("article_id");
		//actions are hidden params and cmds are ajax calls
		boolean ajaxCall = true;
		String action = request.getParameter("articleAction");
		PrintWriter out = response.getWriter();
		String finalAction = "";
		if(action != null && !"".equals(action)){
			ajaxCall = false;
			finalAction = action;
		}else{
			finalAction = request.getParameter("cmd");
		}		
		int status = -1;
		String artJson = "";
		javax.servlet.http.HttpSession ses= request.getSession();
		Registration regi=(Registration)ses.getAttribute(Constant.USER);		
		Constant.log("In Content Action Controller with action::"+finalAction+" by user:"+regi.getEmail_address(), 1);		
		if(finalAction.equals("createArticle")){			
			status = createArticle(request, response, regi);			
			Constant.log("Post Dao Content Save with Result:"+status, 1);
			if(!ajaxCall){
				//Set the article object in the session or servlet Context
				Constant.log("Sending status to article.jsp:"+status, 1);
				//status = 1;
				response.sendRedirect("/cures/article.jsp?status="+status);
			}else{
				Constant.log("Ajax Call Response:"+status, 1);
				//status = 1;
				Constant.log("staus="+status,1);
				out.write(""+status);
				out.flush();
				out.close();
			}				
		}else if (finalAction.equals("findArticle")) {		
			Constant.log("Finding  Article:"+article_id, 1);
			Article article = findArticle(Integer.parseInt(article_id), regi.getRegistration_id().intValue());
			Constant.log("Found Article:"+article.getTitle(), 1);
			String content_loc = article.getContent_location();
			String fullArtJson = null;
			String artMetaJson = SolrUtil.jsonifyObject(article);
			Constant.log("ArticleJSON:"+artMetaJson, 1);
			String artContentJson = null;
			File file = new File(content_loc); 
	        BufferedReader br = new BufferedReader(new FileReader(file));
            while ((artContentJson += br.readLine()) != null){          
            }
            Constant.log("Article Content:"+artContentJson, 1);
            if(!ajaxCall){
            	//If Regular Call
            	Constant.log("Sending response to Article.jsp", 1);
                this.getServletContext().setAttribute("articlemetaData", artMetaJson);			
                this.getServletContext().setAttribute("articleContentFile", artContentJson);
    			response.sendRedirect("/cures/article.jsp");
			}else{
				//If Ajax Call
				Constant.log("Writing JSON response", 1);
	            JSONObject fullArtJO = new JSONObject().put("article_meta", artMetaJson);
	            fullArtJO.put("article_content", artContentJson);
	            fullArtJson = fullArtJO.toString();
	            Constant.log("Full Article:"+fullArtJson, 1);
	            out.write(fullArtJson);
	            out.flush();
	            out.close();
			}            
		}else if (finalAction.equals("updateArticle")) {
			Constant.log("Updating Article:"+article_id, 1);
			boolean artStatus = updateArticle(request, response, Integer.parseInt(article_id), regi.getRegistration_id().intValue());
			if(!ajaxCall){
				//Set the article object in the session or servlet Context
				Constant.log("Sending to article.jsp:"+artJson, 1);
				response.sendRedirect("/cures/article.jsp?message="+artStatus);
			}else{
				Constant.log("Ajax Call Response:"+artJson, 1);
				out.write("status:"+artStatus);
				out.flush();
				out.close();
			}			
		}else if (finalAction.equals("createDashboard")) {
			List<Article> articlearr = null;
			String state = request.getParameter("state");
			int iState = -1; //Articles in any state
			if(state != null && !"".equals(state.trim())){
				iState = Integer.parseInt(state);
			}
			Constant.log("Getting Articles in state:"+state, 0);
			String author=request.getParameter("authId");
			int iAuthId = -1; //All Authors
			if(author != null && !"".equals(author.trim())){
				iAuthId = Integer.parseInt(author);
			}
			Constant.log("Getting Articles with Author:"+author, 0);
			
			articlearr = contentDao.dashboardDisplay(regi.getRegistration_id().intValue(), iAuthId, iState);
			Gson gson = new Gson();
			JSONObject stringToJsonObject =new JSONObject().put ("articleDetail",articlearr);
			// System.out.println("------------json OBJECT OUT>>>>>>>"+stringToJsonObject);
			String jsonData = gson.toJson(stringToJsonObject);
			if(!ajaxCall){
				//Set the article object in the session or servlet Context
				Constant.log("Sending to dashboard.jsp:"+jsonData, 1);
				this.getServletContext().setAttribute("articles", jsonData);
				response.sendRedirect("/cures/dashboard.jsp");
			}else{
				Constant.log("Ajax Call Response:"+jsonData, 1);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");				
				// out.write(json);
				out.write(jsonData);
				out.flush();
				out.close();
			}			
		}else if (finalAction.equals("createBlog")) {
			//ToDo: Not sure why this was created; Will need to most probably remove; But keep it in for time being
			Article articlearray = new Article();
			articlearray= contentDao.findByArticleId(Integer.parseInt(article_id));
			String content_loc=articlearray.getContent_location();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			String jsondata = gson.toJson(articlearray);
			response.getWriter().write(jsondata);
			System.out.println("JSON data---->"+jsondata);
			this.getServletContext().setAttribute("articlemetaData", jsondata);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd"); 
			LocalDateTime now = LocalDateTime.now();
			File file = new File(content_loc); 
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String st; 
            while ((st = br.readLine()) != null){ 
              System.out.println(st);
              this.getServletContext().setAttribute("articleContentFile", st);
            }            
			response.sendRedirect("/cures/blog.jsp");
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
