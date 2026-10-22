package com.example.week2_assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.week2_assignment.data.HardwareItem

@Composable
fun DevClusterScreen(hardwareList: List<HardwareItem>) {
    var totalCost by remember { mutableDoubleStateOf(0.0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val itemQuantities = remember { mutableStateMapOf<String, Int>() }

    val filteredList = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            hardwareList
        } else {
            hardwareList.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        HardwareSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { newText -> searchQuery = newText }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Toplam Sepet Tutarı: ")
                }
                withStyle(style = SpanStyle(color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)) {
                    append("$$totalCost")
                }
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = filteredList,
                key = { it.id }
            ) { item ->
                val quantity = itemQuantities[item.id] ?: 0

                HardwareCard(
                    item = item,
                    quantity = quantity,
                    onIncreaseClick = { clickedItem ->
                        if (quantity < clickedItem.stockCount) {
                            itemQuantities[clickedItem.id] = quantity + 1
                            totalCost += clickedItem.cost
                        }
                    },
                    onDecreaseClick = { clickedItem ->
                        if (quantity > 0) {
                            itemQuantities[clickedItem.id] = quantity - 1
                            totalCost -= clickedItem.cost
                        }
                    }
                )
            }
        }
    }
}