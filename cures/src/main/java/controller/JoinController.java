package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.DailyCoService;

@Controller
@RequestMapping("/notification")

public class JoinController {

	@Autowired
	private DailyCoService dailyCoService;
	
	@GetMapping("/join")
	public String joinWithToken(@RequestParam("room") String roomName,
	                            @RequestParam(value = "userId", required = false) String userId,
	                            @RequestParam(value = "docId", required = false) String docId,
	                            Model model) {

	    String roomUrl = "https://mensi.daily.co/" + roomName;

	    // Decide who’s joining
	    String participantId;
	    String userName;
	    boolean isOwner = false;

	    if (docId != null) {
	        participantId = docId;
	        userName   = "Doctor";     // doctor’s visible name = docId
	        isOwner = true;            // doctors join as owners
	    } else {
	        participantId = userId;
	        userName   = "User";    // user’s visible name = userId
	    }

	    // Mint token JIT with display name
	    String token = dailyCoService.createMeetingToken(
	        roomName,
	        participantId,   // user_id claim
	        userName,     // user_name claim (visible in call)
	        isOwner,
	        600              // TTL in seconds
	    );

	    model.addAttribute("roomUrl", roomUrl);
	    model.addAttribute("token", token);
	    return "join"; // resolves to /WEB-INF/views/join.jsp
	}

}
