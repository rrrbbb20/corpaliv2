package cl.corpaliv.albalab.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.corpaliv.albalab.data.model.Solicitud
import cl.corpaliv.albalab.data.model.Usuario

@Database(entities = [Usuario::class, Solicitud::class], version = 1, exportSchema = false)
abstract class CorpalivDatabase : RoomDatabase() {
    abstract fun dao(): CorpalivDao

    companion object {
        @Volatile private var instance: CorpalivDatabase? = null
        fun get(context: Context): CorpalivDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext, CorpalivDatabase::class.java, "corpaliv_local.db"
            ).build().also { instance = it }
        }
    }
}
