package crashtable;

public class CrashTable {
	
	final int MAX_CONFLICTS = 10;
	int size = 8;
	int p = 89;
	EntryNode[] table;
	EntryNode[] newtable;//second dummy table for resizing purposes
	private static class EntryNode {
		String key;
		Object value;
		EntryNode next;
	}
	
	public CrashTable(){
		table = new EntryNode[size];
		for(int i = 0 ; i < size; i++){
			EntryNode k = new EntryNode();
			table[i] = k;
			table[i].key = "start";
			table[i].value = i;
			table[i].next = table[i];//initializing the beginning values of each bucket, these are not considered as a linkedlist but only used as a starting point
		}
	}
	
	public void print() {//for debugging purposes only: prints each key value pair and its respective index groub
        int count = 0;
        System.out.println("----------------------");
        for(int i=0;i<table.length;i++) {
            if(table[i].next != table[i]) {
                EntryNode node = table[i].next;
                while(node.next != node) {
                    System.out.println(table[i].value + "    "+node.key+" "+node.value);
                    node = node.next;
                    count++;
                }
                System.out.println(table[i].value + "    "+node.key+" "+node.value);
                count++;
            }
        }
        System.out.println(count);
    }
	
	public void print2() {//same thing but for dummy table
        int count = 0;
        System.out.println("----------------------");
        for(int i=0;i<newtable.length;i++) {
            if(newtable[i].next != newtable[i]) {
                EntryNode node = newtable[i].next;
                while(node.next != node) {
                    System.out.println(newtable[i].value + "    "+node.key+" "+node.value);
                    node = node.next;
                    count++;
                }
                System.out.println(newtable[i].value + "    "+node.key+" "+node.value);
                count++;
            }
        }
        System.out.println(count);
    }
	
	private static int hash(String S, int p, int m) {//standard universal required hashing procedure
		int initialhash = 0;
		final char[] s = S.toCharArray();
		long powerp = 1;
        final int n = s.length;
        for (int i = 0; i < n; i++) {
        	initialhash = (int)((initialhash + (s[i] - 'a' + 1) * powerp) % m);
        	powerp = (powerp * p) % m;
        }
        return initialhash;
	}	
	
//
//	// TODO: class attributes
//
	public Object get(String key) {
		int hash = hash(key, p, size);
		int i;
		Object obj = null;
		boolean found = false;
		boolean lastcheck = true;
		for (i = 0; i < table.length; i++) {
			if (table[i].value == (Object) hash && table[i].next != table[i]){
				EntryNode temp = table[i].next;
				while(temp.next != temp) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
						lastcheck = false;
						break;
					}
					temp = temp.next;
				}
				if(lastcheck == true) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
					}
				}
			}
		}
		if(found) {
			return obj;
		}
		else {
			return null;
		}
	}
//
	public Object get(String key, Object def) {//same thing but returns def
		int hash = hash(key, p, size);
		int i;
		Object obj = null;
		boolean found = false;
		boolean lastcheck = true;
		for (i = 0; i < table.length; i++) {
			if (table[i].value == (Object) hash && table[i].next != table[i]){
				EntryNode temp = table[i].next;
				while(temp.next != temp) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
						lastcheck = false;
						break;
					}
					temp = temp.next;
				}
				if(lastcheck == true) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
					}
				}
			}
		}
		if(found) {
			return obj;
		}
		else {
			return def;
		}
	}

	public Object get2(String key) {//same thing but for dummy
		int hash = hash(key, p, size);
		int i;
		Object obj = null;
		boolean found = false;
		boolean lastcheck = true;
		for (i = 0; i < newtable.length; i++) {
			if (newtable[i].value == (Object) hash && newtable[i].next != newtable[i]){
				EntryNode temp = newtable[i].next;
				while(temp.next != temp) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
						lastcheck = false;
						break;
					}
					temp = temp.next;
				}
				if(lastcheck == true) {
					if(temp.key.equals(key)) {
						found = true;
						obj = temp.value;
					}
				}
			}
		}
		if(found) {
			return obj;
		}
		else {
			return null;
		}
	}
//
	public Object put(String key, Object value) {
		//System.out.print(hash(key,p,size) + " ");
		if(get(key) != null) {//case for when we are replacing the value at the specified key since it is already found
			int hash = hash(key, p, size);
			int i;
			Object obj = null;
			for (i = 0; i < table.length; i++) {
				boolean lastcheck = true;
				if (table[i].value == (Object) hash){
					EntryNode temp = table[i].next;
					while(temp.next != temp) {
						if(temp.key.equals(key)) {
							obj = temp.value;
							temp.value = value;
							lastcheck = false;
							return obj;
						}
						temp = temp.next;
					}
					if(lastcheck == true) {
						if(temp.key.equals(key)) {
							obj = temp.value;
							temp.value = value;
							return obj;
						}
					}
				}
			}
		}
		else {
			int hash = hash(key, p, size);
			int i;
			for (i = 0; i < table.length; i++) {
				if (table[i].value == (Object) hash){
					if(table[i].next == table[i]) {//case when the bucket of linkedlist is currently empty REMEMBER: I tied the end of the bucket 
													//linkedlist to themselves to indicate the end of the bucket
						EntryNode newnode = new EntryNode();
						newnode.key = key;
						newnode.value = value;
						table[i].next = newnode;
						newnode.next = newnode;
					}
					else {//case when we have at least one thing inside the bucket
						int counter = 0;
						EntryNode temp = table[i].next;
						while(temp != temp.next) {
							counter++;
							temp = temp.next;
						}
						counter++;
						if( counter != MAX_CONFLICTS) {//when we dont have to resize and rehash
							EntryNode newnode = new EntryNode();
							newnode.key = key;
							newnode.value = value;
							newnode.next = table[i].next;
							table[i].next = newnode;
						}
						else {//when we need to resize and rehash due to us reaching the threshold conflict
							resize(size);
							EntryNode newnode = new EntryNode();
							newnode.key = key;
							newnode.value = value;
							hash = hash(key, p, size);
							for(i = 0; i < table.length; i++) {
								if (table[i].value == (Object) hash){
									if(table[i].next == table[i]) {
										table[i].next = newnode;
										newnode.next = newnode;
									}
									else {
										newnode.next = table[i].next;
										table[i].next = newnode;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
//
	
	public Object put2(String key, Object value) {//same thing but for dummy
		//System.out.print(hash(key,p,size) + " ");
		if(get2(key) != null) {
			int hash = hash(key, p, size);
			int i;
			Object obj = null;
			for (i = 0; i < newtable.length; i++) {
				boolean lastcheck = true;
				if (newtable[i].value == (Object) hash){
					EntryNode temp = newtable[i].next;
					while(temp.next != temp) {
						if(temp.key.equals(key)) {
							obj = temp.value;
							temp.value = value;
							lastcheck = false;
							return obj;
						}
						temp = temp.next;
					}
					if(lastcheck == true) {
						if(temp.key.equals(key)) {
							obj = temp.value;
							temp.value = value;
							return obj;
						}
					}
				}
			}
		}
		else {
			int hash = hash(key, p, size);
			int i;
			for (i = 0; i < newtable.length; i++) {
				if (newtable[i].value == (Object) hash){
					if(newtable[i].next == newtable[i]) {//case when the bucket of linkedlist is currently empty
						EntryNode newnode = new EntryNode();
						newnode.key = key;
						newnode.value = value;
						newtable[i].next = newnode;
						newnode.next = newnode;//correct order?
					}
					else {
						int counter = 0;
						EntryNode temp = newtable[i].next;
						while(temp != temp.next) {
							counter++;
							temp = temp.next;
						}
						counter++;
						if( counter != MAX_CONFLICTS) {//when we dont have to resize and rehash
							EntryNode newnode = new EntryNode();
							newnode.key = key;
							newnode.value = value;
							newnode.next = newtable[i].next;
							newtable[i].next = newnode;
						}
						else {
							resize(size);
							EntryNode newnode = new EntryNode();
							newnode.key = key;
							newnode.value = value;
							hash = hash(key, p, size);
							for(i = 0; i < newtable.length; i++) {
								if (newtable[i].value == (Object) hash){
									if(newtable[i].next == newtable[i]) {
										newtable[i].next = newnode;
										newnode.next = newnode;
									}
									else {
										newnode.next = newtable[i].next;
										newtable[i].next = newnode;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public Object remove(String key) {
		if(get(key) != null) {
			int hash = hash(key, p, size);
			int i;
			Object obj = null;
			boolean lastcheck = true;
			for (i = 0; i < table.length; i++) {
				if (table[i].value == (Object) hash){
					EntryNode tempbehind = table[i];
					int sizeof = 0;
					EntryNode temp = table[i].next;
					while(temp.next != temp) {
						sizeof++;
						temp = temp.next;
					}
					if(sizeof == 0) {	//case for getting rid of a whole hash linked list since there was only 1 element in it and we deleted it.
				        obj = table[i].next.value;
						table[i].next = table[i];
				        return obj;
					}
					else {
						temp = table[i].next;
						while(temp.next != temp) {
							if(temp.key.equals(key)) {
								tempbehind.next = temp.next;
								obj = temp.value;
								lastcheck = false;
								return obj;
							}
							tempbehind = temp;
							temp = temp.next;
						}
						if(lastcheck == true) {
							if(temp.key.equals(key)) {
								obj = temp.value;
								tempbehind.next = tempbehind;
								return obj;
							}
						}
						break;
					}
				}
			}
		}
		else {
			return null;
		}
		return null;
	}
//
	public String[] getKeys() {
		int counter = 0;
		for(int i = 0; i < table.length; i++) {
			if(table[i].next != table[i]) {
				EntryNode temp = table[i].next;
				while(temp.next != temp) {
					counter++;
					temp = temp.next;
				}
				counter++;
			}
		}
		String[] ourarray = new String[counter];//declares an array with the size of the number of elements inside our whole bucket system and adds every element to ourarray
		int j = 0;
		for(int i = 0; i < table.length; i++) {
			if(table[i].next != table[i]) {
				EntryNode temp = table[i].next;
				while(temp.next != temp) {
					ourarray[j] = temp.key;
					j++;
					temp = temp.next;
				}
				ourarray[j] = temp.key;
				j++;
			}
		}
		return ourarray;
	}
//
	public void resize(int oursize) {//resizes our global size by doubling it and declares a new table with the newly hashed values(due to new size) from our current table
		oursize = oursize * 2;		//then after everything is rehashed and put into the new table, this new table is cloned to our normal table named table at the very end.
		size = oursize;
		newtable = new EntryNode[oursize];
		for(int i = 0 ; i < oursize; i++){
			EntryNode k = new EntryNode();
			newtable[i] = k;
			newtable[i].key = "start";
			newtable[i].value = i;
			newtable[i].next = newtable[i];
		}
		for(int i = 0; i < table.length; i++) {
			EntryNode temp = table[i].next;
			if(table[i].next != table[i]) {
				while(temp.next != temp) {
					put2(temp.key,temp.value);//rehashing and insertion to the dummy table is done here at put2
					temp = temp.next;
				}
				put2(temp.key,temp.value);
			}
		}
		table = newtable.clone();
		
	}
}
