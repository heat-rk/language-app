package ru.heatrk.languageapp.common.utils

import androidx.annotation.Px
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size

@Immutable
sealed interface PainterResource {
    data class ByPainter(val painter: Painter) : PainterResource
    data class ByData(val data: Any) : PainterResource
}

fun painterRes(data: Any) = PainterResource.ByData(data)

fun painterRes(painter: Painter) = PainterResource.ByPainter(painter)

@Composable
fun PainterResource.extract(size: Size = Size.ORIGINAL): Painter {
    val isInInspectionMode = LocalInspectionMode.current
    
    return if (isInInspectionMode) {
        inspectionModeExtract(size = size)
    } else {
        defaultExtract(size = size)
    }
}

@Composable
private fun PainterResource.inspectionModeExtract(size: Size) = 
    when {
        this is PainterResource.ByPainter -> painter
        this is PainterResource.ByData && data is Int -> painterResource(id = data)
        else -> defaultExtract(size = size)
    }

@Composable
private fun PainterResource.defaultExtract(size: Size) = 
    when (this) {
        is PainterResource.ByPainter -> painter
        is PainterResource.ByData -> {
            val context = LocalContext.current
            val imageLoader = context.imageLoader
    
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(data)
                    .apply { size(size) }
                    .build(),
                imageLoader = imageLoader,
            )
        }
    }

fun Size(@Px size: Int) = Size(size, size)
