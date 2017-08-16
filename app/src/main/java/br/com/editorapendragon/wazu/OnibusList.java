package br.com.editorapendragon.wazu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Josué on 09/05/2016.
 */
public class OnibusList extends ArrayAdapter<FavOnibus> {

        public OnibusList(Context context, ArrayList<FavOnibus> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final FavOnibus user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
               convertView = LayoutInflater.from(getContext()).inflate(R.layout.favonibus_list, parent, false);
            }

            // Lookup view for data population
            TextView FNome = (TextView) convertView.findViewById(R.id.txtFavONomeList);
            TextView FId = (TextView) convertView.findViewById(R.id.txtFavOnibusID);
            // Populate the data into the template view using the data object
            FNome.setText(user.getNumero().toString());
            FId.setText(String.valueOf(user.getId()));
            // Return the completed view to render on screen
            return convertView;
        }
}
