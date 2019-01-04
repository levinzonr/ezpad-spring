package com.levinzonr.ezpad.domain.responses

class PublishedNotebookResponse(
        val title: String,
        val notesCount: Long,
        val id: String,
        val description: String?,
        val author: UserResponse,
        val tags: Set<String>,
        val commentCount: Int,
        val topic: String?


)