package exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// =========================================
	// OTP / BUSINESS EXCEPTIONS
	// =========================================
	@ExceptionHandler(OtpException.class)
	public ResponseEntity<ErrorResponse> handleOtpException(OtpException ex) {

		System.out.println("Handling OtpException: " + ex.getMessage());

		ErrorResponse error = new ErrorResponse(
				"OTP_ERROR",
				ex.getMessage()
		);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// =========================================
	// RUNTIME EXCEPTIONS
	// =========================================
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

		ErrorResponse error = new ErrorResponse(
				"RUNTIME_ERROR",
				ex.getMessage()
		);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// =========================================
	// GENERIC / UNKNOWN EXCEPTIONS
	// =========================================
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse(
				"INTERNAL_ERROR",
				"Something went wrong"
		);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}