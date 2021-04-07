package id.derysudrajat.composelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.CoilImage
import id.derysudrajat.composelayout.ui.theme.JetpackComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTheme {
        Greeting("Android")
    }
}

/**
 * understand about modifier in compose
 */
@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    /**
     * 1. we given 16.dp padding from main content of row
     * 2. add clickable to item which already have padding
     * 3. add background when click
     * 4. add clip rounded corner shape in click able area
     * 5. we given padding again in outer of clickable area
     */
    Row(modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable { /*lick action */ }
        .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(0.2f)
        ) {
            // image goes here
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    JetpackComposeTheme {
        PhotographerCard()
    }
}

/**
 * understanding scaffold component
 * scaffold is basic layout structure which implement Material Design
 * so we can using common material design component like TopAppBar,
 * BottomAppBar, FloatingActionButton, and Drawer using Scaffold
 */
@Composable
fun ScaffoldLayout() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Example of scaffold")
                },
                actions = {
                    IconButton(onClick = { /*do something here*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Preview
@Composable
fun ScaffoldLayoutPreview() {
    JetpackComposeTheme {
        ScaffoldLayout()
    }
}

@Composable
fun LazyList() {
    Scaffold { innerPadding ->
        val listSize = 100
        // we want to save the scrolling position with this state that can also
        // be used to programmatically scroll the list
        val scrollState = rememberLazyListState()
        // we want to save the coroutine scope where out animated scroll will be executed
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        // 0 is the first item index
                        scrollState.animateScrollToItem(0)
                    }
                }, Modifier.weight(1f)) {
                    Text(text = "Scroll to the top")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(onClick = {
                    coroutineScope.launch {
                        // listSize - 1 is the last index of the list
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                }, Modifier.weight(1f)) {
                    Text(text = "Scroll to the end")
                }
            }
            LazyColumn(state = scrollState) {
                items(listSize) {
                    ImageListItem(index = it, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ImageListItem(index: Int, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(16.dp)
    ) {
        CoilImage(
            data = "https://developer.android.com/images/brand/Android_Robot.png",
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Preview
@Composable
fun LazyListPreview() {
    JetpackComposeTheme {
        LazyList()
    }
}

