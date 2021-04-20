package dev.sanson.tick.todo

import dev.sanson.tick.todo.feature.list.ListsReducer
import dev.sanson.tick.todo.feature.sync.SyncReducer
import org.reduxkotlin.Reducer

/**
 * The root reducer for Tick. Dispatches actions to screen-specific reducers. Nothing within this
 * reducer should change the screen - that is done in the [dev.sanson.tick.todo.feature.navigation.NavigationReducer].
 */
val RootReducer: Reducer<AppState> = { state, action ->
    state.copy(
        currentScreen = when (val screen = state.currentScreen) {
            is Screen.Lists -> ListsReducer(screen, action)
            is Screen.SyncSettings -> SyncReducer(screen, action)
        }
    )
}
