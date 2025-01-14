package dao;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class IntegratedDao {

    private MemcachedClient mcc;

    public IntegratedDao() {
        initializeCacheClient();
    }

    private void initializeCacheClient() {
        try {
            Constant.log("Trying Connection to Memcache server", 0);
            mcc = new MemcachedClient(new ConnectionFactoryBuilder()
                    .setDaemon(true)
                    .setFailureMode(FailureMode.Retry)
                    .build(), AddrUtil.getAddresses(Constant.ADDRESS));
            Constant.log("Connection to Memcache server Successful", 0);
        } catch (IOException e) {
            e.printStackTrace();
            Constant.log("Connection to Memcache server UN-Successful", 3);
        }
    }

    public ArrayList<String> searchUsers(String prefix) {
        try {
            // ✅ Check Memcached for cached results
            ArrayList<String> cachedResult = (ArrayList<String>) mcc.get("search_" + prefix);
            if (cachedResult != null) {
                return cachedResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Fetch from DB if not cached
        ArrayList<String> doctors = fetchDoctorsFromDatabase(prefix);

        // ✅ Only store in Memcached if the result is valid (not null & not empty)
        if (doctors != null && !doctors.isEmpty()) {
            try {
                mcc.set("search_" + prefix, (int) TimeUnit.MINUTES.toSeconds(10), doctors);
                Constant.log("Added to Memcached: " + prefix, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Constant.log("No valid data found for prefix: " + prefix + ". Skipping cache storage.", 1);
        }
        return doctors;
    }

    private ArrayList<String> fetchDoctorsFromDatabase(String prefix) {
    	Session session = HibernateUtil.buildSessionFactory();
        try{
            String sql = "SELECT prefix, docname_first, docname_middle, docname_last " +
                         "FROM Doctors_New WHERE " +
                         "LOWER(docname_first) LIKE LOWER(:prefix) " +
                         "OR LOWER(docname_middle) LIKE LOWER(:prefix) " +
                         "OR LOWER(docname_last) LIKE LOWER(:prefix) " +
                        "ORDER BY CASE WHEN docid <= 63 OR docid >= 14487 THEN 1 ELSE 2 END, docname_first ASC " +
   //   "ORDER BY docname_first ASC " +
                         "LIMIT 10"; // ✅ Prioritize docid ≤ 63 OR docid ≥ 14487
            Query<Object[]> query = session.createNativeQuery(sql);
            query.setParameter("prefix", prefix + "%"); // ✅ Adding % to match prefix
   //         query.setMaxResults(10); // ✅ Limiting results for performance

            List<Object[]> resultList = query.getResultList();
            ArrayList<String> docList = new ArrayList<>();

            for (Object[] row : resultList) {
                String prefixTitle = row[0] != null && !((String) row[0]).trim().isEmpty() ? (String) row[0] + " " : "";
                String firstName = row[1] != null ? (String) row[1] : "";
                String middleName = row[2] != null && !((String) row[2]).trim().isEmpty() ? (String) row[2] + " " : "";
                String lastName = row[3] != null && !((String) row[3]).trim().isEmpty() ? (String) row[3] : "";

                // ✅ Construct full name with prefix
                String fullName = (prefixTitle + firstName + " " + middleName + lastName).trim();

                // ✅ Store full name in list
                docList.add(fullName);
            }

            return docList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    
    public List<String> searchCities(String query) {
        boolean isNumeric = query.matches("\\d+"); // ✅ Check if query contains only digits (PIN)
        Session session = HibernateUtil.buildSessionFactory();
        try  {
            String sql;
            Query<Object[]> dbQuery;

            if (isNumeric) {
                // ✅ Search by PIN
                sql = "SELECT cityname, pincode FROM city WHERE pincode LIKE LOWER(:pin) "  +
                      "ORDER BY CASE WHEN pincode <= 10000 OR pincode >= 90000 THEN 1 ELSE 2 END, cityname ASC LIMIT 10";
                dbQuery = session.createNativeQuery(sql);
                dbQuery.setParameter("pin", query + "%"); // ✅ Convert query to integer safely
            } else {
                // ✅ Search by City Name Prefix
                sql = "SELECT cityname, pincode FROM city WHERE LOWER(cityname) LIKE LOWER(:query) " +
                      "ORDER BY CASE WHEN pincode <= 10000 OR pincode >= 90000 THEN 1 ELSE 2 END, cityname ASC LIMIT 10";
                dbQuery = session.createNativeQuery(sql);
                dbQuery.setParameter("query", query + "%"); // ✅ Add wildcard for prefix search
            }

   //         dbQuery.setMaxResults(10);
            List<Object[]> resultList = dbQuery.getResultList();
            List<String> cityList = new ArrayList<>();
            for (Object[] row : resultList) {
                String cityName = row[0] != null ? (String) row[0] : "";

                int pinCode = 0; // ✅ Default to 0
                if (row[1] != null) {
                    try {
                        pinCode = Integer.parseInt(row[1].toString()); // ✅ Convert safely from String
                    } catch (NumberFormatException e) {
                        pinCode = 0; // ✅ Handle invalid values
                    }
                }

                // ✅ Construct city display string
                String cityDisplay = cityName + " (" + pinCode + ")";
                cityList.add(cityDisplay);
            }

            return cityList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
  
}
