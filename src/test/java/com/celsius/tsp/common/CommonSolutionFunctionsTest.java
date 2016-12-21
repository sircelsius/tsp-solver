package com.celsius.tsp.common;

import static org.junit.Assert.assertEquals;

import com.celsius.tsp.proto.TspService;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link CommonSolutionFunctions}.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
public class CommonSolutionFunctionsTest {
  @Test
  public void testCalculateWeight() throws Exception {
    List<TspService.Edge> edges = Lists.newArrayList();

    for (int i = 0; i < 5; i++) {
      edges.add(
          TspService.Edge.newBuilder()
            .setDepartureVerticeId(i)
            .setArrivalVerticeId(i + 1)
            .setValue(1)
            .build()
      );
    }

    int weight = CommonSolutionFunctions.calculateWeight(edges);
    assertEquals(weight, 5);
  }
}
