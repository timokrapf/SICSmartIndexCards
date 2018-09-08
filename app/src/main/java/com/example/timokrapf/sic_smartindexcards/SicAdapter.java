package com.example.timokrapf.sic_smartindexcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SicAdapter extends RecyclerView.Adapter<SicAdapter.SicViewHolder> {

    //Adapter to fill Overview Activity

    private Context context;
    private LayoutInflater inflater;
    private List<SmartIndexCards> cards;
    private OnItemClickListener listener;
    private SmartIndexCards currentCard;
    private boolean chooseModeIsOn;

    SicAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    //returns viewholder for specific view

    @NonNull
    @Override
    public SicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.overview_item, parent, false);
        return new SicViewHolder(view);
    }
    //connects specific card to viewholder
    @Override
    public void onBindViewHolder(@NonNull SicViewHolder holder, int position) {
        SmartIndexCards card = cards.get(position);
        holder.bind(card, listener);
    }

    @Override
    public int getItemCount() {
        if(cards != null) {
            return cards.size();
        } else {
            return 0;
        }
    }

    //sets boolean to inform adapter about current status

    void setChooseModeIsOn(boolean chooseModeIsOn) {
        this.chooseModeIsOn = chooseModeIsOn;
    }

    //sets list for adapter

    void setCardsList(List<SmartIndexCards> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
    //sets current card for overviewactivity

    void setCurrentCard(SmartIndexCards card) {
        currentCard = card;
    }

    //gets current card

    SmartIndexCards getCurrentCard() {
        return currentCard;
    }

    //connects correct view with specific database item (sets correct Text) and sets listener

    public class SicViewHolder extends RecyclerView.ViewHolder {

        private final TextView view;

        public SicViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.overview_recycler);
        }

        private void bind(final SmartIndexCards card, final OnItemClickListener listener) {
            if(cards == null) {
                view.setText(R.string.no_cards_created);
            } else {
                view.setText(card.getQuestion());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClicked(card, view);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!chooseModeIsOn) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setTitle(R.string.cards_want_to_select);
                            dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listener.onItemLongClicked(card, view);
                                    chooseModeIsOn = true;
                                    dialog.cancel();
                                }
                            });
                            dialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialogBuilder.create().show();
                        }
                        return true;
                    }
                });
            }
        }
    }

    //Listener for events

    public interface OnItemClickListener {
        void onItemClicked(SmartIndexCards card, TextView view);
        void onItemLongClicked(SmartIndexCards card, TextView view);
    }
}
