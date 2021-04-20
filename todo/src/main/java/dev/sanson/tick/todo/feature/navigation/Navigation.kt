@file:Suppress("FunctionName", "unused")

package dev.sanson.tick.todo.feature.navigation

import dev.sanson.tick.todo.Action
import dev.sanson.tick.todo.AppState
import dev.sanson.tick.todo.Screen
import org.reduxkotlin.middleware
import org.reduxkotlin.reducerForActionType

fun BackNavigationMiddleware(exitApplication: () -> Unit) =
    middleware<AppState> { store, next, action ->
        if (action == Action.Navigation.Back && store.state.backstack.isEmpty()) {
            exitApplication()
        } else {
            next(action)
        }
    }

val NavigationReducer = reducerForActionType<AppState, Action.Navigation> { state, action ->
    when (action) {
        Action.Navigation.Back -> state.pop()
        Action.Navigation.SyncSettings -> if (state.currentScreen is Screen.Lists) state.push(
            Screen.SyncSettings.fromBackends(
                state.backends
            )
        ) else state
    }
}

private fun AppState.replace(screen: Screen): AppState = copy(currentScreen = screen)

private fun AppState.push(screen: Screen): AppState = copy(
    backstack = listOf(*backstack.toTypedArray(), currentScreen),
    currentScreen = screen
)

private fun AppState.pop(): AppState = copy(
    backstack = backstack.dropLast(1),
    currentScreen = backstack.last()
)
