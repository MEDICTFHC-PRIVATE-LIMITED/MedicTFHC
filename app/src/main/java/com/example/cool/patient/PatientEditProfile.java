package com.example.cool.patient;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.app.AlertDialog;
import android.support.constraint.solver.Cache;
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
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.camera.CameraManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import br.com.bloder.magic.view.MagicButton;

public class PatientEditProfile extends AppCompatActivity  {

    //animation
    ImageView Image;
    LinearLayout cardViewId;
    TextView fonts,p,a,d,o;
    LinearLayout profile,address,docs,others;
    Button profile_next,address_back,address_next,documents_back,documents_next,others_back;
    Animation downnup,Cardviewdowntoup,Textviewdowntoup;

    ApiBaseUrl baseUrl;
    Calendar mCurrentDate;
    int day,month,year;

    private ImageView imageView;
    private Button btnShare;
    private Button btnSave;
    private AlertDialog dialog;

    ProgressDialog progressDialog;


    Button  btn_aadhar,btn_profile;
    EditText editText;
    RadioGroup radioGroup,radioGroup1,radioGroup2;
    RadioButton genderradioButton,maritalradioButton,female,male,single,married;
    CheckBox promotion_medical,promotion_diagnostic,donor;
    EditText surname,name,email,mobile,salutation,address1,address2,pincode,dob,emergency_mobile,aadhar_num;
    MagicButton gen_btn;
    FloatingActionButton addImageFloatingButton;

    SearchableSpinner city,district,state,blood_group;
    List<String> bloodgroupList,districtsList;;

    List<String> statesList;
    String myQrArrayList;
    String[] mydistrictlist;

    ArrayAdapter<String> adapter1,adapter4;
    ArrayAdapter<String > adapter2,adapter3;
    List<String> citiesList;
    HashMap<Long, String> myCitiesList = new HashMap<Long, String>();
    HashMap<Long, String> myStatesList = new HashMap<Long, String>();
    List<String> myDistrictsList = new ArrayList<String>();
    HashMap<String, String> myBloodGroupList = new HashMap<String, String>();

    String smsUrl = null;
    ImageView aadharimage,decodeimg,qrImage,qrScanIcon;
    final Activity activity = this;
    String Surname,Name,Email,Mobile,Salutation,Address1,Address2,Gender,MaritalStatus,City,District,State,Pincode,Dob,Blood_group,Emergency_mobile,Aadhar_num,Age,MedicalPromotion,DiagnosticPromotion,BloodDonor;
    static String getUserId;
    static String uploadServerUrl = null;

    static String newName,mySurname,myName,myEmail,myMobile,mySalutation,myDistrict,myAddress1,myAddress2,myGender,myMaritalStatus,myPincode,myDob,myBlood_group,myEmergency_mobile,myAadhar_num,myAadharImage;
    static boolean myMedicalPromotion,myDiagnosticPromotion,myBloodDonor;
    static Long myCity,myState,myAge;
    Bitmap mIcon11;
    static String encodedImage;
    Uri selectedImageUri ;
    Bitmap selectedImageBitmap = null;
    final int REQUEST_CODE_GALLERY1 = 999,REQUEST_CODE_GALLERY2 = 1;
    String  mobile_number ;
    static String my_city,my_state,my_district,my_bloodgroup;
    String checkNewUser = null;


    //qr code get data fields
    static String qrName,qrGender,qrDob,qrFullAddress,qrAddress[],qrAddress1,qrAddress2,qrPincode;

    //spinner values
    public static final CharSequence[] BloodGroups  = {"Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-","O+","O-"};
    public static final CharSequence[] cities  = {"City", "Tirupati", "Kakinada","Tuni","Hyderabad",};
    public static final CharSequence[] districts  = {"District", "Chittoor", "East Godavari ","West Godavari "};
    public static final CharSequence[] states  = { "Andhra Pradesh", "Telangana"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

        //get user details based on id
        mobile_number = getIntent().getStringExtra("mobile");
        getUserId = getIntent().getStringExtra("id");
        System.out.print("userid in patientactivity....."+getUserId);


        baseUrl = new ApiBaseUrl();

        uploadServerUrl = baseUrl.getUrl()+"GetPatientByID";

        new GetAllCities().execute(baseUrl.getUrl()+"GetAllCity");

        new GetAllStates().execute(baseUrl.getUrl()+"GetAllState");

        new GetAllDistricts().execute(baseUrl.getUrl()+"GetAllDistrict");

        new GetAllBloodGroups().execute(baseUrl.getUrl()+"GetAllBloodGroup");

        new GetPatientDetails().execute(uploadServerUrl+"?ID="+getUserId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(PatientEditProfile.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PatientEditProfile.this,MainActivity.class);
                        intent.putExtra("id",getUserId);
                        intent.putExtra("mobile",mobile_number);
                        startActivity(intent);

                    }
                }

        );

        //qr code
//        surname = (EditText) findViewById(R.id.surname);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobileNumber);
//        radioGroup = (RadioGroup) findViewById(R.id.gendertype_radio);
//        editText = (EditText) findViewById(R.id.encoded);

        female = (RadioButton) findViewById(R.id.femaleRadio);
        male = (RadioButton) findViewById(R.id.maleRadio);
        single = (RadioButton) findViewById(R.id.single);
        married = (RadioButton) findViewById(R.id.married);

//        salutation = (EditText) findViewById(R.id.salutation);
//        radioGroup1 = (RadioGroup) findViewById(R.id.maritalstatus_radio);

        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        city = (SearchableSpinner) findViewById(R.id.city);
        district = (SearchableSpinner) findViewById(R.id.district);
        state = (SearchableSpinner) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pincode);
        dob = (EditText)findViewById(R.id.date_of_birth);
        blood_group = (SearchableSpinner) findViewById(R.id.bloodgroup);
        emergency_mobile = (EditText) findViewById(R.id.emergencymobileNumber);
        aadhar_num = (EditText) findViewById(R.id.aadhaarNumber);

        aadharimage = (ImageView) findViewById(R.id.aadharimage);
//        decodeimg = (ImageView) findViewById(R.id.decodeimage);

//        qrScanIcon = (ImageView) findViewById(R.id.qricon);
//        profile_image = (ImageView) findViewById(R.id.profileimage);

        addImageFloatingButton = (FloatingActionButton) findViewById(R.id.addImageIcon);
//        btn_profile = (Button) findViewById(R.id.btn_profileimage);
//        gen_btn = (Button) findViewById(R.id.gen_btn);

        qrImage = (ImageView) findViewById(R.id.image);


        promotion_medical = (CheckBox) findViewById(R.id.promotion_medicalstore);
        promotion_diagnostic = (CheckBox) findViewById(R.id.promotion_diagnostic);
        donor = (CheckBox) findViewById(R.id.blood_donor);

        mCurrentDate = Calendar.getInstance();
        day =mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month =mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month+1;

//        dob.setText(day+"/"+month+"/"+year);

//        qrScanIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                IntentIntegrator integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setPrompt("Scan");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(false);
//                integrator.initiateScan();
////                CameraManager a = new CameraManager();
//            }
//        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PatientEditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        dob.setText(dayOfMonth+"/"+monthOfYear+"/"+year);

                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });


//        myQrArrayList = "<QPDA n=\"Amudala Prabhakar Raju Udaya Sri\" u=\"xxxxxxxx1092\" g=\"F\" d=\"29-08-1994\" a=\"3-30/a,balabba vari thopu,Gudipala mandalam,Manchineella Gunta,Naragallu,Chittoor,Andhra Pradesh,517403\" i=\"AAAADGpQICANCocKAAAAFGZ0eXBqcDIgAAAAAGpwMiAAAAAtanAyaAAAABZpaGRyAAAAyAAAAKAAAwcHAAAAAAAPY29scgEAAAAAABAAAAGaanAyY/9P/1EALwAAAAAAoAAAAMgAAAAAAAAAAAAAAKAAAADIAAAAAAAAAAAAAwcBAQcBAQcBAf9SAAwAAAABAQUEBAAA/1wAI0JvGG7qbupuvGcAZwBm4l9MX0xfZEgDSANIRU/ST9JPYf9kACIAAUNyZWF0ZWQgYnk6IEpKMjAwMCB2ZXJzaW9uIDQuMf+QAAoAAAAAAQYAAf9SAAwAAAABAQUEBAAA/5PH0GwgOVnOqMalpgY77jW6GeRm+tdBcV2PoMLaYqPD5SBXnJRTSjysOaRzzQZk56Ftw+UgI1zOxDRWAOa+2Fe4hWFHQMfOTg+E2D4h4CjVdtlfn9ynW65YRsUgmuwcb/YdUyC1cWQ3wqVd3t46Fe9avN53Y/kM9qgYjW+bwEwAH5VtSlOcwEYAI+F6w+FaDqkHS098qXSv7+jjRFeevERQoKFuPgwHKx0qeva/HNHtNhG1eGjTR+wdxy0ZK1ixqRq5OZwAmflQgIDDqUOCAMVCtyIJeecoOR6eSBkDM99xP836gICAgICAgID/2Q==\" x=\"\" s=\"Yhv/nv0BwLVwjIImdEGu/lkEIGDyMMo5uRdUdFpnRCXKRAQeowsIMjP9+De/+eK1O48lzGg+8rhmNXpJT2aq3zKZA3Eh8MgZ2ehKSQ02SISXzKOC34joBSfm9du7hoNWt9ICQoZF/i0LxV3Zme/wvYyAxyhK65fdk0MuQHPUvqM3mxkRSzQ9Md9hp5rouFknPr6B4gqTPB71sWlmgsPfMLO6UnoO6TWEt3CznMW5FCdHbVq6IHs7i61wQ6/0dgxKQqx44qDuxQwGVGPWBlW/3ATQG8hiSMlk0ZiYIq3hfHkN/Nrsqm0OfdPYIfTsvYg0aWMHTC3BPeUFs4LTW+nYsw==\"/>";
//        String arr[] = myQrArrayList.split("=");
//
//        System.out.println("a[0]..."+arr[0]);
//        System.out.println("name..."+arr[1].replaceFirst(".$",""));
//        System.out.println("aadhar..."+arr[2].replaceFirst(".$",""));
//        System.out.println("gender..."+arr[3].replaceFirst(".$",""));
//        System.out.println("dob..."+arr[4].replaceFirst(".$",""));
//        System.out.println("address..."+arr[5].replaceFirst(".$",""));

//        init();
//        setupView();


        addImageFloatingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(
                                PatientEditProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY1
                        );

                    }
                });
//
//        btn_profile.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ActivityCompat.requestPermissions(
//                                PatientEditProfile.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                                REQUEST_CODE_GALLERY2
//                        );
//                    }
//                });

        gen_btn = (MagicButton) findViewById(R.id.gen_btn);
        gen_btn.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateEditProfile();

//                String json = formatDataAsJson();
//
//                System.out.println("json..."+json.toString());
//
//                new sendEditProfileDetails().execute(baseUrl.getUrl()+"UpdatePatient",json.toString());

            }

        });
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
            String json = formatDataAsJson();
            System.out.println("json..."+json.toString());

            new sendEditProfileDetails().execute(baseUrl.getUrl()+"UpdatePatient",json.toString());
        }
    }

    public void intialization()
    {

//        surname,name,email,mobile,salutation,address1,address2,pincode,dob,emergency_mobile,aadhar_num;

//        mySurname = surname.getText().toString().trim();
//        myName = name.getText().toString().trim();

        myMobile = mobile.getText().toString().trim();
        myEmail = email.getText().toString().trim();
        myAddress1 = address1.getText().toString();
        myAddress2 = address2.getText().toString();
        myPincode = pincode.getText().toString();
        myDob = dob.getText().toString();
        myEmergency_mobile = emergency_mobile.getText().toString();
        myAadhar_num = aadhar_num.getText().toString();

    }

    public boolean validate()
    {
        boolean validate = true;

//        surname,name,email,mobile,salutation,address1,address2,pincode,dob,emergency_mobile,aadhar_num;

        if(myMobile.isEmpty() || !Patterns.PHONE.matcher(myMobile).matches())
        {
            mobile.setError("please enter the mobile number");
            validate=false;
        }

        else if(myMobile.length()<10 || myMobile.length()>10)
        {
            mobile.setError(" Invalid phone number ");
            validate=false;
        }

        if(myEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(myEmail).matches())
        {
            email.setError("please enter valid email id");
            validate=false;
        }

        if(myDob.isEmpty())
        {
            dob.setError("please enter date of birth");
            validate=false;
        }

        if(myEmergency_mobile.isEmpty() || !Patterns.PHONE.matcher(myEmergency_mobile).matches())
        {
            emergency_mobile.setError("please enter emergency mobile number");
            validate=false;
        }

        if(myAadhar_num.isEmpty())
        {
            aadhar_num.setError("please enter aadhaar number");
            validate=false;
        }

        if(myAddress1.isEmpty())
        {
            address1.setError("please enter address1");
            validate=false;
        }

        if(myAddress2.isEmpty())
        {
            address2.setError("please enter address2");
            validate=false;
        }

        System.out.println("state key.."+getStateKeyFromValue(myStatesList,state.getSelectedItem().toString()));
        System.out.println("city key.."+getCityKeyFromValue(myCitiesList,city.getSelectedItem().toString()));
        System.out.println("district index.."+district.getSelectedItem().toString());
        System.out.println("blood index.."+blood_group.getSelectedItem().toString());

//        if (state.getSelectedItem().toString().trim().equals("Select State")) {
//            TextView errorText = (TextView)state.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText("please select state");
//            validate=false;
//        }
//
//        if (city.getSelectedItem().toString().trim().equals("Select City")) {
//            TextView errorText = (TextView)city.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText("please select city");
//            validate=false;
//        }
//
//        if (blood_group.getSelectedItem().toString().trim().equals("Select Blood Group")) {
//            TextView errorText = (TextView)blood_group.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText("please select bloodgroup");
//            validate=false;
//        }
//        if (district.getSelectedItem().toString().trim().equals("Select Blood Group")) {
//            TextView errorText = (TextView)district.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText("please select bloodgroup");
//             validate=false;
//        }

        return validate;
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


    private Bitmap convertImageViewToBitmap(ImageView v){

        Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();

        return bm;
    }


    public void showProfile(View v)
    {
        profile.setVisibility(View.VISIBLE);
        a.setVisibility(View.GONE);
        d.setVisibility(View.GONE);
        o.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        docs.setVisibility(View.GONE);
        qrImage.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        gen_btn.setVisibility(View.GONE);
    }

    public void showAddress(View v)
    {
        profile.setVisibility(View.GONE);
        address.setVisibility(View.VISIBLE);
        a.setVisibility(View.VISIBLE);
        d.setVisibility(View.VISIBLE);
        docs.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        gen_btn.setVisibility(View.GONE);
        qrImage.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    public void showDocuments(View v)
    {
        address.setVisibility(View.GONE);
        docs.setVisibility(View.VISIBLE);
        o.setVisibility(View.VISIBLE);
        d.setVisibility(View.VISIBLE);
        profile.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        gen_btn.setVisibility(View.GONE);
        qrImage.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    public void showOthers(View v)
    {
        docs.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        others.setVisibility(View.VISIBLE);
        gen_btn.setVisibility(View.VISIBLE);
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

                selectedImageUri = data.getData();
                BufferedWriter out=null;
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                    final InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


                }
                catch (IOException e)
                {
                    System.out.println("Exception ");

                }
                aadharimage.setImageBitmap(selectedImageBitmap);

                Log.d("hello","I'm in.");

            }
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static Bitmap viewToBitmap(View view,int width,int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



    public void startSave() {
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()){
            Toast.makeText(this,"can't create directory to save image",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name ="Img"+date+".jpg";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);
        try {
            fileOutputStream=new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(this,"save image success",Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);

    }
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return  new File(file,"QR Code");
    }

    public void startShare()
    {
        Bitmap bitmap =viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        File file = new File(Environment.getExternalStorageDirectory()+ File.separator+"QRCode.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch ( IOException e) {
            e.printStackTrace();
        }
        shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///sdcard/QRCode.jpg"));
        startActivity(Intent.createChooser(shareIntent,"Share Image"));
    }


    //Get patient list based on id from api call
    private class GetPatientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading..");

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

            Log.e("TAG result    ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            progressDialog.dismiss();
            getProfileDetails(result);

        }
    }

    private void getProfileDetails(String result) {

        try
        {
            JSONObject js = new JSONObject(result);

            if(js.has("UploadAadharImage"))
            {
//                checkNewUser = "No";

                System.out.println("checkNewUser in if old..."+checkNewUser);

                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");
                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myAddress1 = (String) js.get("Address1");
                myAddress2 = (String) js.get("Address2");
                myGender = (String) js.get("Gender");
                myDob = (String) js.get("DOB");
                myMaritalStatus = (String) js.get("MaritalStatus");
                myState = js.getLong("State");
                myCity = js.getLong("City");
                myPincode = (String) js.get("ZipCode");
                myBlood_group = (String) js.get("BloodGroup");
                myEmergency_mobile = (String) js.get("EmergencyContactNo");

                myMedicalPromotion = (boolean) js.get("ReminderMedicalShop");
                myDiagnosticPromotion = (boolean) js.get("ReminderDiagnosticC");
                myBloodDonor =  js.getBoolean("DonateBlood");

                myAadhar_num = (String) js.get("AadharNumber");
                myAadharImage= (String) js.get("UploadAadharImage");
                myDistrict =  js.getString("District");
                myAge = js.getLong("Age");

                if(myGender.equals("Male"))
                {
                    newName = "Mr.";
                }
                else if(myGender.equals("Female") && myMaritalStatus.equals("Single") )
                {
                    newName = "Ms.";
                }
                else if(myGender.equals("Female") && myMaritalStatus.equals("Married") )
                {
                    newName = "Mrs.";
                }

                name.setText(newName+" "+mySurname+" "+myName);
            }
            else
            {
//                checkNewUser = "Yes";

                myName = (String) js.get("FirstName");
                mySurname = (String) js.get("LastName");
                myMobile = (String) js.get("MobileNumber");
                myEmail = (String) js.get("EmailID");
                myAddress1 = (String) js.get("Address1");
                myAddress2 = (String) js.get("Address2");
                myGender = (String) js.get("Gender");
                myDob = (String) js.get("DOB");
                myMaritalStatus = (String) js.get("MaritalStatus");
                myState = js.getLong("State");
                myCity = js.getLong("City");
                myPincode = (String) js.get("ZipCode");
                myBlood_group = (String) js.get("BloodGroup");
                myEmergency_mobile = (String) js.get("EmergencyContactNo");

                myMedicalPromotion = (boolean) js.get("ReminderMedicalShop");
                myDiagnosticPromotion = (boolean) js.get("ReminderDiagnosticC");
                myBloodDonor = (boolean) js.get("DonateBlood");
                myAadhar_num = (String) js.get("AadharNumber");
//                myAadharImage= (String) js.get("UploadAadharImage");
                myDistrict =  js.getString("District");
                myAge = js.getLong("Age");
                name.setText(mySurname+" "+myName);
            }

            if(myGender.equals("Male"))
            {
                male.setChecked(true);
            }
            else if(myGender.equals("Female"))
            {
                female.setChecked(true);
            }

            else
            {
                male.setChecked(false);
                female.setChecked(false);
            }

            if(myMaritalStatus.equals("Single"))
            {
                single.setChecked(true);
            }

            else if(myMaritalStatus.equals("Married"))
            {
                married.setChecked(true);
            }
            else
            {
                single.setChecked(false);
                married.setChecked(false);
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

            aadhar_num.setText(myAadhar_num);
            dob.setText(myDob);
            pincode.setText(myPincode);
            emergency_mobile.setText(myEmergency_mobile);
            promotion_medical.setChecked(myMedicalPromotion);
            promotion_diagnostic.setChecked(myDiagnosticPromotion);
            donor.setChecked(myBloodDonor);

            name.setEnabled(false);
//            name.setTextColor(this.getResources().getColor(R.color.colorPrimary));
//            surname.setText(mySurname);
            mobile.setText(myMobile);
            email.setText(myEmail);
            address1.setText(myAddress1);
            address2.setText(myAddress2);
            System.out.println("image url.."+myAadharImage);

            new GetImageTask(aadharimage).execute(baseUrl.getImageUrl()+myAadharImage);


            if(myBlood_group.equals(""))
            {
                adapter1 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, bloodgroupList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                blood_group.setAdapter(adapter1); // Apply the adapter to the spinner
            }
            else {
                bloodgroupList.add(0, myBlood_group);
                System.out.println("state list.."+bloodgroupList);
                adapter1 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, bloodgroupList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                blood_group.setAdapter(adapter1); // Apply the adapter to the spinner
            }

            if(myState.equals(""))
            {
                adapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, statesList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                state.setAdapter(adapter2); // Apply the adapter to the spinner
            }
            else
            {
                System.out.println("state key.."+myState);
                long lng = myState;
                int i = (int) lng;
                String getStateName = String.valueOf(statesList.get(i));
                System.out.println("state name.."+getStateName);
                System.out.println("state list.."+myStatesList);
                System.out.println("state list.."+statesList);
                statesList.add(0,getStateName);
                adapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, statesList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                state.setAdapter(adapter2); // Apply the adapter to the spinner
            }

            if(myCity.equals(""))
            {
                adapter3 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                city.setAdapter(adapter3); // Apply the adapter to the spinner
            }
            else
            {
                System.out.println("mycity list.."+myCitiesList);
                System.out.println("city list.."+citiesList);
                System.out.println("city key.."+myCity);
                long lng = myCity;
                int i = (int) lng;
                String getCityName = String.valueOf(myCitiesList.get(myCity));
                System.out.println("get city name.."+getCityName);

                citiesList.add(0,getCityName);
                adapter3 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, citiesList);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                city.setAdapter(adapter3); // Apply the adapter to the spinner
            }

            if(myDistrict.equals(""))
            {
                adapter4 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, districtsList);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                district.setAdapter(adapter4); // Apply the adapter to the spinner
            }
            else
            {
                System.out.println("district key.."+myDistrict);
//                long lng = myDistrict;
//                int i = (int) lng;
//                String getDistrictName = String.valueOf(myDistrictsList.get(i));
//                System.out.println("district name.."+getDistrictName);
//                System.out.println("my district list.."+myDistrictsList);
                districtsList.add(0,myDistrict);
                System.out.println("district name.."+myDistrict);
                adapter4 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, districtsList);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
                district.setAdapter(adapter4); // Apply the adapter to the spinner
            }


//            districtsList.add(myCity,);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    //Get cities list from api call
    private class GetAllCities extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  cities ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getCities(result);

        }
    }

    private void getCities(String result) {
        try
        {

            JSONArray jsonArr = new JSONArray(result);
            citiesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long cityKey = jsonObj.getLong("Key");
                String cityValue = jsonObj.getString("Value");
                myCitiesList.put(cityKey,cityValue);
                citiesList.add(jsonObj.getString("Value"));
                System.out.print("mycity list.."+myCitiesList);
                System.out.print("city list.."+citiesList);
            }

        }
        catch (JSONException e)
        {}
    }

    //Get states list from api call
    private class GetAllStates extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  states  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getStates(result);
        }
    }

    private void getStates(String result) {
        try
        {
            JSONArray jsonArr = new JSONArray(result);
            statesList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                Long stateKey = jsonObj.getLong("Key");
                String stateValue = jsonObj.getString("Value");
                myStatesList.put(stateKey,stateValue);
                statesList.add(jsonObj.getString("Value"));
            }

        }
        catch (JSONException e)
        {}
    }

    //Get districts list from api call
    private class GetAllDistricts extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result  districts  ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            getDistricts(result);

        }
    }

    private void getDistricts(String result) {
        mydistrictlist = result.split(",");
        JSONArray myjson;
        try {
            myjson = new JSONArray(result);
            int len = myjson.length();
        districtsList = new ArrayList<String>();
        for (int i = 0; i < len; i++) {

            districtsList.add(myjson.getString(i));
            myDistrictsList.add(i,myjson.getString(i));
//            districtsList.add(0,myDistrict);
        }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Get bloodgroups list from api call
    private class GetAllBloodGroups extends AsyncTask<String, Void, String> {

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

            Log.e("TAG result bloodgroups ", result); // this is expecting a response code to be sent from your server upon receiving the POST data

            getBloodGroups(result);

        }
    }

    private void getBloodGroups(String result) {

        try
        {
            JSONArray jsonArr = new JSONArray(result);
            bloodgroupList = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {

                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);

                String bloodgroupKey = jsonObj.getString("Key");
                String bloodgroupValue = jsonObj.getString("Value");
                myBloodGroupList.put(bloodgroupKey,bloodgroupValue);
                bloodgroupList.add(jsonObj.getString("Value"));
            }

        }
        catch (JSONException e)
        {}
    }


    public static Object getCityKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static Object getStateKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
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

    private String formatDataAsJson()
    {

        JSONObject data = new JSONObject();

        myEmail = email.getText().toString().trim();
        myMobile = mobile.getText().toString().trim();
//                Salutation = salutation.getText().toString().trim();
        myAddress1 = address1.getText().toString().trim();
        myAddress2 = address2.getText().toString().trim();
//        myGender = genderradioButton.getText().toString();
        if(promotion_diagnostic.isChecked())
        {
            myDiagnosticPromotion = true;
//            myMedicalPromotion = false;
            System.out.println("diagnos if..."+myDiagnosticPromotion);
            System.out.println("medical if..."+myMedicalPromotion);

        }

        else if(promotion_diagnostic.isChecked() && promotion_medical.isChecked())
        {
            myDiagnosticPromotion = true;
            myMedicalPromotion = true;
            System.out.println("diagnos if..."+myDiagnosticPromotion);
            System.out.println("medical if..."+myMedicalPromotion);

        }

        else if(promotion_medical.isChecked())
        {
//            myDiagnosticPromotion = false;
            myMedicalPromotion = true;
            System.out.println("diagnos else..."+myDiagnosticPromotion);
            System.out.println("medical else..."+myMedicalPromotion);
        }
//        else
//        {
//            myDiagnosticPromotion = false;
//            myMedicalPromotion = false;
//            System.out.println("diagnos else..."+myDiagnosticPromotion);
//            System.out.println("medical else..."+myMedicalPromotion);
//        }
        if(donor.isChecked())
        {
            myBloodDonor = true;
            System.out.println("blood donor if..."+myBloodDonor);
        }
        else if(!donor.isChecked())
        {
            myBloodDonor = false;
            System.out.println("blood donor else..."+myBloodDonor);
        }

        if(female.isChecked()){
            myGender = "Female";
        }
        else if(male.isChecked())
        {
            myGender = "Male";
        }

        if(single.isChecked()){
            myMaritalStatus = "Single";
        }
        else if(married.isChecked())
        {
            myMaritalStatus = "Married";
        }

        System.out.println("marital..."+myMaritalStatus);
        City = city.getSelectedItem().toString();

//        getCityKeyFromValue(myCitiesList,City);

        System.out.println("City..."+getCityKeyFromValue(myCitiesList,City));
        myDistrict = district.getSelectedItem().toString();
        System.out.println("district..."+myDistrict);
        State = state.getSelectedItem().toString();
        System.out.println("state..."+getStateKeyFromValue(myStatesList,State));

//        getStateKeyFromValue(myStatesList,State);

        Pincode = pincode.getText().toString().trim();
        System.out.println("pincode..."+Pincode);
        Dob = dob.getText().toString().trim();

        System.out.println("get dob..."+Dob);

//        String a[]= Dob.split("/");
//        myDob = myDob = a[0]+"/"+a[1]+"/"+a[2];

        System.out.println("dob..."+myDob);
        Blood_group = blood_group.getSelectedItem().toString();
        System.out.println("bloodgroup..."+Blood_group);
        Emergency_mobile = emergency_mobile.getText().toString().trim();
        System.out.println("emergency contact..."+Emergency_mobile);
        Aadhar_num = aadhar_num.getText().toString().trim();
        System.out.println("aadhar num..."+Aadhar_num);

        System.out.println("encoded Image is ..."+encodedImage);

        if(encodedImage == null)
        {
            aadharimage.buildDrawingCache();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) aadharimage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos1);
            byte[] b1 = baos1.toByteArray();
            encodedImage = Base64.encodeToString(b1, Base64.DEFAULT);

            System.out.println("image view in if encoded Image..."+encodedImage);

        }

        try{

            if(checkNewUser.equals("Yes"))
            {
                System.out.println("checkNewUser in put new..."+checkNewUser);
                data.put("PatientID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
//                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("Address1",myAddress1);
                data.put("Address2",myAddress2);
                data.put("Gender",myGender);
                data.put("MaritalStatus",myMaritalStatus);
                data.put("State",getStateKeyFromValue(myStatesList,State));
                data.put("City",getCityKeyFromValue(myCitiesList,City));
                data.put("ZipCode",Pincode);
                data.put("BloodGroup",Blood_group);
                data.put("District",myDistrict);
                data.put("EmergencyContactNo",Emergency_mobile);
                data.put("DOB",Dob);
                data.put("AadharNumber",aadhar_num.getText().toString());
                data.put("ReminderMedicalShop", myMedicalPromotion);
                data.put("ReminderDiagnosticC", myDiagnosticPromotion);

                data.put("DonateBlood",myBloodDonor);

                data.put("UploadAadharImage",encodedImage);
                System.out.println("image data in if.."+data.toString());
                return data.toString();
            }
            else if(checkNewUser.equals("No"))
            {
                System.out.println("checkNewUser in put old..."+checkNewUser);
                data.put("PatientID",getUserId);
                data.put("FirstName",mySurname);
                data.put("LastName",myName);
//                data.put("MobileNumber",myMobile);
                data.put("EmailID",myEmail);
                data.put("Address1",myAddress1);
                data.put("Address2",myAddress2);
                data.put("Gender",myGender);
                data.put("MaritalStatus",myMaritalStatus);
                data.put("State",getStateKeyFromValue(myStatesList,State));
                data.put("City",getCityKeyFromValue(myCitiesList,City));
                data.put("ZipCode",Pincode);
                data.put("BloodGroup",Blood_group);
                data.put("District",myDistrict);
                data.put("EmergencyContactNo",Emergency_mobile);
                data.put("DOB",Dob);
//                data.put("AadharNumber",aadhar_num.getText().toString());
                data.put("ReminderMedicalShop", myMedicalPromotion);
                data.put("ReminderDiagnosticC", myDiagnosticPromotion);

                data.put("DonateBlood",myBloodDonor);

                data.put("UploadAadharImage",encodedImage);

                System.out.println("image data in else.."+data.toString());
                return data.toString();
            }

        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }
        return null;
    }

    private class sendEditProfileDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            progressDialog = new ProgressDialog(PatientEditProfile.this);
            // Set progressdialog title
//            progressDialog.setTitle("You are logging");
            // Set progressdialog message
            progressDialog.setMessage("Loading..");

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
                System.out.println("params..editprofile..."+params[1]);
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
                        Intent intent = new Intent(PatientEditProfile.this,MainActivity.class);
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

    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public GetImageTask(ImageView bmImage) {
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
            aadharimage.setImageBitmap(result);
        }

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
