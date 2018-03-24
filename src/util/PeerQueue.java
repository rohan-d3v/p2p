/**
 * Rohan Krishna Ramkhumar
 * Utility class used for Peerqueue/ peeking
 * Javadoc comments aren't there cause methods are self explanatory
 */
package util;

import java.util.LinkedList;

public class PeerQueue<T> {

	private LinkedList<T> q;//As obvious, this is the peer queue

	public T peek(){return q.peek(); }

	public T poll(){ return q.poll(); }

	public void add(T t){ q.add(t); }

	public PeerQueue(){ q = new LinkedList<T>(); }
}
