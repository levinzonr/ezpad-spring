package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.payload.PublishedNotebookPayload
import com.levinzonr.ezpad.domain.responses.CommentResponse
import com.levinzonr.ezpad.domain.responses.PublishedNotebookDetail
import com.levinzonr.ezpad.domain.responses.PublishedNotebookResponse
import com.levinzonr.ezpad.security.EzpadUserDetails
import com.levinzonr.ezpad.services.CommentService
import com.levinzonr.ezpad.services.PublishedNotebookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/shared")
class PublishedNotebooksController {



    @Autowired
    private lateinit var service: PublishedNotebookService

    @Autowired
    private lateinit var commentService: CommentService


    @GetMapping
    fun getRelevant() : List<PublishedNotebookResponse> {
        return service.getMostRelevant().map { it.toResponse() }
    }

    @PostMapping
    fun publishNotebook(@AuthenticationPrincipal userDetails: EzpadUserDetails, @RequestBody notebook: PublishedNotebookPayload) : PublishedNotebookResponse {
        return service.publishNotebook(
                userDetails.userId, notebook.notebookId, notebook.title,
                notebook.description, notebook.topic, notebook.tags ?: setOf(), notebook.universityId).toResponse()
    }

    @GetMapping("/{id}")
    fun getPublishedNotebookDetails(@PathVariable("id") id: String) : PublishedNotebookDetail {
        return service.getPublishedNotebookById(id).toDetailedResponse()
    }


    @PostMapping("/{id}/comment")
    fun postNotebookComment(@AuthenticationPrincipal user: EzpadUserDetails,
                            @RequestParam comment: String,
                            @PathVariable("id") id: String) : CommentResponse {
        return commentService.postNotebookComment(user.userId, id, comment).toResponse()
    }


    @DeleteMapping("/comments/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    fun deleteComment(@PathVariable("id") id: Long) {
        commentService.deleteComment(id)
    }


    @PostMapping("/comments/{id}")
    fun updateComment(@PathVariable("id") id: Long, @RequestParam comment: String) : CommentResponse {
        return commentService.updateComment(id, comment).toResponse()
    }


}