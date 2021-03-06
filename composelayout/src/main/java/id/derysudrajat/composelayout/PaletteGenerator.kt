package id.derysudrajat.composelayout

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.github.dhaval2404.imagepicker.ImagePicker
import id.derysudrajat.composelayout.PaletteHelper.copyTextToCliBoard

class PaletteGenerator : ComponentActivity() {
    private val model by viewModels<PaletteViewModel>()

    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val img =
            "https://img.freepik.com/free-vector/modern-metropolis-night-cityscape-cartoon-vector_1441-2959.jpg?size=626&ext=jpg"
        model.getPalette(img = img, this)
        setContent {
            MaterialTheme {
                GeneratorBody(
                    model.paletteItems,
                    onAddImage = { addNewImage() },
                    model.currentImage.value, this
                )
            }
        }
    }

    private fun addNewImage() {
        ImagePicker.with(this)
            .createIntent { activityForResult.launch(it) }
    }

    private val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        result.handleImagePicker(this) { model.getPalette(it.toString(), this) }
    }

    private fun ActivityResult.handleImagePicker(
        context: Context,
        onResult: (Uri) -> Unit
    ) {
        when (this.resultCode) {
            Activity.RESULT_OK -> onResult(this.data?.data ?: "".toUri())
            ImagePicker.RESULT_ERROR -> Toast.makeText(
                context as Activity,
                ImagePicker.getError(this.data), Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ItemColor(textColor: String, backgroundColor: String, position: Int, activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = if (position == 0) 16.dp else 0.dp,
            topEnd = if (position == 0) 16.dp else 0.dp,
            bottomStart = if (position == listName.size - 1) 16.dp else 0.dp,
            bottomEnd = if (position == listName.size - 1) 16.dp else 0.dp,
        ),
        backgroundColor = getColor(backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                text = listName[position],
                style = TextStyle(color = getColor(textColor))
            )
            Text(
                text = backgroundColor,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    color = getColor(textColor),
                    fontSize = 22.sp
                ),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { backgroundColor.copyTextToCliBoard(activity) }) {
                Icon(Icons.Rounded.ContentCopy, null, tint = getColor(textColor))
            }
        }
    }
}

@Composable
fun PaletteTopBar(contentColor: String, backgroundColor: String) {
    TopAppBar(
        title = {
            Text(text = "Material Palette Generator")
        },
        backgroundColor = getColor(backgroundColor),
        contentColor = getColor(contentColor)
    )
}

@Composable
fun ChooseImageButton(textColor: String, backgroundColor: String, onAddImage: () -> Unit) {
    FloatingActionButton(
        onClick = onAddImage,
        backgroundColor = getColor(backgroundColor),
        contentColor = getColor(textColor)
    ) {
        Icon(Icons.Rounded.Add, null)
    }
}

@ExperimentalCoilApi
@Composable
fun GeneratorBody(
    paletteList: List<Palette.Swatch?>,
    onAddImage: () -> Unit,
    img: String,
    activity: Activity
) {
    println("PaletteList = $paletteList")
    if (paletteList.isNotEmpty()) {

        val dominantText = PaletteHelper.getHexTextColor(paletteList[0], activity)
        val dominantBg = PaletteHelper.getHexBackgroundColor(paletteList[0], activity)

        val vibrantColor = PaletteHelper.getHexBackgroundColor(paletteList[2], activity)
        val vibrantText = PaletteHelper.getHexTextColor(paletteList[2], activity)

        val darkVibrantColor = PaletteHelper.getHexBackgroundColor(paletteList[3], activity)

        activity.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = android.graphics.Color.parseColor(darkVibrantColor)
        }

        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = { PaletteTopBar(vibrantText, vibrantColor) },
            backgroundColor = getColor(dominantBg).copy(0.5f),
            floatingActionButton = {
                ChooseImageButton(dominantText, dominantBg, onAddImage)
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Image(
                        painter = rememberImagePainter(img),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                itemsIndexed(paletteList) { index, palette ->
                    println("Palette = $palette, index = $index")
                    val textColor = PaletteHelper.getHexTextColor(palette, activity)
                    val backgroundColor = PaletteHelper.getHexBackgroundColor(palette, activity)
                    ItemColor(textColor, backgroundColor, position = index, activity)
                }
                item { Spacer(modifier = Modifier.height(68.dp)) }
            }
        }
    }

}

fun getColor(colorString: String): Color {
    return Color(android.graphics.Color.parseColor(colorString))
}

private val listName = listOf(
    "Dominant", "Light Vibrant", "Vibrant", "Dark Vibrant",
    "Light Muted", "Muted", "Dark Muted",
)

object PaletteHelper {
    fun getHexTextColor(palette: Palette.Swatch?, context: Context): String =
        String.format(
            "#%06X", 0xFFFFFF and (palette?.titleTextColor ?: getDefaultTextColor(context))
        )

    fun getHexBackgroundColor(palette: Palette.Swatch?, context: Context): String =
        String.format(
            "#%06X", 0xFFFFFF and (palette?.rgb ?: getDefaultBackgroundColor(context))
        )

    private fun getDefaultTextColor(context: Context): Int =
        ContextCompat.getColor(context, R.color.black)

    private fun getDefaultBackgroundColor(context: Context): Int =
        ContextCompat.getColor(context, R.color.white)

    fun String.copyTextToCliBoard(activity: Activity) {
        val text = this
        (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)
            ?.apply {
                setPrimaryClip(ClipData.newPlainText("Color", text))
            }
        Toast.makeText(activity, "Copying $text to clipboard", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    PaletteTopBar("#ffffff", "#004d40")
}

@Preview(showBackground = true)
@Composable
fun PreviewItemColor() {
    MaterialTheme {
        ItemColor("#ffffff", "#004d40", 0, PaletteGenerator())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFAB() {
    MaterialTheme {
        ChooseImageButton("#ffffff", "#004d40") {}
    }
}

@Preview
@Composable
fun PreviewGeneratorBody() {
    MaterialTheme {
        GeneratorBody(
            listOf(), {},
            "https://img.freepik.com/free-vector/modern-metropolis-night-cityscape-cartoon-vector_1441-2959.jpg?size=626&ext=jpg",
            PaletteGenerator()
        )
    }
}