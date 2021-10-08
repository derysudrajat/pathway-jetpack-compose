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

## Track #4 Compose Animation

* Layout Component

<a><img src="https://user-images.githubusercontent.com/32610660/133020525-36f50536-6c1f-483c-882c-c9b642120723.png" width=70% alt="Animation Component"></a>

* Full Layout

<a><img src="https://user-images.githubusercontent.com/32610660/133020847-699eabd4-9157-4e45-bdae-5540bcc6e3b0.png" width=70% alt="Layout Animation"></a>

* Common Animation

The One of Common Animation in Compose is `AnimatedVisibility` this animation still in `ExperimentalAnimationApi`
```kotlin
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopicRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}
```

## Track #5 Compose Theming

* Layout Component

<a><img src="https://user-images.githubusercontent.com/32610660/136497609-a2effaef-94ee-439b-9cd1-f510c2f4900e.png" width=70% alt="Animation Component"></a>

* Full Layout (Dark and Light)

<a><img src="https://user-images.githubusercontent.com/32610660/136497613-a6639ca1-127c-4727-bd6f-d5f2b29a7f66.png" width=70% alt="Animation Component"></a>

You can define the light theme color and dark theme color in `Theme.kt`

```kotlin
private val LightColors = lightColors(
    primary = Red700,
    primaryVariant = Red900,
    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    onSecondary = Color.White,
    error = Red800,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
private val DarkColors = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red300,
    onSecondary = Color.Black,
    error = Red200
)
```
You can define your custom theme in here
```kotlin
@Composable
fun JetNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = JetNewsTypography,
        shapes = JetNewsShapes,
        content = content
    )
}
```

