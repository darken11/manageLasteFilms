package eu.epfc.projetpocketmovie
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import eu.epfc.projetpocketmovie.data.Movie
import eu.epfc.projetpocketmovie.data.MovieRepository
import eu.epfc.projetpocketmovie.utils.Constants

class DetailsActivity : AppCompatActivity(),MovieRepository.DataUIListenerVideo
{
    val movie: Movie= MovieRepository.instance.movie!!
    var youtubeId:String=MovieRepository.instance.videoID
    var youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeId"))
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)

        val backDrop:ImageView=this.findViewById(R.id.backdropImageView)
        Picasso.get().load(Constants.BACKDROP_PATH+movie.backdrop_path).into(backDrop)
        val title:TextView=this.findViewById(R.id.titleTextView)
        title.text=movie.title
        val info:TextView=this.findViewById(R.id.infoTextView)
        info.text=movie.release_date+" - "+movie.vote_average
        val overview:TextView=this.findViewById(R.id.synopsisTextView)
        overview.text=movie.overview

        val videoButton:Button=this.findViewById(R.id.videoButton)
        videoButton.setOnClickListener { startActivity(youtubeIntent) }
        val pocketCheckBox:CheckBox=this.findViewById(R.id.checkBoxPocket)
        pocketCheckBox.isChecked=MovieRepository.instance.movieInDb(this,movie)
        pocketCheckBox.setOnCheckedChangeListener { _, b -> if(b)
                MovieRepository.instance.addMovie(this,movie)
            else
                MovieRepository.instance.removeMovie(this,movie)
        }
    }

    override fun updateUI() {
        var youtubeId:String=MovieRepository.instance.videoID
        var youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeId"))
    }
}