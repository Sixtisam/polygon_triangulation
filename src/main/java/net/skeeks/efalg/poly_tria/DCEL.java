package net.skeeks.efalg.poly_tria;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class DCEL {

	public List<Vertex> vertices = new ArrayList<Vertex>();

	public List<HalfEdge> edges = new ArrayList<HalfEdge>();

	public List<Face> faces = new ArrayList<Face>();

	public DCEL() {
	}

	/**
	 * Insert an edge from v1 to v2. This methods assumes this new edge halves the
	 * existing polygon in 2 faces.
	 * 
	 */
	public void insertEdge(Vertex v1, Vertex v2) {
		Face newFace = new Face();
		faces.add(newFace);
		HalfEdge[] prevEdges = getPreviousVerticesWithCommonFace(v1, v2);
		HalfEdge v1PreviousEdge = prevEdges[0];
		HalfEdge v2PreviousEdge = prevEdges[1];
		HalfEdge v1Edge = v1PreviousEdge.next;
		HalfEdge v2Edge = v2PreviousEdge.next;
		// create new edges
		HalfEdge newEdge = new HalfEdge();
		HalfEdge newTwinEdge = new HalfEdge();

		newEdge.twin = newTwinEdge;
		newTwinEdge.twin = newEdge;

		// newEdge encloses existing face
		newEdge.from = v1;
		v1PreviousEdge.next = newEdge;
		newEdge.face = v1PreviousEdge.face;
		newEdge.face.edge = newEdge;
		assert v2PreviousEdge.face == v1PreviousEdge.face; // must be same face
		assert v1PreviousEdge.face != null; // must not be the OUTER face
		newEdge.next = v2Edge;

		// newTwinEdge encloses the new face
		newTwinEdge.from = v2;
		v2PreviousEdge.next = newTwinEdge;
		newTwinEdge.next = v1Edge;
		// update face of the new halved polygon
		newFace.edge = newTwinEdge;
		HalfEdge currEdge = newTwinEdge;
		do {
			currEdge.face = newFace;
			currEdge = currEdge.next;
		} while (currEdge != newTwinEdge);
	}

	/**
	 * Edge case: if more than 2 half-edges point to a vertex, we have to find the
	 * common face first.
	 */
	public HalfEdge[] getPreviousVerticesWithCommonFace(Vertex v1, Vertex v2) {

		// TODO ske check if this is necessary
//		if (v1.edge.face == v2.edge.face) {
//			// Find previous edge for v2
//			HalfEdge v2PrevCandidate = v2.previousEdge();
//			while (v2PrevCandidate != v2.edge.twin && v2PrevCandidate.face != v1.edge.face) {
//				v2PrevCandidate = v2PrevCandidate.next.twin;
//			}
//			
//			// Find previous edge for v1
//			HalfEdge v1PrevCandidate = v1.previousEdge();
//			while (v1PrevCandidate != v2.edge.twin && v2PrevCandidate.face != v1.edge.face) {
//				v1PrevCandidate = v1PrevCandidate.next.twin;
//			}
//			if(v1.edge.face != v2PrevCandidate.face || v2.edge.face != v1PrevCandidate.face) {
//				System.out.println("Trying to insert edge " + v1 + " -> " + v2);
//				System.out.println(v1.edge.face + " <-> " + v2PrevCandidate.face);
//				System.out.println(v2.edge.face + " <-> " + v1PrevCandidate.face);
//			}
//			assert v1.edge.face == v2PrevCandidate.face;
//			assert v2.edge.face == v1PrevCandidate.face;
//			return new HalfEdge[] { v1PrevCandidate, v2PrevCandidate };
//		}

		HalfEdge v1PrevCandidate = v1.previousEdge();
		do {
			HalfEdge v2PrevCandidate = v2.previousEdge();
			do {
				if (v1PrevCandidate.face == v2PrevCandidate.face) {
					return new HalfEdge[] { v1PrevCandidate, v2PrevCandidate };
				}
				v2PrevCandidate = v2PrevCandidate.next.twin;
			} while (v2PrevCandidate != v2.edge.twin);
			v1PrevCandidate = v1PrevCandidate.next.twin;
		} while (v1PrevCandidate != v1.edge.twin);

		throw new RuntimeException("No common face found!");
	}

	public void integrityCheck() {
		for (Vertex v : vertices) {
			assert v.edge != null;
			assert v.type != null;
		}

		for (HalfEdge e : edges) {
			assert (e.face != null && e.twin.face == null) || (e.face == null && e.twin.face != null);
			assert e.from != null;
			assert e.next != null;
		}

		for (Face f : faces) {
			assert f.edge != null;
		}
	}

}
