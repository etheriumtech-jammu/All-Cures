package service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.sql.Date;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import javax.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import java.util.concurrent.atomic.AtomicInteger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class TokenValidator {
	private static final String STATIC_TOKEN = "All-Cures";
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public static int isValidToken(String token, String url) {
		if (token.startsWith("Bearer ")) {
			token = token.substring("Bearer ".length());
		}

		System.out.println("Token: " + token);
		Session session = HibernateUtil.buildSessionFactory();
		int res = 0;

		Date current_Date = generatingCurrentDate();
		@SuppressWarnings("unchecked")
		Query<Object[]> query = session.createNativeQuery(
						"SELECT at.TokenID, at.Token, at.Status, at.Max_Allowed, aa.Total_Count, aa.LastUpdateDate FROM API_Tokens at JOIN APITokenAnalytics aa ON at.TokenID = aa.TokenID WHERE at.Token = :token AND aa.API = :url")
				.setParameter("token", token)
				.setParameter("url", url);

		try {
			List<Object[]> results = query.getResultList();
			if(results.size()>0) {
			for (Object[] list : results) {
				Integer TokenID = (Integer) list[0];
				String Token = (String) list[1];
				Integer Status = (Integer) list[2];
				Integer Max_Allowed = (Integer) (list[3] == null ? 0 : list[3]);
				// Integer Total_Count = (Integer) (list[4] == null ? 0 : list[4]);
				AtomicInteger Total_Count = new AtomicInteger((Integer) (list[4] == null ? 0 : list[4]));
				Date LastUpdateDate = (Date) list[5];

				if (token.equals(Token) && Status == 1) {
					
					
					if (Max_Allowed == Total_Count.get() && false == current_Date.equals(LastUpdateDate)) {
						session.beginTransaction();
						String str = "UPDATE APITokenAnalytics SET Total_Count = 1 "
								+ "WHERE TokenID = :tokenID AND API = :url";
						try {
							Query<?> query1 = session.createNativeQuery(str)
									.setParameter("tokenID", TokenID)
									.setParameter("url", url);
							res = query1.executeUpdate();
							session.getTransaction().commit();
						} catch (Exception e) {
							e.printStackTrace();
							session.getTransaction().rollback();
						}
						}
					 else if (Max_Allowed == Total_Count.get() && true == current_Date.equals(LastUpdateDate)) {
						res = 2;
					} else if (Max_Allowed == 0) {
						res = 1;
					} else if (Max_Allowed > Total_Count.get()) {
						session.beginTransaction();
						String str = "UPDATE APITokenAnalytics SET Total_Count = Total_Count + 1 "
								+ "WHERE TokenID = :tokenID AND API = :url";
						try {
							Query<?> query1 = session.createNativeQuery(str)
									.setParameter("tokenID", TokenID)
									.setParameter("url", url);
							res = query1.executeUpdate();
							session.getTransaction().commit();
						} catch (Exception e) {
							e.printStackTrace();
							session.getTransaction().rollback();
						}
					}
				}
			}
			}
		} catch (NoResultException e) {
			System.out.println("No Entry");
		} finally {
			//         session.close(); // Close the session when done
		}

		return res;
	}

	public static Date generatingCurrentDate() {
		LocalDate localDate=LocalDate.now();
		return Date.valueOf(localDate);
	}
}
