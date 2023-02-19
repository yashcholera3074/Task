package com.questionPro.task.Entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public final class PastStory implements Serializable{
	private static final long serialVersionUID = 1L;
	private long seqId;
	private int storyId;
	private String title;
	private int score;
	private String url;
	private Date createdDate;
	private String createBy;
	private String type;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PAST_STORY_SEQ_ID", unique=true, nullable=false, precision=10,scale=0)
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	
	@Column(name="STORY_ID", unique=true, nullable=false)
	public int getStoryId() {
		return storyId;
	}
	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}
	
	@Column(name="TITLE")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="SCORE")
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Column(name="URL")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="CreateDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="CreatedBy")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name="TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
