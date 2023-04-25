import java.util.Comparator;

public class CountComparator implements Comparator<PageEntry> {
    @Override
    public int compare(PageEntry o1, PageEntry o2) {
        return Integer.compare(o1.count, o2.count);

    }
}