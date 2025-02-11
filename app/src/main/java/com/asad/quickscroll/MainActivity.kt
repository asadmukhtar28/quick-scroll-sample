package com.asad.quickscroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asad.quickscroll.ui.theme.QuickScrollTheme

private val contactsList = arrayListOf<String>().apply {
    add("Bilal")
    add("Rahul")
    add("Sharat")
    add("Sumit")
    add("Zuhaib")
    add("Fawad")
    add("Gohar")
    add("Elsadiq")
    add("Harron")
    add("Altamesh")
    add("Ijaz")
    add("Jawed")
    add("Sunny")
    add("Qauid e Azam")
    add("Qasim")
    add("Rosy")
    add("Tony")
    add("Ali")
    add("Willium")
    add("Lulu")
    add("Lukman")
    add("Mohammad")
    add("Nitesh")
    add("Komal")
    add("Asad")
    add("Usman")
    add("Abrar")
    add("Khurrum")
    add("Hamza")
    add("Arslan")
    add("Hakeem")
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
    val alphabetIndexesList = list.map { it.first().uppercaseChar() }.distinct().sorted()
    val indexedLazyColumnScrollState = rememberLazyListState()
    val contactsLazyColumnScrollState = rememberLazyListState()
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
                .align(Alignment.CenterVertically)
        ) {
            LazyColumn(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = indexedLazyColumnScrollState
            ) {
                items(alphabetIndexesList) {
                    Text(text = it.toString())
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemListView(modifier: Modifier = Modifier, name: String = "") {
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