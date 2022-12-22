package az.zero.composeplayground2

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.ceil

private const val TAG = "MainActivityTest"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContent {
//
//            val configuration = LocalConfiguration.current
//            val screenWidth = remember {
//                configuration.screenWidthDp.toFloat()
//            }
//            val screenHeight = remember {
//                (configuration.screenHeightDp).toFloat()
//            }
//
//            val infiniteTransition = rememberInfiniteTransition()
//
//            val widthPosition by infiniteTransition.animateFloat(
//                initialValue = 0f,
//                targetValue = screenWidth,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(5000, easing = LinearEasing)
//                )
//            )
//
//            val heightPosition by infiniteTransition.animateFloat(
//                initialValue = 0f,
//                targetValue = screenHeight,
//                animationSpec = infiniteRepeatable(
//                    animation = tween(5000, easing = LinearEasing)
//                )
//            )
//
//            val z = remember {
//                Animatable(
//                    initialValue = 0f,
//                    visibilityThreshold = 1f
//                )
//            }
//
//            LaunchedEffect(true) {
//                launch {
//                    z.animateTo(screenHeight,tween(5000, easing = LinearEasing))
//                }
//            }
//
//            Row(
//                modifier = Modifier.fillMaxSize(),
//            ) {
//
//                MyCircle(
////                    positionX = widthPosition.dp,
//                    positionY = z.value.dp,
//                )
//                Spacer(modifier = Modifier.size(16.dp))
//                MyCircle(
////                    positionX = widthPosition.dp,
//                    positionY = heightPosition.dp,
//                )
//                Spacer(modifier = Modifier.size(16.dp))
//                MyCircle(
////                    positionX = widthPosition.dp,
//                    positionY = (heightPosition - 100).dp,
//                )
//            }
//        }

        setContent {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {


                val configuration = LocalConfiguration.current
                val screenWidthInFloat = remember(configuration.screenWidthDp) {
                    configuration.screenWidthDp.toFloat()
                }

                val screenHeightInFloat = remember(configuration.screenHeightDp) {
                    configuration.screenHeightDp.toFloat()
                }

//                val infiniteTransition = rememberInfiniteTransition()
//
//                val x by infiniteTransition.animateFloat(
//                    initialValue = 0f,
//                    targetValue = screenWidthInFloat,
//                    animationSpec = infiniteRepeatable(
//                        animation = tween(5000, easing = LinearEasing)
//                    )
//                )


                AnimatableBall(
                    initialX_Position = 0f,
                    finalX_Position = 0f,
                    finalY_Position = screenHeightInFloat
                )


            }
        }
    }
    @Composable
    fun redraw(interval :Long = 500): Int {
        var value = 0

        DisposableEffect(Unit) {
            val handler = Handler(Looper.getMainLooper())

            val runnable = {
                value++
                Unit
            }
            handler.postDelayed(runnable, interval)

            onDispose {
                handler.removeCallbacks(runnable)
            }
        }

        return value
    }

    @Composable
    fun AnimatableBall(
        initialX_Position: Float = 0f,
        initialY_Position: Float = 0f,
        finalX_Position: Float,
        finalY_Position: Float,
    ) {

        val coroutineScope = rememberCoroutineScope()
        val circleSize = 25.dp
        val calcXInitPosition =
            if (initialX_Position > circleSize.value) initialX_Position - circleSize.value
            else initialX_Position

        val calcXFinalPosition =
            if (finalX_Position > circleSize.value) finalX_Position - circleSize.value
            else finalX_Position


        val animatableX = remember { Animatable(initialValue = calcXInitPosition) }

        val animatableY = remember { Animatable(initialValue = initialY_Position) }

        DisposableEffect(true) {
            coroutineScope.launch {
                animatableX.animateTo(calcXFinalPosition, tween(5000, easing = LinearEasing))
            }

            onDispose { coroutineScope.launch { animatableX.stop() } }
        }

        DisposableEffect(true) {
            coroutineScope.launch {
                animatableY.animateTo(finalY_Position, tween(5000, easing = LinearEasing))
            }

            onDispose { coroutineScope.launch { animatableY.stop() } }
        }

        MyCircle(
            positionX = animatableX.value.dp,
            positionY = animatableY.value.dp,
            size = circleSize
        )
    }

    @Composable
    fun MyCircle(
        positionX: Dp = 0.dp,
        positionY: Dp = 0.dp,
        size: Dp,
    ) {
        Canvas(
            modifier = Modifier
                .size(size)
                .offset(
                    x = positionX,
                    y = positionY
                ),
            onDraw = {
                drawCircle(
                    color = Color.Red
                )
            })
    }
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}