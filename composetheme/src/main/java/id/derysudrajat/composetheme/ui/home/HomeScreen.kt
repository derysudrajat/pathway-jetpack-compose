package id.derysudrajat.composetheme.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.derysudrajat.composetheme.R
import id.derysudrajat.composetheme.data.Post
import id.derysudrajat.composetheme.data.PostRepo
import id.derysudrajat.composetheme.ui.theme.JetNewsTheme
import java.util.*

@ExperimentalMaterialApi
@Composable
fun HomeScreen() {
    val featured = remember { PostRepo.getFeaturedPost() }
    val posts = remember { PostRepo.getPosts() }
    JetNewsTheme {
        Scaffold(
            topBar = { AppBar() }
        ) { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                item { Header(text = stringResource(id = R.string.top)) }
                item { FeaturedPost(post = featured, modifier = Modifier.padding(16.dp)) }
                item { Header(text = stringResource(id = R.string.popular)) }
                items(posts) { post ->
                    PostItem(post = post)
                    Divider(startIndent = 72.dp)
                }
            }
        }
    }
}

@Composable
private fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Palette,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        },
        title = {
            Text(text = stringResource(R.string.app_title))
        },
        backgroundColor = MaterialTheme.colors.primarySurface
    )
}

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.onSurface.copy(0.1f),
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier.semantics { heading() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun PostMetaData(
    post: Post,
    modifier: Modifier = Modifier
) {
    val divider = "  •  "
    val tagDivider = "  "
    val text = buildAnnotatedString {
        append(post.metadata.date)
        append(divider)
        append(stringResource(id = R.string.read_time, post.metadata.readTimeMinutes))
        append(divider)
        val tagStyle = MaterialTheme.typography.overline.toSpanStyle().copy(
            background = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        )
        post.tags.forEachIndexed { index, tag ->
            if (index == 0) append(tagDivider)
            withStyle(tagStyle) {
                append(" ${tag.uppercase(Locale.getDefault())}")
            }
        }
    }
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = modifier
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clickable { /*todo*/ }
            .padding(vertical = 8.dp),
        icon = {
            Image(
                painter = painterResource(post.imageThumbId),
                contentDescription = null,
                modifier = Modifier.clip(shape = MaterialTheme.shapes.small)
            )
        },
        text = {
            Text(text = post.title)
        },
        secondaryText = {
            PostMetaData(post)
        }
    )
}

@Composable
fun FeaturedPost(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* todo */ }
        ) {
            Image(
                painter = painterResource(post.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 180.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            val padding = Modifier.padding(horizontal = 16.dp)
            Text(
                text = post.title,
                style = MaterialTheme.typography.h6,
                modifier = padding
            )
            Text(
                text = post.metadata.author.name,
                style = MaterialTheme.typography.body2,
                modifier = padding
            )
            PostMetaData(post, padding)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun MainSurface(
    isDark: Boolean? = false,
    layout: @Composable () -> Unit,
) {
    JetNewsTheme(darkTheme = isDark ?: false) {
        Surface { layout() }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun PreviewPostIte() {
    val post = remember { PostRepo.getFeaturedPost() }
    MainSurface {
        PostItem(post)
    }
}

@Preview(name = "Featured Post", showBackground = true)
@Composable
private fun PreviewPostFeatured() {
    val post = remember { PostRepo.getFeaturedPost() }
    MainSurface {
        FeaturedPost(post)
    }
}

@Preview(name = "Featured Post - Dark", showBackground = true)
@Composable
private fun PreviewPostFeaturedDark() {
    val post = remember { PostRepo.getFeaturedPost() }
    MainSurface(true) {
        FeaturedPost(post)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeader() {
    MainSurface {
        Header(text = "Top story")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAppBar() {
    MainSurface {
        AppBar()
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun PreviewHomer() {
    HomeScreen()
}