public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName; //имя pdf файла
    private final int page;       //номер страницы
    final int count;      //кол-во раз, которое встретилось это слово в ней

    // ???
    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int compareTo(PageEntry o) {
        if (count < o.count) {
            return -1;
        } else if (count > o.count) {
            return 1;
        } else return 0;

    }

}
