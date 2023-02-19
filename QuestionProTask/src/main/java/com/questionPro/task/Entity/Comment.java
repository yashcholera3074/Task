package com.questionPro.task.Entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	private long seqId;
	private int storyId;
	private int commentId;
	private String comment;
	private int subCommentsNo;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="COMMENT_SEQ_ID", unique=true, nullable=false, precision=10,scale=0)
	public long getSeqId() {
		return seqId;
	}
	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}
	
	
	@Column(name="COMMENT_ID", unique=true)
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	
	@Column(name="STORY_ID")
	public int getStoryId() {
		return storyId;
	}
	public void setStoryId(int storyId) {
		this.storyId = storyId;
	}
	
	@Column(name="COMMENT", length = 4000)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Column(name="SUBCOMMENT_NO")
	public int getSubCommentsNo() {
		return subCommentsNo;
	}
	public void setSubCommentsNo(int subCommentsNo) {
		this.subCommentsNo = subCommentsNo;
	}
}
