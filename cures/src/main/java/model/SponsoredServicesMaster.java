package model;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "SponsoredServicesMaster")
public class SponsoredServicesMaster {

    public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public int isPaymentReq() {
		return paymentReq;
	}

	public void setPaymentReq(int paymentReq) {
		this.paymentReq = paymentReq;
	}

	public int isContractReq() {
		return contractReq;
	}

	public void setContractReq(int contractReq) {
		this.contractReq = contractReq;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServiceID")
    private int serviceId;

    @Column(name = "ServiceName", length = 45)
    private String serviceName;

    @Column(name = "ServiceDesc", columnDefinition = "TEXT")
    private String serviceDesc;

    @Column(name = "PaymentReq")
    private int paymentReq;

    @Column(name = "ContractReq")
    private int contractReq;

    @Column(name = "AvailabilityReq")
    private int availabilityReq;

    @Column(name = "CreatedBy")
    private int createdBy;

    @Column(name = "Status")
    private int status;

    
    @Column(name = "CreatedDate")
    private Timestamp createdDate;

    
    @Column(name = "LastUpdatedDate")
    private Timestamp lastUpdatedDate;
    
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

	public int getPaymentReq() {
		return paymentReq;
	}

	public int getContractReq() {
		return contractReq;
	}

	@Column(name = "UpdatedBy")
    private int updatedBy;

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Transient
    private String CreatedName;
    
    @Transient
    private String UpdatedName;
    
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

	
	public int getAvailabilityReq() {
		return availabilityReq;
	}

	public void setAvailabilityReq(int availabilityReq) {
		this.availabilityReq = availabilityReq;
	}
    // Constructors, getters, and setters

    // Constructors with parameters (if needed)

    // Getters and Setters
}
