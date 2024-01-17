package hr.ferit.antonioparadzik.tunesforyou


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DefaultPlaylistFragment : Fragment(),DefaultSongRecyclerAdapter.OnItemClickListener {
    private val db= Firebase.firestore
    private lateinit var defaultSongRecyclerAdapter: DefaultSongRecyclerAdapter
    private val songsList=ArrayList<DefaultSongDisplay>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_default_playlist, container, false)
        val defaultSongsRecyclerView=view.findViewById<RecyclerView>(R.id.defaultSongsList)

        val name=view.findViewById<TextView>(R.id.playlistNameDefaultText)
        name.text=arguments?.getString("playlistName").toString()

        val playlistId=arguments?.getString("playlistID").toString()

        val image=view.findViewById<ImageView>(R.id.playlistDefaultImage)
        val playlistDefaultImageUrl = arguments?.getString("imageUrl").toString()
        Glide.with(view.context).load(playlistDefaultImageUrl).into(image)

        val playlistType=arguments?.getString("playlistList").toString()

        db.collection(playlistType)
            .document(playlistId)
            .collection(name.text.toString())
            .get().addOnSuccessListener {result->
                for (data in result.documents) {
                    val song = data.toObject(DefaultSongDisplay::class.java)
                    if (song != null) {
                        song.id = data.id
                        songsList.add(song)
                    }
                }
                defaultSongRecyclerAdapter = DefaultSongRecyclerAdapter(songsList,this@DefaultPlaylistFragment)

                defaultSongsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                    adapter = defaultSongRecyclerAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.w("HomepageFragment", "Error getting songs.",
                    exception)
            }

        val backBtn=view.findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

    override fun onItemClick(position: Int, list: ArrayList<DefaultSongDisplay>) {
        val bundle=Bundle()
        val fragment=VideoPlayerFragment()
        bundle.putString("videoId", songsList[position].videoId)
        fragment.arguments=bundle

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        songsList.removeAll(songsList.toSet())
    }

}

