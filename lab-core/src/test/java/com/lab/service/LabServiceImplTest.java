package com.lab.service;

import com.lab.BaseTest;
import com.lab.business.ro.LabCreateRO;
import com.lab.business.ro.LabUpdateRO;
import com.lab.service.impl.LabServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LabServiceImplTest extends BaseTest {

    @Autowired
    private LabServiceImpl labServiceImpl;



    @Test
    public void create() {

        LabCreateRO labCreateRO = LabCreateRO.builder()
            .createBaseUserId(1L)
            .name("test")
            .descInfo("dad")
            .build();

        this.labServiceImpl.create(labCreateRO);
    }

    @Test
    public void fakeDeleteById() {
        this.labServiceImpl.fakeDeleteById(1L);
        this.labServiceImpl.fakeDeleteById(2L);
    }



    @Test
    public void update() {
        LabUpdateRO labUpdateRO = LabUpdateRO.builder()
            .name("test")
            .descInfo("dad")
            .build();

        this.labServiceImpl.update(labUpdateRO);
    }

    @Test
    public void getById() {
        Assert.assertNotNull(this.labServiceImpl.getById(1L));
        Assert.assertNull(this.labServiceImpl.getById(6L));
    }

    @Test
    public void listByBelongUserId() {
        Assert.assertEquals(3, this.labServiceImpl.listByBelongUserId(1L).size());
        Assert.assertEquals(2, this.labServiceImpl.listByBelongUserId(2L).size());
    }
}