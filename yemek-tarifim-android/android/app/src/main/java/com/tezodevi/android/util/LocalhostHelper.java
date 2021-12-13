package com.tezodevi.android.util;

public class LocalhostHelper {
    /**
     * open cmd or terminal
     * write -> ipconfig or ifconfig
     * copy ipv4 address and paste here.
     */
    public static final String BASE_MACHINE_LEARNING_URL = "http://your_ipv4_address:5000/";
    public static final String BASE_WEBSOCKET_URL = "http://your_ipv4_address:3000/";
    /**
     * Localhost için base url.
     * Not: Server'ın çalıştığı bilgisayarda ipconfig sorgusu yapılarak (windows), local ip öğrenilebilir.
     * Base url kısmını bu local ip oluşturacaktır. Bilgisayardan bilgisayara değişiklik gösterebilir.
     */

    private static final String BASE_LOCALHOST_URL = "http://your_ipv4_address:8080/";

    public static String getUrl(String path) {
        return BASE_LOCALHOST_URL + path + "/";
    }
}
