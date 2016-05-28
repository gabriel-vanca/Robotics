package uk.ac.ucl.cs.robotsimulator.tests;

import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

// This is a class to test JSon parsing

public class TestJSon
{

	public static void main(String[] args)
	{
		try
		{
			final JsonReader reader = Json.createReader(new FileReader("/Users/ucacsjj/test1.json"));
			final JsonStructure jsonst = reader.read();
			navigateTree(jsonst, null);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void navigateTree(JsonValue tree, String key)
	{
		if (key != null)
		{
			System.out.print("Key " + key + ": ");
		}
		switch (tree.getValueType())
		{
		case OBJECT:
			System.out.println("OBJECT");
			final JsonObject object = (JsonObject) tree;
			for (final String name : object.keySet())
			{
				navigateTree(object.get(name), name);
			}
			break;
		case ARRAY:
			System.out.println("ARRAY");
			final JsonArray array = (JsonArray) tree;
			for (final JsonValue val : array)
			{
				navigateTree(val, null);
			}
			break;
		case STRING:
			final JsonString st = (JsonString) tree;
			System.out.println("STRING " + st.getString());
			break;
		case NUMBER:
			final JsonNumber num = (JsonNumber) tree;
			System.out.println("NUMBER " + num.toString());
			break;
		case TRUE:
		case FALSE:
		case NULL:
			System.out.println(tree.getValueType().toString());
			break;
		}
	}

	public TestJSon()
	{
		// TODO Auto-generated constructor stub
	}

}
