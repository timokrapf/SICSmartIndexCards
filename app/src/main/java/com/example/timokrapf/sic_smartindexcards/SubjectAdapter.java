package com.example.timokrapf.sic_smartindexcards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/*
https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10
https://antonioleiva.com/recyclerview-listener/
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private final LayoutInflater inflater;
    private List<Subject> subjectList;
    private final OnItemClickListener listener;

    SubjectAdapter(Context context, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_subject_item, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.bind(subject, listener);

    }

    @Override
    public int getItemCount() {
        if(subjectList != null) {
            return subjectList.size();
        }
        return 0;
    }


    void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }
     class SubjectViewHolder extends RecyclerView.ViewHolder {

         private final TextView subjectItemView;

         private SubjectViewHolder(View itemView) {
             super(itemView);
             subjectItemView = itemView.findViewById(R.id.recyclerview_textview);
         }

         private void bind(final Subject subject, final OnItemClickListener listener) {
             if (subjectList == null) {
                 subjectItemView.setText(R.string.empty_subject_list);
             } else {
                 subjectItemView.setText(subject.getSubjectTitle());
                 itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         listener.onItemClicked(subject);
                     }
                 });
                 itemView.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View v) {
                         listener.onItemLongClicked(subject);
                         return true;
                     }
                 });
             }

         }
     }

     public interface OnItemClickListener {
        void onItemClicked(Subject subject);
        void onItemLongClicked(Subject subject);
     }
}
