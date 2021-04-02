package id.derysudrajat.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.derysudrajat.compose.ui.BasicsTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainUI() }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainUI() {
        MaterialTheme {
            val typography = MaterialTheme.typography
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.header),
                    contentDescription = "Header",
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    "A day wandering through the sand hills " +
                            "in Shark Fin Cove, and a few of the " +
                            "sights I saw",
                    style = typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Davenport, California",
                    style = typography.body2
                )
                Text(
                    "December 2020",
                    style = typography.body2
                )
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        var isSelected by remember { mutableStateOf(false) }
        val backgroundColor by animateColorAsState(if (isSelected) Color.Blue else Color.Transparent)

        Text(
            text = "Hello $name!",
            modifier = Modifier
                .padding(16.dp)
                .background(color = backgroundColor)
                .clickable { isSelected = !isSelected }
        )
    }

    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        Surface(color = Color.White) {
            content()
        }
    }

    @Composable
    fun Counter(count: Int, updateCount: (Int) -> Unit) {
        Button(
            onClick = { updateCount(count + 1) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (count > 5) Color.Green else Color.LightGray
            )
        ) {
            Text(text = "I've been clicked $count times")
        }
    }

    @Composable
    fun MyScreenContent(names: List<String> = List(100) { "Android #$it" }) {
        val counterState = remember { mutableStateOf(0) }

        Column(modifier = Modifier.fillMaxHeight()) {
            NameList(names, Modifier.weight(1f))
            Counter(
                count = counterState.value,
                updateCount = { newCount ->
                    counterState.value = newCount
                }
            )
        }
    }

    @Composable
    fun NameList(names: List<String>, modifier: Modifier = Modifier) {
        LazyColumn(modifier = modifier) {
            items(names) { name ->
                Greeting(name)
                Divider(color = Color.Black)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AnotherPreview() {
        BasicsTheme { MyApp { MyScreenContent() } }
    }

}