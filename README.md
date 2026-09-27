# Assignment 2—Algorithmic Analysis, Correctness and Performance Trade-offs

##  Overview

This project implements a Dynamic Array, Linked List, and Min-Heap in Java. It tests correctness, proves two loop invariants, analyzes asymptotic complexity, and measures performance on the four workloads required by the assignment.

## Project Structure

```text
assignment-2/
├── src/
│   ├── DynamicArray.java
│   ├── LinkedList.java
│   ├── MinHeap.java
│   ├── Benchmark.java
│   └── Tests.java
├── results/
│   ├── tables/
│   │   ├── workload1_random_access.csv
│   │   ├── workload2_search.csv
│   │   ├── workload3_insertion_removal.csv
│   │   └── workload4_priority_processing.csv
│   └── plots/
│       ├── plot1_execution_time_vs_n.png
│       └── plot2_comparisons_vs_n.png
└── README.md
```

## Correctness

### Proof 1 — Dynamic Array add(index,x)

**Invariant.** At the start of every loop iteration with index variable i, every original element from positions i-1 through size-1 has already been copied one position to the right into positions i through size, while positions before i are unchanged.

**Initialization.** Before the first iteration, i=size. No element has been shifted yet, so the invariant is true for the empty shifted range.

**Maintenance.** The loop copies `data[i-1]` to `data[i]`. This correctly shifts the next remaining element one position right. Then i decreases by one, so the invariant remains true for the next iteration.

**Termination.** The loop stops when i=index. All original elements from index through size-1 are now in positions index+1 through size. Position index is free for x.

**Postcondition.** Writing x to `data[index]` produces the original sequence with x inserted at index. The array size is increased by one, so the required insertion operation is correct.

### Proof 2—Min-Heap extractMin()

**Invariant.** During `siftDown`, all subtrees below the current position i satisfy the heap property, and only the element at i may violate the heap property with its children.

**Initialization.** The root is replaced by the last element after the minimum is saved. Every other subtree is unchanged and still satisfies the heap property, so only the root can violate it.

**Maintenance.** The smaller child is selected. If the current element is not greater than that child, the heap property is restored and the loop stops. Otherwise the two elements are swapped. The smaller element moves to i, and the only possible violation moves down to the selected child, so the invariant is preserved.

**Termination.** The loop ends when there is no child or the current element is not greater than the smaller child. Then the current node is no greater than both children, and all lower subtrees already satisfy the heap property.

**Postcondition.** The saved root was the minimum element before extraction. After sift-down, the remaining structure is a valid Min-Heap, so `extractMin()` returns the minimum and preserves the required heap property.

## Experimental Setup

Input sizes were `n=100, 1,000, 10,000, 100,000`. The workload operation counts were `m=10,000` for random access, `m=1,000` for search, `m=1,000` insertions and `m=1,000` removals for workload 3, and `n` insertions plus `n` extractions for workload 4.

Every experiment was run 5 times and the reported execution time is the average. Timing uses `System.nanoTime()`. Random data and workload inputs are generated before the timed section with seed `42`. A short warm-up is performed before the measurements. Printing and input generation are outside the timed sections.

For workload 1, the Dynamic Array access metric counts array element accesses and the Linked List metric counts node-to-node traversal steps. For workload 2, the metric is element comparisons. For workload 3, Dynamic Array counts element movements, including resize copies, while Linked List counts node traversal accesses. For workload 4, the metric is heap element comparisons.

The included results were measured with OpenJDK 21.0.11 on Linux x86_64 using the JVM default options. Times can differ on another machine or JVM version.

For workload 3, n=100 cannot support 1,000 removals from the original n elements. To keep the required m=1,000, the removal phase starts from n+1,000 elements prepared before timing. This keeps the removal count and index valid for every required n.


##  How to Run

Compile all source files:

```text
javac src/*.java
```

Run correctness tests:

```text
java -cp src Tests
```

Run all four benchmarks and regenerate the CSV tables:

```text
java -cp src Benchmark
```

##  Design Recommendations

| Workload | Structure | Reason |
|---|---|---|
| Random Access | Dynamic Array | Θ(1) indexed access |
| Search | Dynamic Array or Linked List | Both are Θ(n); implementation constants differ |
| Beginning Insertion / Removal | Linked List | Θ(1) at the beginning |
| Middle Insertion / Removal | Depends on workload details | Both are Θ(n) here, but for different physical work |
| Priority Processing | Min-Heap | peekMin() Θ(1), insert/extractMin() Θ(log n) |

## 12. Conclusion

The implementations satisfy the required operations and correctness tests. The experiments show the effect of physical organization: direct array indexing stays constant-time, linked traversal grows with distance through the list, linked beginning updates avoid shifting, and heap ordering supports repeated minimum extraction. The measured results generally follow the theoretical complexity while showing the expected effects of real JVM and hardware behavior.

## 13. References

- Nursultan Khaimuldin, Lecture 3 — Asymptotic Analysis and Algorithmic Correctness: Θ/O/Ω, Invariants, and Empirical Validation.
- Assignment 2 — Algorithmic Analysis, Correctness and Performance Trade-offs.
- R. Sedgewick & K. Wayne, *Algorithms* (4th ed.).
- T. H. Cormen, C. E. Leiserson, R. L. Rivest, C. Stein, *Introduction to Algorithms* (4th ed.).
