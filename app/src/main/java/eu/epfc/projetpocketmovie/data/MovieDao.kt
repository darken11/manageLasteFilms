package eu.epfc.projetpocketmovie.data
import androidx.room.*

@Dao
interface MovieDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun movieInsert(movies: Movie)
    @Query("SELECT * FROM movie")
    fun getAllMovies(): List<Movie>
    @Query("SELECT * FROM movie WHERE movieId LIKE :movieid")
    fun getMovie(movieid: Long): Movie?
    @Delete
    fun deleteMovie(movie: Movie)
}