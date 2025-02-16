package com.asad.quickscroll

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickScroll(
    modifier: Modifier = Modifier,
    animatedIndexModifier: Modifier = Modifier,
    contactsList: List<String> = emptyList(),
    targetIndexToScroll: @Composable (position: Int) -> Unit
) {
    val groupedContacts = rememberSaveable {
        contactsList.groupBy {
            val firstChar = it.first()
            when {
                firstChar.isDigit() -> '#' // Group all numbers under '#'
                else -> firstChar.uppercaseChar()
            }
        }
    }
    val alphabetIndexesList = rememberSaveable {
        groupedContacts.keys.sortedBy {
            if (it == '#') '0' else it // Ensure '#' appears first
        }
    }
    val indexedLazyColumnScrollState = rememberLazyListState()
    var scrollingValue by rememberSaveable {
        mutableStateOf("")
    }
    val letterToIndexMap = rememberSaveable {
        alphabetIndexesList.associateWith { letter ->
            contactsList.indexOfFirst { it.startsWith(letter, ignoreCase = true) }
        }
    }

    Box(
        modifier = modifier
            .heightIn(LocalConfiguration.current.screenHeightDp.dp)
            .drawBehind {
                drawRect(color = Color.Blue.copy(0.1f))
            }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures(onDragStart = { offset: Offset ->
                    val index = findClosestAlphabetIndex(
                        offset.y, indexedLazyColumnScrollState
                    )
                    if (index != -1) {
                        scrollingValue =
                            alphabetIndexesList[index].toString()
                    }
                },
                    onDragCancel = { scrollingValue = "" },
                    onDragEnd = { scrollingValue = "" },
                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                        val index = findClosestAlphabetIndex(
                            change.position.y, indexedLazyColumnScrollState
                        )
                        if (index != -1) {
                            scrollingValue =
                                alphabetIndexesList[index].toString()
                        }
                    })
            },
            verticalArrangement = Arrangement.spacedBy(2.dp),
            state = indexedLazyColumnScrollState
        ) {
            items(alphabetIndexesList, key = { it }) { item ->
                val scale by animateFloatAsState(
                    targetValue = if (scrollingValue.isNotEmpty() && item.toChar() == scrollingValue.first()) 2f else 1f,
                    animationSpec = tween(100),
                    label = "Scale Animation"
                )
                Text(text = item.toString(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(0.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        })
            }
        }
    }
    AnimatedScrollingIndex(
        modifier = animatedIndexModifier, scrollingValue
    )

    if (scrollingValue.isNotEmpty()) {
        val targetIndex = letterToIndexMap[scrollingValue[0]] ?: 0
        targetIndexToScroll.invoke(targetIndex)
    }
}

@Composable
private fun AnimatedScrollingIndex(modifier: Modifier = Modifier, value: String = "A") {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val scalingAnimation = infiniteTransition.animateFloat(
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
                    scaleX = scalingAnimation.value
                    scaleY = scalingAnimation.value
                }
                .padding(0.dp), text = value
        )
    }
}

private fun findClosestAlphabetIndex(offsetY: Float, lazyListState: LazyListState): Int {
    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo

    // Iterate through visible items to find the one at the offset
    for (item in visibleItems) {
        if (offsetY >= item.offset && offsetY <= item.offset + item.size) {
            return item.index
        }
    }
    return -1
}