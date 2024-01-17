package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPlaylistsFragment : Fragment(),
    MyPlaylistsRecyclerAdapter.ContentListener,
    MyPlaylistsRecyclerAdapter.OnItemClickListener
{
    private lateinit var myPlaylistsRecyclerAdapter: MyPlaylistsRecyclerAdapter
    private val db = Firebase.firestore
    private val myPlaylistsList=ArrayList<MyPlaylist>()
    private lateinit var auth:FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userEmail:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_playlists, container, false)

        val myPlaylistsRecyclerView=view.findViewById<RecyclerView>(R.id.myPlaylistsView)
        auth=FirebaseAuth.getInstance()
        user=auth.currentUser!!
        userEmail=user.email!!

        db.collection("users")
            .document(userEmail)
            .collection("myPlaylists")
            .get()
            .addOnSuccessListener { result ->
                for (data in result.documents) {
                    val playlist = data.toObject(MyPlaylist::class.java)
                    if (playlist != null) {
                        playlist.id = data.id
                        myPlaylistsList.add(playlist)
                    }
                }
                myPlaylistsRecyclerAdapter = MyPlaylistsRecyclerAdapter(
                    myPlaylistsList,
                    this@MyPlaylistsFragment,
                    this@MyPlaylistsFragment
                )

                myPlaylistsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        requireActivity(),
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = myPlaylistsRecyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w(
                    "MyPlaylistFragment", "Error getting playlists.",
                    exception
                )
            }

        val addPlaylistButton=view.findViewById<Button>(R.id.addPlaylistButton)
        addPlaylistButton.setOnClickListener {
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, AddPlaylistFragment())?.commit()
        }

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("bundleKey")
            val myPlaylist = MyPlaylist()
            if (result != null) {
                myPlaylist.name = result
            }

            val doc = db.collection("users")
                .document(userEmail)
                .collection("myPlaylists")
                .document()
            myPlaylist.id = doc.id
            doc.set(myPlaylist).addOnSuccessListener {
                Log.i("MyPlaylistFragment", "Playlist added.",)
            }
        }
        return view
    }

    override fun onItemButtonClick(index: Int, myPlaylist: MyPlaylist) {
        myPlaylistsRecyclerAdapter.removeItem(index)
        db.collection("users")
            .document(userEmail)
            .collection("myPlaylists")
            .document(myPlaylist.id)
            .delete()

        db.collection("users")
            .document(userEmail)
            .collection("myPlaylists")
            .document(myPlaylist.id)
            .collection(myPlaylist.name)
            .get().addOnSuccessListener { result->
                for(data in result.documents) {
                    db.collection("users")
                        .document(userEmail)
                        .collection("myPlaylists")
                        .document(myPlaylist.id)
                        .collection(myPlaylist.name).document(data.id).delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MyPlaylistFragment", "Error deleting playlist.",
                    exception)
            }
    }


    override fun onItemClick(position: Int, list: ArrayList<MyPlaylist>) {
        val bundle=Bundle()
        val fragment=CustomPlaylistFragment()
        bundle.putString("userEmail",userEmail)
        bundle.putString("myPlaylistName", myPlaylistsList[position].name)
        bundle.putString("myPlaylistId",myPlaylistsList[position].id)
        fragment.arguments=bundle

        val fragmentTransaction: FragmentTransaction? =
            activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.container, fragment)?.addToBackStack(null)?.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myPlaylistsList.removeAll(myPlaylistsList.toSet())
    }
}