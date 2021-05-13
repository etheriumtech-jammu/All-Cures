package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import dao.SubspecialtiesDaoImp;
import model.Specialties;
import model.Subspecialties;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class SubspecialtiesActionController
 */
public class SubspecialtiesActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubspecialtiesActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		SubspecialtiesDaoImp subspl = new SubspecialtiesDaoImp();
		ArrayList<Subspecialties> subsplArray = new ArrayList<Subspecialties>();

		ArrayList<String> cacheSubsplArray = new ArrayList<String>();
		String cacheSubsplString = null;
		MemcachedClient mcc = null;
		String address = Constant.ADDRESS;

		try {
			mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println("Connection to server sucessfully");
		System.out.println("Get from Cache:"+mcc.get(Constant.SUBSPL));
		cacheSubsplString= (""+ mcc.get(Constant.SUBSPL)+"").toString();
		String ct=Constant.NULL;
		if(cacheSubsplString.contains(ct)){

			subsplArray= subspl.findAllSubSpecialties();
			//JsonArray jsArray = new JsonArray(countryArray.toArray());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(subsplArray);

			// out.write(json);
			response.getWriter().write(jsondata);


			System.out.println("JSON data from database---->"+jsondata);
			out.flush();
		} else{
			cacheSubsplArray.addAll((Collection<? extends String>) mcc.get(Constant.SUBSPL));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(cacheSubsplArray);

			// out.write(json);
			response.getWriter().write(jsondata);


			System.out.println("JSON data from cache---->"+jsondata);
			out.flush();

		}
	}


	//response.getWriter().append("Served at: ").append(request.getContextPath());


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
