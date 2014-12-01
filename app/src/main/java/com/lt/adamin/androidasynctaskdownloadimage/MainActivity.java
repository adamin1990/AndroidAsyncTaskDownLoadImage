package com.lt.adamin.androidasynctaskdownloadimage;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity  {
     EditText selectionText;
    ListView chooseImageList;
    String[] listOfImages;
    ProgressBar downloadImagesProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectionText= (EditText) findViewById(R.id.urlSelectionText);
        chooseImageList= (ListView) findViewById(R.id.chooseImageurl);
        listOfImages=getResources().getStringArray(R.array.imageurls);
        downloadImagesProgressBar= (ProgressBar) findViewById(R.id.downloadProgress);
         chooseImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 selectionText.setText(listOfImages[position]);
             }
         });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void downloadImage(View view){
        if(selectionText.getText().toString()!=null&&selectionText.getText().toString().length()>0){
         MyTask task=new MyTask();
            task.execute(selectionText.getText().toString());
        }

    }
    class MyTask extends AsyncTask<String,Integer,Boolean>{
           private int contentLength=-1;
           private int counter=0;
        private int caculatedProgress=0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadImagesProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean successful=false;
            URL downloadURL=null;
            HttpURLConnection connection=null;
            InputStream inputStream=null;
            FileOutputStream fileOutputStream=null;
            File file=null;
            try {
                downloadURL=new URL(params[0]);
                connection=(HttpURLConnection)downloadURL.openConnection();
                contentLength=connection.getContentLength(); //获取大小
                inputStream=connection.getInputStream();
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                +"/"+ Uri.parse(params[0]).getLastPathSegment());
                fileOutputStream=new FileOutputStream(file);
                Log.d("Absolutepath",file.getAbsolutePath());
                int read=-1;
                byte[] buffer=new byte[1024];
                while ((read=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,read);
                    counter=counter+read;  //进度条值
                    L.m(counter+"");
                    publishProgress(counter);  //推送进度
                }
                successful=true;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }
            return successful;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            caculatedProgress=(int)((double)values[0]/contentLength)*100;
            downloadImagesProgressBar.setProgress(caculatedProgress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            downloadImagesProgressBar.setVisibility(View.GONE);
        }
    }
}
