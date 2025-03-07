package com.TichonTechnologies.manitobahistoricalsocietyapp.info_pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.TichonTechnologies.manitobahistoricalsocietyapp.R

import com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding.DisplayTopAppBar
import com.TichonTechnologies.manitobahistoricalsocietyapp.site_scaffolding.TopBarNavIcon




//Scaffolding used for any added info pages
@Composable
fun InfoPageScaffolding(
    title: String,
    content: @Composable () -> Unit,
    onBackClick: () -> Unit,
    navigateToRoute: (String) -> Unit,

    modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            DisplayTopAppBar(topLeftIcon = {
                TopBarNavIcon(
                painter = painterResource(id = R.drawable.arrow_back_ios),
                contentDescription = "Back",
                onClick = onBackClick
                , modifier = Modifier
                        .size(50.dp)
                        .padding(start = 5.dp)
            )}
                , title = { Text(text = title) },
                navigateToRoute = navigateToRoute,
                modifier = modifier)

        }

    ) {innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        ){
            Row(modifier) {
                content()
            }
        }

    }
}