package spicy.utils;

import java.util.*;

public class FlexibleArray<T> implements Iterable<T>
{
    private T[] elements;
    
    public FlexibleArray(final T[] array) {
        this.elements = array;
    }
    
    public FlexibleArray() {
        this.elements = (T[])new Object[0];
    }
    
    public void add(final T t) {
        if (t != null) {
            final T[] array = (T[])new Object[this.size() + 1];
            for (int i = 0; i < array.length; ++i) {
                if (i < this.size()) {
                    array[i] = this.get(i);
                }
                else {
                    array[i] = t;
                }
            }
            this.set(array);
        }
    }
    
    public void remove(final T t) {
        if (this.contains(t)) {
            final T[] array = (T[])new Object[this.size() - 1];
            boolean b = true;
            for (int i = 0; i < this.size(); ++i) {
                if (b && this.get(i).equals(t)) {
                    b = false;
                }
                else {
                    array[b ? i : (i - 1)] = this.get(i);
                }
            }
            this.set(array);
        }
    }
    
    public boolean contains(final T t) {
        for (final T entry : this.array()) {
            if (entry.equals(t)) {
                return true;
            }
        }
        return false;
    }
    
    private void set(final T[] array) {
        this.elements = array;
    }
    
    public void clear() {
        this.elements = (T[])new Object[0];
    }
    
    public T get(final int index) {
        return this.array()[index];
    }
    
    public int size() {
        return this.array().length;
    }
    
    public T[] array() {
        return this.elements;
    }
    
    public void setElements(final T[] elements) {
        this.elements = elements;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < FlexibleArray.this.size() && FlexibleArray.this.get(this.index) != null;
            }
            
            @Override
            public T next() {
                return FlexibleArray.this.get(this.index++);
            }
            
            @Override
            public void remove() {
                FlexibleArray.this.remove(FlexibleArray.this.get(this.index));
            }
        };
    }
}
