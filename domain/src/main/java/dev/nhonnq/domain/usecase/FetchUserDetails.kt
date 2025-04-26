package dev.nhonnq.domain.usecase

import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.repository.UserRepository
import dev.nhonnq.domain.util.Result

class FetchUserDetails(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginUserName: String): Result<UserEntity> = userRepository.getUserDetails(loginUserName = loginUserName)
}
