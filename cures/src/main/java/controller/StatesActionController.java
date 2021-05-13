package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.StatesDaoImpl;
import dao.countriesDaoImpl;
import model.States;
import model.countries;

/**
 * Servlet implementation class StatesActionController
 */
public class StatesActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatesActionController() {
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

		StatesDaoImpl state = new StatesDaoImpl();

		ArrayList<States> stateArray = state.findAllStates();
		// JsonArray jsArray = new JsonArray(countryArray.toArray());
		System.out.println("list*****************" + stateArray);
		if (stateArray.isEmpty()) {
			response.sendRedirect("/error.jsp");
		}
		request.setAttribute("StateList", stateArray);
		RequestDispatcher view = request.getRequestDispatcher("/StateList.jsp");
		// getServletConfig().getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		view.forward(request, response);

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
