package eu.epfc.projetpocketmovie.data
import android.content.Context
import android.util.Log
import eu.epfc.projetpocketmovie.HttpRequestTask
import eu.epfc.projetpocketmovie.utils.Constants
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class MovieRepository
{
    var applicationContext : Context ? = null
//    private val url = Constants.URI_API
//    private val urlDiscover=Constants.URL_DISCOVER
//    private val urlVideOp1="movie/"//+movieId
//    private val urlVideOp2=Constants.URL_VIDEO2
    private val urlapi = "https://api.themoviedb.org/3/"
        private val urldiscover="discover/movie?api_key="
       private val urlvideop1="movie/"//+idmovie
        private val urlvideop2="/videos?api_key="
    private val keyAPI=Constants.KEY_API

    var movies: MutableList<Movie> = mutableListOf()
        private set // public but read-only
        var movie:Movie?=null
    var videoID:String=""
    // Activity/Fragments interested by update in the model
    private var dataUIListener: WeakReference<DataUIListener>? = null
    private var dataUIListenerVideo: WeakReference<DataUIListenerVideo>? = null

    interface DataUIListener{
        fun updateUI()
    }
    interface DataUIListenerVideo{
               fun updateUI()
    }
    fun onReceiveHttpResponse(response : String?, completed : Boolean, video:Boolean)
    {

        if( completed && response !=null)
        {

            if(!video)
            {
             val movieList=MovieRepository.instance.parseMoviesResponse(response)
                for(i in 0 until movieList.size-1)
                       if(!movies.contains(movieList[i]))
                                  movies.add(movieList[i])
                            // update the UI
                        val myDataUIListener = dataUIListener
                        if(myDataUIListener != null)
                            myDataUIListener.get()?.updateUI()
            }else{

                videoID=MovieRepository.instance.parseMovieVideoResponse(response)
                   // update the UI
                  val myDataUIListener = dataUIListenerVideo
                  if(myDataUIListener != null)
                    myDataUIListener.get()?.updateUI()
            }
        }
    }
        fun getVideoID(context: Context,id:Long)
       {
           val url=urlapi+urlvideop1+id+urlvideop2+keyAPI
           val requestTask = HttpRequestTask(url, context,true)
           val requestThread = Thread(requestTask)
           requestThread.start()
    }

    fun observe(dataUIListener: DataUIListener){
        this.dataUIListener = WeakReference(dataUIListener)
    }

        fun observeVideo(dataUIListener: DataUIListenerVideo){
           this.dataUIListenerVideo = WeakReference(dataUIListener)
        }
    /**Launch asynchronously a request to retrieve movies from web service. When finished,
     * updateUI() will be called on the DataUIListener instance on the UI thread.
     */
    fun fetchMovies(context: Context)
    {
        if (movies.isEmpty())
        {// get new movies from the server
            val url=urlapi+urldiscover+keyAPI
            val requestTask = HttpRequestTask(url, context,false)
            val requestThread = Thread(requestTask)
            requestThread.start()
        }else
        {
            val myDataUIListener = dataUIListener
            if (myDataUIListener != null){
                myDataUIListener.get()?.updateUI()
            }
        }
    }
    /**Parse a json string received from web service
     * @param jsonString : a json string received from the server
     * @return : a list of Movie objects parsed by the method
     */
    private fun parseMoviesResponse(jsonString: String):ArrayList<Movie>
    {
        val newMovies = ArrayList<Movie>()
        try
        {
            val jsonObject = JSONObject(jsonString)
            val jsonMovies = jsonObject.getJSONArray("results")
            for(i in 0 until jsonMovies.length())
            {
                val jsonMovie = jsonMovies.getJSONObject(i)
                val movieId=jsonMovie.getLong("id")
                val title = jsonMovie.getString("title")
                val releaseDate = jsonMovie.getString("release_date")
                val voteAverage = jsonMovie.getDouble("vote_average")
                val overview = jsonMovie.getString("overview")
                val thumbnailUrl = jsonMovie.getString("poster_path")
                val backdropPath=jsonMovie.getString("backdrop_path")
                val newMovie = Movie(movieId,title, releaseDate,voteAverage, overview ,thumbnailUrl,backdropPath)
                newMovies.add(newMovie)
            }
        }catch(e: JSONException)
        {
            Log.e("MovieRepository", Constants.PARSE_EXCEPTION)
            e.printStackTrace()
        }
        return newMovies
    }
    private fun parseMovieVideoResponse(jsonString: String):String
       {
           var key:String=""
           try
           {
                       val jsonObject = JSONObject(jsonString)
                       val jsonMovies = jsonObject.getJSONArray("results")
                       for(i in 0 until jsonMovies.length())
                           {
                                    val jsonMovie = jsonMovies.getJSONObject(i)
                                    if(jsonMovie.getString("type")=="Trailer"||key.isEmpty())
                                           key=jsonMovie.getString("key")
                                }
                    }catch(e: JSONException)
                {
                        Log.e("MovieRepository", Constants.PARSE_EXCEPTION)
                        e.printStackTrace()
                   }
                return key
           }
    companion object
    {/**return the unique static instance of the singleton
         * @return an MovieManager instance
         */
        val instance = MovieRepository()
    }
    fun movieInDb(context: Context,movie: Movie):Boolean
    {
        val movieDao:MovieDao=MovieDatabase.getInstance(context).movieDao()
        if( movieDao.getMovie(movie.movieId)!=null)
            return true
        return false
    }
    fun getMovies(context: Context):List<Movie>
    {
        val movieDao:MovieDao=MovieDatabase.getInstance(context).movieDao()
        return movieDao.getAllMovies()
    }

    fun addMovie(context: Context,movie: Movie)
    {
        listOf(movie)
        val movieDao:MovieDao=MovieDatabase.getInstance(context).movieDao()
        return movieDao.movieInsert(movie)
    }

    fun removeMovie(context: Context,movie: Movie)
    {
        val movieDao:MovieDao=MovieDatabase.getInstance(context).movieDao()
        return movieDao.deleteMovie(movie)
    }
}