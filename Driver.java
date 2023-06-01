package project4;

public class Driver extends BST<Integer>{

	public static void main(String[] args) {
		Integer[] coll = new Integer[]{1, 3, 5, 6, 7, 9, 11};
		BST<Integer> bst = new BST<Integer>(coll);
		System.out.println(bst.toString());
		System.out.println(bst.toStringTreeFormat());
		
		System.out.println(bst.add(12));
		System.out.println(bst.remove(4));
		System.out.println(bst.toString());
		System.out.println(bst.get(3));
	}

}
