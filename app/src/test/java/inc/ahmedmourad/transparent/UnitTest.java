package inc.ahmedmourad.transparent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.util.List;

import inc.ahmedmourad.transparent.query.Query;
import inc.ahmedmourad.transparent.query.elements.Group;
import inc.ahmedmourad.transparent.query.gson.deserializer.Deserializer;
import inc.ahmedmourad.transparent.query.gson.serializer.Serializer;

import static junit.framework.Assert.assertEquals;

public class UnitTest {

	@Test
	public void query_isValid() {

		final String query = Query.with("A")
				.and()
				.beginGroup()
				.param("B")
				.or()
				.param("C")
				.endGroup()
				.or()
				.beginGroup()
				.param("C")
				.and()
				.group(Group.with("D").or().param("E"))
				.endGroup()
				.toString();

		final String query1 = Query.with("A")
				.and()
				.param("B")
				.and()
				.param("B")
				.or()
				.beginGroup()
				.param("B")
				.or()
				.param("B")
				.endGroup()
				.or()
				.and()
				.beginGroup()
				.param("C")
				.and()
				.group(Group.with("D").or().param("E"))
				.endGroup()
				.toString();

		final String query2 = Query.with("A")
				.and()
				.beginGroup()
				.param("B")
				.or()
				.param("C")
				.endGroup()
				.or()
				.beginGroup()
				.param("D")
				.and()
				.group(Group.with("E").or().param("F"))
				.endGroup()
				.toString();

		final Query query3 = Query.with("A")
				.and()
				.beginGroup()
				.param("B")
				.or()
				.param("C")
				.endGroup()
				.or()
				.beginGroup()
				.param("D")
				.and()
				.beginGroup()
				.param("E")
				.or()
				.param("F")
				.and()
				.beginGroup()
				.param("G")
				.and()
				.param("H")
				.endGroup()
				.and()
				.group(Group.with("I").or().param("J"))
				.or()
				.beginGroup()
				.param("K")
				.and()
				.param("L")
				.endGroup()
				.and()
				.group(Group.with("M").or().param("N"))
				.and()
				.param("O")
				.endGroup()
				.and()
				.param("P")
				.endGroup();

		System.out.println(query);
		System.out.println(query1);
		System.out.println(query2);
		System.out.println(query3.toString());

		final Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new Serializer())
				.registerTypeAdapter(List.class, new Deserializer())
				.create();

		final String json = gson.toJson(query3);

		System.out.println(json);

		final String query4 = gson.fromJson(json, Query.class).toString();

		System.out.println(query4);

//		assertEquals("\"A\" AND \"B\" OR (\"C\" AND (\"D\" OR \"E\"))", query);
		assertEquals("\"A\" AND (\"B\" OR \"C\") OR (\"D\" AND (\"E\" OR \"F\" AND (\"G\" AND \"H\") AND (\"I\" OR \"J\") OR (\"K\" AND \"L\") AND (\"M\" OR \"N\") AND \"O\") AND \"P\")", query3.toString());
		assertEquals("\"A\" AND (\"B\" OR \"C\") OR (\"D\" AND (\"E\" OR \"F\" AND (\"G\" AND \"H\") AND (\"I\" OR \"J\") OR (\"K\" AND \"L\") AND (\"M\" OR \"N\") AND \"O\") AND \"P\")", query4);
	}
}
