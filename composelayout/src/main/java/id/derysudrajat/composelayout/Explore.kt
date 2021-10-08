package id.derysudrajat.composelayout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ItemUser(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(0.2f))
            .clickable { }
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(0.5f)
        ) {
            // iamge must be here
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = name
        )
    }
}

@Composable
fun ItemHeader(title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = title,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ItemBox(text: String) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(0.2f))
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            text = text
        )
    }
}

@Composable
fun MainBody(listData: List<Friend>) {
    Scaffold {
        Column {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                items(listData) { friend ->
                    if (friend.type != -1) ItemHeader(title = friend.name)
                    else ItemUser(friend.name)
                }
                item { ItemHeader(title = "Lazy Row") }
                item {
                    LazyRow {
                        items(10) {
                            ItemBox(text = "Box-$it")
                        }
                    }
                }

                item { ItemHeader(title = "Row Repeated 7 times") }
                item { Row { repeat(7) { ItemBox(text = "Box-$it") } } }

                item { ItemHeader(title = "Column Repeated 4 times") }
                item { Column { repeat(4) { ItemBox(text = "Box-$it") } } }
            }
        }
    }
}

val listOfFriend = listOf(
    Friend("Closed Friend", 1),
    Friend("Udin Hamimudin"),
    Friend("Jhon Doe"),
    Friend("Soeloekman Abrahim"),
    Friend("Mamans Soekarman"),
    Friend("Leodarno Wathir"),
)

data class Friend(
    val name: String,
    val type: Int? = -1,
)

@Preview(showBackground = true)
@Composable
fun PreviewItemUser() {
    MaterialTheme {
        ItemUser("Jhon Doe")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemHeader() {
    MaterialTheme {
        ItemHeader(title = "Closed Friend")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemBox() {
    ItemBox("Box-1")
}

@Preview(showBackground = true)
@Composable
fun PreviewBody() {
    MaterialTheme {
        MainBody(listData = listOfFriend)
    }
}