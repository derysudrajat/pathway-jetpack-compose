package id.derysudrajat.compoststate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.derysudrajat.compoststate.utils.generateRandomTodoItem
import kotlin.random.Random

@Composable
fun TodoBody(
    items: List<TodoItem>,
    onItemAdd: (TodoItem) -> Unit,
    onItemRemove: (TodoItem) -> Unit
) {
    Column {
        TodoItemInputBackground(elevate = true, modifier = Modifier.fillMaxWidth()) {
            TodoItemEntryInput(onItemComplete = onItemAdd)
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                TodoRow(
                    todoItem = todo,
                    modifier = Modifier.fillMaxWidth(),
                    onItemClick = onItemRemove
                )
            }
        }
        Button(
            onClick = { onItemAdd(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Add random item")
        }
    }
}

@Composable
fun TodoRow(
    todoItem: TodoItem,
    modifier: Modifier = Modifier,
    onItemClick: (TodoItem) -> Unit,
    iconAlpha: Float = remember(todoItem.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClick(todoItem) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = todoItem.task)
        Icon(
            imageVector = todoItem.icon.imgVector,
            tint = LocalContentColor.current.copy(iconAlpha),
            contentDescription = stringResource(id = todoItem.icon.contentDescription)
        )
    }
}


private fun randomTint(): Float = Random.nextFloat().coerceIn(0.3f, 0.9f)

@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }

    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }

    TodoItemInput(text, setText, icon, setIcon, submit, text.isNotBlank()) {
        TodoEditButton(onClick = submit, text = "Add", enable = text.isNotBlank())
    }
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconVisible: Boolean,
    buttonSlot: @Composable () -> Unit
) {
    Column {
        Row(
            Modifier.padding(horizontal = 16.dp)
        ) {
            TodoInputText(
                text = text,
                onTextChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onImeAction = submit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }
        if (iconVisible) AnimatedIconRow(
            icon,
            onIconChange,
            Modifier
                .padding(top = 8.dp)
                .padding(bottom = 8.dp)
        )
        else Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun TodoInputPreview() {
    TodoInputPreview()
}


@Preview(showBackground = true)
@Composable
fun TodoBodyPreview() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Buy Books", TodoIcon.Square),
        TodoItem("Morning Workout", TodoIcon.Done),
    )
    TodoBody(items, {}, {})
}