package eu.epfc.projetpocketmovie
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.Fragment
import eu.epfc.projetpocketmovie.data.Movie
import eu.epfc.projetpocketmovie.data.MovieRepository

class MainActivity : AppCompatActivity(),MovieRepository.DataUIListener,MyInterface
{
    private lateinit var tablayoutItemsAdapter: TabLayoutItemsAdapter
    private lateinit var viewPager: ViewPager2
   // lateinit var webItemFragment: ItemFragment
    var webItemFragment:ItemFragment?=null
    var dbItemFragment: ItemFragment?=null
    override fun onclick(movie: Movie)
    {
        MovieRepository.instance.movie=movie
        val detailsActivity=DetailsActivity()
        val detailsIntent = Intent(this, detailsActivity::class.java)
        startActivity(detailsIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        MovieRepository.instance.observe(this)
       // MovieRepository.instance.onReceiveHttpResponse(null,false)
        setContentView(R.layout.activity_main)
        tablayoutItemsAdapter = TabLayoutItemsAdapter(this)
        val tabLayout:TabLayout=this.findViewById(R.id.tab_layout)
        viewPager=this.findViewById(R.id.pager)
        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter=pagerAdapter
        viewPager.adapter = tablayoutItemsAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if(position==0)
                tab.text="RECENT MOVIE"
            else
                tab.text="POCKET"
        }.attach()
    }

    override fun onStart()
    {
        super.onStart()
        MovieRepository.instance.fetchMovies(this)
    }

    override fun updateUI(){
        val movieList = MovieRepository.instance.movies
        if(movieList.isNotEmpty() && webItemFragment !=null){
            webItemFragment!!.rvAdapter.values=movieList
            webItemFragment!!.rvAdapter.notifyDataSetChanged()
        }
        //MyItemRecylerViewAdapter.articles = movieList
    }

    private inner class TabLayoutItemsAdapter(fragment: MainActivity) : FragmentStateAdapter(fragment)
    {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment
        {// Return a NEW fragment instance in createFragment(int)
            val fragment = ItemFragment()
            if(position==1) {
                fragment.isPocket = true
                dbItemFragment=fragment
            }
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putInt("ARG_OBJECT", position + 1)
            }
            if(position==0)
                webItemFragment=fragment
            return fragment
        }
    }

    //A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in sequence.
    private inner class ScreenSlidePagerAdapter(fragment:MainActivity):FragmentStateAdapter(fragment)
    {
        override fun getItemCount(): Int = tablayoutItemsAdapter.getItemCount()
        override fun createFragment(position: Int): Fragment = tablayoutItemsAdapter.createFragment(position)
    }
}