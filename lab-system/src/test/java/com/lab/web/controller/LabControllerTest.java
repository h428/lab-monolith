package com.lab.web.controller;

import com.lab.BaseWebTest;
import org.junit.Test;

public class LabControllerTest extends BaseWebTest {

    String PREFIX = "/lab/";

    @Test
    public void getById() {

        final String url = PREFIX + "{id}";

        get(url, 1).token(lyhToken).execute()
            .print();

    }

    @Test
    public void listByIds() {
        final String url = PREFIX + "ids";

        get(url, 1)
            .param("ids[]", "1,2,4,5")
            .token(lyhToken).execute()
            .print();
    }
}