package com.forsrc.utils;


import java.util.*;

public class CollectionUtils {

    public static <E> void foreach(Collection<E> collection, ItemHandler<E> itemHandler) {
        if (collection instanceof RandomAccess && collection instanceof List) {
            int length = collection.size();
            for (int i = 0; i < length; i++) {
                if(itemHandler.handle(((List<E>) ((List) collection)).get(i))){
                    continue;
                }
            }
            return;
        }

        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if(itemHandler.handle(iterator.next())){
                continue;
            }
        }

    }


    public static interface ItemHandler<E> {
        public boolean handle(E e);
    }

    public static void main(String[] args){
        List<Integer> list = new LinkedList<Integer>();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(7);

        CollectionUtils.foreach(list, new ItemHandler<Integer>() {
            @Override
            public boolean handle(Integer integer) {
                System.out.println(integer);
                return true;
            }
        });

    }
}
