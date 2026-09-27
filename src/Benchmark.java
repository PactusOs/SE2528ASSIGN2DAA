import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Benchmark{
    private static final int[] SIZES={100,1000,10000,100000};
    private static final int TRIALS=5;
    private static final int GETS=10000;
    private static final int SEARCHES=1000;
    private static final int UPDATES=1000;
    private static final int SEED=42;
    private static class Result{
        double time;
        long metric;
        boolean valid;
        Result(double time,long metric){
            this.time=time;
            this.metric=metric;
            valid=true;
        }
        Result(double time,long metric,boolean valid){
            this.time=time;
            this.metric=metric;
            this.valid=valid;
        }
    }
    public static void main(String[] args)throws Exception{
        new File("results/tables").mkdirs();
        warmup();
        workload1();
        workload2();
        workload3();
        workload4();
        System.out.println("Benchmark complete");
    }

    private static void workload1()throws IOException{
        try(FileWriter w=new FileWriter("results/tables/workload1_random_access.csv")){
            w.write("n,structure,average_time_ms,accesses,theoretical\n");
            for(int n:SIZES){
                Random r=new Random(SEED);
                int[] values=data(n,r);
                int[] indices=indices(r,n,GETS);
                Result a=avgArrayGet(values,indices);
                Result l=avgListGet(values,indices);
                row(w,n,"DynamicArray",a,"O(1)=Theta(1)");
                row(w,n,"LinkedList",l,"O(n)=Theta(n)");
            }
        }
    }

    private static void workload2()throws IOException{
        try(FileWriter w=new FileWriter("results/tables/workload2_search.csv")){
            w.write("n,structure,average_time_ms,comparisons,theoretical\n");
            for(int n:SIZES){
                Random r=new Random(SEED);
                int[] values=data(n,r);
                int[] queries=data(SEARCHES,r);
                Result a=avgArrayContains(values,queries);
                Result l=avgListContains(values,queries);
                row(w,n,"DynamicArray",a,"best O(1) | average/worst O(n)");
                row(w,n,"LinkedList",l,"best O(1) | average/worst O(n)");
            }
        }
    }

    private static void workload3()throws IOException{
        try(FileWriter w=new FileWriter("results/tables/workload3_insertion_removal.csv")){
            w.write("n,structure,position,operation,average_time_ms,movements_or_accesses,theoretical\n");
            for(int n:SIZES){
                Random r=new Random(SEED);
                int[] values=data(n,r);
                Result a1=avgArrayUpdate(values,0,true);
                Result a2=avgArrayUpdate(values,0,false);
                Result l1=avgListUpdate(values,0,true);
                Result l2=avgListUpdate(values,0,false);
                Result a3=avgArrayUpdate(values,n/2,true);
                Result a4=avgArrayUpdate(values,n/2,false);
                Result l3=avgListUpdate(values,n/2,true);
                Result l4=avgListUpdate(values,n/2,false);
                row(w,n,"DynamicArray","beginning","insertion",a1,"O(n)=Theta(n)");
                row(w,n,"DynamicArray","beginning","removal",a2,"O(n)=Theta(n)");
                row(w,n,"LinkedList","beginning","insertion",l1,"O(1)=Theta(1)");
                row(w,n,"LinkedList","beginning","removal",l2,"O(1)=Theta(1)");
                row(w,n,"DynamicArray","middle","insertion",a3,"O(n)=Theta(n)");
                row(w,n,"DynamicArray","middle","removal",a4,"O(n)=Theta(n)");
                row(w,n,"LinkedList","middle","insertion",l3,"O(n)=Theta(n)");
                row(w,n,"LinkedList","middle","removal",l4,"O(n)=Theta(n)");
            }
        }
    }

    private static void workload4()throws IOException{
        try(FileWriter w=new FileWriter("results/tables/workload4_priority_processing.csv")){
            w.write("n,insert_average_time_ms,insert_comparisons,extract_average_time_ms,extract_comparisons,ordered,theoretical\n");
            for(int n:SIZES){
                Random r=new Random(SEED);
                int[] values=data(n,r);
                Result insert=avgHeapInsert(values);
                Result extract=avgHeapExtract(values);
                w.write(n+","+format(insert.time)+","+insert.metric+","+format(extract.time)+","+extract.metric+","+extract.valid+",insert best O(1) | average/worst O(log n) | peekMin O(1) | extractMin best O(1) | average/worst O(log n)\n");
            }
        }
    }

    private static Result avgArrayGet(int[] values,int[] indices){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            DynamicArray a=buildArray(values);
            a.resetMetrics();
            long start=System.nanoTime();
            for(int index:indices)a.get(index);
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=a.getAccesses();
        }
        return new Result(time/TRIALS,metric);
    }

    private static Result avgListGet(int[] values,int[] indices){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            LinkedList a=buildList(values);
            a.resetMetrics();
            long start=System.nanoTime();
            for(int index:indices)a.get(index);
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=a.getAccesses();
        }
        return new Result(time/TRIALS,metric);
    }

    private static Result avgArrayContains(int[] values,int[] queries){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            DynamicArray a=buildArray(values);
            a.resetMetrics();
            long start=System.nanoTime();
            for(int x:queries)a.contains(x);
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=a.getComparisons();
        }
        return new Result(time/TRIALS,metric);
    }
    private static Result avgListContains(int[] values,int[] queries){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            LinkedList a=buildList(values);
            a.resetMetrics();
            long start=System.nanoTime();
            for(int x:queries)a.contains(x);
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=a.getComparisons();
        }
        return new Result(time/TRIALS,metric);
    }
    private static Result avgArrayUpdate(int[] values,int index,boolean insert){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            DynamicArray a=buildArray(values);
            if(!insert)for(int i=0;i<UPDATES;i++)a.add(i);
            a.resetMetrics();
            long start=System.nanoTime();
            if(insert){
                for(int i=0;i<UPDATES;i++)a.add(index,i);
            }else{
                for(int i=0;i<UPDATES;i++)a.remove(index);
            }
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=a.getMovements();
        }
        return new Result(time/TRIALS,metric);
    }
    private static Result avgListUpdate(int[] values,int index,boolean insert){
        double time=0;
        long timeMetric=0;
        for(int t=0;t<TRIALS;t++){
            LinkedList a=buildList(values);
            if(!insert)for(int i=0;i<UPDATES;i++)a.add(i);
            a.resetMetrics();
            long start=System.nanoTime();
            if(insert){
                for(int i=0;i<UPDATES;i++)a.add(index,i);
            }else{
                for(int i=0;i<UPDATES;i++)a.remove(index);
            }
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            timeMetric=a.getAccesses();
        }
        return new Result(time/TRIALS,timeMetric);
    }
    private static Result avgHeapInsert(int[] values){
        double time=0;
        long metric=0;
        for(int t=0;t<TRIALS;t++){
            MinHeap h=new MinHeap();
            h.resetMetrics();
            long start=System.nanoTime();
            for(int x:values)h.insert(x);
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=h.getComparisons();
        }
        return new Result(time/TRIALS,metric);
    }
    private static Result avgHeapExtract(int[] values){
        double time=0;
        long metric=0;
        boolean valid=true;
        for(int t=0;t<TRIALS;t++){
            MinHeap h=new MinHeap();
            for(int x:values)h.insert(x);
            h.resetMetrics();
            int[] extracted=new int[values.length];
            long start=System.nanoTime();
            for(int i=0;i<values.length;i++)extracted[i]=h.extractMin();
            long end=System.nanoTime();
            time+=(end-start)/1_000_000.0;
            metric=h.getComparisons();
            for(int i=1;i<extracted.length;i++){
                if(extracted[i]<extracted[i-1])valid=false;
            }
        }
        return new Result(time/TRIALS,metric,valid);
    }

    private static DynamicArray buildArray(int[] values){
        DynamicArray a=new DynamicArray();
        for(int x:values)a.add(x);
        return a;
    }
    private static LinkedList buildList(int[] values){
        LinkedList a=new LinkedList();
        for(int x:values)a.add(x);
        return a;
    }

    private static int[] data(int count,Random r){
        int[] values=new int[count];
        for(int i=0;i<count;i++)values[i]=r.nextInt();
        return values;
    }

    private static int[] indices(Random r,int n,int count){
        int[] values=new int[count];
        for(int i=0;i<count;i++)values[i]=r.nextInt(n);
        return values;
    }
    private static void row(FileWriter w,int n,String structure,Result r,String theoretical)throws IOException{
        w.write(n+","+structure+","+format(r.time)+","+r.metric+","+theoretical+"\n");
    }

    private static void row(FileWriter w,int n,String structure,String position,String operation,Result r,String theoretical)throws IOException{
        w.write(n+","+structure+","+position+","+operation+","+format(r.time)+","+r.metric+","+theoretical+"\n");
    }
    private static String format(double value){
        return String.format(java.util.Locale.US,"%.4f",value);
    }

    private static void warmup(){
        Random r=new Random(SEED);
        int[] values=data(10000,r);
        int[] indices=indices(r,10000,GETS);
        avgArrayGet(values,indices);
        avgListGet(values,indices);
        avgArrayContains(values,data(SEARCHES,r));
        avgListContains(values,data(SEARCHES,r));
        avgArrayUpdate(values,0,true);
        avgListUpdate(values,0,true);
        avgHeapInsert(values);
        avgHeapExtract(values);
    }
}
