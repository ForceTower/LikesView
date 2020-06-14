package dev.forcetower.likesview.view.home

import android.view.View
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import dev.forcetower.toolkit.extensions.getPixelsFromDp

@BindingAdapter("selectedProfileMargin")
fun selectedProfileMargin(view: View, selected: Boolean?) {
    val active = selected ?: false
    val context = view.context
    val margin = if (active) 8 else 0
    val pixel = context.getPixelsFromDp(margin).toInt()
    view.updatePadding(0, 0, 0, pixel)
}