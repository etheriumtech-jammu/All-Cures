package model;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "WalletTransaction",
       indexes = {
           @Index(name = "idx_wt_pgtxn", columnList = "payment_gateway_transaction_id"),
           @Index(name = "idx_wt_wallet_owner", columnList = "wallet_master_type_id, owner_id"),
           @Index(name = "idx_wt_doc_created", columnList = "doc_id, created_at")
       })
public class WalletTransaction {

    public enum Direction { CREDIT, DEBIT }
    public enum Status { PENDING, SUCCESS, FAILED, REFUNDED, REVERSED, SETTLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_transaction_id")
    private Long id;

    // FK → paymentgatewaytransaction
    @Column(name = "payment_gateway_transaction_id", nullable = false)
    private Integer paymentGatewayTransactionId;

    // Wallet classification
    @Column(name = "wallet_master_type_id", nullable = false)
    private Integer walletMasterTypeId; // 1=GST, 2=Platform, 3=Doctor

    @Column(name = "owner_id")
    private Integer ownerId; // e.g., doctor/user owning the wallet

    @Column(name = "doc_id")
    private Integer docId; // doctor linked to the earning

    // Money
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false, length = 10)
    private Direction direction; // CREDIT / DEBIT

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 15, nullable = false)
    private Status status = Status.SUCCESS; // default for confirmed rows

    @Column(name = "type", length = 30)
    private String type; // CONSULTATION, REFUND, BONUS, etc.

    @Column(name = "description", length = 255)
    private String description;

    // For refunds, to dedupe partial refunds
    @Column(name = "refund_ref", length = 100, unique = false)
    private String refundRef;

    // Timestamps
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void touch() {
        this.updatedAt = Instant.now();
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPaymentGatewayTransactionId() { return paymentGatewayTransactionId; }
    public void setPaymentGatewayTransactionId(Integer paymentGatewayTransactionId) { this.paymentGatewayTransactionId = paymentGatewayTransactionId; }

    public Integer getWalletMasterTypeId() { return walletMasterTypeId; }
    public void setWalletMasterTypeId(Integer walletMasterTypeId) { this.walletMasterTypeId = walletMasterTypeId; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public Integer getDocId() { return docId; }
    public void setDocId(Integer docId) { this.docId = docId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRefundRef() { return refundRef; }
    public void setRefundRef(String refundRef) { this.refundRef = refundRef; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
