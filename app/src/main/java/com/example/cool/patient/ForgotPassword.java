package com.example.cool.patient;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import br.com.bloder.magic.view.MagicButton;

public class ForgotPassword extends Activity {

    ImageView Image;
    LinearLayout cardViewId;
    TextView fonts;
    MagicButton button;
    Animation downnup,Cardviewdowntoup,Textviewdowntoup;

    EditText mobile_num;
    TextView signin,forgot;
    private TextView mTextMessage;

    static String uploadServerUrl = null;
    String passwordd;
    String mainUrl=null,city;

    LocationManager locationManager;
    String lattitude,longitude;
    double lat,lng;
    Geocoder geocoder;
    List<Address> addresses;
    List<Address> fulladdress;
    private static final int REQUEST_LOCATION = 1;

    ApiBaseUrl baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        baseUrl = new ApiBaseUrl();

        Typeface mytapeface = Typeface.createFromAsset(getAssets(),"Rosewood.ttf");
        TextView mytextview = (TextView) findViewById(R.id.font);
        mytextview.setTypeface(mytapeface);
        fonts = (TextView)findViewById(R.id.font);
        Image = (ImageView)findViewById(R.id.image1);
        cardViewId=(LinearLayout)findViewById(R.id.layoutId);

        signin = (TextView) findViewById(R.id.login);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
            }
        });

        downnup = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        Textviewdowntoup=AnimationUtils.loadAnimation(this,R.anim.textdowntoup);

        Image.setAnimation(downnup);
        fonts.setAnimation(downnup);
        Cardviewdowntoup=AnimationUtils.loadAnimation(this,R.anim.cardviewdowntoup);
        cardViewId.setAnimation(Cardviewdowntoup);
        fonts.setAnimation(Textviewdowntoup);

        mobile_num = (EditText) findViewById(R.id.mobileNumber);

        button = (MagicButton) findViewById(R.id.btn_forgot);
        button.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPassword.this,"Button Clicked",Toast.LENGTH_SHORT).show();
                uploadServerUrl = baseUrl.getUrl()+"ForgetPassword";
                String js = formatDataAsJson();
                new GetPassword().execute(uploadServerUrl+"?MobileNumber="+mobile_num.getText().toString());

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }
//                Intent intent = new Intent(ForgotPassword.this, ForgotPassword.class);
//                startActivity(intent);

            }
        });
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void getLocation() {
        System.out.print("helo this is method");
        if (ActivityCompat.checkSelfPermission(ForgotPassword.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (ForgotPassword.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.print("helo this is if");

            ActivityCompat.requestPermissions(ForgotPassword.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            System.out.print("helo this is else");

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+longitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
////                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                System.out.print("latii...."+lattitude);
                System.out.print("longi...."+lattitude);

                geocoder=new Geocoder(getApplicationContext());

                try {
                    addresses = geocoder.getFromLocation(latti, longi,1);
                    System.out.println("addresses"+addresses);

                    if (addresses.isEmpty())
                    {
//                        cityname.setTitle("waiting");
//                        present_location.setText("Waiting");
                    }
                    else
                    {
                        if(addresses.size()>0)
                        {
                            city=addresses.get(0).getLocality();
//                            present_location.setText(city);
//                            cityname.setTitle(city);
                            System.out.println("city name"+city);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String formatDataAsJson() {
        org.json.simple.JSONObject data = new org.json.simple.JSONObject();
        try{

            data.put("MobileNumber",mobile_num.getText().toString().trim());
            return data.toString();
        }
        catch (Exception e)
        {
            Log.d("JSON","Can't format JSON");
        }

        return null;
    }


    //Get bloodbanks list from api call
    private class GetPassword extends AsyncTask<String, Void, String> {

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

            Log.e("Api response.....", result); // this is expecting a response code to be sent from your server upon receiving the POST data
            getData(result);

        }
    }


    private void getData(String result) {
        passwordd = result;

        new MyForgotPasswordtask().execute();

//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(mobile_num.getText().toString(), null, message, null, null);
//            Toast.makeText(getApplicationContext(), "SMS Sent!",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    "SMS failed, please try again later!",
//                    Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
    }

    private class MyForgotPasswordtask extends AsyncTask<Void, Void,Void>
    {

        URL myURL=null;
        HttpURLConnection myURLConnection=null;
        BufferedReader reader=null;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();
                //HttpURLConnection conn = (HttpURLConnection) new URL("https://www.mgage.solutions/SendSMS/sendmsg.php?uname=MedicTr&pass=X!g@c$R2&send=MEDICC&dest=8465887420&msg=Hi%20Gud%20Morning").openConnection();

//                String bloodbank_name="Apollo";
                String contact_person="Admin";
//                String city="Visakhapatnam";
//                String area="Ramnagar";
                String phone1=mobile_num.getText().toString();
                String link = baseUrl.getLink();


                String message="Your MEDICTFHC.COM password is: "+passwordd+". Thank you.Click here to Login: "+link;
                mainUrl = baseUrl.getSmsUrl();
                String uname="MedicTr";
                String password="X!g@c$R2";
                String sender="MEDICC";
                String mobilenumber = mobile_num.getText().toString();
                String destination=mobilenumber;

                String encode_message= URLEncoder.encode(message, "UTF-8");
                StringBuilder stringBuilder=new StringBuilder(mainUrl);
                stringBuilder.append("uname="+URLEncoder.encode(uname, "UTF-8"));
                stringBuilder.append("&pass="+URLEncoder.encode(password, "UTF-8"));
                stringBuilder.append("&send="+URLEncoder.encode(sender, "UTF-8"));
                stringBuilder.append("&dest="+URLEncoder.encode(destination, "UTF-8"));
                stringBuilder.append("&msg="+encode_message);

                mainUrl=stringBuilder.toString();
                System.out.println("mainurl "+mainUrl);
                myURL=new URL(mainUrl);

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
            showMessage();
            super.onPreExecute();
        }
    }

    public void showMessage(){

        AlertDialog.Builder a_builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        a_builder.setMessage("The Message has sent Successfully to your registered mobile number")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Successfully Sent");
        alert.show();

    }

}