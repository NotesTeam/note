package com.twago.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.RealmResults;

class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private static final String TAG = NoteListAdapter.class.getSimpleName();
    private RealmResults<Note> noteList;
    private NoteListAdapterInterface noteListAdapterInterface;
    private ArrayList<ViewHolder> viewHolders;

    NoteListAdapter(NoteListAdapterInterface noteListAdapterInterface, RealmResults<Note> noteList) {
        this.noteListAdapterInterface = noteListAdapterInterface;
        this.noteList = noteList;
        viewHolders = new ArrayList<>();
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoteListAdapter.ViewHolder holder, final int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
        holder.itemView.setBackgroundColor(position % 2 == 0 ? Constants.COLOR_WHITE : Constants.COLOR_GRAY);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isAnyHolderChecked())
                        noteListAdapterInterface.openDialogFragment(note.getId());
                    else{
                        holder.checkNote.setChecked(!holder.checkNote.isChecked());
                        if (!isAnyHolderChecked()) {
                            hideAllCheckBoxes();
                            noteListAdapterInterface.hideDeleteButton();
                            noteListAdapterInterface.checkOrUncheckNote(note.getId());
                        }
                    }
                }
            });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!isAnyHolderChecked()) {
                        showAllCheckBoxes();
                        noteListAdapterInterface.showDeleteButton();
                        holder.checkNote.setChecked(true);
                        noteListAdapterInterface.checkOrUncheckNote(note.getId());
                    }
                    return true;
                }
            });

        viewHolders.add(holder);
    }

    private void showAllCheckBoxes(){
        for (ViewHolder holder : viewHolders){
            holder.checkNote.setVisibility(View.VISIBLE);
        }
    }
    private void hideAllCheckBoxes(){
        for (ViewHolder holder : viewHolders){
            holder.checkNote.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isAnyHolderChecked(){
        for (ViewHolder holder : viewHolders){
            if(holder.checkNote.isChecked())
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;
        CheckBox checkNote;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_list_row_title);
            text = (TextView) itemView.findViewById(R.id.note_list_row_text);
            checkNote = (CheckBox) itemView.findViewById(R.id.note_list_row_check_note);
        }
    }
}