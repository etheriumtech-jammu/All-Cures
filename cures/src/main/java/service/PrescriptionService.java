package service;

import dto.AttachmentResponse;
import model.Appointment;
import model.Prescription;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import util.HibernateUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PrescriptionService
 *
 * - saves file via FileStorageService - persists Prescription via Hibernate
 * Session/Transaction (using a shared SessionFactory) - sends email via
 * PrescriptionEmailService after successful commit
 */
@Service
public class PrescriptionService {

	private static final Logger LOGGER = Logger.getLogger(PrescriptionService.class.getName());

	private final FileStorageService storage;
	private final PrescriptionEmailService prescriptionEmailService;

	public PrescriptionService(FileStorageService storage, PrescriptionEmailService prescriptionEmailService) {
		this.storage = storage;
		this.prescriptionEmailService = prescriptionEmailService;

	}

	/**
	 * Handles upload: save file -> persist prescription -> send email.
	 *
	 * @return AttachmentResponse with generated presId and filePath
	 */
	public AttachmentResponse handleUpload(Integer appointmentId, Integer uploadedBy, String notes, String issuedAtIso,
			String followUpIso, MultipartFile file) {

		validateFile(file);

		// Save file first
		FileStorageResult saved = storage.save(file);
		String filePath = saved.getPath();

		Session session = null;
		Transaction tx = null;
		Integer generatedId = null;

		try {
			session = HibernateUtil.buildSessionFactory();
			tx = session.beginTransaction();

			// 1) Validate appointment exists
			Appointment appointment = session.get(Appointment.class, appointmentId);
			if (appointment == null) {
				throw new IllegalArgumentException("Appointment not found for id: " + appointmentId);
			}

			// 2) Build entity
			Prescription p = new Prescription();
			p.setAppointmentId(appointmentId);
			p.setFilePath(filePath);
			p.setOriginalName(file.getOriginalFilename());
			p.setNotes(notes);

			if (StringUtils.hasText(issuedAtIso)) {
				p.setIssuedAt(parseIsoToLocalDateTime(issuedAtIso));
			}
			if (StringUtils.hasText(followUpIso)) {
				p.setFollowUpDate(parseIsoToLocalDateTime(followUpIso));
			}

			p.setUploadedBy(uploadedBy);
			p.setStatus("ACTIVE");

			// 3) Persist using Hibernate Session / Transaction
			Object idObj = session.save(p); // returns Serializable (Number for IDENTITY)
			session.flush();

			// commit transaction (ensures DB insert is durable)
			tx.commit();

			// convert id to Integer
			if (idObj instanceof Number) {
				generatedId = ((Number) idObj).intValue();
				System.out.print(generatedId);
			} else {
				try {
					Object idFromEntity = session.getIdentifier(p);
					generatedId = (idFromEntity instanceof Number) ? ((Number) idFromEntity).intValue() : null;
					System.out.print(generatedId);
				} catch (Exception ex) {
					LOGGER.log(Level.WARNING, "Could not obtain generated id", ex);
				}
			}

			if (generatedId == null) {
				// fallback to getter if your entity exposes it
				try {
					generatedId = p.getPresId();
				} catch (Exception ex) {
					LOGGER.log(Level.SEVERE, "Generated id unavailable", ex);
					throw new RuntimeException("Failed to obtain generated id after insert");
				}
			}

			// ... after tx.commit() and generatedId resolution

			// 4) Lookup patient and doctor details (use same session if still open)
			String patientEmail = null;
			String patientName = null;
			String doctorName = null;

			try {
				if (appointmentId != null) {
					// Use the already fetched appointment object
					if (appointment == null) {
						LOGGER.warning("Could not find Appointment for id=" + appointmentId);
					} else {
						Integer patientId = appointment.getUserID(); // model has userID
						Integer docId = appointment.getDocID(); // model has docID

						// --- Native query: registration table for patient email + names ---
						if (patientId != null) {
							try {
								String patientSql = "SELECT email_address, first_name, last_name FROM registration WHERE registration_id = :uid LIMIT 1";
								@SuppressWarnings("unchecked")
								java.util.List<Object[]> patientRows = session.createNativeQuery(patientSql)
									.setParameter("uid", patientId).getResultList();

								if (patientRows != null && !patientRows.isEmpty()) {
									Object[] row = patientRows.get(0);
									if (row.length > 0 && row[0] != null)
										patientEmail = String.valueOf(row[0]).trim();
									String pFirst = (row.length > 1 && row[1] != null) ? String.valueOf(row[1]).trim()
										: "";
									String pLast = (row.length > 2 && row[2] != null) ? String.valueOf(row[2]).trim()
										: "";
									String combined = (pFirst + " " + pLast).trim();
									patientName = combined.isEmpty() ? null : combined;
								} else {
									LOGGER.warning("No registration row found for registration_id=" + patientId);
								}
							} catch (Exception e) {
								LOGGER.log(Level.WARNING,
									"Failed to query registration table for registration_id=" + patientId, e);
							}
						} else {
							LOGGER.warning("Appointment.userID is null for appointmentId=" + appointmentId);
						}

						// --- Native query: doctors_new table for doctor name/username ---
						if (docId != null) {
							try {
								String docSql = "SELECT prefix, docname_first, docname_middle, docname_last FROM doctors_new WHERE docid = :did LIMIT 1";
								@SuppressWarnings("unchecked")
								java.util.List<Object[]> doctorRows = session.createNativeQuery(docSql)
									.setParameter("did", docId).getResultList();

								if (doctorRows != null && !doctorRows.isEmpty()) {
									Object[] drow = doctorRows.get(0);

									String prefix = (drow.length > 0 && drow[0] != null)
										? String.valueOf(drow[0]).trim()
										: "";
									String dFirst = (drow.length > 1 && drow[1] != null)
										? String.valueOf(drow[1]).trim()
										: "";
									String dMiddle = (drow.length > 2 && drow[2] != null)
										? String.valueOf(drow[2]).trim()
										: "";
									String dLast = (drow.length > 3 && drow[3] != null) ? String.valueOf(drow[3]).trim()
										: "";

									// Build a friendly doctor name: include prefix and middle name if present
									StringBuilder nameBuilder = new StringBuilder();
									if (!prefix.isEmpty())
										nameBuilder.append(prefix).append(" ");
									if (!dFirst.isEmpty())
										nameBuilder.append(dFirst).append(" ");
									if (!dMiddle.isEmpty())
										nameBuilder.append(dMiddle).append(" ");
									if (!dLast.isEmpty())
										nameBuilder.append(dLast);
									String candidateName = nameBuilder.toString().trim();
									doctorName = candidateName.isEmpty() ? null : candidateName;
								} else {
									LOGGER.warning("No doctors_new row found for docid=" + docId);
								}
							} catch (Exception e) {
								LOGGER.log(Level.WARNING, "Failed to query doctors_new table for docid=" + docId, e);
							}
						} else {
							LOGGER.warning("Appointment.docID is null for appointmentId=" + appointmentId);
						}
					}
				} else {
					LOGGER.warning("appointmentId is null; skipping patient/doctor lookup");
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Failed to lookup appointment/patient/doctor", e);
			}

			// Ensure patientEmail exists
			if (patientEmail == null || patientEmail.isBlank()) {
				LOGGER.warning(
						"Patient email not found for appointmentId=" + appointmentId + ". Email will not be sent.");
			} else {
				try {
					boolean sent = prescriptionEmailService.sendPrescriptionEmail(patientEmail, // patient email
							patientName, // patient name (nullable)
							doctorName, // doctor display name (nullable)
							followUpIso,
							notes, filePath, file.getOriginalFilename());
					if (!sent) {
						LOGGER.log(Level.WARNING,
								"Prescription email was not sent (sendPrescriptionEmail returned false) for presId="
									+ generatedId);
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Failed to send notification email", e);
				}
			}

			String nowIso = Instant.now().toString();
			return new AttachmentResponse(generatedId, filePath, nowIso);

		} catch (RuntimeException ex) {
			// rollback if tx active
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
				} catch (Exception rbe) {
					LOGGER.log(Level.SEVERE, "Rollback failed", rbe);
				}
			}

			// delete saved file to avoid orphaning
			try {
				storage.delete(filePath);
			} catch (Exception delEx) {
				LOGGER.log(Level.WARNING, "Failed to delete file after DB failure: " + filePath, delEx);
			}

			throw ex;
		} 
	}

	/**
	 * Updates an existing prescription. Allows updating notes, followUpDate, issuedAt, and file.
	 * @param presId The prescription ID to update
	 * @param notes Updated notes (nullable)
	 * @param issuedAtIso Updated issuedAt in ISO format (nullable)
	 * @param followUpIso Updated followUpDate in ISO format (nullable)
	 * @param file New file to replace the old one (nullable)
	 * @return The updated Prescription entity
	 */
	public Prescription updatePrescription(Integer presId, String notes, String issuedAtIso, String followUpIso, MultipartFile file) {
		Session session = null;
		Transaction tx = null;
		Prescription prescription = null;
		try {
			session = HibernateUtil.buildSessionFactory();
			tx = session.beginTransaction();

			prescription = session.get(Prescription.class, presId);
			if (prescription == null) {
				throw new IllegalArgumentException("Prescription not found for id: " + presId);
			}

			// Validate appointment exists before updating prescription
			if (prescription != null) {
				Appointment appointment = session.get(Appointment.class, prescription.getAppointmentId());
				if (appointment == null) {
					throw new IllegalArgumentException("Appointment not found for id: " + prescription.getAppointmentId());
				}
			}

			if (notes != null) prescription.setNotes(notes);
			if (StringUtils.hasText(issuedAtIso)) prescription.setIssuedAt(parseIsoToLocalDateTime(issuedAtIso));
			if (StringUtils.hasText(followUpIso)) prescription.setFollowUpDate(parseIsoToLocalDateTime(followUpIso));

			if (file != null && !file.isEmpty()) {
				validateFile(file);
				// Delete old file if needed
				String oldFilePath = prescription.getFilePath();
				if (oldFilePath != null) {
					try { storage.delete(oldFilePath); } catch (Exception ex) { LOGGER.log(Level.WARNING, "Failed to delete old file: " + oldFilePath, ex); }
				}
				FileStorageResult saved = storage.save(file);
				prescription.setFilePath(saved.getPath());
				prescription.setOriginalName(file.getOriginalFilename());
			}

			session.update(prescription);
			tx.commit();
		} catch (RuntimeException ex) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw ex;
		}
		return prescription;
	}

	private void validateFile(MultipartFile file) {
		if (file == null || file.isEmpty())
			throw new IllegalArgumentException("File required");
		String ct = file.getContentType();
		if (ct == null || !(ct.startsWith("image/") || ct.equals("application/pdf")))
			throw new IllegalArgumentException("Only image or PDF allowed");
		long max = 10L * 1024 * 1024;
		if (file.getSize() > max)
			throw new IllegalArgumentException("File too large (max 10MB)");
	}

	private LocalDateTime parseIsoToLocalDateTime(String iso) {
	    if (!StringUtils.hasText(iso)) return null;

	    try {
	        // Full ISO date-time with zone, e.g. 2025-11-01T09:00:00Z
	        return LocalDateTime.ofInstant(Instant.parse(iso), ZoneOffset.UTC);
	    } catch (DateTimeParseException e1) {
	        try {
	            // LocalDateTime without zone, e.g. 2025-11-01T09:00
	            return LocalDateTime.parse(iso);
	        } catch (Exception e2) {
	            try {
	                // Date-only format, e.g. 2025-11-01 → default time 00:00
	                return LocalDateTime.of(java.time.LocalDate.parse(iso), java.time.LocalTime.MIDNIGHT);
	            } catch (Exception e3) {
	                throw new IllegalArgumentException("Invalid date format: " + iso);
	            }
	        }
	    }
	}

}
