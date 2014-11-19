package de.htw.sdf.photoplatform.common;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.htw.sdf.photoplatform.persistence.models.Role;

/**
 * @author Sergej Meister
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImportSQLTester extends BaseTester {

    @Before
    public final void setUp() throws Exception {
        insertTestData();
    }

    @After
    public final void tearDown() throws Exception {
        clearTables();
        // roleDAO.deleteAll();
    }

    @Test
    public final void test1InitSqlData() {
        List<Role> roles = roleDAO.findAll();
        Assert.assertTrue(roles.size() == 3);
        Assert.assertTrue(roles.get(0).getId().equals(Role.ADMIN_ID));
        Assert.assertTrue(roles.get(0).getName().equals(Role.ADMIN));
        Assert.assertTrue(roles.get(1).getId().equals(Role.CUSTOMER_ID));
        Assert.assertTrue(roles.get(1).getName().equals(Role.CUSTOMER));
        Assert.assertTrue(roles.get(2).getId()
                .equals(Role.PHOTOGRAPHER_ID));
        Assert.assertTrue(roles.get(2).getName().equals(Role.PHOTOGRAPHER));
    }

    @Test
    @Ignore
    public final void test2AfterDelete() {
        List<Role> roles = roleDAO.findAll();
        Assert.assertTrue(roles.size() == 3);
        Assert.assertTrue(roles.get(0).getId().equals(Role.ADMIN_ID));
        Assert.assertTrue(roles.get(0).getName().equals(Role.ADMIN));
        Assert.assertTrue(roles.get(1).getId().equals(Role.CUSTOMER_ID));
        Assert.assertTrue(roles.get(1).getName().equals(Role.CUSTOMER));
        Assert.assertTrue(roles.get(2).getId()
                .equals(Role.PHOTOGRAPHER_ID));
        Assert.assertTrue(roles.get(2).getName().equals(Role.PHOTOGRAPHER));
    }
}
