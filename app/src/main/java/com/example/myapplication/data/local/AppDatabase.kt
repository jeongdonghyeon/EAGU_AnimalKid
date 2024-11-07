package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.model.DTO.GroupDTO
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.CalendarEvent
import com.example.myapplication.data.model.entity.GroupEntity
import com.example.myapplication.data.model.entity.UserEntity

@Database(entities = [UserEntity::class, GroupEntity::class, CalendarEvent::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): UserGroupDao
    abstract fun calendarDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()  // 개발 단계: 파괴적 마이그레이션 사용
                    // .addMigrations(MIGRATION_1_2)   // 운영 단계: 마이그레이션 사용
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 운영 환경에서 사용할 마이그레이션 예제
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user_entity ADD COLUMN new_column_name TEXT DEFAULT ''")
                // 기타 스키마 변경 SQL 구문 추가
            }
        }
    }
}
