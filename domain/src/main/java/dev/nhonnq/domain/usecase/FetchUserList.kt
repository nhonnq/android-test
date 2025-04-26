package dev.nhonnq.domain.usecase

import androidx.paging.PagingData
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FetchUserList(
    private val userRepository: UserRepository
) {
    operator fun invoke(perPage: Int): Flow<PagingData<UserEntity>> = userRepository.getUsers(since = 0, perPage = perPage)
}