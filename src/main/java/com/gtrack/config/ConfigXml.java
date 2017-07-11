package com.gtrack.config;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;


public class ConfigXml {


    public static HashMap<String, HashMap<String, String>>  configMap = new HashMap<>();

    public static HashMap<String, String> getConfig(String key){

        if(configMap.containsKey(key)){

            return configMap.get(key);

        }else{
            HashMap<String, String> map = readConfig(key);
            configMap.put(key, map);
            return map;
        }

    }
    public static HashMap<String, String> readConfig(String key) {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            //File f = new File("H://config.xml");
            File f = new File(ConstDefine.CONFIG_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            NodeList nl = doc.getElementsByTagName(key);
            for (int i = 0; i < nl.getLength(); i++) {
                Element s = (Element) nl.item(i);
                NodeList tm = s.getChildNodes();
                for (int j = 0; j < tm.getLength(); j++) {
                    if (!tm.item(j).getNodeName().startsWith("#")) {
                        String map_key = tm.item(j).getNodeName();
                        String map_val = s.getElementsByTagName(map_key)
                                .item(0).getTextContent();
                        map.put(map_key, map_val);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

}
