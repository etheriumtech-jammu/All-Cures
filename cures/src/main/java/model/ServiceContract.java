package model;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name = "ServiceContractDetails")
public class ServiceContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID")
    private int contractId;

    @Column(name = "ServiceID")
    private int serviceId;

    @Column(name = "UserID")
    private int userId;

    @Column(name = "ContactFirstName", length = 45)
    private String contactFirstName;

    @Column(name = "ContactLastName", length = 45)
    private String contactLastName;

    @Column(name = "CreatedBy")
    private int createdBy;

    @Column(name = "DocumentPath", length = 45)
    private String documentPath;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "Fee", precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(name = "Currency", length = 3)
    private String currency;

    @Column(name = "CreatedDate")
    private Timestamp createdDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp lastUpdatedDate;

    @Column(name = "Status")
    private int status;

    @Column(name = "UpdatedBy")
    private int updatedBy;

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName() {
		return contactLastName;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = Date.valueOf(startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = Date.valueOf(endDate);
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = new BigDecimal(fee);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

    
    // Constructors, getters, and setters

    // Constructors with parameters (if needed)

    // Getters and Setters

    // Ensure to generate getters and setters using your IDE or write them manually based on your requirements.
}
