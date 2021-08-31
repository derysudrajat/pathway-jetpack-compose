package id.derysudrajat.compoststate

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.*

data class TodoItem(
    val task: String,
    val icon: TodoIcon = TodoIcon.Default,
    val id: UUID = UUID.randomUUID()
)

enum class TodoIcon(val imgVector: ImageVector, @StringRes val contentDescription: Int) {
    Square(Icons.Rounded.CropSquare, R.string.cd_expand),
    Done(Icons.Rounded.Done, R.string.cd_done),
    Event(Icons.Rounded.Event, R.string.cd_event),
    Privacy(Icons.Rounded.PrivacyTip, R.string.cd_privacy),
    Trash(Icons.Rounded.RestoreFromTrash, R.string.cd_restore);

    companion object {
        val Default = Square
    }
}