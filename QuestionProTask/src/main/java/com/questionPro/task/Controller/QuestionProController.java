package com.questionPro.task.Controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.questionPro.task.Entity.Comment;
import com.questionPro.task.Entity.PastStory;
import com.questionPro.task.Entity.Story;
import com.questionPro.task.IntFc.QuestionPro;
import com.questionPro.task.Repo.CommentRepo;
import com.questionPro.task.Repo.PastStoryRepo;
import com.questionPro.task.Repo.StoryRepo;

@RestController
public class QuestionProController implements QuestionPro {

	@Autowired
	StoryRepo storyRepo;
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	PastStoryRepo pastStoryRepo;
	
	@Autowired
	CommentRepo commentRepo;
	
	@Override
	@Cacheable("topStoriesCache")
	public Map<String ,Object> topStories() {
		Map<String ,Object> resultMap= new LinkedHashMap<>();
		final String url="https://hacker-news.firebaseio.com/v0/topstories.json";
		RestTemplate restTemplate = new RestTemplate();
		String result= restTemplate.getForObject(url, String.class);
		result=result.replaceAll("[\\[\\]]","");
		String[] ls=result.split(",");
		String storyUrl="https://hacker-news.firebaseio.com/v0/item/";
		for(int i=0;i<ls.length;i++) {
			String storyResult=restTemplate.getForObject(storyUrl+ls[i]+".json", String.class);
			JSONObject jsonObject = new JSONObject(storyResult);
			Story story= new Story();
			try {
			story.setCreatedDate(new Date());
			story.setScore(jsonObject.getInt("score"));
			story.setCreateBy("Yash");
			story.setStoryId(jsonObject.getInt("id"));
			story.setTitle(jsonObject.getString("title"));
			story.setUrl(jsonObject.getString("url"));
			story.setType(jsonObject.getString("type"));
			storyRepo.save(story);
			}catch (Exception e) {
				System.out.println("Exception in persisting");
			}	
		}
		List<Story> topStories= storyRepo.findTop10OrderByScoreDesc();
		if(topStories!=null && !topStories.isEmpty()) {
			for(int i=0;i<topStories.size();i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Title",topStories.get(i).getTitle());
				jsonObject.put("Score",topStories.get(i).getScore());
				jsonObject.put("Url",topStories.get(i).getUrl());
				jsonObject.put("Date of Submission",topStories.get(i).getCreatedDate());
				jsonObject.put("Submitted By",topStories.get(i).getCreateBy());
				resultMap.put(((Integer)topStories.get(i).getStoryId()).toString(), jsonObject.toString());
			
				PastStory pastStory= new PastStory();
				try {
					pastStory.setCreatedDate(new Date());
					pastStory.setScore(topStories.get(i).getScore());
					pastStory.setCreateBy("Yash");
					pastStory.setStoryId(topStories.get(i).getStoryId());
					pastStory.setTitle(topStories.get(i).getTitle());
					pastStory.setUrl(topStories.get(i).getUrl());
					pastStory.setType(topStories.get(i).getType());
					pastStoryRepo.save(pastStory);
					}catch (Exception e) {
						System.out.println("Exception in persisting in paststory"+e);
					}
				}
			}
		else {
			System.out.println("No stories found");
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> pastStories() {
		Map<String ,Object> resultMap= new LinkedHashMap<>();
		List<PastStory> paststory= pastStoryRepo.findTop10OrderByCreateDateDesc();
		if(paststory != null && !paststory.isEmpty()) {
			for(int i=0;i<paststory.size();i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("Title",paststory.get(i).getTitle());
				jsonObject.put("Score",paststory.get(i).getScore());
				jsonObject.put("Url",paststory.get(i).getUrl());
				jsonObject.put("Date of Submission",paststory.get(i).getCreatedDate());
				jsonObject.put("Submitted By",paststory.get(i).getCreateBy());
				resultMap.put(((Integer)paststory.get(i).getStoryId()).toString(), jsonObject.toString());
			}
		}
		else {
			System.out.println("No past stories found");
			resultMap.put("Error message", "No past stories available");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> comments(int storyId) {
		Map<String ,Object> resultMap= new LinkedHashMap<>();
		try {
		RestTemplate restTemplate = new RestTemplate();
		String storyUrl="https://hacker-news.firebaseio.com/v0/item/"+storyId+".json";
		String storyResult=restTemplate.getForObject(storyUrl, String.class);
		if (storyResult!=null && !storyResult.isEmpty()) {
				JSONObject jsonObject = new JSONObject(storyResult);
				if(jsonObject.has("kids")) {
					JSONArray kids=jsonObject.getJSONArray("kids");
					for(int i=0;i<kids.length();++i) {
						List<Comment> commentIdList= commentRepo.findByCommentId((Integer)kids.get(i));
						if(commentIdList!= null && !commentIdList.isEmpty()) {
							System.out.println("Entry is already there for given commentId "+(Integer)kids.get(i));
						}
						else {
								
							try {
							String commentRes= restTemplate.getForObject("https://hacker-news.firebaseio.com/v0/item/"+(Integer)kids.get(i) +".json", String.class);
							JSONObject commentJson = new JSONObject(commentRes);
							Comment comment= new Comment();
							comment.setStoryId(storyId);
							comment.setCommentId((Integer)kids.get(i));
							comment.setComment(commentJson.getString("text"));
							if(commentJson.has("kids")) {
								JSONArray subComments=commentJson.getJSONArray("kids");
								System.out.println(subComments.length());
								comment.setSubCommentsNo(subComments.length());
							}
							else {
								System.out.println("Inserting 0");
								comment.setSubCommentsNo(0);
							}
							
							commentRepo.save(comment);
							}catch (Exception e) {
								System.out.println("Exception in Persisting in Comment "+e);
							}
						}
					}
					List<Comment> commentList= commentRepo.findTop10CommentsOrderBySubCommentsNoDesc(storyId);
					if(commentList!=null && !commentList.isEmpty()) {
						for(int i=0;i<commentList.size();i++) {
							JSONObject  commentListJson= new JSONObject();
							commentListJson.put("Comment",commentList.get(i).getComment());
							commentListJson.put("NoOfComments",commentList.get(i).getSubCommentsNo());
							resultMap.put(((Integer)commentList.get(i).getCommentId()).toString(), commentListJson.toString());
						}
					}
					else {
						System.out.println("No comments found");
						resultMap.put("Error Message","No comments found for given storyId");
					}
				}
				else {
					resultMap.put("Error Message","No comments found for given storyId");
				}
				
			}
		}catch (Exception e) {
			System.out.println("Exception in Comments "+e);
			resultMap.put("Error Message", "Exception occured in comments");
		}
		return resultMap;
	}
	
	
	@Scheduled(fixedRate = 15 * 60000)
	@CacheEvict(value = "topStoriesCache")
    public void clearCache() {
        System.out.println("Clearing cache");
        storyRepo.truncateMyTable();
    }

}
