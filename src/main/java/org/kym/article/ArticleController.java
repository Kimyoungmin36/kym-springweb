package org.kym.article;

import java.util.List;

import org.kym.book.chap11.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class ArticleController {

	@Autowired
	ArticleDao articleDao;

	/**
	 * 글 목록
	 */
	@GetMapping("/article/list")
	public void articleList(
			@RequestParam(value = "page", defaultValue = "1") int page,
			Model model) {

		// 페이지당 행의 수와 페이지의 시작점
		final int COUNT = 100;
		int offset = (page - 1) * COUNT;

		List<Article> articleList = articleDao.listArticles(offset, COUNT);
		int totalCount = articleDao.getArticlesCount();
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("articleList", articleList);
	}

	/**
	 * 글 보기
	 */
	@GetMapping("/article/view")
	public void articleView(@RequestParam("articleId") String articleId,
			Model model) {
		Article article = articleDao.getArticle(articleId);
		model.addAttribute("article", article);
	}

	/**
	 * 글 등록
	 */
	@PostMapping("/article/add")
	public String articleAdd(Article article, 
			@SessionAttribute("MEMBER") Member member) {
		article.setUserId(member.getMemberId());
		article.setName(member.getName());
		articleDao.addArticle(article);
		return "redirect:/app/article/list";
	}
	
	@GetMapping("/article/updateForm") 
	public String articleUpdateForm(@RequestParam("articleId") String articleId, 
			@SessionAttribute("MEMBER") Member member, Model model) { 
		Article article = articleDao.getArticle(articleId); 
		if (!article.getUserId().equals(member.getMemberId())) { 
			return "article/updateFail"; 
		} 
		model.addAttribute("article", article); 
		return "article/updateForm"; 
	} 

	/**
	 * 글 수정
	 */
	@PostMapping("/article/update") 
	public String articleUpdate(Article article) { 
 	articleDao.updateArticle(article); 
 	return "redirect:/app/article/view?articleId=" + article.getArticleId(); 
 	} 

	/**
	 * 글 삭제
	 */
	@GetMapping("/article/delete") 
	public String articleDelete(@RequestParam("articleId") String articleId, @SessionAttribute("MEMBER") Member member) { 
		Article article = articleDao.getArticle(articleId); 
		if (!article.getUserId().equals(member.getMemberId())) { 
			return "article/deleteFail"; 
		} 
		articleDao.deleteArticle(articleId); 
		return "article/delete"; 
	}
	
	
	@PostMapping("/article/updateFail") 
	public String updateFail() { 
		return "article/updateFail"; 
	} 

	@GetMapping("/article/deleteFail") 
	public String deleteFail() { 
		return "article/deleteFail"; 
	} 
}