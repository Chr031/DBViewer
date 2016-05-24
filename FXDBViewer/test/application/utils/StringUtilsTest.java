package application.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void test() {
		String reduced = StringUtils.wordReducer("Abc def ghi", 7);
		Assert.assertEquals("Abc def...", reduced);
		
		reduced = StringUtils.wordReducer("Abc def ghi", 20);
		Assert.assertEquals("Abc def ghi", reduced);
		
		reduced = StringUtils.wordReducer("Abc def ghi", 4);
		Assert.assertEquals("Abc...", reduced);
	}

}
