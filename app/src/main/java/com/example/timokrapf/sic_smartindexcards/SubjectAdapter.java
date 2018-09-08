package com.example.timokrapf.sic_smartindexcards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/*
FROM https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10 and
https://antonioleiva.com/recyclerview-listener/
Minor changes were made.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    //Adapter to set up

    private LayoutInflater inflater;
    private List<Subject> subjectList;
    private OnItemClickListener listener;
    private Context context;
    private boolean chooseModeIsOn;

    SubjectAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
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

    //get number of subjects

    @Override
    public int getItemCount() {
        if(subjectList != null) {
            return subjectList.size();
        }
        return 0;
    }

    //check if subject already exists

    boolean isNewSubject(Subject newSubject) {
        for(int i = 0; i < getItemCount(); i++) {
            Subject currentSubject = subjectList.get(i);
            String currentTitle = currentSubject.getSubjectTitle();
            if(currentTitle.compareToIgnoreCase(newSubject.getSubjectTitle()) == 0) {
                return false;
            }
        }
        return true;
    }


    void setChooseModeIsOn(boolean chooseModeIsOn) {
        this.chooseModeIsOn = chooseModeIsOn;
    }
    void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }
     class SubjectViewHolder extends RecyclerView.ViewHolder {

         private final TextView subjectItemView;

         private SubjectViewHolder(View itemView) {
             super(itemView);
             subjectItemView = (TextView) itemView.findViewById(R.id.recyclerview_textview);
         }

         private void bind(final Subject subject, final OnItemClickListener listener) {
             if (subjectList == null) {
                 subjectItemView.setText(R.string.empty_subject_list);
             } else {
                 subjectItemView.setText(subject.getSubjectTitle());
                 itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         listener.onItemClicked(subject, subjectItemView);
                     }
                 });

                     itemView.setOnLongClickListener(new View.OnLongClickListener() {
                         @Override
                         public boolean onLongClick(View v) {
                             if (!chooseModeIsOn) {
                                 AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                 dialogBuilder.setTitle(R.string.subjects_want_to_select);
                                 dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         listener.onItemLongClicked(subject, subjectItemView);
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

     //interface to implement click Methods

     public interface OnItemClickListener {
        void onItemClicked(Subject subject, TextView itemView);
        void onItemLongClicked(Subject subject, TextView itemView);
     }
}
