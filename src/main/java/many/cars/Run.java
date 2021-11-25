package many.cars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Run {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model solaris = Model.of("Solaris");
            Model elantra = Model.of("Elantra");
            Model sonata = Model.of("Sonata");
            Model tuscon = Model.of("Tuscon");
            Model creta = Model.of("Creta");

            session.save(solaris);
            session.save(elantra);
            session.save(sonata);
            session.save(tuscon);
            session.save(creta);
            Brand hundai = Brand.of("Hundai");

            for (int i = 1; i <= 5; i++) {
                hundai.addModel(session.load(Model.class, i));
            }

            session.save(hundai);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
