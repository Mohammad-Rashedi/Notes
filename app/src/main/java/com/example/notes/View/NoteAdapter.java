package com.example.notes.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.Room.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    List<Note> notes = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(currentNote.getPriority() + "");
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener!= null && position!= RecyclerView.NO_POSITION) {
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }
    public Note getNoteAt(int position) {
        return notes.get(position);

    }

    public interface onItemClickListener{
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}

