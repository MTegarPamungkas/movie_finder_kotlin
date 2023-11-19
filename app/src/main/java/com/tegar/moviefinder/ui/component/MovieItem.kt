package com.tegar.moviefinder.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.tegar.moviefinder.R
import com.tegar.moviefinder.utils.ImageTmdbQuality

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(
    id: Int,
    posterPath: String?,
    releaseDate: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    val year = if (releaseDate.isNotEmpty()) {
        releaseDate.split("-")[0]
    } else {
        stringResource(id = R.string.minus)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column {
            Box {
                GlideImage(
                    model = posterPath?.let { ImageTmdbQuality.quality200(it) },
                    contentDescription = id.toString(),
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(0.75F)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    loading = placeholder(R.drawable.placeholder),
                    failure = placeholder(R.drawable.error)
                )
                Text(
                    text = year,
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = modifier.height(12.dp))
            Text(
                modifier = modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center
            )
        }

    }
}
