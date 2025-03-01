package com.md09.pharmapoly.network;

import android.content.Context;

import com.md09.pharmapoly.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class SafeOkHttpClient {

    public static OkHttpClient getSafeOkHttpClient() {
        return new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }
}