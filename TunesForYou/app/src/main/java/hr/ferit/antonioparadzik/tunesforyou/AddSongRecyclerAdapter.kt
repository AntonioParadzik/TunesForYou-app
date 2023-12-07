package hr.ferit.antonioparadzik.tunesforyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AddSongRecyclerAdapter (
    val items:ArrayList<DefaultSongDisplay>,
    val contentListener: ContentListener,
    ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddSongViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_song_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AddSongViewHolder->{
                holder.bind(items[position],contentListener,position)
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    class AddSongViewHolder(val view: View): RecyclerView.ViewHolder(view){
        private val thumbnailUrl=view.findViewById<ImageView>(R.id.addSongImage)
        private val songName=view.findViewById<TextView>(R.id.addSongNameText)
        private val artistName=view.findViewById<TextView>(R.id.addSongArtistText)
        private val addSongBtn=view.findViewById<ImageButton>(R.id.addSongImageButton)

        fun bind(
            defaultSongDisplay:DefaultSongDisplay,
            contentListener: ContentListener,
            index:Int
        ){
            Glide.with(view.context).load(defaultSongDisplay.thumbnailUrl).into(thumbnailUrl)
            songName.text=defaultSongDisplay.songName
            artistName.text=defaultSongDisplay.artistName

            addSongBtn.setOnClickListener {
                contentListener.onItemButtonClick(index, defaultSongDisplay)
            }
        }

    }

    interface ContentListener {
        fun onItemButtonClick(index: Int, defaultSongDisplay: DefaultSongDisplay)
    }

}