import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.nhonnq.app.ui.main.MainRouter
import dev.nhonnq.app.ui.users.UserList
import dev.nhonnq.app.ui.users.state.UsersNavigationState
import dev.nhonnq.app.ui.users.state.UsersUiState
import dev.nhonnq.app.ui.users.viewmodel.UsersViewModel
import dev.nhonnq.app.ui.widget.PullToRefresh
import dev.nhonnq.app.util.collectAsEffect
import dev.nhonnq.data.entities.UserData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UsersScreen(
    mainRouter: MainRouter,
    viewModel: UsersViewModel
) {
    val usersPaging = viewModel.users.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullRefreshState(uiState.showLoading, { viewModel.onRefresh() })

    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is UsersNavigationState.UserDetails -> mainRouter.navigateToUserDetails(navigationState.loginUserName)
        }
    }
    viewModel.refreshListState.collectAsEffect {
        usersPaging.refresh()
    }

    LaunchedEffect(key1 = usersPaging.loadState) {
        viewModel.onLoadStateUpdate(usersPaging.loadState)
    }

    PullToRefresh(state = pullToRefreshState, refresh = uiState.showLoading) {
        UsersScreenContainer(
            users = usersPaging,
            uiState = uiState,
            onUserClick = viewModel::onUserClicked
        )
    }
}

@Composable
private fun UsersScreenContainer(
    users: LazyPagingItems<UserData>,
    uiState: UsersUiState,
    onUserClick: (loginUserName: String) -> Unit
) {
    Surface {
        Column {
            if (uiState.showLoading) {
                Spacer(Modifier.fillMaxWidth().height(60.dp))
            }
            UserList(users, uiState.showLoading, onUserClick)
        }
    }
}