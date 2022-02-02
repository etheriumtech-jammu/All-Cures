package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import model.Article;
import model.Author;
import model.City;
import util.ArticleUtils;
import util.Constant;
import util.HibernateUtil;

public class AuthorDaoImpl {
	
	public List<Author> getAuthors(int authorId, int numAuthors, int authStatus){
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		Transaction trans =(Transaction)session.beginTransaction();		
		boolean whereClauseAdded = false;
		String qryTxt = "select author_id, author_firstname, author_lastname, author_middlename, author_email, author_status from author ";
		if(authorId > 0){
			Constant.log("Constraining By Author Id:"+authorId, 0);
			if(whereClauseAdded)
				qryTxt += " and author_Id = "+authorId;
			else
				qryTxt += " where author_Id = "+authorId;
		}
		if(authStatus > 0){
			Constant.log("Constraining by Author Status:"+authStatus, 0);
			if(whereClauseAdded)
				qryTxt += " and author_status = "+authStatus;
			else
				qryTxt += " where author_status = "+authStatus;
		}
		if(numAuthors > 0){
			Constant.log("Constraining by Number of Authors:"+numAuthors, 0);
			qryTxt += " limit "+numAuthors;
		}
		Constant.log("Final Query:"+qryTxt, 0);
		Query query = session.createNativeQuery(qryTxt);
		ArrayList<Author> authList = null;
		try{
			authList = (ArrayList<Author>)query.getResultList();			
		}catch(Exception e){
			e.printStackTrace();
			Constant.log("Error while Getting Authors ", 3);
		}
		finally {
			session.getTransaction().commit();   //session.close();
		}
		return authList;
	}
	
	public List<Author> getTopAuthors(int numAuthors, int authStatus){
		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;
		// creating transaction object
		Transaction trans =(Transaction)session.beginTransaction();		
		boolean whereClauseAdded = false;
		//Ideally Authors have a rating column and the results are sorted by that rating column
		String qryTxt = "select author_id, author_firstname, author_lastname, author_middlename, author_email, author_status from author ";		
		if(authStatus > 0){
			if(whereClauseAdded)
				qryTxt += " and author_status = "+authStatus;
			else
				qryTxt += " where author_status = "+authStatus;
		}
		if(numAuthors > 0){			
				qryTxt += " limit "+numAuthors;
		}
		//qryTxt += " order by author_rating desc";
		Query query = session.createNativeQuery(qryTxt+";");
		ArrayList<Author> authList = null;
		try{
			authList = (ArrayList<Author>)query.getResultList();			
		}catch(Exception e){
			e.printStackTrace();
			Constant.log("Error while Getting Top Authors ", 3);
		}finally {
			session.getTransaction().commit();   //session.close();
		}
		return authList;		
	}
	
	public Author createAuthor(String fName, String mName, String lName, String aEmail, String aAddress, String aTel, Integer aStatus){
		Author createdAuthor = new Author();		
		Constant.log("Saving New Author in DB", 1);
		Session session = HibernateUtil.buildSessionFactory();
		// creating session object
		//Session session = factory;		

		session.beginTransaction();

		try {
			createdAuthor.setAuthor_firstname(fName);
			createdAuthor.setAuthor_address(aAddress);
			createdAuthor.setAuthor_email(aEmail);
			createdAuthor.setAuthor_lastname(lName);
			createdAuthor.setAuthor_middlename(mName);
			createdAuthor.setAuthor_telephone(aTel);
			createdAuthor.setAuthor_status(aStatus);
			Constant.log("Saving Author Meta Data", 1);
			session.save(createdAuthor);
//			session.getTransaction().commit();
			Constant.log("New Author CREATED in DB", 1);
			session.getTransaction().commit();   //session.close();			
		}catch (Exception e) {
			e.printStackTrace();
			createdAuthor = null;
//			session.getTransaction().commit(); //session.getTransaction().rollback();
			session.getTransaction().rollback();
		}finally {
			session.getTransaction().commit();   //session.close();
		}
		
		return createdAuthor;		
	}

}
