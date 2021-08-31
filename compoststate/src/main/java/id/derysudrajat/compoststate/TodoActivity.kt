package id.derysudrajat.compoststate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import id.derysudrajat.compoststate.ui.theme.JetpackComposeTheme

class TodoActivity : ComponentActivity() {

    private val model by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TodoActivityScreen(model = model)
                }
            }
        }
    }
}

@Composable
private fun TodoActivityScreen(model: TodoViewModel) {
    TodoBody(
        items = model.todoItems,
        onItemAdd = model::addItem,
        onItemRemove = model::removeItem
    )

}
