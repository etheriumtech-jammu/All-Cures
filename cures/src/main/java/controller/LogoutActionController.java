package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import util.Constant;
import util.CookieManager;

/**
 * Servlet implementation class LogoutActionController
 */
public class LogoutActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutActionController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Destroy the Session Cookie
		CookieManager cMgr = new CookieManager();
		cMgr.destroySessCookie(null, true, request, response);
		
		//Invalidate the Current Server Session
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
		
        String msg= Constant.MSG;
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write("/cures/Login.html?msg="+msg);
		out.flush();
	}
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
