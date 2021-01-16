package dev.forcetower.likesview.view.medias

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import dev.forcetower.likesview.core.model.database.InstagramMedia
import timber.log.Timber

@BindingAdapter("calculateMediaParams")
fun calculateMediaParams(view: View, media: InstagramMedia?) {
    view.doOnLayout {
        val desiredWidth = media?.dimensionWidth ?: 1
        val desiredHeight = media?.dimensionHeight ?: 1

        val viewWidth = view.width
        val viewHeight = viewWidth.toFloat() / (desiredWidth / desiredHeight.toFloat())

        view.layoutParams = ConstraintLayout.LayoutParams(viewWidth, viewHeight.toInt())
    }
}