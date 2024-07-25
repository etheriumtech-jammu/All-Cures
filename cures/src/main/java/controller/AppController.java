package controller;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import util.HibernateUtil;

import org.springframework.ui.Model;

@Controller
public class AppController {
    
   @GetMapping("/*")
	    public String handleRequest4(Model model) {
	//	 System.out.println("all");
		    String title = "All-Cures - Around health and about it";
        String desc = "Centralized, user-powered platform for bringing information on Alternate Systems of medicine from across the world. Ayurveda, Unani, Persian, Homeopathy";
         model.addAttribute("Title", title);
        model.addAttribute("Description", desc);

	        return "success"; // The view name (success.jsp)
	    }
    
   
	 @GetMapping({"/searchmedicine/medicinetype/*", "searchcategory/disease/*","/webstories","/doctor/*","/user/profile","/searchcures/*","/searchName/*","/search/*","/loginForm/verify","/loginForm/ResetPass/*","/paymentStatus","/statusPayment","/notification/mensi.daily.co/*"})
    public String handleRequest1(Model model) {
	    String title = "All-Cures - Around health and about it";
        String desc = "Centralized, user-powered platform for bringing information on Alternate Systems of medicine from across the world. Ayurveda, Unani, Persian, Homeopathy";
         model.addAttribute("Title", title);
        model.addAttribute("Description", desc);
        return "success"; // The view name (success.jsp)
    }


	@GetMapping("/cure/{id}")
    public String document(@PathVariable String id, Model model) {
    	Session session = HibernateUtil.buildSessionFactory();
        // Default title and description
        String title = "All-Cures - Around health and about it";
        String desc = "Centralized, user-powered platform for bringing information on Alternate Systems of medicine from across the world. Ayurveda, Unani, Persian, Homeopathy";
        
        // Replace dashes with spaces to get the article_id
        String article_id = id.replaceAll("-", " ");
        
        // Create session factory object
        try {
            // Query for article details
            Query query = session.createNativeQuery("SELECT title, description FROM article WHERE article_id = :articleId");
            query.setParameter("articleId", article_id);
            List<Object[]> articleList = query.getResultList();
            // If article is found, update title and description
            if (!articleList.isEmpty()) {
                Object[] obj = articleList.get(0);
                title = obj[0] != null ? (String) obj[0] : title;
		
              if (obj[1] != null && !obj[1].equals("null")) {
                    desc = (String) obj[1];
                }
		
            }
        } catch (Exception e) {
            // Handle any exceptions here
            e.printStackTrace();
        }
	
        // Pass title and description to the view
        model.addAttribute("Title", title);
        model.addAttribute("Description", desc);

        // Return the name of the view (success.jsp)
        return "success";
    }

    
}

