package application.model;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import application.utils.ClassFinder;

public class ModelFactory {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ModelFactory.class);

	public static Model createModel(String modelName, String... packageNames) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		for (String packageName : packageNames) {
			List<Class<?>> pClasses = ClassFinder.getPackageClasses(ModelFactory.class.getClassLoader(), packageName);
			classes.addAll(pClasses);
		}		
		return new Model(modelName, ModelFactory.class.getClassLoader(), classes);
	}

	public static Model createModel(String modelName, File jarFile, String... packageNames) throws Exception {
		log.info("Load external model " + modelName + " from packages " + Arrays.toString(packageNames) + " of the file "
				+ jarFile.getAbsolutePath());

		JarFile jFile = new JarFile(jarFile);
		Enumeration<JarEntry> jes = jFile.entries();
		
		URL jarUrl = new URL("jar:" + jarFile.toURI() + "!/");
		ClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl });
		List<Class<?>> classes  = new ArrayList<>();
		
		while (jes.hasMoreElements()) {
			JarEntry je = jes.nextElement();
			for (String packageName : packageNames) {
				
				
				if (je.getName().startsWith(packageName.replaceAll("\\.", "/")) && je.getName().endsWith(".class")) {
					String className = je.getName().replaceAll("/", ".");
					className = className.substring(0, className.length()-6);
					log.debug("Load "+ className);
					Class<?> objectClass = classLoader.loadClass(className);
					classes.add(objectClass);
				}
			}
		}
		
		
		return new Model(modelName, classLoader, classes);

	}

}
