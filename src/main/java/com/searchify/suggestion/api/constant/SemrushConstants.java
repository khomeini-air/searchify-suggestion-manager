package com.searchify.suggestion.api.constant;

public class SemrushConstants {
    public static final String PATH_ROOT = "/";
    public static final String PATH_COUNT_API_UNIT = "integration.semrush.countapiunits.path";
    public static final String PATH_TRAFFIC_SUMMARY = "integration.semrush.traffic.summary";
    public static final String PATH_TRAFFIC_TOP_PAGES = "integration.semrush.traffic.top.pages";
    public static final String QUERY_PARAM_KEY = "key";
    public static final String QUERY_PARAM_TYPE = "type";
    public static final String QUERY_PARAM_PHRASE = "phrase";
    public static final String QUERY_PARAM_EXPORT_COLUMNS = "export_columns";
    public static final String QUERY_PARAM_DISPLAY_OFFSET = "display_offset";
    public static final String QUERY_PARAM_DISPLAY_LIMIT = "display_limit";
    public static final String QUERY_PARAM_DOMAIN = "domain";
    public static final String QUERY_PARAM_DATABASE = "database";
    public static final String QUERY_PARAM_TARGETS = "targets";
    public static final String QUERY_PARAM_TARGET = "target";
    public static final String QUERY_PARAM_DISPLAY_DATE = "display_date";
    public static final String QUERY_PARAM_COUNTRY = "country";

    public static final String TYPE_PHRASE_ALL = "phrase_all";
    public static final String TYPE_PHRASE_KDI = "phrase_kdi";
    public static final String TYPE_DOMAIN_ORGANIC = "domain_organic_organic";
    public static final String EXPORT_COLUMNS = "Dt,Db,Ph,Nq,Cp,Co";
    public static final String EXPORT_COLUMNS_KDI = "Ph,Kd";
    public static final String EXPORT_COLUMNS_ORGANIC = "Dn,Cr";
    public static final String EXPORT_COLUMNS_TRAFFIC_SUMMARY = "target,visits,users";
    public static final String DATABASE_DEFAULT = "us";
    public static final Character CSV_SEPARATOR = ';';
    public static final Character COMMA_SEPARATOR = ',';
}
