package com.searchify.suggestion.api.constant;

public class SemrushConstants {
    public static final String PATH_ROOT = "/";
    public static final String PATH_COUNT_API_UNIT = "integration.semrush.countapiunits.path";
    public static final String PATH_TRAFFIC_SUMMARY = "integration.semrush.traffic.summary";
    public static final String PATH_TRAFFIC_TOP_PAGES = "integration.semrush.traffic.top.pages";
    public static final String PATH_TRAFFIC_TOP_SUBFOLDERS = "integration.semrush.traffic.top.subfolders";
    public static final String PATH_TRAFFIC_TOP_SUBDOMAINS = "integration.semrush.traffic.top.subdomains";
    public static final String PATH_TRAFFIC_SOURCES = "integration.semrush.traffic.sources";
    public static final String PATH_TRAFFIC_DESTINATIONS = "integration.semrush.traffic.destinations";
    public static final String PATH_TRAFFIC_AGE_SEX_DIST = "integration.semrush.traffic.agesex.dist";
    public static final String PATH_TRAFFIC_GEO_DIST = "integration.semrush.traffic.geo.dist";
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
    public static final String QUERY_PARAM_TRAFFIC_TYPE = "traffic_type";
    public static final String QUERY_PARAM_TRAFFIC_CHANNEL = "traffic_channel";
    public static final String QUERY_PARAM_COUNTRY = "country";
    public static final String QUERY_PARAM_SORT_ORDER = "sort_order";
    public static final String TYPE_PHRASE_ALL = "phrase_all";
    public static final String TYPE_PHRASE_KDI = "phrase_kdi";
    public static final String TYPE_DOMAIN_ORGANIC = "domain_organic_organic";
    public static final String EXPORT_COLUMNS = "Dt,Db,Ph,Nq,Cp,Co";
    public static final String EXPORT_COLUMNS_KDI = "Ph,Kd";
    public static final String EXPORT_COLUMNS_ORGANIC = "Dn,Cr";
    public static final String EXPORT_COLUMNS_TRAFFIC_SUMMARY = "target,visits,desktop_visits,mobile_visits,pages_per_visit," +
            "desktop_pages_per_visit,mobile_pages_per_visit,bounce_rate,desktop_bounce_rate,mobile_bounce_rate,users";
    public static final String EXPORT_COLUMNS_TRAFFIC_TOP_PAGES = "page,display_date,traffic_share";
    public static final String EXPORT_COLUMNS_TRAFFIC_TOP_SUBFOLDERS = "subfolder,display_date,traffic_share,unique_pageviews";
    public static final String EXPORT_COLUMNS_TRAFFIC_TOP_SUBDOMAINS = "subdomain,display_date,total_visits,desktop_share,mobile_share";
    public static final String EXPORT_COLUMNS_TRAFFIC_SOURCE = "from_target,display_date,traffic,traffic_type,channel";
    public static final String EXPORT_COLUMNS_TRAFFIC_DESTINATION = "to_target, traffic_share, display_date";
    public static final String EXPORT_COLUMNS_AGESEX_DISTRIBUTION = "age,female_users,male_users,female_share,male_share,display_date";
    public static final String EXPORT_COLUMNS_GEO_DISTRIBUTION = "geo,traffic,traffic_share,desktop_share,mobile_share,display_date";
    public static final String SORT_ORDER_TRAFFIC_DESTINATION = "traffic_share_desc";
    public static final String SORT_ORDER_GEO_DISTRIBUTION = "traffic_share_desc";
    public static final String DATABASE_DEFAULT = "us";
    public static final Character CSV_SEPARATOR = ';';
    public static final Character COMMA_SEPARATOR = ',';
}