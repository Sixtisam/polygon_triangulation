// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
			if(curr == newEdge) {
				sameFace = true;
			}
			curr.face = newFace;
			curr = curr.next;
		} while(curr != newTwinEdge);
		 
		if(sameFace) {
			// we detected that our insertion of the new edge did not partition the polygon into two faces.
			// because of this, we completely overwrite 'commonFace' with the new face.
			faces.remove(commonFace);
		}
		
		integrityCheck();
	}
	
	public HalfEdge findNextEdge(HalfEdge helperEdge, Vertex v) {
		HalfEdge curr = helperEdge;
		while(curr.from != v) {
			curr = curr.prev;
		}
		return curr;
	}
	
	/**
	 * Finds an edge origintating from v with face 'commonFace'
	 */
	public HalfEdge findNextEdge(Vertex v, Face commonFace) {
		HalfEdge curr = v.edge;
		do {
			if(curr.face == commonFace) {
				return curr;
			}
			curr = curr.twin.next;
		} while(curr != v.edge);
		
		throw new RuntimeException("No edge found for common face " + commonFace);
	}
	
//	/**
//	 * Insert an edge from v1 to v2. This methods assumes this new edge halves the
//	 * existing polygon in 2 faces.
//	 * 
//	 */
//	public void insertEdge(Vertex v1, Vertex v2) {
//		Face newFace = new Face(false);
//		addFace(newFace);
//		HalfEdge[] prevEdges = getPreviousVerticesWithCommonFace(v1, v2);
//		HalfEdge v1PreviousEdge = prevEdges[0];
//		HalfEdge v2PreviousEdge = prevEdges[1];
//		HalfEdge v1Edge = v1PreviousEdge.next;
//		HalfEdge v2Edge = v2PreviousEdge.next;
//		assert v1PreviousEdge.next.face == v1PreviousEdge.face;
//		assert v2PreviousEdge.next.face == v2PreviousEdge.face;
//		// create new edges
//		HalfEdge newEdge = new HalfEdge();
//		HalfEdge newTwinEdge = new HalfEdge();
//
//		newEdge.twin = newTwinEdge;
//		newTwinEdge.twin = newEdge;
//
//		// newEdge encloses existing face
//		newEdge.from = v1;
//		v1PreviousEdge.next = newEdge;
//		newEdge.face = v1PreviousEdge.face;
//
//		newEdge.face.edge = newEdge;
//		assert v2PreviousEdge.face == v1PreviousEdge.face; // must be same face
//		assert v1PreviousEdge.face != null; // must not be the OUTER face
//		newEdge.next = v2Edge;
//
//		// newTwinEdge encloses the new face
//		newTwinEdge.from = v2;
//		v2PreviousEdge.next = newTwinEdge;
//		newTwinEdge.next = v1Edge;
//		// update face of the new halved polygon
//		newFace.edge = newTwinEdge;
//		HalfEdge currEdge = newTwinEdge;
//		System.out.println("Updating face counterclockwise, starting from " + currEdge.from);
//		Face sameFace = null;
//		do {
//			// when a polygon has at least one hole, a diagonal connecting to vertex must
//			// not necessary lead 2 separat polygons, they still can be one single polygon
//			// and therefore,
//			if (currEdge == newEdge) {
//				sameFace = newEdge.face;
//			}
//			System.out.println("Updating face " + currEdge);
//			currEdge.face = newFace;
//			currEdge = currEdge.next;
//		} while (currEdge != newTwinEdge);
//
//		if (sameFace != null) {
//			System.out.println("Same face: " + sameFace);
//			faces.remove(sameFace);
//		}
//	}

//	/**
//	 * Edge case: if more than 2 half-edges point to a vertex, we have to find the
//	 * common face first.
//	 */
//	public HalfEdge[] getPreviousVerticesWithCommonFace(Vertex v1, Vertex v2) {
//		// TODO ske in some cases where there is more than one hole, this function does
//		// not return the correct prev edges and the algorithm fails therefore.
//		HalfEdge v1PrevCandidate = v1.previousEdge();
//		do {
//			HalfEdge v2PrevCandidate = v2.previousEdge();
//			do {
//				if (v1PrevCandidate.face == v2PrevCandidate.face) {
//					assert v1PrevCandidate.face != null;
//					return new HalfEdge[] { v1PrevCandidate, v2PrevCandidate };
//				}
//				v2PrevCandidate = v2PrevCandidate.next.twin;
//			} while (v2PrevCandidate != v2.edge.twin);
//			v1PrevCandidate = v1PrevCandidate.next.twin;
//		} while (v1PrevCandidate != v1.edge.twin);
//
//		System.out.println("v1: " + v1);
//		System.out.println("v2: " + v2);
//		System.out.println(faces.stream().map(f -> f.color.toString()).collect(Collectors.joining()));
//		throw new RuntimeException("No common face found!");
//	}

	public void addFace(Face face) {
		assert !face.hole;
		faces.add(face);
	}

	public void integrityCheck() {
		for (Vertex v : vertices) {
			assert v.edge != null;
			assert v.type != null;
		}

		for (HalfEdge e : edges) {
			assert (e.face != null && e.twin.face == null) || (e.face == null && e.twin.face != null);
			assert e.from != null;
			assert e.prev != null;
			assert e.next != null;
		}

		for (Face f : faces) {
			assert f.edge != null;
		}
	}

}
