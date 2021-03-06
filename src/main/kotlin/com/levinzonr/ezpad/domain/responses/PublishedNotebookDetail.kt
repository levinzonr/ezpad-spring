package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.model.University


data class PublishedNotebookDetail(
        val id: String,
        val author: AuthorResponse,
        val comments: List<CommentResponse>,
        val title: String,
        val description: String?,
        val notes: List<NoteResponse>,
        val tags: Set<String>,
        val topic: String?,
        val lastUpdate: Long,
        val languageCode: String?,
        var versionState: VersionStateResponse,
        val excludedFromSearch: Boolean,
        val authoredByMe: Boolean,
        val university: UniversityResponse? = null

)

