package com.tuo.housekeeping.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tuo.housekeeping.Activity_Sign_Orderly;
import com.tuo.housekeeping.R;
import com.tuo.housekeeping.model.PatientData;

import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.PatientViewHolder> {

    private Context mcTx;
    private List<PatientData> patientlist;
    private PatientListAdapter.ItemClickListener mClickListener;
    public PatientListAdapter(Context mcTx, List<PatientData> patientlist) {
        this.mcTx = mcTx;
        this.patientlist = patientlist;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view=LayoutInflater.from(mcTx).inflate(R.layout.job_order,parent,false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        PatientData patientData = patientlist.get(position);
        holder.room=patientData.room;
        holder.sts_desc.setText(patientData.sts_desc);
        holder.date_requested.setText(patientData.date_requested);
        holder.orderly_jo_id=patientData.orderly_jo_id;
        holder.emp_id=patientData.emp_id;
        holder.ns=patientData.ns;
        holder.bed=patientData.bed;
        holder.notes=patientData.notes;
        holder.pat_no.setText(patientData.pat_no);
        holder.pat_name=patientData.pat_name;
        holder.sts_id=patientData.sts_id;
        if(patientData.sts_desc.trim().equals("accept")){
            holder.img.setImageResource(R.mipmap.working);
            holder.card_status.setCardBackgroundColor(Color.rgb(227,242,253));
            holder.joborderstatus.setTextColor(Color.rgb(13,71,161));
        }else if (patientData.sts_desc.trim().equals("finished")){
            holder.img.setImageResource(R.mipmap.finished);
            holder.card_status.setCardBackgroundColor(Color.rgb(161, 247, 177));
            holder.joborderstatus.setTextColor(Color.rgb(24, 173, 52));

        }else if (patientData.sts_desc.trim().equals("acknowledge")){
            holder.img.setImageResource(R.mipmap.acknowledge);
            holder.card_status.setCardBackgroundColor(Color.rgb(227, 242, 253));
            holder.joborderstatus.setTextColor(Color.rgb(13, 71, 161));

        }
        else{
            holder.img.setImageResource(R.mipmap.pending);
            holder.card_status.setCardBackgroundColor(Color.rgb(255,243,224));
            holder.joborderstatus.setTextColor(Color.rgb(230,81,0));
        }
    }

    @Override
    public int getItemCount() {
        return patientlist.size();
    }


    public class PatientViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView pat_no,sts_desc,date_requested,joborderstatus;
        String orderly_jo_id,emp_id,ns,bed,notes,room,pat_name,sts_id ;
        CardView cardView,card_status;
        ImageView img;
        PatientViewHolder(View itemView) {
            super(itemView);
            joborderstatus=itemView.findViewById(R.id.txtstatus_joborder);
            img=itemView.findViewById(R.id.img_pending_joborder);
            cardView=itemView.findViewById(R.id.card_joborder);
            card_status=itemView.findViewById(R.id.card_status_joborder);
            pat_no=itemView.findViewById(R.id.txtroom_joborder);
            sts_desc=itemView.findViewById(R.id.txtstatus_joborder);
            date_requested=itemView.findViewById(R.id.txt_startime_joborder);
            orderly_jo_id="";
            emp_id="";
            ns="";
            bed="";
            room="";
            pat_name="";
            sts_id="";
            notes="";

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Sign_Orderly.class);
                    intent.putExtra("status", joborderstatus.getText());
                    intent.putExtra("ns", ns);
                    intent.putExtra("bed", bed);
                    intent.putExtra("patno", pat_no.getText());
                    intent.putExtra("patname", pat_name);
                    intent.putExtra("orderly_jo_id", orderly_jo_id);
                    intent.putExtra("empid", emp_id);
                    intent.putExtra("room", room);
                    intent.putExtra("notes", notes);
                    intent.putExtra("date_requested", date_requested.getText());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public PatientData getItem(int id) {
        return patientlist.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(PatientListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
