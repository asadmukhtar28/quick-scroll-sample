package com.asad.quickscroll

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asad.quickscroll.ui.theme.QuickScrollTheme

private val contactsList = arrayListOf<String>().apply {
    add("Bilal")
//    add("Rahul")
//    add("Sharat")
    add("Sumit")
    add("Zuhaib")
    add("Fawad")
    add("Gohar")
//    add("Elsadiq")
    add("Harron")
//    add("Altamesh")
    add("Ijaz")
    add("Jawed")
//    add("Sunny")
//    add("Qauid e Azam")
    add("Qasim")
    add("Rosy")
    add("Tony")
//    add("Ali")
    add("Willium")
//    add("Lulu")
    add("Lukman")
    add("Mohammad")
    add("Nitesh")
    add("Komal")
    add("Asad")
    add("Usman")
//    add("Abrar")
//    add("Khurrum")
//    add("Hamza")
//    add("Arslan")
//    add("Hakeem")
    add("Danish")
    add("Ch Ali")
    add("Uganda")
    add("Vox")
    add("Emily")
    add("Pakeeza")
    add("Omar")
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickScrollTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuickScrollListing(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contactsList
                    )
                }
            }
        }
    }
}

@Composable
fun QuickScrollListing(modifier: Modifier = Modifier, contactsList: List<String> = emptyList()) {
    val list = contactsList.sorted()
    val alphabetIndexesList = list.map { it.trim().first().uppercaseChar() }.distinct().sorted()
    val indexedLazyColumnScrollState = rememberLazyListState()
    val contactsLazyColumnScrollState = rememberLazyListState()
    var indexedLazyColumnScrollPosition by rememberSaveable {
        mutableStateOf("")
    }
    val letterToIndexMap = alphabetIndexesList.associateWith { letter ->
        list.indexOfFirst { it.startsWith(letter, ignoreCase = true) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .fillMaxHeight()
                    .background(color = Color.Red.copy(alpha = 0.1f)),
                state = contactsLazyColumnScrollState
            ) {
//            item { ItemHeader() }
                items(list) {
                    ItemListView(name = it)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Blue.copy(0.1f))
                    .align(Alignment.CenterVertically)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .pointerInput(Unit) {
                            detectDragGestures(onDragStart = { offset: Offset ->
                                val index = findClosestEmojiIndex(
                                    offset.y, indexedLazyColumnScrollState
                                )
                                if (index != -1) {
                                    indexedLazyColumnScrollPosition =
                                        alphabetIndexesList[index].toString()
                                }
                            },
                                onDragCancel = { indexedLazyColumnScrollPosition = "" },
                                onDragEnd = { indexedLazyColumnScrollPosition = "" },
                                onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                    val index = findClosestEmojiIndex(
                                        change.position.y, indexedLazyColumnScrollState
                                    )
                                    if (index != -1) {
                                        indexedLazyColumnScrollPosition =
                                            alphabetIndexesList[index].toString()
                                    }
                                })
                        },
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    state = indexedLazyColumnScrollState
                ) {
                    itemsIndexed(alphabetIndexesList) { index, item ->
                        val scale by animateFloatAsState(
                            targetValue = if (indexedLazyColumnScrollPosition.isNotEmpty() && item == indexedLazyColumnScrollPosition.first()) 2f else 1f,
                            animationSpec = tween(100),
                            label = "Scale Animation"
                        )
                        Text(text = item.toString(),
                            fontSize = 16.sp,
                            modifier = Modifier.graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            })
                    }
                }
            }
        }

        AnimatedScrollingIndex(
            modifier = Modifier.align(Alignment.Center), indexedLazyColumnScrollPosition
        )
    }

    LaunchedEffect(key1 = indexedLazyColumnScrollPosition) {
        if (indexedLazyColumnScrollPosition.isNotEmpty()) {
            val targetIndex = letterToIndexMap[indexedLazyColumnScrollPosition[0]] ?: 0
            Log.d(
                "asad-debug",
                "targetIndex: $targetIndex, indexedLazyColumnScrollPosition: $indexedLazyColumnScrollPosition"
            )
            Log.d("asad-debug", "Full Contact List: $list")
            Log.d("asad-debug", "Alphabet Index List: $alphabetIndexesList")
            contactsLazyColumnScrollState.scrollToItem(targetIndex)
        }
    }
}

@Preview
@Composable
fun AnimatedScrollingIndex(modifier: Modifier = Modifier, value: String = "A") {
    val scalingTransitionAnimation = rememberInfiniteTransition(label = "")
    val animation = scalingTransitionAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    if (value.isNotEmpty()) {
        Text(
            modifier = modifier
                .graphicsLayer {
                    scaleX = animation.value
                    scaleY = animation.value
                }
                .drawBehind {
                    drawRoundRect(
                        color = Color.Gray.copy(0.1f),
                        cornerRadius = CornerRadius(10f)
                    )
                }
                .padding(32.dp), text = value
        )
    }
}

private fun findClosestEmojiIndex(offsetY: Float, lazyListState: LazyListState): Int {
    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo

    // Iterate through visible items to find the one at the offset
    for (item in visibleItems) {
        if (offsetY >= item.offset && offsetY <= item.offset + item.size) {
            return item.index
        }
    }
    return -1
}

@Preview
@Composable
fun ItemListView(modifier: Modifier = Modifier, name: String = "A") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.weight(1f, true),
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun ItemHeader(modifier: Modifier = Modifier) {
    Text(
        text = "C", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickScrollTheme {
        QuickScrollListing(modifier = Modifier.fillMaxSize(), contactsList)
    }
}