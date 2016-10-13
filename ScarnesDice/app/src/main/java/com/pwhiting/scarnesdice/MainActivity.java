package com.pwhiting.scarnesdice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int[] DICE_IMAGES = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    private final Random rand = new Random();

    private int playerScore = 0,
                playerTurn = 0,
                computerScore = 0,
                computerTurn = 0;

    private boolean isPlayerTurn = true;

    private Runnable doComputerTurn = new Runnable() {
        @Override
        public void run() {
                isPlayerTurn = false;
                int value = 0;
                while ((value = rollDie()) != 1 && computerTurn < 20) {
                    computerTurn += value;
                    updateStatus();
                }

                if (value != 1) {
                    computerScore += computerTurn;
                    computerTurn = 0;
                } else {
                    computerTurn = 0;
                }
                updateStatus();
                isPlayerTurn = true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetGame();

        Button roll = (Button)findViewById(R.id.rollButton);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayerTurn) return;
                int value = rollDie();

                if (value == 1) {
                    playerTurn = 0;
                    doComputerTurn.run();
                } else { playerTurn += value; }

                updateStatus();
            }
        });

        Button reset = (Button)findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        Button hold = (Button)findViewById(R.id.holdButton);
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlayerTurn) return;
                playerScore += playerTurn;
                playerTurn = 0;
                updateStatus();
                doComputerTurn.run();
            }
        });
    }

    public void clearScore() {
        playerScore = 0;
        playerTurn = 0;
        computerScore = 0;
        computerTurn = 0;
    }

    public void resetGame() {
        setDie(1);
        clearScore();
        updateStatus();
        isPlayerTurn = true;
    }

    public void updateStatus() {

        TextView status = (TextView)findViewById(R.id.textStatus);

        String text = "Your Score: X, Computer Score: X, " +
                (isPlayerTurn ? "Your" : "Computer") + " Turn Score: X";
        text = text.replaceFirst("X", String.valueOf(playerScore));
        text = text.replaceFirst("X", String.valueOf(computerScore));
        text = text.replaceFirst("X", String.valueOf(isPlayerTurn ? playerTurn : computerTurn));

        status.setText(text);
    }

    public int rollDie() {
        int choice = rand.nextInt(6);
        setDie(choice);
        return choice;
    }

    public void setDie(int num) {
        int use = num > 6 ? 6 : (num < 1 ? 1 : num);
        use--;
        ImageView dice = (ImageView)findViewById(R.id.diceImage);
        dice.setImageDrawable(getResources().getDrawable(DICE_IMAGES[use], null));
    }
}
