package com.example.eduhub;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private List<TeacherModel> teacherList;

    public TeacherAdapter(List<TeacherModel> teacherList) {
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        TeacherModel teacher = teacherList.get(position);
        holder.name.setText(teacher.getName());
        holder.designation.setText(teacher.getDesignation());
        holder.email.setText( teacher.getEmail());
        holder.phone.setText(teacher.getPhone());


        holder.phone.setOnClickListener(v -> {
            String phoneNumber=teacher.getPhone();
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phoneNumber));
            v.getContext().startActivity(intent);
        });

        holder.email.setOnClickListener(v -> {
            String emailAccount=teacher.getEmail();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + emailAccount));

            intent.putExtra(Intent.EXTRA_SUBJECT, "Hello Sir");
            intent.putExtra(Intent.EXTRA_TEXT, "I am a student of your department.");


            v.getContext().startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView name, designation, email, phone;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            designation = itemView.findViewById(R.id.teacherDesignation);
            email = itemView.findViewById(R.id.teacherEmail);
            phone = itemView.findViewById(R.id.teacherPhone);
        }
    }
}
