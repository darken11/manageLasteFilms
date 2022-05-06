package eu.epfc.projetpocketmovie
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.epfc.projetpocketmovie.data.Movie
import eu.epfc.projetpocketmovie.data.MovieRepository

/**
 * A fragment representing a list of Items.
 */
class ItemFragment:Fragment()
{
    //private var columnCount = 1
    lateinit var rvAdapter: MyItemRecyclerViewAdapter
    var isPocket:Boolean=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View?
    {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager=LinearLayoutManager(context)
                if(isPocket)
                    adapter = MyItemRecyclerViewAdapter(MovieRepository.instance.getMovies(this.context), myInterface = (requireActivity() as MyInterface))//db
                else{
                    adapter= MyItemRecyclerViewAdapter(MovieRepository.instance.movies,myInterface = (requireActivity() as MyInterface)) //web
                }
            }
            rvAdapter=view.adapter as MyItemRecyclerViewAdapter
        }
        return view
    }

//    companion object
//    {
//        // TODO: Customize parameter argument names
//        const val ARG_COLUMN_COUNT = "column-count"
//        // TODO: Customize parameter initialization
//        @JvmStatic
//        fun newInstance(columnCount: Int) = ItemFragment(false).apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_COLUMN_COUNT, columnCount)
//                }
//            }
//    }
}