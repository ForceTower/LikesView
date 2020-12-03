package dev.forcetower.likesview.core.source.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    val M2TO3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE InstagramMediaPage ADD COLUMN previousPageId TEXT DEFAULT NULL")
        }
    }
}
