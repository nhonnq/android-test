package dev.nhonnq.data.repository.user
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.UserRemoteKeyDbData
import dev.nhonnq.data.mapper.toDb
import dev.nhonnq.data.util.DataConstants.USER_STARTING_PAGE_INDEX
import dev.nhonnq.domain.util.Result

/**
 * [UserRemoteMediator] is responsible for loading user data from the remote data source and
 * caching it in the local database. It implements [RemoteMediator] to handle pagination
 * for a Paging 3 library data source.
 *
 * This class acts as an intermediary between the local database and the remote API,
 * deciding when to fetch more data from the network and how to update the local cache.
 *
 * @property local The local data source (e.g., a Room database) for storing user data and remote keys.
 * @property remote The remote data source (e.g., a network API) for fetching user data.
 */
@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : RemoteMediator<Int, UserData>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UserData>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> USER_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> local.getLastRemoteKey()?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
        }
        val pageSize = state.config.pageSize

        Log.d("UserRemoteMediator", "load() called with: loadType = $loadType, page: $page, stateLastItem = ${state.isEmpty()}")

        // There was a lag in loading the first page; as a result, it jumps to the end of the pagination.
        if (state.isEmpty() && page == 1) return MediatorResult.Success(endOfPaginationReached = false)

        when (val result = remote.fetchUsers(page * pageSize, pageSize)) {
            is Result.Success -> {
                Log.d("UserRemoteMediator", "Get users from remote")
                if (loadType == LoadType.REFRESH) {
                    local.clearUsers()
                    local.clearRemoteKeys()
                }

                val users = result.data

                val endOfPaginationReached = users.isEmpty()

                val prevPage = if (page == USER_STARTING_PAGE_INDEX) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1
                val key = UserRemoteKeyDbData(prevPage = prevPage, nextPage = nextPage)

                local.saveUsers(users.map { it.toDb() })
                local.saveRemoteKey(key)

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            is Result.Error -> {
                return MediatorResult.Error(result.error)
            }
        }
    }
}
