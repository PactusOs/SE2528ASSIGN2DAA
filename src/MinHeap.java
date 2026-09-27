public class MinHeap{
    private int[] data=new int[10];
    private int size;
    private long comparisons;

    public void insert(int x){
        ensureCapacity();
        int i=size++;
        data[i]=x;
        while(i>0){
            int parent=(i-1)/2;
            comparisons++;
            if(data[parent]<=data[i])break;
            swap(parent,i);
            i=parent;
        }
    }

    public int peekMin(){
        if(size==0)throw new java.util.NoSuchElementException();
        return data[0];
    }

    public int extractMin(){
        if(size==0)throw new java.util.NoSuchElementException();
        int min=data[0];
        data[0]=data[--size];
        if(size>0)siftDown(0);
        return min;
    }

    public int size(){
        return size;
    }

    public long getComparisons(){
        return comparisons;
    }

    public void resetMetrics(){
        comparisons=0;
    }

    public boolean isValid(){
        for(int i=0;i<size;i++){
            int left=2*i+1;
            int right=left+1;
            if(left<size&&data[i]>data[left])return false;
            if(right<size&&data[i]>data[right])return false;
        }
        return true;
    }

    private void siftDown(int i){
        while(true){
            int left=2*i+1;
            if(left>=size)break;
            int right=left+1;
            int child=left;
            if(right<size){
                comparisons++;
                if(data[right]<data[left])child=right;
            }
            comparisons++;
            if(data[i]<=data[child])break;
            swap(i,child);
            i=child;
        }
    }

    private void ensureCapacity(){
        if(size==data.length){
            int[] next=new int[data.length*2];
            System.arraycopy(data,0,next,0,size);
            data=next;
        }
    }

    private void swap(int i,int j){
        int t=data[i];
        data[i]=data[j];
        data[j]=t;
    }
}
