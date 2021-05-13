package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import dao.SplSearchDaoImpl;
import model.Doctor;
import util.Constant;

/**
 * Servlet implementation class SplSearchActionController
 */
public class SplSearchActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SplSearchActionController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String lat= request.getParameter("Latitude");
		String lon = request.getParameter("Longitude");
		SplSearchDaoImpl splsearch= new SplSearchDaoImpl();
		List<Doctor> docSolr = new ArrayList<Doctor>();
		docSolr = splsearch.SplSearchLatLon(lat,lon);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JSONObject stringToJsonObject =new JSONObject().put (Constant.DOCTORDETAILS,docSolr);
		// System.out.println("------------json OBJECT OUT>>>>>>>"+stringToJsonObject);
		String jsondata = gson.toJson(stringToJsonObject);

		// out.write(json);
		response.getWriter().write(jsondata);
		//Const obj=Json.parse(jsondata);
		


		System.out.println("JSON data solr database---->"+jsondata);
		this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
		response.sendRedirect("/cures/search.jsp");
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
