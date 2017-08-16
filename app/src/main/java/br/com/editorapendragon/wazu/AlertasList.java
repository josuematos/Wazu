package br.com.editorapendragon.wazu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Josué on 09/05/2016.
 */
public class AlertasList extends ArrayAdapter<Alertas> {

        public AlertasList(Context context, ArrayList<Alertas> entrada) {
            super(context, 0, entrada);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Alertas entrada = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.alertas_list, parent, false);
            }
            // Lookup view for data population
            TextView FNome = (TextView) convertView.findViewById(R.id.txtVerAlertaUsuario);
            TextView FComentario = (TextView) convertView.findViewById(R.id.txtVerAlertaComentario);
            TextView FLat = (TextView) convertView.findViewById(R.id.txtVerAlertasLat);
            TextView FLng = (TextView) convertView.findViewById(R.id.txtVerAlertasLng);

            // Populate the data into the template view using the data object
            FNome.setText(entrada.usuario);
            FComentario.setText(entrada.comentario);
            FLat.setText(entrada.lat);
            FLng.setText(entrada.lng);

            // Return the completed view to render on screen
            return convertView;
        }
}
