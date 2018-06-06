package com.planetpeopleplatform.mybakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.planetpeopleplatform.mybakingapp.R;
import com.planetpeopleplatform.mybakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hammedopejin on 5/25/2018.
 */

public  class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {



    private List<Step> mStepArray;
    public Context mContext;

    final private IngredientAdapterOnClickHandler mClickHandler;
    final private IngredientAdapterOnClickHandler mClickHandler2;

    /**
     * The interface that receives onClick messages.
     */
    public interface IngredientAdapterOnClickHandler {
        void onClick(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mStepThumbnail;
        public final TextView mStepDescriptionTV;


        public ViewHolder(View itemView) {
            super(itemView);
            mStepDescriptionTV = itemView.findViewById(R.id.step_description);
            mStepThumbnail = itemView.findViewById(R.id.step_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mClickHandler.onClick(adapterPosition);
                mClickHandler2.onClick(adapterPosition);
            }
        }
    }

    public IngredientAdapter(Context context, List<Step> stepArray, IngredientAdapterOnClickHandler clickHandler,
                             IngredientAdapterOnClickHandler clickHandler2) {
        mContext = context;
        mStepArray = stepArray;
        mClickHandler = clickHandler;
        mClickHandler2 = clickHandler2;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {

        holder.mStepDescriptionTV.setText(mStepArray.get(position).getShortDescription());

        if (mStepArray.get(position).getThumbnailURL().isEmpty()) {
            holder.mStepThumbnail.setImageResource(R.drawable.baking_pic);
        } else {
            Picasso.with(mContext).load(mStepArray.get(position).getThumbnailURL()).placeholder(R.drawable.baking_pic)
                    .error(R.drawable.baking_pic).into(holder.mStepThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        if (null != mStepArray) {
            return mStepArray.size();
        }
        return  0;
    }

}
