package prv_kolokvium.ex18_FrontPage;

import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String category) {
        super("Category "+category+" was not found");
    }
}

abstract class NewsItem{
    private String title;
    private Date date;
    private Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    abstract String getTeaser();
}

class Category{
    String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryName);
    }
}

class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser(){
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle() + "\n");
        sb.append(((new Date().getTime() - getDate().getTime()) / (1000 * 60)) + "\n");
        sb.append(text.substring(0, Math.min(text.length(), 80)));
        return sb.toString();
    }
}

class MediaNewsItem extends NewsItem{
    private String url;
    private int numViews;

    public MediaNewsItem(String title, Date date, Category category, String url, int numViews) {
        super(title, date, category);
        this.url = url;
        this.numViews = numViews;
    }

    @Override
    public String getTeaser(){
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle() + "\n");
        sb.append(((new Date().getTime() - getDate().getTime()) / (1000 * 60)) + "\n");
        sb.append(url + "\n");
        sb.append(numViews);
        return sb.toString();
    }
}

class FrontPage{
    private ArrayList<NewsItem> newsItems;
    private Category[] categories;

    public FrontPage(Category[] categories){
        this.categories = categories;
        newsItems = new ArrayList<NewsItem>();
    }

    public void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category){
        return newsItems.stream()
                .filter(n -> n.getCategory().categoryName.equals(category.categoryName))
                .collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        Category found = null;

        for (Category c : categories) {
            if (c.categoryName.equals(category)) {
                found = c;
                break;
            }
        }

        if (found == null) {
            throw new CategoryNotFoundException(category);
        }

        return newsItems.stream()
                .filter(n -> n.getCategory().categoryName.equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem n : newsItems){
            sb.append(n.getTeaser() + "\n");
        }
        return sb.toString();
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde
