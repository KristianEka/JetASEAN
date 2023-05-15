package com.ekachandra.jetasean.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ekachandra.jetasean.R
import com.ekachandra.jetasean.ui.ViewModelFactory
import com.ekachandra.jetasean.ui.common.UiState
import com.ekachandra.jetasean.ui.theme.JetASEANTheme

@Composable
fun DetailScreen(
    countryId: Long,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateBack: () -> Unit,
) {
    viewModel.updateStatus(countryId)
    val favoriteStatus by viewModel.favoriteStatus.collectAsState(initial = false)

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getCountryById(countryId)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    image = data.image,
                    title = data.title,
                    capital = data.capital,
                    membership = data.membership,
                    contribution = data.contribution,
                    onBackClick = navigateBack,
                    favoriteStatus = favoriteStatus,
                    updateFavoriteStatus = { viewModel.changeFavorite(data) }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    capital: String,
    membership: String,
    contribution: String,
    onBackClick: () -> Unit,
    favoriteStatus: Boolean,
    updateFavoriteStatus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = { onBackClick() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.detail_title, title)
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                )

                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Text(
                    text = stringResource(R.string.capital, capital),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = stringResource(R.string.membership, membership),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = contribution,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }

        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            onClick = updateFavoriteStatus
        ) {
            Icon(
                imageVector = if (favoriteStatus) {
                    Icons.Default.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },

                contentDescription = if (favoriteStatus) {
                    stringResource(id = R.string.favorite_remove, title)
                } else {
                    stringResource(id = R.string.favorite_add, title)
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailContentPreview() {
    JetASEANTheme {
        DetailContent(
            image = R.drawable.flag_of_indonesia_flat_round,
            title = "Indonesia",
            capital = "Jakarta",
            membership = "08 August 1967",
            contribution = "As the largest country in ASEAN, Indonesia has an important role in leading ASEAN and promoting economic and political integration in the Southeast Asian region. Indonesia also has great influence in terms of maritime security and environmental cooperation in ASEAN.",
            favoriteStatus = false,
            updateFavoriteStatus = {},
            onBackClick = { }
        )
    }
}