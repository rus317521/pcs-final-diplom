import java.util.Comparator;

public class CountComparator implements Comparator<PageEntry> {
    @Override
    public int compare(PageEntry o1, PageEntry o2) {
        return Integer.compare(o2.count, o1.count);

    }
}