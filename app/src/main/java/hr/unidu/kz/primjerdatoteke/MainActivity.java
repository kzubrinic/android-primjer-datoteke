package hr.unidu.kz.primjerdatoteke;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;

public class MainActivity extends Activity {

    EditText textmsg1, textmsg2;
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private final String nazivDatoteke = "datoteka.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textmsg1 = findViewById(R.id.editText1);
        textmsg2 = findViewById(R.id.editText2);
    }

    public void spremiLokalno(View v) {
        try {
            FileOutputStream fileout=openFileOutput(nazivDatoteke, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textmsg1.getText().toString());
            outputWriter.close();

            Toast.makeText(getBaseContext(), "Tekst je spremljen u datoteku!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void citajLokalno(View v) {
        try {
            FileInputStream fileIn = openFileInput(nazivDatoteke);
            InputStreamReader ir = new InputStreamReader(fileIn);
            BufferedReader dat = new BufferedReader(ir);
            StringBuilder sb = new StringBuilder();
            String red;
            while ((red = dat.readLine()) != null) {
                sb.append(red);
                sb.append("\n");
            }
            dat.close();
            ir.close();
            fileIn.close();
            textmsg1.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spremiVanjsko(View v){

        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!canWrite) {
            Toast.makeText(this, "Nemate dozvolu za pisanje u vanjsko spremište!", Toast.LENGTH_SHORT).show();
            return;
        }

        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + nazivDatoteke;

        try {
            File myFile = new File(path);
            FileOutputStream fileout=new FileOutputStream(myFile);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(textmsg2.getText().toString());
            outputWriter.close();
            Toast.makeText(getBaseContext(), "Tekst je spremljen u datoteku "+path,
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void citajVanjsko(View v) {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!canRead) {
            Toast.makeText(this, "Nemate dozvolu za čitanje iz vanjskog spremišta!", Toast.LENGTH_SHORT).show();
            return;
        }

        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + "/" + nazivDatoteke;
        Log.i("ExternalStorageDemo", "Save to: " + path);

        try {
            File myFile = new File(path);
            FileInputStream fileIn = new FileInputStream(myFile);
            InputStreamReader ir = new InputStreamReader(fileIn);
            BufferedReader dat = new BufferedReader(ir);
            StringBuilder sb = new StringBuilder();
            String red;
            while ((red = dat.readLine()) != null) {
                sb.append(red);
                sb.append("\n");
            }
            dat.close();
            ir.close();
            fileIn.close();
            textmsg2.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }
}

