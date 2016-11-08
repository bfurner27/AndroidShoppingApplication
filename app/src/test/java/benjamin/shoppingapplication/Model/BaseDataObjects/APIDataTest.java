package benjamin.shoppingapplication.Model.BaseDataObjects;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test that will exercise the API data class
 */
public abstract class APIDataTest {
    @Test
    public abstract void test_parseJSON();

    @Test
    public abstract void test_toString();
}