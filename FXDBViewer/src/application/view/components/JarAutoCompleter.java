package application.view.components;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JarAutoCompleter extends AutoCompleter {

	private static final Logger log = LogManager.getLogger(JarAutoCompleter.class);

	/**
	 * Should be unit tested!
	 * 
	 * @author Bleu
	 *
	 */
	class Tree {
		final TreeNode root = new TreeNode(null);

		void addNode(TreeNode node) {
			root.addNode(node);
		}

		TreeNode getNode(String nodeName) {
			return root.getNode(nodeName);
		}

		List<TreeNode> getNodes() {
			return root.getNodes();
		}

		List<TreeNode> getNodes(String pattern) {
			return root.getNodes(pattern);
		}

		public void addEntry(String[] entryPath) {
			TreeNode current = root;
			TreeNode next;
			int i = 0;
			while (i < entryPath.length && (next = current.getNode(entryPath[i++])) != null) {
				current = next;
			}
			i--;
			while (i < entryPath.length) {
				current.addNode(current = new TreeNode(entryPath[i++]));
			}
		}

		public List<TreeNode> getNodePath(String[] path) {
			TreeNode current = root;
			TreeNode next = null;
			int i = 0;
			while (i < path.length && (next = current.getNode(path[i++])) != null) {
				current = next;
			}
			if (i==path.length && next != null) 
				return current.getNodes();
			else if (i==path.length && next == null)
				return current.getNodes(path[i-1]);
			else return null;
					
		}
	}

	class TreeNode {
		@Override
		public String toString() {
			return "TreeNode [name=" + name + "]";
		}

		final List<TreeNode> nodes;
		final String name;

		TreeNode(String name) {
			this.nodes = new ArrayList<>();
			this.name = name;
		}

		public TreeNode getNode(String nodeName) {
			for (TreeNode node : nodes) {
				if (node.name.equals(nodeName))
					return node;
			}
			return null;
		}

		void addNode(TreeNode node) {
			nodes.add(node);
		}

		List<TreeNode> getNodes() {
			return getNodes(null);
		}

		List<TreeNode> getNodes(String pattern) {
			List<TreeNode> matchingNodes = new ArrayList<>();
			if (pattern == null)
				matchingNodes.addAll(nodes);
			else
				for (TreeNode node : nodes) {
					if (node.name.startsWith(pattern))
						matchingNodes.add(node);
				}
			return matchingNodes;
		}
	}

	private final JarFile jarFile;

	private final Tree jarTree;

	public JarAutoCompleter(File jarFile) throws IOException {

		this.jarFile = new JarFile(jarFile);
		this.jarTree = new Tree();

		// build the tree
		Enumeration<JarEntry> entries = this.jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			log.debug(entry.getName());
			String[] entryPath = entry.getName().split("[/]");
			jarTree.addEntry(entryPath);
		}

	}

	/**
	 * Comma or semi column separator
	 */
	@Override
	public String getContext(String text) {
		String packages[] = text.split("[,;]");
		return packages[packages.length - 1].trim();
	}

	@Override
	public String[] getPossibilities(String context) {

		String path[] = context.split("[.]");

		log.debug(Arrays.toString(path));
		// Idea is to return the path content, for the value before the last
		// value and matching with the last value.
		List<TreeNode> matchingNodes = jarTree.getNodePath(path);
		if (matchingNodes == null)
			return null;
		List<String> possibilities = new ArrayList<>();
		matchingNodes.forEach((node) -> {
			possibilities.add((node.name.startsWith(path[path.length - 1]) ? "" : ".") + node.name);
		});

		return possibilities.toArray(new String[] {});
	}

}