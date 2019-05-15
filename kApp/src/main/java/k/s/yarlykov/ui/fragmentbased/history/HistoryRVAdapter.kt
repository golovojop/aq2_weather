/**
 * Materials:
 *
 * viewType in onCreateViewHolder^
 * https://forums.bignerdranch.com/t/in-oncreateviewholder-you-pass-in-an-int-called-viewtype/7910
 */

package k.s.yarlykov.ui.fragmentbased.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.History
import k.s.yarlykov.util.Utils.logI

class HistoryRVAdapter(val source: List<History>): RecyclerView.Adapter<HistoryRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        logI(this, "onCreateViewHolder")
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.history_item,
                        parent,
                        false
                )
        )

    }

    override fun getItemCount() = source.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        logI(this, "onBindViewHolder")
        viewHolder.bind(source[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        val imgLogo = itemView.findViewById<ImageView>(R.id.ivIcon)
        val tvValue = itemView.findViewById<TextView>(R.id.tvValue)

        fun bind(history: History) = with(history){
            tvDate.text = history.date
            tvValue.text = history.temperature
            imgLogo.setImageResource(history.img)
        }
    }
}