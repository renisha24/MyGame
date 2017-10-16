package game.my.bulletin.mygame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class levels extends AppCompatActivity {
    TableLayout level_table_layout;
    final Context context = this;
    AlertDialog  alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        level_table_layout = (TableLayout) findViewById(R.id.levelTableLayout);
        level_table_layout.removeAllViews();
        BuildLevelsTable(5,3);
        level_table_layout.setActivated(true);

    }
    private void BuildLevelsTable(int rows, int cols) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      int  unlockedLevel = sharedPref.getInt(getString(R.string.unlocked_level), 1);
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) {
            list.add(new Integer(i));
        }
        // outer for loop
        int m = 0;
        for (int i = 1; i <= rows; i++) {

            TableRow row = new TableRow(this);

            for (int j = 1; j <= cols; j++) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv.setText(String.valueOf(list.get(m)));
                tv.setId(list.get(m));
               // tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(30);
                tv.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
                if(unlockedLevel<list.get(m)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.lock, null));

                    }
                    else
                        tv.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.lock, null));

                }
                else
                    tv.setOnClickListener(levelTableListener);
                row.addView(tv, cellLp);
                m++;
            }

            level_table_layout.addView(row, rowLp);


        }
    }
    public View.OnClickListener levelTableListener = new View.OnClickListener() {
        public void onClick(final View v) {
v.setBackgroundColor(Color.rgb(216, 204, 203));
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
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putExtra("levelChoice",v.getId());
                    startActivity(intent);
                    dialog.dismiss();
                }
            }.start();

            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    };

}
