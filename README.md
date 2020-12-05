# Polygon Triangulation


## Ausführbare Programme

`PolygonTriangulationProgram` liest die Datei `polygon.in` ein und zeigt in einem `JFrame` die Triangulierung all dieser Polygone an.
Mit den Buttons `Next` und `Previous` kann nachvollzogen werden, welche Verbindungskanten eingefügt wurden (+ Reihenfolge). Diese Kanten werden `gelb` dargestellt. Damit lassen sich nur die eingefügten Kanten aus der 1. Phase des Algorithmus anzeigen, da dies für die 2. Phase nicht zuverlässig angezeigt werden kann.
Auch die Kategorien der einzelnen Knoten wird mittels Kreuzen dargestellt:
* Start-Vertex: CYAN
* End-Vertex: GREEN
* Split-Vertex: RED
* Merge-Vertex: ORANGE
* Regular-Vertex: DARK_GREY


`InteractiveHoleTriangulationProgram` bietet die Möglichkeit, selbst beliebige Polygon und Löcher zu zeichnen.
Initial lässt sich ein Polygon zeichen (Gegenuhrzeigersinn beachten).
* Mit `Left Click`setzt man einen neuen Knoten
* Mit `Shift+Mousemove`` kann man den zuletzt gesetzten Knoten verschieben
* Mit `Space` kann man ein neues Polygon beginnen
* Mit `N` wechselt man in den Löcher-Modus bzw. zurück.
	* Löcher müssen im Uhrzeigersinn gezeichnet werden.
* Mit `Right Click` kann man wieder von vorner beginnen.

Die Triangulation wird laufen berechnet und angezeigt.
Während des Zeichnens kann es vorkommen, dass ein Polygon oder Loch kurzzeigt ungültig wird (kreuzende Kanten, überlagernde Polygone). Je nach Konstellation endet dies in einer Endlosschlaufe, worauf das Programm neu gestartet werden muss.

Bei jedem Neuberechnen wird in der Konsole Java-Code für einen Unit-Test ausgegeben.

## Kurzbeschrieb Algorithmus
Mein Algorithmus ist ein wenig anders herausgekommen als die Trapezidee von Ihnen. Ich habe mich dabei vor allem an den Erklärungen von Mark de Berg in seinem Buch ["Computation Geometry"](https://archive.org/details/computationalgeo00berg) orientiert.

Der Algorithmus besteht aus 2 Phasen: In der ersten Phase wird das Polygon (inkl. den Löchern) in y-monotone Polygon unterteilt. Kurz gesagt heisst das ich unterteil so, dass es keine Split- oder/und Merge-Vertices mehr gibt. Dies wird damit erreicht, dass ein Split-Vertex immer zum nächstoberen Knoten verbunden wird, der die gleiche Kante als linker Nachbar hat.
Merge-Vertex müssen grundsätzlich gleich behandelt werden (aber auf dem Kopf), jedoch werden diese nachträglich verbunden, da man zum Zeitpunkt der Behandeln des Merge-Vertex die Vertices unterhalb noch nicht kennt/behandelt hat.
Daraus entstehen mehrere Polygone. Jedes davon wird nachher separat trianguliert.

## Kurzbeschrieb Implementation
Die Erklärungen von Mark de Berg haben mir zwar eine Idee gegeben, die Umsetzung war alles andere als einfach.

 - Bestimmen der Knoten-Typen: Hier muss `Math.atan2` verwendet werden, um den Innenwinkel zu bestimmen.
 - Active Set der Kanten: In der ersten Phase muss jeweils eruiert werden können, welche Kante links von einem Vertex auf Sweep Line liegt. Hier habe ich lang herumgetüftelt. Schlussendlich verwende ich nun eine [`NavigableSet`](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableSet.html). Die Ordnung bestimmt sich aus der x-Koordinate der Kanten auf der Sweep Line. Jedoch müssen nicht immer die x-Koordinaten aller aktiven Kanten berechnet werden, sondern nur `O(log n)` Kanten (beim Einfügen und Suchen). Denn die Ordnung der bereit eingefügt Kanten ändert sich nich, da sich diese nicht kreuzen.
 - Werden Verbindungskanten in der ersten Phase eingefügt, dann muss mein Algorithmus für die 2. Phase die Teilpolygone kennen. Dazu verwende ich eine Doubly Connected Edge List. Jede "Linie" wird von 2 Halb-Kanten repräsentiert (in beide Richtung). Jede dieser Halb-Kanten gehört zu einer Fläche. Die Anzahl Flächen entspricht dann nach der ersten Phase genau der Anzahl y-monotoner Polygone, die noch trianguliert werden müssen.
 - Um eine Verbindungskanten einzufügen, müssen die richtigen Next/Previous Halb-Kanten der beiden Knoten, die verbunden werden sollen, gefunden werden. Gibt es Löcher, kann es sogar mehrere Next/Previous Paare für die gleiche Fläche geben. Glücklicherweise kennt man als Anhaltspunkt die Kante, welche links der beiden Knoten liegt, verwenden. Dies gehört immer zur richtigen Fläche und man muss folglich nur von dieser Kante aus weiterlaufen bis man zum richtigen Knoten und somit zu den richtigen Next/Previous Halbkanten kommt.
 - Der Hauptalgorithmus der 1. Phase ist auch für Löcher genau gleich. Der einzige Unterschied ist beim `MakeMonotoneSweepLineStatus.init`. Löcher müssen anders als das Polygon in die `DCEL` eingefügt werden. Der Hauptunterschied liegt darin, dass die Klassifizierung der Knoten genau umgekehrt ist (z.B. Aussenwinkel statt Innenwinkel).
 - Zuweisen der Fläche bei Halb-Kanten von Löchern: Wenn eine Verbindungskante eingefügt wird, entsteht dadurch meistens eine 2 Fläche. Die Kanten, welche zu dieser Fläche gehören sollen, müssen mit dieser neuen Fläche aktualisiert werden. Dies kann man für den einfachen Fall mit dem entlanglaufen der Kanten lösen. Die klappt nicht für die Halbkanten von Löchern. Meine Lösung dazu ist, dass der erste Knoten eines Loches schaut, welche Kante links davon liegt, und dann alle Kanten des entsprechenden Lochs auf die Fläche aktualisiert.

 

## Korrektheit

### Validierung

Die Polygons werden bei der Eingabe nicht validiert. Trotzdem müssen sie folgende Bedingungen erfüllen:

- Die Kanten dürfen sich nicht überschneiden.
- Die Polygons dürfen sich untereinander nicht schneiden.
- Ein Loch muss ganz innerhalb eines (und nur eines) Polygons liegen.
- Punkte im Polygon dürfen nicht doppelt aufgelistet werden.

### Unit Tests
Ich verwende einige Tests um die Datenstruktur `DCEL`, `Edge`, `Polygon` und `MakeMonotoneSweepLineStatus` zu testen. Dies sind aber nur relativ einfach Tests.
In `TriangulationWithoutHolesTest` und `TriangulationWithHolesTest` werden eine Vielzahl von Polygons getestet. Diese habe ich mittels dem `InteractiveHoleTriangulationProgram` erstellt. So konnte ich von Auge verifizieren, dass die Triangulation korrekt war und dann als Unit-Test ablegen.

Auch das `PolygonTriangulationProgram` kann als Unit-Tests angesehen werden, da dort noch weitere Polygons trianguliert werden.

 ### Coverage
 Der Algorithmus selbst (hauptsächlich das Package `core`) hat eine Coverage von beinahe 100%.
 Die wenigen Ausnahmen wurden analysiert und womöglich weitere Unit-Tests hinzugefügt. Einzelne Zeilen werden in den Tests trotzdem nicht getestet, da diese Zeilen sowie einen Fehler auslösen würden.


### Asserts
Mittels assert-Statements werden im Code des Algorithmus Annahmen und Invarianten überprüft.

### PVS Studio
PVS-Studio wurde durchgeführt, um allfällige Code-Smells zu beseitigen.


## Skalierbarkeit


### Laufzeit
Die theoretische Laufzeigt beträgt `O(n log(n))`.

Ein Loch zu identifizieren dauert `O(v)` (`Polygon.isClockwise()` pro Polygon, also schlimmstenfalls `O(p*v*l)` (p=Anzahl Polygon, l=Anzahl Löcher, v=durchschnittliche Knotenanzahl).

Der Algorithmus trianguliert jedes Polygon einzeln.

**Triangulierung eines einzelnen Polygons (inkl. alle dazugehörigen Löcher)**

v=Anzahl Knoten des Polygons und der dazugehörigen Löcher

- Das Sortieren in `MakeMonotoneSweepLineStatus.init` dauert `O(v log v)`
- Das Suchen der nächst-linken Kanten über das Tree-Set ist `O(log n)` (Rot-Schwarz-Baum von TreeMap). Auch das Einfügen ist `O(log n)`
	- Deshalb dauert das Abhandeln eines Events/Vertex nur `O(log v)`
- Das Einfügen einer Verbindungskante kann bis zu `O(v)` dauern (wenn keine neue Fläche entstanden ist werden alle Vertices aktualisiert). Dies betrifft aber nur beim ersten Mal pro Polygon `v` Kanten. Durchschnittlich gesehen sind hier wohl nur `O(log v)` Kanten betroffen, da je weiter unten die Sweep Line liegt, die Kantenanzahl sinkt.
- Das Triangulieren der y-monotone Teilpolygon dauert lineare Zeit, d.h. schlussendlich `O(v)` für das Triangulieren eines ganzen Polygons (2. Phase des Algorithmus)

Fazit: Der Algorithmus braucht ca. `O(v log v)` um ein einzelne Polygon (inkl. Löcher) zu triangulieren.

### Speicherverbrauch

Für das Einfügen von Verbindungskanten werden jeweils bis zu 3 neue Objekte generiert:
-  2x HalfEdge
- 1x Face

Für die 2. Phase werden die Knoten in einem Array gesammelt, für die Sweep-Line-Event-Liste.
In der 2. Phase wird für jedes Dreieck ein `Triangle` Objekt erstellt.