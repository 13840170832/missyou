package com.lin.missyou.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericAndJson {

    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper){
        GenericAndJson.mapper = mapper;
    }

    public static <T> String objectToJson(T o){
        try{
            return GenericAndJson.mapper.writeValueAsString(o);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    public static <T> T jsonToObject(String s,TypeReference<T> tr) {
        if(null == s){
            return null;
        }
        try{
            T o = GenericAndJson.mapper.readValue(s,tr);
            return o;
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

}
