package com.app.finder.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.finder.domain.Article;
import com.app.finder.domain.ArticleReply;
import com.app.finder.security.AuthoritiesConstants;
import com.app.finder.service.ArticleService;
import com.app.finder.web.rest.dto.ArticleDTO;
import com.app.finder.web.rest.util.HeaderUtil;
import com.app.finder.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);
        
    @Inject
    private ArticleService articleService;
    
    /**
     * POST  /articles -> Create a new article.
     * @throws IOException 
     */
    @RequestMapping(value = "/articles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER) //用户角色可以访问
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) throws URISyntaxException, IOException {
        log.debug("REST request to save Article : {}", article);
        if (article.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("article", "idexists", "A new article cannot already have an ID")).body(null);
        }
        Article result = articleService.save(article);
        return ResponseEntity.created(new URI("/api/articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("article", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articles -> Updates an existing article.
     * @throws IOException 
     */
    @RequestMapping(value = "/articles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ARTICLE_ADMIN) //文章管理者角色可以访问
    public ResponseEntity<Article> updateArticle(@Valid @RequestBody Article article) throws URISyntaxException, IOException {
        log.debug("REST request to update Article : {}", article);
        if (article.getId() == null) {
            return createArticle(article);
        }
        Article result = articleService.save(article);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("article", article.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articles -> get all the articles.
     */
    @RequestMapping(value = "/articles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Article>> getAllArticles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Articles");
        Page<Article> page = articleService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /articles/:id -> get the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        ArticleDTO article = articleService.findOne(id);
        return Optional.ofNullable(article)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /articles/:id -> delete the "id" article.
     */
    @RequestMapping(value = "/articles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ARTICLE_ADMIN) //文章管理者角色可以访问
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("article", id.toString())).build();
    }

    /**
     * SEARCH  /_search/articles/:query -> search for the article corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/articles/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Article> searchArticles(@PathVariable String query) {
        log.debug("Request to search Articles for query {}", query);
        return articleService.search(query);
    }
    
    
    /**
     * POST  /articleReplys -> Create a new articleReply.
     * 通过文章详细页面新建评论
     */
    @RequestMapping(value = "/articleDetailsReplys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ArticleReply>> createArticleDetailsReply(@Valid @RequestBody ArticleReply articleReply) throws URISyntaxException {
        log.debug("REST request to save ArticleReply : {}", articleReply);
        if (articleReply.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleReply", "idexists", "A new articleReply cannot already have an ID")).body(null);
        }
        List<ArticleReply> replies = articleService.createArticleReply(articleReply);
        HttpHeaders headers = HeaderUtil.createAlert("评论保存成功！", "");
        return new ResponseEntity<>(replies, headers, HttpStatus.OK);
    }
}
