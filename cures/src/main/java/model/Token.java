package model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tip_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenID;

    @Column(name = "token_name")
    private String tokenName;

    @Column(name = "registration_id")
    private Integer registrationID;
	
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private Integer status;

    public Integer getTokenID() {
		return tokenID;
	}

	public void setTokenID(Integer tokenID) {
		this.tokenID = tokenID;
	}

	public Integer getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(Integer registrationID) {
		this.registrationID = registrationID;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	// Constructors, getters, and setters
    public Token() {}

    public Token(String tokenName, Integer registrationId) {
        this.tokenName = tokenName;
        this.registrationID = registrationId;
    }

    public Integer getTokenId() {
        return tokenID;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenID = tokenId;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Integer getRegistrationId() {
        return registrationID;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationID = registrationId;
    }
}
