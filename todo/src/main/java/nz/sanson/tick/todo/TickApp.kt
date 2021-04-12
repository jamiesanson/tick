package nz.sanson.tick.todo

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import nz.sanson.tick.todo.di.ApplicationModule
import nz.sanson.tick.todo.feature.list.ListObservationMiddleware
import nz.sanson.tick.todo.feature.navigation.NavigationReducer
import nz.sanson.tick.todo.redux.createThunkMiddleware
import org.koin.core.context.startKoin
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.combineReducers
import org.reduxkotlin.createThreadSafeStore

/**
 * [createApp] wires up all the necessary Redux components, returning the [Store] to the consuming
 * frontend.
 */
fun createApp(context: Context, applicationScope: CoroutineScope): Store<AppState> {
    startKoin {
        modules(ApplicationModule(context, applicationScope))
    }

    val reducer = combineReducers(NavigationReducer, RootReducer)

    val middleware = applyMiddleware(
        createThunkMiddleware(),
        ListObservationMiddleware
    )

    val store = createThreadSafeStore(reducer, AppState(), middleware)

    store.dispatch(Action.SeedDatabaseIfEmpty())

    return store
}
