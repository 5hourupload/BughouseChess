package fhu.bughousechess;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.nsd.NsdManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import android.net.nsd.NsdServiceInfo;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import static fhu.bughousechess.NsdHelper.SERVICE_TYPE;


public class MainActivity extends AppCompatActivity
{
    static ImageView board1[][] = new ImageView[8][8];
    static Piece positions1[][] = new Piece[8][8];
    static ImageView board2[][] = new ImageView[8][8];
    static Piece positions2[][] = new Piece[8][8];

    static ImageView roster1[] = new ImageView[30];
    static Piece roster1p[] = new Piece[30];
    static ImageView roster2[] = new ImageView[30];
    static Piece roster2p[] = new Piece[30];
    static ImageView roster3[] = new ImageView[30];
    static Piece roster3p[] = new Piece[30];
    static ImageView roster4[] = new ImageView[30];
    static Piece roster4p[] = new Piece[30];

    int whiteTurn1 = 1;
    int whiteTurn2 = 1;

    static boolean whiteCastleQueen1 = true;
    static boolean whiteCastleKing1 = true;
    static boolean blackCastleQueen1 = true;
    static boolean blackCastleKing1 = true;
    static boolean whiteCastleQueen2 = true;
    static boolean whiteCastleKing2 = true;
    static boolean blackCastleQueen2 = true;
    static boolean blackCastleKing2 = true;


    static int minute = 5;
    static int second = 0;
    static boolean checking = true;
    static boolean placing = true;
    static boolean reverting = true;
    static boolean firstrank = false;

    boolean searchingForCheckmate1 = false;
    boolean checkmate1 = false;
    boolean searchingForCheckmate2 = false;
    boolean checkmate2 = false;

    static int gameState = 0;
    int turnSave1 = 0;
    int turnSave2 = 0;

    static int milliseconds = 5 * 60 * 1000;

    static String enP[][] = new String[8][4];
    static int board1Turn = 0;
    static int board2Turn = 0;
    private int currentApiVersion;
    private SharedPreferences prefs;

    InterstitialAd mInterstitialAd;


    static boolean position1 = true;
    static boolean position2 = true;
    static boolean position3 = true;
    static boolean position4 = true;

    boolean[] CPUsearch = {false, false, false, false};
    double[] rating = {0, 0, 0, 0};
    static double[] cpuLevel = {0, 0, 0, 0};

    int[] cpuX = {0, 0, 0, 0};
    int[] cpuY = {0, 0, 0, 0};
    int[] cpuX1 = {0, 0, 0, 0};
    int[] cpuY1 = {0, 0, 0, 0};
    int[] cpuRoster = {0, 0, 0, 0};
    boolean[] inCheck = {false, false, false, false};
    String[] moveType = {"0", "0", "0", "0"};
    static int menu_code = 0;
    static int dialog_margin;

    NsdHelper mNsdHelper;
    private TextView mStatusView;
    private Handler mUpdateHandler;
    public static final String TAG = "NsdChat";
    ChatConnection mConnection;
    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;
    NsdManager.DiscoveryListener mDiscoveryListener;
    WifiP2pManager mManager;
    NsdManager mNsdManager;


    Set<Piece> pieces = new HashSet<>();
    // W = White, B = Black, 0 = Not a piece
    // Piece first letter, 0 = Not a piece
    // P = Was a pawn, 0 = Legit
    // Y = Yellow, R = Red, B = Blue, D = Dot, 0 = No background


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
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


        if (!isTaskRoot())
        {
            finish();
            return;
        }


        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("261C12037B82A37B909C2E6BE482F9ED")  // An example device ID
                .addTestDevice("AA47C911625C161A0D482E29715BB8D9")
                .addTestDevice("46B3982AE175C2BBB3DB8E4E99C2A1C4")
                .build();
        mAdView.loadAd(request);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9794567752193168/1898888810");

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                requestNewInterstitial();
            }

        });

        requestNewInterstitial();


        final LinearLayout mainmenu = (LinearLayout) findViewById(R.id.mainmenu);
        mainmenu.setVisibility(View.VISIBLE);

        final LinearLayout optionsMenu = (LinearLayout) findViewById(R.id.optionsmenu);
        optionsMenu.setVisibility(View.INVISIBLE);

        final LinearLayout mainboard = (LinearLayout) findViewById(R.id.mainboard);
        mainboard.setVisibility(View.VISIBLE);

        final LinearLayout rules = (LinearLayout) findViewById(R.id.rules);
        rules.setVisibility(View.INVISIBLE);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.title, 300, 300));

        RelativeLayout side_image = (RelativeLayout) findViewById(R.id.side_image);
        BitmapDrawable horsey = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.main_menu_side_image, 100, 100));
        side_image.setBackground(horsey);

        LinearLayout finishScreen = (LinearLayout) findViewById(R.id.finishScreen);
        finishScreen.setVisibility(View.INVISIBLE);


        final TextView play = (TextView) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainmenu.setVisibility(View.INVISIBLE);
            }
        });

        final TextView connect = (TextView) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nsd();
            }
        });

        final TextView timer1 = (TextView) findViewById(R.id.timer1);
        final TextView timer2 = (TextView) findViewById(R.id.timer2);
        final TextView timer3 = (TextView) findViewById(R.id.timer3);
        final TextView timer4 = (TextView) findViewById(R.id.timer4);
        final Button options = (Button) findViewById(R.id.options);
        options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (gameState == 1)
                {
                    Button start = (Button) findViewById(R.id.start);
                    clean(board1, positions1);
                    clean(board2, positions2);
                    turnSave1 = whiteTurn1;
                    whiteTurn1 = 3;
                    turnSave2 = whiteTurn2;
                    whiteTurn2 = 3;
                    nuke(board1, positions1);
                    nuke(board2, positions2);
                    gameState = 2;
                    start.setText("Resume");
                }
                startSettings();
            }
        });

        final TextView mainToOptions = (TextView) findViewById(R.id.mainToOptions);
        mainToOptions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startSettings();
            }
        });


        final TextView howToPlay = (TextView) findViewById(R.id.howToPlay);
        howToPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                rules.setVisibility(View.VISIBLE);
                mainmenu.setVisibility(View.INVISIBLE);

            }
        });

        final Button rulesToMenu = (Button) findViewById(R.id.rulesToMenu);
        rulesToMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainmenu.setVisibility(View.VISIBLE);
                rules.setVisibility(View.INVISIBLE);


            }
        });


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Montepetrum bold.ttf");
        play.setTypeface(custom_font);
        mainToOptions.setTypeface(custom_font);
        howToPlay.setTypeface(custom_font);
        connect.setTypeface(custom_font);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getString("player1", "0").equals("0"))
        {
            MainActivity.position1 = true;
        }
        else
        {
            MainActivity.cpuLevel[0] = Integer.parseInt(prefs.getString("player1", "0")) - 1;
            MainActivity.position1 = false;
        }
        if (prefs.getString("player2", "0").equals("0"))
        {
            MainActivity.position2 = true;
        }
        else
        {
            MainActivity.cpuLevel[1] = Integer.parseInt(prefs.getString("player2", "0")) - 1;
            MainActivity.position2 = false;
        }
        if (prefs.getString("player3", "0").equals("0"))
        {
            MainActivity.position3 = true;
        }
        else
        {
            MainActivity.cpuLevel[2] = Integer.parseInt(prefs.getString("player3", "0")) - 1;
            MainActivity.position3 = false;
        }
        if (prefs.getString("player4", "0").equals("0"))
        {
            MainActivity.position4 = true;
        }
        else
        {
            MainActivity.cpuLevel[3] = Integer.parseInt(prefs.getString("player4", "0")) - 1;
            MainActivity.position4 = false;
        }
        minute = prefs.getInt("time1", minute);
        second = prefs.getInt("time2", second);
        MainActivity.milliseconds = ((minute * 60) + second) * 1000;

        checking = prefs.getBoolean("checking", checking);
        placing = prefs.getBoolean("placing", placing);
        reverting = prefs.getBoolean("reverting", reverting);
        firstrank = prefs.getBoolean("firstrank", firstrank);

        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        if (gameState == 0)
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


        board1[0][0] = (ImageView) findViewById(R.id.a1_1);
        board1[0][1] = (ImageView) findViewById(R.id.a2_1);
        board1[0][2] = (ImageView) findViewById(R.id.a3_1);
        board1[0][3] = (ImageView) findViewById(R.id.a4_1);
        board1[0][4] = (ImageView) findViewById(R.id.a5_1);
        board1[0][5] = (ImageView) findViewById(R.id.a6_1);
        board1[0][6] = (ImageView) findViewById(R.id.a7_1);
        board1[0][7] = (ImageView) findViewById(R.id.a8_1);

        board1[1][0] = (ImageView) findViewById(R.id.b1_1);
        board1[1][1] = (ImageView) findViewById(R.id.b2_1);
        board1[1][2] = (ImageView) findViewById(R.id.b3_1);
        board1[1][3] = (ImageView) findViewById(R.id.b4_1);
        board1[1][4] = (ImageView) findViewById(R.id.b5_1);
        board1[1][5] = (ImageView) findViewById(R.id.b6_1);
        board1[1][6] = (ImageView) findViewById(R.id.b7_1);
        board1[1][7] = (ImageView) findViewById(R.id.b8_1);

        board1[2][0] = (ImageView) findViewById(R.id.c1_1);
        board1[2][1] = (ImageView) findViewById(R.id.c2_1);
        board1[2][2] = (ImageView) findViewById(R.id.c3_1);
        board1[2][3] = (ImageView) findViewById(R.id.c4_1);
        board1[2][4] = (ImageView) findViewById(R.id.c5_1);
        board1[2][5] = (ImageView) findViewById(R.id.c6_1);
        board1[2][6] = (ImageView) findViewById(R.id.c7_1);
        board1[2][7] = (ImageView) findViewById(R.id.c8_1);

        board1[3][0] = (ImageView) findViewById(R.id.d1_1);
        board1[3][1] = (ImageView) findViewById(R.id.d2_1);
        board1[3][2] = (ImageView) findViewById(R.id.d3_1);
        board1[3][3] = (ImageView) findViewById(R.id.d4_1);
        board1[3][4] = (ImageView) findViewById(R.id.d5_1);
        board1[3][5] = (ImageView) findViewById(R.id.d6_1);
        board1[3][6] = (ImageView) findViewById(R.id.d7_1);
        board1[3][7] = (ImageView) findViewById(R.id.d8_1);

        board1[4][0] = (ImageView) findViewById(R.id.e1_1);
        board1[4][1] = (ImageView) findViewById(R.id.e2_1);
        board1[4][2] = (ImageView) findViewById(R.id.e3_1);
        board1[4][3] = (ImageView) findViewById(R.id.e4_1);
        board1[4][4] = (ImageView) findViewById(R.id.e5_1);
        board1[4][5] = (ImageView) findViewById(R.id.e6_1);
        board1[4][6] = (ImageView) findViewById(R.id.e7_1);
        board1[4][7] = (ImageView) findViewById(R.id.e8_1);

        board1[5][0] = (ImageView) findViewById(R.id.f1_1);
        board1[5][1] = (ImageView) findViewById(R.id.f2_1);
        board1[5][2] = (ImageView) findViewById(R.id.f3_1);
        board1[5][3] = (ImageView) findViewById(R.id.f4_1);
        board1[5][4] = (ImageView) findViewById(R.id.f5_1);
        board1[5][5] = (ImageView) findViewById(R.id.f6_1);
        board1[5][6] = (ImageView) findViewById(R.id.f7_1);
        board1[5][7] = (ImageView) findViewById(R.id.f8_1);

        board1[6][0] = (ImageView) findViewById(R.id.g1_1);
        board1[6][1] = (ImageView) findViewById(R.id.g2_1);
        board1[6][2] = (ImageView) findViewById(R.id.g3_1);
        board1[6][3] = (ImageView) findViewById(R.id.g4_1);
        board1[6][4] = (ImageView) findViewById(R.id.g5_1);
        board1[6][5] = (ImageView) findViewById(R.id.g6_1);
        board1[6][6] = (ImageView) findViewById(R.id.g7_1);
        board1[6][7] = (ImageView) findViewById(R.id.g8_1);

        board1[7][0] = (ImageView) findViewById(R.id.h1_1);
        board1[7][1] = (ImageView) findViewById(R.id.h2_1);
        board1[7][2] = (ImageView) findViewById(R.id.h3_1);
        board1[7][3] = (ImageView) findViewById(R.id.h4_1);
        board1[7][4] = (ImageView) findViewById(R.id.h5_1);
        board1[7][5] = (ImageView) findViewById(R.id.h6_1);
        board1[7][6] = (ImageView) findViewById(R.id.h7_1);
        board1[7][7] = (ImageView) findViewById(R.id.h8_1);

        board2[0][0] = (ImageView) findViewById(R.id.a1_2);
        board2[0][1] = (ImageView) findViewById(R.id.a2_2);
        board2[0][2] = (ImageView) findViewById(R.id.a3_2);
        board2[0][3] = (ImageView) findViewById(R.id.a4_2);
        board2[0][4] = (ImageView) findViewById(R.id.a5_2);
        board2[0][5] = (ImageView) findViewById(R.id.a6_2);
        board2[0][6] = (ImageView) findViewById(R.id.a7_2);
        board2[0][7] = (ImageView) findViewById(R.id.a8_2);

        board2[1][0] = (ImageView) findViewById(R.id.b1_2);
        board2[1][1] = (ImageView) findViewById(R.id.b2_2);
        board2[1][2] = (ImageView) findViewById(R.id.b3_2);
        board2[1][3] = (ImageView) findViewById(R.id.b4_2);
        board2[1][4] = (ImageView) findViewById(R.id.b5_2);
        board2[1][5] = (ImageView) findViewById(R.id.b6_2);
        board2[1][6] = (ImageView) findViewById(R.id.b7_2);
        board2[1][7] = (ImageView) findViewById(R.id.b8_2);

        board2[2][0] = (ImageView) findViewById(R.id.c1_2);
        board2[2][1] = (ImageView) findViewById(R.id.c2_2);
        board2[2][2] = (ImageView) findViewById(R.id.c3_2);
        board2[2][3] = (ImageView) findViewById(R.id.c4_2);
        board2[2][4] = (ImageView) findViewById(R.id.c5_2);
        board2[2][5] = (ImageView) findViewById(R.id.c6_2);
        board2[2][6] = (ImageView) findViewById(R.id.c7_2);
        board2[2][7] = (ImageView) findViewById(R.id.c8_2);

        board2[3][0] = (ImageView) findViewById(R.id.d1_2);
        board2[3][1] = (ImageView) findViewById(R.id.d2_2);
        board2[3][2] = (ImageView) findViewById(R.id.d3_2);
        board2[3][3] = (ImageView) findViewById(R.id.d4_2);
        board2[3][4] = (ImageView) findViewById(R.id.d5_2);
        board2[3][5] = (ImageView) findViewById(R.id.d6_2);
        board2[3][6] = (ImageView) findViewById(R.id.d7_2);
        board2[3][7] = (ImageView) findViewById(R.id.d8_2);

        board2[4][0] = (ImageView) findViewById(R.id.e1_2);
        board2[4][1] = (ImageView) findViewById(R.id.e2_2);
        board2[4][2] = (ImageView) findViewById(R.id.e3_2);
        board2[4][3] = (ImageView) findViewById(R.id.e4_2);
        board2[4][4] = (ImageView) findViewById(R.id.e5_2);
        board2[4][5] = (ImageView) findViewById(R.id.e6_2);
        board2[4][6] = (ImageView) findViewById(R.id.e7_2);
        board2[4][7] = (ImageView) findViewById(R.id.e8_2);

        board2[5][0] = (ImageView) findViewById(R.id.f1_2);
        board2[5][1] = (ImageView) findViewById(R.id.f2_2);
        board2[5][2] = (ImageView) findViewById(R.id.f3_2);
        board2[5][3] = (ImageView) findViewById(R.id.f4_2);
        board2[5][4] = (ImageView) findViewById(R.id.f5_2);
        board2[5][5] = (ImageView) findViewById(R.id.f6_2);
        board2[5][6] = (ImageView) findViewById(R.id.f7_2);
        board2[5][7] = (ImageView) findViewById(R.id.f8_2);

        board2[6][0] = (ImageView) findViewById(R.id.g1_2);
        board2[6][1] = (ImageView) findViewById(R.id.g2_2);
        board2[6][2] = (ImageView) findViewById(R.id.g3_2);
        board2[6][3] = (ImageView) findViewById(R.id.g4_2);
        board2[6][4] = (ImageView) findViewById(R.id.g5_2);
        board2[6][5] = (ImageView) findViewById(R.id.g6_2);
        board2[6][6] = (ImageView) findViewById(R.id.g7_2);
        board2[6][7] = (ImageView) findViewById(R.id.g8_2);

        board2[7][0] = (ImageView) findViewById(R.id.h1_2);
        board2[7][1] = (ImageView) findViewById(R.id.h2_2);
        board2[7][2] = (ImageView) findViewById(R.id.h3_2);
        board2[7][3] = (ImageView) findViewById(R.id.h4_2);
        board2[7][4] = (ImageView) findViewById(R.id.h5_2);
        board2[7][5] = (ImageView) findViewById(R.id.h6_2);
        board2[7][6] = (ImageView) findViewById(R.id.h7_2);
        board2[7][7] = (ImageView) findViewById(R.id.h8_2);


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
                        board1[i][j].setBackground(black);
                        board2[i][j].setBackground(black);
                    }
                    else
                    {
                        board1[i][j].setBackground(white);
                        board2[i][j].setBackground(white);
                    }
                }
                else
                {
                    if (j == 0 || j == 2 || j == 4 || j == 6)
                    {
                        board1[i][j].setBackground(white);
                        board2[i][j].setBackground(white);
                    }
                    else
                    {
                        board1[i][j].setBackground(black);
                        board2[i][j].setBackground(black);
                    }
                }
            }
        }

        final ScrollView scroll1 = (ScrollView) findViewById(R.id.scroll1);
        scroll1.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        final ScrollView scroll3 = (ScrollView) findViewById(R.id.scroll3);
        scroll3.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);

        final ScrollView scroll2 = (ScrollView) findViewById(R.id.scroll2);
        final ScrollView scroll4 = (ScrollView) findViewById(R.id.scroll4);


        final Button start = (Button) findViewById(R.id.start);


        board1[1][1].post(new Runnable()
        {

            @Override
            public void run()
            {

                for (int i = 0; i < 8; i++)
                {
                    for (int j = 0; j < 8; j++)
                    {
                        board1[i][j].getLayoutParams().width = board1[i][j].getHeight();
                        board2[i][j].getLayoutParams().width = board2[i][j].getHeight();
                    }
                }
                timer1.getLayoutParams().width = timer1.getHeight() * 2;
                timer2.getLayoutParams().width = timer2.getHeight() * 2;
                timer3.getLayoutParams().width = timer3.getHeight() * 2;
                timer4.getLayoutParams().width = timer4.getHeight() * 2;
                start.getLayoutParams().width = timer1.getHeight() * 2;
                options.getLayoutParams().width = timer1.getHeight() * 2;
                scroll2.fullScroll(View.FOCUS_DOWN);
                scroll4.fullScroll(View.FOCUS_DOWN);
            }
        });


        roster1[0] = (ImageView) findViewById(R.id.roster1_1);
        roster1[1] = (ImageView) findViewById(R.id.roster2_1);
        roster1[2] = (ImageView) findViewById(R.id.roster3_1);
        roster1[3] = (ImageView) findViewById(R.id.roster4_1);
        roster1[4] = (ImageView) findViewById(R.id.roster5_1);
        roster1[5] = (ImageView) findViewById(R.id.roster6_1);
        roster1[6] = (ImageView) findViewById(R.id.roster7_1);
        roster1[7] = (ImageView) findViewById(R.id.roster8_1);
        roster1[8] = (ImageView) findViewById(R.id.roster9_1);
        roster1[9] = (ImageView) findViewById(R.id.roster10_1);
        roster1[10] = (ImageView) findViewById(R.id.roster11_1);
        roster1[11] = (ImageView) findViewById(R.id.roster12_1);
        roster1[12] = (ImageView) findViewById(R.id.roster13_1);
        roster1[13] = (ImageView) findViewById(R.id.roster14_1);
        roster1[14] = (ImageView) findViewById(R.id.roster15_1);
        roster1[15] = (ImageView) findViewById(R.id.roster16_1);
        roster1[16] = (ImageView) findViewById(R.id.roster17_1);
        roster1[17] = (ImageView) findViewById(R.id.roster18_1);
        roster1[18] = (ImageView) findViewById(R.id.roster19_1);
        roster1[19] = (ImageView) findViewById(R.id.roster20_1);
        roster1[20] = (ImageView) findViewById(R.id.roster21_1);
        roster1[21] = (ImageView) findViewById(R.id.roster22_1);
        roster1[22] = (ImageView) findViewById(R.id.roster23_1);
        roster1[23] = (ImageView) findViewById(R.id.roster24_1);
        roster1[24] = (ImageView) findViewById(R.id.roster25_1);
        roster1[25] = (ImageView) findViewById(R.id.roster26_1);
        roster1[26] = (ImageView) findViewById(R.id.roster27_1);
        roster1[27] = (ImageView) findViewById(R.id.roster28_1);
        roster1[28] = (ImageView) findViewById(R.id.roster29_1);
        roster1[29] = (ImageView) findViewById(R.id.roster30_1);

        roster2[29] = (ImageView) findViewById(R.id.roster1_2);
        roster2[28] = (ImageView) findViewById(R.id.roster2_2);
        roster2[27] = (ImageView) findViewById(R.id.roster3_2);
        roster2[26] = (ImageView) findViewById(R.id.roster4_2);
        roster2[25] = (ImageView) findViewById(R.id.roster5_2);
        roster2[24] = (ImageView) findViewById(R.id.roster6_2);
        roster2[23] = (ImageView) findViewById(R.id.roster7_2);
        roster2[22] = (ImageView) findViewById(R.id.roster8_2);
        roster2[21] = (ImageView) findViewById(R.id.roster9_2);
        roster2[20] = (ImageView) findViewById(R.id.roster10_2);
        roster2[19] = (ImageView) findViewById(R.id.roster11_2);
        roster2[18] = (ImageView) findViewById(R.id.roster12_2);
        roster2[17] = (ImageView) findViewById(R.id.roster13_2);
        roster2[16] = (ImageView) findViewById(R.id.roster14_2);
        roster2[15] = (ImageView) findViewById(R.id.roster15_2);
        roster2[14] = (ImageView) findViewById(R.id.roster16_2);
        roster2[13] = (ImageView) findViewById(R.id.roster17_2);
        roster2[12] = (ImageView) findViewById(R.id.roster18_2);
        roster2[11] = (ImageView) findViewById(R.id.roster19_2);
        roster2[10] = (ImageView) findViewById(R.id.roster20_2);
        roster2[9] = (ImageView) findViewById(R.id.roster21_2);
        roster2[8] = (ImageView) findViewById(R.id.roster22_2);
        roster2[7] = (ImageView) findViewById(R.id.roster23_2);
        roster2[6] = (ImageView) findViewById(R.id.roster24_2);
        roster2[5] = (ImageView) findViewById(R.id.roster25_2);
        roster2[4] = (ImageView) findViewById(R.id.roster26_2);
        roster2[3] = (ImageView) findViewById(R.id.roster27_2);
        roster2[2] = (ImageView) findViewById(R.id.roster28_2);
        roster2[1] = (ImageView) findViewById(R.id.roster29_2);
        roster2[0] = (ImageView) findViewById(R.id.roster30_2);


        roster3[0] = (ImageView) findViewById(R.id.roster1_3);
        roster3[1] = (ImageView) findViewById(R.id.roster2_3);
        roster3[2] = (ImageView) findViewById(R.id.roster3_3);
        roster3[3] = (ImageView) findViewById(R.id.roster4_3);
        roster3[4] = (ImageView) findViewById(R.id.roster5_3);
        roster3[5] = (ImageView) findViewById(R.id.roster6_3);
        roster3[6] = (ImageView) findViewById(R.id.roster7_3);
        roster3[7] = (ImageView) findViewById(R.id.roster8_3);
        roster3[8] = (ImageView) findViewById(R.id.roster9_3);
        roster3[9] = (ImageView) findViewById(R.id.roster10_3);
        roster3[10] = (ImageView) findViewById(R.id.roster11_3);
        roster3[11] = (ImageView) findViewById(R.id.roster12_3);
        roster3[12] = (ImageView) findViewById(R.id.roster13_3);
        roster3[13] = (ImageView) findViewById(R.id.roster14_3);
        roster3[14] = (ImageView) findViewById(R.id.roster15_3);
        roster3[15] = (ImageView) findViewById(R.id.roster16_3);
        roster3[16] = (ImageView) findViewById(R.id.roster17_3);
        roster3[17] = (ImageView) findViewById(R.id.roster18_3);
        roster3[18] = (ImageView) findViewById(R.id.roster19_3);
        roster3[19] = (ImageView) findViewById(R.id.roster20_3);
        roster3[20] = (ImageView) findViewById(R.id.roster21_3);
        roster3[21] = (ImageView) findViewById(R.id.roster22_3);
        roster3[22] = (ImageView) findViewById(R.id.roster23_3);
        roster3[23] = (ImageView) findViewById(R.id.roster24_3);
        roster3[24] = (ImageView) findViewById(R.id.roster25_3);
        roster3[25] = (ImageView) findViewById(R.id.roster26_3);
        roster3[26] = (ImageView) findViewById(R.id.roster27_3);
        roster3[27] = (ImageView) findViewById(R.id.roster28_3);
        roster3[28] = (ImageView) findViewById(R.id.roster29_3);
        roster3[29] = (ImageView) findViewById(R.id.roster30_3);

        roster4[29] = (ImageView) findViewById(R.id.roster1_4);
        roster4[28] = (ImageView) findViewById(R.id.roster2_4);
        roster4[27] = (ImageView) findViewById(R.id.roster3_4);
        roster4[26] = (ImageView) findViewById(R.id.roster4_4);
        roster4[25] = (ImageView) findViewById(R.id.roster5_4);
        roster4[24] = (ImageView) findViewById(R.id.roster6_4);
        roster4[23] = (ImageView) findViewById(R.id.roster7_4);
        roster4[22] = (ImageView) findViewById(R.id.roster8_4);
        roster4[21] = (ImageView) findViewById(R.id.roster9_4);
        roster4[20] = (ImageView) findViewById(R.id.roster10_4);
        roster4[19] = (ImageView) findViewById(R.id.roster11_4);
        roster4[18] = (ImageView) findViewById(R.id.roster12_4);
        roster4[17] = (ImageView) findViewById(R.id.roster13_4);
        roster4[16] = (ImageView) findViewById(R.id.roster14_4);
        roster4[15] = (ImageView) findViewById(R.id.roster15_4);
        roster4[14] = (ImageView) findViewById(R.id.roster16_4);
        roster4[13] = (ImageView) findViewById(R.id.roster17_4);
        roster4[12] = (ImageView) findViewById(R.id.roster18_4);
        roster4[11] = (ImageView) findViewById(R.id.roster19_4);
        roster4[10] = (ImageView) findViewById(R.id.roster20_4);
        roster4[9] = (ImageView) findViewById(R.id.roster21_4);
        roster4[8] = (ImageView) findViewById(R.id.roster22_4);
        roster4[7] = (ImageView) findViewById(R.id.roster23_4);
        roster4[6] = (ImageView) findViewById(R.id.roster24_4);
        roster4[5] = (ImageView) findViewById(R.id.roster25_4);
        roster4[4] = (ImageView) findViewById(R.id.roster26_4);
        roster4[3] = (ImageView) findViewById(R.id.roster27_4);
        roster4[2] = (ImageView) findViewById(R.id.roster28_4);
        roster4[1] = (ImageView) findViewById(R.id.roster29_4);
        roster4[0] = (ImageView) findViewById(R.id.roster30_4);

        roster1[1].post(new Runnable()
        {

            @Override
            public void run()
            {

                for (int i = 0; i < 30; i++)
                {
                    roster1[i].getLayoutParams().height = board1[1][1].getHeight();
                    roster1[i].getLayoutParams().width = board1[1][1].getHeight();
                    roster2[i].getLayoutParams().height = board1[1][1].getHeight();
                    roster2[i].getLayoutParams().width = board1[1][1].getHeight();
                    roster3[i].getLayoutParams().height = board1[1][1].getHeight();
                    roster3[i].getLayoutParams().width = board1[1][1].getHeight();
                    roster4[i].getLayoutParams().height = board1[1][1].getHeight();
                    roster4[i].getLayoutParams().width = board1[1][1].getHeight();
                }
            }
        });

        timer3.setRotation(180);
        timer4.setRotation(180);
        timer1.setTextColor(0xFF848484);
        timer2.setTextColor(0xFF848484);
        timer3.setTextColor(0xFF848484);
        timer4.setTextColor(0xFF848484);

        setPieces(board1, positions1);
        setPieces(board2, positions2);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                enP[i][j] = "0000";
            }
        }

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (gameState == 2)
                {
                    whiteTurn1 = turnSave1;
                    whiteTurn2 = turnSave2;
                    setActions(board1, positions1);
                    setActions(board2, positions2);
                    gameState = 1;
                    start.setText("Pause");
                    startAI();
                    return;
                }
                if (gameState == 1)
                {
                    clean(board1, positions1);
                    clean(board2, positions2);
                    turnSave1 = whiteTurn1;
                    whiteTurn1 = 3;
                    turnSave2 = whiteTurn2;
                    whiteTurn2 = 3;
                    nuke(board1, positions1);
                    nuke(board2, positions2);
                    gameState = 2;
                    start.setText("Resume");
                }
                if (gameState == 0)
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
                    resetBooleans();
                    clean(board1, positions1);
                    clean(board2, positions2);
                    setPieces(board1, positions1);
                    setPieces(board2, positions2);
                    setActions(board1, positions1);
                    setActions(board2, positions2);
                    startTimers();
                    start.setText("Pause");
                    gameState = 1;
                    startAI();
                }
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        dialog_margin = (int) screenWidth / 5;

    }

    private void nsd()
    {

    }


    private void newGame()
    {
        whiteTurn1 = 3;
        whiteTurn2 = 3;
        nuke(board1, positions1);
        nuke(board2, positions2);
        final Button start = (Button) findViewById(R.id.start);
        start.setText("Start");
        gameState = 0;
        clean(board1, positions1);
        clean(board2, positions2);
        setPieces(board1, positions1);
        setPieces(board2, positions2);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("m:ss");
        df.setTimeZone(tz);
        final String time = df.format(new Date(milliseconds));
        final TextView timer1 = (TextView) findViewById(R.id.timer1);
        final TextView timer2 = (TextView) findViewById(R.id.timer2);
        final TextView timer3 = (TextView) findViewById(R.id.timer3);
        final TextView timer4 = (TextView) findViewById(R.id.timer4);
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

    private void startSettings()
    {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);

        menu_code = 0;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (menu_code == 0)
                {

                }
                final LinearLayout mainmenu = (LinearLayout) findViewById(R.id.mainmenu);
                final TextView timer1 = (TextView) findViewById(R.id.timer1);
                final TextView timer2 = (TextView) findViewById(R.id.timer2);
                final TextView timer3 = (TextView) findViewById(R.id.timer3);
                final TextView timer4 = (TextView) findViewById(R.id.timer4);

                TimeZone tz = TimeZone.getTimeZone("UTC");
                SimpleDateFormat df = new SimpleDateFormat("m:ss");
                df.setTimeZone(tz);
                final String time = df.format(new Date(milliseconds));
                if (gameState == 0)
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

                if (menu_code == 2)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mainmenu.setVisibility(View.INVISIBLE);
                            newGame();
                        }
                    });

                }
                if (menu_code == 3)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mainmenu.setVisibility(View.VISIBLE);
                        }
                    });
                }


            }
        }).start();
    }

    private void startTimers()
    {
        final TextView timer1 = (TextView) findViewById(R.id.timer1);
        final TextView timer2 = (TextView) findViewById(R.id.timer2);
        final TextView timer3 = (TextView) findViewById(R.id.timer3);
        final TextView timer4 = (TextView) findViewById(R.id.timer4);

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
                    if (whiteTurn1 == 1)
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
                    if (gameState == 0)
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
                    if (whiteTurn1 == 2)
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
                    if (gameState == 0)
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
                    if (whiteTurn2 == 1)
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
                    if (gameState == 0)
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
                    if (whiteTurn2 == 2)
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
                    if (gameState == 0)
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

    private static void setPieces(ImageView[][] board, Piece[][] positions)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                positions[i][j] = new Empty();
            }
        }
        for (int i = 0; i < 8; i++)
        {
            positions[i][1] = new Pawn("white");
        }
        for (int i = 0; i < 8; i++)
        {
            positions[i][6] = new Pawn("black");
        }
        positions[0][0] = new Rook("white");
        positions[7][0] = new Rook("white");
        positions[0][7] = new Rook("black");
        positions[7][7] = new Rook("black");
        positions[1][0] = new Knight("white");
        positions[6][0] = new Knight("white");
        positions[1][7] = new Knight("black");
        positions[6][7] = new Knight("black");
        positions[2][0] = new Bishop("white");
        positions[5][0] = new Bishop("white");
        positions[2][7] = new Bishop("black");
        positions[5][7] = new Bishop("black");
        positions[3][0] = new Queen("white");
        positions[3][7] = new Queen("black");
        positions[4][0] = new King("white");
        positions[4][7] = new King("black");

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {

                board[i][j].setImageResource(positions[i][j].getResID());

            }
        }
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board1[j][i].setRotation(90);
                board1[7 - j][7 - i].setRotation(270);
                board2[j][i].setRotation(270);
                board2[7 - j][7 - i].setRotation(90);
            }
        }

        for (int i = 0; i < 30; i++)
        {
            roster1p[i] = new Empty();
            roster1[i].setImageResource(roster1p[i].getResID());
            roster2p[i] = new Empty();
            roster2[i].setImageResource(roster2p[i].getResID());
            roster3p[i] = new Empty();
            roster3[i].setImageResource(roster3p[i].getResID());
            roster4p[i] = new Empty();
            roster4[i].setImageResource(roster4p[i].getResID());
        }

    }

    private void clean(ImageView[][] board, Piece[][] positions)
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
                if (positions[i][j].backgroundColor.equals("D"))
                {
                    board[i][j].setImageResource(android.R.color.transparent);
                    positions[i][j].backgroundColor = "0";
                }
                if (positions[i][j].backgroundColor.equals("Y"))
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
                    positions[i][j].backgroundColor = "0";

                }
                if (positions[i][j].backgroundColor.equals("R"))
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
                    positions[i][j].backgroundColor = "0";
                }
                if (positions[i][j].backgroundColor.equals("B"))
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
                    positions[i][j].backgroundColor = "0";
                }
            }
        }
        int light = 0x80B0B0B0;
        int dark = 0x808E8E8E;
        for (int i = 0; i < 30; i++)
        {
            if (board(board, positions) == 1)
            {
                if (roster1p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster1[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster1[i].setBackgroundColor(dark);
                    }
                    roster1p[i].backgroundColor = "0";
                }
                if (roster2p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster2[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster2[i].setBackgroundColor(dark);
                    }
                    roster2p[i].backgroundColor = "0";
                }
            }
            else
            {
                if (roster3p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster3[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster3[i].setBackgroundColor(dark);
                    }
                    roster3p[i].backgroundColor = "0";
                }
                if (roster4p[i].backgroundColor.equals("Y"))
                {
                    if (i % 2 == 0)
                    {
                        roster4[i].setBackgroundColor(light);
                    }
                    else
                    {
                        roster4[i].setBackgroundColor(dark);
                    }
                    roster4p[i].backgroundColor = "0";
                }
            }
        }

    }

    private void setActions(final ImageView[][] board, final Piece[][] positions)
    {
        clean(board, positions);
        castleCheck(board, positions);
        kingStillStanding(board, positions);
        if (whiteInCheck(board, positions))
        {
            setWhiteCheckConditions(board, positions);
        }
        if (blackInCheck(board, positions))
        {
            setBlackCheckConditions(board, positions);
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
                        if (board(board, positions) == 1)
                        {
                            if (whiteTurn1 == 1 && position1)
                            {
                                if (positions[x][y].color.equals("white"))
                                {
                                    setPiece(board, positions, x, y);
                                    ID = positions[x][y].type + "1";
                                }
//                                if (positions[x][y].substring(0, 2).equals("WP"))
//                                {
//                                    setPawn(board, positions, x, y);
//                                    ID = "P1";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WR"))
//                                {
//                                    setRook(board, positions, x, y);
//                                    ID = "R1";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WN"))
//                                {
//                                    setKnight(board, positions, x, y);
//                                    ID = "N1";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WB"))
//                                {
//                                    setBishop(board, positions, x, y);
//                                    ID = "B1";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WQ"))
//                                {
//                                    setQueen(board, positions, x, y);
//                                    ID = "Q1";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WK"))
//                                {
//                                    setKing(board, positions, x, y);
//                                    ID = "K1";
//                                }
//
                            }
                            if (whiteTurn1 == 2 && position2)
                            {
                                if (positions[x][y].color.equals("black"))
                                {
                                    setPiece(board, positions, x, y);
                                    ID = positions[x][y].type + "2";
                                }
//                                if (positions[x][y].substring(0, 2).equals("BP"))
//                                {
//                                    setBPawn(board, positions, x, y);
//                                    ID = "P2";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BR"))
//                                {
//                                    setBRook(board, positions, x, y);
//                                    ID = "R2";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BN"))
//                                {
//                                    setBKnight(board, positions, x, y);
//                                    ID = "N2";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BB"))
//                                {
//                                    setBBishop(board, positions, x, y);
//                                    ID = "B2";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BQ"))
//                                {
//                                    setBQueen(board, positions, x, y);
//                                    ID = "Q2";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BK"))
//                                {
//                                    setBKing(board, positions, x, y);
//                                    ID = "K2";
//                                }
                            }
                        }
                        else
                        {
                            if (whiteTurn2 == 1 && position4)
                            {
                                if (positions[x][y].color.equals("white"))
                                {
                                    setPiece(board, positions, x, y);
                                    ID = positions[x][y].type + "3";
                                }
//                                if (positions[x][y].substring(0, 2).equals("WP"))
//                                {
//                                    setPawn(board, positions, x, y);
//                                    ID = "P3";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WR"))
//                                {
//                                    setRook(board, positions, x, y);
//                                    ID = "R3";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WN"))
//                                {
//                                    setKnight(board, positions, x, y);
//                                    ID = "N3";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WB"))
//                                {
//                                    setBishop(board, positions, x, y);
//                                    ID = "B3";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WQ"))
//                                {
//                                    setQueen(board, positions, x, y);
//                                    ID = "Q3";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("WK"))
//                                {
//                                    setKing(board, positions, x, y);
//                                    ID = "K3";
//                                }
                            }
                            if (whiteTurn2 == 2 && position3)
                            {
                                if (positions[x][y].color.equals("black"))
                                {
                                    setPiece(board, positions, x, y);
                                    ID = positions[x][y].type + "4";
                                }
//                                if (positions[x][y].substring(0, 2).equals("BP"))
//                                {
//                                    setBPawn(board, positions, x, y);
//                                    ID = "P4";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BR"))
//                                {
//                                    setBRook(board, positions, x, y);
//                                    ID = "R4";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BN"))
//                                {
//                                    setBKnight(board, positions, x, y);
//                                    ID = "N4";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BB"))
//                                {
//                                    setBBishop(board, positions, x, y);
//                                    ID = "B4";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BQ"))
//                                {
//                                    setBQueen(board, positions, x, y);
//                                    ID = "Q4";
//                                }
//                                if (positions[x][y].substring(0, 2).equals("BK"))
//                                {
//                                    setBKing(board, positions, x, y);
//                                    ID = "K4";
//                                }
//                            }
                            }
                        }

                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                        {
                            View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(view, ID);

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
                    if (whiteTurn1 == 1 && !roster1p[j].empty && position1)
                    {
                        setRosterPiece(board1, positions1, roster1, roster1p, j);
                        ID = roster1p[j].type + "1";
                    }

                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(view, ID);

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
                    if (!roster2p[j].empty && whiteTurn1 == 2 && position2)
                    {
                        setRosterPiece(board1, positions1, roster2, roster2p, j);
                        ID = roster2p[j].type + "2";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(view, ID);

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
                    if (whiteTurn2 == 2 && !roster3p[j].empty && position3)
                    {
                        setRosterPiece(board2, positions2, roster3, roster3p, j);
                        ID = roster3p[j].type + "4";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(view, ID);

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
                    if (!roster4p[j].empty && whiteTurn2 == 1 && position4)
                    {
                        setRosterPiece(board2, positions2, roster4, roster4p, j);
                        ID = roster4p[j].type + "3";
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !ID.equals("00"))
                    {
                        View.DragShadowBuilder myShadowBuilder = new MyShadowBuilder(view, ID);

                        ClipData data = ClipData.newPlainText("", "");
                        view.startDrag(data, myShadowBuilder, view, 0);
                    }
                    return true;
                }
            });
        }
    }

    private void setRosterPiece(ImageView[][] board, Piece[][] positions, ImageView[] roster, Piece[] rosterp, int i)
    {
        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
        {
            setActions(board, positions);
        }
        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
        {
            setActions(board, positions);
        }

        if (board(board, positions) == 1 && searchingForCheckmate1)
        {
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 8; y++)
                {
                    if (positions[x][y].empty)
                    {
                        moveRoster(board, positions, roster, rosterp, i, x, y);
                    }
                }
            }
            return;
        }


        if (board(board, positions) == 2 && searchingForCheckmate2)
        {
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 8; y++)
                {
                    if (positions[x][y].empty)
                    {
                        moveRoster(board, positions, roster, rosterp, i, x, y);
                    }
                }
            }
            return;
        }
        if (rosterp[i].type.equals("pawn"))
        {
            for (int x = 0; x < 8; x++)
            {
                if (firstrank)
                {
                    if (rosterp[i].color.equals("white"))
                    {
                        for (int y = 0; y < 7; y++)
                        {
                            if (positions[x][y].empty)
                            {
                                moveRoster(board, positions, roster, rosterp, i, x, y);
                            }
                        }
                    }
                    if (rosterp[i].color.equals("black"))
                    {
                        for (int y = 1; y < 8; y++)
                        {
                            if (positions[x][y].empty)
                            {
                                moveRoster(board, positions, roster, rosterp, i, x, y);
                            }
                        }
                    }

                }
                else
                {
                    for (int y = 1; y < 7; y++)
                    {
                        if (positions[x][y].empty)
                        {
                            moveRoster(board, positions, roster, rosterp, i, x, y);
                        }
                    }
                }

            }
        }
        else
        {
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 8; y++)
                {
                    if (positions[x][y].empty)
                    {
                        moveRoster(board, positions, roster, rosterp, i, x, y);
                    }
                }
            }
        }

    }

    private void moveRoster(final ImageView[][] board, final Piece[][] positions, final ImageView[] roster, final Piece[] rosterp, final int i, final int x, final int y)
    {
        if (!placing)
        {
            Piece old = positions[x][y];
            positions[x][y] = rosterp[i];

            if (board(board, positions) == 1)
            {
                if (whiteTurn1 == 1)
                {
                    if (blackInCheck(board,positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
            }
            positions[x][y] = old;
        }
        if (checking || searchingForCheckmate1 || searchingForCheckmate2)
        {
            Piece old = positions[x][y];
            positions[x][y] = rosterp[i];

            if (board(board, positions) == 1)
            {
                if (whiteTurn1 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = old;
                        return;
                    }
                }
            }
            positions[x][y] = old;
        }
        if (searchingForCheckmate1)
        {
            checkmate1 = false;
            return;
        }
        if (searchingForCheckmate2)
        {
            checkmate2 = false;
            return;
        }


        if (board(board, positions) == 1)
        {
            for (int j = 0; j < 2; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, -1, i, x, y, "drop", rosterp, j);
                    return;
                }
            }
        }
        if (board(board, positions) == 2)
        {
            for (int j = 2; j < 4; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, -1, i, x, y, "drop", rosterp, j);
                    return;
                }
            }
        }


        roster[i].setBackgroundColor(Color.YELLOW);
        if (!rosterp[i].backgroundColor.equals("Y"))
        {
            rosterp[i].backgroundColor =("Y");
        }
        board[x][y].setImageResource(R.mipmap.dot);
        positions[x][y].backgroundColor =("D");


        board[x][y].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                rosterShit(board, positions, roster, rosterp, i, x, y);
                return true;
            }
        });
        board[x][y].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x][y].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        int p = x;
                        int j = y;
                        if (p == 0 || p == 2 || p == 4 || p == 6)
                        {
                            if (j == 0 || j == 2 || j == 4 || j == 6)
                            {
                                board[p][j].setBackground(black);
                            }
                            else
                            {
                                board[p][j].setBackground(white);
                            }
                        }
                        else
                        {
                            if (j == 0 || j == 2 || j == 4 || j == 6)
                            {
                                board[p][j].setBackground(white);
                            }
                            else
                            {
                                board[p][j].setBackground(black);
                            }
                        }
                        break;

                    case DragEvent.ACTION_DROP:
                        p = x;
                        j = y;
                        if (p == 0 || p == 2 || p == 4 || p == 6)
                        {
                            if (j == 0 || j == 2 || j == 4 || j == 6)
                            {
                                board[p][j].setBackground(black);
                            }
                            else
                            {
                                board[p][j].setBackground(white);
                            }
                        }
                        else
                        {
                            if (j == 0 || j == 2 || j == 4 || j == 6)
                            {
                                board[p][j].setBackground(white);
                            }
                            else
                            {
                                board[p][j].setBackground(black);
                            }
                        }
                        rosterShit(board, positions, roster, rosterp, i, x, y);
                        break;
                }

                return true;
            }

        });
    }

    private void rosterShit(ImageView[][] board, Piece[][] positions, ImageView[] roster, Piece[] rosterp, int i, int x, int y)
    {
        if (rosterp[i].empty)
        {
            whiteTurn1 = 3;
            whiteTurn2 = 3;
            gameState = 2;
            return;
        }
        clean(board, positions);
        if (board(board, positions) == 1)
        {
            if (rosterp[i].equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave1 = 2;
                }
                else
                {
                    whiteTurn1 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave1 = 1;
                }
                else
                {
                    whiteTurn1 = 1;
                }

            }
            board1Turn++;
        }
        else
        {
            if (rosterp[i].color.equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave2 = 2;
                }
                else
                {
                    whiteTurn2 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave2 = 1;
                }
                else
                {
                    whiteTurn2 = 1;
                }
            }
            board2Turn++;
        }


        positions[x][y] = rosterp[i];
        rosterp[i] = new Empty();
        updateImage(board, positions, x, y);

        board[x][y].setRotation(roster[i].getRotation());
        roster[i].setRotation(0);
        roster[i].setImageResource(android.R.color.transparent);

        pawnCheck(board, positions);
        setActions(board, positions);
    }

    private void setPiece(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
        {
            setActions(board, positions);
        }
        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
        {
            setActions(board, positions);
        }

        Set<Move> moves = positions[x][y].getMoves(board, positions, x, y);
        for (Move m : moves)
        {
            if (m.x == m.x1 && m.y == m.y1)
            {
                System.out.println("awadaf");
            }
            if (m.type.equals("move")) move(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("take")) take(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("whiteKingCastle")) whiteKingCastle(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("whiteQueenCastle")) whiteQueenCastle(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("blackKingCastle")) blackKingCastle(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("blackQueenCastle")) blackQueenCastle(m.board, m.positions, m.x, m.y, m.x1, m.y1);
            if (m.type.equals("whiteEnP")) whiteEnP(m.board, m.positions, m.x, m.x1);
            if (m.type.equals("blackEnP")) blackEnP(m.board, m.positions, m.x, m.x1);

        }
    }


//    private void setPawn(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (y < 7)
//        {
//            if (positions[x][y + 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y + 1);
//                if (y == 1)
//                {
//                    if (positions[x][y + 2].substring(1, 2).equals("0"))
//                    {
//                        move(board, positions, x, y, x, y + 2);
//                    }
//                }
//            }
//        }
//        if (x < 7 && y < 7)
//        {
//            if (positions[x + 1][y + 1].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x + 1, y + 1);
//            }
//            if (y == 4 && positions[x + 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x + 1][1].substring(0, 1).equals("1") && enP[x + 1][1].substring(1, enP[x + 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x + 1][3].substring(0, 1).equals("1") && enP[x + 1][3].substring(1, enP[x + 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x + 1);
//                    }
//                }
//            }
//        }
//        if (x > 0 && y < 7)
//        {
//            if (positions[x - 1][y + 1].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x - 1, y + 1);
//            }
//            if (y == 4 && positions[x - 1][y].substring(0, 2).equals("BP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x - 1][1].substring(0, 1).equals("1") && enP[x - 1][1].substring(1, enP[x - 1][1].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x - 1][3].substring(0, 1).equals("1") && enP[x - 1][3].substring(1, enP[x - 1][3].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        whiteEnP(board, positions, x, x - 1);
//                    }
//                }
//            }
//        }
//
//    }
//
//    private void setRook(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        for (int i = y + 1; i < 8; i++)
//        {
//            for (int j = y + 1; j < i; j++)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = y - 1; i > -1; i--)
//        {
//            for (int j = y - 1; j > i; j--)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x + 1; i < 8; i++)
//        {
//            for (int j = x + 1; j < i; j++)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x - 1; i > -1; i--)
//        {
//            for (int j = x - 1; j > i; j--)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//    }
//
//    private void setKnight(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (x + 1 < 8 && y + 2 < 8)
//        {
//            if (!positions[x + 1][y + 2].substring(0, 1).equals("W"))
//            {
//                if (positions[x + 1][y + 2].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 1, y + 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 1, y + 2);
//                }
//            }
//        }
//        if (x + 2 < 8 && y + 1 < 8)
//        {
//            if (!positions[x + 2][y + 1].substring(0, 1).equals("W"))
//            {
//                if (positions[x + 2][y + 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 2, y + 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 2, y + 1);
//                }
//            }
//        }
//        if (x - 1 > -1 && y + 2 < 8)
//        {
//            if (!positions[x - 1][y + 2].substring(0, 1).equals("W"))
//            {
//                if (positions[x - 1][y + 2].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 1, y + 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 1, y + 2);
//                }
//            }
//        }
//        if (x - 2 > -1 && y + 1 < 8)
//        {
//            if (!positions[x - 2][y + 1].substring(0, 1).equals("W"))
//            {
//                if (positions[x - 2][y + 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 2, y + 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 2, y + 1);
//                }
//            }
//        }
//        if (x + 1 < 8 && y - 2 > -1)
//        {
//            if (!positions[x + 1][y - 2].substring(0, 1).equals("W"))
//            {
//                if (positions[x + 1][y - 2].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 1, y - 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 1, y - 2);
//                }
//            }
//        }
//        if (x + 2 < 8 && y - 1 > -1)
//        {
//            if (!positions[x + 2][y - 1].substring(0, 1).equals("W"))
//            {
//                if (positions[x + 2][y - 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 2, y - 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 2, y - 1);
//                }
//            }
//        }
//        if (x - 1 > -1 && y - 2 > -1)
//        {
//            if (!positions[x - 1][y - 2].substring(0, 1).equals("W"))
//            {
//                if (positions[x - 1][y - 2].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 1, y - 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 1, y - 2);
//                }
//            }
//        }
//        if (x - 2 > -1 && y - 1 > -1)
//        {
//            if (!positions[x - 2][y - 1].substring(0, 1).equals("W"))
//            {
//                if (positions[x - 2][y - 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 2, y - 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 2, y - 1);
//                }
//            }
//        }
//    }
//
//    private void setBishop(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        int z = x;
//        if (y > x)
//        {
//            z = y;
//        }
//        for (int i = 1; i < 8 - z; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y + i);
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y + i);
//                }
//            }
//        }
//        inbetween = false;
//        if (x < y)
//        {
//            z = x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y - i);
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - x < y)
//        {
//            z = 7 - x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y - i);
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - y < x)
//        {
//            z = 7 - y;
//        }
//        else
//        {
//            z = x;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y + i);
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y + i);
//                }
//            }
//        }
//    }
//
//    private void setQueen(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        for (int i = y + 1; i < 8; i++)
//        {
//            for (int j = y + 1; j < i; j++)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = y - 1; i > -1; i--)
//        {
//            for (int j = y - 1; j > i; j--)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x + 1; i < 8; i++)
//        {
//            for (int j = x + 1; j < i; j++)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x - 1; i > -1; i--)
//        {
//            for (int j = x - 1; j > i; j--)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        int z = x;
//        if (y > x)
//        {
//            z = y;
//        }
//        for (int i = 1; i < 8 - z; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y + i);
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y + i);
//                }
//            }
//        }
//        inbetween = false;
//        if (x < y)
//        {
//            z = x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y - i);
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - x < y)
//        {
//            z = 7 - x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y - i);
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - y < x)
//        {
//            z = 7 - y;
//        }
//        else
//        {
//            z = x;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y + i);
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("B"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y + i);
//                }
//            }
//        }
//    }
//
//    private void setKing(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (x + 1 < 8)
//        {
//            if (positions[x + 1][y].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x + 1, y);
//                if (positions[6][0].substring(1, 2).equals("0") && positions[4][0].substring(1, 2).equals("K"))
//                {
//                    positions[5][0] = "W" + positions[5][0].substring(1, 4);
//                    positions[6][0] = "W" + positions[6][0].substring(1, 4);
//                    if (board(board, positions) == 1)
//                    {
//
//                        if (whiteCastleKing1 && positions[6][0].substring(1, 2).equals("0") && !checkCheck(positions, 4, 0) && !checkCheck(positions, 5, 0) && !checkCheck(positions, 6, 0))
//                        {
//                            positions[5][0] = "0" + positions[5][0].substring(1, 4);
//                            positions[6][0] = "0" + positions[6][0].substring(1, 4);
//                            whiteKingCastle(board, positions, x, y, 6, 0);
//                        }
//                    }
//                    else
//                    {
//                        if (whiteCastleKing2 && positions[6][0].substring(1, 2).equals("0") && !checkCheck(positions, 4, 0) && !checkCheck(positions, 5, 0) && !checkCheck(positions, 6, 0))
//                        {
//                            positions[5][0] = "0" + positions[5][0].substring(1, 4);
//                            positions[6][0] = "0" + positions[6][0].substring(1, 4);
//                            whiteKingCastle(board, positions, x, y, 6, 0);
//                        }
//                    }
//                    positions[5][0] = "0" + positions[5][0].substring(1, 4);
//                    positions[6][0] = "0" + positions[6][0].substring(1, 4);
//                }
//
//            }
//            if (positions[x + 1][y].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x + 1, y);
//            }
//            if (y + 1 < 8)
//            {
//                if (positions[x + 1][y + 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x + 1, y + 1);
//                }
//                if (positions[x + 1][y + 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 1, y + 1);
//                }
//            }
//            if (y - 1 > -1)
//            {
//                if (positions[x + 1][y - 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x + 1, y - 1);
//                }
//                if (positions[x + 1][y - 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x + 1, y - 1);
//                }
//            }
//        }
//        if (x - 1 > -1)
//        {
//            if (positions[x - 1][y].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x - 1, y);
//                if (positions[2][0].substring(1, 2).equals("0") && positions[1][0].substring(1, 2).equals("0") && positions[4][0].substring(1, 2).equals("K"))
//                {
//                    positions[1][0] = "W" + positions[1][0].substring(1, 4);
//                    positions[2][0] = "W" + positions[2][0].substring(1, 4);
//                    positions[3][0] = "W" + positions[3][0].substring(1, 4);
//                    if (board(board, positions) == 1)
//                    {
//
//                        if (whiteCastleQueen1 && positions[2][0].substring(1, 2).equals("0") && !checkCheck(positions, 1, 0) && !checkCheck(positions, 2, 0) && !checkCheck(positions, 3, 0) && !checkCheck(positions, 4, 0))
//                        {
//                            positions[1][0] = "0" + positions[1][0].substring(1, 4);
//                            positions[2][0] = "0" + positions[2][0].substring(1, 4);
//                            positions[3][0] = "0" + positions[3][0].substring(1, 4);
//                            whiteQueenCastle(board, positions, x, y, 2, 0);
//                        }
//                    }
//                    else
//                    {
//                        if (whiteCastleQueen2 && positions[2][0].substring(1, 2).equals("0") && !checkCheck(positions, 1, 0) && !checkCheck(positions, 2, 0) && !checkCheck(positions, 3, 0) && !checkCheck(positions, 4, 0))
//                        {
//                            positions[1][0] = "0" + positions[1][0].substring(1, 4);
//                            positions[2][0] = "0" + positions[2][0].substring(1, 4);
//                            positions[3][0] = "0" + positions[3][0].substring(1, 4);
//                            whiteQueenCastle(board, positions, x, y, 2, 0);
//                        }
//                    }
//                    positions[1][0] = "0" + positions[1][0].substring(1, 4);
//                    positions[2][0] = "0" + positions[2][0].substring(1, 4);
//                    positions[3][0] = "0" + positions[3][0].substring(1, 4);
//                }
//
//            }
//            if (positions[x - 1][y].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x - 1, y);
//            }
//            if (y + 1 < 8)
//            {
//                if (positions[x - 1][y + 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x - 1, y + 1);
//                }
//                if (positions[x - 1][y + 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 1, y + 1);
//                }
//            }
//            if (y - 1 > -1)
//            {
//                if (positions[x - 1][y - 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x - 1, y - 1);
//                }
//                if (positions[x - 1][y - 1].substring(0, 1).equals("B"))
//                {
//                    take(board, positions, x, y, x - 1, y - 1);
//                }
//            }
//        }
//        if (y + 1 < 8)
//        {
//            if (positions[x][y + 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y + 1);
//            }
//            if (positions[x][y + 1].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x, y + 1);
//            }
//        }
//        if (y - 1 > -1)
//        {
//            if (positions[x][y - 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y - 1);
//            }
//            if (positions[x][y - 1].substring(0, 1).equals("B"))
//            {
//                take(board, positions, x, y, x, y - 1);
//            }
//        }
//    }

//
//    private void setBPawn(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (y > 0)
//        {
//            if (positions[x][y - 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y - 1);
//                if (y == 6)
//                {
//                    if (positions[x][y - 2].substring(1, 2).equals("0"))
//                    {
//                        move(board, positions, x, y, x, y - 2);
//                    }
//                }
//            }
//        }
//        if (x < 7 && y > 0)
//        {
//            if (positions[x + 1][y - 1].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x + 1, y - 1);
//            }
//            if (y == 3 && positions[x + 1][y].substring(0, 2).equals("WP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x + 1][0].substring(0, 1).equals("1") && enP[x + 1][0].substring(1, enP[x + 1][0].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        blackEnP(board, positions, x, x + 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x + 1][2].substring(0, 1).equals("1") && enP[x + 1][2].substring(1, enP[x + 1][2].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        blackEnP(board, positions, x, x + 1);
//                    }
//                }
//            }
//        }
//        if (x > 0 && y > 0)
//        {
//            if (positions[x - 1][y - 1].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x - 1, y - 1);
//            }
//            if (y == 3 && positions[x - 1][y].substring(0, 2).equals("WP"))
//            {
//                if (board(board, positions) == 1)
//                {
//                    if (enP[x - 1][0].substring(0, 1).equals("1") && enP[x - 1][0].substring(1, enP[x - 1][0].length()).equals(Integer.toString(board1Turn)))
//                    {
//                        blackEnP(board, positions, x, x - 1);
//                    }
//                }
//                if (board(board, positions) == 2)
//                {
//                    if (enP[x - 1][2].substring(0, 1).equals("1") && enP[x - 1][2].substring(1, enP[x - 1][2].length()).equals(Integer.toString(board2Turn)))
//                    {
//                        blackEnP(board, positions, x, x - 1);
//                    }
//                }
//            }
//        }
//    }
//
//    private void setBKnight(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (x + 1 < 8 && y + 2 < 8)
//        {
//            if (!positions[x + 1][y + 2].substring(0, 1).equals("B"))
//            {
//                if (positions[x + 1][y + 2].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 1, y + 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 1, y + 2);
//                }
//            }
//        }
//        if (x + 2 < 8 && y + 1 < 8)
//        {
//            if (!positions[x + 2][y + 1].substring(0, 1).equals("B"))
//            {
//                if (positions[x + 2][y + 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 2, y + 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 2, y + 1);
//                }
//            }
//        }
//        if (x - 1 > -1 && y + 2 < 8)
//        {
//            if (!positions[x - 1][y + 2].substring(0, 1).equals("B"))
//            {
//                if (positions[x - 1][y + 2].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 1, y + 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 1, y + 2);
//                }
//            }
//        }
//        if (x - 2 > -1 && y + 1 < 8)
//        {
//            if (!positions[x - 2][y + 1].substring(0, 1).equals("B"))
//            {
//                if (positions[x - 2][y + 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 2, y + 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 2, y + 1);
//                }
//            }
//        }
//        if (x + 1 < 8 && y - 2 > -1)
//        {
//            if (!positions[x + 1][y - 2].substring(0, 1).equals("B"))
//            {
//                if (positions[x + 1][y - 2].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 1, y - 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 1, y - 2);
//                }
//            }
//        }
//        if (x + 2 < 8 && y - 1 > -1)
//        {
//            if (!positions[x + 2][y - 1].substring(0, 1).equals("B"))
//            {
//                if (positions[x + 2][y - 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 2, y - 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x + 2, y - 1);
//                }
//            }
//        }
//        if (x - 1 > -1 && y - 2 > -1)
//        {
//            if (!positions[x - 1][y - 2].substring(0, 1).equals("B"))
//            {
//                if (positions[x - 1][y - 2].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 1, y - 2);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 1, y - 2);
//                }
//            }
//        }
//        if (x - 2 > -1 && y - 1 > -1)
//        {
//            if (!positions[x - 2][y - 1].substring(0, 1).equals("B"))
//            {
//                if (positions[x - 2][y - 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 2, y - 1);
//                }
//                else
//                {
//                    move(board, positions, x, y, x - 2, y - 1);
//                }
//            }
//        }
//    }
//
//    private void setBRook(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        for (int i = y + 1; i < 8; i++)
//        {
//            for (int j = y + 1; j < i; j++)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = y - 1; i > -1; i--)
//        {
//            for (int j = y - 1; j > i; j--)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x + 1; i < 8; i++)
//        {
//            for (int j = x + 1; j < i; j++)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x - 1; i > -1; i--)
//        {
//            for (int j = x - 1; j > i; j--)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//    }
//
//    private void setBBishop(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        int z = x;
//        if (y > x)
//        {
//            z = y;
//        }
//        for (int i = 1; i < 8 - z; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y + i);
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y + i);
//                }
//            }
//        }
//        inbetween = false;
//        if (x < y)
//        {
//            z = x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y - i);
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - x < y)
//        {
//            z = 7 - x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y - i);
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - y < x)
//        {
//            z = 7 - y;
//        }
//        else
//        {
//            z = x;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y + i);
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y + i);
//                }
//            }
//        }
//    }
//
//    private void setBQueen(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        boolean inbetween = false;
//        for (int i = y + 1; i < 8; i++)
//        {
//            for (int j = y + 1; j < i; j++)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = y - 1; i > -1; i--)
//        {
//            for (int j = y - 1; j > i; j--)
//            {
//                if (!positions[x][j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x][i].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x, i);
//                }
//            }
//            if (positions[x][i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x, i);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x + 1; i < 8; i++)
//        {
//            for (int j = x + 1; j < i; j++)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        for (int i = x - 1; i > -1; i--)
//        {
//            for (int j = x - 1; j > i; j--)
//            {
//                if (!positions[j][y].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[i][y].substring(1, 2).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, i, y);
//                }
//            }
//            if (positions[i][y].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, i, y);
//                }
//            }
//        }
//        inbetween = false;
//        int z = x;
//        if (y > x)
//        {
//            z = y;
//        }
//        for (int i = 1; i < 8 - z; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y + i);
//                }
//            }
//            if (positions[x + i][y + i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y + i);
//                }
//            }
//        }
//        inbetween = false;
//        if (x < y)
//        {
//            z = x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y - i);
//                }
//            }
//            if (positions[x - i][y - i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - x < y)
//        {
//            z = 7 - x;
//        }
//        else
//        {
//            z = y;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x + j][y - j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x + i, y - i);
//                }
//            }
//            if (positions[x + i][y - i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x + i, y - i);
//                }
//            }
//        }
//        inbetween = false;
//        if (7 - y < x)
//        {
//            z = 7 - y;
//        }
//        else
//        {
//            z = x;
//        }
//        for (int i = 1; i < z + 1; i++)
//        {
//            for (int j = 1; j < i; j++)
//            {
//                if (!positions[x - j][y + j].substring(0, 1).equals("0"))
//                {
//                    inbetween = true;
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("0"))
//            {
//                if (!inbetween)
//                {
//                    move(board, positions, x, y, x - i, y + i);
//                }
//            }
//            if (positions[x - i][y + i].substring(0, 1).equals("W"))
//            {
//                if (!inbetween)
//                {
//                    take(board, positions, x, y, x - i, y + i);
//                }
//            }
//        }
//    }
//
//    private void setBKing(ImageView[][] board, String[][] positions, int x, int y)
//    {
//        if (board(board, positions) == 1 && !searchingForCheckmate1 && !CPUsearch[0] && !CPUsearch[1])
//        {
//            setActions(board, positions);
//        }
//        if (board(board, positions) == 2 && !searchingForCheckmate2 && !CPUsearch[2] && !CPUsearch[3])
//        {
//            setActions(board, positions);
//        }
//
//        if (x + 1 < 8)
//        {
//            if (positions[x + 1][y].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x + 1, y);
//                if (positions[6][7].substring(1, 2).equals("0") && positions[4][7].substring(1, 2).equals("K"))
//                {
//                    positions[5][7] = "B" + positions[5][7].substring(1, 4);
//                    positions[6][7] = "B" + positions[6][7].substring(1, 4);
//                    if (board(board, positions) == 1)
//                    {
//                        if (blackCastleKing1 && positions[6][7].substring(1, 2).equals("0") && !checkCheck(positions, 4, 7) && !checkCheck(positions, 5, 7) && !checkCheck(positions, 6, 7))
//                        {
//                            positions[5][7] = "0" + positions[5][7].substring(1, 4);
//                            positions[6][7] = "0" + positions[6][7].substring(1, 4);
//                            blackKingCastle(board, positions, x, y, 6, 7);
//                        }
//                    }
//                    else
//                    {
//                        if (blackCastleKing2 && positions[6][7].substring(1, 2).equals("0") && !checkCheck(positions, 4, 7) && !checkCheck(positions, 5, 7) && !checkCheck(positions, 6, 7))
//                        {
//                            positions[5][7] = "0" + positions[5][7].substring(1, 4);
//                            positions[6][7] = "0" + positions[6][7].substring(1, 4);
//                            blackKingCastle(board, positions, x, y, 6, 7);
//                        }
//                    }
//                    positions[5][7] = "0" + positions[5][7].substring(1, 4);
//                    positions[6][7] = "0" + positions[6][7].substring(1, 4);
//                }
//            }
//            if (positions[x + 1][y].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x + 1, y);
//            }
//            if (y + 1 < 8)
//            {
//                if (positions[x + 1][y + 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x + 1, y + 1);
//                }
//                if (positions[x + 1][y + 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 1, y + 1);
//                }
//            }
//            if (y - 1 > -1)
//            {
//                if (positions[x + 1][y - 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x + 1, y - 1);
//                }
//                if (positions[x + 1][y - 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x + 1, y - 1);
//                }
//            }
//        }
//        if (x - 1 > -1)
//        {
//            if (positions[x - 1][y].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x - 1, y);
//                if (positions[2][7].substring(1, 2).equals("0") && positions[1][7].substring(1, 2).equals("0") && positions[4][7].substring(1, 2).equals("K"))
//                {
//                    positions[1][7] = "B" + positions[1][7].substring(1, 4);
//                    positions[2][7] = "B" + positions[2][7].substring(1, 4);
//                    positions[3][7] = "B" + positions[3][7].substring(1, 4);
//                    if (board(board, positions) == 1)
//                    {
//
//                        if (blackCastleQueen1 && positions[2][7].substring(1, 2).equals("0") && !checkCheck(positions, 1, 7) && !checkCheck(positions, 2, 7) && !checkCheck(positions, 3, 7) && !checkCheck(positions, 4, 7))
//                        {
//                            positions[1][7] = "0" + positions[1][7].substring(1, 4);
//                            positions[2][7] = "0" + positions[2][7].substring(1, 4);
//                            positions[3][7] = "0" + positions[3][7].substring(1, 4);
//                            blackQueenCastle(board, positions, x, y, 2, 7);
//                        }
//                    }
//                    else
//                    {
//                        if (blackCastleQueen2 && positions[2][7].substring(1, 2).equals("0") && !checkCheck(positions, 1, 7) && !checkCheck(positions, 2, 7) && !checkCheck(positions, 3, 7) && !checkCheck(positions, 4, 7))
//                        {
//                            positions[1][7] = "0" + positions[1][7].substring(1, 4);
//                            positions[2][7] = "0" + positions[2][7].substring(1, 4);
//                            positions[3][7] = "0" + positions[3][7].substring(1, 4);
//                            blackQueenCastle(board, positions, x, y, 2, 7);
//                        }
//                    }
//                    positions[1][7] = "0" + positions[1][7].substring(1, 4);
//                    positions[2][7] = "0" + positions[2][7].substring(1, 4);
//                    positions[3][7] = "0" + positions[3][7].substring(1, 4);
//                }
//
//            }
//            if (positions[x - 1][y].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x - 1, y);
//            }
//            if (y + 1 < 8)
//            {
//                if (positions[x - 1][y + 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x - 1, y + 1);
//                }
//                if (positions[x - 1][y + 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 1, y + 1);
//                }
//            }
//            if (y - 1 > -1)
//            {
//                if (positions[x - 1][y - 1].substring(1, 2).equals("0"))
//                {
//                    move(board, positions, x, y, x - 1, y - 1);
//                }
//                if (positions[x - 1][y - 1].substring(0, 1).equals("W"))
//                {
//                    take(board, positions, x, y, x - 1, y - 1);
//                }
//            }
//        }
//        if (y + 1 < 8)
//        {
//            if (positions[x][y + 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y + 1);
//            }
//            if (positions[x][y + 1].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x, y + 1);
//            }
//        }
//        if (y - 1 > -1)
//        {
//            if (positions[x][y - 1].substring(1, 2).equals("0"))
//            {
//                move(board, positions, x, y, x, y - 1);
//            }
//            if (positions[x][y - 1].substring(0, 1).equals("W"))
//            {
//                take(board, positions, x, y, x, y - 1);
//            }
//        }
//    }


    public static int board(ImageView[][] board, Piece[][] positions)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals("white"))
                {
                    if (board[i][j].getRotation() == 90)
                    {
                        return 1;
                    }
                    else
                    {
                        return 2;
                    }
                }

            }
        }
        return 0;
    }

    private void move(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {

        if (checking || searchingForCheckmate1 || searchingForCheckmate2)
        {
            Piece old = positions[x1][y1];
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            if (board(board, positions) == 1)
            {

                if (whiteTurn1 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (whiteInCheck(board ,positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
            }
            positions[x][y] = positions[x1][y1];
            positions[x1][y1] = old;
        }

        if (searchingForCheckmate1)
        {
            checkmate1 = false;
            return;
        }
        if (searchingForCheckmate2)
        {
            checkmate2 = false;
            return;
        }

        if (board(board, positions) == 1)
        {
            for (int j = 0; j < 2; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, x, y, x1, y1, "move", null, j);
                    return;
                }
            }
        }
        if (board(board, positions) == 2)
        {
            for (int j = 2; j < 4; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, x, y, x1, y1, "move", null, j);
                    return;
                }
            }
        }


        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y1].setImageResource(R.mipmap.dot);
        positions[x1][y1].backgroundColor = "D";

        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                changeShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y1;
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
                        changeShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });

    }

    private void changeShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        //?? not sur what this is for, possible a fringe case catcher?
        if (positions[x][y].empty)
        {
            whiteTurn1 = 3;
            whiteTurn2 = 3;
            gameState = 2;
            return;
        }
        clean(board, positions);
        turnChange(board, positions, x, y);
        if (positions[x][y].color.equals("white") && positions[x][y].type.equals("pawn") && y == 1 && y1 == 3)
        {
            if (board(board, positions) == 1)
            {
                enP[x][0] = "1" + Integer.toString(board1Turn);
            }
            if (board(board, positions) == 2)
            {
                enP[x][2] = "1" + Integer.toString(board2Turn);
            }
        }
        if (positions[x][y].color.equals("black") && positions[x][y].type.equals("pawn") && y == 6 && y1 == 4)
        {
            if (board(board, positions) == 1)
            {
                enP[x][1] = "1" + Integer.toString(board1Turn);
            }
            if (board(board, positions) == 2)
            {
                enP[x][3] = "1" + Integer.toString(board2Turn);
            }
        }


        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
        updateImage(board, positions, x1, y1);

        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);

        pawnCheck(board, positions);
        setActions(board, positions);

    }

    private void updateImage(ImageView[][] board, Piece[][] positions, int x1, int y1)
    {
        board[x1][y1].setImageResource(positions[x1][y1].getResID());

//        if (positions[x1][y1].substring(0, 2).equals("WP"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.pawn);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("WR"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.rook);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("WN"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.knight);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("WB"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bishop);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("WQ"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.queen);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("WK"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.king);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BP"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bpawn);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BR"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.brook);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BN"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bknight);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BB"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bbishop);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BQ"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bqueen);
//        }
//        if (positions[x1][y1].substring(0, 2).equals("BK"))
//        {
//            board[x1][y1].setImageResource(R.mipmap.bking);
//        }
    }


    private void take(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {
        if (checking || searchingForCheckmate1 || searchingForCheckmate2)
        {
            Piece old = positions[x1][y1];
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            if (board(board, positions) == 1)
            {
                if (whiteTurn1 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {

                    if (blackInCheck(board, positions))
                    {

                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        return;
                    }
                }
            }
            positions[x][y] = positions[x1][y1];
            positions[x1][y1] = old;
        }


        if (searchingForCheckmate1)
        {
            checkmate1 = false;
            return;
        }
        if (searchingForCheckmate2)
        {
            checkmate2 = false;
            return;
        }


        if (board(board, positions) == 1)
        {
            for (int j = 0; j < 2; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, x, y, x1, y1, "take", null, j);
                    return;
                }
            }
        }
        if (board(board, positions) == 2)
        {
            for (int j = 2; j < 4; j++)
            {
                if (CPUsearch[j])
                {
                    rate(board, positions, x, y, x1, y1, "take", null, j);
                    return;
                }
            }
        }


        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor =  "Y";
        }
        board[x1][y1].setBackgroundColor(Color.RED);
        positions[x1][y1].backgroundColor =  "R";

        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                takeShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFF7F7F);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        board[x1][y1].setBackgroundColor(0xFFFF0000);
                        break;

                    case DragEvent.ACTION_DROP:
                        takeShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });
    }

    private void takeShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        if (positions[x][y].empty)
        {
            whiteTurn1 = 3;
            whiteTurn2 = 3;
            gameState = 2;
            return;
        }
        clean(board, positions);
        turnChange(board, positions, x, y);
        addToRoster(board, positions, x1, y1);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();

        updateImage(board, positions, x1, y1);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);

        pawnCheck(board, positions);
        setActions(board, positions);

    }

    private void addToRoster(ImageView[][] board, Piece[][] positions, int x1, int y1)
    {
        if (board(board, positions) == 1)
        {
            if (positions[x1][y1].color.equals("white"))
            {
                addToRosterShit(board, positions, roster4, roster4p, x1, y1, 270);
            }
            if (positions[x1][y1].color.equals("black"))
            {
                addToRosterShit(board, positions, roster3, roster3p, x1, y1, 90);
            }
        }
        else
        {
            if (positions[x1][y1].color.equals("white"))
            {
                addToRosterShit(board, positions, roster1, roster1p, x1, y1, 90);
            }
            if (positions[x1][y1].color.equals("black"))
            {
                addToRosterShit(board, positions, roster2, roster2p, x1, y1, 270);
            }
        }

    }

    private void addToRosterShit(final ImageView[][] board, final Piece[][] positions, ImageView[] roster, Piece[] rosterp, int x1, int y1, int rotation)
    {
        for (int i = 0; i < 30; i++)
        {
            if (rosterp[i].empty)
            {
                if (reverting)
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
//                if (positions[x1][y1].substring(0, 2).equals("WP"))
//                {
//                    roster[i].setImageResource(R.mipmap.pawn);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("WR"))
//                {
//                    roster[i].setImageResource(R.mipmap.rook);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("WN"))
//                {
//                    roster[i].setImageResource(R.mipmap.knight);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("WB"))
//                {
//                    roster[i].setImageResource(R.mipmap.bishop);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("WQ"))
//                {
//                    roster[i].setImageResource(R.mipmap.queen);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("BP"))
//                {
//                    roster[i].setImageResource(R.mipmap.bpawn);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("BR"))
//                {
//                    roster[i].setImageResource(R.mipmap.brook);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("BN"))
//                {
//                    roster[i].setImageResource(R.mipmap.bknight);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("BB"))
//                {
//                    roster[i].setImageResource(R.mipmap.bbishop);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//                if (positions[x1][y1].substring(0, 2).equals("BQ"))
//                {
//                    roster[i].setImageResource(R.mipmap.bqueen);
//                    roster[i].setRotation(rotation);
//                    rosterp[i] = positions[x1][y1];
//                    break;
//                }
//
            }

        }
    }

    private void whiteKingCastle(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {
        if (checking)
        {
            positions[4][0] = new Empty();
            positions[5][0] = new Rook("white");
            positions[6][0] = new King("white");
            positions[7][0] = new Empty();
            if (whiteInCheck(board, positions))
            {
                positions[4][0] = new King("white");
                positions[4][0].backgroundColor = "Y";
                positions[5][0] = new Empty();
                positions[5][0].backgroundColor = "D";
                positions[6][0] = new Empty();
                positions[7][0] = new Rook("white");
                return;
            }
            positions[4][0] = new King("white");
            positions[4][0].backgroundColor = "Y";
            positions[5][0] = new Empty();
            positions[5][0].backgroundColor = "D";
            positions[6][0] = new Empty();
            positions[7][0] = new Rook("white");

        }

        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y1].setImageResource(R.mipmap.dot);
        positions[x1][y1].backgroundColor = "D";


        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                whiteKingCastleShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y1;
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
                        whiteKingCastleShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });

    }

    private void whiteKingCastleShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        clean(board, positions);
        turnChange(board, positions, x, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
        positions[7][0] = new Empty();
        positions[5][0] = new Rook("white");
        board[x1][y1].setImageResource(R.mipmap.king);
        board[5][0].setImageResource(R.mipmap.rook);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        board[5][0].setRotation(board[7][0].getRotation());
        board[7][0].setRotation(0);
        board[7][0].setImageResource(android.R.color.transparent);
        setActions(board, positions);

    }

    private void whiteQueenCastle(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {
        if (checking)
        {
            positions[0][0] = new Empty();
            positions[2][0] = new King("white");
            positions[3][0] = new Rook("white");
            positions[4][0] = new Empty();
            if (whiteInCheck(board, positions))
            {
                positions[0][0] = new Rook("white");
                positions[2][0] = new Empty();
                positions[3][0] = new Empty();
                positions[3][0].backgroundColor = "D";
                positions[4][0] = new King("white");
                positions[4][0].backgroundColor = "Y";
                return;
            }
            positions[0][0] = new Rook("white");
            positions[2][0] = new Empty();
            positions[3][0] = new Empty();
            positions[3][0].backgroundColor = "D";
            positions[4][0] = new King("white");
            positions[4][0].backgroundColor = "Y";

        }

        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y1].setImageResource(R.mipmap.dot);
        positions[x1][y1].backgroundColor = "D";


        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                whiteQueenCastleShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y1;
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
                        whiteQueenCastleShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });
    }

    private void whiteQueenCastleShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        clean(board, positions);
        turnChange(board, positions, x, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
        positions[0][0] = new Empty();
        positions[3][0] = new Rook("white");
        board[x1][y1].setImageResource(R.mipmap.king);
        board[3][0].setImageResource(R.mipmap.rook);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        board[3][0].setRotation(board[0][0].getRotation());
        board[0][0].setRotation(0);
        board[0][0].setImageResource(android.R.color.transparent);
        setActions(board, positions);

    }

    private void blackKingCastle(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {
        if (checking)
        {
            positions[4][7] = new Empty();
            positions[5][7] = new Rook("black");
            positions[6][7] = new King("black");
            positions[7][7] = new Empty();
            if (blackInCheck(board, positions))
            {
                positions[4][7] = new King("black");
                positions[4][7].backgroundColor = "Y";
                positions[5][7] = new Empty();
                positions[5][7].backgroundColor = "D";
                positions[6][7] = new Empty();
                positions[7][7] = new Rook("black");
                return;
            }
            positions[4][7] = new King("black");
            positions[4][7].backgroundColor = "Y";
            positions[5][7] = new Empty();
            positions[5][7].backgroundColor = "D";
            positions[6][7] = new Empty();
            positions[7][7] = new Rook("black");
        }

        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y1].setImageResource(R.mipmap.dot);
        positions[x1][y1].backgroundColor =  "D";


        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                blackKingCastleShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y1;
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
                        blackKingCastleShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });

    }

    private void blackKingCastleShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        clean(board, positions);
        turnChange(board, positions, x, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
        positions[7][7] = new Empty();
        positions[5][7] = new Rook("black");
        board[x1][y1].setImageResource(R.mipmap.bking);
        board[5][7].setImageResource(R.mipmap.brook);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        board[5][7].setRotation(board[7][7].getRotation());
        board[7][7].setRotation(0);
        board[7][7].setImageResource(android.R.color.transparent);
        setActions(board, positions);

    }

    private void blackQueenCastle(final ImageView[][] board, final Piece[][] positions, final int x, final int y, final int x1, final int y1)
    {
        if (checking)
        {
            positions[0][7] = new Empty();
            positions[2][7] = new King("black");
            positions[3][7] = new Rook("black");
            positions[4][7] = new Empty();
            if (blackInCheck(board, positions))
            {
                positions[0][7] = new Rook("black");
                positions[2][7] = new Empty();
                positions[3][7] = new Empty();
                positions[3][7].backgroundColor = "D";
                positions[4][7] = new King("black");
                positions[4][7].backgroundColor = "Y";
                return;
            }
            positions[0][7] = new Rook("black");
            positions[2][7] = new Empty();
            positions[3][7] = new Empty();
            positions[3][7].backgroundColor = "D";
            positions[4][7] = new King("black");
            positions[4][7].backgroundColor = "Y";
        }

        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor ="Y";
        }
        board[x1][y1].setImageResource(R.mipmap.dot);
        positions[x1][y1].backgroundColor = "D";


        board[x1][y1].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                blackQueenCastleShit(board, positions, x, y, x1, y1);
                return true;
            }
        });
        board[x1][y1].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y1].setBackgroundColor(0xFFFFFF00);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y1;
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
                        blackQueenCastleShit(board, positions, x, y, x1, y1);
                        break;
                }

                return true;
            }

        });
    }

    private void blackQueenCastleShit(final ImageView[][] board, final Piece[][] positions, int x, int y, final int x1, final int y1)
    {
        clean(board, positions);
        turnChange(board, positions, x, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();
        positions[0][7] = new Empty();
        positions[3][7] = new Rook("black");
        board[x1][y1].setImageResource(R.mipmap.bking);
        board[3][7].setImageResource(R.mipmap.brook);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        board[3][7].setRotation(board[0][7].getRotation());
        board[0][7].setRotation(0);
        board[0][7].setImageResource(android.R.color.transparent);
        setActions(board, positions);

    }

    private void whiteEnP(final ImageView[][] board, final Piece[][] positions, final int x, final int x1)
    {
        final int y = 4;
        final int y1 = 5;
        if (checking || searchingForCheckmate1 || searchingForCheckmate2)
        {
            Piece old = positions[x1][y1];
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[x1][y] = new Empty();
            if (board(board, positions) == 1)
            {
                if (whiteTurn1 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("black");
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {

                    if (blackInCheck(board, positions))
                    {

                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("black");
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("black");
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (blackInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("black");
                        return;
                    }
                }
            }
            positions[x][y] = positions[x1][y1];
            positions[x1][y1] = old;
            positions[x1][y] = new Pawn("black");
        }
        if (searchingForCheckmate1)
        {
            checkmate1 = false;
            return;
        }
        if (searchingForCheckmate2)
        {
            checkmate2 = false;
            return;
        }
        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y].setBackgroundColor(Color.RED);
        positions[x1][y].backgroundColor ="R";

        //NOT SURE WHY I USED A SET ON CLICK LISTENER HERE
//        board[x1][y].setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                whiteEnPShit(board, positions, x, x1);
//            }
//        });
        board[x1][y].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                whiteEnPShit(board, positions,x,x1);
                return true;
            }
        });
        board[x1][y].setOnDragListener(new View.OnDragListener()
        {
            @Override
            public boolean onDrag(View v, DragEvent event)
            {

                int dragEvent = event.getAction();
                //TextView dropText = (TextView) v;
                BitmapDrawable black = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.black, 10, 10));
                BitmapDrawable white = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.drawable.white, 10, 10));
                switch (dragEvent)
                {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        board[x1][y].setBackgroundColor(0xFFFF7F7F);
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        int i = x1;
                        int j = y;
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
                        break;

                    case DragEvent.ACTION_DROP:
                        i = x1;
                        j = y;
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
                        whiteEnPShit(board, positions, x, x1);
                        break;
                }

                return true;
            }

        });
    }

    private void whiteEnPShit(final ImageView[][] board, final Piece[][] positions, final int x, final int x1)
    {
        int y = 4;
        int y1 = 5;
        clean(board, positions);
        turnChange(board, positions, x, y);
        addToRoster(board, positions, x1, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();

        updateImage(board, positions, x1, y1);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        positions[x1][y] = new Empty();
        board[x1][y].setRotation(0);
        board[x1][y].setImageResource(android.R.color.transparent);

        setActions(board, positions);
    }

    private void blackEnP(final ImageView[][] board, final Piece[][] positions, final int x, final int x1)
    {
        final int y = 3;
        final int y1 = 2;
        if (checking || searchingForCheckmate1 || searchingForCheckmate2)
        {
            Piece old = positions[x1][y1];
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
            positions[x1][y] = new Empty();
            if (board(board, positions) == 1)
            {
                if (whiteTurn1 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("white");
                        return;
                    }
                }
                if (whiteTurn1 == 2)
                {

                    if (blackInCheck(board, positions))
                    {

                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("white");
                        return;
                    }
                }
            }
            else
            {
                if (whiteTurn2 == 1)
                {
                    if (whiteInCheck(board, positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("white");
                        return;
                    }
                }
                if (whiteTurn2 == 2)
                {
                    if (blackInCheck(board ,positions))
                    {
                        positions[x][y] = positions[x1][y1];
                        positions[x1][y1] = old;
                        positions[x1][y] = new Pawn("white");
                        return;
                    }
                }
            }
            positions[x][y] = positions[x1][y1];
            positions[x1][y1] = old;
            positions[x1][y] = new Pawn("white");
        }
        if (searchingForCheckmate1)
        {
            checkmate1 = false;
            return;
        }
        if (searchingForCheckmate2)
        {
            checkmate2 = false;
            return;
        }

        if (CPUsearch[0] || CPUsearch[1] || CPUsearch[2] || CPUsearch[3])
        {
            //rate(board, positions, x, y, x1, y1);
            return;
        }

        board[x][y].setBackgroundColor(Color.YELLOW);
        if (!positions[x][y].backgroundColor.equals("Y"))
        {
            positions[x][y].backgroundColor = "Y";
        }
        board[x1][y].setBackgroundColor(Color.RED);
        positions[x1][y].backgroundColor = "R";

        board[x1][y].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                blackEnPShit(board, positions, x, x1);
            }
        });
    }

    private void blackEnPShit(final ImageView[][] board, final Piece[][] positions, final int x, final int x1)
    {
        int y = 3;
        int y1 = 2;
        clean(board, positions);
        if (board(board, positions) == 1)
        {
            if (positions[x][y].color.equals("W"))
            {
                whiteTurn1 = 2;
            }
            else
            {
                whiteTurn1 = 1;
            }

        }
        else
        {
            if (positions[x][y].color.equals("W"))
            {
                whiteTurn2 = 2;
            }
            else
            {
                whiteTurn2 = 1;
            }
        }
        addToRoster(board, positions, x1, y);
        positions[x1][y1] = positions[x][y];
        positions[x][y] = new Empty();

        updateImage(board, positions, x1, y1);
        board[x1][y1].setRotation(board[x][y].getRotation());
        board[x][y].setRotation(0);
        board[x][y].setImageResource(android.R.color.transparent);
        positions[x1][y] = new Empty();
        board[x1][y].setRotation(0);
        board[x1][y].setImageResource(android.R.color.transparent);

        setActions(board, positions);
    }

    private void castleCheck(ImageView[][] board, Piece[][] positions)
    {
//        if (board(board, positions) == 1)
//        {
//            if (!positions[0][0].substring(0, 2).equals("WR"))
//            {
//                whiteCastleQueen1 = false;
//            }
//            if (!positions[7][0].substring(0, 2).equals("WR"))
//            {
//                whiteCastleKing1 = false;
//            }
//            if (!positions[4][0].substring(0, 2).equals("WK"))
//            {
//                whiteCastleKing1 = false;
//                whiteCastleQueen1 = false;
//            }
//            if (!positions[0][7].substring(0, 2).equals("BR"))
//            {
//                blackCastleQueen1 = false;
//            }
//            if (!positions[7][7].substring(0, 2).equals("BR"))
//            {
//                blackCastleKing1 = false;
//            }
//            if (!positions[4][7].substring(0, 2).equals("BK"))
//            {
//                blackCastleKing1 = false;
//                blackCastleQueen1 = false;
//            }
//        }
//        else
//        {
//            if (!positions[0][0].substring(0, 2).equals("WR"))
//            {
//                whiteCastleQueen2 = false;
//            }
//            if (!positions[7][0].substring(0, 2).equals("WR"))
//            {
//                whiteCastleKing2 = false;
//            }
//            if (!positions[4][0].substring(0, 2).equals("WK"))
//            {
//                whiteCastleKing2 = false;
//                whiteCastleQueen2 = false;
//            }
//            if (!positions[0][7].substring(0, 2).equals("BR"))
//            {
//                blackCastleQueen2 = false;
//            }
//            if (!positions[7][7].substring(0, 2).equals("BR"))
//            {
//                blackCastleKing2 = false;
//            }
//            if (!positions[4][7].substring(0, 2).equals("BK"))
//            {
//                blackCastleKing2 = false;
//                blackCastleQueen2 = false;
//            }
//        }
    }

    private void pawnCheck(final ImageView[][] board, final Piece[][] positions)
    {
//        final LinearLayout pawnOptions1 = (LinearLayout) findViewById(R.id.pawnOptions1);
//        final LinearLayout pawnOptions2 = (LinearLayout) findViewById(R.id.pawnOptions2);
//        final Button queen1 = (Button) findViewById(R.id.queen1);
//        Button queen2 = (Button) findViewById(R.id.queen2);
//        Button rook1 = (Button) findViewById(R.id.rook1);
//        Button rook2 = (Button) findViewById(R.id.rook2);
//        Button bishop1 = (Button) findViewById(R.id.bishop1);
//        Button bishop2 = (Button) findViewById(R.id.bishop2);
//        Button knight1 = (Button) findViewById(R.id.knight1);
//        Button knight2 = (Button) findViewById(R.id.knight2);
//
//        final ImageView square = (ImageView) findViewById(R.id.a1_1);
//        int width = square.getWidth();
//
//        pawnOptions1.getLayoutParams().width = width * 8;
//        pawnOptions1.getLayoutParams().height = width * 8;
//        pawnOptions2.getLayoutParams().width = width * 8;
//        pawnOptions2.getLayoutParams().height = width * 8;
//
//        if (board(board, positions) == 1)
//        {
//            if (whiteTurn1 == 2)
//            {
//                for (int i = 0; i < 8; i++)
//                {
//                    final int x = i;
//                    if (positions[i][7].substring(0, 2).equals("WP"))
//                    {
//                        whiteTurn1 = 3;
//                        nuke(board, positions);
//                        pawnOptions1.setVisibility(View.VISIBLE);
//                        pawnOptions1.setRotation(90);
//                        if (gameState == 1)
//                        {
//                            queen1.setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View v)
//                                {
//                                    positions[x][7] = "WQP0";
//                                    board[x][7].setImageResource(R.mipmap.queen);
//                                    if (gameState == 1)
//                                    {
//                                        pawnOptions1.setVisibility(View.INVISIBLE);
//                                        whiteTurn1 = 2;
//                                        setActions(board, positions);
//                                    }
//                                }
//                            });
//                            rook1.setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View v)
//                                {
//                                    positions[x][7] = "WRP0";
//                                    board[x][7].setImageResource(R.mipmap.rook);
//                                    if (gameState == 1)
//                                    {
//                                        pawnOptions1.setVisibility(View.INVISIBLE);
//                                        whiteTurn1 = 2;
//                                        setActions(board, positions);
//                                    }
//                                }
//                            });
//                            bishop1.setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View v)
//                                {
//                                    positions[x][7] = "WBP0";
//                                    board[x][7].setImageResource(R.mipmap.bishop);
//                                    if (gameState == 1)
//                                    {
//                                        pawnOptions1.setVisibility(View.INVISIBLE);
//                                        whiteTurn1 = 2;
//                                        setActions(board, positions);
//                                    }
//                                }
//                            });
//                            knight1.setOnClickListener(new View.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(View v)
//                                {
//                                    positions[x][7] = "WNP0";
//                                    board[x][7].setImageResource(R.mipmap.knight);
//                                    if (gameState == 1)
//                                    {
//                                        pawnOptions1.setVisibility(View.INVISIBLE);
//                                        whiteTurn1 = 2;
//                                        setActions(board, positions);
//                                    }
//
//                                }
//                            });
//                            if (!position1)
//                            {
//                                positions[x][7] = "WQP0";
//                                board[x][7].setImageResource(R.mipmap.queen);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions1.setVisibility(View.INVISIBLE);
//                                    whiteTurn1 = 2;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (whiteTurn1 == 1)
//            {
//                for (int i = 0; i < 8; i++)
//                {
//                    final int x = i;
//                    if (positions[i][0].substring(0, 2).equals("BP"))
//                    {
//                        whiteTurn1 = 3;
//                        nuke(board, positions);
//                        pawnOptions1.setVisibility(View.VISIBLE);
//                        pawnOptions1.setRotation(270);
//
//                        queen1.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BQP0";
//                                board[x][0].setImageResource(R.mipmap.bqueen);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions1.setVisibility(View.INVISIBLE);
//                                    whiteTurn1 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        rook1.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BRP0";
//                                board[x][0].setImageResource(R.mipmap.brook);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions1.setVisibility(View.INVISIBLE);
//                                    whiteTurn1 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        bishop1.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BBP0";
//                                board[x][0].setImageResource(R.mipmap.bbishop);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions1.setVisibility(View.INVISIBLE);
//                                    whiteTurn1 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        knight1.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BNP0";
//                                board[x][0].setImageResource(R.mipmap.bknight);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions1.setVisibility(View.INVISIBLE);
//                                    whiteTurn1 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        if (!position2)
//                        {
//                            positions[x][0] = "BQP0";
//                            board[x][0].setImageResource(R.mipmap.bqueen);
//                            if (gameState == 1)
//                            {
//                                pawnOptions1.setVisibility(View.INVISIBLE);
//                                whiteTurn1 = 1;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        if (board(board, positions) == 2)
//        {
//            if (whiteTurn2 == 2)
//            {
//                for (int i = 0; i < 8; i++)
//                {
//                    final int x = i;
//                    if (positions[i][7].substring(0, 2).equals("WP"))
//                    {
//                        whiteTurn2 = 3;
//                        nuke(board, positions);
//                        pawnOptions2.setVisibility(View.VISIBLE);
//                        pawnOptions2.setRotation(270);
//                        queen2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][7] = "WQP0";
//                                board[x][7].setImageResource(R.mipmap.queen);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 2;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        rook2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][7] = "WRP0";
//                                board[x][7].setImageResource(R.mipmap.rook);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 2;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        bishop2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][7] = "WBP0";
//                                board[x][7].setImageResource(R.mipmap.bishop);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 2;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        knight2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][7] = "WNP0";
//                                board[x][7].setImageResource(R.mipmap.knight);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 2;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        if (!position4)
//                        {
//                            positions[x][7] = "WQP0";
//                            board[x][7].setImageResource(R.mipmap.queen);
//                            if (gameState == 1)
//                            {
//                                pawnOptions2.setVisibility(View.INVISIBLE);
//                                whiteTurn2 = 2;
//                            }
//                        }
//                    }
//                }
//            }
//            if (whiteTurn2 == 1)
//            {
//                for (int i = 0; i < 8; i++)
//                {
//                    final int x = i;
//                    if (positions[i][0].substring(0, 2).equals("BP"))
//                    {
//                        whiteTurn2 = 3;
//                        nuke(board, positions);
//                        pawnOptions2.setVisibility(View.VISIBLE);
//                        pawnOptions2.setRotation(90);
//                        queen2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BQP0";
//                                board[x][0].setImageResource(R.mipmap.bqueen);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        rook2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BRP0";
//                                board[x][0].setImageResource(R.mipmap.brook);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        bishop2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BBP0";
//                                board[x][0].setImageResource(R.mipmap.bbishop);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        knight2.setOnClickListener(new View.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(View v)
//                            {
//                                positions[x][0] = "BNP0";
//                                board[x][0].setImageResource(R.mipmap.bknight);
//                                if (gameState == 1)
//                                {
//                                    pawnOptions2.setVisibility(View.INVISIBLE);
//                                    whiteTurn2 = 1;
//                                    setActions(board, positions);
//                                }
//                            }
//                        });
//                        if (!position3)
//                        {
//                            positions[x][0] = "BQP0";
//                            board[x][0].setImageResource(R.mipmap.bqueen);
//                            if (gameState == 1)
//                            {
//                                pawnOptions2.setVisibility(View.INVISIBLE);
//                                whiteTurn2 = 1;
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private void turnChange(final ImageView[][] board, final Piece[][] positions, final int x, final int y)
    {
        /**
         if (gameState == 2)
         {
         new Thread(new Runnable()
         {
         @Override public void run()
         {
         while (gameState == 2)
         {
         try
         {
         Thread.sleep(1);
         }
         catch (InterruptedException e)
         {
         e.printStackTrace();
         }
         }


         }
         }).start();
         }
         */

        if (board(board, positions) == 1)
        {
            if (positions[x][y].color.equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave1 = 2;
                }
                else
                {
                    whiteTurn1 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave1 = 1;
                }
                else
                {
                    whiteTurn1 = 1;
                }

            }
            board1Turn++;
        }
        else
        {
            if (positions[x][y].color.equals("white"))
            {
                if (gameState == 2)
                {
                    turnSave2 = 2;
                }
                else
                {
                    whiteTurn2 = 2;
                }
            }
            else
            {
                if (gameState == 2)
                {
                    turnSave2 = 1;
                }
                else
                {
                    whiteTurn2 = 1;
                }
            }
            board2Turn++;
        }

    }

    private void nuke(final ImageView[][] board, final Piece[][] positions)
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
        if (board(board, positions) == 1)
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

    private boolean whiteInCheck(ImageView[][] board, Piece[][] positions)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals("white") && positions[i][j].type.equals("king")&& checkCheck(board, positions, i, j))
                {
                    return true;
                }
            }
        }
        return false;

    }

    private boolean blackInCheck(ImageView[][] board, Piece[][] positions)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (positions[i][j].color.equals("black") && positions[i][j].type.equals("king")&& checkCheck(board, positions, i, j))
                {
                    return true;
                }

            }
        }
        return false;
    }

    public static boolean checkCheck(ImageView[][] board, Piece[][] positions, int x, int y)
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j =0; j < 8;j++)
            {
                if (positions[i][j].isOpposite(positions[x][y]))
                {
                    Set<Move> moves = positions[i][j].getMoves(board, positions, i, j);
                    for (Move m : moves)
                    {
                        if (m.type.equals("take") && m.x1 == x && m.y1 == y) return true;
                    }
                }
            }
        }
        return false;
    }

    private void setWhiteCheckConditions(ImageView[][] board, Piece[][] positions)
    {
//        if (checking)
//        {
//            for (int i = 0; i < 8; i++)
//            {
//                for (int j = 0; j < 8; j++)
//                {
//                    if (positions[i][j].substring(0, 2).equals("WK"))
//                    {
//                        board[i][j].setBackgroundColor(Color.BLUE);
//                        positions[i][j] = positions[i][j].substring(0, 3) + ("B");
//                    }
//                }
//            }
//        }
//        if (board(board, positions) == 1 && whiteTurn1 == 1)
//        {
//            searchingForCheckmate1 = true;
//            checkmate1 = true;
//
//            for (int x = 0; x < 8; x++)
//            {
//                for (int y = 0; y < 8; y++)
//                {
//                    if (positions[x][y].substring(0, 2).equals("WP"))
//                    {
//                        setPawn(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WR"))
//                    {
//                        setRook(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WN"))
//                    {
//                        setKnight(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WB"))
//                    {
//                        setBishop(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WQ"))
//                    {
//                        setQueen(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WK"))
//                    {
//                        setKing(board, positions, x, y);
//                    }
//                }
//            }
//
//            boolean piecesOnRoster = false;
//            for (int i = 0; i < 30; i++)
//            {
//                if (!roster1p[i].substring(1, 2).equals("0"))
//                {
//                    piecesOnRoster = true;
//                    setRosterPiece(board, positions, roster1, roster1p, i);
//                }
//            }
//            if (!piecesOnRoster)
//            {
//                roster1p[0] = "WQ00";
//                setRosterPiece(board, positions, roster1, roster1p, 0);
//                roster1p[0] = "0000";
//            }
//
//            searchingForCheckmate1 = false;
//            if (checkmate1)
//            {
//                gameEndProcedures(1, 0);
//            }
//        }
//        if (board(board, positions) == 2 && whiteTurn2 == 1)
//        {
//            searchingForCheckmate2 = true;
//            checkmate2 = true;
//
//            for (int x = 0; x < 8; x++)
//            {
//                for (int y = 0; y < 8; y++)
//                {
//                    if (positions[x][y].substring(0, 2).equals("WP"))
//                    {
//                        setPawn(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WR"))
//                    {
//                        setRook(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WN"))
//                    {
//                        setKnight(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WB"))
//                    {
//                        setBishop(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WQ"))
//                    {
//                        setQueen(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("WK"))
//                    {
//                        setKing(board, positions, x, y);
//                    }
//                }
//            }
//            boolean piecesOnRoster = false;
//            for (int i = 0; i < 30; i++)
//            {
//                if (!roster4p[i].substring(1, 2).equals("0"))
//                {
//                    piecesOnRoster = true;
//                    setRosterPiece(board, positions, roster4, roster4p, i);
//                }
//            }
//            if (!piecesOnRoster)
//            {
//                roster4p[0] = "WQ00";
//                setRosterPiece(board, positions, roster4, roster4p, 0);
//                roster4p[0] = "0000";
//            }
//
//            searchingForCheckmate2 = false;
//            if (checkmate2)
//            {
//                gameEndProcedures(0, 0);
//            }
//        }

    }

    private void setBlackCheckConditions(ImageView[][] board, Piece[][] positions)
    {
//        if (checking)
//        {
//            for (int i = 0; i < 8; i++)
//            {
//                for (int j = 0; j < 8; j++)
//                {
//                    if (positions[i][j].substring(0, 2).equals("BK"))
//                    {
//                        board[i][j].setBackgroundColor(Color.BLUE);
//                        positions[i][j] = positions[i][j].substring(0, 3) + ("B");
//
//                    }
//                }
//            }
//        }
//        if (board(board, positions) == 1 && whiteTurn1 == 2)
//        {
//            searchingForCheckmate1 = true;
//            checkmate1 = true;
//
//            for (int x = 0; x < 8; x++)
//            {
//                for (int y = 0; y < 8; y++)
//                {
//                    if (positions[x][y].substring(0, 2).equals("BP"))
//                    {
//                        setBPawn(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BR"))
//                    {
//                        setBRook(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BN"))
//                    {
//                        setBKnight(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BB"))
//                    {
//                        setBBishop(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BQ"))
//                    {
//                        setBQueen(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BK"))
//                    {
//                        setBKing(board, positions, x, y);
//                    }
//                }
//            }
//
//            boolean piecesOnRoster = false;
//            for (int i = 0; i < 30; i++)
//            {
//                if (!roster2p[i].substring(1, 2).equals("0"))
//                {
//                    piecesOnRoster = true;
//                    setRosterPiece(board, positions, roster2, roster2p, i);
//                }
//            }
//            if (!piecesOnRoster)
//            {
//                roster2p[0] = "BQ00";
//                setRosterPiece(board, positions, roster2, roster2p, 0);
//                roster2p[0] = "0000";
//            }
//
//            searchingForCheckmate1 = false;
//            if (checkmate1)
//            {
//                gameEndProcedures(0, 0);
//            }
//        }
//        if (board(board, positions) == 2 && whiteTurn2 == 2)
//        {
//            searchingForCheckmate2 = true;
//            checkmate2 = true;
//
//            for (int x = 0; x < 8; x++)
//            {
//                for (int y = 0; y < 8; y++)
//                {
//                    if (positions[x][y].substring(0, 2).equals("BP"))
//                    {
//                        setBPawn(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BR"))
//                    {
//                        setBRook(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BN"))
//                    {
//                        setBKnight(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BB"))
//                    {
//                        setBBishop(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BQ"))
//                    {
//                        setBQueen(board, positions, x, y);
//                    }
//                    if (positions[x][y].substring(0, 2).equals("BK"))
//                    {
//                        setBKing(board, positions, x, y);
//                    }
//                }
//            }
//            boolean piecesOnRoster = false;
//            for (int i = 0; i < 30; i++)
//            {
//                if (!roster3p[i].substring(1, 2).equals("0"))
//                {
//                    piecesOnRoster = true;
//                    setRosterPiece(board, positions, roster3, roster3p, i);
//                }
//            }
//            if (!piecesOnRoster)
//            {
//                roster3p[0] = "BQ00";
//                setRosterPiece(board, positions, roster3, roster3p, 0);
//                roster3p[0] = "0000";
//            }
//
//            searchingForCheckmate2 = false;
//            if (checkmate2)
//            {
//                gameEndProcedures(1, 0);
//            }
//        }
//
    }

    private void startAI()
    {
//
//        if (!position1)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (gameState == 0 || gameState == 2)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (whiteTurn1 == 1)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (whiteTurn1 == 1)
//                        {
//                            CPUsearch[0] = true;
//                            rating[0] = 0;
//                            for (int x = 0; x < 8; x++)
//                            {
//                                for (int y = 0; y < 8; y++)
//                                {
//                                    if (positions1[x][y].substring(0, 2).equals("WP"))
//                                    {
//                                        setPawn(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("WR"))
//                                    {
//                                        setRook(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("WN"))
//                                    {
//                                        setKnight(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("WB"))
//                                    {
//                                        setBishop(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("WQ"))
//                                    {
//                                        setQueen(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("WK"))
//                                    {
//                                        setKing(board1, positions1, x, y);
//                                    }
//                                }
//                            }
//                            for (int i = 0; i < 30; i++)
//                            {
//                                if (!roster1p[i].substring(1, 2).equals("0"))
//                                {
//                                    setRosterPiece(board1, positions1, roster1, roster1p, i);
//                                }
//                            }
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (rating[0] == 0)
//                                    {
//                                        return;
//                                    }
//                                    if (moveType[0].equals("move"))
//                                    {
//                                        changeShit(board1, positions1, cpuX[0], cpuY[0], cpuX1[0], cpuY1[0]);
//                                    }
//                                    if (moveType[0].equals("take"))
//                                    {
//                                        takeShit(board1, positions1, cpuX[0], cpuY[0], cpuX1[0], cpuY1[0]);
//                                    }
//                                    if (moveType[0].equals("drop"))
//                                    {
//                                        rosterShit(board1, positions1, roster1, roster1p, cpuRoster[0], cpuX1[0], cpuY1[0]);
//                                    }
//                                }
//                            });
//                            CPUsearch[0] = false;
//                        }
//                    }
//                }
//            }).start();
//        }
//
//        if (!position2)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (gameState == 0 || gameState == 2)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (whiteTurn1 == 2)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (whiteTurn1 == 2)
//                        {
//                            CPUsearch[1] = true;
//                            rating[1] = 0;
//
//                            for (int x = 0; x < 8; x++)
//                            {
//                                for (int y = 0; y < 8; y++)
//                                {
//                                    if (positions1[x][y].substring(0, 2).equals("BP"))
//                                    {
//                                        setBPawn(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("BR"))
//                                    {
//                                        setBRook(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("BN"))
//                                    {
//                                        setBKnight(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("BB"))
//                                    {
//                                        setBBishop(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("BQ"))
//                                    {
//                                        setBQueen(board1, positions1, x, y);
//                                    }
//                                    if (positions1[x][y].substring(0, 2).equals("BK"))
//                                    {
//                                        setBKing(board1, positions1, x, y);
//                                    }
//                                }
//                            }
//                            for (int i = 0; i < 30; i++)
//                            {
//                                if (!roster2p[i].substring(1, 2).equals("0"))
//                                {
//                                    setRosterPiece(board1, positions1, roster2, roster2p, i);
//                                }
//                            }
//
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (rating[1] == 0)
//                                    {
//                                        return;
//                                    }
//                                    if (moveType[1].equals("move"))
//                                    {
//                                        changeShit(board1, positions1, cpuX[1], cpuY[1], cpuX1[1], cpuY1[1]);
//                                    }
//                                    if (moveType[1].equals("take"))
//                                    {
//                                        takeShit(board1, positions1, cpuX[1], cpuY[1], cpuX1[1], cpuY1[1]);
//                                    }
//                                    if (moveType[1].equals("drop"))
//                                    {
//                                        rosterShit(board1, positions1, roster2, roster2p, cpuRoster[1], cpuX1[1], cpuY1[1]);
//                                    }
//                                }
//                            });
//                            CPUsearch[1] = false;
//                        }
//                    }
//
//                }
//            }).start();
//
//        }
//
//        if (!position3)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (gameState == 0 || gameState == 2)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (whiteTurn2 == 2)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (whiteTurn2 == 2)
//                        {
//                            CPUsearch[2] = true;
//                            rating[2] = 0;
//
//                            for (int x = 0; x < 8; x++)
//                            {
//                                for (int y = 0; y < 8; y++)
//                                {
//                                    if (positions2[x][y].substring(0, 2).equals("BP"))
//                                    {
//                                        setBPawn(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("BR"))
//                                    {
//                                        setBRook(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("BN"))
//                                    {
//                                        setBKnight(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("BB"))
//                                    {
//                                        setBBishop(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("BQ"))
//                                    {
//                                        setBQueen(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("BK"))
//                                    {
//                                        setBKing(board2, positions2, x, y);
//                                    }
//
//                                }
//                            }
//                            for (int i = 0; i < 30; i++)
//                            {
//                                if (!roster3p[i].substring(1, 2).equals("0"))
//                                {
//                                    setRosterPiece(board2, positions2, roster3, roster3p, i);
//                                }
//                            }
//
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (rating[2] == 0)
//                                    {
//                                        return;
//                                    }
//                                    if (moveType[2].equals("move"))
//                                    {
//                                        changeShit(board2, positions2, cpuX[2], cpuY[2], cpuX1[2], cpuY1[2]);
//                                    }
//                                    if (moveType[2].equals("take"))
//                                    {
//                                        takeShit(board2, positions2, cpuX[2], cpuY[2], cpuX1[2], cpuY1[2]);
//                                    }
//                                    if (moveType[2].equals("drop"))
//                                    {
//                                        rosterShit(board2, positions2, roster3, roster3p, cpuRoster[2], cpuX1[2], cpuY1[2]);
//                                    }
//                                }
//                            });
//                            CPUsearch[2] = false;
//                        }
//                    }
//
//                }
//            }).start();
//
//        }
//        if (!position4)
//        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    boolean a = true;
//                    while (a)
//                    {
//                        if (gameState == 0 || gameState == 2)
//                        {
//                            a = false;
//                        }
//                        Random rand = new Random();
//                        int wait = rand.nextInt(1000);
//                        try
//                        {
//                            Thread.sleep(wait);
//                        } catch (InterruptedException e)
//                        {
//                            System.out.println("got interrupted!");
//                        }
//                        if (whiteTurn2 == 1)
//                        {
//                            try
//                            {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e)
//                            {
//                                System.out.println("got interrupted!");
//                            }
//                        }
//                        if (whiteTurn2 == 1)
//                        {
//                            CPUsearch[3] = true;
//                            rating[3] = 0;
//                            for (int x = 0; x < 8; x++)
//                            {
//                                for (int y = 0; y < 8; y++)
//                                {
//                                    if (positions2[x][y].substring(0, 2).equals("WP"))
//                                    {
//                                        setPawn(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("WR"))
//                                    {
//                                        setRook(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("WN"))
//                                    {
//                                        setKnight(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("WB"))
//                                    {
//                                        setBishop(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("WQ"))
//                                    {
//                                        setQueen(board2, positions2, x, y);
//                                    }
//                                    if (positions2[x][y].substring(0, 2).equals("WK"))
//                                    {
//                                        setKing(board2, positions2, x, y);
//                                    }
//                                }
//                            }
//                            for (int i = 0; i < 30; i++)
//                            {
//                                if (!roster4p[i].substring(1, 2).equals("0"))
//                                {
//                                    setRosterPiece(board2, positions2, roster4, roster4p, i);
//                                }
//                            }
//
//                            runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    if (rating[3] == 0)
//                                    {
//                                        return;
//                                    }
//                                    if (moveType[3].equals("move"))
//                                    {
//                                        changeShit(board2, positions2, cpuX[3], cpuY[3], cpuX1[3], cpuY1[3]);
//                                    }
//                                    if (moveType[3].equals("take"))
//                                    {
//                                        takeShit(board2, positions2, cpuX[3], cpuY[3], cpuX1[3], cpuY1[3]);
//                                    }
//                                    if (moveType[3].equals("drop"))
//                                    {
//                                        rosterShit(board2, positions2, roster4, roster4p, cpuRoster[3], cpuX1[3], cpuY1[3]);
//                                    }
//                                }
//                            });
//                            CPUsearch[3] = false;
//                        }
//                    }
//
//                }
//            }).start();
//
//        }
//
    }

    private void rate(ImageView[][] board, Piece[][] positions, int x, int y, int x1, int y1, String type, Piece[] rosterp, int cpu)
    {
        if (cpuLevel[cpu] == 0)
        {
            Random rand = new Random();
            int value = rand.nextInt(100);
            if (value > rating[cpu])
            {
                rating[cpu] = value;
                cpuX[cpu] = x;
                cpuY[cpu] = y;
                cpuX1[cpu] = x1;
                cpuY1[cpu] = y1;
                cpuRoster[cpu] = y;
                moveType[cpu] = type;
            }
            return;
        }
        double tempRating = 0;
        double XProx = 0;
        double YProx = 0;
        XProx = Math.abs(3.5 - x1);
        YProx = Math.abs(3.5 - y1);
        tempRating = 17 - (XProx + YProx);
        if (x != -1)
        {
            if (positions[x][y].type.equals("K"))
            {
                tempRating = XProx + YProx;
            }
        }
        if (type.equals("drop"))
        {
            tempRating = tempRating + 20;
        }
        if (type.equals("take"))
        {
            tempRating = tempRating + 30;
        }
        if (x != -1)
        {
            if (checkCheck(board, positions, x, y))
            {
                inCheck[cpu] = true;
            }
            else
            {
                inCheck[cpu] = false;
            }
        }

        Piece positionSave = positions[x1][y1];
        if (x == -1)
        {
            positions[x1][y1] = rosterp[y];
        }
        else
        {
            positions[x1][y1] = positions[x][y];
            positions[x][y] = new Empty();
        }

        if (whiteInCheck(board, positions) || blackInCheck(board, positions))
        {
            tempRating = tempRating + 40;
        }
        if (cpuLevel[cpu] == 2)
        {
            if (positions[x1][y1].type.equals("Q") || positions[x1][y1].type.equals("R")
                    || positions[x1][y1].type.equals("N") || positions[x1][y1].type.equals("B"))
            {
                if (inCheck[cpu] && !checkCheck(board, positions, x1, y1))
                {
                    tempRating = tempRating + 40;
                }
                if (checkCheck(board, positions, x1, y1))
                {
                    tempRating = 1;
                }
            }
        }

        if (x == -1)
        {
            positions[x1][y1] = new Empty();
        }
        else
        {
            positions[x][y] = positions[x1][y1];
            positions[x1][y1] = positionSave;
        }

        if (x != -1)
        {
            if (positions[x][y].color.equals("white") && positions[x][y].type.equals("pawn") && y == 6)
            {
                tempRating = tempRating + 30;
            }
            if (positions[x][y].color.equals("black") && positions[x][y].type.equals("pawn") && y == 1)
            {
                tempRating = tempRating + 30;
            }
        }

        if (tempRating >= rating[cpu])
        {
            if (tempRating == rating[cpu] && rating[cpu] != 0)
            {
                Random rand = new Random();
                int value = rand.nextInt(2);
                if (value == 0)
                {
                    return;
                }
            }
            rating[cpu] = tempRating;
            cpuX[cpu] = x;
            cpuY[cpu] = y;
            cpuX1[cpu] = x1;
            cpuY1[cpu] = y1;
            cpuRoster[cpu] = y;
            moveType[cpu] = type;
        }
    }

    private void kingStillStanding(ImageView[][] board, Piece[][] positions)
    {
        boolean player1 = false;
        boolean player2 = false;
        boolean player3 = false;
        boolean player4 = false;

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (board(board, positions) == 1)
                {
                    if (positions1[i][j].color.equals("white") && positions1[i][j].type.equals("king"))
                    {
                        player1 = true;
                    }
                    if (positions1[i][j].color.equals("black") && positions1[i][j].type.equals("king"))
                    {
                        player2 = true;
                    }
                }
                if (board(board, positions) == 2)
                {
                    if (positions2[i][j].color.equals("white") && positions2[i][j].type.equals("king"))
                    {
                        player3 = true;
                    }
                    if (positions2[i][j].color.equals("black") && positions2[i][j].type.equals("king"))
                    {
                        player4 = true;
                    }
                }
            }
        }

        if (board(board, positions) == 1)
        {
            if (!player2)
            {
                gameEndProcedures(0, 1);
            }
            if (!player1)
            {
                gameEndProcedures(1, 1);
            }
        }
        if (board(board, positions) == 2)
        {
            if (!player4)
            {
                gameEndProcedures(1, 1);
            }
            if (!player3)
            {
                gameEndProcedures(0, 1);
            }
        }


    }

    private void gameEndProcedures(int side, int type)
    {
        clean(board1, positions1);
        clean(board2, positions2);
        whiteTurn1 = 3;
        whiteTurn2 = 3;
        nuke(board1, positions1);
        nuke(board2, positions2);
        final Button start = (Button) findViewById(R.id.start);
        start.setText("Start");
        gameState = 0;
        TextView timeNotice = (TextView) findViewById(R.id.timeNotice);
        timeNotice.setVisibility(View.INVISIBLE);
        final LinearLayout finishScreen = (LinearLayout) findViewById(R.id.finishScreen);

        if (side == 0)
        {
            TextView L = (TextView) findViewById(R.id.L);
            L.setText("WINNER");
            L.setRotation(90);
            TextView R_ = (TextView) findViewById(R.id.R);
            R_.setText("LOSER");
            R_.setRotation(270);

        }
        else
        {
            TextView L = (TextView) findViewById(R.id.L);
            L.setText("LOSER");
            L.setRotation(90);
            TextView R_ = (TextView) findViewById(R.id.R);
            R_.setText("WINNER");
            R_.setRotation(270);


        }
        final TextView endType = (TextView) findViewById(R.id.endType);
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

    private void resetBooleans()
    {
        whiteTurn1 = 1;
        whiteTurn2 = 1;

        whiteCastleQueen1 = true;
        whiteCastleKing1 = true;
        blackCastleQueen1 = true;
        blackCastleKing1 = true;
        whiteCastleQueen2 = true;
        whiteCastleKing2 = true;
        blackCastleQueen2 = true;
        blackCastleKing2 = true;
        searchingForCheckmate1 = false;
        checkmate1 = false;
        searchingForCheckmate2 = false;
        checkmate2 = false;

        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                enP[i][j] = "0000";
            }
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
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

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
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

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (gameState == 1)
        {
            clean(board1, positions1);
            clean(board2, positions2);
            turnSave1 = whiteTurn1;
            whiteTurn1 = 3;
            turnSave2 = whiteTurn2;
            whiteTurn2 = 3;
            nuke(board1, positions1);
            nuke(board2, positions2);
            gameState = 2;
            final Button start = (Button) findViewById(R.id.start);
            start.setText("Resume");
        }

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
            if (ID.substring(length-1, length).equals("1") || ID.substring(length-1, length).equals("3"))
            {
                if (ID.substring(0, length-1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.pawn, 100, 100));
                }
                if (ID.substring(0, length-1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.rook, 100, 100));
                }
                if (ID.substring(0, length-1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.knight, 100, 100));
                }
                if (ID.substring(0, length-1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bishop, 100, 100));
                }
                if (ID.substring(0, length-1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.queen, 100, 100));
                }
                if (ID.substring(0, length-1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.king, 100, 100));
                }
            }
            if (ID.substring(length-1, length).equals("2") || ID.substring(length-1, length).equals("4"))
            {
                if (ID.substring(0, length-1).equals("pawn"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bpawn, 100, 100));
                }
                if (ID.substring(0, length-1).equals("rook"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.brook, 100, 100));
                }
                if (ID.substring(0, length-1).equals("knight"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bknight, 100, 100));
                }
                if (ID.substring(0, length-1).equals("bishop"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bbishop, 100, 100));
                }
                if (ID.substring(0, length-1).equals("queen"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bqueen, 100, 100));
                }
                if (ID.substring(0, length-1).equals("king"))
                {
                    PIC = new BitmapDrawable(getResources(), decodeSampledBitmapFromResource(getResources(), R.mipmap.bking, 100, 100));
                }
            }
            if (ID.substring(length-1, length).equals("1") || ID.substring(length-1, length).equals("4"))
            {
                rotation = 90;
            }
            if (ID.substring(length-1, length).equals("2") || ID.substring(length-1, length).equals("3"))
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
            height = (int) (getView().getHeight() * 2);
            width = (int) (getView().getHeight() * 2);

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

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("261C12037B82A37B909C2E6BE482F9ED")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
