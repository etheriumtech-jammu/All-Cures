package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Component;

import util.Constant;
import util.HibernateUtil;

@Component
public class AdminDaoImpl {

	public int createTable(String table, HashMap<String, Object> tableDetails) {
//		Doctor doc = new Doctor();
		Constant.log("Saving New Table in DB", 1);
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
		int ret = 0;
		String insertStr = "INSERT into " + table + " (";
		String insertStr_values = "(";
		try {
//			if(null != tableDetails.get("prefix")) doc.setPrefix((String) tableDetails.get("prefix"));
//			if(null != tableDetails.get("name_first")) doc.setDocname_first((String) tableDetails.get("name_first"));
//			if(null != tableDetails.get("name_last")) doc.setDocname_last((String) tableDetails.get("name_last"));
			// Iterating HashMap through for loop
			for (Map.Entry<String, Object> docDetail : tableDetails.entrySet()) {

				// Printing all elements of a Map
//				System.out.println(docDetail.getKey() + " = " + docDetail.getValue());
				insertStr += docDetail.getKey() + ", ";
				if (docDetail.getValue() instanceof Integer) {
//					Integer new_name = (Integer) docDetail.getValue();
					insertStr_values += (Integer) (docDetail.getValue()) + ", ";
				} else {
					insertStr_values += "'" + (String) (docDetail.getValue()) + "' , ";
				}

			}

			insertStr = insertStr.substring(0, insertStr.lastIndexOf(","));
			insertStr_values = insertStr_values.substring(0, insertStr_values.lastIndexOf(","));
			String completInsertStr = insertStr + ")" + " values " + insertStr_values + " );";
			Constant.log("Saving " + table + " Meta Data", 1);

			Query query = session.createNativeQuery(completInsertStr);

			// needs other condition too but unable to find correct column
			ret = query.executeUpdate();
			session.getTransaction().commit();
//			session.save(doc);
			Constant.log("New " + table + " CREATED in DB", 1);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit(); // session.close();
		}

		return ret;
	}

	public List<Map<String,Object>> fetchTable(String table) {
		// TODO Auto-generated method stub
		Constant.log("Fetch New table details from database", 1);
		Session session = HibernateUtil.buildSessionFactory();
		session.beginTransaction();
//		int ret = 0;
		String fetchStr = "SELECT * from " + table + ";";
//		List hmFinal = new ArrayList();
		List<Map<String,Object>> aliasToValueMapList=null;
		try {
////			if(null != tableDetails.get("prefix")) doc.setPrefix((String) tableDetails.get("prefix"));
////			if(null != tableDetails.get("name_first")) doc.setDocname_first((String) tableDetails.get("name_first"));
////			if(null != tableDetails.get("name_last")) doc.setDocname_last((String) tableDetails.get("name_last"));
//			// Iterating HashMap through for loop
//			for (Map.Entry<String, Object> docDetail : tableDetails.entrySet()) {
//
//				// Printing all elements of a Map
////				System.out.println(docDetail.getKey() + " = " + docDetail.getValue());
//				insertStr += docDetail.getKey() + ", ";
//				if (docDetail.getValue() instanceof Integer) {
////					Integer new_name = (Integer) docDetail.getValue();
//					insertStr_values += (Integer) (docDetail.getValue()) + ", ";
//				}
//				else {
//					insertStr_values += "'"+(String) (docDetail.getValue()) + "' , ";
//				}
//
//			}
//
//			insertStr = insertStr.substring(0,insertStr.lastIndexOf(","));
//			insertStr_values = insertStr_values.substring(0,insertStr_values.lastIndexOf(","));
//			String completInsertStr = insertStr + ")" +" values "+ insertStr_values + " );";
//			Constant.log("Saving "+table+" Meta Data" + completInsertStr, 1);

			Query query = session.createNativeQuery(fetchStr);

			// needs other condition too but unable to find correct column
//			ret = query.executeUpdate();

			List<Object[]> results = (List<Object[]>) query.getResultList();
//			query.getParameterMetadata();
//			System.out.println("result list article@@@@@@@@@@@@@" + results.size());
//			session.getTransaction().commit();   //session.close();
			List<Tuple> result = query.getResultList();
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			aliasToValueMapList=query.list();
//			Map<String, Object> colMap = new HashMap<String, Object>();
//			colMap.put("columnnames", aliasToValueMapList.get(0).keySet());
//			aliasToValueMapList.add(aliasToValueMapList.size(), (Map<String, Object>) colMap);
//			return  aliasToValueMapList.get(0).keySet();

//			System.out.println(aliasToValueMapList.toArray());
			
//			aliasToValueMapList
//			List hmFinal = new ArrayList();
//			for (Object[] objects : results) {
//				HashMap hm = new HashMap();
//
//			    // Get Column Names
////			    List<TupleElement<Object>> elements = row.getElements();
////			    for (TupleElement<Object> element : elements ) {
////			        System.out.println(element.getAlias());
////			    }
//				for (int i = 0; i < objects.length; i++) {
//					String colVal = (String) objects[i];
//					hm.put(colVal + "", colVal);
//
//				}
////				int article_id = (int) objects[0];
////				String title = (String) objects[1];
////
////				hm.put("article_id", article_id);
////				hm.put("title", title);
//
//				hmFinal.add(hm);
////				System.out.println(hm);
//			}//			session.getTransaction().commit();   
			// session.close();

			session.getTransaction().commit();
//			session.save(doc);
			Constant.log("details " + table + " FETCH from DB", 1);
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
//			session.getTransaction().commit(); // session.close();
		}
		return aliasToValueMapList;
	}

}
