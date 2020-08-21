package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		if (g == null || !g.map.containsKey(p1) || !g.map.containsKey(p2)) 
			return null;
		boolean[] visited = new boolean[g.members.length];
		for (int i = 0; i < visited.length; i++) 
			visited[i] = false;
		int temp = g.map.get(p1);
		visited[temp] = true;
		Queue <Person> q = new Queue<Person>();
		q.enqueue(g.members[temp]);
		Person start = g.members[g.map.get(p1)];
		Person target = g.members[g.map.get(p2)];
		Person front = start;
		boolean found = false;
		int[] paths = new int[g.members.length];
		while (!q.isEmpty()) {
			front = q.dequeue();
			if (front.name.equals(p2)) {
				found = true;
				break;
			}
			else {
				Friend fr = front.first;
				while (fr != null) {
					if (visited[fr.fnum] == false) {
						paths[fr.fnum] = g.map.get(front.name); 
						visited[fr.fnum] = true; 
						q.enqueue(g.members[fr.fnum]);
					}
					fr = fr.next;
				}
			}
		}
		if (found) {
			ArrayList<String> list = new ArrayList<String>();
			int b = g.map.get(p2);
			int a = g.map.get(p1);
			if (b == a) {
				list.add(p2);
				return list;
			}
			else {
				list.add(g.members[b].name);
			}
			while (b != a) {
				b = paths[b];
				list.add(0, g.members[b].name);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		if (g == null)
			return null;
		school = school.toLowerCase();
		ArrayList<ArrayList<String>> cliqs = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		Person p = null;
		boolean[] visited = new boolean[g.members.length];
		Stack<Person> stk = new Stack<Person>();
		for (int i = 0; i < g.members.length; i ++) {
			p = g.members[i];
			if (p.student && p.school.equals(school))
				stk = dfs(g, p, stk, visited, school);
			if (!stk.isEmpty()) {
				while (!stk.isEmpty()) 
					temp.add(stk.pop().name);
				cliqs.add(temp);
				temp = new ArrayList<String>();
			}
		}
		return cliqs;
	}
	
	private static Stack<Person> dfs(Graph g, Person p, Stack<Person> stk, boolean[] visited, String school){
		if (visited[g.map.get(p.name)]) 
			return stk;
		Person temp = null;
		if (p.student && p.school.equals(school)) {
			if (visited[g.map.get(p.name)] == false) {
				visited[g.map.get(p.name)] = true;
				for (Friend fr = p.first; fr != null; fr = fr.next) {
					temp = g.members[fr.fnum];
					dfs(g, temp, stk, visited, school);
				}
				stk.push(p);
			}
		}
		return stk;
	}
	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		if (g == null || g.members == null)
			return null;
		ArrayList<String> con = new ArrayList<String>();
	    int count = 0;
	    int n = g.members.length;
        int[] lo = new int[n];
        int[] prev = new int[n];
        boolean[] cons = new boolean[n];
        for (int v = 0; v < n; v++)
            lo[v] = -1;
        for (int v = 0; v < n; v++)
            prev[v] = -1;        
        for (int v = 0; v < n; v++) {
            if (prev[v] == -1)
                cons = dfs(g, v, v, prev, lo, count, cons);
        }
	    for (int i = 0; i < n; i++) {
	    	if (cons[i]) 
	    		con.add(g.members[i].name);
	    }
		return con;
	}
	
	private static boolean[] dfs(Graph g, int u, int v, int[] prev, int[] lo, int count, boolean[] cons) {
		int c = 0;
		prev[v] = count++;
		lo[v] = prev[v];
		for (Friend fr = g.members[v].first; fr != null; fr = fr.next) { 
		if (prev[fr.fnum] == -1) {
		    c++;
		    int f = fr.fnum;
		    dfs(g, v, f, prev, lo, count, cons);
		    lo[v] = Math.min(lo[v], lo[fr.fnum]);
		    if (lo[fr.fnum] >= prev[v] && u != v) 
		        cons[v] = true;
		}
		else if (fr.fnum != u)
			lo[v] = Math.min(lo[v], prev[fr.fnum]);
		}	
		 if (u == v && c > 1)
			 cons[v] = true;
		 return cons;
	}
}