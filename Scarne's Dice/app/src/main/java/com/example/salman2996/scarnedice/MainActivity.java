package com.example.salman2996.scarnedice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int userTotal=0, userTurn=0, compTotal=0, compTurn=0;
    private Random random=new Random();
    private Integer images[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    boolean flag=true;

    ImageView imageView;
    Button b1;
    Button b2;
    Button b3;
    TextView textView;
    Handler handler=new Handler();
    //Handler handler1=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button3);
        imageView=(ImageView)findViewById(R.id.imageView);
        textView=(TextView)findViewById(R.id.textView);

        textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);

        b2.setEnabled(false);

        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(images[0]);
                int n=computerTurn();
                if(n==0) {
                    Toast.makeText(MainActivity.this, "Computer Rolled 1, Your turn", Toast.LENGTH_SHORT).show();
                    compTurn=0;
                    imageView.setImageResource(images[0]);
                    computerDisplay();
                    //handler.removeCallbacks(this);
                }
                else if(n==20) {
                    Toast.makeText(MainActivity.this, "Computer Holds, Your turn", Toast.LENGTH_SHORT).show();
                    computerDisplay();
                    //handler.removeCallbacks(this);
                }
                else
                    handler.postDelayed(this,2000);
            }
        };

        /*final Runnable runnable1=new Runnable() {
            @Override
            public void run() {
                check();
                if(flag==true) {
                    imageView.setImageResource(images[0]);
                    userTotal = 0;
                    userTurn = 0;
                    compTotal = 0;
                    compTurn = 0;
                    b1.setEnabled(true);
                    b2.setEnabled(true);
                    textView.setText("User Total: " + userTotal + "\nComputer Total: " + compTotal + "\nUser Turn: " + userTurn + "\nComputer Turn: " + compTurn);
                }
            }
        };*/

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b2.setEnabled(true);
                int x=random.nextInt(6);
                int y=calcScore(x);
                userTurn=userTurn+y;
                textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
                if(y==0) {
                    userTurn=0;
                    //imageView.setImageResource(images[0]);
                    b1.setEnabled(false);
                    b2.setEnabled(false);
                    textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
                    Toast.makeText(MainActivity.this,"Computer's Turn",Toast.LENGTH_SHORT).show();
                    handler.post(runnable);
                    if(compTurn>20)
                        handler.removeCallbacks(runnable);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTotal=userTotal+userTurn;
                check();
                if(flag){
                    reset(runnable);
                }
                userTurn=0;
                /*handler1.post(runnable1);
                if(flag==false)
                    handler1.removeCallbacks(runnable1);
                else
                    return;*/
                b1.setEnabled(false);
                b2.setEnabled(false);
                textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
                Toast.makeText(MainActivity.this,"Computer's Turn",Toast.LENGTH_SHORT).show();
                handler.post(runnable);
                if(compTurn>20)
                    handler.removeCallbacks(runnable);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(runnable);
            }
        });

    }

    public int calcScore(int x){
        int sum=0;
        if(x!=0){
            sum=sum+x+1;
            imageView.setImageResource(images[x]);
            return  sum;
        }
        imageView.setImageResource(images[0]);
        return 0;
    }

    public int computerTurn(){
        int x = random.nextInt(6);
        int y = calcScore(x);
        compTurn=compTurn+y;
        textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
        if(y==0)
            return 0;
        if(compTurn>20)
            return 20;
        return 1;
    }

    public void computerDisplay(){
        compTotal=compTotal+compTurn;
        compTurn=0;
        textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
        check();
        if(flag){
            imageView.setImageResource(images[0]);
            userTotal=0;
            userTurn=0;
            compTotal=0;
            compTurn=0;
            b1.setEnabled(true);
            b2.setEnabled(false);
            textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
        }else {
            b1.setEnabled(true);
            b2.setEnabled(true);
        }
    }

    public void reset(Runnable runnable){
        handler.removeCallbacks(runnable);
        imageView.setImageResource(images[0]);
        userTotal=0;
        userTurn=0;
        compTotal=0;
        compTurn=0;
        b1.setEnabled(true);
        b2.setEnabled(false);
        textView.setText("User Total: "+userTotal+"\nComputer Total: "+compTotal+"\nUser Turn: "+userTurn+"\nComputer Turn: "+compTurn);
    }

    public void check(){
        if(userTotal>=100 && compTotal<100){
            Toast.makeText(MainActivity.this,"User Wins!\nScore: "+userTotal,Toast.LENGTH_LONG).show();
            flag =true;
        }else if(compTotal>=100 && userTotal<100){
            Toast.makeText(MainActivity.this,"Computer Wins!\nScore: "+compTotal,Toast.LENGTH_LONG).show();
            flag=true;
        }else
            flag=false;
    }
}
