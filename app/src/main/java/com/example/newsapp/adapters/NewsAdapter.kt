package com.example.newsapp.adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapp.News
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemListViewBinding

class NewsAdapter(private val newsArray: ArrayList<News>) :
    BaseAdapter() {
    override fun getCount() = newsArray.size

    override fun getItem(position: Int) = newsArray[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val context = parent?.context
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.item_list_view, parent, false)

        val news = newsArray[position]
        val textViewTitle = rowView.findViewById<TextView>(R.id.text_view_title)
        textViewTitle.text = news.title
        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        myIntent.setPackage("com.android.chrome")
//        binding.imageViewWebLink

        textViewTitle.setOnClickListener {
            try {
                context.startActivity(myIntent)
            } catch (ex: ActivityNotFoundException) {
                myIntent.setPackage(null);
                context.startActivity(myIntent);
            }
        }
        return rowView
    }


}