package id.derysudrajat.compoststate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.derysudrajat.compoststate.utils.generateRandomTodoItem
import kotlin.random.Random

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
    Column {
        val enableTopSection = currentlyEditing == null
        TodoItemInputBackground(elevate = true, modifier = Modifier.fillMaxWidth()) {
            if (enableTopSection) {
                TodoItemEntryInput(onItemComplete = onItemAdd)
            } else Text(
                text = "Editing Item",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                if (currentlyEditing?.id == todo.id) TodoItemInlineEditor(
                    item = currentlyEditing,
                    onEditItemChange = onEditItemChange,
                    onEditDone = onEditDone,
                    onRemoveItem = { onItemRemove(todo) }
                ) else TodoRow(
                    todoItem = todo,
                    modifier = Modifier.fillMaxWidth(),
                    onItemClick = { onStartEdit(it) }
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

@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submit = onEditDone,
    iconVisible = true,
    buttonSlot = {
        Row {
            val shrinkButtons = Modifier.widthIn(20.dp)
            TextButton(onClick = onEditDone, modifier = shrinkButtons) {
                Text(
                    text = "\uD83D\uDCBE",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(30.dp)
                )
            }
            TextButton(onClick = onRemoveItem, modifier = shrinkButtons) {
                Text(
                    text = "❌",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(30.dp)
                )
            }
        }
    }
)


@Preview(showBackground = true)
@Composable
fun TodoInputPreview() {
    TodoItemInput(
        text = "",
        onTextChange = {},
        icon = TodoIcon.Done,
        onIconChange = {},
        submit = {},
        iconVisible = true
    ) {
        TodoEditButton(onClick = { }, text = "Add", enable = true)
    }
}


@Preview(showBackground = true)
@Composable
fun TodoBodyPreview() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Buy Books", TodoIcon.Square),
        TodoItem("Morning Workout", TodoIcon.Done),
    )
    TodoBody(items, null, {}, {}, {}, {}, {})
}

@Preview(showBackground = true)
@Composable
fun InLineEditorPreview() {
    val todo = remember { generateRandomTodoItem() }
    TodoItemInlineEditor(item = todo, onEditItemChange = {}, onEditDone = { }) {}
}