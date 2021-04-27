package dev.sanson.tick.todo.feature.list

import dev.sanson.tick.model.Todo
import dev.sanson.tick.model.copy
import dev.sanson.tick.todo.Action
import dev.sanson.tick.todo.Screen
import org.reduxkotlin.Reducer

/**
 * Reducer for the main list screen
 */
val ListsReducer: Reducer<Screen.Lists> = { state, action ->
    when (action) {
        is Action.ListsLoaded -> state.copy(lists = action.lists)
        is Action.UpdateListTitle -> state.copy(
            lists = state.lists.map {
                if (it == action.list) {
                    it.copy(title = action.title)
                } else {
                    it
                }
            }
        )
        is Action.UpdateTodo -> state.copy(
            lists = state.lists.map { list ->
                list.copy(
                    items = list.items.map { item ->
                        if (item == action.item) {
                            action.item
                        } else {
                            item
                        }
                    }
                )
            }
        )
        is Action.AddTodo -> state.copy(
            lists = state.lists.map {
                if (it == action.list) {
                    it.copy(items = it.items + Todo(text = "", isDone = false))
                } else {
                    it
                }
            }
        )
        is Action.AddTodoAsSibling -> {
            val list = state.lists.find { it.items.contains(action.sibling) }
                ?: throw IllegalArgumentException("No list found for sibling: ${action.sibling}")

            state.copy(
                lists = state.lists.map {
                    if (it == list) {
                        it.copy(items = it.items + Todo(text = "", isDone = false))
                    } else {
                        it
                    }
                }
            )
        }
        is Action.DeleteTodo -> state.copy(
            lists = state.lists.map { list ->
                if (list.items.contains(action.item)) {
                    list.copy(items = list.items - action.item)
                } else {
                    list
                }
            }
        )
        else -> state
    }
}
