package com.example.manitobahistoricalsocietyapp.site_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.storage_classes.SiteDisplayState
import com.example.manitobahistoricalsocietyapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteMainPageTopBar(
    onClickChangeDisplayState: (SiteDisplayState) -> Unit,
    displayState: SiteDisplayState,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {

            /*AnimatedVisibility(
                visible = (displayState != SiteDisplayState.FullMap ),
                enter = slideInHorizontally(
                    initialOffsetX = {fullWidth -> -fullWidth },
                    animationSpec = tween(1000)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = {fullWidth -> -fullWidth },
                    animationSpec = tween(1000)
                )
            ) {
                IconButton(
                    onClick = { onClickChangeDisplayState(SiteDisplayState.FullMap) }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp)
                    )

                }
            }*/
            //On click show full map
            if (displayState != SiteDisplayState.FullMap){
                IconButton(
                    onClick = { onClickChangeDisplayState(SiteDisplayState.FullMap) }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp)


                    )

                }

            }
            //However if it is already full map then display nothing
            else{
                // I have the blank icon button to keep the padding the same
                IconButton(onClick = {  }) {
                    Spacer(modifier = Modifier
                        .size(40.dp))
                }

            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Text(text = "Historical Sites")
        },
        modifier = modifier.animateContentSize(
            tween(500)

        )
    )

}

@Preview
@Composable
private fun PreviewAppBar() {
    AppTheme {
        Surface {
            SiteMainPageTopBar(
                displayState = SiteDisplayState.FullSite,
                onClickChangeDisplayState = {}

            )
        }
    }
}