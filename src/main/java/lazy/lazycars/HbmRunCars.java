package lazy.lazycars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HbmRunCars {
    public static void main(String[] args) {
        List<Brand> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Brand brand = Brand.of("Hunday");
            Model model1 = Model.of("Solaris", brand);
            Model model2 = Model.of("Sonata", brand);
            Model model3 = Model.of("Creta", brand);
            session.save(brand);
            session.save(model1);
            session.save(model2);
            session.save(model3);
            session.getTransaction().commit();
            session.close();

            Session session2 = sf.openSession();
            session2.beginTransaction();
            list = session2.createQuery("select distinct b from Brand "
                    + "b join fetch b.models").list();
            session2.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Brand brand : list) {
            for (Model model : brand.getModels()) {
                System.out.println(model);
            }
        }
    }
}
