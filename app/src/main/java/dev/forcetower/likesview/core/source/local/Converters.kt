package dev.forcetower.likesview.core.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.forcetower.likesview.core.model.database.InstagramMedia

object Converters {
    @JvmStatic
    @TypeConverter
    fun galleryContentToString(value: InstagramMedia.GalleryContent): String {
        return Gson().toJson(value)
    }

    @JvmStatic
    @TypeConverter
    fun stringToGalleryContent(value: String): InstagramMedia.GalleryContent {
        return Gson().fromJson(value, InstagramMedia.GalleryContent::class.java)
    }
}
