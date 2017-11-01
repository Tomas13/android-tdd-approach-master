package com.example.androidtdd.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidtdd.R;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.util.UserListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by quanlt on 19/01/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> users;
    private UserListener mListener;
    public UserAdapter(UserListener listener) {
        users = new ArrayList<>();
        mListener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyItemRangeInserted(0, users.size());
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.mNameTextView.setText(users.get(position).getName());
        holder.mEmailTextView.setText(users.get(position).getEmail());
        holder.mPhoneTextView.setText(users.get(position).getPhone());
        holder.mWebsiteTextView.setText(users.get(position).getWebsite());
        holder.itemView.setOnClickListener(view->mListener.onUserClick(users.get(position)));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_name)
        TextView mNameTextView;
        @BindView(R.id.text_phone)
        TextView mPhoneTextView;
        @BindView(R.id.text_website)
        TextView mWebsiteTextView;
        @BindView(R.id.text_email)
        TextView mEmailTextView;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
