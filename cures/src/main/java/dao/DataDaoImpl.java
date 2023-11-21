package dao;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import util.HibernateUtil;
import java.util.LinkedHashMap;

@Component
public class DataDaoImpl {

	
	public static int addDataDetails(HashMap dataMap) {

		// creating seession factory object
		Session session = HibernateUtil.buildSessionFactory();

		// creating session object
		//Session session = factory;
		// creating transaction object
		session.beginTransaction();

		String device_id = (String) dataMap.get("device_id");
		
		String event_type = (String) dataMap.get("event_type");
		String event_value = (String) dataMap.get("event_value");
		int user_id = (int) dataMap.get("user_id");
//		int date = (int) dataMap.get("date");;
		

		Query query = session
				.createNativeQuery("INSERT INTO `data_store`\r\n" + " (`device_id`,"
						+ " `event_type`," + " `event_value`," + " `user_id`)" 
						+ " VALUES"
						+ " ('" + device_id + "'," + " '"+ event_type +" ' ," +"' "+event_value + "' ," + user_id + ");");
		
//		'" + promo_code + "'," + " '" + promo_start_datetime + "'
		// needs other condition too but unable to find correct column
		int ret = 0;
		try {
			ret = query.executeUpdate();
			session.getTransaction().commit();
			System.out.println("inserted new entry to data_store table for device_id =  " + device_id);

		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			// session.getTransaction().commit();   //session.close();
//			session.getTransaction().commit();   //session.close();
		}
		// session.getTransaction().commit();   //session.close();

		return ret;
	}


	public static int file_upload_NewsLetter(CommonsMultipartFile image) throws IOException {
		Session session = HibernateUtil.buildSessionFactory();
    	int i= 0;
    	String path = System.getProperty( "catalina.base" ) + "/webapps"+ "/cures_articleimages"+"/newsletter_images";
    	
    	
    	String filename = image.getOriginalFilename();//Test_image.jpg
    	
    	
    	String finalPath="/cures_articleimages"+"/newsletter_images" +"/"+ filename;
    	
    	try {
			byte barr[] = image.getBytes();
            i=1;
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();

		} catch (Exception e) {
			System.out.println(e);
		}
    	
    	 
		int ret = 0;
		
		try {
			session.beginTransaction();
			Query query = session.createNativeQuery(
	    			 "INSERT INTO NewsLetter_File_Upload (image) VALUES ('"+finalPath+"');");
			ret = query.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
    	 
    	return ret;
    }

	
	public static List file_Get_NewsLetter() {
		
		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"select image from NewsLetter_File_Upload");
		List<String> results = (List<String>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (String objects : results) {
			hmFinal.add(objects);

		}
		
		
		return hmFinal; 
	}

	public static int file_upload_webStories(HashMap webData,CommonsMultipartFile image) throws IOException {
		Session session = HibernateUtil.buildSessionFactory();
    	int i= 0;
    	String title= "";
    	String description= "";
    	String link= "";
    	String altText= "";
    	

    	String path = System.getProperty( "catalina.base" ) + "/webapps"+ "/cures_articleimages"+"/webstories_images";
    	System.out.println(path);
    	
    	String filename = image.getOriginalFilename();
    	
    	
    	String finalPath= "/cures_articleimages"+"/webstories_images" +"/"+ filename;
    	
    	try {
			byte barr[] = image.getBytes();
            i=1;
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();

		} catch (Exception e) {
			System.out.println(e);
		}
    	
    	if(webData.containsKey("title")||webData.containsKey("description")||webData.containsKey("link")
    			||webData.containsKey("ImageAltText")) {
    		title= (String) webData.get("title");
        	description= (String) webData.get("description");
        	link= (String) webData.get("link");
        	altText= (String) webData.get("ImageAltText");
    	}
    	 
		int ret = 0;
		
		try {
			session.beginTransaction();
			Query query = session.createNativeQuery(
					"INSERT INTO WebStories_Data (Title,Description,Link,image,Alt_Text)"
			    			 + " VALUES"
			    			 + " ('"+title+"','"+description+"','"+link+"'"+","+"'"+finalPath+"'"+","+"'"+altText+"');");
			
			ret = query.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
		    e.printStackTrace();
		} 
		
    	 
    	return ret;
    }
	
	public static List file_Get_webStories() {
		
		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"select Title,Description,Link,image,Alt_Text from WebStories_Data");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, String> hm= new LinkedHashMap<String, String>();
			hm.put("title", (String)objects[0]);
			hm.put("description", (String)objects[1]);
			hm.put("link", (String)objects[2]);
			hm.put("image", (String)objects[3]);
			hm.put("ImageAltText", (String)objects[4]);
			
			hmFinal.add(hm);

		}
		
		return hmFinal; 
	}

	public static List doctor_image() {
		
		Session session = HibernateUtil.buildSessionFactory();

		Query query = session.createNativeQuery(
				"SELECT rowno, img_Loc FROM doctors;");
		List<Object[]> results = (List<Object[]>) query.getResultList();
		System.out.println(results.size());
		List hmFinal = new ArrayList();
		for (Object[] objects : results) {
			LinkedHashMap<String, Object> hm= new LinkedHashMap<String, Object>();
			hm.put("rowno", (Integer)objects[0]);
			hm.put("img_Loc", (String)objects[1]);
			
			hmFinal.add(hm);

		}
		
		return hmFinal; 
		
	}

	


	}
