# pathway-jetpack-compose
Example of Jetpack Compose Implementation


## Track #1 Compose Basics
<a><img src="https://live.staticflickr.com/65535/51089612817_7f49d87094_b.jpg" width=70% alt="Screenshot 2021-04-02 192558"></a>

## Track #2 Compose Layout
* Understand Modifier
* Understand Scaffold
* Working with List

<a><img src="https://live.staticflickr.com/65535/51110727864_a74e7d3ef5_z.jpg" width=70% alt="Screenshot 2021-04-12 133303"></a>

* Create your own Custom Layout

<a><img src="https://live.staticflickr.com/65535/51110729429_c37a1d6312_z.jpg" width=70% alt="Screenshot 2021-04-12 133407"></a>

* Working with ConstrainstLayout

<a><img src="https://live.staticflickr.com/65535/51110982061_290f46427a_z.jpg" width=70% alt="Screenshot 2021-04-12 133626"></a>

* Understand about Intrinsics

<a><img src="https://live.staticflickr.com/65535/51111760640_ea187819b2_z.jpg" width=70% alt="Screenshot 2021-04-12 133546"></a>

## Track #3 Compose State

* Todo Screen

<a><img src="https://user-images.githubusercontent.com/32610660/132662393-e875ea93-e813-4e37-bcc9-532fb7b25803.png" width=70% alt="Todo Screen"></a>

* Todo Component

<a><img src="https://user-images.githubusercontent.com/32610660/132662676-7d4c28d3-c7d3-49f0-9c2a-1cb9f7280363.png" width=70% alt="Todo Component"></a>

* We using StateLess strategy using `Kotlin Lambda`

```kotlin
@Composable
fun TodoBody(
    items: List<TodoItem>,
    currentlyEditing: TodoItem?,
    onItemAdd: (TodoItem) -> Unit,
    onItemRemove: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit
) {
  //... write some magic here
}
```

* On TodoViewModel we change `mutableLiveData` to `mutableStateList`

```kotlin
class TodoViewModel : ViewModel() {

    var todoItems = mutableStateListOf<TodoItem>()
        private set

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
    }
    
    // ... another method 
}
```

* To make it simple and state less this new way to implement view model

```kotlin
@Composable
private fun TodoActivityScreen(model: TodoViewModel) {
    TodoBody(
        items = model.todoItems,
        currentlyEditing = model.currentEditItem,
        onItemAdd = model::addItem,
        onItemRemove = model::removeItem,
        onStartEdit = model::onEditItemSelected,
        onEditItemChange = model::onEditItemChange,
        onEditDone = model::onEditDone
    )
}
```
