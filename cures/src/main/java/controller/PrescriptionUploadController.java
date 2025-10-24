package controller;

import service.PrescriptionService;
import dto.AttachmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/prescriptions")
@Validated
public class PrescriptionUploadController {

    private final PrescriptionService service;

    public PrescriptionUploadController(PrescriptionService service) {
        this.service = service;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentResponse> upload(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("appointmentId") @NotNull Integer appointmentId,
            @RequestParam("uploadedBy") @NotNull Integer uploadedBy,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "issuedAt", required = false) String issuedAtIso, // optional ISO
            @RequestParam(value = "followUpDate", required = false) String followUpIso
    ) {
        AttachmentResponse resp = service.handleUpload(appointmentId, uploadedBy, notes, issuedAtIso, followUpIso, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
