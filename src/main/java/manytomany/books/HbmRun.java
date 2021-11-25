package manytomany.books;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()

                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Author one = Author.of("Author 1");
            Author two = Author.of("Author 2");
            Author three = Author.of("Author 3");

            Book bookOne = Book.of("Book 1");
            Book bookTwo = Book.of("Book 2");

            bookOne.getAuthors().add(one);
            bookOne.getAuthors().add(two);
            bookTwo.getAuthors().add(three);

            session.persist(bookOne);
            session.persist(bookTwo);

            session.remove(bookTwo);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

