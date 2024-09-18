package com.example.manitobahistoricalsocietyapp.info_pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.manitobahistoricalsocietyapp.R
import com.example.manitobahistoricalsocietyapp.navigation.AboutDestination
import com.example.manitobahistoricalsocietyapp.ui.theme.AppTheme

@Composable
fun AboutPage(
    onBackClick: () -> Unit,
    navigateToRoute: (String) -> Unit,

    modifier: Modifier = Modifier) {
    InfoPageScaffolding(
        title = AboutDestination.title,
        modifier = modifier.padding(20.dp),
        navigateToRoute = navigateToRoute,
        onBackClick = onBackClick,
        content = {
            Text(text = stringResource(id = R.string.about_text),
                style = MaterialTheme.typography.bodyLarge,
                )
        }
    )

//    OutlinedCard(
//        /*colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//        ),*/
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
//        modifier = modifier.padding(5.dp)
//
//        ) {
//        Text(
//            text = stringResource(id = R.string.about_title),
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier
//                .padding(10.dp)
//        )
//
//        Text(
//            text = stringResource(id = R.string.about_text),
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier
//                .padding( 10.dp)
//        )
//
//    }
}



@PreviewLightDark
@Composable
fun PreviewAboutPage(){
    AppTheme {
        Surface {
            AboutPage(
                navigateToRoute = {},
                onBackClick = {}
            )
        }
    }
}