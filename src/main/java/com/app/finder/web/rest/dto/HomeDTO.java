package com.app.finder.web.rest.dto;

import java.util.List;

import com.app.finder.domain.Article;

/**
 * 首页数据DTO
 *
 */
public class HomeDTO {

	// 收获喜欢最多的一条文章数据  顶部图片用
	private Article favoriteData;
		
	// 浏览数最多的一条文章数据  顶部图片用
	private Article pageViewData;
	
	// 评论最多的一条文章数据  顶部图片用
	private Article commentData;
	
	/*
	 * 列表分页数据用到的分页参数
	 * 当前页数
	 */
	private Integer pageNumber;
	
	/*
	 * 列表分页数据用到的分页参数
	 * 总页数
	 */
	private Integer totalPages;
	
	// 列表分页数据DTO数据显示用
	List<HomePageDataDTO> pageDataDTO;

	// 活跃作者(文章数最多)
	private List<HotAuthorDTO> authors;
	
	// 用户收获喜欢数(最多)
	List<HotFavoriteDTO> favorites;

	// 热门文章(访问最多的数据)
	private List<Article> hotArticles;
	
	public HomeDTO(Article favoriteData, Article pageViewData, Article commentData,
			List<HotAuthorDTO> authors, List<HotFavoriteDTO> favorites, List<Article> hotArticles, 
			List<HomePageDataDTO> pageDataDTO, Integer pageNumber, Integer totalPages) {
		this.favoriteData = favoriteData;
		this.pageViewData = pageViewData;
		this.commentData = commentData;
		this.authors = authors;
		this.favorites = favorites;
		this.hotArticles = hotArticles;
		this.pageDataDTO = pageDataDTO;
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
	}

	public HomeDTO(List<HomePageDataDTO> pageDataDTO, 
			Integer pageNumber, Integer totalPages) {
		this.pageDataDTO = pageDataDTO;
		this.pageNumber = pageNumber;
		this.totalPages = totalPages;
	}

	public Article getPageViewData() {
		return pageViewData;
	}

	public void setPageViewData(Article pageViewData) {
		this.pageViewData = pageViewData;
	}
	
	public Article getFavoriteData() {
		return favoriteData;
	}

	public void setFavoriteData(Article favoriteData) {
		this.favoriteData = favoriteData;
	}

	public List<HotAuthorDTO> getAuthors() {
		return authors;
	}

	public void setAuthors(List<HotAuthorDTO> authors) {
		this.authors = authors;
	}

	public List<Article> getHotArticles() {
		return hotArticles;
	}

	public void setHotArticles(List<Article> hotArticles) {
		this.hotArticles = hotArticles;
	}

	public List<HomePageDataDTO> getPageDataDTO() {
		return pageDataDTO;
	}

	public void setPageDataDTO(List<HomePageDataDTO> pageDataDTO) {
		this.pageDataDTO = pageDataDTO;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<HotFavoriteDTO> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<HotFavoriteDTO> favorites) {
		this.favorites = favorites;
	}

	public Article getCommentData() {
		return commentData;
	}

	public void setCommentData(Article commentData) {
		this.commentData = commentData;
	}
	
}
