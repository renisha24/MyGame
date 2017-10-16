package game.my.bulletin.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

final Context context = this;
    AlertDialog  alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }

    public void startGame(View view) {
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
       dialog.setContentView(R.layout.open_dialog);
        dialog.setCancelable(false);
       final TextView openText=(TextView)dialog.findViewById(R.id.startup_text);

        openText.setText(R.string.startup);
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
            TextView timeText=(TextView)dialog.findViewById(R.id.startup_timer_text);
                timeText.setText(String.valueOf(millisUntilFinished/1000));
            }
            public void onFinish() {
                dialog.dismiss();
                Intent intent = new Intent(context, GameActivity.class);
                startActivity(intent);
            }
        }.start();

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    public void levels(View view) {


        Intent intent = new Intent(context, levels.class);
        startActivity(intent);

    }

    public void resetGame(View view) {

        AlertDialog.Builder  dialog = new AlertDialog.Builder(context,R.style.DialogTheme);
        dialog.setTitle(R.string.reset_text);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Continue",new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.unlocked_level), 1);
                        editor.putInt(getString(R.string.high_score), 0);
                        editor.commit();
                        Toast.makeText(context, " RESET COMPLETE ", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.setNegativeButton("Cancel",new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


}


