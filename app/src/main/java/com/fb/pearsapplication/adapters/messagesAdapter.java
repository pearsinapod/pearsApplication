package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fb.pearsapplication.R;
import com.fb.pearsapplication.models.PearMessage;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;


public class messagesAdapter extends RecyclerView.Adapter<messagesAdapter.ViewHolder> {
    private List<PearMessage> mPearMessages;
    private Context mContext;
    private String mUserId;

    public messagesAdapter(Context context, String userId, List<PearMessage> pearMessages) {
        mPearMessages = pearMessages;
        this.mUserId = userId;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_message, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        com.fb.pearsapplication.models.PearMessage message = mPearMessages.get(position);
//        final boolean isMe = () != null && message.getUserId().equals(mUserId);
//
//        if (isMe) {
//            holder.imageMe.setVisibility(View.VISIBLE);
//            holder.imageOther.setVisibility(View.GONE);
//            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
//        } else {
//            holder.imageOther.setVisibility(View.VISIBLE);
//            holder.imageMe.setVisibility(View.GONE);
//            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//        }
//
//        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
//        Glide.with(mContext).load(getProfileUrl(message.getUserId())).into(profileView);
//        holder.body.setText(message.getBody());
//
//    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }


    @Override
    public int getItemCount() {
        return mPearMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }

    }
}
