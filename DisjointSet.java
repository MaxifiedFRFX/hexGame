package hexGame;

/**
 *   Maintains a disjoint set of equivalence classes
 */
public class DisjointSet  {
    // 1. Add private data (including an array of appropriate size) to
    //    maintain the state of the disjoint set.
    // 2. Add a constructor that provides for up to 125 disjoint sets
    //    (the 121 hex cells plus the 4 bases).
    // 3. Add a method to make a set.
    // 4. Add a find method.
    // 5. Add a union method (unlike C++, union is a valid identifier in Java).
    // 6. Add a method that returns the number of equivalence classes
    //    (recall that the number of equivalence classes reduce by one
    //    after the application of a union operation).
    // 7. Add a method that splits all the equivalence classes into
    //    sets containing just one element (useful for resetting the
    //    board for a new game).
	
	private int[] set;
	
	public DisjointSet(int length) {
		set = new int[length];
		makeSet(length);
	}
	
	public void makeSet(int length) {
		for (int i = 0; i < length; i++) {
			set[i] = i;
		}
	}
	
	public int find(int index) {
		System.out.println("index: " + index);
		if (set[index] == index) {
			return index;
		}
		
		return find(set[index]);
	}
	
	public void union(int i1, int i2) {
		int p1 = find(i1),
			p2 = find(i2);
		set[p1] = p2;
	}
	
}
