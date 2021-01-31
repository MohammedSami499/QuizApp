package com.example.quizapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.example.quizapp.model.QuizListModel;

import java.util.ArrayList;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {
    ArrayList<QuizListModel> ModelList = new ArrayList<>();
    private static final String TAG = "QuizListAdapter";
    private OnQuizListItemClicked onQuizListItemClicked;

    public QuizListAdapter(OnQuizListItemClicked onQuizListItemClicked){
        this.onQuizListItemClicked = onQuizListItemClicked;
    }
    @NonNull
    @Override
    public QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListViewHolder holder, int position) {

        String imageUrl = ModelList.get(position).getImage();
        Glide
                .with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);

        holder.title.setText(ModelList.get(position).getName());
        holder.desc.setText(ModelList.get(position).getDesc());
        holder.level.setText(ModelList.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
            return ModelList.size();
    }

    public void getListFromDataBase(ArrayList<QuizListModel> movieList) {
        ModelList = movieList;
        notifyDataSetChanged();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView title;
        TextView level;
        TextView desc;
        Button listButton;
        public QuizListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.list_image);
            title = itemView.findViewById(R.id.list_title);
            desc = itemView.findViewById(R.id.list_disc);
            level = itemView.findViewById(R.id.list_difficulty);
            listButton = itemView.findViewById(R.id.list_btn);

            listButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizListItemClicked.onItemClicked(getAdapterPosition());
        }
    }
    public interface OnQuizListItemClicked {
        public void onItemClicked(int position);
    }

}
