package ripple.com.rippleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ripple.com.rippleview.view.RippleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RippleView rippleView = findViewById(R.id.ripple_view);

        rippleView.setMixRadius(100);
        rippleView.setmDuration(2000);
        rippleView.setmRate(500);
        rippleView.setColor(getResources().getColor(R.color.colorAccent));
        rippleView.start();
    }
}
