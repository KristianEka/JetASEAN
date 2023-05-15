package com.ekachandra.jetasean.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ekachandra.jetasean.R
import com.ekachandra.jetasean.model.Country
import com.ekachandra.jetasean.ui.ViewModelFactory
import com.ekachandra.jetasean.ui.common.UiState
import com.ekachandra.jetasean.ui.components.CountryItem
import com.ekachandra.jetasean.ui.components.ScrollToTopButton
import com.ekachandra.jetasean.ui.components.SearchBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToDetail: (Long) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllCountries()
            }

            is UiState.Success -> {
                HomeContent(
                    countryList = uiState.data,
                    navigateToDetail = navigateToDetail,
                    viewModel = viewModel,
                    modifier = modifier
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    countryList: List<Country>,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val query by viewModel.query.collectAsState()
    val scope = rememberCoroutineScope()

    val showButton: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    Box(modifier = modifier) {
        Column {
            SearchBar(
                query = query,
                onQueryChange = { query ->
                    viewModel.search(query)

                    if (query.isEmpty()) {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
            )

            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .testTag("HomeList")
            ) {
                if (countryList.isNotEmpty()) {
                    items(countryList, key = { it.id }) { item ->
                        CountryItem(
                            title = item.title,
                            image = item.image,
                            membership = item.membership,
                            modifier = Modifier
                                .clickable {
                                    navigateToDetail(item.id)
                                }
                                .animateItemPlacement(tween(durationMillis = 500))
                        )
                    }
                } else {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.not_found),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(vertical = 40.dp)
                            )

                            Text(
                                text = stringResource(id = R.string.search_not_found)
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }

    }
}