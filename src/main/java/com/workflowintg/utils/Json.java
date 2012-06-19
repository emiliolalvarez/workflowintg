package com.workflowintg.utils;

/*  

 * Copyright (C) 2011 Miami-Dade County.  

 *  

 * Licensed under the Apache License, Version 2.0 (the "License");  

 * you may not use this file except in compliance with the License.  

 * You may obtain a copy of the License at  

 *  

 * http://www.apache.org/licenses/LICENSE-2.0  

 *  

 * Unless required by applicable law or agreed to in writing, software  

 * distributed under the License is distributed on an "AS IS" BASIS,  

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  

 * See the License for the specific language governing permissions and  

 * limitations under the License.  

 *   

 * Note: this file incorporates source code from 3d party entities. Such code   

 * is copyrighted by those entities as indicated below.  

 */

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * 
 * <p>
 * 
 * Represents a JSON (JavaScript Object Notation) entity. For more information
 * about JSON, please see
 * 
 * <a href="http://www.json.org" target="_">http://www.json.org</a>.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * A JSON entity can be one of several things: an object (set of name/Json
 * entity pairs), an array (a list of
 * 
 * other JSON entities), a string, a number, a boolean or null. All of those are
 * represented as <code>Json</code>
 * 
 * instances. Each of the different types of entities supports a different set
 * of operations. However, this class
 * 
 * unifies all operations into a single interface so in Java one is always
 * dealing with a single object type: this class.
 * 
 * The approach effectively amounts to dynamic typing where using an unsupported
 * operation won't be detected at
 * 
 * compile time, but will throw a runtime {@link UnsupportedOperationException}.
 * It simplifies working with JSON
 * 
 * structures considerably and it leads to shorter at cleaner Java code. It
 * makes much easier to work
 * 
 * with JSON structure without the need to convert to "proper" Java
 * representation in the form of
 * 
 * POJOs and the like. When traversing a JSON, there's no need to type-cast at
 * each step because there's
 * 
 * only one type: <code>Json</code>.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * One can examine the concrete type of a <code>Json</code> with one of the
 * <code>isXXX</code> methods:
 * 
 * {@link #isObject()}, {@link #isArray()},{@link #isNumber()},
 * {@link #isBoolean()},{@link #isString()},
 * 
 * {@link #isNull()}.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * The underlying representation of a given <code>Json</code> instance can be
 * obtained by calling
 * 
 * the generic {@link #getValue()} method or one of the <code>asXXX</code>
 * methods such
 * 
 * as {@link #asBoolean()} or {@link #asString()} etc.
 * 
 * JSON objects are represented as Java {@link Map}s while JSON arrays are
 * represented as Java
 * 
 * {@link List}s. Because those are mutable aggregate structures, there are two
 * versions of the
 * 
 * corresponding <code>asXXX</code> methods: {@link #asMap()} which performs a
 * deep copy of the underlying
 * 
 * map, unwrapping every nested Json entity to its Java representation and
 * {@link #asJsonMap()} which
 * 
 * simply return the map reference. Similarly there are {@link #asList()} and
 * {@link #asJsonList()}.
 * 
 * </p>
 * 
 * 
 * 
 * <h3>Constructing and Modifying JSON Structures</h3>
 * 
 * 
 * 
 * <p>
 * 
 * There are several static factory methods in this class that allow you to
 * create new
 * 
 * <code>Json</code> instances:
 * 
 * 
 * 
 * <table>
 * 
 * <tr>
 * <td>{@link #read(String)}</td>
 * 
 * <td>Parse a JSON string and return the resulting <code>Json</code> instance.
 * The syntax
 * 
 * recognized is as defined in <a
 * href="http://www.json.org">http://www.json.org</a>.
 * 
 * </td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #make(Object)}</td>
 * 
 * <td>Creates a Json instance based on the concrete type of the parameter. The
 * types
 * 
 * recognized are null, numbers, primitives, String, Map, Collection, Java
 * arrays
 * 
 * and <code>Json</code> itself.</td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #nil()}</td>
 * 
 * <td>Return a <code>Json</code> instance representing JSON <code>null</code>.</td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #object()}</td>
 * 
 * <td>Create and return an empty JSON object.</td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #object(Object...)}</td>
 * 
 * <td>Create and return a JSON object populated with the key/value pairs
 * 
 * passed as an argument sequence. Each even parameter becomes a key (via
 * 
 * <code>toString</code>) and each odd parameter is converted to a
 * <code>Json</code>
 * 
 * value.</td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #array()}</td>
 * 
 * <td>Create and return an empty JSON array.</td>
 * 
 * </tr>
 * 
 * <tr>
 * <td>{@link #array(Object...)}</td>
 * 
 * <td>Create and return a JSON array from the list of arguments.</td>
 * 
 * </tr>
 * 
 * </table>
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * If a <code>Json</code> instance is an object, you can set its properties by
 * 
 * calling the {@link #set(String, Object)} method which will add a new property
 * or replace an existing one.
 * 
 * Adding elements to an array <code>Json</code> is done with the
 * {@link #add(Object)} method.
 * 
 * Removing elements by their index (or key) is done with the
 * {@link #delAt(int)} (or
 * 
 * {@link #delAt(String)}) method. You can also remove an element from an array
 * without
 * 
 * knowing its index with the {@link #remove(Object)} method. All these methods
 * return the
 * 
 * <code>Json</code> instance being manipulated so that method calls can be
 * chained.
 * 
 * If you want to remove an element from an object or array and return the
 * removed element
 * 
 * as a result of the operation, call {@link #atDel(int)} or
 * {@link #atDel(String)} instead.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * If you want to add properties to an object in bulk or append a sequence of
 * elements to array,
 * 
 * use the {@link #with(Json)} method. When used on an object, this method
 * expects another
 * 
 * object as its argument and it will copy all properties of that argument into
 * itself. Similarly,
 * 
 * when called on array, the method expects another array and it will append all
 * elements of its
 * 
 * argument to itself.
 * 
 * </p>
 * 
 * 
 * 
 * <h3>Navigating JSON Structures</h3>
 * 
 * 
 * 
 * <p>
 * 
 * The {@link #at(int)} method returns the array element at the specified index
 * and the
 * 
 * {@link #at(String)} method does the same for a property of an object
 * instance. You can
 * 
 * use the {@link #at(String, Object)} version to create an object property with
 * a default
 * 
 * value if it doesn't exist already.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * To help in navigating JSON structures, instances of this class contain a
 * reference to the
 * 
 * enclosing JSON entity (object or array) if any. The enclosing entity can be
 * accessed
 * 
 * with {@link #up()} method.
 * 
 * </p>
 * 
 * 
 * 
 * <p>
 * 
 * The combination of method chaining when modifying <code>Json</code> instances
 * and
 * 
 * the ability to navigate "inside" a structure and then go back to the
 * enclosing
 * 
 * element lets one accomplish a lot in a single Java statement, without the
 * need
 * 
 * of intermediary variables. Here for example how the following JSON structure
 * can
 * 
 * be created in one statement using chained calls:
 * 
 * </p>
 * 
 * 
 * 
 * <pre>
 * <code>  
 * 
 * {"menu": {  
 * 
 * "id": "file",  
 * 
 * "value": "File",  
 * 
 * "popup": {  
 * 
 *   "menuitem": [  
 * 
 *     {"value": "New", "onclick": "CreateNewDoc()"},  
 * 
 *     {"value": "Open", "onclick": "OpenDoc()"},  
 * 
 *     {"value": "Close", "onclick": "CloseDoc()"}  
 * 
 *   ]  
 * 
 * }  
 * 
 * "position": 0  
 * 
 * }}  
 * 
 * </code>
 * </pre>
 * 
 * 
 * 
 * <pre>
 * <code>  
 * 
 * import json.Json;  
 * 
 * import static json.Json.*;  
 * 
 * ...  
 * 
 * Json j = object()  
 * 
 *  .at("menu", object())  
 * 
 *    .set("id", "file")   
 * 
 *    .set("value", "File")  
 * 
 *    .at("popup", object())  
 * 
 *      .at("menuitem", array())  
 * 
 *        .add(object("value", "New", "onclick", "CreateNewDoc()"))  
 * 
 *        .add(object("value", "Open", "onclick", "OpenDoc()"))  
 * 
 *        .add(object("value", "Close", "onclick", "CloseDoc()"))  
 * 
 *        .up()  
 * 
 *      .up()  
 * 
 *    .set("position", 0);         
 * 
 * ...  
 * 
 * </code>
 * </pre>
 * 
 * 
 * 
 * <p>
 * 
 * If there's no danger of naming conflicts, a static import of the factory
 * methods (<code>  
 * import static json.Json.*;</code>) would reduce typing even further and make
 * the code more
 * 
 * readable.
 * 
 * </p>
 * 
 * 
 * 
 * <h3>Converting to String</h3>
 * 
 * 
 * 
 * <p>
 * 
 * To get a compact string representation, simply use the {@link #toString()}
 * method. If you
 * 
 * want to wrap it in a JavaScript callback (for JSON with padding), use the
 * {@link #pad(String)}
 * 
 * method.
 * 
 * </p>
 * 
 * 
 * 
 * @author Borislav Iordanov
 * 
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class Json

{
	public static String repeat(String s, int times) {
	    if (times <= 0) return "";
	    else return s + repeat(s, times-1);
	}
	
	public enum JsonType{
		Object,
		Array,
		String,
		Number,
		Boolean,
		NULL
	}
	/**
	 * 
	 * <p>
	 * 
	 * Parse a JSON entity from its string representation.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param jsonAsString
	 *            A valid JSON representation as per the <a
	 *            href="http://www.json.org">json.org</a>
	 * 
	 *            grammar. Cannot be <code>null</code>.
	 * 
	 * @return The JSON entity parsed: an object, array, string, number or
	 *         boolean, or null. Note that
	 * 
	 *         this method will never return the actual Java <code>null</code>.
	 */

	public static Json read(String jsonAsString) {
		return (Json) new Reader().read(jsonAsString);
	}
	
	/**
	 * 
	 * <p>
	 * 
	 * Parse a JSON entity from a {@link CharacterIterator}.
	 * 
	 * </p>
	 * 
	 * @see #read(String)
	 */

	public static Json read(CharacterIterator it) {
		return (Json) new Reader().read(it);
	}

	/**
	 * 
	 * <p>
	 * Return the <code>null Json</code> instance.
	 * </p>
	 */

	public static Json nil() {
		return Json.topnull;
	}

	/**
	 * 
	 * <p>
	 * Return a newly constructed, empty JSON object.
	 * </p>
	 */

	public static Json object() {
		return new Json.ObjectJson();
	}

	/**
	 * 
	 * <p>
	 * Return a new JSON object initialized from the passed list of
	 * 
	 * name/value pairs. The number of arguments must
	 * 
	 * be even. Each argument at an even position is taken to be a name
	 * 
	 * for the following value. The name arguments are normally of type
	 * 
	 * Java String, but they can be of any other type having an appropriate
	 * 
	 * <code>toString</code> method. Each value is first converted
	 * 
	 * to a <code>Json</code> instance using the {@link #make(Object)} method.
	 * 
	 * </p>
	 * 
	 * @param args
	 *            A sequence of name value pairs.
	 */

	public static Json object(Object... args)

	{

		Json j = object();

		if (args.length % 2 != 0)

			throw new IllegalArgumentException(
					"An even number of arguments is expected.");

		for (int i = 0; i < args.length; i++)

			j.set(args[i].toString(), make(args[++i]));

		return j;

	}

	/**
	 * 
	 * <p>
	 * Return a new constructed, empty JSON array.
	 * </p>
	 */

	public static Json array() {
		return new Json.ArrayJson();
	}

	/**
	 * 
	 * <p>
	 * Return a new JSON array filled up with the list of arguments.
	 * </p>
	 * 
	 * 
	 * 
	 * @param args
	 *            The initial content of the array.
	 */

	public static Json array(Object... args)

	{

		Json A = array();

		for (Object x : args)

			A.add(make(x));

		return A;

	}

	/**
	 * 
	 * <p>
	 * 
	 * Convert an arbitrary Java instance to a {@link Json} instance.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * <p>
	 * 
	 * Maps, Collections and arrays are recursively copied where each of
	 * 
	 * their elements concerted into <code>Json</code> instances as well. The
	 * keys
	 * 
	 * of a {@link Map} parameter are normally strings, but anything with a
	 * meaningful
	 * 
	 * <code>toString</code> implementation will work as well.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param anything
	 * 
	 * @return The <code>Json</code>. This method will never return
	 *         <code>null</code>. It will
	 * 
	 *         throw an {@link IllegalArgumentException} if it doesn't know how
	 *         to convert the argument
	 * 
	 *         to a <code>Json</code> instance.
	 * 
	 * @throws IllegalArgumentException
	 *             when the concrete type of the parameter is
	 * 
	 *             unknown.
	 */
	public static Json make(Object anything)

	{

		if (anything == null)

			return topnull;

		else if (anything instanceof Json)

			return (Json) anything;

		else if (anything instanceof String)

			return new StringJson((String) anything, null);

		else if (anything instanceof Collection)

		{

			Json L = array();

			for (Object x : (Collection) anything)

				L.add(make(x));

			return L;

		}

		else if (anything instanceof Map)

		{

			Json O = object();

			for (Map.Entry x : ((Map<?, ?>) anything).entrySet())

				O.set(x.getKey().toString(), make(x.getValue()));

			return O;

		}

		else if (anything instanceof Boolean)

			return new BooleanJson((Boolean) anything, null);

		else if (anything instanceof Number)

			return new NumberJson((Number) anything, null);

		else if (anything.getClass().isArray())

		{

			Class comp = anything.getClass().getComponentType();

			if (!comp.isPrimitive())

				return array((Object[]) anything);

			Json A = array();

			if (boolean.class == comp)

				for (boolean b : (boolean[]) anything)
					A.add(b);

			else if (byte.class == comp)

				for (byte b : (byte[]) anything)
					A.add(b);

			else if (char.class == comp)

				for (char b : (char[]) anything)
					A.add(b);

			else if (short.class == comp)

				for (short b : (short[]) anything)
					A.add(b);

			else if (int.class == comp)

				for (int b : (int[]) anything)
					A.add(b);

			else if (long.class == comp)

				for (long b : (long[]) anything)
					A.add(b);

			else if (float.class == comp)

				for (float b : (float[]) anything)
					A.add(b);

			else if (boolean.class == comp)

				for (double b : (double[]) anything)
					A.add(b);

			return A;

		}

		else

			throw new IllegalArgumentException(
					"Don't know how to convert to Json : " + anything);

	}

	// end of static utility method section

	Json enclosing = null;

	Json() {
	}

	Json(Json enclosing) {
		this.enclosing = enclosing;
	}
	
	public Json getChild( String path ){
		String childs[] = path.split(".");
		
		if( childs.length == 0 && path.length() > 0 ){
			childs = new String[1];
			childs[0] = path;
		}
		
		Json current = this;
		for( String c : childs ){
			current = current.at(c);
		}
		
		return current;
	}
	
	/**
	 * 
	 * <p>
	 * Return the <code>Json</code> entity, if any, enclosing this
	 * 
	 * <code>Json</code>. The returned value can be <code>null</code> or
	 * 
	 * a <code>Json</code> object or list, but not one of the primitive types.
	 * </p>
	 */

	public final Json up() {
		return enclosing;
	}

	/**
	 * 
	 * <p>
	 * Return the <code>Json</code> element at the specified index of this
	 * 
	 * <code>Json</code> array. This method applies only to Json arrays.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param index
	 *            The index of the desired element.
	 */

	public Json at(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Return the specified property of a <code>Json</code> object or
	 * <code>null</code>
	 * 
	 * if there's no such property. This method applies only to Json objects.
	 * 
	 * </p>
	 */

	public Json at(String property) {
		throw new UnsupportedOperationException();
	}

	public String asString(String property) {
		throw new UnsupportedOperationException();
	}

	public Integer asInteger(String property) {
		throw new UnsupportedOperationException();
	}
	
	public JsonType getType(){
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Return the specified property of a <code>Json</code> object if it exists.
	 * 
	 * If it doesn't, then create a new property with value the <code>def</code>
	 * 
	 * parameter and return that parameter.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property to return.
	 * 
	 * @param def
	 *            The default value to set and return in case the property
	 *            doesn't exist.
	 */

	public final Json at(String property, Json def)

	{

		Json x = at(property);

		if (x == null)

		{

			set(property, def);

			return def;

		}

		else

			return x;

	}

	/**
	 * 
	 * <p>
	 * 
	 * Return the specified property of a <code>Json</code> object if it exists.
	 * 
	 * If it doesn't, then create a new property with value the <code>def</code>
	 * 
	 * parameter and return that parameter.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property to return.
	 * 
	 * @param def
	 *            The default value to set and return in case the property
	 *            doesn't exist.
	 */

	public final Json at(String property, Object def)

	{

		return at(property, make(def));

	}

	/**
	 * 
	 * <p>
	 * 
	 * Return true if this <code>Json</code> object has the specified property
	 * 
	 * and false otherwise.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The name of the property.
	 */

	public boolean has(String property) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Add the specified <code>Json</code> element to this array.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @return this
	 */

	public Json add(Json el) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Add an arbitrary Java object to this <code>Json</code> array. The object
	 * 
	 * is first converted to a <code>Json</code> instance by calling the static
	 * 
	 * {@link #make} method.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param anything
	 *            Any Java object that can be converted to a Json instance.
	 * 
	 * @return this
	 */

	public final Json add(Object anything) {
		return add(make(anything));
	}

	/**
	 * 
	 * <p>
	 * 
	 * Remove the specified property from a <code>Json</code> object and return
	 * 
	 * that property.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property to be removed.
	 * 
	 * @return The property value or <code>null</code> if the object didn't have
	 *         such
	 * 
	 *         a property to begin with.
	 */

	public Json atDel(String property) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Remove the element at the specified index from a <code>Json</code> array
	 * and return
	 * 
	 * that element.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param index
	 *            The index of the element to delete.
	 * 
	 * @return The element value.
	 */

	public Json atDel(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Delete the specified property from a <code>Json</code> object.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property to be removed.
	 * 
	 * @return this
	 */

	public Json delAt(String property) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Remove the element at the specified index from a <code>Json</code> array.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param index
	 *            The index of the element to delete.
	 * 
	 * @return this
	 */

	public Json delAt(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Remove the specified element from a <code>Json</code> array.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param el
	 *            The element to delete.
	 * 
	 * @return this
	 */

	public Json remove(Json el) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Remove the specified Java object (converted to a Json instance)
	 * 
	 * from a <code>Json</code> array. This is equivalent to
	 * 
	 * <code>remove({@link #make(anything)})</code>.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param anything
	 *            The object to delete.
	 * 
	 * @return this
	 */

	public final Json remove(Object anything) {
		return remove(make(anything));
	}

	/**
	 * 
	 * <p>
	 * 
	 * Set a <code>Json</code> objects's property.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property name.
	 * 
	 * @param value
	 *            The value of the property.
	 * 
	 * @return this
	 */

	public Json set(String property, Json value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Set a <code>Json</code> objects's property.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param property
	 *            The property name.
	 * 
	 * @param value
	 *            The value of the property, converted to a <code>Json</code>
	 *            representation
	 * 
	 *            with {@link #make}.
	 * 
	 * @return this
	 */

	public final Json set(String property, Object value) {
		return set(property, make(value));
	}

	/**
	 * 
	 * <p>
	 * 
	 * Combine this object or array with the passed in object or array. The
	 * types of
	 * 
	 * <code>this</code> and the <code>object</code> argument must match. If
	 * both are
	 * 
	 * <code>Json</code> objects, all properties of the parameter are added to
	 * <code>this</code>.
	 * 
	 * If both are arrays, all elements of the parameter are appended to
	 * <code>this</code>
	 * 
	 * </p>
	 * 
	 * @param object
	 *            The object or array whose properties or elements must be added
	 *            to this
	 * 
	 *            Json object or array.
	 * 
	 * @return this
	 */

	public Json with(Json object) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the underlying value of this <code>Json</code> entity. The actual
	 * value will
	 * 
	 * be a Java Boolean, String, Number, Map, List or null. For complex
	 * entities (objects
	 * 
	 * or arrays), the method will perform a deep copy and extra underlying
	 * values recursively
	 * 
	 * for all nested elements.
	 * </p>
	 */

	public Object getValue() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the boolean value of a boolean <code>Json</code> instance. Call
	 * 
	 * {@link #isBoolean()} first if you're not sure this instance is indeed a
	 * 
	 * boolean.
	 * </p>
	 */

	public boolean asBoolean() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the string value of a string <code>Json</code> instance. Call
	 * 
	 * {@link #isString()} first if you're not sure this instance is indeed a
	 * 
	 * string.
	 * </p>
	 */

	public String asString() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the integer value of a number <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public int asInteger() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the float value of a float <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public float asFloat() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the double value of a number <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public double asDouble() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the long value of a number <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public long asLong() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the short value of a number <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public short asShort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the byte value of a number <code>Json</code> instance. Call
	 * 
	 * {@link #isNumber()} first if you're not sure this instance is indeed a
	 * 
	 * number.
	 * </p>
	 */

	public byte asByte() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the first character of a string <code>Json</code> instance. Call
	 * 
	 * {@link #isString()} first if you're not sure this instance is indeed a
	 * 
	 * string.
	 * </p>
	 */

	public char asChar() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return a map of the properties of an object <code>Json</code> instance.
	 * The map
	 * 
	 * is a clone of the object and can be modified safely without affecting it.
	 * Call
	 * 
	 * {@link #isObject()} first if you're not sure this instance is indeed a
	 * 
	 * <code>Json</code> object.
	 * </p>
	 */

	public Map<String, Object> asMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the underlying map of properties of a <code>Json</code> object.
	 * The returned
	 * 
	 * map is the actual object representation so any modifications to it are
	 * modifications
	 * 
	 * of the <code>Json</code> object itself. Call
	 * 
	 * {@link #isObject()} first if you're not sure this instance is indeed a
	 * 
	 * <code>Json</code> object.
	 * 
	 * </p>
	 */

	public Map<String, Json> asJsonMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return a list of the elements of a <code>Json</code> array. The list is a
	 * clone
	 * 
	 * of the array and can be modified safely without affecting it. Call
	 * 
	 * {@link #isArray()} first if you're not sure this instance is indeed a
	 * 
	 * <code>Json</code> array.
	 * 
	 * </p>
	 */

	public List<Object> asList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return the underlying {@link List} representation of a <code>Json</code>
	 * array.
	 * 
	 * The returned list is the actual array representation so any modifications
	 * to it
	 * 
	 * are modifications of the <code>Json</code> array itself. Call
	 * 
	 * {@link #isArray()} first if you're not sure this instance is indeed a
	 * 
	 * <code>Json</code> array.
	 * 
	 * </p>
	 */

	public List<Json> asJsonList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> null entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isNull() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> string entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isString() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> number entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isNumber() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> boolean entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isBoolean() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> array (i.e. list)
	 * entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isArray() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> object entity
	 * 
	 * and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isObject() {
		return false;
	}

	/**
	 * 
	 * <p>
	 * Return <code>true</code> if this is a <code>Json</code> primitive entity
	 * 
	 * (one of string, number or boolean) and <code>false</code> otherwise.
	 * 
	 * </p>
	 */

	public boolean isPrimitive() {
		return isString() || isNumber() || isBoolean();
	}

	/**
	 * 
	 * <p>
	 * 
	 * Json-pad this object as an argument to a callback function.
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param callback
	 *            The name of the callback function. Can be null or empty,
	 * 
	 *            in which case no padding is done.
	 * 
	 * @return The jsonpadded, stringified version of this object if the
	 *         <code>callback</code>
	 * 
	 *         is not null or empty, or just the stringified version of the
	 *         object.
	 */

	public String pad(String callback)

	{

		return (callback != null && callback.length() > 0)

		? callback + "(" + toString() + ");"

		: toString();

	}
	
	public String toBeautyString(){
		return toBeautyString(0);
	}
	
	protected abstract String toBeautyString(int nestLevel);

	// -------------------------------------------------------------------------

	// END OF PUBLIC INTERFACE

	// -------------------------------------------------------------------------

	static class NullJson extends Json

	{

		NullJson() {
		}

		NullJson(Json e) {
			super(e);
		}

		public boolean isNull() {
			return true;
		}

		public String toString() {
			return "null";
		}

		public List<Object> asList() {
			return (List) Collections.singletonList(null);
		}
		
		@Override
		public JsonType getType() {
			return JsonType.NULL;
		}

		public int hashCode() {
			return 0;
		}

		public boolean equals(Object x)

		{

			return x instanceof Json && x == this;

		}

		@Override
		protected String toBeautyString(int nestLevel) {
			return toString();
		}
	}

	static NullJson topnull = new NullJson();

	static class BooleanJson extends Json

	{

		boolean val;

		BooleanJson() {
		}

		BooleanJson(Json e) {
			super(e);
		}

		BooleanJson(Boolean val, Json e) {
			super(e);
			this.val = val;
		}

		public boolean asBoolean() {
			return val;
		}

		public boolean isBoolean() {
			return true;
		}
		
		@Override
		public JsonType getType() {
			return JsonType.Boolean;
		}

		public String toString() {
			return val ? "true" : "false";
		}

		public List<Object> asList() {
			return (List) Collections.singletonList(val);
		}

		public int hashCode() {
			return val ? 1 : 0;
		}

		public boolean equals(Object x)

		{

			return x instanceof BooleanJson && ((BooleanJson) x).val == val;

		}
		
		@Override
		protected String toBeautyString(int nestLevel) {
			return toString();
		}

	}

	static class StringJson extends Json

	{

		String val;

		StringJson() {
		}

		StringJson(Json e) {
			super(e);
		}

		StringJson(String val, Json e) {
			super(e);
			this.val = val;
		}

		public boolean isString() {
			return true;
		}

		public Object getValue() {
			return val;
		}
		
		@Override
		public JsonType getType() {
			return JsonType.String;
		}

		public String asString() {
			return val;
		}

		public int asInteger() {
			return Integer.parseInt(val);
		}

		public float asFloat() {
			return Float.parseFloat(val);
		}

		public double asDouble() {
			return Double.parseDouble(val);
		}

		public long asLong() {
			return Long.parseLong(val);
		}

		public short asShort() {
			return Short.parseShort(val);
		}

		public byte asByte() {
			return Byte.parseByte(val);
		}

		public char asChar() {
			return val.charAt(0);
		}

		public List<Object> asList() {
			return (List) Collections.singletonList(val);
		}

		public String toString()

		{

			return '"' + escaper.escapeJsonString(val) + '"';

		}

		public int hashCode() {
			return val.hashCode();
		}

		public boolean equals(Object x)

		{

			return x instanceof StringJson && ((StringJson) x).val.equals(val);

		}

		@Override
		protected String toBeautyString(int nestLevel) {
			return toString();
		}
	}

	static class NumberJson extends Json

	{

		Number val;

		NumberJson() {
		}

		NumberJson(Json e) {
			super(e);
		}

		NumberJson(Number val, Json e) {
			super(e);
			this.val = val;
		}

		public boolean isNumber() {
			return true;
		}

		public Object getValue() {
			return val;
		}

		public String asString() {
			return val.toString();
		}

		public int asInteger() {
			return val.intValue();
		}

		public float asFloat() {
			return val.floatValue();
		}

		public double asDouble() {
			return val.doubleValue();
		}
		
		@Override
		public JsonType getType() {
			return JsonType.Number;
		}

		public long asLong() {
			return val.longValue();
		}

		public short asShort() {
			return val.shortValue();
		}

		public byte asByte() {
			return val.byteValue();
		}

		public List<Object> asList() {
			return (List) Collections.singletonList(val);
		}

		public String toString() {
			return val.toString();
		}

		public int hashCode() {
			return val.hashCode();
		}

		public boolean equals(Object x)

		{

			return x instanceof NumberJson && ((NumberJson) x).val.equals(val);

		}
		
		@Override
		protected String toBeautyString(int nestLevel) {
			return toString();
		}

	}

	static class ArrayJson extends Json

	{

		List<Json> L = new ArrayList<Json>();

		ArrayJson() {
		}

		ArrayJson(Json e) {
			super(e);
		}

		@Override
		public List<Json> asJsonList() {
			return L;
		}

		public List<Object> asList()

		{

			ArrayList<Object> A = new ArrayList<Object>();

			for (Json x : L)

				A.add(x.getValue());

			return A;

		}

		public Object getValue() {
			return asList();
		}

		public boolean isArray() {
			return true;
		}

		public Json at(int index) {
			return L.get(index);
		}
		
		@Override
		public JsonType getType() {
			return JsonType.Array;
		}

		public Json add(Json el) {
			L.add(el);
			el.enclosing = this;
			return this;
		}

		public Json remove(Json el) {
			L.remove(el);
			el.enclosing = null;
			return this;
		}

		public Json with(Json object)

		{

			if (!object.isArray())

				throw new UnsupportedOperationException();

			L.addAll(((ArrayJson) object).L);

			return this;

		}

		public Json atDel(int index)

		{

			Json el = L.remove(index);

			if (el != null)

				el.enclosing = null;

			return el;

		}

		public Json delAt(int index)

		{

			Json el = L.remove(index);

			if (el != null)

				el.enclosing = null;

			return this;

		}

		public String toString()

		{

			StringBuilder sb = new StringBuilder("[");

			for (Iterator<Json> i = L.iterator(); i.hasNext();)

			{

				sb.append(i.next().toString());

				if (i.hasNext())

					sb.append(",");

			}

			sb.append("]");

			return sb.toString();

		}
		
		@Override
		protected String toBeautyString(int nestLevel) {
			StringBuilder sb = new StringBuilder();
			sb.append( "[" );

			for (Iterator<Json> i = L.iterator(); i.hasNext();)

			{

				sb.append(i.next().toBeautyString(nestLevel+1));

				if (i.hasNext())

					sb.append(",");

			}

			sb.append("]");

			return sb.toString();
		}

		public int hashCode() {
			return L.hashCode();
		}

		public boolean equals(Object x)

		{

			return x instanceof ArrayJson && ((ArrayJson) x).L.equals(L);

		}

	}

	static class ObjectJson extends Json

	{

		Map<String, Json> object = new HashMap<String, Json>();

		ObjectJson() {
		}

		ObjectJson(Json e) {
			super(e);
		}

		public boolean has(String property)

		{

			return object.containsKey(property);

		}

		public Json at(String property)

		{

			return object.get(property);

		}

		public String asString(String property)

		{

			Json o = object.get(property);

			if (o == null || o instanceof NullJson) {

				return null;

			}

			return o.asString();

		}

		public Integer asInteger(String property)

		{

			Json o = object.get(property);

			if (o == null) {

				return null;

			}

			if (o instanceof NullJson) {

				return null;

			}

			try {

				return o.asInteger();

			} catch (NumberFormatException e) {

				return null;

			}

		}

		public Json with(Json x)

		{

			if (!x.isObject())

				throw new UnsupportedOperationException();

			object.putAll(((ObjectJson) x).object);

			return this;

		}

		public Json set(String property, Json el)

		{

			el.enclosing = this;

			object.put(property, el);

			return this;

		}

		public Json atDel(String property)

		{

			Json el = object.remove(property);

			if (el != null)

				el.enclosing = null;

			return el;

		}

		public Json delAt(String property)

		{

			Json el = object.remove(property);

			if (el != null)

				el.enclosing = null;

			return this;

		}

		public Object getValue() {
			return asMap();
		}
		
		@Override
		public JsonType getType() {
			return JsonType.Object;
		}

		public boolean isObject() {
			return true;
		}

		public Map<String, Object> asMap()

		{

			HashMap<String, Object> m = new HashMap<String, Object>();

			for (Map.Entry<String, Json> e : object.entrySet())

				m.put(e.getKey(), e.getValue().getValue());

			return m;

		}

		@Override
		public Map<String, Json> asJsonMap() {
			return object;
		}

		public String toString()

		{

			StringBuilder sb = new StringBuilder("{");

			for (Iterator<Map.Entry<String, Json>> i = object.entrySet()
					.iterator(); i.hasNext();)

			{

				Map.Entry<String, Json> x = i.next();

				sb.append('"');

				sb.append(escaper.escapeJsonString(x.getKey()));

				sb.append('"');

				sb.append(":");

				sb.append(x.getValue().toString());

				if (i.hasNext())

					sb.append(",");

			}

			sb.append("}");

			return sb.toString();

		}
		
		protected String toBeautyString(int nestLevel)

		{

			StringBuilder sb = new StringBuilder();
			sb.append("{");

			for (Iterator<Map.Entry<String, Json>> i = object.entrySet()
					.iterator(); i.hasNext();)

			{

				Map.Entry<String, Json> x = i.next();

				sb.append("\n");
				sb.append( repeat("\t", nestLevel+1) );
				sb.append('"');

				sb.append(escaper.escapeJsonString(x.getKey()));

				sb.append('"');

				sb.append(":");

				sb.append(x.getValue().toBeautyString(nestLevel+1));

				if (i.hasNext())

					sb.append(",");

			}
			
			if(object.size() > 0){
				sb.append("\n");
				sb.append(repeat("\t", nestLevel));
			}
			sb.append("}");

			return sb.toString();

		}

		public int hashCode() {
			return object.hashCode();
		}

		public boolean equals(Object x)

		{

			return x instanceof ObjectJson
					&& ((ObjectJson) x).object.equals(object);

		}

	}

	// ------------------------------------------------------------------------

	// Extra utilities, taken from around the internet:

	// ------------------------------------------------------------------------

	/*
	 * 
	 * Copyright (C) 2008 Google Inc.
	 * 
	 * 
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * 
	 * you may not use this file except in compliance with the License.
	 * 
	 * You may obtain a copy of the License at
	 * 
	 * 
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * 
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * 
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * 
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * 
	 * See the License for the specific language governing permissions and
	 * 
	 * limitations under the License.
	 */

	/**
	 * 
	 * A utility class that is used to perform JSON escaping so that ", <, >,
	 * etc. characters are
	 * 
	 * properly encoded in the JSON string representation before returning to
	 * the client code.
	 * 
	 * 
	 * 
	 * <p>
	 * This class contains a single method to escape a passed in string value:
	 * 
	 * <pre>
	 * 
	 * String jsonStringValue = &quot;beforeQuote\&quot;afterQuote&quot;;
	 * 
	 * String escapedValue = Escaper.escapeJsonString(jsonStringValue);
	 * 
	 * </pre>
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @author Inderjeet Singh
	 * 
	 * @author Joel Leitch
	 */

	static Escaper escaper = new Escaper(false);

	final static class Escaper {

		private static final char[] HEX_CHARS = {

		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f'

		};

		private static final Set<Character> JS_ESCAPE_CHARS;

		private static final Set<Character> HTML_ESCAPE_CHARS;

		static {

			Set<Character> mandatoryEscapeSet = new HashSet<Character>();

			mandatoryEscapeSet.add('"');

			mandatoryEscapeSet.add('\\');

			JS_ESCAPE_CHARS = Collections.unmodifiableSet(mandatoryEscapeSet);

			Set<Character> htmlEscapeSet = new HashSet<Character>();

			htmlEscapeSet.add('<');

			htmlEscapeSet.add('>');

			htmlEscapeSet.add('&');

			htmlEscapeSet.add('=');

			htmlEscapeSet.add('\'');

			// htmlEscapeSet.add('/'); -- Removing slash for now since it causes
			// some incompatibilities

			HTML_ESCAPE_CHARS = Collections.unmodifiableSet(htmlEscapeSet);

		}

		private final boolean escapeHtmlCharacters;

		Escaper(boolean escapeHtmlCharacters) {

			this.escapeHtmlCharacters = escapeHtmlCharacters;

		}

		public String escapeJsonString(CharSequence plainText) {

			StringBuilder escapedString = new StringBuilder(
					plainText.length() + 20);

			try {

				escapeJsonString(plainText, escapedString);

			} catch (IOException e) {

				throw new RuntimeException(e);

			}

			return escapedString.toString();

		}

		private void escapeJsonString(CharSequence plainText, StringBuilder out)
				throws IOException {

			int pos = 0; // Index just past the last char in plainText written
							// to out.

			int len = plainText.length();

			for (int charCount, i = 0; i < len; i += charCount) {

				int codePoint = Character.codePointAt(plainText, i);

				charCount = Character.charCount(codePoint);

				if (!isControlCharacter(codePoint)
						&& !mustEscapeCharInJsString(codePoint)) {

					continue;

				}

				out.append(plainText, pos, i);

				pos = i + charCount;

				switch (codePoint) {

				case '\b':

					out.append("\\b");

					break;

				case '\t':

					out.append("\\t");

					break;

				case '\n':

					out.append("\\n");

					break;

				case '\f':

					out.append("\\f");

					break;

				case '\r':

					out.append("\\r");

					break;

				case '\\':

					out.append("\\\\");

					break;

				case '/':

					out.append("\\/");

					break;

				case '"':

					out.append("\\\"");

					break;

				default:

					appendHexJavaScriptRepresentation(codePoint, out);

					break;

				}

			}

			out.append(plainText, pos, len);

		}

		private boolean mustEscapeCharInJsString(int codepoint) {

			if (!Character.isSupplementaryCodePoint(codepoint)) {

				char c = (char) codepoint;

				return JS_ESCAPE_CHARS.contains(c)

				|| (escapeHtmlCharacters && HTML_ESCAPE_CHARS.contains(c));

			}

			return false;

		}

		private static boolean isControlCharacter(int codePoint) {

			// JSON spec defines these code points as control characters, so
			// they must be escaped

			return codePoint < 0x20

			|| codePoint == 0x2028 // Line separator

					|| codePoint == 0x2029 // Paragraph separator

					|| (codePoint >= 0x7f && codePoint <= 0x9f);

		}

		private static void appendHexJavaScriptRepresentation(int codePoint,
				Appendable out)

		throws IOException {

			if (Character.isSupplementaryCodePoint(codePoint)) {

				// Handle supplementary unicode values which are not
				// representable in

				// javascript. We deal with these by escaping them as two 4B
				// sequences

				// so that they will round-trip properly when sent from java to
				// javascript

				// and back.

				char[] surrogates = Character.toChars(codePoint);

				appendHexJavaScriptRepresentation(surrogates[0], out);

				appendHexJavaScriptRepresentation(surrogates[1], out);

				return;

			}

			out.append("\\u")

			.append(HEX_CHARS[(codePoint >>> 12) & 0xf])

			.append(HEX_CHARS[(codePoint >>> 8) & 0xf])

			.append(HEX_CHARS[(codePoint >>> 4) & 0xf])

			.append(HEX_CHARS[codePoint & 0xf]);

		}

	}

	private static class Reader

	{

		private static final Object OBJECT_END = new Object();

		private static final Object ARRAY_END = new Object();

		private static final Object COLON = new Object();

		private static final Object COMMA = new Object();

		public static final int FIRST = 0;

		public static final int CURRENT = 1;

		public static final int NEXT = 2;

		private static Map<Character, Character> escapes = new HashMap<Character, Character>();

		static

		{

			escapes.put(new Character('"'), new Character('"'));

			escapes.put(new Character('\\'), new Character('\\'));

			escapes.put(new Character('/'), new Character('/'));

			escapes.put(new Character('b'), new Character('\b'));

			escapes.put(new Character('f'), new Character('\f'));

			escapes.put(new Character('n'), new Character('\n'));

			escapes.put(new Character('r'), new Character('\r'));

			escapes.put(new Character('t'), new Character('\t'));

		}

		private CharacterIterator it;

		private char c;

		private Object token;

		private StringBuffer buf = new StringBuffer();

		private char next()

		{

			if (it.getIndex() == it.getEndIndex())

				throw new RuntimeException("Reached end of input at the " +

				it.getIndex() + "th character.");

			c = it.next();

			return c;

		}

		private char previous()

		{

			c = it.previous();

			return c;

		}

		private void skipWhiteSpace()

		{

			do

			{

				if (Character.isWhitespace(c))

					;

				else if (c == '/')

				{

					next();

					if (c == '*')

					{

						// skip multiline comments

						while (c != CharacterIterator.DONE)

							if (next() == '*' && next() == '/')

								break;

						if (c == CharacterIterator.DONE)

							throw new RuntimeException(
									"Unterminated comment while parsing JSON string.");

					}

					else if (c == '/')

						while (c != '\n' && c != CharacterIterator.DONE)

							next();

					else

					{

						previous();

						break;

					}

				}

				else

					break;

			} while (next() != CharacterIterator.DONE);

		}

		public Object read(CharacterIterator ci, int start)

		{

			it = ci;

			switch (start)

			{

			case FIRST:

				c = it.first();

				break;

			case CURRENT:

				c = it.current();

				break;

			case NEXT:

				c = it.next();

				break;

			}

			return read();

		}

		public Object read(CharacterIterator it)

		{

			return read(it, NEXT);

		}

		public Object read(String string)

		{

			return read(new StringCharacterIterator(string), FIRST);

		}

		private <T> T read()

		{

			skipWhiteSpace();

			char ch = c;

			next();

			switch (ch)

			{

			case '"':
				token = readString();
				break;

			case '[':
				token = readArray();
				break;

			case ']':
				token = ARRAY_END;
				break;

			case ',':
				token = COMMA;
				break;

			case '{':
				token = readObject();
				break;

			case '}':
				token = OBJECT_END;
				break;

			case ':':
				token = COLON;
				break;

			case 't':

				if (c != 'r' || next() != 'u' || next() != 'e')

					throw new RuntimeException(
							"Invalid JSON token: expected 'true' keyword.");

				next();

				token = new BooleanJson(Boolean.TRUE, null);

				break;

			case 'f':

				if (c != 'a' || next() != 'l' || next() != 's' || next() != 'e')

					throw new RuntimeException(
							"Invalid JSON token: expected 'false' keyword.");

				next();

				token = new BooleanJson(Boolean.FALSE, null);

				break;

			case 'n':

				if (c != 'u' || next() != 'l' || next() != 'l')

					throw new RuntimeException(
							"Invalid JSON token: expected 'null' keyword.");

				next();

				token = nil();

				break;

			default:

				c = it.previous();

				if (Character.isDigit(c) || c == '-') {

					token = readNumber();

				}

			}

			// System.out.println("token: " + token); // enable this line to see
			// the token stream

			return (T) token;

		}

		private String readObjectKey()

		{

			Object o = read();

			if (o == OBJECT_END) {

				return null;

			}

			Json key = (Json) o;

			if (key == null)

				throw new RuntimeException(
						"Missing object key (don't forget to put quotes!).");

			else

				return key.asString();

		}

		private Json readObject()

		{

			Json ret = object();

			String key = readObjectKey();

			Object prevToken = null;
			while (token != OBJECT_END  && (prevToken == null || !token.equals( prevToken ) ) )

			{
				prevToken = token;
				
				read(); // should be a colon
				
				
				
				if (token != OBJECT_END)

				{

					Json value = read();

					ret.set(key, value);

					Object ntoken = read();
					if (ntoken == COMMA) {

						key = readObjectKey();

					}
					else if( ntoken != OBJECT_END ){
						String v = ntoken.toString();
						if( ntoken instanceof Json ){
							v = ( (Json)ntoken ).asString();
						}
						throw new RuntimeException("Unexpected token "+ v);
					}

				}

			}

			return ret;

		}

		private Json readArray()

		{

			Json ret = array();

			Object value = read();

			while (token != ARRAY_END)

			{

				ret.add((Json) value);

				if (read() == COMMA)

					value = read();

				else if (token != ARRAY_END)

					throw new RuntimeException("Unexpected token in array "
							+ token);

			}

			return ret;

		}

		private Json readNumber()

		{

			int length = 0;

			boolean isFloatingPoint = false;

			buf.setLength(0);

			if (c == '-')

			{

				add();

			}

			length += addDigits();

			if (c == '.')

			{

				add();

				length += addDigits();

				isFloatingPoint = true;

			}

			if (c == 'e' || c == 'E')

			{

				add();

				if (c == '+' || c == '-')

				{

					add();

				}

				addDigits();

				isFloatingPoint = true;

			}

			String s = buf.toString();

			Number n = isFloatingPoint

			? (length < 17) ? Double.valueOf(s) : new BigDecimal(s)

			: (length < 20) ? Long.valueOf(s) : new BigInteger(s);

			return new NumberJson(n, null);

		}

		private int addDigits()

		{

			int ret;

			for (ret = 0; Character.isDigit(c); ++ret)

			{

				add();

			}

			return ret;

		}

		private Json readString()

		{

			buf.setLength(0);

			while (c != '"')

			{

				if (c == '\\')

				{

					next();

					if (c == 'u')

					{

						add(unicode());

					}

					else

					{

						Object value = escapes.get(new Character(c));

						if (value != null)

						{

							add(((Character) value).charValue());

						}

					}

				}

				else

				{

					add();

				}

			}

			next();

			return new StringJson(buf.toString(), null);

		}

		private void add(char cc)

		{

			buf.append(cc);

			next();

		}

		private void add()

		{

			add(c);

		}

		private char unicode()

		{

			int value = 0;

			for (int i = 0; i < 4; ++i)

			{

				switch (next())

				{

				case '0':
				case '1':
				case '2':
				case '3':
				case '4':

				case '5':
				case '6':
				case '7':
				case '8':
				case '9':

					value = (value << 4) + c - '0';

					break;

				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':

					value = (value << 4) + (c - 'a') + 10;

					break;

				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':

					value = (value << 4) + (c - 'A') + 10;

					break;

				}

			}

			return (char) value;

		}

	}

	// END Reader

	/*
	 * 
	 * public static void main(String [] argv)
	 * 
	 * {
	 * 
	 * Json j = object()
	 * 
	 * .at("menu", object())
	 * 
	 * .set("id", "file")
	 * 
	 * .set("value", "File")
	 * 
	 * .at("popup", object())
	 * 
	 * .at("menuitem", array())
	 * 
	 * .add(object("value", "New", "onclick", "CreateNewDoc()"))
	 * 
	 * .add(object("value", "Open", "onclick", "OpenDoc()"))
	 * 
	 * .add(object("value", "Close", "onclick", "CloseDoc()"))
	 * 
	 * .up()
	 * 
	 * .up()
	 * 
	 * .set("position", 0);
	 * 
	 * System.out.println(j);
	 * 
	 * }
	 */

}