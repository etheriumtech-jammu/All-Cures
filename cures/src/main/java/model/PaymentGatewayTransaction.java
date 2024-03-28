package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Payment_Gateway_Transactions")
public class PaymentGatewayTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_gateway_transaction_id")
    private Long paymentGatewayTransactionId;

    @Column(name = "AppointmentID")
    private Integer appointmentID;

    @Column(name = "order_id")
    private String orderID;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "order_status")
    private Integer orderStatus;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "status_message")
    private String statusMessage;

    @Column(name = "settlement_flag")
    private Integer settlementFlag;

    @Column(name = "bank_ref_no", columnDefinition = "text")
    private String bankRefNo;

    @Column(name = "trans_date")
    private Date transactionDate;

    @Column(name = "trackingID")
    private String trackingId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "LastUpdatedBy")
    private Integer lastUpdatedBy;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

	public Long getPaymentGatewayTransactionId() {
		return paymentGatewayTransactionId;
	}

	public void setPaymentGatewayTransactionId(Long paymentGatewayTransactionId) {
		this.paymentGatewayTransactionId = paymentGatewayTransactionId;
	}

	public Integer getAppointmentId() {
		return appointmentID;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentID = appointmentId;
	}

	public String getOrderId() {
		return orderID;
	}

	public void setOrderId(String orderId) {
		this.orderID = orderId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Integer getSettlementFlag() {
		return settlementFlag;
	}

	public void setSettlementFlag(Integer settlementFlag) {
		this.settlementFlag = settlementFlag;
	}

	public String getBankRefNo() {
		return bankRefNo;
	}

	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

    // Getters and setters omitted for brevity
}
