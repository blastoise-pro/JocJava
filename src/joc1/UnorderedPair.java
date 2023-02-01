package joc1;

import java.util.Objects;

public final class UnorderedPair<T> {
    final T first, second;

    public UnorderedPair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnorderedPair<?>)) return false;
        UnorderedPair<?> that = (UnorderedPair<?>) o;
        return (Objects.equals(first, that.first) && Objects.equals(second, that.second)) ||
                (Objects.equals(first, that.second) && Objects.equals(second, that.first));
    }

    @Override
    public int hashCode() {
        int hashFirst = first.hashCode();
        int hashSecond = second.hashCode();
        return hashFirst + hashSecond; // Hash simètric de forma que hash(x, y) = hash(y, x)
    }
}
