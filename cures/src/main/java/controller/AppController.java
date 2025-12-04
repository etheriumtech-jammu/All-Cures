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
		    String title = "All-Cures: Natural Remedies & Holistic Health Solutions";
        String desc = "Discover expert-backed natural remedies, personalized wellness plans, and traditional medical insights from Ayurveda, Unani, Chinese, Persian, and more. Heal the natural way with All-Cures.";
         model.addAttribute("Title", title + " | All-Cures");
        model.addAttribute("Description", desc + " | All-Cures");

	        return "success"; // The view name (success.jsp)
	    }
    
   
	 @GetMapping({"/doctor/*","/user/profile","/searchcures/*","/searchName/*","/search/*","/loginForm/verify","/doctor-connect/*","/loginForm/ResetPass/*","/paymentStatus","/statusPayment","/notification/mensi.daily.co/*"})
    public String handleRequest1(Model model) {
	    String title = "All-Cures: Natural Remedies & Holistic Health Solutions";
        String desc = "Discover expert-backed natural remedies, personalized wellness plans, and traditional medical insights from Ayurveda, Unani, Chinese, Persian, and more. Heal the natural way with All-Cures.";
         model.addAttribute("Title", title + " | All-Cures");
        model.addAttribute("Description", desc + " | All-Cures");
        return "success"; // The view name (success.jsp)
    }

	 @GetMapping({"/searchmedicine/medicinetype/*"})
	    public String trendingCures(Model model) {
		    String title = "Natural Healing Cures & Traditional Wellness Therapies | All-Cures";
	        String desc = "Explore time-tested healing practices, herbal remedies, and wellness traditions from around the world—Ayurveda, Chinese medicine, Unani, and more. Your trusted source for holistic health at All-Cures";
	         model.addAttribute("Title", title);
	        model.addAttribute("Description", desc);
	        return "success"; // The view name (success.jsp)
	    }

	 @GetMapping("/searchcategory/disease/{diseaseSlug}")
	 public String categories(@PathVariable String diseaseSlug, Model model) {
	     // Step 1: Extract readable disease name from slug
	     String[] parts = diseaseSlug.split("-", 2); // split only at first hyphen
	     String diseaseNameRaw = parts.length > 1 ? parts[1] : diseaseSlug;

	     // Step 2: Convert to Title Case (e.g., "blood-disorders" → "Blood Disorders")
	     String[] words = diseaseNameRaw.split("-");
	     StringBuilder formattedName = new StringBuilder();
	     for (String word : words) {
	         if (!word.isEmpty()) {
	             formattedName.append(Character.toUpperCase(word.charAt(0)))
	                          .append(word.substring(1))
	                          .append(" ");
	         }
	     }
	     String h1 = formattedName.toString().trim();

	     // Step 3: Inject H1 into templates
	     String title = h1 + " | Natural Remedies, Causes, and Treatments | All-Cures";
	     String desc = "Explore effective, time-tested natural remedies and holistic treatments for " + h1 +
	                   ". Learn how herbs, diet, and alternative therapies can support your healing journey at All-Cures.";

	     // Step 4: Pass to model
	     model.addAttribute("Title", title);
	     model.addAttribute("Description", desc);

	     return "success"; // view name (success.jsp or success.html)
	 }


	@GetMapping("/cure/{id}")
    public String document(@PathVariable String id, Model model) {
    	Session session = HibernateUtil.buildSessionFactory();
        // Default title and description
        String title = "All-Cures: Natural Remedies & Holistic Health Solutions";
        String desc = "Discover expert-backed natural remedies, personalized wellness plans, and traditional medical insights from Ayurveda, Unani, Chinese, Persian, and more. Heal the natural way with All-Cures.";
        
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
        model.addAttribute("Title", title + " | All-Cures");
        model.addAttribute("Description", desc + " | All-Cures");

        // Return the name of the view (success.jsp)
        return "success";
    }

    
}
