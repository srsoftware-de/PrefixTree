package de.srsoftware.tools;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class PrefixTree implements Collection<String> {

	private TreeMap<Character, PrefixTree> collection;
	private int size = -1;
	private boolean ignoreCase = true;

	public PrefixTree() {
		this(false);
	}

	public PrefixTree(boolean ignoreCase) {
		collection = new TreeMap<Character, PrefixTree>();
		this.ignoreCase = ignoreCase;
	}

	public boolean add(String s) {
		if (s == null) return false;
		s = s.trim();
		if (s.length() < 1) return false;
		return internAdd(s);
	}

	public boolean addAll(Collection<? extends String> strings) {
		for (String s : strings) {
			if (!internAdd(s)) return false;
		}
		return true;
	}

	public void clear() {
		collection.clear();
		size = -1;
	}

	public boolean contains(Object o) {
		if (o == null) return false;
		String s = o.toString();
		if (s.length() < 1) return true;
		if (ignoreCase) s = s.toLowerCase();
		char c = s.charAt(0);
		PrefixTree subtree = collection.get(c);
		if (subtree == null) return false;
		return subtree.contains(s.substring(1));
	}

	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) return false;
		}
		return true;
	}

	public Vector<String> get(String prefix) {
		if (prefix == null) return null;
		Vector<String> result = new Vector<String>();
		if (prefix.length() < 1) return getAll();
		if (ignoreCase) prefix = prefix.toLowerCase();
		char c = prefix.charAt(0);
		PrefixTree subset = collection.get(c);
		if (subset == null) return result;
		for (String substring : subset.get(prefix.substring(1)))
			result.add(c + substring);

		return result;
	}

	public Vector<String> getAll() {
		Vector<String> result = new Vector<String>();
		for (char c : collection.keySet()) {
			PrefixTree subset = collection.get(c);
			if (subset.isEmpty()) result.add("" + c);
			for (String substring : subset.getAll())
				result.add(c + substring);
		}
		return result;
	}

	private boolean internAdd(String s) {
		if (s == null) return false;
		if (s.length() < 1) return false;
		size = -1;
		if (ignoreCase) s = s.toLowerCase();
		char c = s.charAt(0);
		PrefixTree subtree = collection.get(c);
		if (subtree == null) subtree = new PrefixTree();
		subtree.internAdd(s.substring(1));
		collection.put(c, subtree);
		return true;
	}

	public boolean isEmpty() {
		return collection.isEmpty();
	}

	public Iterator<String> iterator() {
		return getAll().iterator();
	}

	public boolean remove(Object o) {
		if (o == null) return false;
		String s = o.toString().trim();
		if (s.length() < 1) return true;
		size = -1;
		char c = s.charAt(0);
		PrefixTree subset = collection.get(c);
		if (subset == null) return false;
		subset.remove(s.substring(1));
		if (subset.isEmpty()) collection.remove(c);
		return true;
	}

	public boolean removeAll(Collection<?> col) {
		for (Object o : col) {
			if (!remove(o)) return false;
		}
		return true;
	}

	public boolean retainAll(Collection<?> col) {
		Vector<String> all = getAll();
		for (String s : all) {
			if (!col.contains(s)) {
				if (!remove(s)) return false;
			}
		}
		return true;
	}

	public int size() {
		if (size == -1) size = getAll().size();
		return size;
	}

	public Object[] toArray() {
		return getAll().toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		return null;
	}

	@Override
	public String toString() {
		return collection.toString();
	}

	public static void main(String[] args) {
		PrefixTree tree = new PrefixTree();
		tree.add("anton");
		tree.add("berta");
		tree.add("brett");
		tree.add("brot");
		tree.add("ei");
		tree.add("end");
		System.out.println(tree);
		System.out.println(tree.get("br"));
		tree.remove("brot");
		System.out.println(tree);
		System.out.println(tree.getAll());
		System.out.println(tree.get("br"));
		System.out.println(tree.get("e"));
	} //
}
