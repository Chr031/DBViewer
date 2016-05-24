package general;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import application.utils.ClassFinder;
@Ignore
public class TestClassLoader {
	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(TestClassLoader.class);

	@Test
	public void test() throws Exception {
		File jarFile = new File("./planets.jar");

		URL jarUrl = new URL("jar:"+ jarFile.toURI().toURL()+ "!/");
		log.info(jarUrl.toString() + " " + jarUrl.toExternalForm());
		ClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl }, this.getClass().getClassLoader());
		
		log.info(jarUrl.getContent());
		
		//Class<?> planet = Class.forName("game.planet.Planet");
		URL urlPlanet  = classLoader.getResource("game/planet/Planet.class");
		
		log.info(urlPlanet);

		Enumeration<URL> urls = classLoader.getResources("game/planet/");

		Assert.assertTrue("resource url should not be empty ", urls.hasMoreElements());
		List<Class<?>> classList = ClassFinder.getPackageClasses(classLoader, "game.planet");
		Assert.assertTrue("No class has been found ", classList.size() > 0);

	}

}
