package model;

import java.sql.Time;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

@Entity
@Table(name = "DoctorAvailability")
public class AvailabilitySchedule {

    public int getContractId() {
		return ContractID;
	}

	public void setContractId(int ContractID) {
		this.ContractID = ContractID;
	}

	public int getMonAvailability() {
		return MonAvailability;
	}

	public void setMonAvailability(int monAvailability) {
		this.MonAvailability = monAvailability;
	}

	public int getTueAvailability() {
		return TueAvailability;
	}

	public void setTueAvailability(int tueAvailability) {
		this.TueAvailability = tueAvailability;
	}

	public int getWedAvailability() {
		return WedAvailability;
	}

	public void setWedAvailability(int wedAvailability) {
		this.WedAvailability = wedAvailability;
	}

	public int getThuAvailability() {
		return ThuAvailability;
	}

	public void setThuAvailability(int thuAvailability) {
		this.ThuAvailability = thuAvailability;
	}

	public int getFriAvailability() {
		return FriAvailability;
	}

	public void setFriAvailability(int friAvailability) {
		this.FriAvailability = friAvailability;
	}

	public int getWeekDayOnly() {
		return WeekDayOnly;
	}

	public void setWeekDayOnly(int weekDayOnly) {
		this.WeekDayOnly = weekDayOnly;
	}

	public int getDocId() {
		return DocID;
	}

	public void setDocId(int docId) {
		this.DocID = docId;
	}

	public int getSlotDuration() {
		return SlotDuration;
	}

	public void setSlotDuration(int slotDuration) {
		this.SlotDuration = slotDuration;
	}

	public Time getFromTime() {
		return FromTime;
	}

	public void setFromTime(Time fromTime) {
		this.FromTime = fromTime;
	}

	public Time getToTime() {
		return ToTime;
	}

	public void setToTime(Time toTime) {
		this.ToTime = toTime;
	}

	public Timestamp getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.CreatedDate = createdDate;
	}

	public Timestamp getLastUpdatedDate() {
		return LastUpdatedDate;
	}

	public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
		this.LastUpdatedDate = lastUpdatedDate;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		this.Status = status;
	}

	public int getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(int createdBy) {
		this.CreatedBy = createdBy;
	}

	public int getUpdatedBy() {
		return UpdatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.UpdatedBy = updatedBy;
	}

//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID")
//	@JsonProperty("ContractID")
    private int ContractID;

	@JsonProperty("MonAvailability")
  //  @Column(name = "MonAvailability")
    private int MonAvailability;

    @Column(name = "TueAvailability")
    private int TueAvailability;

    @Column(name = "WedAvailability")
    private int WedAvailability;

    @Column(name = "ThuAvailability")
    private int ThuAvailability;

    @Column(name = "FriAvailability")
    private int FriAvailability;

    @Column(name = "WeekDayOnly")
    private int WeekDayOnly;

    @Id
    @Column(name = "DocID")
    private int DocID;

    @Column(name = "SlotDuration")
    private int SlotDuration;

    @Column(name = "FromTime")
    private Time FromTime;

    @Column(name = "ToTime")
    private Time ToTime;

    @Column(name = "CreatedDate")
    private Timestamp CreatedDate;

    @Column(name = "LastUpdatedDate")
    private Timestamp LastUpdatedDate;

    @Column(name = "Status")
    private Integer Status;

    @Column(name = "CreatedBy")
    private Integer CreatedBy;

    @Column(name = "UpdatedBy")
    private Integer UpdatedBy;

    
    @Transient
    private String docname_first;
    
    @Transient
    private String docname_middle;
    
    @Transient
    private String docname_last;
    // Constructors, getters, and setters

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

	public String getDocname_first() {
		return docname_first;
	}

	public void setDocname_first(String docname_first) {
		this.docname_first = docname_first;
	}

	public String getDocname_middle() {
		return docname_middle;
	}

	public void setDocname_middle(String docname_middle) {
		this.docname_middle = docname_middle;
	}

	public String getDocname_last() {
		return docname_last;
	}

	public void setDocname_last(String docname_last) {
		this.docname_last = docname_last;
	}

    // Constructors with parameters (if needed)

    // Getters and Setters
}
