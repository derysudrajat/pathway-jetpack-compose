package id.derysudrajat.compoststate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

    var todoItems = mutableStateListOf<TodoItem>()
        private set

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
    }

    private var currentlyEditPosition by mutableStateOf(-1)
    val currentEditItem: TodoItem? get() = todoItems.getOrNull(currentlyEditPosition)

    fun onEditItemSelected(todoItem: TodoItem) {
        currentlyEditPosition = todoItems.indexOf(todoItem)
    }

    fun onEditDone() {
        currentlyEditPosition = -1
    }

    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }
        todoItems[currentlyEditPosition] = item
    }
}