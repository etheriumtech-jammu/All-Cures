package exception;
import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	  @ExceptionHandler(OtpException.class)
	    public ResponseEntity<?> handleOtpException(OtpException ex) {
		  System.out.println("Handling OtpException: " + ex.getMessage());
	        ErrorResponse error = new ErrorResponse(
	                "OTP_ERROR",
	                ex.getMessage()
	        );

	        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<?> handleGenericException(Exception ex) {

	        ErrorResponse error = new ErrorResponse(
	                "INTERNAL_ERROR",
	                "Something went wrong"
	        );

	        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
//	    @PostConstruct
//	    public void init() {
//	        System.out.println("GlobalExceptionHandler Loaded");
//	    }
}
