package eu.epfc.projetpocketmovie.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
class Movie( @PrimaryKey(autoGenerate=false) val movieId : Long,
            val title : String,
            val release_date: String,
            val vote_average:Double,
            val overview : String,
            val poster_path : String,
            val backdrop_path:String,
           )
