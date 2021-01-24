
package com.example.lib;

public class SecurityCons {
    public static final String SECRET = "WIE_OFT_DARF_MAN_BEI_NOTWEHR_NACHLADEN";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/login";
}
