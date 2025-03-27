package edu.uncc.assessment03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uncc.assessment03.models.CreditCategory;

public class CreditCategoryAdapter extends ArrayAdapter<CreditCategory> {
    public CreditCategoryAdapter(Context context, CreditCategory[] categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the layout for each item in the list
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_credit_category, parent, false);
        }

        // Get the current CreditCategory object
        CreditCategory category = getItem(position);

        // Bind the ImageView and TextView
        ImageView imageView = convertView.findViewById(R.id.imageViewCredit2);
        TextView textView = convertView.findViewById(R.id.textViewText);

        // Set the data from the CreditCategory object to the view
        if (category != null) {
            textView.setText(category.getName());
            imageView.setImageResource(category.getImageResourceId());
        }

        return convertView;
    }
}



