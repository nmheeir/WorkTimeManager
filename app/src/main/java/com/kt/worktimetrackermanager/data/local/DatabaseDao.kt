package com.kt.worktimetrackermanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kt.worktimetrackermanager.data.local.entities.NotificationEntity
import com.kt.worktimetrackermanager.data.local.entities.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    //    ================ Insert ============================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileEntity)


    //    ================ Update ============================
    @Update
    suspend fun update(notification: NotificationEntity)

    @Update
    suspend fun update(profile: ProfileEntity)

    @Update
    suspend fun update(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = :isRead WHERE id IN (:notificationIds)")
    suspend fun updateByIds(notificationIds: List<Int>, isRead: Boolean)


    //    ================ Delete ============================
    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Delete
    suspend fun delete(profile: ProfileEntity)

    @Delete
    suspend fun delete(notifications: List<NotificationEntity>)

    @Query("DELETE FROM notifications WHERE id IN (:notificationIds)")
    suspend fun deleteByIds(notificationIds: List<Int>)


    // ==================== Query ============================
    @Query("SELECT * FROM notifications WHERE receivedUsername = :receivedUsername ORDER BY receivedAt DESC")
    fun notifications(receivedUsername: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM profile LIMIT 1")
    fun profile(): Flow<ProfileEntity>

    @Query("DELETE FROM profile")
    fun clearProfile()
}