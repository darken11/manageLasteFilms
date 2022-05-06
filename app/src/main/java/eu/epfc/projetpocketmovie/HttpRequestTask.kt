package eu.epfc.projetpocketmovie
import android.content.Context
import android.os.Handler
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import eu.epfc.projetpocketmovie.data.MovieRepository

class HttpRequestTask(private val url:String, private val applicationContext:Context,private val video:Boolean):Runnable
{/**This method will run on a background thread.*/
    override fun run()
    {// Create http client
        val okHttpClient = OkHttpClient()
        // build a request
        val request = Request.Builder().url(url).build()
        // send the request
        try{
            val response = okHttpClient.newCall(request).execute()
            // extract data from the response
            val responseString : String? = response.body?.string()

            if (responseString != null)
            {// switch to main thread
                val mainHandler = Handler(applicationContext.mainLooper)
                mainHandler.post {// run on main thread
                    MovieRepository.instance.onReceiveHttpResponse(responseString, true,video)
                }
            }
        }catch(exception : IOException)
        {// switch to main thread
            val mainHandler = Handler(applicationContext.mainLooper)
            mainHandler.post {// run on main thread
                MovieRepository.instance.onReceiveHttpResponse(null, false,video)
            }
        }
    }
}