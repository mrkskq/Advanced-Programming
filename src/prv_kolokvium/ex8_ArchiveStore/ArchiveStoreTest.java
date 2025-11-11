

// ne e doreshena :)


package prv_kolokvium.ex8_ArchiveStore;

import java.util.*;
import java.util.Scanner;

// вашиот код овде
class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super("Item with id "+id+" doesn't exist");
    }
}


class Archive {
    private int id;
    private Date dateArchived;

    public Archive(int id, Date dateArchived) {
        this.id = id;
        this.dateArchived = dateArchived;
    }

    public Archive(int id) {
        this.id = id;
    }

    public Date getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(Date dateArchived) {
        this.dateArchived = dateArchived;
    }

    public int getId() {
        return id;
    }
}


class ArchiveStore{
    private List<Archive> archives;
    private StringBuilder sb;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.sb = new StringBuilder();
    }

    public void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        archives.add(item);
        //За секоја акција на архивирање во текст треба да се додаде следната порака Item [id] archived at [date]
        sb.append("Item "+item.getId()+" archived at "+date+"\n");
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        Archive a = findById(id);
        if (a == null) {
            throw new NonExistingItemException(id);
        }

        //При отварање ако се работи за LockedArhive и датумот на отварање е пред датумот кога може да се отвори,
        //да се додаде порака Item [id] cannot be opened before [date]
        if (a instanceof LockedArchive la){
            if (date.before(la.getDateToOpen())){
                sb.append("Item "+la.getId()+" cannot be opened before "+la.getDateToOpen()+"\n");
            }
            else{
                sb.append("Item "+la.getId()+" opened at "+date+"\n");
            }
        }

        //Ако се работи за SpecialArhive и се обидиеме да ја отвориме повеќе пати од дозволениот број (maxOpen)
        //да се додаде порака Item [id] cannot be opened more than [maxOpen] times.
        else if (a instanceof SpecialArchive sa){
            sa.incrementOpenedCount();
            if (sa.getOpenedCount() >= sa.getMaxOpen()){
                sb.append("Item "+sa.getId()+" cannot be opened more than "+sa.getMaxOpen()+" times\n");
            }
            else {
                sb.append("Item "+sa.getId()+" opened at "+date+"\n");
            }
        }

        else {
            sb.append("Item "+id+" opened at "+date+"\n");
        }
    }

    private Archive findById(int id) {
        for (Archive a : archives) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public String getLog(){
        return sb.toString();
    }
}


class LockedArchive extends Archive {
    private Date dateToOpen;

    LockedArchive(int id, Date dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public Date getDateToOpen() {
        return dateToOpen;
    }
}


class SpecialArchive extends Archive {
    private int maxOpen;
    private int openedCount;

    public SpecialArchive(int id, int maxOpen){
        super(id);
        this.maxOpen = maxOpen;
        this.openedCount = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public int getOpenedCount() {
        return openedCount;
    }

    public void incrementOpenedCount() {
        openedCount++;
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}


