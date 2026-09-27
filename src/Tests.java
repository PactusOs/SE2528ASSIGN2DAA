public class Tests{
    public static void main(String[] args){
        testDynamicArray();
        testLinkedList();
        testMinHeap();
        System.out.println("All tests passed");
    }

    private static void testDynamicArray(){
        DynamicArray a=new DynamicArray();
        check(a.size()==0);
        expectIndex(()->a.get(0));
        expectIndex(()->a.remove(0));
        a.add(5);
        check(a.get(0)==5);
        a.add(0,3);
        a.add(2,5);
        check(a.contains(5));
        check(a.contains(3));
        check(!a.contains(7));
        check(a.remove(0)==3);
        check(a.remove(a.size()-1)==5);
        expectIndex(()->a.get(-1));
        expectIndex(()->a.add(a.size()+1,9));
        DynamicArray b=new DynamicArray();
        java.util.ArrayList<Integer> ref=new java.util.ArrayList<>();
        for(int i=0;i<10000;i++){
            b.add(i%100);
            ref.add(i%100);
        }
        b.add(5000,77);
        ref.add(5000,77);
        b.remove(5000);
        ref.remove(5000);
        for(int i=0;i<ref.size();i++)check(b.get(i)==ref.get(i));
    }

    private static void testLinkedList(){
        LinkedList a=new LinkedList();
        check(a.size()==0);
        expectIndex(()->a.get(0));
        expectIndex(()->a.remove(0));
        a.add(5);
        check(a.get(0)==5);
        a.add(0,3);
        a.add(2,5);
        check(a.contains(5));
        check(a.contains(3));
        check(!a.contains(7));
        check(a.remove(0)==3);
        check(a.remove(a.size()-1)==5);
        expectIndex(()->a.get(-1));
        expectIndex(()->a.add(a.size()+1,9));
        LinkedList b=new LinkedList();
        java.util.LinkedList<Integer> ref=new java.util.LinkedList<>();
        for(int i=0;i<10000;i++){
            b.add(i%100);
            ref.add(i%100);
        }
        b.add(5000,77);
        ref.add(5000,77);
        b.remove(5000);
        ref.remove(5000);
        for(int i=0;i<ref.size();i++)check(b.get(i)==ref.get(i));
    }

    private static void testMinHeap(){
        MinHeap h=new MinHeap();
        check(h.size()==0);
        expectEmpty(()->h.peekMin());
        expectEmpty(()->h.extractMin());
        int[] values={5,3,3,8,1,9,2,7};
        java.util.PriorityQueue<Integer> ref=new java.util.PriorityQueue<>();
        for(int x:values){
            h.insert(x);
            ref.add(x);
            check(h.isValid());
            check(h.peekMin()==ref.peek());
        }
        int prev=Integer.MIN_VALUE;
        while(h.size()>0){
            int x=h.extractMin();
            check(x>=prev);
            check(h.isValid());
            check(x==ref.poll());
            prev=x;
        }
        check(ref.isEmpty());
    }

    private static void check(boolean value){
        if(!value)throw new AssertionError();
    }

    private static void expectIndex(Runnable r){
        try{
            r.run();
            throw new AssertionError();
        }catch(IndexOutOfBoundsException e){}
    }

    private static void expectEmpty(Runnable r){
        try{
            r.run();
            throw new AssertionError();
        }catch(java.util.NoSuchElementException e){}
    }
}
