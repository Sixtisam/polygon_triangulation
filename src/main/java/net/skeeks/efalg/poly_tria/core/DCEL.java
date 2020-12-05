// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class DCEL {
	/**
	 * contains all added HalfEdge added through phase 1.
	 * It is needed so we now for which edges we should start phase 2.
	 */
	public List<HalfEdge> newConnections = new ArrayList<>();

	public DCEL() {
	}

	public void insertEdge(Vertex v1, HalfEdge helperEdge) {
		Vertex v2 = helperEdge.helper;
		assert v2 != null;

		HalfEdge v2Next = findNextEdge(helperEdge, v2);
		HalfEdge v2Prev = v2Next.prev;

		// because v is always the vertex currently inspected by the sweep line and the
		// fact that only MERGE vertex typ add two outgoing edges, we know that v.edge
		// is always CORRECT.
		HalfEdge v1Next = v1.edge;
		HalfEdge v1Prev = v1Next.prev;

		// create new edges
		HalfEdge newEdge = new HalfEdge();
		HalfEdge newTwinEdge = new HalfEdge();

		newEdge.twin = newTwinEdge;
		newTwinEdge.twin = newEdge;

		newEdge.from = v1;
		newEdge.prev = v1Prev;
		newEdge.next = v2Next;

		v1Prev.next = newEdge;
		v2Next.prev = newEdge;

		newTwinEdge.from = v2;
		newTwinEdge.prev = v2Prev;
		newTwinEdge.next = v1Next;

		v2Prev.next = newTwinEdge;
		v1Next.prev = newTwinEdge;

		newConnections.add(newEdge);
		newConnections.add(newTwinEdge);
	}

	public HalfEdge findNextEdge(HalfEdge helperEdge, Vertex v) {
		if (helperEdge.from == v) {
			return helperEdge;
		}
		HalfEdge curr = helperEdge.prev;

		while (curr.from != v && curr != helperEdge) {
			curr = curr.prev;
		}
		if (curr == helperEdge) {
			throw new RuntimeException("Vertex " + v + " not reachable from edge " + helperEdge);
		} else {
			return curr;
		}
	}
}
