package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.dto.FieldError
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    override fun createUser(email: String, password: String,
                            firstName: String?, lastName: String?, photoUrl: String?): User {

        userRepository.findByEmail(email)?.let {
           throw BadRequestException(ApiMessages.ErrorMessages.ERROR_USER_EXISTS)
        }


        val user = User(
                email = email,
                password = passwordEncoder.encode(password),
                firstName = firstName,
                lastName = lastName,
                displayName = "$firstName $lastName",
                photoUrl = photoUrl
        )

        return userRepository.save(user)
    }

    override fun getUserById(uuid: UUID): User {
        return userRepository.findById(uuid)
                .orElseThrow {
                    NotFoundException.Builder(User::class)
                        .buildWithId(uuid.toString())
                }
    }

    override fun updateUserById(uuid: String, firstName: String?, lastName: String?, password: String?) : User {
        val user = getUserById(UUID.fromString(uuid))
        val newPassword = if (password != null) passwordEncoder.encode(password) else null
        val updated = user.copy(
                firstName = firstName ?: user.firstName,
                lastName = lastName ?: user.lastName,
                displayName = "$firstName $lastName",
                password = newPassword ?: user.password
        )
        return userRepository.save(updated)
    }
}