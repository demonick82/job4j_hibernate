package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class HBmRun implements AutoCloseable {
    final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    public void add(Candidate candidate) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(candidate);
        session.getTransaction().commit();
        session.close();
    }

    public List<Candidate> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Candidate> result = session.createQuery("from Candidate ").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Candidate findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Candidate c where c.id=:id")
                .setParameter("id", id);
        Candidate result = (Candidate) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Candidate findByName(String name) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Candidate c where c.name=:sname")
                .setParameter("sname", name);
        Candidate result = (Candidate) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public void updateCandidate(int id, Candidate candidate) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery(
                "update Candidate s set s.name = :newName, s.experience = :newExperience, s.salary=:newSalary"
                        + " where s.id = :fId"
        ).setParameter("newName", candidate.getName())
                .setParameter("newExperience", candidate.getExperience())
                .setParameter("newSalary", candidate.getSalary())
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void deleteCandidate(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Candidate where id = :fId")
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public static void main(String[] args) {
        HBmRun run = new HBmRun();
        Candidate candidate1 = Candidate.of("Дмитрий", "junior", 70000);
        Candidate candidate2 = Candidate.of("Сергей", "middle", 200000);
        Candidate candidate3 = Candidate.of("Антон", "senior", 350000);
        Candidate candidate4 = Candidate.of("Павел", "junior", 80000);
        run.add(candidate1);
        run.add(candidate2);
        run.add(candidate3);
        run.findAll().forEach(System.out::println);
        System.out.println(run.findById(1));
        System.out.println(run.findByName("Антон"));
        run.deleteCandidate(3);
        run.updateCandidate(2, candidate4);
    }
}
