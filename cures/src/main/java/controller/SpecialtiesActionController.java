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

import dao.SpecialtiesDaoImpl;
import model.Specialties;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class SpecialtiesActionController
 */
public class SpecialtiesActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SpecialtiesActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		SpecialtiesDaoImpl spl = new SpecialtiesDaoImpl();
		ArrayList<Specialties> splArray = new ArrayList<Specialties>();

		ArrayList<String> cachesplArray = new ArrayList<String>();
		String cachesplString = null;
		MemcachedClient mcc = null;
		String address = Constant.ADDRESS;

		try {
			mcc = new MemcachedClient(
					new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
					AddrUtil.getAddresses(address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection to server sucessfully");
		System.out.println("Get from Cache:" + mcc.get(Constant.SPL));
		cachesplString = ("" + mcc.get(Constant.SPL) + "").toString();
		String ct = Constant.NULL;
		if (cachesplString.contains(ct)) {
			splArray = spl.findAllSpecialties();
			// JsonArray jsArray = new JsonArray(countryArray.toArray());
			//System.out.println("list*****************" + splArray);
			String arrayToString = splArray.toString(); // convert array to
														// string
			// JSONObject stringToJsonObject =new JSONObject().put
			// ("Spl_name",arrayToString); // convert string to json object

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(splArray);

			// out.write(json);
			response.getWriter().write(jsondata);

			//System.out.println("JSON data from database---->" + jsondata);
		} else {
			cachesplArray.addAll((Collection<? extends String>) mcc.get(Constant.SPL));
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			// System.out.println("------------json OBJECT
			// OUT>>>>>>>"+stringToJsonObject);
			String jsondata = gson.toJson(cachesplArray);

			// out.write(json);
			response.getWriter().write(jsondata);

			//System.out.println("JSON data cache---->" + jsondata);
			// RequestDispatcher view =
			// request.getRequestDispatcher("/test.html");
			// response.sendRedirect("test.html");
			// view.forward(request, response);
			out.flush();

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
