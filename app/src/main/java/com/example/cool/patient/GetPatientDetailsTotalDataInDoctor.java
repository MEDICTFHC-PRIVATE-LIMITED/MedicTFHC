package com.example.cool.patient;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class GetPatientDetailsTotalDataInDoctor extends AppCompatActivity {

    TextView aadharnumber,mobilenumber,timeslot,patientname;
    Spinner spinner;
    ProgressDialog progressDialog;

    String myPatientname,aadhar,mobilenum,timeslt,str,url,status1;
    static String doctorMobile,doctorId,patientId,appointmentDate,AppointmentID,DoctorComment,Approved,Amount,Prescrition,Payment=null;
    int AppointmentID1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static String[] status = {"---Status---","Accept","Reschedule","Reject"};

    String encodedLicenceImage;
    final int REQUEST_CODE_GALLERY1 = 999;
    Uri selectedLicenceImageUri;
    Bitmap selectedLicenceImageBitmap = null;

    FloatingActionButton camaraicon,licenceicon;
    ImageView image;
    //MultiAutoCompleteTextView comment;
    CheckBox paytm,netbanking,cashonhand,creditcard;
    EditText amount,comment;
    Button button;

    ArrayAdapter<String> statusAdapter;

    ApiBaseUrl baseUrl;

    String smsUrl = null,uploadServerUrl;
    String Address1,cityname;

    String getDoctorID, addressID;
    String  getAddressID;

    String mydoctorname,mydoctormobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_patient_details_total_data_in_doctor);

        baseUrl = new ApiBaseUrl();

        camaraicon = (FloatingActionButton) findViewById(R.id.prescription_camaraIcon);
        licenceicon = (FloatingActionButton) findViewById(R.id.Licence_ImageIcon);
        image = (ImageView) findViewById(R.id.prescription);
        comment=(EditText) findViewById(R.id.Comments_Others);
        button=(Button) findViewById(R.id.button);
        paytm=(CheckBox)findViewById(R.id.pay_paytm);
        netbanking=(CheckBox)findViewById(R.id.net_banking);
        cashonhand=(CheckBox)findViewById(R.id.cash_on_hand);
        creditcard=(CheckBox)findViewById(R.id.debit_card);
        amount=(EditText)findViewById(R.id.amount);

        spinner = (Spinner) findViewById(R.id.status);

        aadharnumber=(TextView)findViewById(R.id.aadhaarNumber);
        mobilenumber=(TextView)findViewById(R.id.mobilenumber);
        timeslot=(TextView)findViewById(R.id.time_slot);
        patientname=(TextView)findViewById(R.id.Patient_name);

        myPatientname = getIntent().getStringExtra("patientname");
        aadhar = getIntent().getStringExtra("aadharnumber");
        mobilenum = getIntent().getStringExtra("mobilenumber");
        timeslt = getIntent().getStringExtra("timeslot");
        status1 = getIntent().getStringExtra("status");
        AppointmentID1 = getIntent().getIntExtra("AppointmentID",1);
        doctorId = getIntent().getStringExtra("doctorId");
        doctorMobile = getIntent().getStringExtra("doctorMobile");
        addressID=getIntent().getStringExtra("doctorAddressID");
        appointmentDate = getIntent().getStringExtra("appointmentDate");
        patientId = Integer.toString(getIntent().getIntExtra("patientID",1));


        System.out.println("mobile number"+mobilenum);

        uploadServerUrl = baseUrl.getUrl()+"DoctorGetAllAddress?ID="+doctorId;
        new GetDoctorAllAddressDetails().execute(uploadServerUrl);
        System.out.println("aadhar in patient total data..."+aadhar);

        System.out.println("Appintmentid..."+AppointmentID1);
        System.out.println("patientid totaldatadoc..."+patientId);
        System.out.println("doctorid..."+doctorId);
        AppointmentID=Integer.toString(AppointmentID1);
        //AppointmentID1=Integer.parseInt(AppointmentID);

        patientname.setText(myPatientname);
        mobilenumber.setText(mobilenum);
        aadharnumber.setText(aadhar);
        timeslot.setText(timeslt);

        new GetDoctorDetails().execute(baseUrl.getUrl()+"GetDoctorByID"+"?id="+doctorId);

        statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        spinner.setAdapter(statusAdapter); // Apply the adapter to the spinner


        DoctorComment = comment.getText().toString().trim();
        Amount = amount.getText().toString();

        licenceicon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                GetPatientDetailsTotalDataInDoctor.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment = "Pay with Paytm";

                netbanking.setEnabled(false);
                cashonhand.setEnabled(false);
                creditcard.setEnabled(false);
            }
        });

        netbanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Net Banking";
                cashonhand.setEnabled(false);
                creditcard.setEnabled(false);
                paytm.setEnabled(false);
            }
        });
        cashonhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Cash On Hand";
                creditcard.setEnabled(false);
                paytm.setEnabled(false);
                netbanking.setEnabled(false);
            }
        });


        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Payment="Credit/Debit Card";
                paytm.setEnabled(false);
                netbanking.setEnabled(false);
                cashonhand.setEnabled(false);

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("doctor comment..."+comment.getText().toString().trim());

                System.out.println("Amount..."+amount.getText().toString());

                System.out.println("status btn..."+spinner.getSelectedItem().toString());

                String json=formatDataAsJson();
                new SendDetails().execute(baseUrl.getUrl()+"DoctotUpdateAppointment",json.toString());
            }
        });

        camaraicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
                }
            }
        });

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
//        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetPatientDetailsTotalDataInDoctor.this,GetPatientDetailsListInDoctor.class);
                        intent.putExtra("doctorId",doctorId);
                        intent.putExtra("date",appointmentDate);
                        intent.putExtra("doctorMobile",doctorMobile);
                        startActivity(intent);

                    }
                }

        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY1);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_CODE_GALLERY1) {
////            onSelectFromGalleryResult(data);
////             Make sure the request was successful
//            Log.d("hello","I'm out.");
//            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//                selectedLicenceImageUri = data.getData();
//                BufferedWriter out=null;
//                try {
//                    selectedLicenceImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedLicenceImageUri);
//
//                    //licence base64
//                    final InputStream imageStream = getContentResolver().openInputStream(selectedLicenceImageUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
//                    byte[] b = baos.toByteArray();
//                    encodedLicenceImage = Base64.encodeToString(b, Base64.DEFAULT);
//                }
//                catch (IOException e)
//                {
//                    System.out.println("Exception ");
//
//                }
//                image.setImageBitmap(selectedLicenceImageBitmap);
//                Log.d("hello","I'm in.");
//
//            }
//        }
//
//        else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result docprofile", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try {
            JSONObject object = new JSONObject(result);

            mydoctorname = object.getString("FirstName");
            mydoctormobile = object.getString("MobileNumber");

            System.out.println("doc name...."+mydoctorname);
            System.out.println("doc mobile...."+mydoctormobile);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }



    //Get all addresses for doctor list from api call
    private class GetDoctorAllAddressDetails extends AsyncTask<String, Void, String> {

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
                System.out.println("u...."+params[0]);
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
            Log.e("Api response.....", result);
            try
            {
                JSONObject jsono = new JSONObject(result);
                String ss = (String) jsono.get("Message");
                if(ss.equals("No data found."))
                {
                    //showMessage();
                    Log.e("Api response if.....", result);
                }
                else
                {
                    getData(result);
                    Log.e("Api response else.....", result);
                }
            }
            catch (Exception e)
            {}
            getData(result);
//            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

    private void getData(String result) {
        try {

            JSONArray jarray = new JSONArray(result);

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                getDoctorID = object.getString("DoctorID");
                getAddressID = object.getString("AddressID");
                if(getAddressID.equals(addressID))
                {
                    Address1=object.getString("Address1");
                    cityname=object.getString("CityName");
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_CAMERA_REQUEST_CODE)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
        }
        else if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedLicenceImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedLicenceImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedLicenceImageUri);

                    //licence base64
                    final InputStream imageStream = getContentResolver().openInputStream(selectedLicenceImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedLicenceImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                image.setImageBitmap(selectedLicenceImageBitmap);
                Log.d("hello","I'm in.");

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private class SendDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setUseCaches(false);
                //connection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                Log.d("Service","Started");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                System.out.println("params....."+params[1]);
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                int statuscode = httpURLConnection.getResponseCode();

                System.out.println("status code....."+statuscode);

                InputStream in = null;
                if (statuscode == 200) {

                    in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }

                }
                else if(statuscode == 404){
                    in = httpURLConnection.getErrorStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                }

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 1017)
                {
                    showSuccessMessage(js.getString("Message"));
                }
                else
                {
                    showErrorMessage(js.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

        }
    }

    public void showSuccessMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        new MyTask().execute();
                        Intent intent = new Intent(GetPatientDetailsTotalDataInDoctor.this,DoctorDashboard.class);
                        intent.putExtra("id",doctorId);
                        startActivity(intent);
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Your Appointment");
        alert.show();
    }

    public void showErrorMessage(String message){

        android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = a_builder.create();
        alert.setTitle("Updating Your Appointment");
        alert.show();

    }


    private String formatDataAsJson()
    {
        JSONObject data = new JSONObject();

        if(paytm.isChecked())
        {
            Payment = "Pay with Paytm";
        }


        if(netbanking.isChecked())
        {
            Payment = "Net Banking";
        }

        if(cashonhand.isChecked())
        {
            Payment="Cash On Hand";
        }

        if(creditcard.isChecked())
        {
            Payment="Credit/Debit Card";
        }

        System.out.println("payment mode..."+Payment);

        if(encodedLicenceImage == null)
        {
            image.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedLicenceImage = Base64.encodeToString(b1, Base64.DEFAULT);
        }

//        "Accept","Reschedule","Reject"

        int statusId =0;
        if(spinner.getSelectedItem().toString().equals("Reject"))
        {
            statusId = 1;
        }
        if(spinner.getSelectedItem().toString().equals("Accept"))
        {
            statusId = 2;
        }
        if(spinner.getSelectedItem().toString().equals("Reschedule"))
        {
            statusId = 3;
        }

        try{

            data.put("AppointmentID", AppointmentID);
            data.put("DoctorComment",comment.getText().toString().trim());
            data.put("Approved",statusId);
            data.put("Payment",Payment);
            data.put("Amount",amount.getText().toString());
            data.put("Prescription",encodedLicenceImage);

            return data.toString();

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }

    private void showalert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalDataInDoctor.this);
        builder.setMessage("Appointment updated successfully.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showalert1() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetPatientDetailsTotalDataInDoctor.this);
        builder.setMessage("Appointment updated not successfull.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Doctor after reject the patient appointment:

    private class MyTask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                String username = myPatientname;
                String doctorname = mydoctorname;
                String doctormobilenum = mydoctormobile;
                String Status=spinner.getSelectedItem().toString();
                String address=Address1+","+cityname;

                String message= null;

                if(Status.equals("Reject"))
                {
                    message="Hi "+username+", Sorry for your in convince your appointment has been "+Status+" by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule. "+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }
                if(Status.equals("Reschedule"))
                {
                    message="Hi "+username+", Sorry for your in convince your appointment has been "+Status+" by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule. "+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }
                else if(Status.equals("Accept"))
                {
                    message="Hi "+username+", Your appointment has confirmed by Dr."+doctorname+" on "+appointmentDate+" at "+address+". Please call "+doctormobilenum+" for any assistance/re-schedule."+" Thank You."+" Click here to navigate:"+baseUrl.getLink();
                }

                System.out.println("message"+message);
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = mobilenum;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));

                stringBuilder.append("&msg="+encode_message);

                smsUrl=stringBuilder.toString();
                System.out.println("smsUrl.. "+smsUrl);
                myURL=new URL(smsUrl);

                myURLConnection=(HttpURLConnection) myURL.openConnection();
                myURLConnection.connect();
                reader=new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                String response;
                while ((response = reader.readLine()) != null) {
                    Log.d("RESPONSE", "" + response);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            return null;
        }

        @Override
        protected void onPreExecute() {
//            Toast.makeText(getApplicationContext(), "the message", Toast.LENGTH_LONG).show();
            super.onPreExecute();
        }
    }

}
