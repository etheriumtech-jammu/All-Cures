package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dp_chat")
public class Chat{
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer Chat_id;
	private Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getChat_id() {
		return Chat_id;
	}

	public void setChat_id(Integer chat_id) {
		Chat_id = chat_id;
	}
	
}
