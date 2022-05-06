package eu.epfc.projetpocketmovie.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class  MovieDatabase() : RoomDatabase()
{
    companion object
    {
        private const val DATABASE_NAME = "PocketMovieDB"
        private var sInstance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase
        {
            if (sInstance == null)
            {
                val dbBuilder = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, DATABASE_NAME)
                dbBuilder.allowMainThreadQueries()
                sInstance = dbBuilder.build()
            }
            return sInstance!!
        }
    }

    abstract fun movieDao(): MovieDao
}