package hr.ferit.antonioparadzik.tunesforyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlaylistRecyclerAdapter(
    val items:ArrayList<PlaylistCover>,
    val listener:OnItemClickListener
    ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PlaylistViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class PlaylistViewHolder(val view:View):RecyclerView.ViewHolder(view),View.OnClickListener{
        private val playlistImage=view.findViewById<ImageView>(R.id.playlistImage)
        private val playlistName=view.findViewById<TextView>(R.id.playlistNameText)

        fun bind(playlistCover: PlaylistCover){
            Glide.with(view.context).load(playlistCover.imageUrl).into(playlistImage)
            playlistName.text=playlistCover.name
        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position=absoluteAdapterPosition
            listener.onItemClick(position,items)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,list:ArrayList<PlaylistCover>)
    }
}