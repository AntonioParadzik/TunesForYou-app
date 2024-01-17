package hr.ferit.antonioparadzik.tunesforyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyPlaylistsRecyclerAdapter(
    val items:ArrayList<MyPlaylist>,
    val contentListener: ContentListener,
    val listener:OnItemClickListener
    ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyPlaylistViewholder(
            LayoutInflater.from(parent.context).inflate(R.layout.my_playlists_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MyPlaylistViewholder->{
                holder.bind(items[position],contentListener,position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(myPlaylist: MyPlaylist){
        items.add(0,myPlaylist)
        notifyItemInserted(0)
        notifyItemRangeChanged(0,items.size)
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    inner class MyPlaylistViewholder(val view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        private val playlistName=view.findViewById<TextView>(R.id.myPlaylistNameText)
        private val deleteBtn=view.findViewById<ImageButton>(R.id.deletePlaylistImageButton)

        fun bind(
            myPlaylist: MyPlaylist,
            contentListener:ContentListener,
            index: Int,

        ){
            playlistName.text=myPlaylist.name

            deleteBtn.setOnClickListener {
                contentListener.onItemButtonClick(index, myPlaylist)
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
        fun onItemButtonClick(index: Int, myPlaylist: MyPlaylist)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,list:ArrayList<MyPlaylist>)
    }
}