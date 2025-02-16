package com.asad.quickscrollsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asad.quickscroll.QuickScroll
import com.asad.quickscrollsample.ui.theme.QuickScrollTheme

private val contactsList = arrayListOf<String>().apply {
    add("1231")
    add("231")
    add("#")
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
                    ContactsListing(
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
fun ContactsListing(modifier: Modifier = Modifier, contactsList: List<String> = emptyList()) {
    val list = rememberSaveable {
        contactsList.sorted()
    }
    val contactsLazyColumnScrollState = rememberLazyListState()
    val groupedContacts = rememberSaveable {
        list.groupBy {
            val firstChar = it.first()
            when {
                firstChar.isDigit() -> '#' // Group all numbers under '#'
                else -> firstChar.uppercaseChar()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red.copy(alpha = 0.1f)),
            state = contactsLazyColumnScrollState
        ) {
            groupedContacts.forEach { (letter, contactsList) ->
                item { ItemHeader(header = letter.toString()) }
                items(contactsList) {
                    ItemListView(name = it)
                }
            }
        }

        QuickScroll(
            modifier = Modifier.align(Alignment.CenterEnd),
            Modifier.align(Alignment.Center), contactsList = list
        ) { position: Int ->
            LaunchedEffect(key1 = position) {
                if (position != -1) {
                    contactsLazyColumnScrollState.scrollToItem(position)
                }
            }
        }

    }
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
        /*Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )*/
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
fun ItemHeader(modifier: Modifier = Modifier, header: String = "") {
    Text(
        text = header, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickScrollTheme {
        ContactsListing(modifier = Modifier.fillMaxSize(), contactsList)
    }
}