package com.example.cool.patient;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.app.AlertDialog;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bloder.magic.view.MagicButton;

import static android.app.AlertDialog.*;

public class DoctorEditProfile extends AppCompatActivity {


    final Activity activity = this;

    ProgressDialog progressDialog;

    EditText salutation,email,Experience,mobileNumber,qualificationdoctor,registrationNumber,name,aadhar_num;
    SearchableSpinner Speciality;
    Bitmap mIcon11;
    ImageView uploadCertificate, adharimage, DoctorImage;
    List<String> Spaliaty;
    CheckBox cash_on_hand, debit_card, net_banking, pay_paym;
    MagicButton gen_btn;

    RadioGroup radioGroup;
    RadioButton radioButton;
    int radiobuttonid;
    RadioButton female,male;

    List<String>districtsList;
    List<String> myDistrictsList = new ArrayList<String>();
    ArrayAdapter<String> adapter1,adapter4;
    String Aadhar_num,Emergency_mobile;


    List<String> specialityList;
    HashMap<String, String> mySpecialitiesList = new HashMap<String, String>();

    ArrayAdapter<String > specialityAdapter;
//    static String mySpeciality,mySpecialityid;

    static String getUserId,mobile_number;
    static String uploadServerUrl = null;
    static String newName, mySurname, myName, myEmail, myMobile, mySalutation,mySpeciality,mySpecialityid,myQualification, myAddress1, myAddress2, myGender,myExperience,
            myregistrationNumber,myuploadCertificate,myadharimage,mydoctorImage,myAadhar_num;
    boolean myEmergencyService;
    static boolean myMedicalPromotion,myDiagnosticPromotion,myBloodDonor ,mycash_on_hand,mydebit_card ,mynet_banking,mypay_paym;
    FloatingActionButton addCertificateIcon,addAadharIcon,addProfileIcon;


    //qr code get data fields
    static String qrName,qrGender,qrDob,qrFullAddress,qrAddress[],qrAddress1,qrAddress2,qrPincode;
    String myQrArrayList;

    String encodedAadharImage,encodedCertificateImage,encodedProfileimage;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 44,REQUEST_CODE_GALLERY3 = 1;
    Uri selectedCertificateImageUri,selectedAadharImageUri,selectedProfileImageUri;
    Bitmap selectedCertificateImageBitmap = null,selectedAadharImageBitmap = null,selectedProfileImageBitmap = null;

    ApiBaseUrl baseUrl;

    String smsUrl = null;
    String checkNewUser = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        baseUrl = new ApiBaseUrl();
        uploadServerUrl = baseUrl.getUrl()+"GetDoctorByID";


        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("doctorid in profile....."+getUserId+"...mobile.."+mobile_number);

        new GetAllSpeciality().execute(baseUrl.getUrl()+"GetSpeciality");

        new GetDoctorDetails().execute(uploadServerUrl+"?id="+getUserId);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorEditProfile.this,DoctorDashboard.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",mobile_number);
                        startActivity(intent);

                    }
                }

        );

        salutation = (EditText) findViewById(R.id.salutation);
        email = (EditText) findViewById(R.id.email);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        Experience = (EditText) findViewById(R.id.Experience);
        qualificationdoctor = (EditText) findViewById(R.id.Qulificationdoctor);
        Speciality = (SearchableSpinner) findViewById(R.id.Speciality);
        registrationNumber = (EditText) findViewById(R.id.registrationNumber);


        uploadCertificate = (ImageView) findViewById(R.id.uploadCertificate);
        adharimage = (ImageView) findViewById(R.id.doc_aadhar_image);
        DoctorImage = (ImageView) findViewById(R.id.doc_profileimage);
        cash_on_hand = (CheckBox) findViewById(R.id.cash_on_hand);
        debit_card = (CheckBox) findViewById(R.id.debit_card);
        net_banking = (CheckBox) findViewById(R.id.net_banking);
        pay_paym = (CheckBox) findViewById(R.id.pay_paym);

        female = (RadioButton) findViewById(R.id.femaleRadio);
        male = (RadioButton) findViewById(R.id.maleRadio);

        // find the radioButton by returned id
        radioGroup = (RadioGroup) findViewById(R.id.gendertype_radio);

        radiobuttonid = radioGroup.getCheckedRadioButtonId();

        System.out.println("radio id...."+radiobuttonid);

        radioButton = (RadioButton) findViewById(radiobuttonid);

//        System.out.println("gender text..."+radioButton.getText());

        aadhar_num = (EditText) findViewById(R.id.aadhaarNumber);

        addCertificateIcon = (FloatingActionButton) findViewById(R.id.addCertificateIcon);
        addAadharIcon = (FloatingActionButton) findViewById(R.id.addAadharIcon);
        addProfileIcon = (FloatingActionButton) findViewById(R.id.addprofileIcon);

        gen_btn = (MagicButton) findViewById(R.id.gen_btn);
        gen_btn.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEditProfile();
            }
        });

        addCertificateIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DoctorEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });

        addAadharIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DoctorEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY2
                        );

                    }
                });

        addProfileIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                DoctorEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY3
                        );

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qricon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.qricon)
        {
//            qrScanIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {

            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
            return true;
//                CameraManager a = new CameraManager();
//                }
//            });
        }

        return super.onOptionsItemSelected(item);
    }


    //get doctor details based on id from api call
    private class GetDoctorDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("Your searching process is");
            // Set progressdialog message
            progressDialog.setMessage("Loading...");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }

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
            progressDialog.dismiss();
            getProfileDetails(result);
        }

    }

    private void getProfileDetails(String result) {
        try
        {
            JSONObject js = new JSONObject(result);

//            (String) js.get("DoctorID");
            if(js.has("DoctorImage") && js.has("Speciality")) {
//                checkNewUser = "No";
                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myregistrationNumber = (String) js.get("RegistationNumber");
                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");

                myQualification = (String) js.getString("Qualification");
                mySpecialityid = (String) js.getString("Speciality");

                System.out.println("Speciality id.." + mySpecialityid);

                mySpeciality = js.getString("SpecialityName");
                mydoctorImage = (String) js.get("DoctorImage");
                myExperience = (String) js.get("Experience");
                myGender = (String) js.get("Gender");

                myEmergencyService =  js.getBoolean("EmergencyService");

                myuploadCertificate = (String) js.get("CertificateImage");
                myAadhar_num = (String) js.get("AadharNumber");
                myadharimage = js.getString("AadharImage");

                mycash_on_hand = (boolean) js.get("CashOnHand");
                mydebit_card = (boolean) js.get("CreditDebit");
                mynet_banking = (boolean) js.get("Netbanking");
                mypay_paym = (boolean) js.get("Paytm");

                if(myGender.equals("Male"))
                {
                    newName = "Mr.";
                }
                else if(myGender.equals("Female"))
                {
                    newName = "Ms.";
                }
                else if(myGender.equals("Female"))
                {
                    newName = "Mrs.";
                }

                salutation.setText(newName+mySurname+" "+myName);
            }
            else {
//                checkNewUser = "Yes";
                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myregistrationNumber = (String) js.get("RegistationNumber");
                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");
                myQualification = (String) js.getString("Qualification");
                mySpeciality = js.getString("SpecialityName");
                mydoctorImage = (String) js.get("DoctorImage");
                myExperience = (String) js.get("Experience");
                myGender = (String) js.get("Gender");

                myEmergencyService = js.getBoolean("EmergencyService");

                myuploadCertificate =  (String) js.get("CertificateImage");
                myAadhar_num = (String) js.get("AadharNumber");
                myadharimage       =  (String) js.get("AadharImage");

                mycash_on_hand     =  (boolean) js.get("CashOnHand");
                mydebit_card       =  (boolean) js.get("CreditDebit");
                mynet_banking      =  (boolean) js.get("Netbanking");
                mypay_paym         =   (boolean) js.get("Paytm");

                if(myGender.equals(""))
                {
                    newName = "";
                }

                newName ="";
                salutation.setText(newName+mySurname+" "+myName);
            }



            System.out.println("hand.."+mycash_on_hand);
            System.out.println("card.."+mydebit_card);
            System.out.println("net.."+mynet_banking);
            System.out.println("paytm.."+mypay_paym);

            if(myGender.equals("Male"))
            {
                male.setChecked(true);
            }
            else if(myGender.equals("Female"))
            {
                female.setChecked(true);
            }
            else if(myGender.equals(""))
            {
                male.setChecked(false);
                female.setChecked(false);
            }

            if(myAadhar_num.equals(""))
            {
                checkNewUser = "Yes";
                System.out.println("checkNewUser in no aadhar num.."+checkNewUser);
            }

            else
            {
                checkNewUser = "No";
                System.out.println("checkNewUser in no aadhar num.."+checkNewUser);
            }

//            if(myGender.equals("Male"))
//            {
//                newName = "Mr.";
//            }
//            else if(myGender.equals("Female"))
//            {
//                newName = "Ms.";
//            }
//            else if(myGender.equals("Female"))
//            {
//                newName = "Mrs.";
//            }


//            salutation.setText(newName+mySurname+" "+myName);
//            salutation.setEnabled(false);

//            salutation.setTextColor(this.getResources().getColor(R.color.colorPrimary));
//            surname.setText(mySurname);

            mobileNumber.setText(myMobile);
            email.setText(myEmail);
            Experience.setText(myExperience);

            System.out.println("image a url.."+ myadharimage);
            System.out.println("image d url.."+ mydoctorImage);
            System.out.println("image c url.."+ myuploadCertificate);

            new GetAadharImageTask(adharimage).execute(baseUrl.getImageUrl()+myadharimage);

            new GetProfileImageTask(DoctorImage).execute(baseUrl.getImageUrl()+mydoctorImage);

            new GetCertificateImageTask(uploadCertificate).execute(baseUrl.getImageUrl()+myuploadCertificate);

            if(mySpeciality.equals(""))
            {
                specialityAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, specialityList);
                specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                Speciality.setAdapter(specialityAdapter); // Apply the adapter to the spinner
            }
            else
            {
                System.out.println("mySpeciality list.."+mySpecialitiesList);
                System.out.println("Speciality list.."+specialityList);
                System.out.println("Speciality key.."+mySpeciality);

//                int i = Integer.parseInt(mySpecialityid);
//                String getCityName = String.valueOf(mySpecialitiesList.get(i));
//                System.out.println("get city name.."+getCityName);

                specialityList.add(0,mySpeciality);
                specialityAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, specialityList);
                specialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                Speciality.setAdapter(specialityAdapter); // Apply the adapter to the spinner
            }



            aadhar_num.setText(myAadhar_num);
            qualificationdoctor.setText(myQualification);
            registrationNumber.setText(myregistrationNumber);
            cash_on_hand.setChecked(mycash_on_hand);
            debit_card.setChecked(mydebit_card);
            net_banking.setChecked(mynet_banking);
            pay_paym.setChecked(mypay_paym);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    private class GetAllSpeciality extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result specialities", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            getSpecialities(result);

        }
    }


    private void getSpecialities(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            specialityList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++) {
                System.out.print("myspeciality for loop in..");

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                String specialityKey = jsonObj.getString("SpecialityID");
                String specialityValue = jsonObj.getString("Speciality");
                mySpecialitiesList.put(specialityKey,specialityValue);
                specialityList.add(jsonObj.getString("Speciality"));
//                System.out.print("myspeciality list.."+mySpecialitiesList);
//                System.out.print("speciality list in get method.."+specialityList);
            }

        }
        catch (JSONException e)
        {}
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

        else if (requestCode == REQUEST_CODE_GALLERY2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY2);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        else if (requestCode == REQUEST_CODE_GALLERY3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY3);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();

                myQrArrayList = result.getContents();
                String arr[] = myQrArrayList.split("=");

                qrName = arr[1].replaceFirst(".$","");
                qrGender = arr[3].replaceFirst(".$","");
                qrDob = arr[4].replaceFirst(".$","");
                qrFullAddress = arr[5].replaceFirst(".$","");

                qrAddress = qrFullAddress.split(",");

                qrAddress1 = qrAddress[0]+","+qrAddress[1];
                qrAddress2 = qrAddress[2]+","+qrAddress[3];

                qrPincode = qrAddress[7];


//                System.out.println("a[0]..."+arr[0]);
                System.out.println("name..."+arr[1].replaceFirst(".$",""));
                System.out.println("aadhar..."+arr[2].replaceFirst(".$",""));
                System.out.println("gender..."+arr[3].replaceFirst(".$",""));
                System.out.println("dob..."+arr[4].replaceFirst(".$",""));
                System.out.println("address..."+arr[5].replaceFirst(".$",""));

                System.out.println("qr code data..."+result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == REQUEST_CODE_GALLERY1) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedCertificateImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedCertificateImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedCertificateImageUri);

                    //certificate base64
                    final InputStream imageStream = getContentResolver().openInputStream(selectedCertificateImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedCertificateImage = Base64.encodeToString(b, Base64.DEFAULT);
                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                uploadCertificate.setImageBitmap(selectedCertificateImageBitmap);
                Log.d("hello","I'm in.");

            }
        }


        else if (requestCode == REQUEST_CODE_GALLERY2) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedAadharImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedAadharImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedAadharImageUri);

                    //aadhar base64
                    final InputStream imageStream1 = getContentResolver().openInputStream(selectedAadharImageUri);
                    final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream1);

                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    selectedImage1.compress(Bitmap.CompressFormat.JPEG,100,baos1);
                    byte[] b1 = baos1.toByteArray();
                    encodedAadharImage = Base64.encodeToString(b1, Base64.DEFAULT);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                adharimage.setImageBitmap(selectedAadharImageBitmap);
                Log.d("hello","I'm in.");

            }
        }


        else if (requestCode == REQUEST_CODE_GALLERY3) {
//            onSelectFromGalleryResult(data);
//             Make sure the request was successful
            Log.d("hello","I'm out.");
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                selectedProfileImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedProfileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedProfileImageUri);

                    //profile base64
                    final InputStream imageStream3 = getContentResolver().openInputStream(selectedProfileImageUri);
                    final Bitmap selectedImage3 = BitmapFactory.decodeStream(imageStream3);
                    ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                    selectedImage3.compress(Bitmap.CompressFormat.JPEG,100,baos3);
                    byte[] b3 = baos3.toByteArray();
                    encodedProfileimage = Base64.encodeToString(b3, Base64.DEFAULT);

                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                DoctorImage.setImageBitmap(selectedProfileImageBitmap);
                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class sendEditProfileDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(DoctorEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading");

            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

//            HttpURLConnection connection=null;
            HttpURLConnection httpURLConnection = null;
            try {
                System.out.println("dsfafssss....");

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setUseCaches(false);
//                httpURLConnection.setChunkedStreamingMode(1024);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                httpURLConnection.setRequestProperty("Accept", "application/json");
                Log.d("Service","Started");
//                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                //write
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
                else if(statuscode == 500){
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
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
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
//
            Log.e("TAG result profile   ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            progressDialog.dismiss();

            JSONObject js;

            try {
                js= new JSONObject(result);
                int s = js.getInt("Code");
                if(s == 200)
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


        }
    }

    public void showSuccessMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        new Mytask().execute();
                        Intent intent = new Intent(DoctorEditProfile.this,DoctorDashboard.class);
                        intent.putExtra("id",getUserId);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();
    }

    public void showErrorMessage(String message){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();

    }

    private String SendPatientId() {

        JSONObject data = new JSONObject();

//        Salutation = salutation.getText().toString().trim();
//        BloodDonor = donor.getText().toString();

        try {
            data.put("ID", getUserId);

            return data.toString();

        } catch (Exception e) {
            Log.d("JSON", "Can't format JSON");
        }

        return null;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public void showMessage(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);

        a_builder.setMessage("Your details are successfully updated")
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                        Intent intent = new Intent(DoctorEditProfile.this,DoctorDashboard.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Edit Profile");
        alert.show();
    }

    private class GetAadharImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetAadharImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            adharimage.setImageBitmap(result);
        }

    }

    private class GetCertificateImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetCertificateImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            uploadCertificate.setImageBitmap(result);
        }

    }

    private class GetProfileImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetProfileImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            DoctorImage.setImageBitmap(result);
        }

    }


    public void validateEditProfile()
    {
        intialization();
        if(!validate())
        {
//            Toast.makeText(this,"Please enter above fields" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            String js = formatDataAsJson();
            new sendEditProfileDetails().execute(baseUrl.getUrl()+"DoctorEditprofile",js.toString());
        }
    }

    public void intialization()
    {

//        salutation,email,Experience,mobileNumber,qualificationdoctor,emergencymobileNumber,name,aadhar_num;

        mySalutation = salutation.getText().toString().trim();
        myEmail = email.getText().toString().trim();
        myExperience = Experience.getText().toString();
        myMobile = mobileNumber.getText().toString().trim();
        myQualification = qualificationdoctor.getText().toString();
        myregistrationNumber = registrationNumber.getText().toString();
        myAadhar_num = aadhar_num.getText().toString();

    }

    public boolean validate()
    {
        boolean validate = true;

        if(myMobile.isEmpty() || !Patterns.PHONE.matcher(myMobile).matches())
        {
            mobileNumber.setError("please enter the mobile number");
            validate=false;
        }

        else if(myMobile.length()<10 || myMobile.length()>10)
        {
            mobileNumber.setError(" Invalid phone number ");
            validate=false;
        }

        if(myEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(myEmail).matches())
        {
            email.setError("please enter valid email id");
            validate=false;
        }

        if(myExperience.isEmpty())
        {
            Experience.setError("please enter experience");
            validate=false;
        }

        if(myregistrationNumber.isEmpty() || !Patterns.PHONE.matcher(myregistrationNumber).matches())
        {
            registrationNumber.setError("please enter reg.no");
            validate=false;
        }

        if(myQualification.isEmpty() )
        {
            qualificationdoctor.setError("please enter qualification");
            validate=false;
        }

        if(myAadhar_num.isEmpty())
        {
            aadhar_num.setError("please enter aadhaar number");
            validate=false;
        }

        if(!cash_on_hand.isChecked() && !net_banking.isChecked() && !debit_card.isChecked() && !pay_paym.isChecked())
        {
//            Toast.makeText(getApplicationContext(),"Please select one option",Toast.LENGTH_SHORT).show();

            cash_on_hand.setError("please checked");
            net_banking.setError("plwase checked");
            debit_card.setError("please checked");
            pay_paym.setError("please checked");
            validate=false;
        }


        return validate;
    }

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        myEmail = email.getText().toString().trim();
        myMobile = mobileNumber.getText().toString().trim();
        mySalutation = salutation.getText().toString().trim();
        myQualification = qualificationdoctor.getText().toString();
        myExperience = Experience.getText().toString();
        Aadhar_num = aadhar_num.getText().toString().trim();
        mySpeciality = Speciality.getSelectedItem().toString();

        if(female.isChecked()){
            myGender = "Female";
        }
        else if(male.isChecked())
        {
            myGender = "Male";
        }
        System.out.println("aadhar num..."+Aadhar_num);

        myregistrationNumber = registrationNumber.getText().toString();

        if(cash_on_hand.isChecked())
        {
            mycash_on_hand = true;
            System.out.println("mycash_on_hand if..."+mycash_on_hand);
        }

        if(debit_card.isChecked())
        {
//            myDiagnosticPromotion = false;
            mydebit_card = true;
            System.out.println("mydebit_card if..."+ mydebit_card);

        }

        if(net_banking.isChecked())
        {
//            myDiagnosticPromotion = false;
            mynet_banking = true;
            System.out.println("mynet_banking if..."+ mynet_banking);
        }

        if(pay_paym.isChecked())
        {
//            myDiagnosticPromotion = false;
            mypay_paym = true;
            System.out.println("mypay_paym if..."+ mypay_paym);
        }

        if(encodedCertificateImage == null)
        {
            uploadCertificate.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) uploadCertificate.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedCertificateImage = Base64.encodeToString(b1, Base64.DEFAULT);
        }

        if(encodedAadharImage == null)
        {
            adharimage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) adharimage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedAadharImage = Base64.encodeToString(b1, Base64.DEFAULT);

//            System.out.println("image view encoded Image..."+encodedCertificateImage);
        }

        if(encodedProfileimage == null)
        {
            DoctorImage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) DoctorImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedProfileimage = Base64.encodeToString(b1, Base64.DEFAULT);

//            System.out.println("image view encoded Image..."+encodedCertificateImage);
        }

        try{

            if(checkNewUser.equals("Yes"))
            {
                data.put("DoctorID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
                data.put("Qualification",myQualification);

                System.out.println("spec key.."+getKeyFromValue(mySpecialitiesList,mySpeciality));

                data.put("Speciality",getKeyFromValue(mySpecialitiesList,mySpeciality));

                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("Experience",myExperience);
                data.put("AadharNumber",Aadhar_num);
                data.put("Gender",myGender);
                data.put("RegistationNumber",myregistrationNumber);
                data.put("CashOnHand", mycash_on_hand);
                data.put("Suffix", newName);
                data.put("Netbanking", mynet_banking);
                data.put("Paytm", mypay_paym);
                data.put("CreditDebit", mydebit_card);
                data.put("AadharImage",encodedAadharImage);
                data.put("CertificateImage",encodedCertificateImage);
                data.put("DoctorImage",encodedProfileimage);

                return data.toString();
            }
            else if(checkNewUser.equals("No"))
            {
                data.put("DoctorID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
                data.put("Qualification",myQualification);

                System.out.println("spec key.."+getKeyFromValue(mySpecialitiesList,mySpeciality));

                data.put("Speciality",getKeyFromValue(mySpecialitiesList,mySpeciality));

                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("Experience",myExperience);
                data.put("Gender",myGender);
                data.put("RegistationNumber",myregistrationNumber);
                data.put("CashOnHand", mycash_on_hand);
                data.put("Suffix", newName);
                data.put("Netbanking", mynet_banking);
                data.put("Paytm", mypay_paym);
                data.put("CreditDebit", mydebit_card);
                data.put("AadharImage",encodedAadharImage);
                data.put("CertificateImage",encodedCertificateImage);
                data.put("DoctorImage",encodedProfileimage);
                return data.toString();
            }


        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }


    /////Editprofile sms////
    private class Mytask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();
                //HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();


                String message="Your profile has been successfully updated for MEDIC TFHC.COM"+". Thank you."+"Click here to Login:"+baseUrl.getLink();
                smsUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String destination = myMobile;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(smsUrl);
                stringBuilder.append("uname="+ URLEncoder.encode(uname, "UTF-8"));
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
