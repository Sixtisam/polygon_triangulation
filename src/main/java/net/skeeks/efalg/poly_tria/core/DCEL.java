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
	public List<Face> faces = new ArrayList<Face>();

	public DCEL() {
	}

	public void insertEdge(Vertex v1, HalfEdge helperEdge) {
		Vertex v2 = helperEdge.helper;
		assert v2 != null;

		HalfEdge v2Next = findNextEdge(helperEdge, v2);
		HalfEdge v2Prev = v2Next.prev;

		Face commonFace = v2Next.face;

		HalfEdge v1Next = findNextEdge(v1, commonFace);
		HalfEdge v1Prev = v1Next.prev;

		// create new edges
		HalfEdge newEdge = new HalfEdge();
		HalfEdge newTwinEdge = new HalfEdge();

		newEdge.twin = newTwinEdge;
		newTwinEdge.twin = newEdge;

		newEdge.from = v1;
		newEdge.face = commonFace;
		newEdge.prev = v1Prev;
		newEdge.next = v2Next;
		commonFace.edge = newEdge;

		v1Prev.next = newEdge;
		v2Next.prev = newEdge;

		newTwinEdge.from = v2;
		newTwinEdge.prev = v2Prev;
		newTwinEdge.next = v1Next;
		// face set below

		v2Prev.next = newTwinEdge;
		v1Next.prev = newTwinEdge;

		// add new face
		Face newFace = new Face(false);
		addFace(newFace);
		newFace.edge = newTwinEdge;

		// set new face on twin edge and other edges in same area
		HalfEdge curr = newTwinEdge;
		boolean sameFace = false;
		do {
			if (curr == newEdge) {
				sameFace = true;
			}
			curr.face = newFace;
			curr = curr.next;
		} while (curr != newTwinEdge);

		if (sameFace) {
			// we detected that our insertion of the new edge did not partition the polygon
			// into two faces.
			// because of this, we completely overwrite 'commonFace' with the new face.
			faces.remove(commonFace);
		}
	}

	public HalfEdge findNextEdge(HalfEdge helperEdge, Vertex v) {
		HalfEdge curr = helperEdge;
		while (curr.from != v) {
			curr = curr.prev;
		}
		return curr;
	}

	/**
	 * Finds an edge originating from v with face 'commonFace'
	 */
	public HalfEdge findNextEdge(Vertex v, Face commonFace) {
		// because v is always the vertex currently inspected by the sweep line and the fact that only MERGE vertex typ add two outgoing edges, we know that v.edge is always CORRECT.
		assert v.edge.face == commonFace;
		return v.edge;
//		HalfEdge curr = v.edge;
//		do {
//			if (curr.face == commonFace) {
//				return curr;
//			}
//			curr = curr.twin.next;
//		} while (curr != v.edge);
//
//		throw new RuntimeException("No edge found for common face " + commonFace);
	}

	public void addFace(Face face) {
		assert !face.hole;
		faces.add(face);
	}

}
