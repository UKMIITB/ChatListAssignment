package com.example.chatlistassignment.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatlistassignment.R;
import com.example.chatlistassignment.interfaces.ItemClickListener;
import com.example.chatlistassignment.model.User;
import com.example.chatlistassignment.utils.ChatDateHeader;

public class ChatListRecyclerViewAdapter extends PagedListAdapter<User, ChatListRecyclerViewAdapter.ViewHolder> {
    ItemClickListener itemClickListener;


    public static DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.get_id() == newItem.get_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    public ChatListRecyclerViewAdapter(ItemClickListener itemClickListener) {
        super(DIFF_CALLBACK);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = getItem(position);
        holder.textViewName.setText(user.getName());
        holder.textViewNumber.setText(user.getContactNumber());

        setUpHeaderData(user, holder.textViewHeader, position);

        if (user.getProfilePic() != null) {
            Glide.with(holder.imageViewProfilePic.getContext())
                    .load(Uri.parse(user.getProfilePic()))
                    .error(R.drawable.ic_baseline_person_24)
                    .into(holder.imageViewProfilePic);
        } else {
            Glide.with(holder.imageViewProfilePic.getContext())
                    .load(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .into(holder.imageViewProfilePic);
        }
    }

    private void setUpHeaderData(User user, TextView textViewHeader, int position) {
        if (user == null)
            return;

        String currentUserDateHeader = ChatDateHeader.getChatDateHeader(user.getDate());
        if (position > 0) {

            User prevUser = getItem(position - 1);

            if (prevUser != null) {

                String prevUserDateHeader = ChatDateHeader.getChatDateHeader(prevUser.getDate());
                if (currentUserDateHeader.equals(prevUserDateHeader))
                    textViewHeader.setVisibility(View.GONE);
                else
                    setHeaderDate(currentUserDateHeader, textViewHeader);

            } else
                setHeaderDate(currentUserDateHeader, textViewHeader);

        } else
            setHeaderDate(currentUserDateHeader, textViewHeader);
    }

    private void setHeaderDate(String date, TextView dateTextView) {
        dateTextView.setVisibility(View.VISIBLE);
        dateTextView.setText(date);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView imageViewProfilePic;
        TextView textViewName, textViewNumber, textViewHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfilePic = itemView.findViewById(R.id.image_view_profile_pic);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewNumber = itemView.findViewById(R.id.text_view_number);
            textViewHeader = itemView.findViewById(R.id.text_view_header);

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClicked(view, getItem(getAdapterPosition()));
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemLongClicked(view, getItem(getAdapterPosition()), getAdapterPosition());

            return true;
        }
    }
}
