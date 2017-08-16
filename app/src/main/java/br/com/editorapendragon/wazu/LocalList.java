package br.com.editorapendragon.wazu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Josué on 09/05/2016.
 */


public class LocalList extends ArrayAdapter<FavLocal> {

        public LocalList(Context context, ArrayList<FavLocal> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            FavLocal user = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.favlocal_list, parent, false);
            }
            // Lookup view for data population
            TextView FNome = (TextView) convertView.findViewById(R.id.txtFavNomeList);
            TextView FId = (TextView) convertView.findViewById(R.id.txtFavLocalID);
            TextView FLat = (TextView) convertView.findViewById(R.id.txtFavLocalLat);
            TextView FLng = (TextView) convertView.findViewById(R.id.txtFavLocalLng);
            // Populate the data into the template view using the data object
            FNome.setText(user.getNome().toString());
            FId.setText(String.valueOf(user.getId()));
            FLat.setText(String.valueOf(user.getLat()));
            FLng.setText(String.valueOf(user.getLng()));

            // Return the completed view to render on screen
            return convertView;
        }
}
