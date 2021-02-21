package com.uer.healthcure;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class HeartRateDignosis extends Activity {
    ListView lst1;
    String heartrate[];
    String personname[];
    String oxylevel[];
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartratedignosis);
        lst1=(ListView)findViewById(R.id.list11);

        try{
            GetData gettrans=new GetData();
            DbParameter host=new DbParameter();
            String url=host.getHostpath();
            url=url+"HeartDignosis.php";
            // url=url+"pass="+URLEncoder.encode(upass)+"&";
            gettrans.execute(url);

        }catch(Exception e){
            //Toast.makeText(HeartRateDignosis.this, ""+e, Toast.LENGTH_LONG).show();
        }



    }




    private class GetData extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out="";
        int count=0;
        @Override
        protected String doInBackground(String... geturl) {


            try{
                //	String url= ;
                HttpClient http=new DefaultHttpClient();
                HttpPost http_get= new HttpPost(geturl[0]);
                HttpResponse response=http.execute(http_get);
                HttpEntity http_entity=response.getEntity();
                BufferedReader br= new BufferedReader(new InputStreamReader(http_entity.getContent()));
                out = br.readLine();

            }catch (Exception e){

                out= e.toString();
            }
            return out;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(HeartRateDignosis.this, null,"Searching Information...");

            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            try{
                Toast.makeText(HeartRateDignosis.this," "+out, Toast.LENGTH_LONG).show();
                //- Extracting the Dataset Symptoms through JSon Decoding

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength=jsonMainNode.length();
                heartrate=new String[arraylength];
                personname=new String[arraylength];
                oxylevel=new String[arraylength];


                for (int i = 0; i < jsonMainNode.length(); i++) {

                    // Problability getting
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    heartrate[i]=jsonChildNode.optString("HeartRate");
                    int heartrat11= (int)Float.parseFloat(heartrate[i]);
                    //personname[i] = jsonChildNode.optString("Name : "+personname[i]+" Heart Rate : "+heartrate[i]);
                    Toast.makeText(HeartRateDignosis.this, ""+heartrat11, Toast.LENGTH_SHORT).show();
                    if(heartrat11<60 || heartrat11>100 ) {
                        personname[i] = "Name : "+jsonChildNode.optString("Name")+" Heart Rate : "+heartrate[i];
                    }
                    //oxylevel[i]=jsonChildNode.optString("Oxygen");

                }


                LevelAdapter adapter=new LevelAdapter(HeartRateDignosis.this,  personname);
                lst1.setAdapter(adapter)	;

            }catch(Exception e){
                Toast.makeText(HeartRateDignosis.this," ", Toast.LENGTH_LONG).show();
            }

            progress.dismiss();
        }



    }
}
