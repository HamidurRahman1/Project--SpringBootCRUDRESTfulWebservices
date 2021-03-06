package com.hamidur.springBootCRUDRESTfulWebservices.api;

import com.hamidur.springBootCRUDRESTfulWebservices.dbModels.Article;
import com.hamidur.springBootCRUDRESTfulWebservices.dbModels.Author;
import com.hamidur.springBootCRUDRESTfulWebservices.dbModels.Comment;
import com.hamidur.springBootCRUDRESTfulWebservices.services.AuthorService;
import com.hamidur.springBootCRUDRESTfulWebservices.services.ArticleService;
import com.hamidur.springBootCRUDRESTfulWebservices.services.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RESTController
{
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthorById(@PathVariable Long authorId)
    {
        Author author = authorService.getAuthorById(authorId);
        return author != null ? new ResponseEntity<>(author, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Author>> getAllAuthors()
    {
        List<Author> authors = authorService.getAuthors();
        if(authors == null || authors.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping(value = "/author/{authorId}/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getAllArticlesByAuthor(@PathVariable Long authorId)
    {
        List<Article> articles = authorService.getAuthorById(authorId).getArticles();
        if(articles == null || articles.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getArticles()
    {
        List<Article> articles = articleService.getArticles();
        if(articles == null || articles.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(value = "/article/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> getArticleById(@PathVariable Long articleId)
    {
        Article article = articleService.getArticleById(articleId);
        return article != null ? new ResponseEntity<>(article, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/comment/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId)
    {
        Comment comment = commentService.getCommentById(commentId);
        return comment != null ? new ResponseEntity<>(comment, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/author/{authorId}/insertArticle", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertPost(@RequestBody Article incomingArticle, @PathVariable Long authorId)
    {
        Author retrievedAuthor = authorService.getAuthorById(authorId);
        retrievedAuthor.getArticles().add(incomingArticle);
        incomingArticle.setAuthor(retrievedAuthor);
        Article retrievedArticle = articleService.insertArticle(incomingArticle);
        authorService.updateAuthor(retrievedAuthor);
        return retrievedArticle.getArticleId() != null ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/article/{articleId}/insertComment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertComment(@RequestBody Comment incomingComment, @PathVariable Long articleId)
    {
        Article retrievedArticle = articleService.getArticleById(articleId);
        Comment retrievedComment = commentService.insertComment(incomingComment);
        retrievedArticle.getComments().add(incomingComment);
        articleService.updateArticle(retrievedArticle);
        return retrievedComment.getCommentId() != null ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/insertAuthor", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> insertAuthor(@RequestBody Author author)
    {
        Author author1 = authorService.insertAuthor(author);
        return author1.getAuthorId() != null ? new ResponseEntity<>(author1, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/author/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> updateAuthorById(@RequestBody Author newAuthor)
    {
        if(newAuthor == null || newAuthor.getAuthorId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(authorService.updateAuthor(newAuthor), HttpStatus.OK);
    }

    @PutMapping(value = "/comment/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment)
    {
        if(comment == null || comment.getCommentId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(commentService.updateComment(comment), HttpStatus.OK);
    }

    @PutMapping(value = "/article/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@RequestBody Article article)
    {
        if(article == null || article.getArticleId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(articleService.updateArticle(article), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/author/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId)
    {
        Author author = authorService.getAuthorById(authorId);
        if(author != null)
        {
            authorService.deleteAuthor(author);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/article/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId)
    {
        Article article = articleService.getArticleById(articleId);
        if(article == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Author author = article.getAuthor();
        author.getArticles().remove(article);
        article.setAuthor(null);
        authorService.updateAuthor(author);
        articleService.deleteArticle(article.getArticleId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/comment/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId)
    {
        if(commentService.getCommentById(commentId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
