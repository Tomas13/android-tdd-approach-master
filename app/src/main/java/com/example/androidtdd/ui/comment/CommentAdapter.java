package com.example.androidtdd.ui.comment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by quanlt on 23/01/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    public CommentAdapter() {
        comments = new ArrayList<>();
    }

    public void setComments(List<Comment> comments) {
        this.comments.addAll(comments);
        notifyItemRangeInserted(0, comments.size());
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.mBodyTextView.setText(comments.get(position).getBody());
        holder.mExtraTextView.setText(comments.get(position).getName()
                + "|" + comments.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_body)
        TextView mBodyTextView;
        @BindView(R.id.text_extra)
        TextView mExtraTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
