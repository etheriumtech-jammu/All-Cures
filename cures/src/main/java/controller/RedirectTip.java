package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

@WebServlet("/view/article")
public class RedirectTip extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	Integer id = null;
    	String title = null;
    	Session session = HibernateUtil.buildSessionFactory();
    	Query query1= session.createNativeQuery("SELECT tip.article_id,article.title\r\n"
    			+ "FROM  tip inner join article where tip.article_id=article.article_id\r\n"
    			+ "ORDER BY tip_date DESC\r\n"
    			+ "LIMIT 1; ");
		List<Object[]> results = (List<Object[]>) query1.getResultList();
		
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm = new LinkedHashMap<>();
			// add linkedhashmap to preserve the order
			 id=(Integer) objects[0];
			 title = (String) objects[1];
		}
		
    	System.out.println(id+title);
    	// Set the desired URL to redirect to
    	String redirectURL="https://all-cures.com/cure/" + id + "-" + title;
        

        // Perform the redirection
        response.sendRedirect(redirectURL);
    }
}
