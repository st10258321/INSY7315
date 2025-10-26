package vcmsa.projects.wil_hustlehub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Model.Review
import vcmsa.projects.wil_hustlehub.R

class ReviewAdapter(
    private var reviewList: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewerName: TextView = itemView.findViewById(R.id.reviewerName)
        val reviewText: TextView = itemView.findViewById(R.id.reviewText)
        val reviewStarLayout: LinearLayout = itemView.findViewById(R.id.reviewStarRatingLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]

        // Set reviewer name and review text
        holder.reviewerName.text = review.reviewerName
        holder.reviewText.text = review.reviewText

        // Update stars dynamically
        val totalStars = 5
        holder.reviewStarLayout.removeAllViews() // Clear existing stars to avoid duplication

        for (i in 1..totalStars) {
            val star = ImageView(holder.itemView.context)
            star.layoutParams = LinearLayout.LayoutParams(40, 40) // Adjust size if needed
            star.setImageResource(R.drawable.ic_star_24dp)

            // Apply color tint based on rating
            val tintColor = if (i <= review.stars)
                holder.itemView.context.getColor(R.color.soft_orange)
            else
                holder.itemView.context.getColor(R.color.text_light_grey)

            star.setColorFilter(tintColor)
            holder.reviewStarLayout.addView(star)
        }
    }

    override fun getItemCount(): Int = reviewList.size

    // Optional: allow updating the list dynamically
    fun updateReviews(newReviews: List<Review>) {
        reviewList = newReviews
        notifyDataSetChanged()
    }
}
