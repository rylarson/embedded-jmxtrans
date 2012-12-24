/*
 * Copyright 2008-2012 Xebia and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmxexporter.util.concurrent;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Automatically discard the oldest element if the queue is full.
 *
 * @author <a href="mailto:cleclerc@xebia.fr">Cyrille Le Clerc</a>
 */
public class DiscardingBlockingQueue<E> extends ArrayBlockingQueue<E> {
    public DiscardingBlockingQueue(int capacity) {
        super(capacity);
    }

    public DiscardingBlockingQueue(int capacity, boolean fair) {
        super(capacity, fair);
    }

    public DiscardingBlockingQueue(int capacity, boolean fair, Collection<? extends E> c) {
        super(capacity, fair, c);
    }

    protected boolean discardingOffer(E e) {
        boolean added;
        while (added = super.offer(e) != true) {
            poll();
        }
        return added;
    }

    @Override
    public boolean add(E e) {
        return discardingOffer(e);
    }

    @Override
    public boolean offer(E e) {
        return discardingOffer(e);
    }

    @Override
    public void put(E e) throws InterruptedException {
        discardingOffer(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return discardingOffer(e);

    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            discardingOffer(e);
        }
        return !c.isEmpty();
    }
}