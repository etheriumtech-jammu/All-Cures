package controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    
    @GetMapping("/AboutUs")
    public String handleRequest1() {
        return "success"; // The view name (index.jsp)
    }
     @GetMapping("/*")
    public String handleRequest3() {
        return "success"; // The view name (index.jsp)
    }
    @GetMapping("/webstories")
    public String handleRequest2() {
        return "success"; // The view name (index.jsp)
    }
    
}

