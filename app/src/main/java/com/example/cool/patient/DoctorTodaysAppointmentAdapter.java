package com.example.cool.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Udayasri on 17-05-2018.
 */

public class DoctorTodaysAppointmentAdapter extends RecyclerView.Adapter<DoctorTodaysAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<PatientData_DoctorTodaysAppointmentsClass> my_data;
    String age,reason;

    public DoctorTodaysAppointmentAdapter(Context context, List<PatientData_DoctorTodaysAppointmentsClass> data_list) {
        this.context=context;
        this.my_data=data_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_doctor_todays_appointments,parent,false);
        return new DoctorTodaysAppointmentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.PatientName.setText(my_data.get(position).getPatientName());
        holder.mobileNumber.setText(my_data.get(position).getMobileNo());
        holder.Timeslot.setText(my_data.get(position).getTimeSlots());
        age=my_data.get(position).getAge().toString();
        reason=my_data.get(position).getReasonAppointments().toString();


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView PatientName,mobileNumber,Timeslot,Status;


        public ViewHolder(final View itemView) {
            super(itemView);
            PatientName = (TextView) itemView.findViewById(R.id.patientname);
            mobileNumber=(TextView)itemView.findViewById(R.id.phonenumber);
            Timeslot=(TextView)itemView.findViewById(R.id.time_slot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("this is adapter");
                    Intent intent=new Intent(itemView.getContext(),DoctorTodaysAppointment.class);
                    intent.putExtra("patientname",PatientName.getText().toString());
                    intent.putExtra("age",age);
                    intent.putExtra("reason",reason);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}