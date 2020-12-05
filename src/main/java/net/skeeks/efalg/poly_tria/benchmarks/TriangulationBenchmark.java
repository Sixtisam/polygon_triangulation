package net.skeeks.efalg.poly_tria.benchmarks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import net.skeeks.efalg.poly_tria.PolygonGenerator;
import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.PolygonTriangulation;
import net.skeeks.efalg.poly_tria.core.Triangle;

@BenchmarkMode(Mode.AverageTime)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class TriangulationBenchmark {

	public static void main(String[] args) throws Exception {
		Main.main(args);
	}

	@State(Scope.Benchmark)
	public static class MyState {
		public final List<Polygon> polygon1_000_000 = Collections
				.singletonList(PolygonGenerator.generatePolygon(1_00_000));
		public final List<Polygon> polygon5_000_000 = Collections
				.singletonList(PolygonGenerator.generatePolygon(5_000_000));
		public final List<Polygon> polygon10_000_000 = Collections
				.singletonList(PolygonGenerator.generatePolygon(10_000_000));
	}

	@Benchmark
	public static List<Triangle> triangulation1_000_000(MyState state) {
		return PolygonTriangulation.triangulate(state.polygon1_000_000, Collections.emptyList());
	}
//
//	@Benchmark
//	public static List<Triangle> triangulation5_000_000(MyState state) {
//		return PolygonTriangulation.triangulate(state.polygon5_000_000, Collections.emptyList());
//	}
//
//	@Benchmark
//	public static List<Triangle> triangulation10_000_000(MyState state) {
//		return PolygonTriangulation.triangulate(state.polygon10_000_000, Collections.emptyList());
//	}
}
