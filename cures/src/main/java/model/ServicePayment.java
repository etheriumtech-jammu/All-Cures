package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ServicePaymentMaster")
public class ServicePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServicePaymentMasterID")
    private int ServicePaymentMasterID;

    @Column(name = "ServicePaymentMasterName", length = 45)
    private String ServicePaymentMasterName;

    @Column(name = "ServicePaymentDesc", columnDefinition = "TEXT")
    private String ServicePaymentDesc;

    @Column(name = "CreatedBy")
    private int CreatedBy;

    @Column(name = "CreatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate;

    @Column(name = "LastUpdatedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date LastUpdatedDate;

    @Column(name = "Status")
    private int Status;

    @Column(name = "UpdatedBy")
    private Integer UpdatedBy; // Assuming it can be null

	public int getServicePaymentMasterID() {
		return ServicePaymentMasterID;
	}

	public void setServicePaymentMasterID(int servicePaymentMasterID) {
		ServicePaymentMasterID = servicePaymentMasterID;
	}

	public String getServicePaymentMasterName() {
		return ServicePaymentMasterName;
	}

	public void setServicePaymentMasterName(String servicePaymentMasterName) {
		this.ServicePaymentMasterName = servicePaymentMasterName;
	}

	public String getServicePaymentDesc() {
		return ServicePaymentDesc;
	}

	public void setServicePaymentDesc(String servicePaymentDesc) {
		ServicePaymentDesc = servicePaymentDesc;
	}

	public int getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return LastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		LastUpdatedDate = lastUpdatedDate;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Integer getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		UpdatedBy = updatedBy;
	}

    // Constructors, getters, setters, and other methods

    
    // Getters and setters...

    // Override toString() for debugging or logging

    
}
