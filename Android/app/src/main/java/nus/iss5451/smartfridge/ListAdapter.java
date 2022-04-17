package nus.iss5451.smartfridge;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdapter extends ArrayAdapter<Item> {

    private Context context;
    private AlertDialog.Builder builder;

    public ListAdapter(Context context, ArrayList<Item> userArrayList){
        super(context,R.layout.item_layout,userArrayList);
        this.context = context;

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Please input expiry date");
        builder.setMessage("Expiry Date:");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Item item = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);

        }

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView expiryDate = convertView.findViewById(R.id.expiryDate);
        TextView addDate = convertView.findViewById(R.id.addDate);
        ImageView warning = convertView.findViewById(R.id.warning);
        ImageView warning_y = convertView.findViewById(R.id.warning_yellow);


        itemName.setText(item.type);
        addDate.setText(item.addDate);
        expiryDate.setText(item.expiredDate);

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date ex = ft.parse(item.expiredDate);
            long diff = ex.getTime() - new Date().getTime();
            if (diff <= 24 * 60 * 60 * 1000 && diff > 0) {
                warning_y.setVisibility(View.VISIBLE);
                warning.setVisibility(View.GONE);
            } else if (diff <= 0) {
                warning_y.setVisibility(View.GONE);
                warning.setVisibility(View.VISIBLE);
            }else{
                warning.setVisibility(View.GONE);
                warning_y.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            warning.setVisibility(View.GONE);
            warning_y.setVisibility(View.GONE);
        }

        return convertView;
    }
}
