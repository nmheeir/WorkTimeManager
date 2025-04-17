package com.kt.worktimetrackermanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kt.worktimetrackermanager.data.local.entities.NotificationEntity
import com.kt.worktimetrackermanager.data.local.entities.UserSessionEntity

class AppDatabase(
    private val delegate: InternalDatabase,
) : DatabaseDao by delegate.dao {

    fun query(block: AppDatabase.() -> Unit) = with(delegate) {
        queryExecutor.execute {
            block(this@AppDatabase)
        }
    }

    fun transaction(block: AppDatabase.() -> Unit) = with(delegate) {
        transactionExecutor.execute {
            runInTransaction {
                block(this@AppDatabase)
            }
        }
    }

    fun close() = delegate.close()
}

@Database(
    entities = [
        UserSessionEntity::class, NotificationEntity::class
    ],
    exportSchema = true,
    version = 2
)
@TypeConverters(Converters::class)
abstract class InternalDatabase : RoomDatabase() {
    abstract val dao: DatabaseDao

    companion object {
        private const val DB_NAME = "worktimetrackermanager.db"

        fun newInstance(context: Context) =
            AppDatabase(
                delegate = Room.databaseBuilder(context, InternalDatabase::class.java, DB_NAME)
                    .build()
            )

    }
}