package eu.epfc.projetpocketmovie
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import eu.epfc.projetpocketmovie.databinding.FragmentItemBinding
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import eu.epfc.projetpocketmovie.data.Movie
import eu.epfc.projetpocketmovie.utils.Constants

class MyItemRecyclerViewAdapter(var values: List<Movie>, private val myInterface:MyInterface):RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>()
{
    lateinit var binding:FragmentItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder
    {
        binding=FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = values[position]
        holder.itemView.setOnClickListener { myInterface.onclick(item) }
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item:Movie)
        {
            binding.movie=item
            binding.executePendingBindings()
        }
    }
}

@BindingAdapter("movieName")
fun TextView.setMovieTitle(item:Movie)
{
    text=item.title
}

@BindingAdapter("movieRate")
fun TextView.setMovieRate(item:Movie)
{
    text=item.vote_average.toString()
}

@BindingAdapter("moviePoster")
fun ImageView.setMoviePoster(item:Movie)
{
    Picasso.get().load(Constants.POSTER_PATH+item.poster_path).into(this)
}