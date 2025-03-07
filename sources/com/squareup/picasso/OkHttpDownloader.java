package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Downloader;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkHttpDownloader implements Downloader {
    private final OkHttpClient client;

    private static OkHttpClient defaultOkHttpClient() {
        OkHttpClient client2 = new OkHttpClient();
        client2.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        client2.setReadTimeout(20000, TimeUnit.MILLISECONDS);
        client2.setWriteTimeout(20000, TimeUnit.MILLISECONDS);
        return client2;
    }

    public OkHttpDownloader(Context context) {
        this(Utils.createDefaultCacheDir(context));
    }

    public OkHttpDownloader(File cacheDir) {
        this(cacheDir, Utils.calculateDiskCacheSize(cacheDir));
    }

    public OkHttpDownloader(Context context, long maxSize) {
        this(Utils.createDefaultCacheDir(context), maxSize);
    }

    public OkHttpDownloader(File cacheDir, long maxSize) {
        this(defaultOkHttpClient());
        try {
            this.client.setCache(new Cache(cacheDir, maxSize));
        } catch (IOException e) {
        }
    }

    public OkHttpDownloader(OkHttpClient client2) {
        this.client = client2;
    }

    /* access modifiers changed from: protected */
    public final OkHttpClient getClient() {
        return this.client;
    }

    public Downloader.Response load(Uri uri, int networkPolicy) throws IOException {
        CacheControl cacheControl = null;
        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                cacheControl = CacheControl.FORCE_CACHE;
            } else {
                CacheControl.Builder builder = new CacheControl.Builder();
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.noCache();
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    builder.noStore();
                }
                cacheControl = builder.build();
            }
        }
        Request.Builder builder2 = new Request.Builder().url(uri.toString());
        if (cacheControl != null) {
            builder2.cacheControl(cacheControl);
        }
        Response response = this.client.newCall(builder2.build()).execute();
        int responseCode = response.code();
        if (responseCode < 300) {
            boolean fromCache = response.cacheResponse() != null;
            ResponseBody responseBody = response.body();
            return new Downloader.Response(responseBody.byteStream(), fromCache, responseBody.contentLength());
        }
        response.body().close();
        throw new Downloader.ResponseException(responseCode + " " + response.message(), networkPolicy, responseCode);
    }

    public void shutdown() {
        Cache cache = this.client.getCache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException e) {
            }
        }
    }
}
