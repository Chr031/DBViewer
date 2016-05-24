package application.utils;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

public class ClassFinder {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ClassFinder.class);

	/**
	 * <p>
	 * Retrieves all class present in a specific package by reading the content
	 * of the directory present in the sources.
	 * </p>
	 * <p>
	 * FIXED May not work when dealing with jars ....
	 * </p>
	 * 
	 * @param cl
	 * @param pack
	 * @return
	 * @throws Exception
	 */
	public static List<Class<?>> getPackageClasses(ClassLoader cl, String pack)
			throws Exception {

		String dottedPackage = pack.replaceAll("[/]", ".");
		pack = pack.replaceAll("\\.", "/");
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// URL upackage = cl.getResource(pack);
		Enumeration<URL> urls = cl.getResources(pack);
		

		while (urls.hasMoreElements()) {
			URL upackage = urls.nextElement();
			switch (upackage.getProtocol()) {
			case "file":
				DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
				String line = null;
				while ((line = dis.readLine()) != null) {
					if (line.endsWith(".class")) {
						classes.add(cl.loadClass(dottedPackage + "." + line.substring(0, line.lastIndexOf('.'))));
					}
				}
				break;
			case "jar":
				
				// should be optimized !!!
				JarURLConnection jcx = (JarURLConnection) upackage.openConnection();
				Enumeration<JarEntry> entries = jcx.getJarFile().entries();
				while (entries.hasMoreElements()) {

					JarEntry je = entries.nextElement();
					// log.debug(je.getName());

					if (je.getName().startsWith(pack) && je.getName().endsWith(".class")) {
						line = je.getName().substring(pack.length() + 1);
						// not in a sub directory
						if (line.indexOf("/") < 0) {
							String className = dottedPackage + "." + line.substring(0, line.lastIndexOf('.'));

							classes.add(cl.loadClass(className));
							log.debug(className + " loaded");

						}
					}
				}

			}

		}
		return classes;
	}
}
