package com.fb.pearsapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fb.pearsapplication.R;
import com.fb.pearsapplication.fragments.groupDetailsFragment;
import com.fb.pearsapplication.models.Group;
import com.parse.ParseFile;

import java.util.List;

public class groupsAdapter extends RecyclerView.Adapter<groupsAdapter.ViewHolder>{

    private Context context;
    private List<Group> groups;

    public groupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }



    //Clean all elements of the recycler
    public void clear() {
        groups.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Group> list) {
        groups.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //track view objects
        private de.hdodenhof.circleimageview.CircleImageView ivGroupImage;
        private TextView tvGroupName;
        //private TextView tvGroupNumber;
        //private TextView tvGroupDescription;
        //private Button btnJoin;

        public ViewHolder(View itemView) {
            super(itemView);
            // lookup view objects by id
            ivGroupImage = itemView.findViewById(R.id.ivGroupImage);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);

            // add this as the itemView's OnClickListener

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the group at the position, this won't work if the class is static
                        Group group = groups.get(position);
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("anything", group);
                        Fragment fragment = new groupDetailsFragment();
                        fragment.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, 0, 0)
                                .replace(R.id.flContainter, fragment).addToBackStack(null).commit();
                    }
                }
            });
        }

        public void bind(Group group) {
            tvGroupName.setText(group.getGroupName());
            ParseFile image = group.getGroupImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivGroupImage);
            }
            else{
                Glide.with(context).load(R.drawable.group_search_placeholder).apply(RequestOptions.circleCropTransform()).into(ivGroupImage);
            }
        }


    }
}
