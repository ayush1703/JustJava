package in.co.ayushjain.justjava;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends Activity {

    private int numberOfCoffees=2;
    String billName;
    String orderSummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void submitOrder(View view) {
        CheckBox cream = (CheckBox) findViewById(R.id.checkBox);
        CheckBox addChoclate = (CheckBox) findViewById(R.id.checkBox1);
        EditText nameOrder = (EditText) findViewById(R.id.editText);
        billName = nameOrder.getText().toString();
        boolean wCream=  (cream.isChecked());
        boolean choc = (addChoclate.isChecked());
        displayQuantity(numberOfCoffees);
        //createOrderSummary(calculatePrice(numberOfCoffees));
        orderSummary= createOrderSummary(calculatePrice(numberOfCoffees,wCream,choc),wCream,choc,billName);
        displayMessage( orderSummary);
SendEmail sm =new SendEmail();
        sm.execute();
        
        //displayPrice(numberOfCoffees *5);

    }

    public void increment(View view)
    {
        if(numberOfCoffees==100)
        {
            Toast.makeText(this,"Max 100 coffee could be ordered",Toast.LENGTH_SHORT).show();
            return;}

        numberOfCoffees++;
        displayQuantity(numberOfCoffees);
    }
    public void decrement(View view)
    {
        if(numberOfCoffees==1)
        {
            Toast.makeText(this,"Minimum one coffee needs to be ordered",Toast.LENGTH_SHORT).show();
            return;}
        numberOfCoffees--;
        displayQuantity(numberOfCoffees);
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

   /* private void displayPrice(int number)
    {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }*/
    private void displayMessage(String message)
    {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    private int calculatePrice(int quantity,boolean cream, boolean choclate) {
        int basePrice=5;
        if(cream==true){basePrice=basePrice+1;}
        if(choclate==true){basePrice=basePrice+2;}
        int price = quantity * basePrice;
        return price;
    }
    private String createOrderSummary(int totalPrice,boolean addCream, boolean addChoc, String name)
    {
        String pricemessage=getString(R.string.order_summary_name,name)+"\n"+getString(R.string.order_summary_quantity,numberOfCoffees)+"\n"+getString(R.string.order_summary_whipped_cream,addCream)+"\n" +
                getString(R.string.order_summary_chocolate,addChoc)+" \n"+getString(R.string.order_summary_price,totalPrice)+"\n" + getString(R.string.thank_you);
return pricemessage;
    }
    private class SendEmail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                GMailSender sender = new GMailSender("sender_email@gmail.com", "password");
                sender.sendMail("Coffee Order for: "+billName,
                        orderSummary,
                        "sender_email@gmail.com",
                        "receiver_email@gmail.com");
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }
        Log.i("Email sent", "from Async task");
            return null;
        }

    }
}

