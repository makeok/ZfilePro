package im.zhaojun.zfile.module.storage.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @project: hitec_product_2.1
 * @author: youxin
 * @date: 2020/12/31 09:36
 * @version: 1.0
 * @description:
 */
public enum FileItemTypeEnum {

    JPG(1, "jpg"),
    JPEG(2, "jpeg"),
    BMP(3, "bmp"),
    PNG(4, "png"),
    GIF(7, "gif"),



    AVI(21, "avi"),
    MP4(22, "mp4"),

    RM(23, "rm"),

    MKV(25, "mkv"),
    FLV(26, "rm"),
    RMVB(27, "rmvb"),

    ZIP(41, "zip"),
    RAR(42, "rar"),
    GZ(43, "gz"),
    TZ(44, "7z"),
    TAR(45, "tar"),
    ISO(45, "iso"),


    MP3(61, "mp3"),
    CDA(62, "cda"),
    WAV(63, "wav"),
    AIF(64, "aif"),
    AIFF(65, "aiff"),
    MID(66, "mid"),
    WMA(67, "wma"),
    RA(68, "ra"),
    VQF(69, "vqf"),
    APE(70, "ape"),
    M4A(71, "m4a"),
    OGG(72, "ogg"),
    FLAC(73, "flac"),
    AAC(74, "aac"),
    ASF(75, "asf"),

    DOC(81, "doc"),
    DOCX(82, "docx"),
    PPT(83, "ppt"),
    PPTX(84, "pptx"),
    XLS(85, "xls"),
    XLSX(86, "xlsx"),
    PDF(87, "pdf"),
    TXT(88, "txt"),
    CSV(89, "csv"),
    XML(90, "xml"),
    INI(91, "ini"),
    MD(92, "md"),
    YML(93, "yml"),
    PROPERTIES(94, "properties"),
    CFG(95, "cfg"),
    LOG(96, "log"),
    ;

    /**
     * 图片格式
     */
    public static List<String> IMAGE = Arrays.asList(JPEG.getName(),JPG.getName(),BMP.getName(),PNG.getName(),GIF.getName());

    //压缩文件格式
    public static List<String> ZIPS = Arrays.asList(ZIP.getName(),RAR.getName(),GZ.getName(),TZ.getName(),TAR.getName(),ISO.getName());

    public static List<String> VEDIOS = Arrays.asList(AVI.getName(),MP4.getName(),RM.getName(),FLV.getName(),MKV.getName(),RMVB.getName());

    public static List<String> MUSICS = Arrays.asList(MP3.getName(),CDA.getName(),WAV.getName(),AIF.getName(),
            AIFF.getName(),MID.getName(),WMA.getName(),RA.getName(),VQF.getName(),APE.getName(),M4A.getName(),
            OGG.getName(),FLAC.getName(),AAC.getName(),ASF.getName());

    public static List<String> DOCMENTS = Arrays.asList(DOC.getName(),DOCX.getName(),PPT.getName(),PPTX.getName(),
            XLS.getName(),XLSX.getName(),PDF.getName(),TXT.getName(),CSV.getName(),XML.getName(),INI.getName(),
            MD.getName(),YML.getName(),PROPERTIES.getName(),CFG.getName(),LOG.getName());

    int code;
    String name;

    FileItemTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
