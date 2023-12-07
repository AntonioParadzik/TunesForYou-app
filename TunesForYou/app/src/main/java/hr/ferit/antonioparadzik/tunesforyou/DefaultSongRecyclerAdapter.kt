package hr.ferit.antonioparadzik.tunesforyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DefaultSongRecyclerAdapter(
    val items:ArrayList<DefaultSongDisplay>,
    val listener: OnItemClickListener
    ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultSongViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.default_song_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is DefaultSongViewHolder->{
                holder.bind(items[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    inner class DefaultSongViewHolder(val view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        private val thumbnailUrl=view.findViewById<ImageView>(R.id.thumbnailImage)
        private val songName=view.findViewById<TextView>(R.id.songNameText)
        private val artistName=view.findViewById<TextView>(R.id.artistNameText)

        fun bind(
            defaultSongDisplay:DefaultSongDisplay
        ){
            Glide.with(view.context).load(defaultSongDisplay.thumbnailUrl).into(thumbnailUrl)
            songName.text=defaultSongDisplay.songName
            artistName.text=defaultSongDisplay.artistName
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
        fun onItemClick(position: Int,list:ArrayList<DefaultSongDisplay>)
    }
}