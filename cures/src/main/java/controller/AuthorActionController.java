package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import dao.AuthorDaoImpl;
import util.Constant;
import util.SolrUtil;
import model.Author;

public class AuthorActionController  extends HttpServlet {
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Author> authors = null;
		String jsonAuthors = null;
		Constant.log("In Author Action Controller", 0);
		
		boolean ajaxCall = true;
		String action = request.getParameter("action");
		PrintWriter out = response.getWriter();
		String finalAction = "";
		if(action != null && !"".equals(action.trim())){
			ajaxCall = false;
			finalAction = action;
		}else{
			finalAction = request.getParameter("cmd");
		}		
		int status = -1;

		if(finalAction != null && finalAction.equalsIgnoreCase("getAuthors")){
			Constant.log("Getting Authors", 0);
			authors = getAuthors(request);			
		}else if(finalAction != null && finalAction.equalsIgnoreCase("getAuthorInfo")){
			Constant.log("Getting Author Info", 0);
			authors = getAuthors(request);			
		}else if(finalAction != null && finalAction.equalsIgnoreCase("getTopAuthors")){
			Constant.log("Getting Top Authors", 0);
			authors = getAuthors(request);			
		}else if(finalAction != null && finalAction.equalsIgnoreCase("createAuthor")){
			Constant.log("Creating New Author", 0);
			authors = createAuthor(request);
		}
		
		if(ajaxCall){
			if(authors != null){
				jsonAuthors = SolrUtil.jsonifyObject(authors);			
				status = authors.size();
			}else{
				jsonAuthors = "errMsg=No Authors Found&status="+status;
			}
			out.write(jsonAuthors);
			out.flush();
		}else{
			if(authors != null){
				this.getServletContext().setAttribute("authors", authors);
				status = authors.size();
				response.sendRedirect("/cures/authors.jsp?status="+status);
			}else{
				response.sendRedirect("/cures/authors.jsp?errMsg=No Authors Found&status="+status);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public List<Author> getAuthors(HttpServletRequest request){
		int iAuthorId = -1;
		int iNumAuths = -1;
		int iAuthStatus = -1;
		
		String authorId = request.getParameter("authorId");
		Constant.log("AuthorId:"+authorId, 0);
		if(authorId != null && !authorId.equals("")){			
			iAuthorId = Integer.parseInt(authorId);
		}		
		String numAuthors = request.getParameter("numAuthors");		
		Constant.log("Num of Authors:"+numAuthors, 0);
		if(numAuthors != null && !numAuthors.equals("")){
			iNumAuths = Integer.parseInt(numAuthors);
		}		
		String authStatus = request.getParameter("authStatus");		
		Constant.log("AuthorStatus:"+authStatus, 0);
		if(authStatus != null && !authStatus.equals("")){
			iAuthStatus = Integer.parseInt(authStatus);
		}
		AuthorDaoImpl authDao = new AuthorDaoImpl();				
		List authors = authDao.getAuthors(iAuthorId, iNumAuths, iAuthStatus);
		return authors;
	}
	
	public List<Author> createAuthor(HttpServletRequest request){
		String authorFName = request.getParameter("authorFN");
		Constant.log("Author FN:"+authorFName, 0);
		
		String authorLName = request.getParameter("authorLN");
		Constant.log("Author LN:"+authorLName, 0);
		
		String authorMName = request.getParameter("authorMN");
		Constant.log("Author MN:"+authorMName, 0);
		
		String authorAddr = request.getParameter("authorAddr");
		Constant.log("Author Addr:"+authorAddr, 0);
		
		String authorTel = request.getParameter("authorTel");
		Constant.log("Author Tel:"+authorTel, 0);
		
		String authorEmail = request.getParameter("authorEmail");
		Constant.log("Author Email:"+authorEmail, 0);
		
		String authStatus = request.getParameter("authStatus");		
		Constant.log("AuthorStatus:"+authStatus, 0);
		int iAuthStatus = 1;
		if(authStatus != null && !authStatus.equals("")){
			iAuthStatus = Integer.parseInt(authStatus);
		}
		AuthorDaoImpl authDao = new AuthorDaoImpl();				
		Author author = authDao.createAuthor(authorFName, authorMName, authorLName, authorEmail, authorAddr, authorTel, iAuthStatus);
		if(author != null){
			List<Author> authList = new ArrayList<Author>();
			authList.add(author);
			return authList;
		}else{
			return null;
		}
	}

}
