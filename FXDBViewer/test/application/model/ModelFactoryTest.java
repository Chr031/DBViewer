package application.model;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class ModelFactoryTest {

	@Test
	public void testExternalJarTest() throws Exception {
		Model model = ModelFactory.createModel("testModel", "application.model.pojo.acl");
		Assert.assertTrue(model.getObjectClassList().size()>0);
	
		Model model2 = ModelFactory.createModel("planets",new File("./planets.jar"), "game.planet");
		Assert.assertTrue(model2.getObjectClassList().size()>0);
	
	}

}
