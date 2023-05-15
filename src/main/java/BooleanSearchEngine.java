import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    //???
    private Map<String, List<PageEntry>> wordsStatistic = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        //в конструкторе для каждого слова нужно сохранить готовый на возможный
        // будущий запрос ответ в виде List<PageEntry>
        // (для этого можно использовать мапу, где ключом будет слово,
        // а значением - искомый список).
        Map<String, List<PageEntry>> wordsStat = new HashMap<>();
        //Пройдемся циклом по всем файлам из каталога pdfsDir
        for (File pdfFile : pdfsDir.listFiles()
        ) {
            var doc = new PdfDocument(new PdfReader(pdfFile));//файл
            String pdfName = pdfFile.getName(); //имя файла
            int countDocPages = doc.getNumberOfPages(); //кол-во страниц в файле
            //Пройдемся циклом по всем страницам файла
            for (int i = 1; i <= countDocPages; i++) {   //Страница
                PdfPage pdfPage = doc.getPage(i);
                //получаем текст со страницы
                var text = PdfTextExtractor.getTextFromPage(pdfPage);
                // разбиваем текст на слова
                var words = text.split("\\P{IsAlphabetic}+");

                //подсчитываем частоту слов на одной странице
                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    // После подсчёта, для каждого уникального слова пдф-файла
                    // создаёте объект PageEntry и сохраняете в мапу в поле

                } //посчитали слова на очередной странице файла
                for (String key : freqs.keySet()
                ) {
                    PageEntry pageEntry = new PageEntry(pdfName, i, freqs.get(key));
                    if (wordsStat.get(key) != null) {
                        wordsStat.get(key).add(pageEntry);
                    } else {
                        List<PageEntry> pageEntries = new ArrayList<>();
                        pageEntries.add(pageEntry);
                        wordsStat.put(key, pageEntries);
                    }
                }

            }
        }
        //Отсортируем для каждого слова списки ответов в порядке уменьшения поля count
        for (String key : wordsStat.keySet()
        ) {
            Collections.sort(wordsStat.get(key), new CountComparator());
        }
        this.wordsStatistic = wordsStat;
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        word = word.toLowerCase();
        if (this.wordsStatistic.containsKey(word)) {
            return this.wordsStatistic.get(word);
        } else {
            List<PageEntry> list = new ArrayList<>();
            return list;
        }

    }
}