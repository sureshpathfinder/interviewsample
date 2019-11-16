package com.example.contactsample.utils;

// Java import
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashMap;

// JSON import
import org.json.JSONObject;
import org.json.JSONArray;

public class HttpDataWrapper
{

    public static String getString(Object obj)
    {
        try
        {
            if(obj instanceof Hashtable)
            {
                return constructJSON((Hashtable)obj, false).toString();
            }
            else if(obj instanceof ArrayList)
            {
                return constructJSON((ArrayList)obj, false).toString();
            }
            else if(obj instanceof HashMap)
            {
                return constructJSON((HashMap)obj, false).toString();
            }
            else
            {
                return obj.toString();
            }
        }
        catch(Exception exp)
        {
            return null;
        }
    }

    public static String getValuesAsString(Object obj)
    {
        try
        {
           if(obj instanceof Hashtable)
            {
                return constructJSON((Hashtable)obj, true).toString();
            }
            else if(obj instanceof ArrayList)
            {
                return constructJSON((ArrayList)obj, true).toString();
            }
            else if(obj instanceof HashMap)
            {
                return constructJSON((HashMap)obj, true).toString();
            }
            else
            {
                return obj.toString();
            }
        }
        catch(Exception exp)
        {
            return null;
        }
    }


    public static Object clone(Object obj)
    {
        try
        {
            if(obj instanceof Hashtable)
            {
                return (Hashtable) getObject(getString(obj));
            }
            else if (obj instanceof ArrayList)
            {
                return (ArrayList) getObject(getString(obj));
            }
            else
            {
                return obj;
            }
        }
        catch(Exception exp)
        {
            return null;
        }
    }

    private static JSONObject constructJSON(Hashtable ht, boolean converttostr) throws Exception
    {
        JSONObject obj = new JSONObject();

        for(Enumeration e=ht.keys();e.hasMoreElements();)
        {
            Object key = e.nextElement();
            Object value = ht.get(key);
            if(value instanceof Hashtable)
            {
                obj.put(""+key, constructJSON((Hashtable) value, converttostr));
            }
            else if(value instanceof ArrayList)
            {
                obj.put(""+key, constructJSON((ArrayList) value, converttostr));
            }
            else if(value instanceof HashMap)
            {
                obj.put(""+key, constructJSON((HashMap) value, converttostr));
            }
            else
            {
                obj.put(""+key, (converttostr) ? ""+value : value);
            }
        }

        return obj;
    }


    private static JSONObject constructJSON(HashMap hm, boolean converttostr) throws Exception
    {
        JSONObject obj = new JSONObject();

        for(Iterator it=hm.keySet().iterator(); it.hasNext();)
        {
            Object key =  it.next();
            Object value = hm.get(key);
            if(value instanceof Hashtable)
            {
                obj.put(""+key, constructJSON((Hashtable) value, converttostr));
            }
            else if(value instanceof HashMap)
            {
                obj.put(""+key, constructJSON((HashMap) value, converttostr));
            }
            else if(value instanceof ArrayList)
            {
                obj.put(""+key, constructJSON((ArrayList) value, converttostr));
            }
            else
            {
                obj.put(""+key, (converttostr) ? ""+value : value);
            }
        }

        return obj;
    }

    private static JSONArray constructJSON(ArrayList al, boolean converttostr) throws Exception
    {
        JSONArray ar = new JSONArray();

        for(Iterator it=al.iterator(); it.hasNext();)
        {
            Object value = it.next();
            if(value instanceof Hashtable)
            {
                ar.put(constructJSON((Hashtable) value, converttostr));
            }
            else if(value instanceof ArrayList)
            {
                ar.put(constructJSON((ArrayList) value, converttostr));
            }
            else if(value instanceof HashMap)
            {
                ar.put(constructJSON((HashMap) value, converttostr));
            }
            else
            {
                ar.put((converttostr) ? ""+value : value);
            }
        }

        return ar;
    }

    public static Object getObject(String jstr)
    {
        try
        {
            if(jstr.startsWith("{"))
            {
                return getObject(new JSONObject(jstr));
            }
            else if(jstr.startsWith("["))
            {
                return getObject(new JSONArray(jstr));
            }
            return jstr;
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
            return null;
        }
    }

    public static Object getMap(String jstr)
    {
        try
        {
            if(jstr.startsWith("{"))
            {
                return getMap(new JSONObject(jstr));
            }
            else if(jstr.startsWith("["))
            {
                return getObject(new JSONArray(jstr));
            }
            return jstr;
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
            return null;
        }
    }

    private static Hashtable getObject(JSONObject jo) throws Exception
    {
        Hashtable ht = new Hashtable();

        for(Iterator it=jo.keys();it.hasNext();)
        {
            String key = (String) it.next();
            Object value = jo.get(key);
            if(value instanceof JSONObject)
            {
                ht.put(key, getObject((JSONObject) value));
            }
            else if(value instanceof JSONArray)
            {
                ht.put(key, getObject((JSONArray) value));
            }
            else
            {
                ht.put(key,value);
            }
        }

        return ht;
    }


    public static HashMap getMap(JSONObject jo) throws Exception
    {
        HashMap hm = new HashMap();

        for(Iterator it=jo.keys();it.hasNext();)
        {
            String key = (String) it.next();
            Object value = jo.get(key);
            if(value instanceof JSONObject)
            {
                hm.put(key, getMap((JSONObject) value));
            }
            else if(value instanceof JSONArray)
            {
                hm.put(key, getMap((JSONArray) value));
            }
            else
            {
                hm.put(key,value);
            }
        }

        return hm;
    }

    private static ArrayList getMap(JSONArray ja) throws Exception
    {
        ArrayList al = new ArrayList();

        for(int i=0; i<ja.length(); i++)
        {
            Object value = ja.get(i);
            if(value instanceof JSONObject)
            {
                al.add(getMap((JSONObject) value));
            }
            else if(value instanceof JSONArray)
            {
                al.add(getMap((JSONArray) value));
            }
            else
            {
                al.add(value);
            }
        }

        return al;
    }

    private static ArrayList getObject(JSONArray ja) throws Exception
    {
        ArrayList al = new ArrayList();

        for(int i=0; i<ja.length(); i++)
        {
            Object value = ja.get(i);
            if(value instanceof JSONObject)
            {
                al.add(getObject((JSONObject) value));
            }
            else if(value instanceof JSONArray)
            {
                al.add(getObject((JSONArray) value));
            }
            else
            {
                al.add(value);
            }
        }

        return al;
    }
}