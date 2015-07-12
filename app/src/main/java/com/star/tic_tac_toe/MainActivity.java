package com.star.tic_tac_toe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button[][] mButtons = new Button[3][3];

    private Button[] mModeButtons = new Button[3];

    private TextView mResultTextView;
    
    private String[] mStrings = { "O", "X" };

    private enum Mode {
        WITH_FRIEND, COMPUTER_FIRST, COMPUTER_SECOND
    }

    private enum Player {
        FIRST_PLAYER, SECOND_PLAYER
    }

    private boolean mGameOver;

    private Mode mMode = Mode.WITH_FRIEND;
    private Player mPlayer = Player.FIRST_PLAYER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtons[0][0] = (Button) findViewById(R.id.upper_left_button);
        mButtons[0][1] = (Button) findViewById(R.id.upper_middle_button);
        mButtons[0][2] = (Button) findViewById(R.id.upper_right_button);

        mButtons[1][0] = (Button) findViewById(R.id.middle_left_button);
        mButtons[1][1] = (Button) findViewById(R.id.middle_middle_button);
        mButtons[1][2] = (Button) findViewById(R.id.middle_right_button);

        mButtons[2][0] = (Button) findViewById(R.id.lower_left_button);
        mButtons[2][1] = (Button) findViewById(R.id.lower_middle_button);
        mButtons[2][2] = (Button) findViewById(R.id.lower_right_button);

        mModeButtons[0] = (Button) findViewById(R.id.with_friend);
        mModeButtons[1] = (Button) findViewById(R.id.computer_first);
        mModeButtons[2] = (Button) findViewById(R.id.computer_second);

        mResultTextView = (TextView) findViewById(R.id.result);

        initGame();
        initButtons();
        
    }

    private void initGame() {
        
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            mModeButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mGameOver = false;
                    mPlayer = Player.FIRST_PLAYER;
                    mResultTextView.setText("");

                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            mButtons[i][j].setText("");
                        }
                    }
                    
                    switch (finalI) {
                        case 0:
                            mMode = Mode.WITH_FRIEND;
                            break;
                        case 1:
                            mMode = Mode.COMPUTER_FIRST;
                            computerPlay();
                            swapPlayer();
                            break;
                        case 2:
                            mMode = Mode.COMPUTER_SECOND;
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
    
    private void initButtons() {
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int finalI = i;
                final int finalJ = j;
                mButtons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("".equals(mButtons[finalI][finalJ].getText().toString()) && !mGameOver) {
                            mButtons[finalI][finalJ].setText(signPlayer(mPlayer));
                            mGameOver = isGameOver();
                            if (mGameOver) {
                                decideWinner();
                            } else {
                                swapPlayer();
                                switch (mMode) {
                                    case COMPUTER_FIRST:
                                    case COMPUTER_SECOND:
                                        computerPlay();
                                        mGameOver = isGameOver();
                                        if (mGameOver) {
                                            decideWinner();
                                        } else {
                                            swapPlayer();
                                        }
                                        break;
                                }
                            }

                        }
                    }
                });
            }
        }
    }

    private String signPlayer(Player player) {
        if (player == Player.FIRST_PLAYER) {
            return mStrings[0];
        } else {
            return mStrings[1];
        }
    }

    private String signOpponent(Player player) {
        if (player == Player.FIRST_PLAYER) {
            return mStrings[1];
        } else {
            return mStrings[0];
        }
    }
    
    private void swapPlayer() {
        if (mPlayer == Player.FIRST_PLAYER) {
            mPlayer = Player.SECOND_PLAYER;
        } else {
            mPlayer = Player.FIRST_PLAYER;
        }
    }

    private void computerPlay() {

        String[][] strings = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                strings[i][j] = mButtons[i][j].getText().toString();
            }
        }

        if (isWinPoint(strings, signPlayer(mPlayer))) {
            return;
        }

        if (isWinPoint(strings, signOpponent(mPlayer))) {
            return;
        }

        if ("".equals(strings[1][1])) {
            mButtons[1][1].setText(signPlayer(mPlayer));
            return;
        }

        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 3; j += 2) {
                if ("".equals(strings[i][j])) {
                    mButtons[i][j].setText(signPlayer(mPlayer));
                    return;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j ++) {
                if ("".equals(strings[i][j])) {
                    mButtons[i][j].setText(signPlayer(mPlayer));
                    return;
                }
            }
        }


    }

    private boolean isGameOver() {

        if (hasWinner()) {
            return true;
        }

        return isTied();
    }

    private void decideWinner() {

        String winner = "";

        if (isTied()) {
            winner = "Nobody";
        } else {
            switch (mMode) {
                case WITH_FRIEND:
                    if (mPlayer == Player.FIRST_PLAYER) {
                        winner = "First Player";
                    } else {
                        winner = "Second Player";
                    }
                    break;
                case COMPUTER_FIRST:
                    if (mPlayer == Player.FIRST_PLAYER) {
                        winner = "Computer";
                    } else {
                        winner = "You";
                    }
                    break;
                case COMPUTER_SECOND:
                    if (mPlayer == Player.SECOND_PLAYER) {
                        winner = "Computer";
                    } else {
                        winner = "You";
                    }
                    break;
                default:
                    break;
            }
        }

        mResultTextView.setText("The winner is " + winner);
    }

    private boolean isWinPoint(String[][] strings, String winSign) {
        for (int i = 0; i < 3; i++) {
            if (getWinSign(strings[i][0], strings[i][1], strings[i][2]).equals(winSign)) {
                for (int j = 0; i < 3; j++) {
                    if ("".equals(strings[i][j])) {
                        mButtons[i][j].setText(signPlayer(mPlayer));
                        return true;
                    }
                }
            }

            if (getWinSign(strings[0][i], strings[1][i], strings[2][i]).equals(winSign)) {
                for (int j = 0; i < 3; j++) {
                    if ("".equals(strings[j][i])) {
                        mButtons[j][i].setText(signPlayer(mPlayer));
                        return true;
                    }
                }
            }
        }

        if (getWinSign(strings[0][0], strings[1][1], strings[2][2]).equals(winSign)) {
            for (int i = 0; i < 3; i++) {
                if ("".equals(strings[i][i])) {
                    mButtons[i][i].setText(signPlayer(mPlayer));
                    return true;
                }
            }
        }

        if (getWinSign(strings[2][0], strings[1][1], strings[0][2]).equals(winSign)) {
            for (int i = 0; i < 3; i++) {
                if ("".equals(strings[2 - i][i])) {
                    mButtons[2 - i][i].setText(signPlayer(mPlayer));
                    return true;
                }
            }
        }

        return false;
    }

    private String getWinSign(String s1, String s2, String s3) {

        for (int i = 0; i < mStrings.length; i++) {
            if ((mStrings[i] + mStrings[i]).equals(s1 + s2 + s3)) {
                return mStrings[i];
            }
        }

        return "";

    }

    private boolean hasWinner() {
        String[][] strings = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                strings[i][j] = mButtons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (strings[i][0].equals(strings[i][1]) &&
                    strings[i][0].equals(strings[i][2]) &&
                    !"".equals(strings[i][0])) {
                return true;
            }

            if (strings[0][i].equals(strings[1][i]) &&
                    strings[0][i].equals(strings[2][i]) &&
                    !"".equals(strings[0][i])) {
                return true;
            }

        }

        if (strings[0][0].equals(strings[1][1]) &&
                strings[0][0].equals(strings[2][2]) &&
                !"".equals(strings[0][0])) {
            return true;
        }

        if (strings[2][0].equals(strings[1][1]) &&
                strings[2][0].equals(strings[0][2]) &&
                !"".equals(strings[2][0])) {
            return true;
        }

        return false;
    }

    private boolean isTied() {

        if (hasWinner()) {
            return false;
        }

        String[][] strings = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                strings[i][j] = mButtons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ("".equals(strings[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

}
