package com.tuo.housekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tuo.housekeeping.model.MedsData;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private Context mcTx;
    private List<MedsData> medslist;

    public MedicationAdapter(Context mcTx, List<MedsData> medslist) {
        this.mcTx = mcTx;
        this.medslist = medslist;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view=LayoutInflater.from(mcTx).inflate(null,parent,false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        MedsData medsData = medslist.get(position);
        holder.medpatno.setText(medsData.patno);
        holder.medstockdesc.setText(medsData.stockdesc);
        holder.medfreqdesc.setText(medsData.freqdesc);
        holder.meddatestarted.setText(medsData.datestarted);
        holder.meddoctor.setText(medsData.doctor);
    }

    @Override
    public int getItemCount() {
        return medslist.size();
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder{
        TextView medpatno,medstockdesc,medfreqdesc,meddatestarted,meddoctor;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
//            medpatno=itemView.findViewById(R.id.txt_medlistpatno);
//            medstockdesc=itemView.findViewById(R.id.txt_medliststockdesc);
//            medfreqdesc=itemView.findViewById(R.id.txt_medlistfreqdesc);
//            meddatestarted=itemView.findViewById(R.id.txt_medlistdatestarted);
//            meddoctor=itemView.findViewById(R.id.txt_medlistdoctor);
        }
    }

}
