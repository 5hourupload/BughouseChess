package fhu.bughousechess;


import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import fhu.bughousechess.pieces.Bishop;
import fhu.bughousechess.pieces.Knight;
import fhu.bughousechess.pieces.Pawn;
import fhu.bughousechess.pieces.Piece;
import fhu.bughousechess.pieces.Queen;
import fhu.bughousechess.pieces.Rook;

public class GameActivity extends AppCompatActivity {

    static int milliseconds = 5 * 60 * 1000;

    static ImageView board[][][] = new ImageView[2][8][8];
    static ImageView roster1[] = new ImageView[30];
    static ImageView roster2[] = new ImageView[30];
    static ImageView roster3[] = new ImageView[30];
    static ImageView roster4[] = new ImageView[30];

    static int minute = 5;
    static int second = 0;

    private SharedPreferences prefs;

    InterstitialAd mInterstitialAd;

    static double[] cpuLevel = {0, 0, 0, 0};

    int[] cpuRoster = {0, 0, 0, 0};
    boolean[] inCheck = {false, false, false, false};
    String[] moveType = {"0", "0", "0", "0"};
    static int dialog_margin;

    public GameStateManager game;

    Button start;

    TextView timer1 = null;
    TextView timer2 = null;
    TextView timer3 = null;
    TextView timer4 = null;
    Button options = null;
    ScrollView scroll1 = null;
    ScrollView scroll3 = null;
    ScrollView scroll2 = null;
    ScrollView scroll4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        connectViews();



        MainActivity.currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (MainActivity.currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {
                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }



        game = new GameStateManager();

        setBoardBackground();
        setStartingPiecesUI();

        getPrefs();


        resizeRosterViews();

        setStartButton();

    }


    private void connectViews()
    {

        board[0][0][0] = findViewById(R.id.a1_1);
        board[0][0][1] = findViewById(R.id.a2_1);
        board[0][0][2] = findViewById(R.id.a3_1);
        board[0][0][3] = findViewById(R.id.a4_1);
        board[0][0][4] = findViewById(R.id.a5_1);
        board[0][0][5] = findViewById(R.id.a6_1);
        board[0][0][6] = findViewById(R.id.a7_1);
        board[0][0][7] = findViewById(R.id.a8_1);

        board[0][1][0] = findViewById(R.id.b1_1);
        board[0][1][1] = findViewById(R.id.b2_1);
        board[0][1][2] = findViewById(R.id.b3_1);
        board[0][1][3] = findViewById(R.id.b4_1);
        board[0][1][4] = findViewById(R.id.b5_1);
        board[0][1][5] = findViewById(R.id.b6_1);
        board[0][1][6] = findViewById(R.id.b7_1);
        board[0][1][7] = findViewById(R.id.b8_1);

        board[0][2][0] = findViewById(R.id.c1_1);
        board[0][2][1] = findViewById(R.id.c2_1);
        board[0][2][2] = findViewById(R.id.c3_1);
        board[0][2][3] = findViewById(R.id.c4_1);
        board[0][2][4] = findViewById(R.id.c5_1);
        board[0][2][5] = findViewById(R.id.c6_1);
        board[0][2][6] = findViewById(R.id.c7_1);
        board[0][2][7] = findViewById(R.id.c8_1);

        board[0][3][0] = findViewById(R.id.d1_1);
        board[0][3][1] = findViewById(R.id.d2_1);
        board[0][3][2] = findViewById(R.id.d3_1);
        board[0][3][3] = findViewById(R.id.d4_1);
        board[0][3][4] = findViewById(R.id.d5_1);
        board[0][3][5] = findViewById(R.id.d6_1);
        board[0][3][6] = findViewById(R.id.d7_1);
        board[0][3][7] = findViewById(R.id.d8_1);

        board[0][4][0] = findViewById(R.id.e1_1);
        board[0][4][1] = findViewById(R.id.e2_1);
        board[0][4][2] = findViewById(R.id.e3_1);
        board[0][4][3] = findViewById(R.id.e4_1);
        board[0][4][4] = findViewById(R.id.e5_1);
        board[0][4][5] = findViewById(R.id.e6_1);
        board[0][4][6] = findViewById(R.id.e7_1);
        board[0][4][7] = findViewById(R.id.e8_1);

        board[0][5][0] = findViewById(R.id.f1_1);
        board[0][5][1] = findViewById(R.id.f2_1);
        board[0][5][2] = findViewById(R.id.f3_1);
        board[0][5][3] = findViewById(R.id.f4_1);
        board[0][5][4] = findViewById(R.id.f5_1);
        board[0][5][5] = findViewById(R.id.f6_1);
        board[0][5][6] = findViewById(R.id.f7_1);
        board[0][5][7] = findViewById(R.id.f8_1);

        board[0][6][0] = findViewById(R.id.g1_1);
        board[0][6][1] = findViewById(R.id.g2_1);
        board[0][6][2] = findViewById(R.id.g3_1);
        board[0][6][3] = findViewById(R.id.g4_1);
        board[0][6][4] = findViewById(R.id.g5_1);
        board[0][6][5] = findViewById(R.id.g6_1);
        board[0][6][6] = findViewById(R.id.g7_1);
        board[0][6][7] = findViewById(R.id.g8_1);

        board[0][7][0] = findViewById(R.id.h1_1);
        board[0][7][1] = findViewById(R.id.h2_1);
        board[0][7][2] = findViewById(R.id.h3_1);
        board[0][7][3] = findViewById(R.id.h4_1);
        board[0][7][4] = findViewById(R.id.h5_1);
        board[0][7][5] = findViewById(R.id.h6_1);
        board[0][7][6] = findViewById(R.id.h7_1);
        board[0][7][7] = findViewById(R.id.h8_1);

        board[1][0][0] = findViewById(R.id.a1_2);
        board[1][0][1] = findViewById(R.id.a2_2);
        board[1][0][2] = findViewById(R.id.a3_2);
        board[1][0][3] = findViewById(R.id.a4_2);
        board[1][0][4] = findViewById(R.id.a5_2);
        board[1][0][5] = findViewById(R.id.a6_2);
        board[1][0][6] = findViewById(R.id.a7_2);
        board[1][0][7] = findViewById(R.id.a8_2);

        board[1][1][0] = findViewById(R.id.b1_2);
        board[1][1][1] = findViewById(R.id.b2_2);
        board[1][1][2] = findViewById(R.id.b3_2);
        board[1][1][3] = findViewById(R.id.b4_2);
        board[1][1][4] = findViewById(R.id.b5_2);
        board[1][1][5] = findViewById(R.id.b6_2);
        board[1][1][6] = findViewById(R.id.b7_2);
        board[1][1][7] = findViewById(R.id.b8_2);

        board[1][2][0] = findViewById(R.id.c1_2);
        board[1][2][1] = findViewById(R.id.c2_2);
        board[1][2][2] = findViewById(R.id.c3_2);
        board[1][2][3] = findViewById(R.id.c4_2);
        board[1][2][4] = findViewById(R.id.c5_2);
        board[1][2][5] = findViewById(R.id.c6_2);
        board[1][2][6] = findViewById(R.id.c7_2);
        board[1][2][7] = findViewById(R.id.c8_2);

        board[1][3][0] = findViewById(R.id.d1_2);
        board[1][3][1] = findViewById(R.id.d2_2);
        board[1][3][2] = findViewById(R.id.d3_2);
        board[1][3][3] = findViewById(R.id.d4_2);
        board[1][3][4] = findViewById(R.id.d5_2);
        board[1][3][5] = findViewById(R.id.d6_2);
        board[1][3][6] = findViewById(R.id.d7_2);
        board[1][3][7] = findViewById(R.id.d8_2);

        board[1][4][0] = findViewById(R.id.e1_2);
        board[1][4][1] = findViewById(R.id.e2_2);
        board[1][4][2] = findViewById(R.id.e3_2);
        board[1][4][3] = findViewById(R.id.e4_2);
        board[1][4][4] = findViewById(R.id.e5_2);
        board[1][4][5] = findViewById(R.id.e6_2);
        board[1][4][6] = findViewById(R.id.e7_2);
        board[1][4][7] = findViewById(R.id.e8_2);

        board[1][5][0] = findViewById(R.id.f1_2);
        board[1][5][1] = findViewById(R.id.f2_2);
        board[1][5][2] = findViewById(R.id.f3_2);
        board[1][5][3] = findViewById(R.id.f4_2);
        board[1][5][4] = findViewById(R.id.f5_2);
        board[1][5][5] = findViewById(R.id.f6_2);
        board[1][5][6] = findViewById(R.id.f7_2);
        board[1][5][7] = findViewById(R.id.f8_2);

        board[1][6][0] = findViewById(R.id.g1_2);
        board[1][6][1] = findViewById(R.id.g2_2);
        board[1][6][2] = findViewById(R.id.g3_2);
        board[1][6][3] = findViewById(R.id.g4_2);
        board[1][6][4] = findViewById(R.id.g5_2);
        board[1][6][5] = findViewById(R.id.g6_2);
        board[1][6][6] = findViewById(R.id.g7_2);
        board[1][6][7] = findViewById(R.id.g8_2);

        board[1][7][0] = findViewById(R.id.h1_2);
        board[1][7][1] = findViewById(R.id.h2_2);
        board[1][7][2] = findViewById(R.id.h3_2);
        board[1][7][3] = findViewById(R.id.h4_2);
        board[1][7][4] = findViewById(R.id.h5_2);
        board[1][7][5] = findViewById(R.id.h6_2);
        board[1][7][6] = findViewById(R.id.h7_2);
        board[1][7][7] = findViewById(R.id.h8_2);


        roster1[0] = findViewById(R.id.roster1_1);
        roster1[1] = findViewById(R.id.roster2_1);
        roster1[2] = findViewById(R.id.roster3_1);
        roster1[3] = findViewById(R.id.roster4_1);
        roster1[4] = findViewById(R.id.roster5_1);
        roster1[5] = findViewById(R.id.roster6_1);
        roster1[6] = findViewById(R.id.roster7_1);
        roster1[7] = findViewById(R.id.roster8_1);
        roster1[8] = findViewById(R.id.roster9_1);
        roster1[9] = findViewById(R.id.roster10_1);
        roster1[10] = findViewById(R.id.roster11_1);
        roster1[11] = findViewById(R.id.roster12_1);
        roster1[12] = findViewById(R.id.roster13_1);
        roster1[13] = findViewById(R.id.roster14_1);
        roster1[14] = findViewById(R.id.roster15_1);
        roster1[15] = findViewById(R.id.roster16_1);
        roster1[16] = findViewById(R.id.roster17_1);
        roster1[17] = findViewById(R.id.roster18_1);
        roster1[18] = findViewById(R.id.roster19_1);
        roster1[19] = findViewById(R.id.roster20_1);
        roster1[20] = findViewById(R.id.roster21_1);
        roster1[21] = findViewById(R.id.roster22_1);
        roster1[22] = findViewById(R.id.roster23_1);
        roster1[23] = findViewById(R.id.roster24_1);
        roster1[24] = findViewById(R.id.roster25_1);
        roster1[25] = findViewById(R.id.roster26_1);
        roster1[26] = findViewById(R.id.roster27_1);
        roster1[27] = findViewById(R.id.roster28_1);
        roster1[28] = findViewById(R.id.roster29_1);
        roster1[29] = findViewById(R.id.roster30_1);

        roster2[29] = findViewById(R.id.roster1_2);
        roster2[28] = findViewById(R.id.roster2_2);
        roster2[27] = findViewById(R.id.roster3_2);
        roster2[26] = findViewById(R.id.roster4_2);
        roster2[25] = findViewById(R.id.roster5_2);
        roster2[24] = findViewById(R.id.roster6_2);
        roster2[23] = findViewById(R.id.roster7_2);
        roster2[22] = findViewById(R.id.roster8_2);
        roster2[21] = findViewById(R.id.roster9_2);
        roster2[20] = findViewById(R.id.roster10_2);
        roster2[19] = findViewById(R.id.roster11_2);
        roster2[18] = findViewById(R.id.roster12_2);
        roster2[17] = findViewById(R.id.roster13_2);
        roster2[16] = findViewById(R.id.roster14_2);
        roster2[15] = findViewById(R.id.roster15_2);
        roster2[14] = findViewById(R.id.roster16_2);
        roster2[13] = findViewById(R.id.roster17_2);
        roster2[12] = findViewById(R.id.roster18_2);
        roster2[11] = findViewById(R.id.roster19_2);
        roster2[10] = findViewById(R.id.roster20_2);
        roster2[9] = findViewById(R.id.roster21_2);
        roster2[8] = findViewById(R.id.roster22_2);
        roster2[7] = findViewById(R.id.roster23_2);
        roster2[6] = findViewById(R.id.roster24_2);
        roster2[5] = findViewById(R.id.roster25_2);
        roster2[4] = findViewById(R.id.roster26_2);
        roster2[3] = findViewById(R.id.roster27_2);
        roster2[2] = findViewById(R.id.roster28_2);
        roster2[1] = findViewById(R.id.roster29_2);
        roster2[0] = findViewById(R.id.roster30_2);

        roster3[0] = findViewById(R.id.roster1_3);
        roster3[1] = findViewById(R.id.roster2_3);
        roster3[2] = findViewById(R.id.roster3_3);
        roster3[3] = findViewById(R.id.roster4_3);
        roster3[4] = findViewById(R.id.roster5_3);
        roster3[5] = findViewById(R.id.roster6_3);
        roster3[6] = findViewById(R.id.roster7_3);
        roster3[7] = findViewById(R.id.roster8_3);
        roster3[8] = findViewById(R.id.roster9_3);
        roster3[9] = findViewById(R.id.roster10_3);
        roster3[10] = findViewById(R.id.roster11_3);
        roster3[11] = findViewById(R.id.roster12_3);
        roster3[12] = findViewById(R.id.roster13_3);
        roster3[13] = findViewById(R.id.roster14_3);
        roster3[14] = findViewById(R.id.roster15_3);
        roster3[15] = findViewById(R.id.roster16_3);
        roster3[16] = findViewById(R.id.roster17_3);
        roster3[17] = findViewById(R.id.roster18_3);
        roster3[18] = findViewById(R.id.roster19_3);
        roster3[19] = findViewById(R.id.roster20_3);
        roster3[20] = findViewById(R.id.roster21_3);
        roster3[21] = findViewById(R.id.roster22_3);
        roster3[22] = findViewById(R.id.roster23_3);
        roster3[23] = findViewById(R.id.roster24_3);
        roster3[24] = findViewById(R.id.roster25_3);
        roster3[25] = findViewById(R.id.roster26_3);
        roster3[26] = findViewById(R.id.roster27_3);
        roster3[27] = findViewById(R.id.roster28_3);
        roster3[28] = findViewById(R.id.roster29_3);
        roster3[29] = findViewById(R.id.roster30_3);

        roster4[29] = findViewById(R.id.roster1_4);
        roster4[28] = findViewById(R.id.roster2_4);
        roster4[27] = findViewById(R.id.roster3_4);
        roster4[26] = findViewById(R.id.roster4_4);
        roster4[25] = findViewById(R.id.roster5_4);
        roster4[24] = findViewById(R.id.roster6_4);
        roster4[23] = findViewById(R.id.roster7_4);
        roster4[22] = findViewById(R.id.roster8_4);
        roster4[21] = findViewById(R.id.roster9_4);
        roster4[20] = findViewById(R.id.roster10_4);
        roster4[19] = findViewById(R.id.roster11_4);
        roster4[18] = findViewById(R.id.roster12_4);
        roster4[17] = findViewById(R.id.roster13_4);
        roster4[16] = findViewById(R.id.roster14_4);
        roster4[15] = findViewById(R.id.roster15_4);
        roster4[14] = findViewById(R.id.roster16_4);
        roster4[13] = findViewById(R.id.roster17_4);
        roster4[12] = findViewById(R.id.roster18_4);
        roster4[11] = findViewById(R.id.roster19_4);
        roster4[10] = findViewById(R.id.roster20_4);
        roster4[9] = findViewById(R.id.roster21_4);
        roster4[8] = findViewById(R.id.roster22_4);
        roster4[7] = findViewById(R.id.roster23_4);
        roster4[6] = findViewById(R.id.roster24_4);
        roster4[5] = findViewById(R.id.roster25_4);
        roster4[4] = findViewById(R.id.roster26_4);
        roster4[3] = findViewById(R.id.roster27_4);
        roster4[2] = findViewById(R.id.roster28_4);
        roster4[1] = findViewById(R.id.roster29_4);
        roster4[0] = findViewById(R.id.roster30_4);


    }

    private void resizeRosterViews()
    {
        roster1[1].post(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 30; i++)
                {
                    roster1[i].getLayoutParams().height = board[0][1][1].getHeight();
                    roster1[i].getLayoutParams().width = board[0][1][1].getHeight();
                    roster1[i].requestLayout();
                    roster2[i].getLayoutParams().height = board[0][1][1].getHeight();
                    roster2[i].getLayoutParams().width = board[0][1][1].getHeight();
                    roster2[i].requestLayout();
                    roster3[i].getLayoutParams().height = board[0][1][1].getHeight();
                    roster3[i].getLayoutParams().width = board[0][1][1].getHeight();
                    roster3[i].requestLayout();
                    roster4[i].getLayoutParams().height = board[0][1][1].getHeight();
                    roster4[i].getLayoutParams().width = board[0][1][1].getHeight();
                    roster4[i].requestLayout();
                }
            }
        });

        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);
        timer3 = findViewById(R.id.timer3);
        timer4 = findViewById(R.id.timer4);
        options = findViewById(R.id.options);
        scroll1 = findViewById(R.id.scroll1);
        scroll1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        scroll3 = findViewById(R.id.scroll3);
        scroll3.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

        scroll2 = findViewById(R.id.scroll2);
        scroll4 = findViewById(R.id.scroll4);

        start = findViewById(R.id.start);


        board[0][1][1].post(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        board[0][i][j].getLayoutParams().width = board[0][i][j].getHeight();
                        board[1][i][j].getLayoutParams().width = board[1][i][j].getHeight();
                        board[0][i][j].requestLayout();
                        board[1][i][j].requestLayout();
                    }
                }
                timer1.getLayoutParams().width = timer1.getHeight() * 2;
                timer2.getLayoutParams().width = timer2.getHeight() * 2;
                timer3.getLayoutParams().width = timer3.getHeight() * 2;
                timer4.getLayoutParams().width = timer4.getHeight() * 2;
                timer1.requestLayout();
                timer2.requestLayout();
                timer3.requestLayout();
                timer4.requestLayout();
                start.getLayoutParams().width = timer1.getHeight() * 2;
                start.requestLayout();
                options.getLayoutParams().width = timer1.getHeight() * 2;
                options.requestLayout();
                scroll2.fullScroll(View.FOCUS_DOWN);
                scroll2.requestLayout();
                scroll4.fullScroll(View.FOCUS_DOWN);
                scroll4.requestLayout();
            }
        });

        timer3.setRotation(180);
        timer4.setRotation(180);
        timer1.setTextColor(0xFF848484);
        timer2.setTextColor(0xFF848484);
        timer3.setTextColor(0xFF848484);
        timer4.setTextColor(0xFF848484);



        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (game.gameState == 1)
                {
                    Button start = findViewById(R.id.start);
                    game.clean();
                    clean(board[0],0);
                    clean(board[1],1);
                    game.turnSave1 = game.whiteTurn1;
                    game.whiteTurn1 = 3;
                    game.turnSave2 = game.whiteTurn2;
                    game.whiteTurn2 = 3;
                    nukeListeners(board[0], 0);
                    nukeListeners(board[1], 1);
                    game.gameState = 2;
                    start.setText("Resume");
                }
                startSettings();
            }
        });
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        if (game.gameState == 0)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    timer1.setText(time);
                    timer2.setText(time);
                    timer3.setText(time);
                    timer4.setText(time);
                }
            });
        }
    }

    private void getPrefs()
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getString("player1", "0").equals("0"))
        {
            game.position1 = true;
        }
        else
        {
            GameActivity.cpuLevel[0] = Integer.parseInt(prefs.getString("player1", "0")) - 1;
            game.position1 = false;
        }
        if (prefs.getString("player2", "0").equals("0"))
        {
            game.position2 = true;
        }
        else
        {
            GameActivity.cpuLevel[1] = Integer.parseInt(prefs.getString("player2", "0")) - 1;
            game.position2 = false;
        }
        if (prefs.getString("player3", "0").equals("0"))
        {
            game.position3 = true;
        }
        else
        {
            GameActivity.cpuLevel[2] = Integer.parseInt(prefs.getString("player3", "0")) - 1;
            game.position3 = false;
        }
        if (prefs.getString("player4", "0").equals("0"))
        {
            game.position4 = true;
        }
        else
        {
            GameActivity.cpuLevel[3] = Integer.parseInt(prefs.getString("player4", "0")) - 1;
            game.position4 = false;
        }
        minute = prefs.getInt("time1", minute);
        second = prefs.getInt("time2", second);
        GameActivity.milliseconds = ((minute * 60) + second) * 1000;

        game.checking = prefs.getBoolean("checking", game.checking);
        game.placing = prefs.getBoolean("placing", game.placing);
        game.reverting = prefs.getBoolean("reverting", game.reverting);
        game.firstrank = prefs.getBoolean("firstrank", game.firstrank);

    }

    public void setBoardBackground()
    {
        final BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
        final BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (i == 0 || i == 2 || i == 4 || i == 6)
                {
                    if (j == 0 || j == 2 || j == 4 || j == 6)
                    {
                        board[0][i][j].setBackground(black);
                        board[1][i][j].setBackground(black);
                    }
                    else
                    {
                        board[0][i][j].setBackground(white);
                        board[1][i][j].setBackground(white);
                    }
                }
                else
                {
                    if (j == 0 || j == 2 || j == 4 || j == 6)
                    {
                        board[0][i][j].setBackground(white);
                        board[1][i][j].setBackground(white);
                    }
                    else
                    {
                        board[0][i][j].setBackground(black);
                        board[1][i][j].setBackground(black);
                    }
                }
            }
        }
    }

    private void setStartingPiecesUI()
    {
        for (int b = 0; b < 2; b++)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    board[b][i][j].setImageResource(game.positions[b][i][j].getResID());
                }
            }
        }

        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[0][j][i].setRotation(90);
                board[0][7 - j][7 - i].setRotation(270);
                board[1][j][i].setRotation(270);
                board[1][7 - j][7 - i].setRotation(90);
            }
        }

        for (int i = 0; i < 30; i++)
        {
            roster1[i].setImageResource(game.roster1p[i].getResID());
            roster2[i].setImageResource(game.roster2p[i].getResID());
            roster3[i].setImageResource(game.roster3p[i].getResID());
            roster4[i].setImageResource(game.roster4p[i].getResID());
        }

    }

    private void setStartButton() {
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (game.gameState == 2)
                {
                    game.whiteTurn1 = game.turnSave1;
                    game.whiteTurn2 = game.turnSave2;
                    setInitialSquareListeners(board[0], 0);
                    setInitialSquareListeners(board[1], 1);
                    game.gameState = 1;
                    start.setText("Pause");
                    startAI();
                    return;
                }
                if (game.gameState == 1)
                {
                    game.clean();
                    clean(board[0], 0);
                    clean(board[1], 1);
                    game.turnSave1 = game.whiteTurn1;
                    game.whiteTurn1 = 3;
                    game.turnSave2 = game.whiteTurn2;
                    game.whiteTurn2 = 3;
                    nukeListeners(board[0], 0);
                    nukeListeners(board[1], 1);
                    game.gameState = 2;
                    start.setText("Resume");
                }
                if (game.gameState == 0)
                {
                    scroll1.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            scroll1.fullScroll(View.FOCUS_UP);
                            scroll3.fullScroll(View.FOCUS_UP);
                            scroll2.fullScroll(View.FOCUS_DOWN);
                            scroll4.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    game.resetBooleans();
                    clean(board[0],0);
                    clean(board[1],1);
                    game.clean();

                    setStartingPiecesUI();

                    setInitialSquareListeners(board[0], 0);
                    setInitialSquareListeners(board[1], 1);
                    LinearLayout pawnOptions1 = findViewById(R.id.pawnOptions1);
                    LinearLayout pawnOptions2 = findViewById(R.id.pawnOptions2);
                    pawnOptions1.setVisibility(View.INVISIBLE);
                    pawnOptions2.setVisibility(View.INVISIBLE);
                    startTimers();
                    start.setText("Pause");
                    game.gameState = 1;
                    startAI();
                }
            }
        });
    }


    private void startTimers()
    {
        final TextView timer1 = findViewById(R.id.timer1);
        final TextView timer2 = findViewById(R.id.timer2);
        final TextView timer3 = findViewById(R.id.timer3);
        final TextView timer4 = findViewById(R.id.timer4);

        new CountDownTimer(milliseconds * 2, 100)
        {
            boolean gameEnded = false;
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (!gameEnded)
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.whiteTurn1 == 1)
                    {
                        timer1.setText(time);
                        saved = saved - 100;
                        timer1.setBackgroundColor(0xFFFFC900);
                        timer1.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(1, 2);
                        }
                    }
                    else
                    {
                        timer1.setBackgroundColor(0x00FFFFFF);
                        timer1.setTextColor(0xFF848484);
                    }
                    if (game.gameState == 0)
                    {
                        gameEnded = true;
                        timer1.setBackgroundColor(0x00FFFFFF);
                        timer1.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            boolean gameEnded = false;
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (!gameEnded)
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.whiteTurn1 == 2)
                    {
                        timer2.setText(time);
                        saved = saved - 100;
                        timer2.setBackgroundColor(0xFFFFC900);
                        timer2.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(0, 2);
                        }
                    }
                    else
                    {
                        timer2.setBackgroundColor(0x00FFFFFF);
                        timer2.setTextColor(0xFF848484);
                    }
                    if (game.gameState == 0)
                    {
                        gameEnded = true;
                        timer2.setBackgroundColor(0x00FFFFFF);
                        timer2.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            boolean gameEnded = false;
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (!gameEnded)
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.whiteTurn2 == 1)
                    {
                        timer4.setText(time);
                        saved = saved - 100;
                        timer4.setBackgroundColor(0xFFFFC900);
                        timer4.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(0, 2);
                        }
                    }
                    else
                    {
                        timer4.setBackgroundColor(0x00FFFFFF);
                        timer4.setTextColor(0xFF848484);
                    }
                    if (game.gameState == 0)
                    {
                        gameEnded = true;
                        timer4.setBackgroundColor(0x00FFFFFF);
                        timer4.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();
        new CountDownTimer(milliseconds * 2, 100)
        {
            boolean gameEnded = false;
            int saved = milliseconds;

            public void onTick(long millisUntilFinished)
            {
                if (!gameEnded)
                {
                    int millis = saved;
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("m:ss");
                    df.setTimeZone(tz);
                    String time = df.format(new Date(millis));
                    if (game.whiteTurn2 == 2)
                    {
                        timer3.setText(time);
                        saved = saved - 100;
                        timer3.setBackgroundColor(0xFFFFC900);
                        timer3.setTextColor(0xFFFFFFFF);
                        if (saved == 0)
                        {
                            gameEndProcedures(1, 2);
                        }
                    }
                    else
                    {
                        timer3.setBackgroundColor(0x00FFFFFF);
                        timer3.setTextColor(0xFF848484);
                    }
                    if (game.gameState == 0)
                    {
                        gameEnded = true;
                        timer3.setBackgroundColor(0x00FFFFFF);
                        timer3.setTextColor(0xFF848484);
                    }
                }
            }

            public void onFinish()
            {
                this.cancel();
            }
        }.start();

    }

    private void clean(ImageView[][] board, int boardNumber)
    {
        BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
        BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j].setOnDragListener(new View.OnDragListener()
                {
                    @Override
                    public boolean onDrag(View v, DragEvent event)
                    {
                        return false;
                    }
                });
                if (game.positions[boardNumber][i][j].backgroundColor.equals("D"))
                {
                    board[i][j].setImageResource(android.R.color.transparent);
                }
                if (game.positions[boardNumber][i][j].backgroundColor.equals("Y"))
                {
                    if (i == 0 || i == 2 || i == 4 || i == 6)
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(black);
                        }
                        else
                        {
                            board[i][j].setBackground(white);
                        }
                    }
                    else
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(white);
                        }
                        else
                        {
                            board[i][j].setBackground(black);
                        }
                    }

                }
                if (game.positions[boardNumber][i][j].backgroundColor.equals("R"))
                {
                    if (i == 0 || i == 2 || i == 4 || i == 6)
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(black);
                        }
                        else
                        {
                            board[i][j].setBackground(white);
                        }
                    }
                    else
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(white);
                        }
                        else
                        {
                            board[i][j].setBackground(black);
                        }
                    }
                }
                if (game.positions[boardNumber][i][j].backgroundColor.equals("B"))
                {
                    if (i == 0 || i == 2 || i == 4 || i == 6)
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(black);
                        }
                        else
                        {
                            board[i][j].setBackground(white);
                        }
                    }
                    else
                    {
                        if (j == 0 || j == 2 || j == 4 || j == 6)
                        {
                            board[i][j].setBackground(white);
                        }
                        else
                        {
                            board[i][j].setBackground(black);
                        }
                    }
                }
            }
        }
        int light = 0x80B0B0B0;
        int dark = 0x808E8E8E;
        for (int i = 0; i < 30; i++)
        {
            if (boardNumber == 0)
            {
                if (game.roster1p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster1[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster1[i].setBackgroundColor(dark);
                    }
                }
                if (game.roster2p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster2[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster2[i].setBackgroundColor(dark);
                    }
                }
            }
            else
            {
                if (game.roster3p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster3[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster3[i].setBackgroundColor(dark);
                    }
                }
                if (game.roster4p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster4[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster4[i].setBackgroundColor(dark);
                    }
                }
            }
        }

    }

    private void setInitialSquareListeners(final ImageView[][] board, int boardNumber)
    {
        clean(board, boardNumber);
        game.updateLegalCastlingVariables(boardNumber);
        game.checkIfKingsStillStanding(boardNumber);
        if (game.whiteInCheck(game.positions[boardNumber], boardNumber))
        {
            setWhiteCheckConditions(board, boardNumber);
        }
        if (game.blackInCheck(game.positions[boardNumber], boardNumber))
        {
            setBlackCheckConditions(board, boardNumber);
        }

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                final int x = i;
                final int y = j;
                board[i][j].setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent)
                    {
                        String ID = "00";
                        if (boardNumber == 0)
                        {
                            if (game.whiteTurn1 == 1 && game.position1)
                            {
                                if (game.positions[boardNumber][x][y].color.equals("white"))
                                {
                                    setPiecePotentialMoves(board, x, y, boardNumber);
                                    ID = game.positions[boardNumber][x][y].type + "1";
                                }
                            }
                            if (game.whiteTurn1 == 2 && game.position2)
                            {
                                if (game.positions[boardNumber][x][y].color.equals("black"))
                                {
                                    setPiecePotentialMoves(board, x, y, boardNumber);
                                    ID = game.positions[boardNumber][x][y].type + "2";
                                }
                            }
                        }
                        if (boardNumber == 1)
                        {
                            if (game.whiteTurn2 == 1 && game.position4)
                            {
                                if (game.positions[boardNumber][x][y].color.equals("white"))
                                {
                                    setPiecePotentialMoves(board, x, y, boardNumber);
                                    ID = game.positions[boardNumber][x][y].type + "3";
                                }
                            }
                            if (game.whiteTurn2 == 2 && game.position3)
                            {
                                if (game.positions[boardNumber][x][y].color.equals("black"))
                                {
                                    setPiecePotentialMoves(board, x, y, boardNumber);
                                    ID = game.positions[boardNumber][x][y].type + "4";
                                }
                            }
                        }

                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                        {
                            View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                            ClipData data = ClipData.newPlainText("", "");
                            view.startDrag(data, myShadowBuilder, view, 0);
                        }

                        return true;
                    }
                });
            }
        }

        for (int i = 0; i < 30; i++)
        {
            final int j = i;
            roster1[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.whiteTurn1 == 1 && !game.roster1p[j].empty && game.position1)
                    {
                        setRosterPiecePotentialMoves(board, roster1, j, 0);
                        ID = game.roster1p[j].type + "1";
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster2[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (!game.roster2p[j].empty && game.whiteTurn1 == 2 && game.position2)
                    {
                        setRosterPiecePotentialMoves(board, roster2, j, 0);
                        ID = game.roster2p[j].type + "2";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster3[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (game.whiteTurn2 == 2 && !game.roster3p[j].empty && game.position3)
                    {
                        setRosterPiecePotentialMoves(board, roster3, j ,1);
                        ID = game.roster3p[j].type + "4";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
            roster4[i].setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    String ID = "00";
                    if (!game.roster4p[j].empty && game.whiteTurn2 == 1 && game.position4)
                    {
                        setRosterPiecePotentialMoves(board, roster4, j, 1);
                        ID = game.roster4p[j].type + "3";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new GameActivity.MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private void setWhiteCheckConditions(ImageView[][] board, int boardNumber)
    {
        if (game.checking)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (game.positions[boardNumber][i][j].color.equals("white") && game.positions[boardNumber][i][j].type.equals("king"))
                    {
                        board[i][j].setBackgroundColor(Color.BLUE);
                        game.positions[boardNumber][i][j].backgroundColor = "B";
                    }
                }
            }
        }
        game.checkForCheckmate("white", boardNumber);
    }

    private void setBlackCheckConditions(ImageView[][] board, int boardNumber)
    {
        if (game.checking)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (game.positions[boardNumber][i][j].color.equals("black") && game.positions[boardNumber][i][j].type.equals("king"))
                    {
                        board[i][j].setBackgroundColor(Color.BLUE);
                        game.positions[boardNumber][i][j].backgroundColor = "B";

                    }
                }
            }
        }
        game.checkForCheckmate("black", boardNumber);
    }



    private void setRosterPiecePotentialMoves(ImageView[][] board, ImageView[] roster, int i, int boardNumber)
    {
        setInitialSquareListeners(board, boardNumber);
        Piece[] rosterArray = game.getCurrentRosterArray(boardNumber);
        Set<Move> moves = rosterArray[i].getRosterMoves(game.positions[boardNumber], rosterArray, i);
        for (Move m : moves)
        {
            setPotentialRosterMove(board, roster, m.i, m.x1, m.y1, boardNumber);
        }
    }

    private void setPotentialRosterMove(final ImageView[][] board, final ImageView[] roster, final int i, final int x, final int y, int boardNumber)
    {
        Piece[] rosterArray = game.getCurrentRosterArray(boardNumber);

        if (!game.rosterMoveIsLegal(rosterArray[i],x, y, boardNumber)) return;

        roster[i].setBackgroundColor(Color.YELLOW);
        if (!rosterArray[i].backgroundColor.equals("Y"))
        {
            rosterArray[i].backgroundColor = ("Y");
        }
        board[x][y].setImageResource(R.mipmap.dot);
        game.positions[boardNumber][x][y].backgroundColor = ("D");


        board[x][y].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                performRosterMove(board, roster, i, x, y, boardNumber);
                return true;
            }
        });
        board[x][y].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                int dragEvent = event.getAction();
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x][y].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        dragClean(board, x, y);
                        break;

                    case DragEvent.ACTION_DROP:
                        dragClean(board, x, y);
                        performRosterMove(board, roster, i, x, y, boardNumber);
                        break;
                }
                return true;
            }
        });
    }

    private void performRosterMove(ImageView[][] board, ImageView[] roster, int i, int x, int y, int boardNumber)
    {
        game.performRosterMove(i, x, y, boardNumber);
        clean(board, boardNumber);
        board[x][y].setImageResource(game.positions[boardNumber][x][y].getResID());
        board[x][y].setRotation(roster[i].getRotation());
        roster[i].setRotation(0);
        roster[i].setImageResource(android.R.color.transparent);
        pawnCheck(board, game.positions[boardNumber], boardNumber);
        setInitialSquareListeners(board, boardNumber);
    }

    private void setPiecePotentialMoves(ImageView[][] board, int x, int y, int boardNumber)
    {
        setInitialSquareListeners(board, boardNumber);

        Set<Move> moves = game.positions[boardNumber][x][y].getMoves(game.positions[boardNumber], x, y, boardNumber);
        for (Move m : moves)
        {
            setPotentialMove(m.type, board, m.x, m.y, m.x1, m.y1, boardNumber);
        }
    }


    private void setPotentialMove(final String moveType, final ImageView[][] board, final int x, final int y, final int x1, final int y1, int boardNumber)
    {
        if (game.checkIfMoveResultsInCheck(moveType,x,y,x1,y1,boardNumber)) return;

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!game.positions[boardNumber][x][y].backgroundColor.equals("Y"))
        {
            game.positions[boardNumber][x][y].backgroundColor = "Y";
        }
        if (moveType.equals("take") || moveType.equals("whiteEnP") || moveType.equals("blackEnP"))
        {
            board[x1][y1].setBackgroundColor(Color.RED);
            game.positions[boardNumber][x1][y1].backgroundColor = "R";
        }
        else
        {
            board[x1][y1].setImageResource(R.mipmap.dot);
            game.positions[boardNumber][x1][y1].backgroundColor = "D";
        }

        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                performMove(moveType, board, x, y, x1, y1, boardNumber);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                int dragEvent = event.getAction();
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        dragClean(board, x1, y1);
                        break;

                    case DragEvent.ACTION_DROP:
                        dragClean(board, x1, y1);
                        performMove(moveType, board, x, y, x1, y1, boardNumber);
                        break;
                }
                return true;
            }
        });
    }


    private void performMove(String moveType, final ImageView[][] board, int x, int y, final int x1, final int y1, int boardNumber)
    {
        game.performMove(moveType,x,y,x1,y1,boardNumber);
        clean(board, boardNumber);

        if (moveType.equals("take") || moveType.equals("whiteEnP") || moveType.equals("blackEnP"))
            addToRoster(game.positions[boardNumber], x1, y1, boardNumber);


        switchBoardImages(moveType, board, x, y, x1, y1, boardNumber);
        pawnCheck(board, game.positions[boardNumber], boardNumber);
        setInitialSquareListeners(board, boardNumber);
    }



    private void switchBoardImages(String moveType, ImageView[][] board, int x, int y, int x1, int y1, int boardNumber)
    {
        if (moveType.equals("whiteKingCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.king);
            board[5][0].setImageResource(R.mipmap.rook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[5][0].setRotation(board[7][0].getRotation());
            board[7][0].setRotation(0);
            board[7][0].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("whiteQueenCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.king);
            board[3][0].setImageResource(R.mipmap.rook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[3][0].setRotation(board[0][0].getRotation());
            board[0][0].setRotation(0);
            board[0][0].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackKingCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.bking);
            board[5][7].setImageResource(R.mipmap.brook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[5][7].setRotation(board[7][7].getRotation());
            board[7][7].setRotation(0);
            board[7][7].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackQueenCastle"))
        {
            board[x1][y1].setImageResource(R.mipmap.bking);
            board[3][7].setImageResource(R.mipmap.brook);
            board[x1][y1].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            board[3][7].setRotation(board[0][7].getRotation());
            board[0][7].setRotation(0);
            board[0][7].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("whiteEnP"))
        {
            board[x1][y].setRotation(0);
            board[x1][y].setImageResource(android.R.color.transparent);
            board[x1][5].setImageResource(game.positions[boardNumber][x1][5].getResID());
            board[x1][5].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            return;
        }
        if (moveType.equals("blackEnP"))
        {
            board[x1][y].setRotation(0);
            board[x1][y].setImageResource(android.R.color.transparent);
            board[x1][2].setImageResource(game.positions[boardNumber][x1][2].getResID());
            board[x1][2].setRotation(board[x][y].getRotation());
            board[x][y].setRotation(0);
            board[x][y].setImageResource(android.R.color.transparent);
            return;
        }
        board[x1][y1].setImageResource(game.positions[boardNumber][x1][y1].getResID());
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
    }

    private void addToRoster(Piece[][] positions, int x1, int y1, int boardNumber)
    {
        if (boardNumber == 0)
        {
            if (positions[x1][y1].color.equals("white"))
            {
                addToRosterShit(positions, roster4, game.roster4p, x1, y1, 270);
            }
            if (positions[x1][y1].color.equals("black"))
            {
                addToRosterShit(positions, roster3, game.roster3p, x1, y1, 90);
            }
        }
        else
        {
            if (positions[x1][y1].color.equals("white"))
            {
                addToRosterShit(positions, roster1, game.roster1p, x1, y1, 90);
            }
            if (positions[x1][y1].color.equals("black"))
            {
                addToRosterShit(positions, roster2, game.roster2p, x1, y1, 270);
            }
        }

    }

    private void addToRosterShit(final Piece[][] positions, ImageView[] roster, Piece[] rosterp, int x1, int y1, int rotation)
    {
        for (int i = 0; i < 30; i++)
        {
            if (rosterp[i].empty)
            {
                if (game.reverting)
                {
                    if (positions[x1][y1].wasPawn)
                    {
                        if (positions[x1][y1].color.equals("white"))
                        {
                            roster[i].setImageResource(R.mipmap.pawn);
                            roster[i].setRotation(rotation);
                            rosterp[i] = new Pawn("white");
                            break;
                        }
                        if (positions[x1][y1].color.equals("black"))
                        {
                            roster[i].setImageResource(R.mipmap.bpawn);
                            roster[i].setRotation(rotation);
                            rosterp[i] = new Pawn("black");
                            break;
                        }
                    }
                }
                roster[i].setImageResource(positions[x1][y1].getResID());
                roster[i].setRotation(rotation);
                rosterp[i] = positions[x1][y1];
                break;
            }
        }
    }



    private void pawnCheck(final ImageView[][] board, final Piece[][] positions, int boardNumber)
    {
        final LinearLayout pawnOptions1 = findViewById(R.id.pawnOptions1);
        final LinearLayout pawnOptions2 = findViewById(R.id.pawnOptions2);
        final Button queen1 = findViewById(R.id.queen1);
        Button queen2 = findViewById(R.id.queen2);
        Button rook1 = findViewById(R.id.rook1);
        Button rook2 = findViewById(R.id.rook2);
        Button bishop1 = findViewById(R.id.bishop1);
        Button bishop2 = findViewById(R.id.bishop2);
        Button knight1 = findViewById(R.id.knight1);
        Button knight2 = findViewById(R.id.knight2);

        final ImageView square = findViewById(R.id.a1_1);
        int width = square.getWidth();

        pawnOptions1.getLayoutParams().width = width * 8;
        pawnOptions1.getLayoutParams().height = width * 8;
        pawnOptions2.getLayoutParams().width = width * 8;
        pawnOptions2.getLayoutParams().height = width * 8;

        if (boardNumber == 0)
        {
            if (game.whiteTurn1 == 2)
            {
                for (int i = 0; i < 8; i++)
                {
                    final int x = i;
                    if (positions[i][7].color.equals("white") && positions[i][7].type.equals("pawn"))
                    {
                        game.whiteTurn1 = 3;
                        nukeListeners(board, boardNumber);
                        pawnOptions1.setVisibility(View.VISIBLE);
                        pawnOptions1.setRotation(90);
                        if (game.gameState == 1)
                        {
                            queen1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
//                                    positions[x][7] = "WQP0";
                                    positions[x][7] = new Queen("white");
                                    positions[x][7].wasPawn = true;
                                    board[x][7].setImageResource(R.mipmap.queen);
                                    if (game.gameState == 1)
                                    {
                                        pawnOptions1.setVisibility(View.INVISIBLE);
                                        game.whiteTurn1 = 2;
                                        setInitialSquareListeners(board, boardNumber);
                                    }
                                }
                            });
                            rook1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    positions[x][7] = new Rook("white");
                                    positions[x][7].wasPawn = true;
                                    board[x][7].setImageResource(R.mipmap.rook);
                                    if (game.gameState == 1)
                                    {
                                        pawnOptions1.setVisibility(View.INVISIBLE);
                                        game.whiteTurn1 = 2;
                                        setInitialSquareListeners(board, boardNumber);
                                    }
                                }
                            });
                            bishop1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    positions[x][7] = new Bishop("white");
                                    positions[x][7].wasPawn = true;
                                    board[x][7].setImageResource(R.mipmap.bishop);
                                    if (game.gameState == 1)
                                    {
                                        pawnOptions1.setVisibility(View.INVISIBLE);
                                        game.whiteTurn1 = 2;
                                        setInitialSquareListeners(board, boardNumber);
                                    }
                                }
                            });
                            knight1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    positions[x][7] = new Knight("white");
                                    positions[x][7].wasPawn = true;
                                    board[x][7].setImageResource(R.mipmap.knight);
                                    if (game.gameState == 1)
                                    {
                                        pawnOptions1.setVisibility(View.INVISIBLE);
                                        game.whiteTurn1 = 2;
                                        setInitialSquareListeners(board, boardNumber);
                                    }

                                }
                            });
                            if (!game.position1)
                            {
                                positions[x][7] = new Queen("white");
                                positions[x][7].wasPawn = true;
                                board[x][7].setImageResource(R.mipmap.queen);
                                if (game.gameState == 1)
                                {
                                    pawnOptions1.setVisibility(View.INVISIBLE);
                                    game.whiteTurn1 = 2;
                                }
                            }
                        }
                    }
                }
            }
            if (game.whiteTurn1 == 1)
            {
                for (int i = 0; i < 8; i++)
                {
                    final int x = i;
                    if (positions[i][0].color.equals("black") && positions[i][0].type.equals("pawn"))
                    {
                        game.whiteTurn1 = 3;
                        nukeListeners(board, boardNumber);
                        pawnOptions1.setVisibility(View.VISIBLE);
                        pawnOptions1.setRotation(270);

                        queen1.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
//                                positions[x][0] = "BQP0";
                                positions[x][0] = new Queen("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bqueen);
                                if (game.gameState == 1)
                                {
                                    pawnOptions1.setVisibility(View.INVISIBLE);
                                    game.whiteTurn1 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        rook1.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Rook("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.brook);
                                if (game.gameState == 1)
                                {
                                    pawnOptions1.setVisibility(View.INVISIBLE);
                                    game.whiteTurn1 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        bishop1.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Bishop("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bbishop);
                                if (game.gameState == 1)
                                {
                                    pawnOptions1.setVisibility(View.INVISIBLE);
                                    game.whiteTurn1 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        knight1.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Knight("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bknight);
                                if (game.gameState == 1)
                                {
                                    pawnOptions1.setVisibility(View.INVISIBLE);
                                    game.whiteTurn1 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        if (!game.position2)
                        {
                            positions[x][0] = new Queen("black");
                            positions[x][0].wasPawn = true;
                            board[x][0].setImageResource(R.mipmap.bqueen);
                            if (game.gameState == 1)
                            {
                                pawnOptions1.setVisibility(View.INVISIBLE);
                                game.whiteTurn1 = 1;
                            }
                        }
                    }
                }
            }
        }
        if (boardNumber == 1)
        {
            if (game.whiteTurn2 == 2)
            {
                for (int i = 0; i < 8; i++)
                {
                    final int x = i;
                    if (positions[i][7].color.equals("white") && positions[i][7].type.equals("pawn"))
                    {
                        game.whiteTurn2 = 3;
                        nukeListeners(board, boardNumber);
                        pawnOptions2.setVisibility(View.VISIBLE);
                        pawnOptions2.setRotation(270);
                        queen2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
//                                positions[x][7] = "WQP0";
                                positions[x][7] = new Queen("white");
                                positions[x][7].wasPawn = true;
                                board[x][7].setImageResource(R.mipmap.queen);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 2;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        rook2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][7] = new Rook("white");
                                positions[x][7].wasPawn = true;
                                board[x][7].setImageResource(R.mipmap.rook);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 2;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        bishop2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][7] = new Bishop("white");
                                positions[x][7].wasPawn = true;
                                board[x][7].setImageResource(R.mipmap.bishop);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 2;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        knight2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][7] = new Knight("white");
                                positions[x][7].wasPawn = true;
                                board[x][7].setImageResource(R.mipmap.knight);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 2;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        if (!game.position4)
                        {
                            positions[x][7] = new Queen("white");
                            positions[x][7].wasPawn = true;
                            board[x][7].setImageResource(R.mipmap.queen);
                            if (game.gameState == 1)
                            {
                                pawnOptions2.setVisibility(View.INVISIBLE);
                                game.whiteTurn2 = 2;
                            }
                        }
                    }
                }
            }
            if (game.whiteTurn2 == 1)
            {
                for (int i = 0; i < 8; i++)
                {
                    final int x = i;
                    if (positions[i][0].color.equals("black") && positions[i][0].type.equals("pawn"))
                    {
                        game.whiteTurn2 = 3;
                        nukeListeners(board, boardNumber);
                        pawnOptions2.setVisibility(View.VISIBLE);
                        pawnOptions2.setRotation(90);
                        queen2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Queen("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bqueen);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        rook2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Rook("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.brook);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        bishop2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Bishop("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bbishop);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        knight2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                positions[x][0] = new Knight("black");
                                positions[x][0].wasPawn = true;
                                board[x][0].setImageResource(R.mipmap.bknight);
                                if (game.gameState == 1)
                                {
                                    pawnOptions2.setVisibility(View.INVISIBLE);
                                    game.whiteTurn2 = 1;
                                    setInitialSquareListeners(board, boardNumber);
                                }
                            }
                        });
                        if (!game.position3)
                        {
                            positions[x][0] = new Queen("black");
                            positions[x][0].wasPawn = true;
                            board[x][0].setImageResource(R.mipmap.bqueen);
                            if (game.gameState == 1)
                            {
                                pawnOptions2.setVisibility(View.INVISIBLE);
                                game.whiteTurn2 = 1;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Removes all listeners on all squares
     */
    private void nukeListeners(final ImageView[][] board, int boardNumber)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                board[i][j].setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        return false;
                    }
                });
            }
        }
        if (boardNumber == 0)
        {
            for (int i = 0; i < 30; i++)
            {
                roster1[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                roster2[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
            }

        }
        else
        {
            for (int i = 0; i < 30; i++)
            {
                roster3[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                roster4[i].setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
            }

        }
    }


    private void startAI()
    {
        if (!game.position1)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    boolean a = true;
                    while (a)
                    {
                        if (game.gameState == 0 || game.gameState == 2)
                        {
                            System.out.println("a false");
                            a = false;
                        }
                        Random rand = new Random();
                        int wait = rand.nextInt(1000);
                        try
                        {
                            Thread.sleep(wait);
                        } catch (InterruptedException e)
                        {
                            System.out.println("got interrupted!");
                        }
                        if (game.whiteTurn1 == 1)
                        {
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                System.out.println("got interrupted!");
                            }
                        }
                        if (game.whiteTurn1 == 1)
                        {
                            AIMinimax ai = new AIMinimax("white", board[0], getArrayClone(game.positions[0]), roster1, game.roster1p.clone(), roster2, game.roster2p.clone(), 0);
                            final Move bestMove = ai.getBestMove();
                            if (bestMove == null)
                            {
                                continue;
                            }
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (bestMove.type.equals("roster"))
                                        performRosterMove(board[0],  roster1, bestMove.i, bestMove.x1, bestMove.y1, 0);
                                    else
                                        performMove(bestMove.type, board[0], bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 0);
                                }
                            });
                        }
                    }
                }
            }).start();
        }
        if (!game.position2)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    boolean a = true;
                    while (a)
                    {
                        if (game.gameState == 0 || game.gameState == 2)
                        {
                            a = false;
                        }
                        Random rand = new Random();
                        int wait = rand.nextInt(1000);
                        try
                        {
                            Thread.sleep(wait);
                        } catch (InterruptedException e)
                        {
                            System.out.println("got interrupted!");
                        }
                        if (game.whiteTurn1 == 2)
                        {
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                System.out.println("got interrupted!");
                            }
                        }
                        if (game.whiteTurn1 == 2)
                        {
                            AIMinimax ai = new AIMinimax("black", board[0], getArrayClone(game.positions[0]), roster2, game.roster2p.clone(), roster1, game.roster1p.clone(), 0);
                            final Move bestMove = ai.getBestMove();
                            if (bestMove == null)
                            {
                                continue;
                            }
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (bestMove.type.equals("roster"))
                                        performRosterMove(board[0], roster2, bestMove.i, bestMove.x, bestMove.y, 0);
                                    else
                                        performMove(bestMove.type, board[0], bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 0);
                                }
                            });
                        }
                    }

                }
            }).

                    start();
        }
        if (!game.position3)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    boolean a = true;
                    while (a)
                    {
                        if (game.gameState == 0 || game.gameState == 2)
                        {
                            a = false;
                        }
                        Random rand = new Random();
                        int wait = rand.nextInt(1000);
                        try
                        {
                            Thread.sleep(wait);
                        } catch (InterruptedException e)
                        {
                            System.out.println("got interrupted!");
                        }
                        if (game.whiteTurn2 == 2)
                        {
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                System.out.println("got interrupted!");
                            }
                        }
                        if (game.whiteTurn2 == 2)
                        {
                            AIMinimax ai = new AIMinimax("black", board[1], getArrayClone(game.positions[1]), roster4, game.roster4p.clone(), roster3, game.roster3p.clone(), 12);
                            final Move bestMove = ai.getBestMove();
                            if (bestMove == null)
                            {
                                continue;
                            }
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (bestMove.type.equals("roster"))
                                        performRosterMove(board[1], roster4, bestMove.i, bestMove.x, bestMove.y,1);
                                    else
                                        performMove(bestMove.type, board[1], bestMove.x, bestMove.y, bestMove.x1, bestMove.y1,1);
                                }
                            });
                        }
                    }
                }
            }).start();
        }
        if (!game.position4)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    boolean a = true;
                    while (a)
                    {
                        if (game.gameState == 0 || game.gameState == 2)
                        {
                            a = false;
                        }
                        Random rand = new Random();
                        int wait = rand.nextInt(1000);
                        try
                        {
                            Thread.sleep(wait);
                        } catch (InterruptedException e)
                        {
                            System.out.println("got interrupted!");
                        }
                        if (game.whiteTurn2 == 1)
                        {
                            try
                            {
                                Thread.sleep(1000);
                            } catch (InterruptedException e)
                            {
                                System.out.println("got interrupted!");
                            }
                        }
                        if (game.whiteTurn2 == 1)
                        {
                            AIMinimax ai = new AIMinimax("white", board[1], getArrayClone(game.positions[1]), roster3, game.roster3p.clone(), roster4, game.roster4p.clone(), 1);
                            final Move bestMove = ai.getBestMove();
                            if (bestMove == null)
                            {
                                continue;
                            }
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (bestMove.type.equals("roster"))
                                        performRosterMove(board[1],  roster3, bestMove.i, bestMove.x, bestMove.y, 1);
                                    else
                                        performMove(bestMove.type, board[1], bestMove.x, bestMove.y, bestMove.x1, bestMove.y1, 1);
                                }
                            });
                        }
                    }
                }
            }).start();
        }
    }

    private void gameEndProcedures(int side, int type)
    {
        clean(board[0], 0);
        clean(board[1], 1);
        game.whiteTurn1 = 3;
        game.whiteTurn2 = 3;
        nukeListeners(board[0], 0);
        nukeListeners(board[1], 1);
        final Button start = findViewById(R.id.start);
        start.setText("Start");
        game.gameState = 0;
        TextView timeNotice = findViewById(R.id.timeNotice);
        timeNotice.setVisibility(View.INVISIBLE);
        final LinearLayout finishScreen = findViewById(R.id.finishScreen);

        if (side == 0)
        {
            TextView L = findViewById(R.id.L);
            L.setText("WINNER");
            L.setRotation(90);
            TextView R_ = findViewById(R.id.R);
            R_.setText("LOSER");
            R_.setRotation(270);

        }
        else
        {
            TextView L = findViewById(R.id.L);
            L.setText("LOSER");
            L.setRotation(90);
            TextView R_ = findViewById(R.id.R);
            R_.setText("WINNER");
            R_.setRotation(270);


        }
        final TextView endType = findViewById(R.id.endType);
        if (type == 0)
        {
            endType.setText("CHECKMATE");
        }
        if (type == 1)
        {
            endType.setText("KING CAPTURED");
        }
        if (type == 2)
        {
            endType.setText("TIME RAN OUT");
        }
        new CountDownTimer(2000, 1)
        {
            public void onTick(long millisUntilFinished)
            {
                double trouble = ((double) (2000 - millisUntilFinished) / 4000) * 255;
                finishScreen.setAlpha((float) (2000 - millisUntilFinished) / 2000);
                //fssection.getBackground().setAlpha((int) trouble);
                finishScreen.setVisibility(View.VISIBLE);
            }

            public void onFinish()
            {
                finishScreen.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finishScreen.setVisibility(View.INVISIBLE);
                        if (mInterstitialAd.isLoaded())
                        {
                            mInterstitialAd.show();
                        }
                    }
                });
            }
        }.start();


    }

    private void dragClean(ImageView[][] board, int x1, int y1)
    {
        BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
        BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));

        int i = x1;
        int j = y1;
        if (i == 0 || i == 2 || i == 4 || i == 6)
        {
            if (j == 0 || j == 2 || j == 4 || j == 6)
            {
                board[i][j].setBackground(black);
            }
            else
            {
                board[i][j].setBackground(white);
            }
        }
        else
        {
            if (j == 0 || j == 2 || j == 4 || j == 6)
            {
                board[i][j].setBackground(white);
            }
            else
            {
                board[i][j].setBackground(black);
            }
        }
    }

    private class MyShadowBuilder extends View.DragShadowBuilder
    {
        private BitmapDrawable PIC;
        private int rotation;

        public MyShadowBuilder(View v, String ID)
        {
            super(v);

            int length = ID.length();
            if (ID.substring(length - 1, length).equals("1") || ID.substring(length - 1, length).equals("3"))
            {
                if (ID.substring(0, length - 1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.pawn, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.rook, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.knight, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bishop, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.queen, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.king, 100, 100));
                }
            }
            if (ID.substring(length - 1, length).equals("2") || ID.substring(length - 1, length).equals("4"))
            {
                if (ID.substring(0, length - 1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bpawn, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.brook, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bknight, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bbishop, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bqueen, 100, 100));
                }
                if (ID.substring(0, length - 1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bking, 100, 100));
                }
            }
            if (ID.substring(length - 1, length).equals("1") || ID.substring(length - 1, length).equals("4"))
            {
                rotation = 90;
            }
            if (ID.substring(length - 1, length).equals("2") || ID.substring(length - 1, length).equals("3"))
            {
                rotation = 270;
            }
        }

        @Override
        public void onDrawShadow(Canvas canvas)
        {
            //canvas.rotate(90);
            canvas.rotate(rotation, getView().getWidth(), getView().getHeight());
            PIC.draw(canvas);

        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint)
        {
            int height, width;
            height = getView().getHeight() * 2;
            width = getView().getHeight() * 2;

            PIC.setBounds(0, 0, width, height);

            shadowSize.set(width, height);

            if (rotation == 90)
            {
                shadowTouchPoint.set(0, height / 2);
            }
            if (rotation == 270)
            {
                shadowTouchPoint.set(width, height / 2);
            }
        }
    }
    private Piece[][] getArrayClone(Piece[][] positions)
    {
        Piece[] temp1 = positions[0].clone();
        Piece[] temp2 = positions[1].clone();
        Piece[] temp3 = positions[2].clone();
        Piece[] temp4 = positions[3].clone();
        Piece[] temp5 = positions[4].clone();
        Piece[] temp6 = positions[5].clone();
        Piece[] temp7 = positions[6].clone();
        Piece[] temp8 = positions[7].clone();
        Piece[][] tempPositions = {temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8};
        return tempPositions;
    }
    private void newGame()
    {
        game.whiteTurn1 = 3;
        game.whiteTurn2 = 3;
        nukeListeners(board[0], 0);
        nukeListeners(board[1], 1);
        final Button start = findViewById(R.id.start);
        start.setText("Start");
        game.gameState = 0;
        game.clean();
        clean(board[0], 0);
        clean(board[1], 1);
        setStartingPiecesUI();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        final TextView timer1 = findViewById(R.id.timer1);
        final TextView timer2 = findViewById(R.id.timer2);
        final TextView timer3 = findViewById(R.id.timer3);
        final TextView timer4 = findViewById(R.id.timer4);
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                timer1.setText(time);
                timer2.setText(time);
                timer3.setText(time);
                timer4.setText(time);
            }
        });
        LinearLayout pawnOptions1 = findViewById(R.id.pawnOptions1);
        LinearLayout pawnOptions2 = findViewById(R.id.pawnOptions2);
        pawnOptions1.setVisibility(View.INVISIBLE);
        pawnOptions2.setVisibility(View.INVISIBLE);
    }

    private void startSettings()
    {
        Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
        startActivity(intent);

        MainActivity.menu_code = 0;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (MainActivity.menu_code == 0)
                {
                    try
                    {
                        Thread.sleep(1);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                final LinearLayout mainmenu = findViewById(R.id.mainmenu);
                final TextView timer1 = findViewById(R.id.timer1);
                final TextView timer2 = findViewById(R.id.timer2);
                final TextView timer3 = findViewById(R.id.timer3);
                final TextView timer4 = findViewById(R.id.timer4);

                TimeZone tz = TimeZone.getTimeZone("UTC");
                SimpleDateFormat df = new SimpleDateFormat("m:ss");
                df.setTimeZone(tz);
                final String time = df.format(new Date(milliseconds));
                if (game.gameState == 0)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            timer1.setText(time);
                            timer2.setText(time);
                            timer3.setText(time);
                            timer4.setText(time);
                        }
                    });
                }

                if (MainActivity.menu_code == 2)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            newGame();
                        }
                    });
                }
            }
        }).start();
    }
}
