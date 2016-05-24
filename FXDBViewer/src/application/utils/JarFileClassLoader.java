package application.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileClassLoader extends URLClassLoader {

	private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(JarFileClassLoader.class);
	
	private JarFile jarFile;
	private File file;

	public JarFileClassLoader(File jarFile) throws IOException {
		super(new URL[] { new URL("jar:" + jarFile.toURI().toURL() + "!/") });
		this.file = jarFile;
		this.jarFile = new JarFile(jarFile);
	}

	public JarFileClassLoader(File jarFile, ClassLoader parent) throws IOException {
		super(new URL[] { new URL("jar:" + jarFile.toURI().toURL() + "!/") }, parent);
		this.jarFile = new JarFile(jarFile);
	}

	private String getJarBaseURLString() throws MalformedURLException {
		return "jar:" + file.toURI().toURL() + "!/";
	}

	@Override
	public URL findResource(String name) {
		URL res = super.findResource(name);
		if (res == null) {
			JarEntry jarEntry = jarFile.getJarEntry(name);
			if (jarEntry != null)
				try {
					return new URL(getJarBaseURLString() + name);
				} catch (MalformedURLException e) {
					log.error("unable to get the resource url ", e);
				}
		}
		return res;
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		Enumeration<URL> res = super.findResources(name);
		if (res == null || !res.hasMoreElements()) {
			
			
			Enumeration<JarEntry> jes = jarFile.entries();
			while (jes.hasMoreElements()) {
				log.debug("jar entry : " + jes.nextElement().getName());
			}
			
			JarEntry jarEntry = jarFile.getJarEntry(name);
			if (jarEntry != null)
				return new Enumeration<URL>() {
					boolean nextElementCalled = false;
					
					@Override
					public boolean hasMoreElements() {
						// TODO Auto-generated method stub
						return !nextElementCalled;
					}

					@Override
					public URL nextElement() {
						nextElementCalled = true;
						try {
							return new URL(getJarBaseURLString() + name);
						} catch (MalformedURLException e) {
							log.error("unable to get the resource url ", e);
						}
						return null;
					}
				};

		}

		return res;
	}

}
