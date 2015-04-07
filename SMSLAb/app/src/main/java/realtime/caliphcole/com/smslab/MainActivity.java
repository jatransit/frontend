package realtime.caliphcole.com.smslab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements View.OnClickListener{

    private EditText telnum;
    private EditText message;
    private Button send ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = new Button(this);
        message = new EditText(this);
        telnum = new EditText(this);
        send = (Button)findViewById(R.id.send);
        telnum = (EditText)findViewById(R.id.telnumtext);
        message = (EditText)findViewById(R.id.mesbox);

        send.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
     try {
         Intent sendIntent = new Intent(Intent.ACTION_VIEW);
         sendIntent.putExtra("sms_body", send.getText().toString());
         sendIntent.setType("vnd.android-dir/mms-sms");
         startActivity(sendIntent);

         String to = message.getText().toString().trim();
         sendSMS(to);
     }catch(Exception e){
         e.printStackTrace();
     }
    }



    private void sendSMS(String to) {

        SmsManager manager = SmsManager.getDefault() ;
        manager.sendTextMessage(to, null, telnum.getText().toString(), null, null);
    }
}