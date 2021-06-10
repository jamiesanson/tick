package dev.sanson.tick.screen.list

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import dev.sanson.tick.android.LocalDispatch
import dev.sanson.tick.model.Todo
import dev.sanson.tick.model.TodoList
import dev.sanson.tick.theme.TickTheme
import dev.sanson.tick.todo.Action
import kotlinx.coroutines.delay

@Composable
fun ListScreen(lists: List<TodoList>) {
    val focusManager = LocalFocusManager.current
    var focusDirectionToMove by remember { mutableStateOf<FocusDirection?>(null) }

    val dispatch = LocalDispatch.current
    val wrappedDispatch: (Any) -> Any = { action ->
        when (action) {
            is Action.AddTodo, is Action.AddTodoAsSibling -> focusDirectionToMove = FocusDirection.Down
            is Action.DeleteTodo -> focusDirectionToMove = FocusDirection.Up
        }

        dispatch(action)
    }

    TodoListColumn(lists, wrappedDispatch)

    LaunchedEffect(lists) {
        focusDirectionToMove?.let(focusManager::moveFocus)
        focusDirectionToMove = null
    }
}

@Composable
private fun TodoListColumn(
    lists: List<TodoList>,
    dispatch: (Any) -> Any
) {
    LazyColumn {
        for (list in lists) {
            item {
                ListTitle(
                    title = list.title,
                    onValueChange = { dispatch(Action.UpdateListTitle(list, it)) },
                    onDoneAction = { dispatch(Action.AddTodo(list)) }
                )
            }

            items(list.items) { item ->
                AnimatedTodoVisibility {
                    TodoRow(
                        text = item.text,
                        isDone = item.isDone,
                        callbacks = TodoRowCallbacks(item, dispatch)
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedTodoVisibility(block: @Composable () -> Unit) {
    val alpha = remember { Animatable(0F) }

    LaunchedEffect(true) {
        alpha.animateTo(1F)
    }

    Box(modifier = Modifier.graphicsLayer(alpha = alpha.value)) {
        block()
    }
}

@Preview(showBackground = true, name = "Single todo list")
@Composable
fun ListPreview() {
    TickTheme {
        Scaffold {
            ListScreen(
                listOf(
                    TodoList(
                        title = "Work, 23rd Feb",
                        items = listOf(
                            Todo(
                                text = "Book that meeting",
                                isDone = false
                            )
                        )
                    )
                )
            )
        }
    }
}
