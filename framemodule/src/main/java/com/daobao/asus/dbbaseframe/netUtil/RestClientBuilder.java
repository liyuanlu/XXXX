package com.daobao.asus.dbbaseframe.netUtil;

import android.content.Context;

import com.daobao.asus.dbbaseframe.netUtil.CallBack.IDownloadCallBack;
import com.daobao.asus.dbbaseframe.netUtil.CallBack.IFailure;
import com.daobao.asus.dbbaseframe.netUtil.CallBack.IRequest;
import com.daobao.asus.dbbaseframe.netUtil.CallBack.ISuccess;
import java.io.File;
import java.util.WeakHashMap;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 建造者
 * Created by db on 2017/10/29.
 */

public class RestClientBuilder{
    private String mUrl = null;
    private static final WeakHashMap<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mRequest = null;
    private ISuccess mSuccess = null;
    private IFailure mFailure = null;
    private RequestBody mBody = null;
    private Context mContext = null;
    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;
    private IDownloadCallBack mDownloadCallBack = null;

    RestClientBuilder(){

    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder onRequst(IRequest iRequest) {
        this.mRequest = iRequest;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String,Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key,value);
        return this;
    }
    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }

    public final RestClientBuilder name(String name) {
        this.mName = name;
        return this;
    }
    public final RestClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final RestClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mSuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure iFailure) {
        this.mFailure = iFailure;
        return this;
    }

    public final RestClientBuilder context(Context context) {
        this.mContext = context;
        return this;
    }

    public final RestClientBuilder downloadCallBack(IDownloadCallBack downloadCallBack){
        this.mDownloadCallBack = downloadCallBack;
        return this;
    }


    public final RestClient build() {
        return new RestClient(mUrl, PARAMS,
                mDownloadDir, mExtension, mName,
                mRequest, mSuccess, mFailure,
                mBody, mFile, mContext,mDownloadCallBack);
    }

}
