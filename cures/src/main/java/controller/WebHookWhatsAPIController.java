package controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/webhook")
public class WebHookWhatsAPIController {

	@Value("${allcures.webhookinterakt.key}")
	private String interaktwebookKey;

	@PostMapping("/apisuccess")
	public ResponseEntity<String> paymentSuccess(HttpServletRequest request) throws Exception {
		System.out.println("<<<<<<<<<<<<<<<<<<IN apisuccess 1st line>>>>>>>>>>>>>>>>>>>>>>>>>>>POST");

		String key_secret_verify = "ZWHYR/5vDZI/xyBKDQe6ew==";//interaktwebookKey;
		// ZWHYR/5vDZI/xyBKDQe6ew==
		if (!key_secret_verify.equals((String)request.getParameter("secretKEYinterakt"))) {
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		}

		// do whatever you want to do with the valid paddle request
		System.out.println(request.getParameterMap());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

	@GetMapping("/apisuccess")
	public ResponseEntity<String> paymentSuccessGet(HttpServletRequest request) throws Exception {
		System.out.println("<<<<<<<<<<<<<<<<<<IN apisuccess 1st line>>>>>>>>>>>>>>>>>>>>>>>>>>>GET");

		String key_secret_verify = "ZWHYR/5vDZI/xyBKDQe6ew==";//interaktwebookKey;
		// ZWHYR/5vDZI/xyBKDQe6ew==
		if (!key_secret_verify.equals((String)request.getParameter("secretKEYinterakt"))) {
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
		}

		// do whatever you want to do with the valid paddle request
		System.out.println(request.getParameterMap());
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

//	@RequestMapping(value = "/subscribe/{mobile}", produces = "application/json", method = RequestMethod.POST)
//	public @ResponseBody int subscribe(@PathVariable long mobile, @RequestBody HashMap ns_map) {
//		
//	}
}