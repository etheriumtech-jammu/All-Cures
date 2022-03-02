package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.DoctorsDaoImpl;
import model.Doctors;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class DoctorsActionController
 */
public class DoctorsActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static MemcachedClient mcc = null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoctorsActionController() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void getProfile(HttpServletRequest request, HttpServletResponse response, boolean jsonResponse) throws ServletException, IOException {
		String id=request.getParameter("rowno");
		String cacheString = null;
		Constant.log("Got Req for Profile For DocID: "+id, 1);
		int rowno = -1;
		DoctorsDaoImpl doctorDao = null;
		Doctors doctorObj = null;
		if(id != null){
			rowno = Integer.parseInt(id);
			cacheString = findDocInCache(rowno);	
			String jsondata = null;
			if(cacheString == null || "".equals(cacheString) || "null".equalsIgnoreCase(cacheString)){
				//Doctor Not Found in MemCache
				Constant.log("Got Null From MemCache on the Doc:"+id, 1);
				doctorDao = new DoctorsDaoImpl();
				doctorObj = doctorDao.getAllDoctorsInfo(rowno);
				//SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
				//Date date = new Date(System.currentTimeMillis());
				//Constant.log("Date:"+formatter.format(date), 0);
				if(mcc == null)
					initializeCacheClient();
				//Add the Doctor Found to the Cache since the ID was not there
				Gson gson = new GsonBuilder().serializeNulls().create();	
				jsondata = gson.toJson(doctorObj);
				mcc.add(Constant.ROWNO+"_"+id,360000 ,jsondata).getStatus();
				//System.out.println("Adding up in docobj cache:"+ mcc.add("docObj",360000 ,doctorObj.toString()).getStatus());						
			}else{
				//Doctor Found in Memcache
				//Date waitTime= (Date) mcc.get("waiting_time");
				Constant.log("Found Doctor in Memcache and serving from there", 1);
				jsondata = (String)mcc.get(Constant.ROWNO+"_"+id);
				//doctorObj = new Doctors(jsondata);
				Constant.log("Done Constructing Doctor JSON From Memcache", 1);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");			
			Constant.log("Sending Response Now", 0);
			if(jsonResponse){				
				Constant.log("JSON Response Requested", 1);
				PrintWriter out = response.getWriter();				
				out.write(jsondata);
				out.flush();
				Constant.log("Responding with JSON data---->"+jsondata, 1);				
			}else{
				Constant.log("Responding with Servlet Redirect after putting JsonData in ServletContext---->"+jsondata, 1);
				this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
				response.sendRedirect("/cures/profile.jsp");
			}
		}else{
			if(jsonResponse){
				response.sendError(404, "No Doctor Found with Id:"+id);
			}else{
				//received an Empty DocId; Should send to home page or a page that says that no valid docid was selected
				response.sendRedirect(URLEncoder.encode("/cures/error.jsp?msg=Invalid Doctor Id", java.nio.charset.StandardCharsets.UTF_8.toString()));
			}
		}
	}
	
	public void getProfilebydocid(HttpServletRequest request, HttpServletResponse response, boolean jsonResponse) throws ServletException, IOException {
		String doctorid=request.getParameter("docid");
		String cacheString = null;
		Constant.log("Got Req for Profile For DocID: "+doctorid, 1);
		int docid = -1;
		DoctorsDaoImpl doctorDao = null;
		Doctors doctorObj = null;
		if(doctorid != null){
			docid = Integer.parseInt(doctorid);
			cacheString = findDocInCacheByDocID(docid);	
			String jsondata = null;
			if(cacheString == null || "".equals(cacheString) || "null".equalsIgnoreCase(cacheString)){
				//Doctor Not Found in MemCache
				Constant.log("Got Null From MemCache on the Doc:"+doctorid, 1);
				doctorDao = new DoctorsDaoImpl();
				doctorObj = doctorDao.getAllDoctorsInfoByDocId(docid);
				//SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
				//Date date = new Date(System.currentTimeMillis());
				//Constant.log("Date:"+formatter.format(date), 0);
				if(mcc == null)
					initializeCacheClient();
				//Add the Doctor Found to the Cache since the ID was not there
				Gson gson = new GsonBuilder().serializeNulls().create();	
				jsondata = gson.toJson(doctorObj);
				mcc.add(Constant.DOCID+"_"+doctorid,360000 ,jsondata).getStatus();
				//System.out.println("Adding up in docobj cache:"+ mcc.add("docObj",360000 ,doctorObj.toString()).getStatus());						
			}else{
				//Doctor Found in Memcache
				//Date waitTime= (Date) mcc.get("waiting_time");
				Constant.log("Found Doctor in Memcache and serving from there", 1);
				jsondata = (String)mcc.get(Constant.DOCID+"_"+doctorid);
				//doctorObj = new Doctors(jsondata);
				Constant.log("Done Constructing Doctor JSON From Memcache", 1);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");			
			Constant.log("Sending Response Now", 0);
			if(jsonResponse){				
				Constant.log("JSON Response Requested", 1);
				PrintWriter out = response.getWriter();				
				out.write(jsondata);
				out.flush();
				Constant.log("Responding with JSON data---->"+jsondata, 1);				
			}else{
				Constant.log("Responding with Servlet Redirect after putting JsonData in ServletContext---->"+jsondata, 1);
				this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
				response.sendRedirect("/cures/profile.jsp");
			}
		}else{
			if(jsonResponse){
				response.sendError(404, "No Doctor Found with Id:"+doctorid);
			}else{
				//received an Empty DocId; Should send to home page or a page that says that no valid docid was selected
				response.sendRedirect(URLEncoder.encode("/cures/error.jsp?msg=Invalid Doctor Id", java.nio.charset.StandardCharsets.UTF_8.toString()));
			}
		}
	}
	
	public MemcachedClient initializeCacheClient(){
		try {
			Constant.log("Trying Connection to Memcache server",0);
			mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(Constant.ADDRESS));
			Constant.log("Connection to Memcache server Sucessful",0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constant.log("Connection to Memcache server UN-Sucessful",3);
		}		
		return mcc;
	}
	
	public String findDocInCache(int rowno){
		String cacheString = null;
		
		//This is the ADDRESS OF MEMCACHE
		//TODO: Move to a Config Entry in Web.xml
		if(mcc == null){
			initializeCacheClient();
		}
		Constant.log("Getting docid from MemCache",0);
		if(mcc.get(Constant.ROWNO+"_"+rowno) != null)
			cacheString = mcc.get(Constant.ROWNO+"_"+rowno).toString();
		Constant.log("Found In MemCache:"+cacheString,0);
		return cacheString;
	}
	
	public String findDocInCacheByDocID(int doctId){
		String cacheString = null;
		
		//This is the ADDRESS OF MEMCACHE
		//TODO: Move to a Config Entry in Web.xml
		if(mcc == null){
			initializeCacheClient();
		}
		Constant.log("Getting docid from MemCache",0);
		if(mcc.get(Constant.DOCID+"_"+doctId) != null)
			cacheString = mcc.get(Constant.DOCID+"_"+doctId).toString();
		Constant.log("Found In MemCache:"+cacheString,0);
		return cacheString;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		String cmd = request.getParameter("cmd");
		if(cmd != null && "getProfile".equalsIgnoreCase(cmd)){
			getProfile(request, response, true);
		}else if(cmd != null && "getProfilebydocid".equalsIgnoreCase(cmd)){
			getProfilebydocid(request, response, true);
		}else{
			getProfile(request, response, false);
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
