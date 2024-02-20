package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ServicePaymentMethod")
public class ServicePaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServicePaymentMethodID")
    private int ServicePaymentMethodID;

    @Column(name = "ServiceID")
    private int ServiceID;

    @Column(name = "ServicePaymentMasterID")
    private int ServicePaymentMasterID;

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

	@Transient
    private String CreatedName;
    
    @Transient
    private String UpdatedName;
    
    @Transient
    private String ServiceName;
    
    @Transient
    private String PaymentName;
    
	public String getServiceName() {
		return ServiceName;
	}

	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}

	public String getPaymentName() {
		return PaymentName;
	}

	public void setPaymentName(String paymentName) {
		PaymentName = paymentName;
	}

	public String getCreated_Name() {
		return CreatedName;
	}

	public void setCreated_Name(String Created_Name) {
		CreatedName = Created_Name;
	}

	public String getUpdated_Name() {
		return UpdatedName;
	}

	public void setUpdated_Name(String Updated_Name) {
		UpdatedName = Updated_Name;
	}

	public int getServicePaymentMethodID() {
		return ServicePaymentMethodID;
	}

	public void setServicePaymentMethodID(int servicePaymentMethodID) {
		ServicePaymentMethodID = servicePaymentMethodID;
	}

	public int getServiceID() {
		return ServiceID;
	}

	public void setServiceID(int serviceID) {
		ServiceID = serviceID;
	}

	public int getServicePaymentMasterID() {
		return ServicePaymentMasterID;
	}

	public void setServicePaymentMasterID(int servicePaymentMasterID) {
		ServicePaymentMasterID = servicePaymentMasterID;
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

    // Getters and setters...

    // Override toString() for debugging or logging

   
}
