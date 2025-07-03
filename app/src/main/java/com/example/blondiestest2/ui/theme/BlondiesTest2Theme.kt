package com.example.blondiestest2.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blondiestest2.R

// 0) CompositionLocal holding our dynamic scale factor (1f…2f)
val LocalUiScale = compositionLocalOf { 1f }

// 1) COLOR PALETTE
private val DeepCharcoal   = Color(0xFF333333)
private val CreamLight     = Color(0xFFFCF8F1)
private val CreamMedium    = Color(0xFFF0E6D8)
private val AccentViolet   = Color(0xFF6C63FF)
private val AccentTeal     = Color(0xFF00CFC1)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val OnBackground   = Color(0xFF333333)
private val SurfaceLight   = CreamMedium

private val LightColors = lightColorScheme(
    primary      = DeepCharcoal,
    onPrimary    = OnPrimaryLight,
    secondary    = AccentViolet,
    tertiary     = AccentTeal,
    background   = CreamLight,
    onBackground = OnBackground,
    surface      = SurfaceLight,
    onSurface    = OnBackground,
)

// 2) FONT FAMILY
private val FunnelDisplay = FontFamily(
    Font(R.font.funnel_display_regular, weight = FontWeight.Normal),
    Font(R.font.funnel_display_bold,    weight = FontWeight.Bold)
)

// 3) SHAPES
val BlondiesShapes = Shapes(
    small  = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large  = RoundedCornerShape(0.dp)
)

// 4) THEME WRAPPER (reads LocalUiScale, scales typography)
@Composable
fun BlondiesTest2Theme(
    content: @Composable () -> Unit
) {
    val scale = LocalUiScale.current

    // scale each TextStyle’s fontSize & lineHeight
    val scaledTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = FunnelDisplay,
            fontWeight  = FontWeight.Bold,
            fontSize    = 36.sp * scale,
            lineHeight  = 44.sp * scale
        ),
        titleMedium = TextStyle(
            fontFamily = FunnelDisplay,
            fontWeight  = FontWeight.Bold,
            fontSize    = 18.sp * scale,
            lineHeight  = 24.sp * scale
        ),
        bodyMedium = TextStyle(
            fontFamily = FunnelDisplay,
            fontWeight  = FontWeight.Normal,
            fontSize    = 14.sp * scale,
            lineHeight  = 20.sp * scale
        )
        // …add other styles if needed, all multiplied by `scale`…
    )

    MaterialTheme(
        colorScheme = LightColors,
        typography  = scaledTypography,
        shapes      = BlondiesShapes,
        content     = content
    )
}
