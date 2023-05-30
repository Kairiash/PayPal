package com.example.paypal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.affordabletherapyafrica.paypal.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    EditText amount;
    Button btn;

    public static final String clientId="";
    public static final int PAYPAL_REQUEST_CODE = 123;
    public static PayPalConfiguration configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amount = findViewById(R.id.amount);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayment();
            }
        });
    }

    private void getPayment() {

        String amounts = amount.getText().toString();

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amounts), "EUR","Learn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE){

            PaymentConfirmation config = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (config != null){

                try {
                    String paymentDetails = config.toJSONObject().toString(4);

                    JSONObject payObj = new JSONObject(paymentDetails);
                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("Error","Something went wrong");
                }

            }

        } else if (requestCode == Activity.RESULT_CANCELED){
            Log.i("Error","Something went wrong");
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Log.i("Payment","INVALID PAYMENT");
        }

    }
}