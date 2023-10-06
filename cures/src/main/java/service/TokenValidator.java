package service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class TokenValidator {
    private static final String STATIC_TOKEN = "All-Cures";

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

            for (Object[] list : results) {
                Integer TokenID = (Integer) list[0];
                String Token = (String) list[1];
                Integer Status = (Integer) list[2];
                Integer Max_Allowed = (Integer) (list[3] == null ? 0 : list[3]);
                Integer Total_Count = (Integer) (list[4] == null ? 0 : list[4]);
                Date LastUpdateDate = (Date) list[5];

                if (token.equals(Token) && Status == 1) {
                    if (Max_Allowed == Total_Count && false == current_Date.equals(LastUpdateDate)) {
                        TokenValidationInterceptor.url=url;
                    	TokenValidationInterceptor.tokenID=TokenID;
                    	TokenValidationInterceptor.totalCount=Total_Count;
                    	TokenValidationInterceptor.toDo=1;
                    	TokenValidationInterceptor.session=session;
                    	TokenValidationInterceptor.whenTORun=true;
                        res = 1;
                    } else if (Max_Allowed == Total_Count && true == current_Date.equals(LastUpdateDate)) {
                        res = 2;
                    } else if (Max_Allowed == 0) {
                        res = 1;
                    } else if (Max_Allowed > Total_Count) {
                        TokenValidationInterceptor.url=url;
                    	TokenValidationInterceptor.tokenID=TokenID;
                    	TokenValidationInterceptor.totalCount=Total_Count;
                    	TokenValidationInterceptor.toDo=2;
                    	TokenValidationInterceptor.session=session;
                    	TokenValidationInterceptor.whenTORun=true;
                        res = 1;
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

    @Async
    public static int updateTotalCount(String url, Integer tokenID, Integer totalCount, int toDo, Session session) {
        session.beginTransaction();
        int ret = 0;
        String str = "";
	System.out.println("Asynchronous Call");
        if (totalCount == 0) {
            str = "UPDATE APITokenAnalytics SET Total_Count = 1 WHERE TokenID = :tokenID AND API = :url";
            toDo = 0;
        } else if (toDo == 1) {
            str = "UPDATE APITokenAnalytics SET Total_Count = 1 WHERE TokenID = :tokenID AND API = :url";
        } else if (toDo == 2) {
            str = "UPDATE APITokenAnalytics SET Total_Count = Total_Count + 1 WHERE TokenID = :tokenID AND API = :url";
        }

        try {
            Query<?> query = session.createNativeQuery(str)
                    .setParameter("tokenID", tokenID)
                    .setParameter("url", url);
            ret = query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }

        return ret;
    }

    public static Date generatingCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = currentDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }
}
