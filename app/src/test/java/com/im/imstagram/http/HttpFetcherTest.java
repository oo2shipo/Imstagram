package com.im.imstagram.http;

import com.im.imstagram.common.HndResp;
import com.im.imstagram.datatype.ExtractFactory;
import com.im.imstagram.datatype.PhotoEntry;
import com.im.imstagram.utils.Util4Custom;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by vioooiv on 2017-01-11.
 */
public class HttpFetcherTest
{
    static final String HTTP_URL = "https://www.instagram.com/design/media/?max_id=0";
    static final String DEFAULT_USER_ID = "design";
    static final String DEFAULT_MAX_ID = "0";

    private HndResp hndResp = null;

    @Before
    public void setUp() throws Exception
    {

    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void runFetcher() throws Exception
    {
        /* 핸들러 설정 */
        hndResp = new HndResp() {
            @Override
            public void onComplete(final String msg) {

                // Not Null
                Assert.assertNotNull("msg is null", msg);

                /* Json 추출 */
                ArrayList<PhotoEntry> alPhotoEntry = ExtractFactory.create(ExtractFactory.N_TYPE_I).extract(msg);
                Assert.assertNotNull("msg is null", alPhotoEntry);

            }

            @Override
            public void onError(final String msg) {
                // Not Null
                Assert.assertNotNull("msg is null", msg);
            }
        };

        /* 이미지 Url 요청 */
        HttpFetcher.runFetcher(hndResp, Util4Custom.getUrl(DEFAULT_USER_ID, DEFAULT_MAX_ID));
    }

}