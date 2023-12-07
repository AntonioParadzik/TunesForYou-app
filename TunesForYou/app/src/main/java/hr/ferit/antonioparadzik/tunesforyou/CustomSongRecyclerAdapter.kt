package hr.ferit.antonioparadzik.tunesforyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomSongRecyclerAdapter (
    val items:ArrayList<DefaultSongDisplay>,
    val contentListener: ContentListener,
    val listener: OnItemClickListener,
    ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomSongViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_song_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CustomSongViewHolder->{
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

    inner class CustomSongViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        private val thumbnailUrl=view.findViewById<ImageView>(R.id.mySongImage)
        private val songName=view.findViewById<TextView>(R.id.mySongNameText)
        private val artistName=view.findViewById<TextView>(R.id.myArtistNameText)
        private val deleteSongBtn=view.findViewById<ImageButton>(R.id.deleteSongImageButton)

        fun bind(
            defaultSongDisplay:DefaultSongDisplay,
            contentListener: ContentListener,
            index:Int,
        ){
            Glide.with(view.context).load(defaultSongDisplay.thumbnailUrl).into(thumbnailUrl)
            songName.text=defaultSongDisplay.songName
            artistName.text=defaultSongDisplay.artistName

            deleteSongBtn.setOnClickListener {
                contentListener.onItemButtonClick(index, defaultSongDisplay)
            }
        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position=absoluteAdapterPosition
            listener.onItemClick(position,items)
        }

    }

    interface ContentListener {
        fun onItemButtonClick(index: Int, defaultSongDisplay: DefaultSongDisplay)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,list:ArrayList<DefaultSongDisplay>)
    }
}