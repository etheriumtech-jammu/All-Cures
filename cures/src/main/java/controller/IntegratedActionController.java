package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.google.gson.Gson;

import dao.DoctorsDaoImpl;
import dao.SpecialtiesDaoImpl;
import dao.SubspecialtiesDaoImp;
import model.Doctors;
import model.Registration;
import model.Specialties;
import model.Subspecialties;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class IntegratedActionController
 */
public class IntegratedActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IntegratedActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		DoctorsDaoImpl doctors = new DoctorsDaoImpl();
		ArrayList<String> doctorArray = new ArrayList<String>();
		//ArrayList<String> cacheArray = new ArrayList<String>();
		SpecialtiesDaoImpl spl = new SpecialtiesDaoImpl();
		ArrayList<Specialties> splArray = new ArrayList<Specialties>();
		SubspecialtiesDaoImp subspl = new SubspecialtiesDaoImp();
		ArrayList<Subspecialties> subsplArray = new ArrayList<Subspecialties>();
		ArrayList<String> cachedocArray= new ArrayList<String>();
		ArrayList<String> cachesplArray= new ArrayList<String>();
		ArrayList<String> cachesubsplArray= new ArrayList<String>();

		
		String cachedocnameString = null;
		String cacheSplString = null;
		String cacheSplSubString = null;

		//String cacheString = null;
		MemcachedClient mcc = null;
		String address = Constant.ADDRESS;

		try {
			mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Constant.log("Connection to memcache server sucessful", 1);
		cachedocnameString =  (""+mcc.get(Constant.DOCNAME)+"").toString();
		cacheSplString  = (""+mcc.get(Constant.SPL)+"").toString();
		cacheSplSubString = (""+mcc.get(Constant.SUBSPL)+"").toString();
		String ct=Constant.NULL;
		if ((cachedocnameString.contains(ct)) && (cacheSplString.contains(ct)) && (cacheSplSubString.contains(ct))){
			cachedocnameString=Constant.NULL;
			cacheSplString=Constant.NULL;	
			cacheSplSubString=Constant.NULL;
			doctorArray = doctors.findAllDoctors();
			splArray = spl.findAllSpecialties();
			subsplArray= subspl.findAllSubSpecialties();
			//Constant.log("splarr"+splArray);
			Constant.log(("Adding to mem cache:"+ mcc.add(Constant.DOCNAME,360000,doctorArray).getStatus()), 1);
			Constant.log(("Adding up in cache:"+ mcc.add(Constant.SPL,360000 ,splArray).getStatus()), 1);
			Constant.log(("Adding up in cache:"+ mcc.add(Constant.SUBSPL,360000 ,subsplArray).getStatus()), 1);
			JSONObject stringToJsonObject =new JSONObject().put (Constant.DOCNAME,doctorArray);
			stringToJsonObject.put(Constant.SPECIALTIES, splArray);
			stringToJsonObject.put(Constant.SUBSPECIALTIES, subsplArray);
			//cacheArray.addAll((Collection<? extends String>) mcc.get("docname"));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			// Constant.log("------------json OBJECT OUT>>>>>>>"+stringToJsonObject);
			String jsondata = gson.toJson(stringToJsonObject);

			// out.write(json);
			response.getWriter().write(jsondata);


			//Constant.log(("JSON data database---->"+jsondata), 1);
			//RequestDispatcher view = request.getRequestDispatcher("/test.html");
			//response.sendRedirect("test.html");
			//view.forward(request, response);
			out.flush();
		}else{
			cachedocArray.addAll((Collection<? extends String>) mcc.get(Constant.DOCNAME));
			cachesplArray.addAll((Collection<? extends String>) mcc.get(Constant.SPL));
			cachesubsplArray.addAll((Collection<? extends String>) mcc.get(Constant.SUBSPL));
			/* int length= 0;
			    if(length!=0){
			   ArrayList<String> limitdocnameCache= new ArrayList<String>();
			   ArrayList<String> limitsplCache= new ArrayList<String>();
			   ArrayList<String> limitSubsplCache= new ArrayList<String>();
			   for(int i=0;i<length;i++){
				   limitdocnameCache.add(cachedocArray.get(i));
				   limitsplCache.add(cachesplArray.get(i));
				   limitSubsplCache.add(cachesubsplArray.get(i));
			   }JSONObject stringToJsonObject =new JSONObject().put ("Doctorname",limitdocnameCache);
				stringToJsonObject.put("Specialties", limitsplCache);
				stringToJsonObject.put("SubSpecialties", limitSubsplCache);

			   }else{*/
			/**/
			JSONObject stringToJsonObject =new JSONObject().put (Constant.DOCTORNAME,cachedocArray);
			stringToJsonObject.put(Constant.SPECIALTIES, cachesplArray);
			stringToJsonObject.put(Constant.SUBSPECIALTIES, cachesubsplArray);
			// }
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(stringToJsonObject);

			// out.write(json);
			response.getWriter().write(jsondata);


			//Constant.log(("JSON data cache---->"+jsondata), 1);
			out.flush();

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
