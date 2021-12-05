package com.hust.smartparking.utils;

import org.springframework.web.multipart.MultipartFile;

public class Utils {
    public static boolean checkValidFile(MultipartFile file){
        if(file!=null && !file.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
