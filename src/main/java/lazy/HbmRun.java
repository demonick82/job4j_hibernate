package lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Category> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Category category1 = Category.of("category 1");

            Task task1 = Task.of("Task1", category1);
            Task task2 = Task.of("Task2", category1);
            Task task3 = Task.of("Task3", category1);
            session.save(category1);
            session.save(task1);
            session.save(task2);
            session.save(task3);

            list = session.createQuery("select distinct c from Category "
                    + "c join fetch c.tasks").list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Category category : list) {
            for (Task task : category.getTasks()) {
                System.out.println(task);
            }
        }
    }
}
