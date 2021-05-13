package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.countriesDaoImpl;
import model.countries;

/**
 * Servlet implementation class CountriesActionController
 */
public class CountriesActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CountriesActionController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("Inside the dopost bhjhhjhjhj");
		countriesDaoImpl country= new countriesDaoImpl();
		ArrayList<countries> countryArray= country.findAllCountries();
		//JsonArray jsArray = new JsonArray(countryArray.toArray());
		System.out.println("list*****************"+countryArray);
		if(countryArray.isEmpty()){
			response.sendRedirect("/error.jsp");
		}
		request.setAttribute("countryList",countryArray) ;
		System.out.println(countryArray);
		RequestDispatcher view = request.getRequestDispatcher("/CountryList.jsp");
		//getServletConfig().getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
