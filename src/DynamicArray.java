public class DynamicArray{
    private int[] data;
    private int size;
    private long accesses;
    private long comparisons;
    private long movements;
    public DynamicArray(){
        data=new int[10];
    }

    public void add(int x){
        ensureCapacity();
        data[size++]=x;
    }
    public void add(int index,int x){
        checkAddIndex(index);
        ensureCapacity();
        for(int i=size;i>index;i--){
            data[i]=data[i-1];
            movements++;
        }
        data[index]=x;
        size++;
    }
    public int remove(int index){
        checkIndex(index);
        int value=data[index];
        for(int i=index;i<size-1;i++){
            data[i]=data[i+1];
            movements++;
        }
        data[--size]=0;
        return value;
    }
    public int get(int index){
        checkIndex(index);
        accesses++;
        return data[index];
    }
    public boolean contains(int x){
        for(int i=0;i<size;i++){
            comparisons++;
            if(data[i]==x)return true;
        }
        return false;
    }
    public int size(){
        return size;
    }

    public void resetMetrics(){
        accesses=0;
        comparisons=0;
        movements=0;
    }
    public long getAccesses(){
        return accesses;
    }
    public long getComparisons(){
        return comparisons;
    }
    public long getMovements(){
        return movements;
    }
    private void ensureCapacity(){
        if(size==data.length){
            int[] next=new int[data.length*2];
            for(int i=0;i<size;i++){
                next[i]=data[i];
                movements++;
            }
            data=next;
        }
    }
    private void checkIndex(int index){
        if(index<0||index>=size)throw new IndexOutOfBoundsException();
    }
    private void checkAddIndex(int index){
        if(index<0||index>size)throw new IndexOutOfBoundsException();
    }
}
