package br.com.editorapendragon.wazu;


import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Josué on 02/05/2016.
 */
public class RoutesJSONParser {
    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONArray jStepsIN = null;

        try {

            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            List path = new ArrayList<HashMap<String, String>>();
            int menor=10000;
            int indice = 0;
            int qparadas = 0;
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String tipo_trecho = (String) ((JSONObject) jSteps.get(k)).get("travel_mode");
                        if (tipo_trecho.equalsIgnoreCase("TRANSIT")) {
                            qparadas++;
                        }
                    }
                }
                if (menor > qparadas) {
                    menor = qparadas;
                    indice = i;
                }
                qparadas = 0;

            }
            for(int i=0;i<jRoutes.length();i++){

                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                //if (i==indice) {
                    HashMap<String, String> hmST = new HashMap<String, String>();
                    String rlat = "";
                    String rlng = "";
                    String elat = "";
                    String elng = "";

                    String Duracao = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).get("duration")).get("text");
                    String Distancia = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).get("distance")).get("text");
                    String enderecoS = (String) (((JSONObject) jLegs.get(0)).get("start_address"));
                    String enderecoD = (String) (((JSONObject) jLegs.get(0)).get("end_address"));
                    rlat = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).opt("start_location")).optString("lat");
                    rlng = (String) ((JSONObject) ((JSONObject) jLegs.get(0)).opt("start_location")).optString("lng");
                    hmST.put("rdistance", "{\"dados\" : [{ \"cabecalho\" : \"cabecalho\",\"lat\" : \""+rlat+"\",\"lng\" : \""+rlng+"\" }]}");
                    hmST.put("rdescricao", "Duração: " + Duracao+" - "+Distancia);
                    path.add(hmST);
                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String rdistance = "";
                            String rduration = "";
                            String rdescricao = "";
                            String mdescricao = "";
                            rdistance = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("distance")).get("text");
                            rduration = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("duration")).get("text");
                            rdescricao = String.valueOf(Html.fromHtml((String) ((JSONObject) jSteps.get(k)).get("html_instructions")));
                            String tipo_trecho = (String) ((JSONObject) jSteps.get(k)).get("travel_mode");

                            rlat = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("start_location")).optString("lat");
                            rlng = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("start_location")).optString("lng");
                            elat = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("end_location")).optString("lat");
                            elng = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("end_location")).optString("lng");

                            HashMap<String, String> hmIN = new HashMap<String, String>();


                            if (tipo_trecho.equalsIgnoreCase("WALKING")) {
                                hmIN.put("rdistance", "{\"dados\" : [{ \"cabwalking\" : \"cabwalking\",\"lat\" : \""+rlat+"\",\"lng\" : \""+rlng+"\",\"elat\" : \""+elat+"\",\"elng\" : \""+elng+"\" }]}");
                                hmIN.put("rdescricao", "Caminhada - Distância: " + rdistance + "\n" + "Duração: " + rduration+" - "+rdescricao);
                                path.add(hmIN);
                            }else if (tipo_trecho.equalsIgnoreCase("TRANSIT")) {

                                mdescricao = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).get("line")).get("vehicle")).optString("type");
                                if (mdescricao.equals("SUBWAY")) {
                                    hmIN.put("rdistance", "{\"dados\" : [{ \"cabmetro\" : \"cabmetro\",\"lat\" : \""+rlat+"\",\"lng\" : \""+rlng+"\",\"elat\" : \""+elat+"\",\"elng\" : \""+elng+"\" }]}");
                                }else  if (mdescricao.equals("HEAVY_RAIL")) {
                                    hmIN.put("rdistance", "{\"dados\" : [{ \"cabtrem\" : \"cabtrem\",\"lat\" : \"" + rlat + "\",\"lng\" : \"" + rlng + "\",\"elat\" : \"" + elat + "\",\"elng\" : \"" + elng + "\" }]}");
                                }else{
                                    hmIN.put("rdistance", "{\"dados\" : [{ \"cabtransit\" : \"cabtransit\",\"lat\" : \""+rlat+"\",\"lng\" : \""+rlng+"\",\"elat\" : \""+elat+"\",\"elng\" : \""+elng+"\" }]}");
                                }
                                hmIN.put("rdescricao", "Transporte Público - Distância: " + rdistance + "\n" + "Duração: " + rduration+" - "+rdescricao);
                                path.add(hmIN);
                            }
                            if (tipo_trecho.equalsIgnoreCase("WALKING")) {
                                jStepsIN = ((JSONObject) jSteps.get(k)).getJSONArray("steps");
                                for (int z = 0; z < jStepsIN.length(); z++) {
                                    rdistance = (String) ((JSONObject) ((JSONObject) jStepsIN.get(z)).get("distance")).get("text");
                                    rduration = (String) ((JSONObject) ((JSONObject) jStepsIN.get(z)).get("duration")).get("text");
                                    rdescricao = String.valueOf(Html.fromHtml((String) ((JSONObject) jStepsIN.get(z)).optString("html_instructions")));
                                    rlat = (String) ((JSONObject) ((JSONObject) jStepsIN.get(z)).opt("end_location")).optString("lat");
                                    rlng = (String) ((JSONObject) ((JSONObject) jStepsIN.get(z)).opt("end_location")).optString("lng");
                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("rdistance", "{\"dados\" : [{ \"coord\" : \"coordw\",\"lat\" : \""+rlat+"\",\"lng\" : \""+rlng+"\",\"elat\" : \""+rlat+"\",\"elng\" : \""+rlng+"\" }]}");
                                    hm.put("rdescricao", "Distância: " + rdistance + " - " + "Duração: " + rduration+"\n"+rdescricao);
                                    path.add(hm);
                                }
                            } else if (tipo_trecho.equalsIgnoreCase("TRANSIT")) {
                                mdescricao = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).get("line")).get("vehicle")).optString("type");
                                rdescricao = (String) ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).get("line")).get("vehicle")).optString("name");
                                rdescricao = rdescricao + " - ";
                                rdescricao = rdescricao + (String) ((JSONObject) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).get("line")).optString("short_name");
                                rdescricao = rdescricao + " - ";
                                rdescricao = rdescricao + (String) ((JSONObject) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).get("line")).get("name");
                                String paradas = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("transit_details")).optString("num_stops");
                                rlat = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("start_location")).optString("lat");
                                rlng = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("start_location")).optString("lng");
                                elat = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("end_location")).optString("lat");
                                elng = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).opt("end_location")).optString("lng");
                                HashMap<String, String> hm = new HashMap<String, String>();
                                if (mdescricao.equals("SUBWAY")) {
                                    hm.put("rdistance", "{\"dados\" : [{ \"coord\" : \"coordm\",\"lat\" : \"" + rlat + "\",\"lng\" : \"" + rlng + "\",\"elat\" : \"" + elat + "\",\"elng\" : \"" + elng + "\" }]}");
                                }else if (mdescricao.equals("HEAVY_RAIL")) {
                                    hm.put("rdistance", "{\"dados\" : [{ \"coord\" : \"coordTrem\",\"lat\" : \"" + rlat + "\",\"lng\" : \"" + rlng + "\",\"elat\" : \"" + elat + "\",\"elng\" : \"" + elng + "\" }]}");
                                }else{
                                    hm.put("rdistance", "{\"dados\" : [{ \"coord\" : \"coordt\",\"lat\" : \"" + rlat + "\",\"lng\" : \"" + rlng + "\",\"elat\" : \"" + elat + "\",\"elng\" : \"" + elng + "\" }]}");
                                }
                                hm.put("rdescricao", "Linha: " + rdescricao+"\nParadas: " + paradas);
                                path.add(hm);
                            } else {
                                Log.i("PUT", "ELSE");
                            }
                        }
                    }

                //}

            }
            routes.add(path);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }
}
