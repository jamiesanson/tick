@file:Suppress("FunctionName")

package dev.sanson.tick.todo

import dev.sanson.tick.model.Todo
import dev.sanson.tick.model.TodoList

sealed class Action {
    sealed class Navigation : Action() {
        object Back : Navigation()
        object SyncSettings : Navigation()
    }

    data class UpdateListTitle(val list: TodoList, val title: String): Action()
    data class UpdateTodo(val item: Todo): Action()
    data class AddTodo(val list: TodoList): Action()
    data class AddTodoAsSibling(val sibling: Todo): Action()
    data class DeleteTodo(val item: Todo): Action()

    data class ListsLoaded(val lists: List<TodoList>): Action()
}
