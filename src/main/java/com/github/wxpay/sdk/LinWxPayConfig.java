package com.github.wxpay.sdk;

import java.io.InputStream;

public class LinWxPayConfig extends WXPayConfig {

    @Override
    String getAppID() {
        return null;
    }

    @Override
    String getMchID() {
        return null;
    }

    @Override
    String getKey() {
        return null;
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return null;
    }
}
