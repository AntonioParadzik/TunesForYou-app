package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomepageFragment : Fragment(),PlaylistRecyclerAdapter.OnItemClickListener {
    private val db=Firebase.firestore
    private lateinit var moodPlaylistRecyclerAdapter:PlaylistRecyclerAdapter
    private lateinit var genrePlaylistRecyclerAdapter:PlaylistRecyclerAdapter
    var moodPlaylistsList = ArrayList<PlaylistCover>()
    var genrePlaylistsList = ArrayList<PlaylistCover>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth=FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_homepage, container, false)
        val logoutBtn = view.findViewById<Button>(R.id.logoutButton)
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val fragmentTransaction : FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, LoginFragment())?.commit()
        }

        val moodRecyclerView=view.findViewById<RecyclerView>(R.id.moodPlaylistsView)
        val genreRecyclerView=view.findViewById<RecyclerView>(R.id.genrePlaylistsView)

        db.collection("moodPlaylistCovers")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val playlist = data.toObject(PlaylistCover::class.java)
                    if (playlist != null) {
                        playlist.id = data.id
                        moodPlaylistsList.add(playlist)
                    }
                }
                moodPlaylistRecyclerAdapter = PlaylistRecyclerAdapter(moodPlaylistsList,this@HomepageFragment)

                moodRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = moodPlaylistRecyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w("HomepageFragment", "Error getting documents.",
                    exception)
            }

        db.collection("genrePlaylistCovers")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val playlist = data.toObject(PlaylistCover::class.java)
                    if (playlist != null) {
                        playlist.id = data.id
                        genrePlaylistsList.add(playlist)
                    }
                }
                genrePlaylistRecyclerAdapter = PlaylistRecyclerAdapter(genrePlaylistsList,this@HomepageFragment)

                genreRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = genrePlaylistRecyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w("HomepageFragment", "Error getting documents.",
                    exception)
            }
        return view
    }

    override fun onItemClick(position: Int, list: ArrayList<PlaylistCover>) {
        val bundle=Bundle()
        val fragment=DefaultPlaylistFragment()
        if(list==moodPlaylistsList) {
            bundle.putString("playlistList","moodPlaylistCovers")
            bundle.putString("imageUrl",moodPlaylistsList[position].imageUrl)
            bundle.putString("playlistName", moodPlaylistsList[position].name)
            bundle.putString("playlistID",moodPlaylistsList[position].id)
            fragment.arguments=bundle
        }
        else if(list==genrePlaylistsList){
            bundle.putString("playlistList","genrePlaylistCovers")
            bundle.putString("imageUrl",genrePlaylistsList[position].imageUrl)
            bundle.putString("playlistName", genrePlaylistsList[position].name)
            bundle.putString("playlistID",genrePlaylistsList[position].id)
            fragment.arguments=bundle
        }
        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moodPlaylistsList.removeAll(moodPlaylistsList.toSet())
        genrePlaylistsList.removeAll(genrePlaylistsList.toSet())
    }
}