package game.my.bulletin.mygame;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;

import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {
    final Context context = this;
    TableLayout table_layout;
    Button resetButton;
    TextView highScoreTVVar;
    TextView levelTVVar;
    int bonusPoints;

    int checkCounter = 1;
    int unlockedLevel;


    int playingLevel=0;
    int highScore;
    int checkCountFin;
    CountDownTimer cTBlink;
    CountDownTimer cT;
    ProgressBar pb;
    float k=0;
    long secForProgress;
    SharedPreferences sharedPref ;
    Animation animSlideUp;
    Animation blink;
    boolean blinkon;

    View tbRow;
   long timeElapsed;
int randomNumStrt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);
        resetButton = (Button) findViewById(R.id.reset_btn_id);
        highScoreTVVar=(TextView)findViewById(R.id.highScoreTV) ;
        highScoreTVVar.setGravity(Gravity.CENTER);
        levelTVVar=(TextView)findViewById(R.id.levelTV) ;
        levelTVVar.setGravity(Gravity.CENTER);
         pb = (ProgressBar) findViewById(R.id.progressbar);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Drawable drawable =  ResourcesCompat.getDrawable(getResources(), R.drawable.progess_bar, null);

        pb.setProgressDrawable(drawable);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        resetButton.setOnClickListener(mResetListener);
        unlockedLevel = sharedPref.getInt(getString(R.string.unlocked_level), 1);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            playingLevel = (int) extras.get("levelChoice");
        }
        else {
            playingLevel=unlockedLevel;
        }


        highScoreTVVar.setText("SCORE:   "+String.valueOf(highScore));
        LevelEnums.LevelInfo levelVal= LevelEnums.LevelInfo.valueOf("level"+playingLevel);
            table_layout.removeAllViews();
            BuildTable(levelVal.row,levelVal.col,levelVal.seconds);
            table_layout.setActivated(true);


    }


    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    };


    public View.OnClickListener tableListener = new View.OnClickListener() {
        public void onClick(final View v) {

            if (table_layout.isActivated()) {
                final int clicked_id = v.getId();
                if (checkCounter != clicked_id) {
                        v.setBackgroundColor(Color.rgb(209, 79, 75));
                    new CountDownTimer(100, 50) {

                        @Override
                        public void onTick(long arg0) {
                            // TODO Auto-generated method stub

                        }

                        public void onFinish() {
                            if (clicked_id < checkCounter) {

                                    v.setBackgroundColor(Color.rgb(216, 204, 203));
                            } else
                                v.setBackgroundResource(0);

                        }
                    }.start();
                } else {
                    highScore=highScore+(checkCounter*25);
                    highScoreTVVar.setText("SCORE:   "+String.valueOf(highScore));
                        v.setBackgroundColor(Color.rgb(216, 204, 203));
                    checkCounter++;
                    if (randomNumStrt!=0&&(randomNumStrt+5)==v.getId()&&blinkon)
                    {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast,
                                (ViewGroup) findViewById(R.id.custom_toast_container));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText(" TIME BOOSTED X 2 ");

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                       // Toast.makeText(context, " TIME BOOSER X2 ", Toast.LENGTH_SHORT).show();
                        blinkon=false;
                        cTBlink.cancel();
                        cT.cancel();
                        v.clearAnimation();
                        countDownTimerProgress((timeElapsed*2));//timeElapsed*4
                        new CountDownTimer(2000, 200) {

                            Animation blinkPB;
                            @Override
                            public void onTick(long millisUntilFinished) {
                                blinkPB = AnimationUtils.loadAnimation(context, R.anim.blink);
                                pb.startAnimation(blinkPB);
                            }

                            public void onFinish() {
                                blinkPB.cancel();
                                pb.clearAnimation();
                            }
                        }.start();



                        v.setBackgroundColor(Color.rgb(216, 204, 203));


                    }

                    if (checkCounter == checkCountFin) {
                       // highScore= (int) (highScore+ bonusPoints);

                        highScoreTVVar.setText("SCORE:   "+String.valueOf(highScore));
                        cT.cancel();
                        playingLevel++;

                                SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.unlocked_level), playingLevel);
                        //editor.putInt(getString(R.string.high_score), hsLocal);
                        editor.commit();
                        show_dialog(false);
                        checkCounter = 1;

                    }
                    if(randomNumStrt==v.getId()){
if ((randomNumStrt+5)<checkCountFin){
                            tbRow = findViewById(randomNumStrt + 5);
                            blinkTimer(3500);
                        }
                    }

                }
            }

        }
    };

    public void show_dialog(boolean gameOver) {
final boolean go=gameOver;
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {                table_layout.removeAllViews();
                if(go) {
                    LevelEnums.LevelInfo levelVal = LevelEnums.LevelInfo.valueOf("level" + playingLevel);
                    BuildTable(levelVal.row, levelVal.col, levelVal.seconds);
                    table_layout.setActivated(true);

                }
                else
                {
                    LevelEnums.LevelInfo levelVal = LevelEnums.LevelInfo.valueOf("level" + (playingLevel-1));
                    levelTVVar.setText("Level "+String.valueOf(playingLevel-1));
                    BuildTable(levelVal.row, levelVal.col, levelVal.seconds);
                    table_layout.setActivated(true);
                }

                dialog.dismiss();
            }
        });
    final Button btnOkDialog = (Button) dialog.findViewById(R.id.button_nl);
        final TextView animTV=(TextView)dialog.findViewById(R.id.scoreanim);
        final TextView scoreTV=(TextView)dialog.findViewById(R.id.score);
        if(!go) {
            scoreTV.setText("TOTAL SCORE : "+String.valueOf(highScore));
            animTV.setText("Bonus Points +"+String.valueOf(bonusPoints));
            animTV.setVisibility(View.VISIBLE);
            animTV.startAnimation(animSlideUp);
            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues((int)highScore, (highScore+bonusPoints));
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    scoreTV.setText("TOTAL SCORE : "+ (int) animation.getAnimatedValue());
                }
            });
            animator.start();
    btnOkDialog.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {
            highScore=  (highScore+bonusPoints);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.high_score), highScore);
            editor.commit();
            table_layout.removeAllViews();
            LevelEnums.LevelInfo levelVal = LevelEnums.LevelInfo.valueOf("level" + playingLevel);

            BuildTable(levelVal.row, levelVal.col, levelVal.seconds);
            table_layout.setActivated(true);
            dialog.dismiss();
        }
    });
}else
        {
            animTV.setVisibility(View.GONE);
            scoreTV.setVisibility(View.GONE);
            btnOkDialog.setVisibility(View.GONE);
        }

        final Button btnRetry = (Button) dialog.findViewById(R.id.retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                table_layout.removeAllViews();
                if(go) {
                    LevelEnums.LevelInfo levelVal = LevelEnums.LevelInfo.valueOf("level" + playingLevel);
                    BuildTable(levelVal.row, levelVal.col, levelVal.seconds);
                    table_layout.setActivated(true);
                }
                else
                {
                    playingLevel=  playingLevel-1;
                    LevelEnums.LevelInfo levelVal = LevelEnums.LevelInfo.valueOf("level" + (playingLevel));
                    BuildTable(levelVal.row, levelVal.col, levelVal.seconds);
                    table_layout.setActivated(true);
                }

                dialog.dismiss();
            }
        });
        final TextView finalText=(TextView)dialog.findViewById(R.id.gameEnding);
        if(gameOver) {
            finalText.setText(R.string.game_over);

        }
        else

            finalText.setText(R.string.bravo);

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void BuildTable(int rows, int cols, int seconds) {
        if((rows*cols)>16){
            randomNumStrt=randInt(1,(int)(0.5*rows*cols));

        }
        levelTVVar.setText("Level "+String.valueOf(playingLevel));
        highScore=sharedPref.getInt(getString(R.string.high_score), 0);
        highScoreTVVar.setText("SCORE:   "+String.valueOf(highScore));
        checkCountFin=(rows*cols)+1;
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= (rows*cols); i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
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
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                tv.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));
                tv.setTextSize(30);
                tv.setOnClickListener(tableListener);
                row.addView(tv, cellLp);
                m++;
            }

            table_layout.addView(row, rowLp);


        }


        pb.setProgress(100);
        secForProgress=seconds*1000;
        countDownTimerProgress(secForProgress);

    }
    public  int randInt(int min, int max) {
if(max<checkCountFin) {
    Random rand = new Random();


    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
}
        return 0;
    }
    public void countDownTimerProgress(long sec){
        cT =  new CountDownTimer(sec, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                pb.setProgress((int) (((millisUntilFinished)*100)/secForProgress));
                bonusPoints= (int) (millisUntilFinished/10);
                timeElapsed=millisUntilFinished;
            }

            public void onFinish() {
                pb.setProgress(0);
                show_dialog(true);
                checkCounter = 1;
            }
        };
        cT.start();
    }
    public void blinkTimer(long sec){
        cTBlink =  new CountDownTimer(sec, 400) {
            @Override
            public void onTick(long millisUntilFinished) {
                blinkon=true;
                blink=AnimationUtils.loadAnimation(context, R.anim.blink);
                tbRow.startAnimation(blink);
                tbRow.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    tbRow.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.timeboost, null));
//
//                }
//                else
//                    tbRow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.timeboost, null));

            }

            public void onFinish() {
                blinkon=false;
                tbRow.clearAnimation();
                   tbRow.setBackgroundResource(0);
                    randomNumStrt=randInt(tbRow.getId(),tbRow.getId()+5);

            }
        };
        cTBlink.start();
    }


}
