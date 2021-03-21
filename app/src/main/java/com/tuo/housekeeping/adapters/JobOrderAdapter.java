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

import com.tuo.housekeeping.Activity_Signature;
import com.tuo.housekeeping.R;
import com.tuo.housekeeping.model.JobOrders;

import java.util.ArrayList;
import java.util.List;

public class JobOrderAdapter extends RecyclerView.Adapter<JobOrderAdapter.JobViewHolder> implements Filterable {
    private Context mcTx;
    private List<JobOrders> jobOrders;
    private List<JobOrders> filteredjobOrders;
    private ItemClickListener mClickListener;
    public JobOrderAdapter(Context mcTx, List<JobOrders> jobOrders) {
        this.mcTx = mcTx;
        this.jobOrders = jobOrders;

    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcTx).inflate(R.layout.fragment_pending,parent,false);

        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobOrderAdapter.JobViewHolder holder,int position) {
        JobOrders jobOrder = jobOrders.get(position);
        holder.spot.setText(jobOrder.spot_name);
        holder.start_time.setText(jobOrder.start_time);
        holder.joborderstatus.setText(jobOrder.area_sts_desc);
        holder.time=jobOrder.getStart_time();
        holder.area_id=jobOrder.area_id;
        holder.area_name=jobOrder.area_name;
        holder.spot_id=jobOrder.spot_id;
        holder.routine_id=jobOrder.routine_id;
        holder.notes=jobOrder.notes;
        holder.dateid=jobOrder.date_id;
        holder.start_date=jobOrder.start_date;
        holder.routine_job_id=jobOrder.routine_job_id;
        holder.emp_id=jobOrder.emp_id;

        if(jobOrder.area_sts_desc.trim().equals("working")){
            holder.img.setImageResource(R.mipmap.working);
            holder.card_status.setCardBackgroundColor(Color.rgb(227,242,253));
            holder.joborderstatus.setTextColor(Color.rgb(13,71,161));
        }else if (jobOrder.area_sts_desc.trim().equals("finished")){
            holder.img.setImageResource(R.mipmap.finished);
            holder.card_status.setCardBackgroundColor(Color.rgb(161, 247, 177));
            holder.joborderstatus.setTextColor(Color.rgb(24, 173, 52));

        }else if (jobOrder.area_sts_desc.trim().equals("acknowledge")){
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
                    for (JobOrders row : jobOrders) {


                        //change this to filter according to your case
                        if (row.spot_name.toLowerCase().contains(charString.toLowerCase())) {
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
          TextView joborderstatus,spot,start_time;
           String spot_id,routine_id,area_id,area_name,notes,dateid,start_date,time,routine_job_id,emp_id;
           CardView cardView,card_status;
         ImageView img;
         JobViewHolder(View itemView) {
            super(itemView);
             joborderstatus=itemView.findViewById(R.id.txtstatus);
             spot=itemView.findViewById(R.id.txtroom);
             start_time=itemView.findViewById(R.id.txt_startime);
             img=itemView.findViewById(R.id.img_pending);
             cardView=itemView.findViewById(R.id.card_pending);
             card_status=itemView.findViewById(R.id.card_status);
             spot_id="";
             routine_id="";
             area_id="";
             dateid="";
             area_name="";
             time="";
             routine_job_id="";
             emp_id="";

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Signature.class);
                    intent.putExtra("status", joborderstatus.getText());
                    intent.putExtra("spot_name", spot.getText());
                    intent.putExtra("start_time", time);
                    intent.putExtra("spot_id", spot_id);
                    intent.putExtra("routine_id", routine_id);
                    intent.putExtra("dateid", dateid);
                    intent.putExtra("area_id", area_id);
                    intent.putExtra("area_name", area_name);
                    intent.putExtra("notes", notes);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("emp_id", emp_id);
                    intent.putExtra("routine_job_id", routine_job_id);

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
    public JobOrders getItem(int id) {
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
