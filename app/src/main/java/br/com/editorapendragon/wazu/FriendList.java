package br.com.editorapendragon.wazu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Josué on 09/05/2016.
 */
public class FriendList extends ArrayAdapter<Friends> {

        public FriendList(Context context, ArrayList<Friends> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Friends user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.friends_list, parent, false);
            }
            // Lookup view for data population
            TextView FNome = (TextView) convertView.findViewById(R.id.Friendname);
            TextView Fid = (TextView) convertView.findViewById(R.id.FriendId);
            ImageView FFoto = (ImageView) convertView.findViewById(R.id.Friendicon);
            // Populate the data into the template view using the data object
            FNome.setText(user.nome);
            Fid.setText(user.id);
            FFoto.setImageBitmap(user.foto);
            // Return the completed view to render on screen
            return convertView;
        }
}
