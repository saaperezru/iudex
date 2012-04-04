package org.xtremeware.iudex.vo;

import java.util.Date;

public class FeedbackVo extends ValueObject{

	private Long id;
	private FeedbackTypeVo type;
	private String content;
	private Date date;

	@Override
	public boolean equals(Object ob) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int compareTo(ValueObject vo) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FeedbackTypeVo getType() {
		return type;
	}

	public void setType(FeedbackTypeVo type) {
		this.type = type;
	}

		
	
}
