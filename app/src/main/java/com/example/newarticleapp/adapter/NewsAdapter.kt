package com.example.newarticleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.newarticleapp.data.Article
import com.example.newarticleapp.databinding.ItemNewsBinding

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class NewsAdapter(
    private val onItemClick: ((Article) -> Unit)? = null
) : ListAdapter<Article, NewsAdapter.NewsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.tvTitle.text = article.title ?: "No title"
            binding.tvDescription.text = article.description ?: ""
            binding.tvPublishedAt.text = formatPublishedAt(article.publishedAt)

            val imageUrl = article.urlToImage
            if (imageUrl.isNullOrBlank()) {
                binding.ivThumbnail.setImageResource(android.R.color.transparent)
                binding.ivThumbnail.visibility = android.view.View.GONE
            } else {
                binding.ivThumbnail.visibility = android.view.View.VISIBLE
                binding.ivThumbnail.load(imageUrl) {
                    crossfade(true)
                    placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    error(android.R.drawable.ic_menu_report_image)
                }
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(article)
            }
        }

        private fun formatPublishedAt(raw: String?): String {
            if (raw.isNullOrBlank()) return ""
            return try {
                // API gives ISO8601 like 2023-07-03T10:00:00Z
                val odt = OffsetDateTime.parse(raw)
                odt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
            } catch (e: DateTimeParseException) {
                raw // fallback to raw string
            } catch (t: Throwable) {
                raw
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            // URL is usually unique; fallback to title if URL missing
            return (oldItem.url != null && oldItem.url == newItem.url)
                    || (oldItem.title != null && oldItem.title == newItem.title)
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }
}

