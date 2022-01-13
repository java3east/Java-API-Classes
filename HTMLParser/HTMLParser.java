package net.ansc.jb.util;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTMLParser
{

    private static final List<String> ignoredObjects = new ArrayList<String>(Arrays.asList("<br>", "<hr>"));

    public static List<HTMLObject> parse(String htmlString)
    {
        htmlString = htmlString.replace("<!DOCTYPE html>", "");

        List<HTMLObject> obs = new ArrayList<>();

        char currentChar;
        int currentIndex = 0;
        StringBuilder inner = new StringBuilder();
        StringBuilder data = new StringBuilder();
        boolean open = true;
        boolean addData = false;
        int innerCount = 0;
        boolean ignore = false;
        while(currentIndex < htmlString.length())
        {
            currentChar = htmlString.charAt(currentIndex);

            if(currentChar == '<')
            {
                open = htmlString.charAt(currentIndex + 1) != '/';

                if(open){
                    addData = innerCount == 0;
                    innerCount++;
                }else
                {
                    innerCount--;
                    addData = innerCount == 0;
                }

                if(addData)
                {
                    data.append(currentChar);
                }else
                {
                    inner.append(currentChar);
                }


                //<br>
                int i = currentIndex;
                StringBuilder s = new StringBuilder();
                s.append(currentChar);
                char c = currentChar;
                while (c != '>')
                {
                    i++;
                    c = htmlString.charAt(i);
                    s.append(c);
                }

                if(ignoredObjects.contains(s.toString()))
                    ignore = true;






            }else if(currentChar == '>')
            {
                if((innerCount < 2 && open) || innerCount < 1)
                {
                    data.append(currentChar);
                }else
                    inner.append(currentChar);


                if(open && innerCount <= 1 && ignore)
                {
                    inner.append(data);
                    data = new StringBuilder();
                }

                if(ignore) {
                    innerCount--;
                    ignore = false;
                }

                if(!open && addData)
                {
                    String _inner_ = inner.toString();
                    String _data_ = data.toString();
                    inner = new StringBuilder();
                    data = new StringBuilder();

                    if(!ignoredObjects.contains(_data_))
                    {
                        HTMLObject obj = new HTMLObject(_data_);
                        obj.parseInner(_inner_);
                        obs.add(obj);
                    }

                    addData = false;
                }else if(innerCount == 1)
                {
                    addData = false;
                }

            }else if(addData)
            {
                data.append(currentChar);
            }else
                inner.append(currentChar);


            currentIndex++;
        }

        if(!data.toString().contains("<"))
        {
            inner.append(data);
        }

        if(inner.toString().length() > 0)
        {
            obs.add(new HTMLObject(inner.toString()));
        }
        return obs;
    }

    public static String toJsonString(String html)
    {
        List<HTMLObject> htmlObjects = parse(html);

        JSONArray array = new JSONArray();
        array.addAll(htmlObjects);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", array);

        return jsonObject.toJSONString();
    }

    public static class HTMLObject {

        public List<HTMLObject> inner = new ArrayList<>();
        public final String data;

        public HTMLObject(String data)
        {
            this.data = data;
        }

        public void setInner(List<HTMLObject> inner)
        {
            this.inner = inner;
        }

        public void parseInner(String innerHTML)
        {
            setInner(HTMLParser.parse(innerHTML));
        }

        public String[] getLines()
        {
            return data.split("<br>");
        }

        @Override
        public String toString()
        {
            return "{" +
                    "\"data\" : \"" + data +
                    "\", \"inner\" : " + inner +
                    "}";
        }

        public List<HTMLObject> getByData(String data)
        {
            List<HTMLObject> ret = new ArrayList<>();

            for(HTMLObject object:inner)
            {
                if(object.data.equals(data))
                {
                    ret.add(object);
                }
            }

            return ret;
        }

        public JSONObject toJSONObject()
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", data);

            JSONArray _inner_ = new JSONArray();
            for(HTMLObject obj:inner)
            {
                _inner_.add(obj);
            }

            jsonObject.put("inner", _inner_);

            return jsonObject;
        }
    }
}
