package hr.ferit.antonioparadzik.tunesforyou

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoPlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video_player, container, false)

        val youTubePlayerView=view.findViewById<YouTubePlayerView>(R.id.youtubePlayerView)
        val videoId=arguments?.getString("videoId").toString()
        youTubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady( youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0F)
            }
        })

        val backButton=view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return view
    }
}