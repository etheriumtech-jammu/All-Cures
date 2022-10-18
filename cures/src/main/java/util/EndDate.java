package util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.query.Query;


public class EndDate {
	
	public static int updateStatus(String curdate ) {

		Session session = HibernateUtil.buildSessionFactory();

		session.beginTransaction();

	
		Query query = session
				.createNativeQuery("UPDATE orders SET status=0 WHERE End_date  = " + curdate  + ";");
		int ret = 0;
		try {
			ret = query.executeUpdate();
			System.out.println("soft deleteed from orders, where End_date  =  " + curdate );
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {

		}

		return ret;
	}
	
	
}

//
