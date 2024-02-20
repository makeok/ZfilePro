package im.zhaojun.zfile.core.util;

import com.alibaba.fastjson2.JSONObject;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @author lipeng
 * @date 2021/11/4 10:26
 */
@Slf4j
public class ImgUtil {
    // 读取图片的全部信息
    public static Map<String, String> readImgAll(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Map<String,String> map = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                map.put(tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    log.error("ERROR: %s", error);
                }
            }
        }
        return map;
    }
    public static Date readImgCaptrueTime(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directory != null) {
            Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            return date;
        }
        return null;
    }
    // 读取图片的常用信息,并将信息封装到集合里面
    public static Map readImgNormal(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Map<String,String> map = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                if (tagName.equals("Image Height")) {
                    map.put("imgHeigh" , desc);
                } else if (tagName.equals("Image Width")) {
                    map.put("imgWeith" , desc);
                } else if (tagName.equals("Date/Time Original")) {
                    map.put("time" , desc);
                } else if (tagName.equals("GPS Latitude")) {
                    map.put("lat" , GpsUtil.pointToLatlong(desc));
                } else if (tagName.equals("GPS Longitude")) {
                    map.put("lon" , GpsUtil.pointToLatlong(desc));
                }
            }
        }
        return map;
    }
    /**
     * api_key：注册的百度api的key
     * coords：经纬度坐标
     * http://api.map.baidu.com/reverse_geocoding/v3/?ak="+api_key+"&output=json&coordtype=wgs84ll&location="+coords
     * <p>
     * 经纬度转地址信息
     *
     * @param lat  维度
     * @param lon  经度
     * @return add 地址
     */
    public static Map parseAddress(String lon, String lat, String apiKey) throws IOException {
        Map<String,String> adress = new HashMap<>();
        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey + "&output=json&coordtype=wgs84ll&location=" + (lat + "," + lon);
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line+"\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(res);
        if (object.containsKey("result")) {
            JSONObject result = object.getJSONObject("result");
            if (result.containsKey("addressComponent")) {
                JSONObject addressObj = object.getJSONObject("result").getJSONObject("addressComponent");
                adress.put("country", (String) addressObj.get("country"));
                adress.put("province", (String) addressObj.get("province"));
                adress.put("city", (String) addressObj.get("city"));
                adress.put("district", (String) addressObj.get("district"));
                adress.put("street", (String) addressObj.get("street"));
            }
        }else {
            adress = null;
        }
        return adress;
    }
    // 将图片里面的经纬度通过百度地图的api解析成详细的地址
    public static Map parseAddressByImg(File file, String apiKey) throws ImageProcessingException, IOException {
        Map map = readImgNormal(file);
        if(map.get("lon") == null || map.get("lat") == null) {
            return null;
        }
        String lat = (String) map.get("lat");
        String lon = (String) map.get("lon");
        return parseAddress(lon, lat, apiKey);
    }

    public static void main(String[] args) throws ImageProcessingException, IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\IMG_20231104_124925.jpg");
        ImgUtil.readImgAll(file);
        Map map = ImgUtil.readImgNormal(file);
        System.out.println(map);
        Map add1 = ImgUtil.parseAddress("116.3039", "39.97646", "YNxcSCAphFvuPD4LwcgWXwC3SEZZc7Ra");
        System.out.println(add1);
        Map add2 = ImgUtil.parseAddressByImg(file, "YNxcSCAphFvuPD4LwcgWXwC3SEZZc7Ra");
        System.out.println(add2);
    }
}
