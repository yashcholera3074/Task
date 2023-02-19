package com.questionPro.task.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.questionPro.task.Entity.Comment;

public abstract interface CommentRepo extends CrudRepository<Comment, Long>{

	@Query("from Comment where storyId=:storyId order by subCommentsNo desc LIMIT 10")
	List<Comment> findTop10CommentsOrderBySubCommentsNoDesc(@Param("storyId") Integer storyId);
	
	List<Comment> findByCommentId(int commentId);
}
