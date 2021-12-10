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

    public void addCandidate(Candidate candidate) {
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
        Query query = session.createQuery("select distinct cn from Candidate cn"
                + " join fetch cn.base b "
                + "join fetch b.vacancies "
                + "where cn.id=:id ", Candidate.class)
                .setParameter("id", id);
        Candidate result = (Candidate) query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Candidate> findByName(String name) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Candidate c where c.name=:sname")
                .setParameter("sname", name);
        List result = query.list();
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
        Vacancy vacancy1 = Vacancy.of("Junior Java Developer");
        Vacancy vacancy2 = Vacancy.of("Middle Java Developer");
        Vacancy vacancy3 = Vacancy.of("Senior Java Developer");
        Base base1 = Base.of("it Candidates");
        base1.addVacancy(vacancy1);
        base1.addVacancy(vacancy2);
        base1.addVacancy(vacancy3);
        Candidate candidate1 = Candidate.of("Дмитрий", "junior", 70000, base1);
        System.out.println(candidate1);
        Candidate candidate2 = Candidate.of("Сергей", "middle", 200000, base1);
        Candidate candidate3 = Candidate.of("Антон", "senior", 350000, base1);
        Candidate candidate5 = Candidate.of("Дмитрий", "senior", 380000, base1);
        run.addCandidate(candidate1);
        run.addCandidate(candidate2);
        run.addCandidate(candidate3);
        run.addCandidate(candidate5);
        System.out.println(run.findById(1));

    }
}
