package com.example.cool.patient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetPatientDetailsList extends AppCompatActivity {

    String date,geturl;
    int userId;

    String DStatus,Status1,PatientName,EmailID,MobileNo,ReasonAppointments,Comments,TimeSlots,AadharNumber,Prescription;
    int Dstatus,PatientID,AppointmentID;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    LinearLayoutManager layoutManager;
    private MyPatientDataAdapter adapter;

    private List<MyPatientData> data_list;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_list);

        baseUrl = new ApiBaseUrl();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        data_list=new ArrayList<>();

        userId = getIntent().getIntExtra("userId",userId);
        date=getIntent().getStringExtra("date");

        System.out.println("doc id in getpatient list..."+userId);

        new GetPatientDetails().execute(baseUrl.getUrl()+"GetDoctorAppointToAccept"+"?id="+userId+"&AppointmentDate="+date);

        gridLayoutManager = new GridLayoutManager(this,2);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyPatientDataAdapter(this,data_list);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Patients List");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetPatientDetailsList.this,DoctorDashboard.class);
                        intent.putExtra("id",userId);
                        startActivity(intent);

                    }
                }

        );

    }

    //Get patient details  based on doctor id and appointment date
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service", "Started");
                httpURLConnection.setRequestMethod("GET");

//                httpURLConnection.setDoOutput(true);
                System.out.println("u...." + params[0]);
                System.out.println("dsfafssss....");
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getDetails(result);
            adapter.notifyDataSetChanged();

        }
    }

    private void getDetails(String result) {
        try {

            JSONArray jsonArray=new JSONArray(result);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject js = jsonArray.getJSONObject(i);

                Dstatus = js.getInt("DStatus");
                Status1 = (String) js.get("Status");
                AppointmentID=js.getInt("AppointmentID");
                PatientName=(String)js.get("PatientName");
                EmailID = (String) js.get("EmailID");
                MobileNo=(String) js.get("MobileNo");
                Prescription=(String) js.get("Prescription");
                PatientID= js.getInt("PatientID");
                Comments=(String) js.get("Comments");
                ReasonAppointments=(String) js.get("ReasonAppointments");
                AadharNumber=(String) js.get("Aadharnumber");
                TimeSlots=(String) js.get("TimeSlots");

                MyPatientData myPatientData = new MyPatientData(Dstatus,Status1,AppointmentID,PatientName,EmailID,MobileNo,Prescription,PatientID,Comments,ReasonAppointments,AadharNumber,TimeSlots);

                data_list.add(myPatientData);

//                System.out.println("DStatus"+Dstatus);
//                System.out.println("Status1"+Status1);
//                System.out.println("AppointmentID"+AppointmentID);
//                System.out.println("EmailID"+EmailID);
//                System.out.println("MobileNo"+MobileNo);
//                System.out.println("Prescription"+Prescription);
//                System.out.println("PatientID"+PatientID);
//                System.out.println("Comments"+Comments);
//                System.out.println("ReasonAppointments"+ReasonAppointments);
//                System.out.println("AadharNumber"+AadharNumber);
//                System.out.println("TimeSlots"+TimeSlots);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
