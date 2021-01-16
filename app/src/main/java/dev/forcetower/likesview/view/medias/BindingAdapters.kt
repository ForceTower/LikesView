package dev.forcetower.likesview.view.medias

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import dev.forcetower.likesview.core.model.database.InstagramMedia

@BindingAdapter("calculateMediaParams")
fun calculateMediaParams(view: View, media: InstagramMedia?) {
    val desiredWidth = media?.dimensionWidth ?: 1
    val desiredHeight = media?.dimensionHeight ?: 1

    val viewWidth = view.width
    val viewHeight = viewWidth / (desiredWidth / desiredHeight.toFloat())

    view.layoutParams = ConstraintLayout.LayoutParams(viewWidth, viewHeight.toInt())
}