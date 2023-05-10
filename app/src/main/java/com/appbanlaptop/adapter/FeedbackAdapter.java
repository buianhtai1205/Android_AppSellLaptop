package com.appbanlaptop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.appbanlaptop.model.Feedback;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{
    List<Feedback> arrayFeedback;

    public FeedbackAdapter(List<Feedback> arrayFeedback) {
        this.arrayFeedback = arrayFeedback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageViewAvatar;
        AppCompatRatingBar ratingBar;
        TextView tvComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent,false);
        return new FeedbackAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(arrayFeedback.get(position).getUser_avatar()).into(holder.imageViewAvatar);
        holder.ratingBar.setRating((float)arrayFeedback.get(position).getStar());
        holder.tvComment.setText(arrayFeedback.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return arrayFeedback.size();
    }
}
