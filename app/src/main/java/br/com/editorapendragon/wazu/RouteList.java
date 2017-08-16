package br.com.editorapendragon.wazu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Josué on 09/05/2016.
 */
public class RouteList extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listGroup;
    private HashMap<String, List<String>> listChild;


    public RouteList(Context context, List<String> listGroup,
                     HashMap<String, List<String>> listChild) {
        this.context = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
    }

    public void clearData() {
        // clear the data
        this.listGroup.clear();
        this.listChild.clear();
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listGroup.get(groupPosition)).get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.route_group_list, null);
        }
        String textGroup = (String) getGroup(groupPosition);
            TextView FNome = (TextView) convertView.findViewById(R.id.gRoutename);
            TextView FNum = (TextView) convertView.findViewById(R.id.gRoutenumber);
            FNome.setText(textGroup);
            FNum.setText(String.valueOf(groupPosition+1));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.route_list, null);
        }
        String text = (String) getChild(groupPosition, childPosition);

        try{
            JSONObject jObject = new JSONObject(text);
            JSONArray jRoutes;
            jRoutes = jObject.getJSONArray("dados");
            JSONObject data = jRoutes.getJSONObject(0);
            Double jLat=0.0;
            Double jLng=0.0;
            Double eLat=0.0;
            Double eLng=0.0;
            jLat = Double.valueOf(data.optString("lat"));
            jLng = Double.valueOf(data.optString("lng"));
            eLat = Double.valueOf(data.optString("elat"));
            eLng = Double.valueOf(data.optString("elng"));

            TextView FNome = (TextView) convertView.findViewById(R.id.Routename);
            ImageView FIcone = (ImageView) convertView.findViewById(R.id.Routeicon);
            TextView FLat = (TextView) convertView.findViewById(R.id.RouteLat);
            TextView FLng = (TextView) convertView.findViewById(R.id.RouteLng);
            FNome.setText(data.optString("descricao"));
            FLat.setText(data.optString("lat"));
            FLng.setText(data.optString("lng"));

            if (data.optString("icone").equals("WALKING")) {
                FIcone.setImageResource(R.drawable.andando);
            }else if (data.optString("icone").equals("TRANSIT")) {
                FIcone.setImageResource(R.drawable.buslado);
            }else if (data.optString("icone").equals("SUBWAY")) {
                FIcone.setImageResource(R.drawable.metro);
            }else if (data.optString("icone").equals("HSUBWAY")) {
                FIcone.setImageResource(R.drawable.metrocolorido);
            }else if (data.optString("icone").equals("TRAIN")) {
                FIcone.setImageResource(R.drawable.train);
            }else if (data.optString("icone").equals("HTRAIN")) {
                FIcone.setImageResource(R.drawable.cabtrem);
            }else if (data.optString("icone").equals("HEADER")) {
                FIcone.setImageResource(R.drawable.wazu);
            }else if (data.optString("icone").equals("HTRANSIT")) {
                FIcone.setImageResource(R.drawable.busladocolorido);
            }else if (data.optString("icone").equals("HWALKING")) {
                FIcone.setImageResource(R.drawable.andandocolorido);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
