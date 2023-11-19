package com.tegar.moviefinder.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tegar.moviefinder.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit,
    searchText: String,
    onSearchFinished: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = searchText,
        onValueChange = { onSearchTextChanged(it) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = Color.Transparent,
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent

        ),
        placeholder = {
            Text(stringResource(R.string.search_movie))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .testTag("SearchField"),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            onSearchFinished()
        })
    )
}


