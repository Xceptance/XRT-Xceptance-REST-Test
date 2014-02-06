package com.xceptance.xrt.document;

import org.hamcrest.Matcher;

import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;

/**
 * This class manages everything around JSON documents. It allows to create new
 * documents with a various number of constructors and methods and at the same
 * time allows a simple access to all properties via JSONPath. With the use of
 * different assertions the validation of JSON documents should be as easy as
 * possible.
 * 
 * @author Patrick Thaele
 * 
 */
public class JSON
{
    /****************************************************************************************
     ************************ Private Properties ********************************************
     ****************************************************************************************/

    /**
     * Object that allows assertions on the current JSON instance. Changes to
     * the JSON document always need to update the asserter.
     */
    JsonAsserter asserter;

    /****************************************************************************************
     ************************ Constructors **************************************************
     ****************************************************************************************/

    /**
     * Constructor that takes a JSON string as an argument.
     * 
     * @param json
     *            The JSON document as a string.
     */
    public JSON( String json )
    {
        this.asserter = JsonAssert.with( json );
    }

    /****************************************************************************************
     ************************ Public Methods ************************************************
     ****************************************************************************************/

    // Adapter methods for com.jayway.jsonassert.JsonAsserter. These methods are
    // necessary for several reasons:
    // * Certain methods should not be exposed.
    // * The order of the arguments of an assertion are not JUnit standard.
    // * The methods should return a different value.

    /**
     * Asserts that the object specified by the JSON path is equal to the
     * expected value. If they are not, an {@link java.lang.AssertionError
     * AssertionError} is thrown.
     * 
     * @param expected
     *            The expected value.
     * @param path
     *            The JSON path specifying the value being compared.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public <T> JSON assertEquals( T expected, String path )
    {
        this.asserter.assertEquals( path, expected );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is equal to the
     * expected value. If they are not, an {@link java.lang.AssertionError
     * AssertionError} with the given message is thrown.
     * 
     * @param message
     *            The message of the {@link java.lang.AssertionError
     *            AssertionError} if the assert fails.
     * @param expected
     *            The expected value.
     * @param path
     *            The JSON path specifying the value being compared.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public <T> JSON assertEquals( String message, T expected, String path )
    {
        this.asserter.assertEquals( path, expected, message );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is not defined within
     * the document. If the document contains the given object, an
     * {@link java.lang.AssertionError AssertionError} is thrown.
     * 
     * @param path
     *            The JSON path specifying the not defined object.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNotDefined( String path )
    {
        this.asserter.assertNotDefined( path );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is not defined within
     * the document. If the document contains the given object, an
     * {@link java.lang.AssertionError AssertionError} with the given message is
     * thrown.
     * 
     * @param message
     *            The message of the {@link java.lang.AssertionError
     *            AssertionError} if the assert fails.
     * @param path
     *            The JSON path specifying the not defined object.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNotDefined( String message, String path )
    {
        this.asserter.assertNotDefined( path, message );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is NOT null. If it is,
     * an {@link java.lang.AssertionError AssertionError} is thrown.
     * 
     * @param path
     *            The JSON path specifying the value being NOT null.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNotNull( String path )
    {
        this.asserter.assertNotNull( path );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is NOT null. If it is,
     * an {@link java.lang.AssertionError AssertionError} with the given message
     * is thrown.
     * 
     * @param message
     *            The message of the {@link java.lang.AssertionError
     *            AssertionError} if the assert fails.
     * @param path
     *            The JSON path specifying the value being NOT null.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNotNull( String message, String path )
    {
        this.asserter.assertNotNull( path, message );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is null. If it is null
     * an {@link java.lang.AssertionError AssertionError} is thrown.
     * 
     * @param path
     *            The JSON path specifying the value being null.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNull( String path )
    {
        this.asserter.assertNull( path );
        return this;
    }

    /**
     * Asserts that the object specified by the JSON path is null. If it is null
     * an {@link java.lang.AssertionError AssertionError} with the given message
     * is thrown.
     * 
     * @param message
     *            The message of the {@link java.lang.AssertionError
     *            AssertionError} if the assert fails.
     * @param path
     *            The JSON path specifying the value being null.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     */
    public JSON assertNull( String message, String path )
    {
        this.asserter.assertNull( path, message );
        return this;
    }

    /**
     * <p>
     * Asserts that the object specified by the JSON path satisfies the
     * condition specified by the matcher. If the assert fails an
     * {@link java.lang.AssertionError AssertionError} is thrown providing
     * information about the matcher and its failing value.
     * <p/>
     * <br/>
     * <p>
     * Example:
     * </p>
     * {@code
     * assertThat("items[0].name", equalTo("Bobby"))
     * }
     * 
     * @param matcher
     *            An expression, built of Matchers, specifying how values are
     *            verified.
     * @param path
     *            The JSON path specifying the value to be asserted.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     * 
     * @see org.hamcrest.Matcher
     * @see org.hamcrest.Matchers
     */
    public <T> JSON assertThat( Matcher<T> matcher, String path )
    {
        this.asserter.assertThat( path, matcher );
        return this;
    }

    /**
     * <p>
     * Asserts that the object specified by the JSON path satisfies the
     * condition specified by the matcher. If the assert fails an
     * {@link java.lang.AssertionError AssertionError} with the given message is
     * thrown also providing information about the matcher and its failing
     * value.
     * <p/>
     * <br/>
     * <p>
     * Example:
     * </p>
     * {@code
     * assertThat("items[0].name", equalTo("Bobby"))
     * }
     * 
     * @param message
     *            The message of the {@link java.lang.AssertionError
     *            AssertionError} if the assert fails.
     * @param matcher
     *            An expression, built of Matchers, specifying how values are
     *            verified.
     * @param path
     *            The JSON path specifying the value to be asserted.
     * 
     * @return The JSON instance to allow fluent assertion chains.
     * 
     * @see org.hamcrest.Matcher
     * @see org.hamcrest.Matchers
     */
    public <T> JSON assertThat( String message, Matcher<T> matcher, String path )
    {
        this.asserter.assertThat( path, matcher, message );
        return this;
    }
}