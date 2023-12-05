package model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FailureMaster")
public class VideoFailure {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FailureID")
    private int FailureID;
	
	@Column(name = "Reasons")
    private String Reasons;
	
	@Column(name = "Refund")
    private int Refund;
	
	@Column(name = "Penalty")
    private int Penalty;
	
	@Column(name = "CreatedDate")
    private Timestamp CreatedDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp LastUpdatedDate;

    @Column(name = "Status")
    private int Status;

    @Column(name = "CreatedBy")
    private int CreatedBy;

    @Column(name = "UpdatedBy")
    private int UpdatedBy;

	public int getFailureID() {
		return FailureID;
	}

	public void setFailureID(int failureID) {
		FailureID = failureID;
	}

	public String getReasons() {
		return Reasons;
	}

	public void setReasons(String reasons) {
		Reasons = reasons;
	}

	public int getRefund() {
		return Refund;
	}

	public void setRefund(int refund) {
		Refund = refund;
	}

	public int getPenalty() {
		return Penalty;
	}

	public void setPenalty(int penalty) {
		Penalty = penalty;
	}

	public Timestamp getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		CreatedDate = createdDate;
	}

	public Timestamp getLastUpdatedDate() {
		return LastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		LastUpdatedDate = lastUpdatedDate;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}

	public int getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}

	
	
}
