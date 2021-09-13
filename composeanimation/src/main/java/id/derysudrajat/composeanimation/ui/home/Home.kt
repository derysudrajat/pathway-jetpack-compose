package id.derysudrajat.composeanimation.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.derysudrajat.composeanimation.R
import id.derysudrajat.composeanimation.ui.theme.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private enum class TabPage {
    Home, Work
}

/**
 * show the entire screen
 */
@Composable
fun Home() {
    val allTask = stringArrayResource(R.array.tasks)
    val allTopics = stringArrayResource(R.array.topics).toList()

    val tasks = remember { mutableStateListOf(*allTask) }
    val firstTopic = allTopics.first()
    val lastTopic = allTopics.last()

    var tabPage by remember { mutableStateOf(TabPage.Home) }

    var weatherLoading by remember { mutableStateOf(false) }

    var expandedTopic by remember { mutableStateOf<String?>(null) }

    var editMessageShown by remember { mutableStateOf(false) }

    suspend fun loadWeather() {
        if (!weatherLoading) {
            weatherLoading = true
            delay(3000L)
            weatherLoading = false
        }
    }

    suspend fun showEditMessage() {
        if (!editMessageShown) {
            editMessageShown = true
            delay(3000L)
            editMessageShown = false
        }
    }

    val lazyListState = rememberLazyListState()

    val backgroundColor by animateColorAsState(if (tabPage == TabPage.Home) Purple100 else Green300)

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HomeTabBar(
                backgroundColor = backgroundColor,
                tabPage = tabPage, onTabSelected = {
                    tabPage = it
                })
        },
        backgroundColor = backgroundColor,
        floatingActionButton = {
            HomeFloatingActionButton(
                extended = lazyListState.isScrollingUp(),
                onClick = {
                    coroutineScope.launch {
                        showEditMessage()
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            state = lazyListState
        ) {
            item { Header(title = stringResource(R.string.weather)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    if (weatherLoading) {
                        LoadingRow()
                    } else {
                        WeatherRow {
                            coroutineScope.launch {
                                loadWeather()
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Topics
            item { Header(title = stringResource(R.string.topics)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(allTopics) { topic ->
                TopicRow(
                    topic = topic,
                    expanded = expandedTopic == topic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            when (topic) {
                                firstTopic -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                lastTopic -> RoundedCornerShape(
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                )
                                else -> RoundedCornerShape(0.dp)
                            }
                        )
                ) {
                    expandedTopic = if (expandedTopic == topic) null else topic
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Task
            item { Header(title = stringResource(R.string.tasks)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            if (tasks.isEmpty()) {
                item {
                    TextButton(onClick = { tasks.clear(); tasks.addAll(allTask) }) {
                        Text(text = stringResource(R.string.add_tasks))
                    }
                }
            }
            items(tasks.size) { index ->
                val task = tasks.getOrNull(index)
                if (task != null) {
                    key(task) {
                        TaskRow(
                            task = task,
                            modifier = Modifier.clip(
                                when (index) {
                                    0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    tasks.lastIndex -> RoundedCornerShape(
                                        bottomStart = 8.dp,
                                        bottomEnd = 8.dp
                                    )
                                    else -> RoundedCornerShape(0.dp)
                                }
                            ),
                            onRemove = { tasks.remove(task) }
                        )
                    }
                }
            }

        }
        EditMessage(editMessageShown)
    }
}

/**
 * show the floating action button
 *
 * @param extended whether the tab should be shown in its expanded state
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeFloatingActionButton(
    extended: Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            AnimatedVisibility(extended) {
                Text(
                    text = stringResource(R.string.edit),
                    modifier = Modifier.padding(start = 8.dp, top = 3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun EditMessage(shown: Boolean) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondary,
            elevation = 4.dp
        ) {
            Text(
                text = stringResource(R.string.edit_message),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
private fun Header(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.typography.h5
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TopicRow(topic: String, expanded: Boolean, modifier: Modifier, onClick: () -> Unit) {
    TopicRowSpacer(visible = expanded)
    val localModifier = if (expanded) modifier.clip(
        RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        )
    ) else modifier
    Surface(
        modifier = localModifier,
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = topic,
                    style = MaterialTheme.typography.body1
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.lorem_ipsum),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopicRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun HomeTabBar(
    backgroundColor: Color,
    tabPage: TabPage,
    onTabSelected: (tabPage: TabPage) -> Unit
) {
    TabRow(
        selectedTabIndex = tabPage.ordinal,
        backgroundColor = backgroundColor,
        indicator = { tabPositions ->
            HomeTabIndicator(tabPositions, tabPage)
        }
    ) {
        HomeTab(
            icon = Icons.Default.Home,
            title = stringResource(R.string.home),
            onClick = { onTabSelected(TabPage.Home) }
        )
        HomeTab(
            icon = Icons.Default.AccountBox,
            title = stringResource(R.string.work),
            onClick = { onTabSelected(TabPage.Work) }
        )
    }
}

@Composable
private fun HomeTabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: TabPage,
) {
    val transition = updateTransition(tabPage, label = "Tab Indicator")
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (TabPage.Home isTransitioningTo TabPage.Work)
                spring(stiffness = Spring.StiffnessVeryLow)
            else spring(stiffness = Spring.StiffnessMedium)
        }, label = "Indicator left"
    ) { page ->
        tabPositions[page.ordinal].left
    }

    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (TabPage.Home isTransitioningTo TabPage.Work)
                spring(stiffness = Spring.StiffnessMedium)
            else spring(stiffness = Spring.StiffnessVeryLow)
        }, label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }

    val color by transition.animateColor(
        label = "Border Color"
    ) { page ->
        if (page == TabPage.Home) Purple700 else Green800
    }

    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, color),
                RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun HomeTab(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}

@Composable
private fun WeatherRow(
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Amber600)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = stringResource(R.string.temperature), fontSize = 24.sp)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.refresh)
            )
        }
    }
}

@Composable
private fun LoadingRow(theAlpha: Float? = 0f) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.75f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = theAlpha ?: alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = theAlpha ?: alpha))
        )
    }
}

@Composable
private fun TaskRow(task: String, modifier: Modifier, onRemove: () -> Unit) {
    val localModifier = modifier
        .fillMaxWidth()
        .swipeToDismiss(onRemove)
    Surface(
        modifier = localModifier,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = task, style = MaterialTheme.typography.body1)
        }
    }
}

private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // used to calculate a settling position of a fling animation
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while (true) {
                // wait for a tosh down event
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetX.stop()
                // prepare for drag events and record velocity of a flag.
                val velocityTracker = VelocityTracker()
                // wait for drag events
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // record the velocity of the drag.
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch { offsetX.snapTo(horizontalDragOffset) }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // consume the gesture event, passed to external
                        change.consumePositionChange()
                    }
                }
                // dragging finished. Calculate the velocity of the flag.
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width)
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    else {
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset {
            IntOffset(offsetX.value.roundToInt(), 0)
        }
}


@Preview(showBackground = true)
@Composable
private fun PreviewHomeTabBar() {
    HomeTabBar(
        backgroundColor = Purple100,
        tabPage = TabPage.Home,
        onTabSelected = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeader() {
    Header(title = stringResource(R.string.weather))
}

@Preview(showBackground = true)
@Composable
private fun PreviewEditMessage() {
    EditMessage(shown = true)
}

@Preview(showBackground = true)
@Composable
private fun PreviewWeatherRow() {
    WeatherRow {}
}


@Preview(showBackground = true)
@Composable
private fun PreviewLoadingRow() {
    LoadingRow(1f)
}

@Preview(showBackground = true)
@Composable
private fun PreviewTopicRow() {
    TopicRow(
        topic = stringArrayResource(R.array.topics).toList().random(),
        expanded = false,
        modifier = Modifier
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun PreviewTopicRowExpanded() {
    TopicRow(
        topic = stringArrayResource(R.array.topics).toList().random(),
        expanded = true,
        modifier = Modifier
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskRow() {
    TaskRow(task = stringArrayResource(R.array.tasks).toList().random(), modifier = Modifier) {}
}

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    Home()
}