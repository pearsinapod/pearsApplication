package com.fb.pearsapplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.json.simple.JSONObject;


public class apioriAlgorithm {
    static JSONObject hobbyOccurences = new JSONObject();

    /*
        curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Group
                curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/_User
curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/GroupUserRelation


                curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Message


        --output JSONObject.txt

        // allows cast: System.out.println(obj.get("objectId") instanceof String);
*/

    public static String[] arrGroupObjIds(JSONObject jsonObj) {
        org.json.simple.JSONArray jsonArray = convertJSONObjToArr(jsonObj);
        int JSONArrSize = jsonArray.size();
        String[] objectIds = new String[JSONArrSize];
        for (int ele = 0; ele < JSONArrSize; ele++) {
            JSONObject obj = convertObjtoJSONObj(jsonArray.get(ele));
            String id =  (String)obj.get("objectId");
            objectIds[ele] = id;

        }
        return objectIds;
    }

    public static  Tuple[] arrGroupUserRelations(JSONObject jsonObj){ // array of Tuples (user id, group id)
        org.json.simple.JSONArray jsonArray = convertJSONObjToArr(jsonObj);
        int JSONArrSize = jsonArray.size();
        Tuple[] userGroup = new Tuple[JSONArrSize];
        for (int ele = 0; ele < JSONArrSize; ele++) {
            JSONObject obj = convertObjtoJSONObj(jsonArray.get(ele));
            JSONObject user = convertObjtoJSONObj(obj.get("user"));
            JSONObject group = convertObjtoJSONObj(obj.get("group"));
            String group_id = (String) group.get("objectId");
            String user_id = (String) user.get("objectId");
            Tuple tuples = new Tuple(user_id, group_id);
            userGroup[ele] = tuples;

        }
        return userGroup;
    }

    public static List<List<String>> gettingPowersets(String[] objectIds) {
        List<List<String>> groupIdsSubsets = Powerset.subsets((objectIds));
        return groupIdsSubsets;
    }

    public static  Map<String, ArrayList> hashMapUserGroup(Tuple[] allTuples){
        Map<String, ArrayList>  userGroupMap = new HashMap<String, ArrayList>();
        for (int ug = 0 ; ug<allTuples.length; ug++){
            Tuple current_tuple = allTuples[ug];
            String current_user_id = current_tuple.getX();
            if (userGroupMap.containsKey(current_user_id)){
                ArrayList current_value =  userGroupMap.get(current_user_id);
                current_value.add(current_tuple.getY());
                userGroupMap.put(current_tuple.getX(), current_value);
            }
            else{
                ArrayList newMapKeyValue = new ArrayList();
                newMapKeyValue.add(current_tuple.getY());
                userGroupMap.put(current_tuple.getX(), newMapKeyValue);
            }

        }
        return userGroupMap;
    }

    public static ArrayList updatingJSON(List<String> powerset, Map<String, ArrayList>  userGroupMap) { //how many times this set occurs , if it contains both object ids then count it
        int occurences_count = 0;
        boolean containsAllGroups;
        Set key_set = userGroupMap.keySet();
        ArrayList<String> arr_key_set = new ArrayList<>();
        arr_key_set.addAll(key_set);
        for (int user = 0; user < userGroupMap.size(); user++) {
            ArrayList tuple_current_groups = userGroupMap.get(arr_key_set.get(user));
            containsAllGroups = true;
            for (int powersetGroup =0 ; powersetGroup< powerset.size(); powersetGroup++){
                if (!tuple_current_groups.contains(powerset.get(powersetGroup))){
                    containsAllGroups = false;
                }
            }
            if (containsAllGroups){
                occurences_count +=1;
            }

        }
        double threshold = calculatingThreshold(occurences_count, 0, userGroupMap);
        ArrayList poweroccurthresh = new ArrayList();
        poweroccurthresh.add(powerset);
        poweroccurthresh.add(occurences_count);
        poweroccurthresh.add(threshold);
       return poweroccurthresh;
    }

    public static double calculatingThreshold(int occurences, final int minThreshold, Map<String, ArrayList> userGroupMap) { // min threshold in percentage form
        double hobbyThreshold = (occurences / userGroupMap.size()) * 100;
        if (hobbyThreshold < minThreshold) {
           hobbyThreshold = -1;
        }
        return hobbyThreshold;
    }

    public JSONObject fileToJSON(java.lang.String user, java.lang.String filename) {
        java.lang.String filePath = "/Users/" + user + "/AndroidStudioProjects/pearsApplication/app/src/main/assets/" + filename;
        File file = new File(filePath);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        java.lang.String st;
        try {
            while ((st = br.readLine()) != null) {
                JSONParser parser = new JSONParser();
                JSONObject json = null;
                try {
                    json = (JSONObject) parser.parse(st);
                } catch (org.json.simple.parser.ParseException e) {
                    e.printStackTrace();
                }
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static org.json.simple.JSONArray convertJSONObjToArr(JSONObject JSONObj) {
        java.lang.Object results = JSONObj.get("results");
        org.json.simple.JSONArray JSONarr = (org.json.simple.JSONArray) results;
        return JSONarr;

    }

    public static JSONObject convertObjtoJSONObj(java.lang.Object obj) {
        return (JSONObject) obj;
    }

    public static org.json.simple.JSONArray convertObjToJSONArr(java.lang.Object obj) {
        return (org.json.simple.JSONArray) obj;
    }

    public static JSONObject creatingJSON() {
        apioriAlgorithm a = new apioriAlgorithm();
        JSONObject json = a.fileToJSON("angcast", "JSONGroup.json");
        List<List<String>> powerset= gettingPowersets(arrGroupObjIds(json));

        JSONObject json2 = a.fileToJSON("angcast","GroupUserRelations.json");
        Tuple[] gur = arrGroupUserRelations(json2);
        Map<String, ArrayList> map = hashMapUserGroup(gur);
        JSONArray arr = new JSONArray();
        for (int subset = 0 ; subset<powerset.size(); subset++){
            ArrayList subset_info = updatingJSON(powerset.get(subset), map);
            JSONObject one_object = new JSONObject();
            one_object.put("powerset", subset_info.get(0));
            one_object.put("occurences", subset_info.get(1));
            one_object.put("threshold", subset_info.get(2));
            arr.add(one_object);
        }
        JSONObject final_object = new JSONObject();
        final_object.put("results", arr);
        return final_object;
    }


    public static void main(java.lang.String args[]) {
        System.out.println(creatingJSON());

    }
}