package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.query.Query;
import com.google.gson.Gson;

import dao.SearchDaoImpl;
import model.Doctor;
import util.Constant;
import util.HibernateUtil;
/**
 * Servlet implementation class SearchActionController
 */
public class SearchActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchActionController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String cmd = request.getParameter("cmd");
		if(cmd != null && "getResults".equalsIgnoreCase(cmd)){
			getResults(request, response, true);
		}else{
			getResults(request, response, false);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
		
	}
	
	public void getResults(HttpServletRequest request, HttpServletResponse response, boolean jsonResponse) throws ServletException, IOException {
		String featuredDoctors="";
		try{
		//	String lat= request.getParameter("Latitude");
	//		String lon = request.getParameter("Longitude");
			String city_pin = request.getParameter(Constant.CITYVALUE)== null ? "": request.getParameter(Constant.CITYVALUE);
			String doc_details = request.getParameter(Constant.DOCTORS)== null ? "": request.getParameter(Constant.DOCTORS);
			Constant.log("************************"+doc_details, 0);
	//		Constant.log("Latitude>>>>>>>>>>>>>>>>"+lat, 0);
	//		Constant.log("Longitude>>>>>>>>>>>>>>>>"+lon, 0);
			Constant.log("City>>>>>>>>>>>>>>>>"+city_pin, 0);
			
			SearchDaoImpl search = new SearchDaoImpl();
			//Map<String,List<String>> docSolr  = new HashMap<String,List<String>>();
			List<Doctor> docSolr = new ArrayList<Doctor>();
			
	//		String featuredDoctors= request.getParameter("FeaturedDoctors");
			Session session = HibernateUtil.buildSessionFactory();

			if(doc_details == "" && city_pin == "" )
			{
				
				Query query1 = session.createNativeQuery("SELECT docid, prefix " +
				        "FROM Doctors_New  where MedicineTypeID is Not NULL and (docid<=63 or docid>=14485) " +
				        "ORDER BY docid desc "  + ";");
		//		List<Integer> rownoList = query1.getResultList();
				List<Object[]> resultList = query1.getResultList();

				// Convert the rowno values to a comma-separated string without the "rowno:" prefix
		//		String featuredDoctors = rownoList.stream()
		//		    .map(String::valueOf)
		//		    .collect(Collectors.joining(","));
				
				 featuredDoctors = resultList.stream()
				        .map(row -> String.valueOf(row[0]))
				        .collect(Collectors.joining(","));
				System.out.println(featuredDoctors);
			}
			
			if (null !=featuredDoctors && featuredDoctors.length() != 0) {
				System.out.println("In Featured doctor..");
				docSolr = search.featuredDoctors(featuredDoctors);
				System.out.println("docSolr" + docSolr);
			}else {		
				if(!city_pin.equals("") && doc_details.equals("")){
					Constant.log("Searching By City Pin:"+city_pin, 1);
					docSolr = search.searchByCityPin(city_pin);
				}else if (!doc_details.equals("") && city_pin.equals("")) {
					Constant.log("Searching By Doc Details:"+doc_details, 1);
					docSolr = search.searchByDocSpl(doc_details);
				}else {
					Constant.log("Searching By Both City:"+city_pin, 1);
					docSolr = search.searchByBoth(doc_details, city_pin);
				}
			}
			
			response.setContentType("application/json");
			
	                response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
			Gson gson = new Gson();
			//Converting Results to a JSON Object
			JSONObject stringToJsonObject = new JSONObject().put (Constant.DOCTORDETAILS, docSolr);
			// Constant.log("------------json OBJECT OUT>>>>>>>"+stringToJsonObject);
			//Converting JSON to String using GSON
			String jsondata = gson.toJson(stringToJsonObject);
			if(jsonResponse){
				 
	                OutputStream out = response.getOutputStream();
	                try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
	                		PrintWriter writer = new PrintWriter(new OutputStreamWriter(gzipOutputStream, StandardCharsets.UTF_8))) {
	                      writer.write(jsondata);
	                }finally {
	                    out.flush();
	                    out.close();
	                }
			}else{
				Constant.log("Sending Response to search.jsp page", 0);
				this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
				response.sendRedirect("/cures/search.jsp");
			}			
		}catch(Exception ex){
			ex.printStackTrace();
			Constant.log("Error Getting Results", 3);
		}
	}

}
