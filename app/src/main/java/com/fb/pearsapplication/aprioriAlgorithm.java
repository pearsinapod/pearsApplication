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
    /* NOTES:

        A. getting things from terminal:
            curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Group
            curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/_User
            curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/GroupUserRelation
            curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Message

        B. Saving what curl function returns to a variable
            INSTANCE_REGION=$(curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/Group)
            INSTANCE_REGION=$(curl -X GET \-H "X-Parse-Application-Id: fbu-pears" \ -H "X-Parse-Master-Key: AngelicaAarushiJaleel" \http://fbu-pears.herokuapp.com/parse/classes/GroupUserRelation)

        echo $INSTANCE_REGION

        C. allows cast: System.out.println(obj.get("objectId") instanceof String);
        */

public class aprioriAlgorithm {

    // returns every single group object id in a String Array
    public static String[] arrGroupObjIds(JSONObject jsonObj) {
        org.json.simple.JSONArray JAParseGroups = convertJSONObjToArr(jsonObj);
        int JAGroupsSize = JAParseGroups.size();
        String[] objectIds = new String[JAGroupsSize];
        for (int group = 0; group < JAGroupsSize; group++) {
            JSONObject JOGroupInfo = convertObjtoJSONObj(JAParseGroups.get(group));
            String groupObjectId =  (String)JOGroupInfo.get("objectId");
            objectIds[group] = groupObjectId;
        }
        return objectIds;
    }

    // returns Array of Tuples containing Tuples that have user object ids and group object ids

    public static  Tuple[] arrGroupUserRelations(JSONObject jsonObj){ // array of Tuples (user object id, group object id)
        org.json.simple.JSONArray JAParseGUR = convertJSONObjToArr(jsonObj);
        int JAGURSize = JAParseGUR.size();
        Tuple[] userGroupIds = new Tuple[JAGURSize];
        for (int relation = 0; relation < JAGURSize; relation++) {
            JSONObject obj = convertObjtoJSONObj(JAParseGUR.get(relation));
            JSONObject user = convertObjtoJSONObj(obj.get("user"));
            JSONObject group = convertObjtoJSONObj(obj.get("group"));
            String group_id = (String) group.get("objectId");
            String user_id = (String) user.get("objectId");
            Tuple tuples = new Tuple(user_id, group_id);
            userGroupIds[relation] = tuples;

        }
        return userGroupIds;
    }

    // returns powerset of String array

    public static List<List<String>> gettingPowersets(String[] collection) {
        List<List<String>> powerset = Powerset.subsets((collection));
        return powerset;
    }

    // Used to map every user with every group they are in ; returns this map

    public static  Map<String, ArrayList> hashMapUserGroup(Tuple[] allTuples){
        Map<String, ArrayList>  userGroupMap = new HashMap<String, ArrayList>();
        for (int ug = 0 ; ug<allTuples.length; ug++){
            Tuple current_tuple = allTuples[ug];
            String current_user_object_id = current_tuple.getX();
            if (userGroupMap.containsKey(current_user_object_id)){
                ArrayList updated_groups =  userGroupMap.get(current_user_object_id);
                updated_groups.add(current_tuple.getY());
                userGroupMap.put(current_tuple.getX(), updated_groups);
            }
            else{
                ArrayList newUserGroups = new ArrayList();
                newUserGroups.add(current_tuple.getY());
                userGroupMap.put(current_tuple.getX(), newUserGroups);
            }

        }
        return userGroupMap;
    }

    // calculates how many times this subset occurs in the user group map ; return arrayList of subset, occurrence, and threshold of that
    // subset
    public static ArrayList gettingOccurrences(List<String> powerset, Map<String, ArrayList>  userGroupMap) {
        int occurrencesCount = 0;
        boolean containsAllGroups;
        Set setAllKeys = userGroupMap.keySet();
        ArrayList<String> arrAllKeys = new ArrayList<>();
        arrAllKeys.addAll(setAllKeys);
        for (int user = 0; user < userGroupMap.size(); user++) {
            ArrayList tuple_current_groups = userGroupMap.get(arrAllKeys.get(user));
            containsAllGroups = true;
            for (int groupObjectId =0 ; groupObjectId< powerset.size(); groupObjectId++){
                if (!tuple_current_groups.contains(powerset.get(groupObjectId))){
                    containsAllGroups = false;
                }
            }
            if (containsAllGroups){
                occurrencesCount +=1;
            }

        }
        double threshold = calculatingThreshold(occurrencesCount, 15, userGroupMap);

        ArrayList subOccurThresh = new ArrayList();
        subOccurThresh.add(powerset);
        subOccurThresh.add(occurrencesCount);
        subOccurThresh.add(threshold);
       return subOccurThresh;
    }

    // returns threshold of subset
    public static double calculatingThreshold(int occurences, final int minThreshold, Map<String, ArrayList> userGroupMap) { // min threshold in percentage form
        double subsetThreshold = ((double)occurences / userGroupMap.size()) * 100;
        if (subsetThreshold < minThreshold) {
           subsetThreshold = -1;
        }
        return subsetThreshold;
    }

    // reads file and returns JSONObject
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

    // converts JSON Object to JSONArray and returns JSONArray
    public static org.json.simple.JSONArray convertJSONObjToArr(JSONObject JSONObj) {
        java.lang.Object results = JSONObj.get("results");
        org.json.simple.JSONArray JSONarr = (org.json.simple.JSONArray) results;
        return JSONarr;

    }

    // converts Object to JSONObject and returns JSONObject
    public static JSONObject convertObjtoJSONObj(java.lang.Object obj) {
        return (JSONObject) obj;
    }

    //converts Object to JSONArray and returns JSONArray
    public static org.json.simple.JSONArray convertObjToJSONArr(java.lang.Object obj) {
        return (org.json.simple.JSONArray) obj;
    }

    //Builds final JSON Object with all the subsets, their occurrences, and their threshold
    public static JSONObject creatingJSON(String groupFileName, String GURFileName) {
        aprioriAlgorithm apriori = new aprioriAlgorithm();
        JSONObject jsonAllGroups = apriori.fileToJSON("angcast", groupFileName);
        List<List<String>> powersetGroups= gettingPowersets(arrGroupObjIds(jsonAllGroups));
        JSONObject jsonAllGUR = apriori.fileToJSON("angcast",GURFileName);
        Tuple[] tupleUserGroup = arrGroupUserRelations(jsonAllGUR);
        Map<String, ArrayList> userGroupMap = hashMapUserGroup(tupleUserGroup);
        JSONArray allSubsetsInfo = new JSONArray();
        for (int subset = 0 ; subset<powersetGroups.size(); subset++){
            ArrayList subset_info = gettingOccurrences(powersetGroups.get(subset), userGroupMap);
            JSONObject one_object = new JSONObject();
            double threshold = (double)subset_info.get(2);
            if (threshold!=-1){
                one_object.put("subset", subset_info.get(0));
                one_object.put("occurrences", subset_info.get(1));
                one_object.put("threshold", subset_info.get(2));
                allSubsetsInfo.add(one_object);
            }
        }
        JSONObject final_object = new JSONObject();
        final_object.put("results", allSubsetsInfo);
        return final_object;
    }

    public static void main(java.lang.String args[]) {
        String groupFileName = "JSONGroupTESTING.json";
        String GURFileName = "GroupUserRelationsTESTING.json";
       // System.out.println(creatingJSON(groupFileName, GURFileName));
        //JSONObject jsonAllGUR = apriori.fileToJSON("angcast", "JSONParseGroupUserRelations.jsonAllGroups");
        //JSONObject jsonAllGroups = apriori.fileToJSON("angcast", "JSONParseGroups.jsonAllGroups");
    }
}