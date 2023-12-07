package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult


class AddPlaylistFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_add_playlist, container, false)

        val playlistName = view.findViewById<EditText>(R.id.enteredPlaylistName)
        val cancelText = view.findViewById<TextView>(R.id.cancelText)
        val createText = view.findViewById<TextView>(R.id.createText)

        cancelText.setOnClickListener {
            val fragmentTransaction: FragmentTransaction? =
                activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, MyPlaylistsFragment())?.commit()
        }

        createText.setOnClickListener {
            val result = playlistName.text.toString()
            if (TextUtils.isEmpty(result)) {
                playlistName.error = "Enter playlist name."
                playlistName.requestFocus()
            } else {
                setFragmentResult("requestKey", bundleOf("bundleKey" to result))
                val fragmentTransaction: FragmentTransaction? =
                    activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.container, MyPlaylistsFragment())?.commit()
            }
        }
        return view
    }
}