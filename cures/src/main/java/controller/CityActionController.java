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
import com.google.gson.JsonArray;

import dao.CityDaoImpl;
import dao.countriesDaoImpl;
import model.City;
import model.countries;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class CityActionController
 */
public class CityActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CityActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Inside the dopost bhjhhjhjhj");
		CityDaoImpl cityPin= new CityDaoImpl();
		ArrayList<City> CityArray= new ArrayList<City>();
		ArrayList<City> Pincode= new ArrayList<City>();
		ArrayList<String> cacheCityArray =new ArrayList<String>();
		ArrayList<String> cachepinArray = new ArrayList<String>();
		String cacheCityString = null;
		String cachepinString = null;

		MemcachedClient mcc = null;
		String address = Constant.ADDRESS;

		try {
			mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println("Connection to server sucessfully");
		System.out.println("Get from Cache cityname:"+"'"+mcc.gets(Constant.CITY)+"'");
		System.out.println("Get from Cache pincode:"+"'"+mcc.gets(Constant.PIN)+"'");
		cacheCityString =  (""+mcc.get(Constant.CITY)+"").toString();
		cachepinString  =(""+mcc.get(Constant.PIN)+"").toString();
		String ct=Constant.NULL;
		if ((cacheCityString.contains(ct)) &&( cachepinString.contains(ct))){
			cacheCityString=Constant.NULL;
			cachepinString=Constant.NULL;
			// if((cacheCityArray.contains(null) )&&( cachepinArray.contains(null))){

			CityArray=cityPin.findAllCity();
			Pincode=cityPin.findAllPincode();
			System.out.println("Adding up in cache:"+ mcc.add(Constant.CITY,360000 ,CityArray).getStatus());
			System.out.println("Adding up in cache:"+ mcc.add(Constant.PIN,360000 ,Pincode).getStatus());
			String arrayToStringCity = CityArray.toString();  // convert array to string
			String arrayToStringPin = Pincode.toString();
			JSONObject stringToJsonObject =new JSONObject().put (Constant.CITYNAME,arrayToStringCity);
			stringToJsonObject.put(Constant.PINCODE, arrayToStringPin);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(stringToJsonObject);

			// out.write(json);
			response.getWriter().write(jsondata);


			System.out.println("JSON data database---->"+jsondata);
			out.flush();
		}
		else{
			cacheCityArray.addAll((Collection<? extends String>) mcc.get(Constant.CITY));
			cachepinArray.addAll((Collection<? extends String>) mcc.get(Constant.PIN));
			/* int length= 0;
			    if(length!=0){
			   ArrayList<String> limitCityCache= new ArrayList<String>();
			   ArrayList<String> limitPinCache= new ArrayList<String>();
			   for(int i=0;i<length;i++){
				   limitCityCache.add(cacheCityArray.get(i));
				   limitPinCache.add(cachepinArray.get(i));
			   }String arrayToStringCity = limitCityCache.toString();
			   String arrayToStringPin = limitPinCache.toString();
			   }else{*/
			String arrayToStringCity = cacheCityArray.toString();  // convert array to string
			String arrayToStringPin = cachepinArray.toString();
			// }
			JSONObject stringToJsonObject =new JSONObject().put (Constant.CITYNAME,arrayToStringCity);
			stringToJsonObject.put(Constant.PINCODE, arrayToStringPin);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String jsondata = new Gson().toJson(stringToJsonObject);

			// out.write(json);
			response.getWriter().write(jsondata);


			System.out.println("JSON data cache---->"+jsondata);
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
