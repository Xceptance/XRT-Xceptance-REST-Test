package com.xceptance.xrt.document;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

/**
 * Verifies all custom methods of @link {@link JSON}. Methods that only work as
 * an adapter are not tested here.
 * 
 * @author Patrick Thaele
 * 
 */
public class TestJSON
{
    private static JSON json;

    @BeforeClass
    public static void setup()
    {        
        json = new JSON( "{\"message\":\"Hello World!\", \"counter\":4, \"number\":4.234, \"array\":[4,3,2], \"list\":{\"string\":\"check\", \"number\":3.24}}" );
        System.out.println(json.toString());
    }

    @Test
    public void elementExists()
    {
        Assert.assertTrue( json.exists( "message" ) );
    }

    @Test
    public void elementExistsDeep()
    {
        Assert.assertTrue( json.exists( "list.string" ) );
    }

    @Test
    public void elementDoesNotExist()
    {
        Assert.assertFalse( json.exists( "messages" ) );
    }

    @Test
    public void elementDoesNotExistDeep()
    {
        Assert.assertFalse( json.exists( "list.strings" ) );
        Assert.assertFalse( json.exists( "lists.string" ) );
    }
}
