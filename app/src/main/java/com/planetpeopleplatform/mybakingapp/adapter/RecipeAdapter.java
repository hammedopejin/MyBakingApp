package com.planetpeopleplatform.mybakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planetpeopleplatform.mybakingapp.R;
import com.planetpeopleplatform.mybakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hammedopejin on 5/25/2018.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> mRecipe;
    public Context mContext;

    final private RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;
        public final TextView mRecipeNameTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.thumbnail);
            mRecipeNameTV = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mClickHandler.onClick(adapterPosition);
            }
        }
    }

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mRecipe = recipes;
        mClickHandler = clickHandler;
    }


    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, final int position) {

        holder.mRecipeNameTV.setText(mRecipe.get(position).getName());

        if (TextUtils.isEmpty(mRecipe.get(position).getImage())) {
            switch (position) {
                case 0:
                    holder.mImageView.setImageResource(R.drawable.nutella_pie);
                    break;
                case 1:
                    holder.mImageView.setImageResource(R.drawable.brownies);
                    break;
                case 2:
                    holder.mImageView.setImageResource(R.drawable.yellow_cake);
                    break;
                case 3:
                    holder.mImageView.setImageResource(R.drawable.cheesecake);
                    break;
            }
        } else {
            Picasso.with(mContext).load(mRecipe.get(position).getImage()).placeholder(R.drawable.baking_pic)
                    .error(R.drawable.baking_pic).into(holder.mImageView);
        }

    }

    @Override
    public int getItemCount() {
        if (null != mRecipe) {
            return mRecipe.size();
        }
        return  0;
    }

}
