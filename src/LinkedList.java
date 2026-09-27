public class LinkedList{
    private static class Node{
        int value;
        Node next;
        Node prev;
        Node(int value){
            this.value=value;
        }
    }
    private Node head;
    private Node tail;
    private int size;
    private long accesses;
    private long comparisons;

    public void add(int x){
        Node node=new Node(x);
        if(size==0)head=tail=node;
        else{
            tail.next=node;
            node.prev=tail;
            tail=node;
        }
        size++;
    }
    public void add(int index,int x){
        checkAddIndex(index);
        if(index==size){
            add(x);
            return;
        }
        Node current=nodeAt(index);
        Node node=new Node(x);
        node.next=current;
        node.prev=current.prev;
        if(current.prev==null)head=node;
        else current.prev.next=node;
        current.prev=node;
        size++;
    }
    public int remove(int index){
        checkIndex(index);
        Node current=nodeAt(index);
        if(current.prev==null)head=current.next;
        else current.prev.next=current.next;
        if(current.next==null)tail=current.prev;
        else current.next.prev=current.prev;
        size--;
        return current.value;
    }
    public int get(int index){
        return nodeAt(index).value;
    }
    public boolean contains(int x){
        Node current=head;
        while(current!=null){
            comparisons++;
            if(current.value==x)return true;
            current=current.next;
            if(current!=null)accesses++;
        }
        return false;
    }
    public int size(){
        return size;
    }

    public void resetMetrics(){
        accesses=0;
        comparisons=0;
    }
    public long getAccesses(){
        return accesses;
    }
    public long getComparisons(){
        return comparisons;
    }
    public long getMovements(){
        return 0;
    }
    private Node nodeAt(int index){
        checkIndex(index);
        if(index<size/2){
            Node current=head;
            for(int i=0;i<index;i++){
                current=current.next;
                accesses++;
            }
            return current;
        }
        Node current=tail;
        for(int i=size-1;i>index;i--){
            current=current.prev;
            accesses++;
        }
        return current;
    }
    private void checkIndex(int index){
        if(index<0||index>=size)throw new IndexOutOfBoundsException();
    }
    private void checkAddIndex(int index){
        if(index<0||index>size)throw new IndexOutOfBoundsException();
    }
}
