package com.tuo.housekeeping.adapters;

import android.content.Context;
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

import com.tuo.housekeeping.R;
import com.tuo.housekeeping.model.RoomsModel;

import java.util.ArrayList;
import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder> implements Filterable {
    private Context mcTx;
    private List<RoomsModel> roomsModel;
    private List<RoomsModel> filteredroommodel;
    private ItemClickListener mClickListener;

    public RoomsAdapter(Context mcTx, List<RoomsModel> roomsModel) {
        this.mcTx = mcTx;
        this.roomsModel = roomsModel;
        this.filteredroommodel = filteredroommodel;
        this.mClickListener = mClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredroommodel = roomsModel;

                } else {
                    List filteredroommodel = new ArrayList<>();
                    for (RoomsModel row : roomsModel) {


                        //change this to filter according to your case
                        if (row.patno.toLowerCase().contains(charString.toLowerCase())) {
                            filteredroommodel.add(row);
                        }
                    }

                    filteredroommodel = filteredroommodel;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredroommodel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredroommodel = (ArrayList) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcTx).inflate(R.layout.fragment_rooms,parent,false);

        return new RoomsAdapter.RoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.RoomsViewHolder holder, int position) {
        RoomsModel roomsModels = roomsModel.get(position);
        holder.txt_roomcode.setText(roomsModels.roomcode+'|'+roomsModels.roombedno);
        holder.txt_roomdesc.setText(roomsModels.listroomstatus);
        holder.txt_patno.setText(roomsModels.patno);
        holder.txt_patientname.setText(roomsModels.patientname);

    }

    @Override
    public int getItemCount() {
        return roomsModel.size();
    }
    public class RoomsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_roomcode,txt_roomdesc,txt_bedno,txt_patno,txt_patientname;
        String notes,hkjoborderid,johkid;
        CardView cardView;
        ImageView img;
        RoomsViewHolder(View itemView) {
            super(itemView);
            txt_roomcode=itemView.findViewById(R.id.txt_roomcode);
            txt_roomdesc=itemView.findViewById(R.id.txt_roomdesc);
            txt_patno=itemView.findViewById(R.id.txt_patno);
            txt_patientname=itemView.findViewById(R.id.txt_patientname);
            cardView=itemView.findViewById(R.id.card_rooms);
//            notes="";
//            hkjoborderid="";
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), Activity_Signature.class);
//                    intent.putExtra("status", joborderstatus.getText());
//                    intent.putExtra("room", room.getText());
//                    intent.putExtra("bed", bed.getText());
//                    intent.putExtra("ns", jobsaddedby.getText());
//                    intent.putExtra("notes", notes);
//                    intent.putExtra("johkid", johkid);
//                    intent.putExtra("joborderid", hkjoborderid.trim());
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    v.getContext().startActivity(intent);
//
//                }
//            });

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public RoomsModel getItem(int id) {
        return roomsModel.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(RoomsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
