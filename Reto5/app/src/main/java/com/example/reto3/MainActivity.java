package com.example.reto3;

import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuHost;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Lifecycle;

public class MainActivity extends AppCompatActivity {
    private TicTacToeGame game;
    // Buttons making up the board
    private Button[] mBoardButtons;
    // Various text displayed
    private TextView mInfoTextView;
    private TextView mHumanWinsTextView;
    private TextView mTiesTextView;
    private TextView mAndroidWinsTextView;
    private BoardView mBoardView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    private boolean mGameOver = false;
    private int human_wins = 0;
    private int ties = 0;
    private int android_wins = 0;
    private boolean humanTurn = false;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Set up Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Attach menu
        MenuHost menuHost = this;
        menuHost.addMenuProvider(new MyMenuProvider(this),
                this, Lifecycle.State.RESUMED);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mInfoTextView = findViewById(R.id.information);
        mHumanWinsTextView = (TextView) findViewById(R.id.human_wins);
        mTiesTextView = (TextView) findViewById(R.id.ties);
        mAndroidWinsTextView = (TextView) findViewById(R.id.android_wins);
        game = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(game);
        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);

        startNewGame();
    }

    public void startNewGame() {
        game.clearBoard();
        mGameOver = false;
        mBoardView.invalidate();
        humanTurn = false;
        // Human goes first
        if (Math.random() <= 0.5) {
            humanTurn = true;
            mInfoTextView.setText(R.string.first_human);
        } else {
            mInfoTextView.setText(R.string.turn_computer);
            int move = game.getComputerMove();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    mComputerMediaPlayer.start();
                    mInfoTextView.setText(R.string.turn_human);
                    humanTurn = true;
                }
            }, 2000);
        }
    }

    private boolean setMove(char player, int location) {
        if (game.setMove(player, location)) {
            Log.i(TAG, "Invalidate");
            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            if (humanTurn) {
                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)){
                    setMove(TicTacToeGame.HUMAN_PLAYER, pos);
                    mHumanMediaPlayer.start();
                    humanTurn = false;
                    // If no winner yet, let the computer make a move
                    int winner = game.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.turn_computer);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int move = game.getComputerMove();
                                Log.i(TAG, "Move: " + move);
                                boolean status = setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                                Log.i(TAG, "Status: " + status);
                                mComputerMediaPlayer.start();
                                humanTurn = true;
                                int winner = game.checkForWinner();
                                if (winner != 0) {
                                    mGameOver = true;
                                }
                                if (winner == 0) {
                                    mInfoTextView.setText(R.string.turn_human);

                                } else if (winner == 1) {
                                    mInfoTextView.setText(R.string.result_tie);
                                    ties += 1;
                                    mTiesTextView.setText("Ties: " + ties);
                                } else if (winner == 2) {
                                    mInfoTextView.setText(R.string.result_human_wins);
                                    human_wins += 1;
                                    mHumanWinsTextView.setText("Human: " + human_wins);
                                } else {
                                    mInfoTextView.setText(R.string.result_computer_wins);
                                    android_wins += 1;
                                    mAndroidWinsTextView.setText("Android: " + android_wins);
                                }
                            }
                        }, 1500);
                        return true;
                    }
                    if (winner != 0) {
                        mGameOver = true;
                    }
                    if (winner == 0 && humanTurn) {
                        mInfoTextView.setText(R.string.turn_human);

                    } else if (winner == 1) {
                        mInfoTextView.setText(R.string.result_tie);
                        ties += 1;
                        mTiesTextView.setText("Ties: " + ties);
                    } else if (winner == 2) {
                        mInfoTextView.setText(R.string.result_human_wins);
                        human_wins += 1;
                        mHumanWinsTextView.setText("Human: " + human_wins);
                    } else if (winner == 3) {
                        mInfoTextView.setText(R.string.result_computer_wins);
                        android_wins += 1;
                        mAndroidWinsTextView.setText("Android: " + android_wins);
                    }
                }
            }
            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    public TicTacToeGame getGame() {
        return game;
    }
}