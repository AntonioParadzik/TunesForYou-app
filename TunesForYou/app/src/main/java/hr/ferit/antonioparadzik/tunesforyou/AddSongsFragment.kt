package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class AddSongsFragment : Fragment(),AddSongRecyclerAdapter.ContentListener{
    private val db= Firebase.firestore
    private lateinit var addSongRecyclerAdapter: AddSongRecyclerAdapter
    private val songsList=ArrayList<DefaultSongDisplay>()
    private val currentPlaylistList=ArrayList<DefaultSongDisplay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_songs, container, false)

        val addSongRecyclerView=view.findViewById<RecyclerView>(R.id.addSongsView)

        val userEmail=arguments?.getString("userEmail")!!
        val myPlaylistId=arguments?.getString("myPlaylistId")!!
        val myPlaylistName=arguments?.getString("myPlaylistName").toString()

        db.collection("users")
            .document(userEmail)
            .collection("myPlaylists")
            .document(myPlaylistId)
            .collection(myPlaylistName)
            .get().addOnSuccessListener{result->
                for(data in result.documents){
                    val song=data.toObject(DefaultSongDisplay::class.java)
                    if(song!=null){
                        song.id=data.id
                        currentPlaylistList.add(song)
                    }
                }
            }

        db.collection("moodPlaylistCovers")
            .get().addOnSuccessListener {result1->
                for (data1 in result1.documents) {
                    val playlist = data1.toObject(PlaylistCover::class.java)
                    if (playlist != null) {
                        playlist.id=data1.id
                        db.collection("moodPlaylistCovers")
                            .document(playlist.id)
                            .collection(playlist.name).get().addOnSuccessListener { result2->
                                for(data2 in result2.documents){
                                    val song = data2.toObject(DefaultSongDisplay::class.java)
                                    if(song!=null){
                                        if(currentPlaylistList.any{it.songName==song.songName}) {
                                            continue
                                        }
                                        else{
                                            song.id = data2.id
                                            songsList.add(song)
                                        }
                                    }
                                }
                                addSongRecyclerAdapter = AddSongRecyclerAdapter(songsList,this@AddSongsFragment)

                                addSongRecyclerView.apply {
                                    layoutManager =
                                        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                                    adapter = addSongRecyclerAdapter
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("AddSongsFragment", "Error getting songs.",
                                    exception)
                            }
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w("AddSongsFragment", "Error getting songs.",
                    exception)
            }

        db.collection("genrePlaylistCovers")
            .get().addOnSuccessListener {result1->
                for (data1 in result1.documents) {
                    val playlist = data1.toObject(PlaylistCover::class.java)
                    if (playlist != null) {
                        playlist.id=data1.id
                        db.collection("genrePlaylistCovers")
                            .document(playlist.id)
                            .collection(playlist.name).get().addOnSuccessListener { result2->
                                for(data2 in result2.documents){
                                    val song = data2.toObject(DefaultSongDisplay::class.java)
                                    if(song!=null){
                                        if(currentPlaylistList.any{it.songName==song.songName}) {
                                            continue
                                        }
                                        else{
                                            song.id = data2.id
                                            songsList.add(song)
                                        }
                                    }
                                }
                                addSongRecyclerAdapter = AddSongRecyclerAdapter(songsList,this@AddSongsFragment)

                                addSongRecyclerView.apply {
                                    layoutManager =
                                        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                                    adapter = addSongRecyclerAdapter
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("AddSongsFragment", "Error getting songs.",
                                    exception)
                            }
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w("AddSongsFragment", "Error getting songs.",
                    exception)
            }

        val backBtn=view.findViewById<ImageButton>(R.id.backButtonAddSongs)
        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

    private val myArrayList = ArrayList<DefaultSongDisplay>()
    override fun onItemButtonClick(index: Int, defaultSongDisplay: DefaultSongDisplay) {
        addSongRecyclerAdapter.removeItem(index)
        val gson= Gson()
        myArrayList.add(defaultSongDisplay)
        val jsonString = gson.toJson(myArrayList)
        setFragmentResult("request_key", Bundle().apply { putString("results", jsonString) })
    }
}