package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomPlaylistFragment : Fragment(),
    CustomSongRecyclerAdapter.OnItemClickListener,
    CustomSongRecyclerAdapter.ContentListener
{
    private val db= Firebase.firestore
    private lateinit var customSongRecyclerAdapter: CustomSongRecyclerAdapter
    private val songsList=ArrayList<DefaultSongDisplay>()
    lateinit var customPlaylistName:TextView
    lateinit var customPlaylistId:String
    lateinit var userEmail:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom_playlist, container, false)
        val customSongRecyclerView = view.findViewById<RecyclerView>(R.id.customSongsView)

        customPlaylistName = view.findViewById(R.id.customPlaylistName)
        customPlaylistName.text = arguments?.getString("myPlaylistName").toString()
        customPlaylistId = arguments?.getString("myPlaylistId").toString()
        userEmail=arguments?.getString("userEmail").toString()

        val backBtn = view.findViewById<ImageButton>(R.id.backButtonCoustomPlaylist)
        backBtn.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        setFragmentResultListener("request_key"){ requestKey, bundle ->
            val gson= Gson()
            val jsonString = bundle.getString("results")
            val listType = object : TypeToken<ArrayList<DefaultSongDisplay>>() {}.type
            val myArrayList: ArrayList<DefaultSongDisplay> = gson.fromJson(jsonString, listType)

            for(data in myArrayList) {
                db.collection("users").document(userEmail)
                    .collection("myPlaylists")
                    .document(customPlaylistId)
                    .collection(customPlaylistName.text.toString()).add(data)
                    .addOnSuccessListener {
                        Log.d("CustomPlaylistFragment", "Song added.")
                        data.id = it.id
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "CustomPlaylistFragment", "Error adding song.",
                            exception
                        )
                    }
            }
        }

        db.collection("users").document(userEmail)
            .collection("myPlaylists")
            .document(customPlaylistId)
            .collection(customPlaylistName.text.toString())
            .get().addOnSuccessListener { result ->
                for (data in result.documents) {
                    val song = data.toObject(DefaultSongDisplay::class.java)
                    if (song != null) {
                        song.id = data.id
                        songsList.add(song)
                    }
                }
                customSongRecyclerAdapter = CustomSongRecyclerAdapter(
                    songsList,
                    this@CustomPlaylistFragment,
                    this@CustomPlaylistFragment
                )
                customSongRecyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = customSongRecyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w(
                    "CustomPlaylistFragment", "Error getting songs.",
                    exception
                )
            }

        val addSongsButton = view.findViewById<Button>(R.id.addSongsButton)
        addSongsButton.setOnClickListener {
            val bundle=Bundle()
            val fragment=AddSongsFragment()
            bundle.putString("userEmail",userEmail)
            bundle.putString("myPlaylistId",customPlaylistId)
            bundle.putString("myPlaylistName",customPlaylistName.text.toString())
            fragment.arguments=bundle
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, fragment)?.addToBackStack(null)
                ?.commit()
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

    override fun onItemButtonClick(index: Int, defaultSongDisplay: DefaultSongDisplay) {
        customSongRecyclerAdapter.removeItem(index)
        db.collection("users").document(userEmail)
            .collection("myPlaylists")
            .document(customPlaylistId)
            .collection(customPlaylistName.text.toString())
            .document(defaultSongDisplay.id).delete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        songsList.removeAll(songsList.toSet())
    }

}