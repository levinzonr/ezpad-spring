package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.AuthorResponse
import com.levinzonr.ezpad.domain.responses.UserResponse
import javax.persistence.*

@Entity(name = "studypad_user")
data class User(
        @Id
        val id: String? = null,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val displayName: String? = null,
        val photoUrl: String? = null,

        val currentLocaleString :String? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        val firebaseTokens: List<String> = arrayListOf(),

        @ManyToOne
        @JoinColumn(name = "university_id")
        val university: University? = null,

        val password: String? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        val roles: Set<UserRole> = setOf(UserRole.USER)) {


        @Transient
        var isNewUser: Boolean = false

    fun toResponse(notifications: Int): UserResponse {
        return UserResponse(
                uuid = id!!,
                email = email,
                firstName = firstName,
                lastName = lastName,
                displayName = displayName,
                university = university?.toResponse(),
                photoUrl = photoUrl,
                isNewUser = isNewUser,
                unreadNotifications = notifications
        )
    }

    fun toAuthorResponse() : AuthorResponse {
        return AuthorResponse(id!!, displayName, photoUrl, university?.toResponse())
    }
}