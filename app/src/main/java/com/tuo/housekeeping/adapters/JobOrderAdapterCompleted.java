package com.tuo.housekeeping.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tuo.housekeeping.Activity_Sign_JobOrder;
import com.tuo.housekeeping.R;
import com.tuo.housekeeping.model.JO;

import java.util.ArrayList;
import java.util.List;

public class JobOrderAdapterCompleted extends RecyclerView.Adapter<JobOrderAdapterCompleted.JobViewHolder> implements Filterable {
    private Context mcTx;
    private List<JO> jobOrders;
    private List<JO> filteredjobOrders;
    private ItemClickListener mClickListener;
    public JobOrderAdapterCompleted(Context mcTx, List<JO> jobOrders) {
        this.mcTx = mcTx;
        this.jobOrders = jobOrders;

    }

    @NonNull
    @Override
    public JobOrderAdapterCompleted.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcTx).inflate(R.layout.job_order,parent,false);
        return new JobOrderAdapterCompleted.JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobOrderAdapterCompleted.JobViewHolder holder, int position) {
        JO jo = jobOrders.get(position);
        holder.joborderstatus.setText(jo.sts_desc);
        holder.room.setText(jo.room);
        holder.bed=jo.bed;
        holder.hkjoborderid=jo.hk_jo_id;
        holder.notes=jo.notes;
        holder.date_requested_info=jo.date_requested;
        holder.date_requested.setText(jo.date_requested);
        holder.emp_id=jo.emp_id;
        if(jo.sts_desc.trim().equals("working")){
            holder.img.setImageResource(R.mipmap.working);
            holder.card_status.setCardBackgroundColor(Color.rgb(227,242,253));
            holder.joborderstatus.setTextColor(Color.rgb(13,71,161));
        }else if (jo.sts_desc.trim().equals("finished")){
            holder.img.setImageResource(R.mipmap.finished);
            holder.card_status.setCardBackgroundColor(Color.rgb(161, 247, 177));
            holder.joborderstatus.setTextColor(Color.rgb(24, 173, 52));

        }else if (jo.sts_desc.trim().equals("acknowledge")){
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
        return jobOrders.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredjobOrders = jobOrders;

                } else {
                    List filteredjoborder = new ArrayList<>();
                    for (JO row : jobOrders) {


                        //change this to filter according to your case
                        if (row.room.toLowerCase().contains(charString.toLowerCase())) {
                            filteredjoborder.add(row);
                        }
                    }

                    filteredjobOrders = filteredjoborder;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredjobOrders;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredjobOrders = (ArrayList) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }
    public class JobViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView joborderstatus,room,date_requested;
        String notes,hkjoborderid,jobsaddedby,bed,ns,emp_id,date_requested_info;
        CardView cardView,card_status;
        ImageView img;
        JobViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_pending_joborder);
            cardView=itemView.findViewById(R.id.card_joborder);
            date_requested=itemView.findViewById(R.id.txt_startime_joborder);
            jobsaddedby="";
            card_status=itemView.findViewById(R.id.card_status_joborder);
            joborderstatus=itemView.findViewById(R.id.txtstatus_joborder);
            room=itemView.findViewById(R.id.txtroom_joborder);
            bed="";
            ns="";
            notes="";
            hkjoborderid="";
            emp_id="";
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Sign_JobOrder.class);
                    intent.putExtra("status", joborderstatus.getText());
                    intent.putExtra("room", room.getText());
                    intent.putExtra("bed", bed);
                    intent.putExtra("notes", notes);
                    intent.putExtra("ns", jobsaddedby);
                    intent.putExtra("joborderid", hkjoborderid);
                    intent.putExtra("emp_id", emp_id);
                    intent.putExtra("date_requested_info", date_requested.getText());

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
    public JO getItem(int id) {
        return jobOrders.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
