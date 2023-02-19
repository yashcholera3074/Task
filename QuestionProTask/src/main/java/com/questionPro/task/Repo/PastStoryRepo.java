package com.questionPro.task.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.questionPro.task.Entity.PastStory;

public abstract interface PastStoryRepo extends CrudRepository<PastStory, Long> {
	@Query("from PastStory order by createdDate desc LIMIT 10")
	List<PastStory> findTop10OrderByCreateDateDesc();

	/*@Query("from PastStory WHERE type=:type order by noOfComments desc LIMIT 10")
	List<PastStory> findTop10OrderByNoOfCommentsDesc(@Param("type") String type);*/

}
