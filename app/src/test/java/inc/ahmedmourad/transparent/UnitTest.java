package inc.ahmedmourad.transparent;

import org.junit.Test;

import inc.ahmedmourad.transparent.query.Query;
import inc.ahmedmourad.transparent.query.elements.Group;

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

//		final String query = Query.with("A")
//				.and()
//				.param("B")
//				.param("B")
//				.beginGroup()
//				.param("B")
//				.param("B")
//				.endGroup()
//				.or()
//				.and()
//				.beginGroup()
//				.param("C")
//				.and()
//				.group(Group.with("D").or().param("E"))
//				.endGroup()
//				.toString();

		System.out.println(query);

//		assertEquals("\"A\" AND \"B\" OR (\"C\" AND (\"D\" OR \"E\"))", query);
	}
}
